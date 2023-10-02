import csv 
import numpy as np
import random

import logging  # For terminal-based debug logging
logging.basicConfig(level=logging.DEBUG)

# Thresholds for a 'willing' and 'competent' agent
# I decided on a MIN and MAX for each, so we can have a 
# bit more granularity when creating conditional actions.

# E.g. A robot may only want to ignore removing a tree if the 
# willingness of the human is positive. (> -0.2, to accomodate for fluctuation)

STARTING_CONFIDENCE = 0.1
IDLE_TIMEOUT = 400          # Timeout in game ticks.
IDLE_TIMEOUT_COMPETENT = 200

MIN_WILLINGNESS = -0.2
MAX_WILLINGNESS = 0.2

MIN_COMPETENCE = -0.2
MAX_COMPETENCE = 0.2

trust_evaluation = {
    'ALWAYS-TRUST': {
        'competence': 1,
        'willingness': 1
    },
    'NEVER-TRUST':  {
        'competence': -1,
        'willingness': -1
    },
    'RANDOM-TRUST': {
        'competence': random.uniform(-1, 1),
        'willingness': random.uniform(-1, 1)
    }
}

class Trust:
    '''
    Models the trust system of a provided human agent.
    # NOTE: No longer 2D dictionary as we are only concerned
    # with one agent. 2D functionality was also not used in OfficialAgent.
    '''
    def __init__(self, human, folder,eval_type=None):
        self.human = human
        self.folder = folder + '/beliefs/'
        # for evaluation: if in evaluation mode -> competence and willingness values are not updated at all
        self.eval_type = eval_type
        if eval_type is None:
            self.belief = self.load_belief(human)
        else:
            self.belief = trust_evaluation.get(eval_type) 
        # Bounded between[0, 1]
        self.confidence = STARTING_CONFIDENCE

        # Attributes to keep track of
        self.read_messages = []
        self.idle_timeout = IDLE_TIMEOUT
       
        
        
    '''
    Specifying a bunch of frequently-used trust functions
    As well as their thresholds.
    '''

    def willing(self, msg=None):
        if msg: logging.debug(f'trusting your willingness ({self.belief["willingness"]}) for {msg}')
        return self.confidence * self.belief['willingness'] > MIN_WILLINGNESS

    def competent(self, msg=None):
        if msg: logging.debug(f'trusting your competence ({self.belief["willingness"]}) for {msg}')
        return self.confidence * self.belief['competence'] > MIN_COMPETENCE

    def unwilling(self, msg=None):
        if msg: logging.debug(f'ignoring your willingness ({self.belief["willingness"]}) for {msg}')
        return self.confidence * self.belief['willingness'] < MAX_WILLINGNESS

    def incompetent(self, msg=None):
        if msg: logging.debug(f'ignoring your competence ({self.belief["willingness"]}) for {msg}')
        return self.confidence * self.belief['competence'] < MAX_COMPETENCE

    def _increase_confidence(self, amount=0.3):
        '''
        Should only be called within this class.
        For now, I just said any time trust is updated, confidence improves.
        Increases confidence by optionally specified percentage.
        '''
        self.confidence *= (1 + amount)
        self.confidence = np.clip(self.confidence, 0.1, 1)
        logging.debug(f'increased confidence to {self.confidence:.2f}')

    def _decrease_confidence(self, amount=0.3):
        '''
        Should only be called within this class.
        Decreases confidence by optionally specified percentage.
        '''
        self.confidence *= (1 - amount)
        self.confidence = np.clip(self.confidence, 0.1, 1)
        logging.debug(f'decreased confidence to {self.confidence:.2f}')
        

    def increase_competence(self, msg='', amount=0.1):
        if self.eval_type is None:
            self.belief['competence'] += amount
            self.belief['competence'] = np.clip(self.belief['competence'], -1, 1)
            logging.debug(f'increased competence to {self.belief["competence"]:.2f} because {msg}')

            self._increase_confidence()
            self.save_belief()


    def decrease_competence(self, msg='', amount=0.1):
        if self.eval_type is None:
            self.belief['competence'] -= amount
            self.belief['competence'] = np.clip(self.belief['competence'], -1, 1)
            logging.debug(f'decreased competence to {self.belief["competence"]:.2f} because {msg}')

            self._increase_confidence()
            self.save_belief()


    def increase_willingness(self, msg='', amount=0.1):
        if self.eval_type is None:
            self.belief['willingness'] += amount
            self.belief['willingness'] = np.clip(self.belief['willingness'], -1, 1)
            logging.debug(f'increased willingness to {self.belief["willingness"]:.2f} because {msg}')

            self._increase_confidence()
            self.save_belief()


    def decrease_willingness(self, msg='', amount=0.1):
        if self.eval_type is None:
            self.belief['willingness'] -= amount
            self.belief['willingness'] = np.clip(self.belief['willingness'], -1, 1)
            logging.debug(f'decreased willingness to {self.belief["willingness"]:.2f} because {msg}')

            self._increase_confidence()
            self.save_belief()


    def update_belief(self, **kwargs):

        if self.eval_type is None and 'messages' in kwargs:
            self._update_from_messages(kwargs['messages'])


    def _update_from_messages(self, messages):
        '''
        Adapted from baseline example.
        Increases the human's competence if the message contains 'collect',
        given that the human has high enough willingness. Otherwise, it can
        be assumed that the human is lying.
        '''
        # Update the trust value based on for example the received messages
        for message in messages:      
            # Update competence if agent is believably willing to collect 
            if message not in self.read_messages:
                logging.debug(f'Received message: {message}')
                    
                # Increase agent trust in a team member that rescued a victim
                if 'Collect' in message and self.willing():
                    self.increase_competence(msg= \
                    f'{self.human} has sufficient willingness and is collecting a victim.')
                    self.increase_willingness(msg= \
                    f'{self.human} is willing to collect a victim',amount=0.05)
                
                # If human searches room x, increase the agent's willingness belief
                elif 'Search' in message:
                    # if the same room is searched before by the human, then he is not capable of
                    # remembering which rooms are searched -> decrease competence
                    #if 'Search' in message and message in self.read_messages:
                    for msg in self.read_messages:
                        if (message.split()[0]+' ' + message.split()[1]) in msg:
                            self.decrease_competence(msg= \
                            f'{self.human} has searched the same room more than once.')
                            break

                    # if the previous message was also 'search' but another room,
                    # human may be lying about the previous room he searched
                    # But he may not be lying if the previous room had no obstacles+injured victims,
                    # thus, decrease confidence by a small value
                    if self.read_messages and 'Search' in self.read_messages[-1] and message.split()[1]!=self.read_messages[-1].split()[1]:
                            logging.debug(f'Agent {self.human} might be lying')
                            self._decrease_confidence(0.15)

                            
                    # increase willingness by a small amount since the human is willing to play the game
                    # but we don't know if it is a lie or not
                    else:
                        self.increase_willingness(msg= \
                        f'{self.human} is willing to search an area',amount=0.05)
                
                # includes all messages that start with 'rescue together', 'rescue alone' etc.
                elif 'Rescue' in message and 'Rescue':
                    self.increase_willingness(msg= \
                        f'{self.human} is willing to rescue a victim',amount=0.05)
                
                # when the human asks for help for removing an obstacle
                elif 'Remove: at' in message:
                    self.increase_willingness(msg= \
                        f'{self.human} is willing to remove an obstacle',amount=0.05)
                
                elif 'Found' in message and self.willing():
                    self.increase_willingness(msg= \
                    f'{self.human} found a victim',amount=0.05)
                    # If the human sent two found messages on top of each other, decrease confidence
                    # human might be lying but he might also find two victims in a single room
                    if self.read_messages and 'Found' in self.read_messages[-1]:
                        self._decrease_confidence(0.15)
                
                self.read_messages.append(message)
            

                 
    # If 
    def reset_timeout(self):
        if self.competent():
            self.idle_timeout = IDLE_TIMEOUT_COMPETENT
        else: 
            self.idle_timeout = IDLE_TIMEOUT
    
    def waiting(self, agent, action, msg='while idle'):
        '''
        Method that keeps track of how long the agent has been waiting
        for the human to respond. Lowers willingness after TIMEOUT
        params:
          agent:            passed reference so we can change its state
          action:           to default to after timeout, None if there is none.
          msg (optional):   why the agent is waiting
        '''
        # If waiting time is up or,
        # the human is not willing and there is a default action

        if not self.willing():

            self.reset_timeout()
            logging.debug(f'{self.human} is likely not willing ({self.belief["willingness"]:.2f}) enough to reply, skipping wait')
            return False

        if self.idle_timeout == 0:

            self.reset_timeout()
            logging.debug(f'{self.human} ran out of time.')
            self.decrease_willingness(f'no {msg}')
            return False

        # Otherwise, debug a periodic message.
        elif self.idle_timeout % 100 == 0: 
            logging.debug(f'waiting {msg} ({self.idle_timeout} ticks)')

        self.idle_timeout -= 1
        return True # ROBOT IS WAITING


    def load_belief(self, human_name):
        '''
        Updates allTrustBeliefs with currentTrustBelief from last run.
        Returns a dictionary with cumulative trust values for the given human.
        TODO: fix: This method fails if currentTrustBelief is empty, 
                    or if the human in the last round was named 'name'.
        '''
        # Create a dictionary with trust values for all team members
        belief = {}
        # Set a default starting trust value
        default = 0.0
        trustfile_header = []
        trustfile_contents = []

        # add the trust values from the previous run, and update allTrustBeliefs.csv
        # with open(self.folder + 'currentTrustBelief.csv') as csvfile:
        #     reader = csv.reader(csvfile, delimiter=';', quotechar="'")
        #     for row in reader:
        #         if row and row[0] == 'name':
        #             continue
        #         else:
        #             trustfile_contents.append(row)
        competence = default
        willingness = default
        belief[human_name] = {'competence': competence, 'willingness': willingness}
        with open(self.folder + 'allTrustBeliefs.csv') as csvfile:
            reader = csv.reader(csvfile, delimiter=';', quotechar="'")
            for row in reader:
                if trustfile_header==[]:
                    trustfile_header=row
                    continue
                if row and row[0] == human_name:
                    name = row[0]
                    competence = float(row[1])
                    willingness = float(row[2])
                    belief[name] = {'competence': competence, 'willingness': willingness}
        
        # The provided code elsewhere automatically writes the trust value at the end of the game
        # for row in trustfile_contents:
        #     # Retrieve trust values if the human exists, else they will be the default values.
        #     if row and row[0] == human_name:
        #         name = row[0]
        #         competence = float(row[1])
        #         willingness = float(row[2])
        #         belief[name] = {'competence': competence, 'willingness': willingness}

        # with open(self.folder + 'allTrustBeliefs.csv', mode='w') as csv_file:
        #     csv_writer = csv.writer(csv_file, delimiter=';', quotechar='"', quoting=csv.QUOTE_MINIMAL)
        #     csv_writer.writerow(['name','competence','willingness'])
        #     for row in trustfile_contents:
        #         csv_writer.writerow(row)

        return belief[human_name]

        """ old provided version.
        # Check if agent already collaborated with this human before, if yes: load the corresponding trust values, if no: initialize using default trust values
        with open(folder+'/beliefs/allTrustBeliefs.csv') as csvfile:
            reader = csv.reader(csvfile, delimiter=';', quotechar="'")
            for row in reader:
                if trustfile_header==[]:
                    trustfile_header=row
                    continue
                # Retrieve trust values
                if row and row[0]==self._humanName:
                    name = row[0]
                    competence = float(row[1])
                    willingness = float(row[2])
                    trustBeliefs[name] = {'competence': competence, 'willingness': willingness}
                # Initialize default trust values
                if row and row[0]!=self._humanName:
                    competence = default
                    willingness = default
                    trustBeliefs[self._humanName] = {'competence': competence, 'willingness': willingness}

        """

    def save_belief(self):
        '''
        Saves the current state of the trust belief system to currentTrustBelief
        '''
        with open(self.folder + 'currentTrustBelief.csv', mode='w') as csv_file:
            csv_writer = csv.writer(csv_file, delimiter=';', quotechar='"', quoting=csv.QUOTE_MINIMAL)
            csv_writer.writerow(['name','competence','willingness'])
            csv_writer.writerow([self.human,self.belief['competence'],self.belief['willingness']])

        return self.belief 