import numpy as np

from layer import AbstractLayer


class FullyConnectedLayer(AbstractLayer):

    def __init__(self, input_size, output_size):
        # Weights randomly chosen from a standard normal distribution (0, 1) and adjusted to be /srt(nr of input neurons), in order to avoid
        # the problem of having over saturated neurons generating too little impact in the error propagated back along the network
        self.weights = np.random.rand(input_size, output_size) / np.sqrt(input_size)
        self.momentum = np.zeros_like(self.weights)
        self.bias = np.random.rand(1, output_size)

    def forward_propagation(self, input_data):
        self.input = input_data
        self.output = np.dot(self.input, self.weights) + self.bias
        return self.output

    def backward_propagation(self, output_error, learning_rate):
        input_error = np.dot(output_error, self.weights.T)
        weights_error = np.dot(self.input.T, output_error)
        w_decay = 1 - learning_rate * self.cost_decay / self.nr_of_elems
        self.momentum = self.momentum_coefficient * self.momentum + weights_error * learning_rate / self.batch_size
        self.weights = self.weights * w_decay + self.momentum
        # print("weights error is: ", weights_error)
        self.bias += learning_rate / self.batch_size * np.mean(output_error, axis=0)
        return input_error
