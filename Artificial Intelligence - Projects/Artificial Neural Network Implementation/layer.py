class AbstractLayer:
    
    def __init__(self):
        self.input = None
        self.output = None
        self.batch_size = 0
        self.cost_decay = 0
        self.nr_of_elems = 0
        self.momentum_coefficient = 0

    def forward_propagation(self, input):
        pass

    def backward_propagation(self, output_error, learning_rate):
        pass

    def setParams(self, n, b_size, regularization_c, mom_c):
        self.nr_of_elems = n
        self.batch_size = b_size
        self.cost_decay = regularization_c
        self.momentum_coefficient = mom_c