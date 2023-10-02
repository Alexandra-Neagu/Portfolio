import numpy as np

from layer import AbstractLayer


class ActivationLayer(AbstractLayer):
    def __init__(self, activation, activation_prime):
        self.activation = activation
        self.activation_prime = activation_prime

    def forward_propagation(self, input_data):
        self.input = input_data
        self.output = self.activation(self.input)
        return self.output

    #Feeding the nn backwards from right to left with the left layer's error and the sigmoid derivative application
    def backward_propagation(self, output_error, learning_rate):
        a = self.activation_prime(self.input)
        res = np.multiply(a, output_error)
        return res
