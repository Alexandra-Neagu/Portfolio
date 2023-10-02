import numpy as np
import matplotlib.pyplot as plt

from activation_layer import ActivationLayer
from activations import sigmoid, sigmoid_prime
from fully_connected_layer import FullyConnectedLayer
from losses import mse, mse_prime, cross_entropy, cross_entropy_prime
from neural_net import NeuralNetwork


# Code used for plotting the performance of our nn regarding the number of neurons inside our hidden layer
def plot_nr_neurons(x, y):
    figure = plt.figure()
    x = [a for a in range(10, 31, 5)]
    plt.plot(x, y)
    plt.xlabel("Number of neurons in the hidden layer")
    plt.ylabel("Error on the validation set")
    plt.show()
    figure.savefig("Accuracy_vs_nrNeurons.png", dpi=figure.dpi)

'''Computes a confusion matrix using numpy for two np.arrays '''
def compute_confusion_matrix(true, pred):
    p = [np.argmax(a) for a in pred]
    t = [np.argmax(a) for a in true]
    k = len(np.unique(t)) # Number of classes
    result = np.zeros((k, k))
    for i in range(len(true)):
        result[t[i]][p[i]] += 1
    return result.astype(int)

# Method for plotting the confusion matrix of our algorithm, based on its parameters after training and test set
def plot_confusion_matrix(y_test, final_result, labels):
    confusion_matrix = compute_confusion_matrix(y_test, final_result)
    lbls = np.arange(labels)
    fig, axes = plt.subplots()
    im = axes.imshow(confusion_matrix, cmap='Blues')
    cbar = fig.colorbar(im, ticks=[np.min(confusion_matrix), np.max(confusion_matrix)])

    axes.set_xticks(lbls)
    axes.set_yticks(lbls)

    axes.set_xticklabels(lbls + 1)
    axes.set_yticklabels(lbls + 1)

    # plt.setp(axes.get_xticklabels(), rotation=45, ha="right", rotation_mode="anchor")
    for i in range(labels):
        for j in range(labels):
            text = axes.text(j, i, confusion_matrix[i, j], ha="center", va="center", color="y")

    axes.set_title("Confusion matrix on test data")
    fig.tight_layout
    plt.xlabel("Predicted category")
    plt.ylabel("Actual category")
    plt.show()
    fig.savefig("Confusion_matrix.png", dpi=fig.dpi)

def predict_unknown_sample(net):
    unknown_sample = np.genfromtxt("unknown.txt", delimiter=',')
    unknown_result = net.predict(scale(unknown_sample))
    s = []
    length = len(unknown_result)
    for i in range(length):
        a = np.argmax(unknown_result[i])
        s.append(a + 1)
    res = ""
    for i in range(len(s)):
        if i == len(s) - 1:
            res += str(s[i])
        else:
            res += str(s[i]) + ","
    filename = "Group_12_classes.txt"
    filepath = "./" + filename
    f = open(filepath, "w")
    f.write(res)

# Normalize the values inside this array to be between 0 and 1, with consideration that new unseen values might slightly deviate from this interval
def scale(arr):
    minimum = np.min(arr) * 0.8
    maximum = np.max(arr) * 1.2
    arr = (arr - minimum) / (maximum - minimum)
    return arr

if __name__ == "__main__":

    features = np.genfromtxt("features.txt", delimiter=",")
    targets = np.genfromtxt("targets.txt", delimiter=",")
    targets = targets.reshape(len(targets), 1)
    labels = len(np.unique(targets))

    test_frac = 0.15
    validation_frac = 0.15
    train_frac = 1 - test_frac - validation_frac
    bootstrap_factor = 5

    # Scale the features down to be between 0 and 1, in order to allow some bootstrapping and better activations value for future neuron layers
    features = scale(features)
    features_targets = np.append(features, targets, axis=1)


    # Initializing the hyperparameters of our nn:
    net = NeuralNetwork(epochs=100, learning_rate=0.107,
                        batch_size=7, regularization_factor=0.55,
                        momentum_coefficient=0.13, early_stop_epochs=10,
                        early_stop_value=0.088)
    #Set the splitting criteria for the whole working sample
    net.set(nr_of_categories=labels, train_fraction=train_frac, test_fraction=test_frac)

    np.random.shuffle(features_targets)
    #Retrieve the sample sets for training, validation and testing
    x_train, x_validation, x_test, y_train, y_validation, y_test = net.split(features_targets)
    original_x_train = x_train
    original_y_train = y_train
    for i in range(bootstrap_factor):
        x_train_bootstrap = original_x_train + np.random.rand(*original_x_train.shape) * 0.033
        x_train = np.append(x_train, x_train_bootstrap, axis=0)
        y_train = np.append(y_train, original_y_train, axis=0)

    samples_to_train_on = np.append(x_train, y_train, axis=1)
    net.setNr(len(samples_to_train_on))

    net.use(cross_entropy, cross_entropy_prime)
    # validation_acc_weight_init = [] #// used for exercise 10 in monitoring the impact random weight values
    # has on the final accuracy of our model
    # weights_init = [] #// used for ex 10
    # Used for finding the right amount of neurons inside the hidden layer for ex 11
    # y = []
    # for i in range(10, 31, 5):
    iterations = 1
    validation_accuracies = [0 for a in range(net.epochs)]
    training_error = [0 for a in range(net.epochs)]
    for i in range(iterations):
        net.add(FullyConnectedLayer(10, 15))
        net.add(ActivationLayer(sigmoid, sigmoid_prime))
        net.add(FullyConnectedLayer(15, 7))
        net.add(ActivationLayer(sigmoid, sigmoid_prime))
        # weights_init.append(net.getWeights_avg()) #// used for ex 10
        # initial_result = net.predict(x_test)
        # init_accuracy = net.evaluate(initial_result, y_test)
        # print('Initial accuracy:', init_accuracy)
        # validation_acc_weight_init.append(valid_acc[-1]) #// used for ex 10
        valid_acc, train_err, nr_epoch = net.think(samples_to_train_on, x_validation, y_validation)
        validation_accuracies = [a + b for a, b in zip(validation_accuracies, valid_acc)]
        training_error = [a + b for a, b in zip(training_error, train_err)]
        # net.reset()
    # y.append(validation_accuracies[-1]) #// Used for ex 11
    # x2 = [a for a in range(10, 31, 5)] # // Used for ex 11

    # /// Those lines were used for ex 10 in plotting the effect of different weight values
    # x2 = weights_init
    # w = sorted(zip(x2, validation_acc_weight_init), key=lambda t: t[0])
    # x2,validation_acc_weight_init = zip(*w)
    # hidden_n.append(validation_accuracies[-1]) //Code used for ex 11

    # figure = plt.figure()
    # plt.plot(x, validation_accuracies)
    # plt.xlabel("Number of epochs")
    # plt.ylabel("Accuracy on the validation set")
    # plt.show()
    # figure.savefig("validation_accuracy.png", dpi=figure.dpi)

    # plot_confusion_matrix(y_test=y_test, final_result=final_result, labels=labels) #// Used for ex 14

    x = np.arange(nr_epoch)
    x_entropy_train = training_error[-1]
    final_result = net.predict(x_test)
    test_accuracy, x_entropy_test = net.evaluate(final_result, y_test, True)
    test_accuracy = round(test_accuracy * 100, 2)
    print("Final cross entropy loss for training test: ", x_entropy_train)
    print("Final cross entropy loss for test set: ", x_entropy_test)
    print("Final accuracy on validation set: ", validation_accuracies[-1])
    print('Final accuracy on test set:', test_accuracy)

    predict_unknown_sample(net)