from Maze import Maze
from Agent import Agent
from mysolution.MyQLearning import MyQLearning
from mysolution.MyEGreedy import MyEGreedy

if __name__ == "__main__":
    # load the maze
    # TODO replace this with the location to your maze on your file system
    file = "..\\..\\data\\toy_maze.txt"
    maze = Maze(file)

    # Set the reward at the bottom right to 10
    maze.set_reward(maze.get_state(9, 9), 10)

    # create a robot at starting and reset location (0,0) (top left)
    robot = Agent(0, 0)

    # make a selection object (you need to implement the methods in this class)
    selection = MyEGreedy()

    # make a Qlearning object (you need to implement the methods in this class)
    learn = MyQLearning()

    stop = 50
    i = 0
    epsilon = 1
    alpha = 0.7
    gamma = 0.9
    action_counts = []
    # state = maze.get_state(0,0)

    # Trial averages for the toy_maze and easy_maze for the plotting
    # avgs_toy_maze = [1099.12, 1216.04, 1729.16, 1373.6, 2539.36, 1725.36, 2445.28, 1312.48, 1607, 3565.04]
    # avgs_easy_maze = [1573.08, 1588.36, 2001.36, 1985.4, 1807.48, 2001.6, 1718.28, 2303.36, 2473.84, 4020.64]

    # keep learning until you decide to stop
    # while state is not terminal:???
    while i != stop:
        state = robot.get_state(maze)
        # to choose action with e-greedy + take it: analyze R , get Q'-of best next action, update Q
        action = selection.get_egreedy_action(robot, maze, learn, epsilon)
        new_state = robot.do_action(action, maze)
        learn.update_q(state, action, maze.get_reward(new_state), new_state, maze.get_valid_actions(robot), alpha, gamma)
        if new_state == maze.get_state(9, 9) or maze.get_reward(new_state) != 0:
            if robot.nr_of_actions_since_reset <= 200:
                epsilon = max(epsilon - 0.1, 0)
            action_counts.append(robot.nr_of_actions_since_reset)
            robot.nr_of_actions_since_reset = 0
            robot.reset()
            i += 1
    print(action_counts)
