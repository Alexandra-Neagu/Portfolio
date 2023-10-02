import numpy as np


class MyEGreedy:

    def __init__(self):
        print("Made EGreedy")

    def get_random_action(self, agent, maze):
        # TODO to select an action at random in State s
        actions = maze.get_valid_actions(agent)
        p_a = np.empty(len(actions))
        p_a[:] = 1 / len(actions)
        selected_action = np.random.choice(actions, 1, p=p_a)
        return selected_action[0]

    def get_best_action(self, agent, maze, q_learning):
        # TODO to select the best possible action currently known in State s.
        actions = maze.get_valid_actions(agent)
        state = agent.get_state(maze)
        q_values = q_learning.get_action_values(state, actions)
        selected_action = actions[np.argmax(q_values)]
        return selected_action

    def get_egreedy_action(self, agent, maze, q_learning, epsilon):
        # TODO to select between random or best action selection based on epsilon.
        selections = np.array([0, 1])  # 0 - uniform, 1 - greedy
        p = np.array([epsilon, 1 - epsilon])
        choice = np.random.choice(selections, 1, p=p)
        if choice == 0:
            return self.get_random_action(agent, maze)
        else:
            return self.get_best_action(agent, maze, q_learning)

