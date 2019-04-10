package project7;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

/**
 * Project 7 a javafx application in which the task performs calculations of your 
 * own choosing that return continual output to a ListView control including 
 * update to a progress bar; the task can be canceled at any time.
 * 
 *@author Andy Osorio / Thomas Lawless / Justin Moran SCCC Spring 2019
 */
public class Project7 extends Application {

    private TextField textFieldInput;
    private Button buttonGetNumber;
    private HBox hBoxInput;

    private ListView listViewNumbers;

    private Button buttonCancel;
    private ProgressBar progressBar;
    private Label labelStatus;

    private HBox hBoxStatus;

    private VBox vBoxNumbers;
    
    // Stores the list of numbers received from Calculator
    private final ObservableList Numbers = FXCollections.observableArrayList();
    private Calculator calc;

    /**
     *
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        textFieldInput = new TextField();
        textFieldInput.setPrefWidth(125);

        buttonGetNumber = new Button("Get Numbers");
        buttonGetNumber.setStyle("-fx-text-fill: black; -fx-background-color: brown; -fx-font-weight: bold;");
        buttonGetNumber.setOnAction(e -> buttonGetNumberPressed(e));

        hBoxInput = new HBox(15, new Label(":"), textFieldInput, buttonGetNumber);
        hBoxInput.setStyle("-fx-padding: 5; -fx-border-style: solid inside; -fx-border-width: 2; -fx-border-radius: 3; -fx-border-color: rgb(176, 176, 176);");

        listViewNumbers = new ListView();
        listViewNumbers.setPrefSize(380, 150);

        buttonCancel = new Button("Cancel");
        buttonCancel.setStyle("-fx-text-fill: black; -fx-background-color: red; -fx-font-weight: bold;");
        buttonCancel.setDisable(true);
        buttonCancel.setOnAction(e -> buttonCancelPressed(e));

        progressBar = new ProgressBar();
        progressBar.setPrefWidth(150);

        labelStatus = new Label(" Counter");

        hBoxStatus = new HBox(18, buttonCancel, progressBar, labelStatus);
        hBoxStatus.setStyle("-fx-padding: 5; -fx-border-style: solid inside; -fx-border-width: 2; -fx-border-radius: 3; -fx-border-color: rgb(176, 176, 176);");

        vBoxNumbers = new VBox(0, hBoxInput, listViewNumbers, hBoxStatus);

        Scene scene = new Scene(vBoxNumbers, 400, 250);

        primaryStage.setTitle("Calculator");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Creates a task that counts numbers from the Calculator class
     * and displays the results in a ListView control.
     * 
     * @param event
     */
    private void buttonGetNumberPressed(ActionEvent event) {
        
        // Empties previous contents of the ObservableList numbers
        // Binds listViewNumbers's items to the ObservableList numbers
        
        Numbers.clear();
        listViewNumbers.setItems(Numbers);

        try {
            // Display calc's messages in labelStatus
            // Update progressBar based on calc's progressProperty
            // Store intermediate results in the ObservableList numbers

            int input = Integer.parseInt(textFieldInput.getText());
            calc = new Calculator(input);

            labelStatus.textProperty().bind(calc.messageProperty());

            progressBar.progressProperty().bind(calc.progressProperty());

            calc.valueProperty().addListener((observable, oldValue, newValue)
                    -> {
                if ((int) newValue != 0) 
                {
                    Numbers.add(newValue);
                    listViewNumbers.scrollTo(listViewNumbers.getItems().size());
                }
            });

            calc.setOnRunning((succeededEvent)
                    -> {
                buttonGetNumber.setDisable(true);
                buttonCancel.setDisable(false);
            });

            calc.setOnSucceeded((succeededEvent)
                    -> {
                buttonGetNumber.setDisable(false);
                buttonCancel.setDisable(true);
            });

            ExecutorService executorService = Executors.newFixedThreadPool(1);
            executorService.execute(calc);
            executorService.shutdown();
        } catch (NumberFormatException ex) {
            textFieldInput.setText("Enter an integer");
            textFieldInput.selectAll();
            textFieldInput.requestFocus();
        }
    }

    /**
     * Cancel the task when user presses the Cancel Button.
     *
     * @param event the ActionEvent parameter
     */
    private void buttonCancelPressed(ActionEvent event) {
        if (calc != null) {
            calc.cancel(); //terminate the task
            buttonGetNumber.setDisable(false);
            buttonCancel.setDisable(true);
        }
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
