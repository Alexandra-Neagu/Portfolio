import numpy as np
import math

from fully_connected_layer import FullyConnectedLayer

class NeuralNetwork:

    def __init__(self, epochs, learning_rate, batch_size,
                 regularization_factor, momentum_coefficient,
                 early_stop_epochs, early_stop_value):
        self.batch_size = batch_size
        self.learning_rate = learning_rate
        self.epochs = epochs
        self.reg_cost_d = regularization_factor
        self.momentum_coeff = momentum_coefficient
        self.layers = []
        self.loss = None
        self.loss_prime = None
        self.nr_categ, self.train_frac, self.test_frac = 0, 0, 0
        self.nr_items = 0
        self.early_stop = early_stop_epochs
        self.early_stop_value = early_stop_value

    def set(self, nr_of_categories, train_fraction, test_fraction):
        self.nr_categ = nr_of_categories
        self.train_frac = train_fraction
        self.test_frac = test_fraction

    def setNr(self, n):
        self.nr_items = n

    def add(self, layer):
        layer.setParams(self.nr_items, self.batch_size, self.reg_cost_d, self.momentum_coeff)
        self.layers.append(layer)

    def use(self, loss, loss_prime):
        self.loss = loss
        self.loss_prime = loss_prime

    def predict(self, input_data):
        output = input_data
        for layer in self.layers:
            output = layer.forward_propagation(output)
        return output

    # The main cycle of our nn, looping over the number of epochs and with the already initialized batch_size
    def think(self, pairs_feat_target, x_validation, y_validation):
        l = math.floor(int(len(pairs_feat_target)) / self.batch_size)
        size = len(pairs_feat_target)
        validation_accuracy = []
        training_loss = []
        i = 0
        accuracy, counter = 0, 0
        while i < self.epochs and counter < self.early_stop:
            x_train, y_train = self.shuffle(pairs_feat_target)
            error = 0
            if self.batch_size == 1:
            # Splitting the approach between having batch_size 1 and >1
                for start in range(0, l):
                    upper_l = start + 1
                    x_batch = x_train[start:upper_l, :]
                    y_batch = y_train[start:upper_l, :]
                    error += self.fit(x_batch, y_batch)
            else:
                for start in range(0, size, self.batch_size):
                    if (start + self.batch_size) >= size:
                        upper_l = size
                    else:
                        upper_l = start + self.batch_size
                    x_batch = x_train[start:upper_l, :]
                    y_batch = y_train[start:upper_l, :]
                    error += self.fit(x_batch, y_batch)
            error = error/l
            accuracy_valid = round(self.evaluate(self.predict(x_validation), y_validation, False) * 100, 2)
            validation_accuracy.append(accuracy_valid)
            training_loss.append(error)
            print('Epoch %d/%d   error=%f' % (i + 1, self.epochs, error))
            print("-accuracy on the validation set: %f" % accuracy_valid)
            i += 1
            diff = accuracy_valid - accuracy
            if counter >= 3:
                self.learning_rate /= 3
            if diff <= self.early_stop_value:
                counter += 1
            else:
                counter = 0
            accuracy = accuracy_valid
        return validation_accuracy, training_loss, i

    #The main subroutine that does the learning process over one mini-batch of samples
    def fit(self, x_train, y_train):
        output = x_train
        for layer in self.layers:
            output = layer.forward_propagation(output)
        err = self.loss(y_train, output)
        error = self.loss_prime(y_train, output, self.layers[-1])
        # print("forward is done and interval is: ", interval)
        # print("ERROR FOR EPOCH %d is ", error, i+1)
        for layer in reversed(self.layers):
            error = layer.backward_propagation(error, self.learning_rate)
        return err

    def shuffle(self, features_targets):
        # Shuffling the original array
        np.random.shuffle(features_targets)

        delim = len(features_targets[0]) - 7
        # Splitting the given sample into train/test/validation
        x_train = features_targets[:, 0: delim]
        y_train = features_targets[:, delim:]
        return x_train, y_train

    def split(self, features_targets):
        # # Shuffling the original array
        # np.random.shuffle(features_targets)
        delim = len(features_targets[0]) - 1
        nr_items = len(features_targets)
        size_train_set = int(nr_items * self.train_frac)
        size_test_set = size_train_set + int(nr_items * self.test_frac)
        # Splitting the given sample into train/test/validation
        x_train = features_targets[0:size_train_set, 0: delim]
        x_test = features_targets[size_train_set:size_test_set, 0: delim]
        x_validation = features_targets[size_test_set:, 0:delim]
        y_train = self.convert_from_single_label(features_targets[0:size_train_set, delim:])
        y_test = self.convert_from_single_label(features_targets[size_train_set:size_test_set, delim:])
        y_validation = self.convert_from_single_label(features_targets[size_test_set:, delim:])
        return x_train, x_validation, x_test, y_train, y_validation, y_test


    # Converts a (m, 1) numpy array to a (m, x) one, where x is the number of target classes
    def convert_from_single_label(self, arr):
        result = np.array(arr).astype(int).reshape(-1) - 1
        return np.eye(self.nr_categ)[result]

    # Evaluates the accuracy of our nn on the @excpected training set
    def evaluate(self, actual, expected, loss):
        # print("actual result", np.shape(actual))
        # print("expected result", np.shape(expected))
        assert len(actual) == len(expected)
        correct = 0
        for i in range(len(actual)):
            if np.argmax(actual[i]) == np.argmax(expected[i]):
                correct += 1
        err = self.loss(expected, actual)
        if loss:
            return float(correct) / len(actual), err
        else:
            return float(correct) / len(actual)

    def getWeights_avg(self):
        res = 0
        for l in self.layers:
            if isinstance(l, FullyConnectedLayer):
                res = res + np.mean(l.weights)
        return res

    def reset(self):
        self.layers = []