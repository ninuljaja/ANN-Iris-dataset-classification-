/**
 * ANN class
 */
package com.example.ai_test2;

import java.util.Arrays;

public class ANN {
    private double[][] weightsInputHidden;
    private double[][] weightsHiddenOutput;
    private double[] hiddenLayer;
    private double[] outputLayer;
    private double learningRate = 0.1;

    /**
     * Constructor that creates ANN object
     * @param inputSize an integer that holds number of input values
     * @param hiddenSize an integer that holds number of input layers
     * @param outputSize an integer that holds number of output values
     */
    public ANN(int inputSize, int hiddenSize, int outputSize) {
        weightsInputHidden = new double[inputSize + 1][hiddenSize]; // +1 fo the bias neuron
        weightsHiddenOutput = new double[hiddenSize][outputSize];
        hiddenLayer = new double[hiddenSize];
        outputLayer = new double[outputSize];

        // Initialize weights
        initializeWeights(weightsInputHidden);
        initializeWeights(weightsHiddenOutput);
    }
    /**
     * Method that initializes weights with random values between -0.5 and 0.5
     */
    private void initializeWeights(double[][] weights){
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                weights[i][j] = Math.random()-0.5;
            }
        }
    }

    /**
     * Method that initiates training
     * @param trainingData 2D array of doubles that holds training input data
     * @param targets 2D array of doubles that represents expected output for each sample of training input data
     */
    public void training(double[][] trainingData, double[][] targets){
        // run training until predictions for all training samples are correct
        double[] inputWithBias;
        for (int j = 0; j < 10000; j++) {
            for (int i = 0; i < trainingData.length; i++) {
                inputWithBias = Arrays.copyOf(trainingData[i], trainingData[i].length + 1);
                inputWithBias[inputWithBias.length-1] = 1.0;
                train(inputWithBias, targets[i]);
            }
        }
    }
    /**
     * Method that updates weights
     * @param input array of doubles that holds one sample of training input data
     * @param target array of doubles that represents expected output fot the sample
     */
    private void train(double[] input, double target[]) {
        // find predicted output
        predict(input);
        // Calculate error term for output units
        double[] errorTermsOutput = new double[outputLayer.length];
        for (int j = 0; j < outputLayer.length; j++) {
            errorTermsOutput[j] = outputLayer[j]*(1-outputLayer[j])*(target[j] - outputLayer[j]);
        }

        // Update weights for the hidden to output layer
        for (int i = 0; i < hiddenLayer.length; i++) {
            for (int j = 0; j < outputLayer.length; j++) {
                weightsHiddenOutput[i][j] += learningRate * errorTermsOutput[j] * hiddenLayer[i];
            }
        }
        // Calculate error term for hidden units
        double[] errorTermsHidden = new double[hiddenLayer.length];
        for (int i = 0; i < hiddenLayer.length; i++) {
            double sum = 0;
            for (int j = 0; j < errorTermsOutput.length; j++){
                sum += errorTermsOutput[j]*weightsHiddenOutput[i][j];
            }
            errorTermsHidden[i] = hiddenLayer[i]*(1-hiddenLayer[i])*sum;
        }
        // Update weights for the input to hidden layer
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < hiddenLayer.length; j++) {
                weightsInputHidden[i][j] += learningRate * errorTermsHidden[j] * input[i];
            }
        }
    }


    /**
     * Method that calculates and returns the predicted output for the sample data
     * @param input array of doubles that holds input data
     * @return array of doubles that represents the predicted output layer
     */
    public double[] predict(double [] input) {
        // Sigma function
        // calculate output from the hidden layer
        for (int i = 0; i < hiddenLayer.length; i++) {
            double sum = findWeightedSum(input, weightsInputHidden, i);
            hiddenLayer[i] = sigma(sum);
        }
        // calculate output from the output layer
        for (int i = 0; i < outputLayer.length; i++) {
            double sum = findWeightedSum(hiddenLayer, weightsHiddenOutput, i);
            outputLayer[i] = sigma(sum);
        }
        return outputLayer;
    }

    /**
     * Method that calculates weighted sums to the layer
     * @param values an array of doubles that holds values of the layer
     * @param weight a 2D array of doubles that hold current weights
     * @param j an integer that represents one unit for with weighted sum calculated
     * @return a double that represents a weighted sum
     */
    private double findWeightedSum(double[] values, double[][] weight, int j){
        double sum = 0;
        for (int i = 0; i < values.length; i++){
            sum += values[i]*weight[i][j];
        }
        return sum;
    }

    /**
     * Method that calculates and returns the output from the layer using sigmoid function
     * @param S a double that holds weighted sum of inputs
     * @return a double that represents the output from the layer
     */
    private double sigma(double S){
        return 1/(1.0+Math.exp(-S));
    }
}

