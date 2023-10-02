import random
from matplotlib import pyplot as plt


class SinglePerceptron(object):

    def __init__(self, input_size, threshold_theta, learning_rate):
        self.weights = [random.uniform(-0.5, 0.5) for _ in range(input_size)]
        self.theta = threshold_theta
        self.alpha = learning_rate

    def calculate_output(self, input_xs):
        assert len(input_xs) == len(self.weights)
        result = 0
        for i in range(len(self.weights)):
            result += input_xs[i] * self.weights[i]
        result -= self.theta
        if result >= 0:
            return 1
        else:
            return 0
        
    def update_weights(self, input_xs, desired_output):
        actual_output = self.calculate_output(input_xs)
        errors = []
        for i in range(len(self.weights)):
            error = desired_output - actual_output
            errors.append(error)
            weight_correction = self.alpha * input_xs[i] * error
            self.weights[i] = self.weights[i] + weight_correction
        sum = 0
        for error in errors:
            sum += error
        return float(sum) / len(errors)

    def train(self, inputs, outputs, epochs):
        assert len(inputs) == len(outputs)
        print('Training')
        errors = []
        for epoch in range(1, epochs + 1):
            print('Epoch', epoch)
            epoch_errors = []
            for i, input_xs in enumerate(inputs):
                epoch_errors.append(self.update_weights(input_xs, outputs[i]))
            sum_error = 0
            for error in epoch_errors:
                sum_error += error
            errors.append(float(sum_error) / len(epoch_errors))
        print('Done')
        print('Errors:', errors)
        plt.plot(list(range(1, epochs + 1)), errors)
        plt.xlabel('Epochs')
        plt.ylabel('Error')
        plt.title('Error over epochs for XOR function')
        plt.show()


if __name__ == "__main__":
    perceptron = SinglePerceptron(2, 0.2, 0.1)
    inputs = [[0, 0], [0, 1], [1, 0], [1, 1]]
    outputs = [0, 1, 1, 0]
    initial_results = [perceptron.calculate_output(input_xs) for input_xs in inputs]
    perceptron.train(inputs, outputs, 3)
    final_results = [perceptron.calculate_output(input_xs) for input_xs in inputs]
    print('Expected output:', outputs)
    print('Initial output:', initial_results)
    print('Final output:', final_results)
