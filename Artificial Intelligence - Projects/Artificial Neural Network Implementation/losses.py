import numpy as np

def mse(y_true, y_pred):
    a = np.mean(np.power(y_true - y_pred, 2))
    return a

def mse_prime(y_true, y_pred, L):
    b = 2*(y_true - y_pred)
    return b

def cross_entropy(targets, predictions):
    epsilon = 1e-12
    predictions = np.clip(predictions, epsilon, None)
    xEntropy = -np.sum(targets * np.log(predictions)) / len(predictions)
    return xEntropy

def cross_entropy_prime(targets, predictions, L):
    res = np.divide((targets - predictions), L.activation_prime(L.input))
    return res