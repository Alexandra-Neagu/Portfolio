from QLearning import QLearning


class MyQLearning(QLearning):

    def update_q(self, state, action, r, state_next, possible_actions, alfa, gamma):
        # TODO Auto-generated method stub

        # get the current value of q
        curr_q = self.get_q(state, action)

        # find the best action from all the possible ones that can be taken
        max_action = max(self.get_action_values(state_next, possible_actions))

        # calculate the new (updated) value of q using the formula
        # Q(s, a) = Q(s, a) + alfa * (r + gamma * max(Q(s', a') - Q(s, a))
        curr_q = curr_q + alfa * (r + gamma * max_action - curr_q)

        # update the new value of q in the dictionary
        self.set_q( state, action, curr_q)

        return
