/**
 * Controller Class
 */
package com.example.ai_test2;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Controller {
    private int trainingDataSize, testingDataSize;
    private double[][] trainingData = null;
    private double[][] testingData = null;
    private double[][] target = null;
    private double[][] expectedTestingOutput = null;
    private double[] testingInputWithBias;
    private double[] predict;
    private Text topology, timeForTraining, timeForTesting;
    private long startTime, endTime, elapsedTime;
    @FXML
    protected TextFlow finalResult;
    @FXML
    protected Button startTrainingBtn, startTestingBtn;
    private ANN ann;
    private ArrayList<String[]> irisDataset;
    Scanner scan;

    /**
     * Method that resets GUI and reads the file with Iris dataset
     */
    public void initialize(){
        topology = null;
        startTestingBtn.setDisable(true);
        finalResult.getChildren().setAll(new Text("Start the training"));
        ann = null;

        File irisFile;
        try {
            // read the file with dataset
            irisFile = new File("iris.csv");
            scan = new Scanner(irisFile);
            scan.nextLine();
            irisDataset = new ArrayList<>();
            while (scan.hasNextLine()){
                String data = scan.nextLine();
                irisDataset.add(data.split(","));
            }
            // divide the dataset into two training set and testing set (50/50)
            testingDataSize = irisDataset.size()/2;
            if(irisDataset.size()%2 == 0){
                trainingDataSize = irisDataset.size()/2;
            } else {
                trainingDataSize = irisDataset.size()/2 + 1;
            }
            trainingData = new double[trainingDataSize][irisDataset.get(1).length - 1];
            testingData = new double[testingDataSize][irisDataset.get(1).length - 1];
            // create arrays for target output for training data and expected output for testing data
            // each output represented as array of size 3. Index 0 is for output "Setosa", 1 - for "Versicolor", 2 - for "Virginica"
            // 0 in the arrays represent false, 1 - true
            target = new double[trainingDataSize][3];
            expectedTestingOutput = new double[testingDataSize][3];
            for (int i = 0; i < irisDataset.size(); i++) {
                try {
                    if (i % 2 == 0) {
                        int j;
                        for(j = 0; j < irisDataset.get(i).length - 1; j++){
                            testingData[i/2][j] = Double.parseDouble(irisDataset.get(i)[j]);
                        }
                        if(irisDataset.get(i)[j].equalsIgnoreCase("setosa")){
                            expectedTestingOutput[i/2][0] = 1;
                        } else if(irisDataset.get(i)[j].equalsIgnoreCase("versicolor")){
                            expectedTestingOutput[i/2][1] = 1;
                        } else if(irisDataset.get(i)[j].equalsIgnoreCase("virginica")) {
                            expectedTestingOutput[i / 2][2] = 1;
                        }
                    } else {
                        int j;
                        for(j = 0; j < irisDataset.get(i).length - 1; j++){
                            trainingData[i/2][j] = Double.parseDouble(irisDataset.get(i)[j]);
                        }
                        if(irisDataset.get(i)[j].equalsIgnoreCase("setosa")){
                            target[i/2][0] = 1;
                        } else if(irisDataset.get(i)[j].equalsIgnoreCase("versicolor")){
                            target[i/2][1] = 1;
                        } else if(irisDataset.get(i)[j].equalsIgnoreCase("virginica")) {
                            target[i / 2][2] = 1;
                        }
                    }
                } catch (NumberFormatException e){
                    // if the input is incorrect, display an alert
                    Alert alert = new Alert(Alert.AlertType.ERROR,"Invalid data.\nThe program will be terminated", ButtonType.OK);
                    alert.setHeaderText("");
                    alert.showAndWait();
                    if (alert.getResult() == ButtonType.OK){
                        System.exit(0);
                    }
                }
            }
        } catch (IOException e){
            // if the path is incorrect, display an alert
            Alert alert = new Alert(Alert.AlertType.ERROR,"ERROR. Could not read a file.\nThe program will be terminated", ButtonType.OK);
            alert.setHeaderText("");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK){
                System.exit(0);
            }
        }
    }

    /**
     * Event handler for the "Start training" button. Starts training
     */
    @FXML
    protected void onStartTrainingBtn(){
        chooseTopology();
    }

    /**
     * Method that displays an alert with a chose of the topology
     */
    private void chooseTopology() {
        ButtonType topology1 = new ButtonType("5-2-3");
        ButtonType topology2 = new ButtonType("5-6-3");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Choose topology", topology1, topology2, ButtonType.CANCEL);
        alert.setContentText("Choose topology");
        alert.showAndWait();
        if (alert.getResult() == topology1){
            // if the user chooses topology of 5-2-3
            topology = new Text("(topology of 5-2-3):\n\n");
            // start timer
            startTime = System.nanoTime();
            // Create s ANN object with the topology of 5-2-3
            ann = new ANN(5,2,3);
            // Training the neural network
            ann.training(trainingData, target);
            // stop timer
            endTime = System.nanoTime();
            // calculate time spent for training
            elapsedTime = endTime - startTime;
            timeForTraining = new Text("Training time: " + elapsedTime/1000000.0 + " milliseconds.\n\n");
            startTestingBtn.setDisable(false);
            finalResult.getChildren().setAll(new Text("Training completed using the topology of 5-2-3\n"+
                    "Start the testing or press start training to choose another topology"));
            alert.close();
        } else if(alert.getResult() == topology2){
            // if the user chooses topology of 5-6-3
            topology = new Text("(Topology of 5-6-3):\n\n");
            // start timer
            startTime = System.nanoTime();
            // Create s ANN object with the topology of 5-6-3
            ann = new ANN(5,6,3);
            // Training the neural network
            ann.training(trainingData, target);
            // stop timer
            endTime = System.nanoTime();
            // calculate time spent for training
            elapsedTime = endTime - startTime;
            timeForTraining = new Text("Training time: " + elapsedTime/1000000.0 + " milliseconds.\n\n");
            startTestingBtn.setDisable(false);
            finalResult.getChildren().setAll(new Text("Training completed using the topology of 5-6-3\n"+
                    "Start the testing or press start training to choose another topology"));
            alert.close();
        } else if(alert.getResult() == ButtonType.CANCEL){
            alert.close();
        }
    }
    /**
     * Event handler for the "Start training" button. Starts testing
     */
    public void onStartTestingBtn() {
        int correct = 0;
        // reset TextFlow
        Text textResult = new Text("Result ");
        finalResult.getChildren().setAll(textResult, topology);
        // start timer
        startTime = System.nanoTime();
        for (int i = 0; i < testingData.length; i++) {
            // add biased neuron in the input layer
            testingInputWithBias = Arrays.copyOf(testingData[i], testingData[i].length + 1);
            testingInputWithBias[testingInputWithBias.length-1] = 1.0;
            // find predicted output
            predict = ann.predict(testingInputWithBias);
            String prediction = "", expected = "";
            // choose the greatest value of output as predicted value
            if(predict[0] > predict[1] && predict[0] > predict[2] ){
                prediction = "Setosa";
            } else if(predict[1] > predict[0] && predict[1] > predict[2]){
                prediction = "Versicolor";
            } else if(predict[2] > predict[1] && predict[2] > predict[0]){
                prediction = "Virginica";
            } else {
                System.out.println("ERROR");
            }
            if(expectedTestingOutput[i][0] == 1){
                expected = "Setosa";
            } else if(expectedTestingOutput[i][1] == 1){
                expected = "Versicolor";
            } else if(expectedTestingOutput[i][2] == 1){
                expected = "Virginica";
            }
            // add the result of the sample testing to the finalResult TextFlow
            Text textPredict1=new Text("Sample " + (i+1) + ". Predicted output: ");
            Text textPredict2 = new Text(prediction + "\n");
            Text textExpect1 = new Text("\t\tExpected output: ");
            Text textExpect2 = new Text(expected + "\n\n");
            textPredict2.setStyle("-fx-font-weight: bold");
            textExpect2.setStyle("-fx-font-weight: bold");
           
            if(expected.equalsIgnoreCase(prediction)){
                correct++;
            } else {
                // setting color of the text to red if prediction is incorrect
                textPredict2.setFill(Color.RED);
                textExpect2.setFill(Color.RED);
            }
            finalResult.getChildren().addAll(textPredict1, textPredict2, textExpect1,textExpect2);
        }
        // stop timer
        endTime = System.nanoTime();
        elapsedTime = endTime - startTime;
        timeForTesting = new Text("Testing time: " + elapsedTime/1000000.0 + " milliseconds.\n\n");
        String formattedText = String.format("\nAccuracy = %.2f%%\n\n", correct*100.0/testingData.length);
        Text accuracyText = new Text(formattedText);
        // calculate accuracy of predictions and display the results
        accuracyText.setStyle("-fx-font-weight: bold");
        finalResult.getChildren().addAll(new Text("\nTest completed."), accuracyText, timeForTraining, timeForTesting);
    }
    /**
     * Event handler for the "Exit" button. Terminates the program after confirmation
     */
    @FXML
    protected void onExitBtn() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to exit?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES){
            System.exit(0);
        }
    }

}