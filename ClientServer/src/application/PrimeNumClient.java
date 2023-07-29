//client portion of the application
package application;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

//client class
//this class takes a number input from the user and returns if the number is
//prime or not. If it is, a 'yes' is returned, otherwise a 'no' is returned.
public class PrimeNumClient extends Application {

	//initialize socket and in/out streams, all init to null
    private Socket socket = null;

    private PrintWriter out = null;

    private BufferedReader in = null;

    //start function (req by javaFX)
    //start is called by launch() which is located in main()
    //For all intensive purposes, start() is the new main()
    @Override
    public void start(Stage primaryStage) throws IOException {

    	//create a label for context, a field for the user to enter a number, and a button to submit
    	//create another label for the result passed back by the server
    	Label instructions = new Label("Enter a number to see if it is prime. Type 'exit' to close the program");
        TextField userInput = new TextField();
        Button submitButton = new Button("Submit");
        Label result = new Label();

        //try the following code
        try {
        	//create the new socket on the local machine on port 4444
            socket = new Socket("localhost", 4444);
            //init the output stream
            out = new PrintWriter(socket.getOutputStream(), true);
            //init the input stream
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        //if there is an error throw an exception and return information to the user
        } catch (IOException e) {
            result.setText("Couldn't get I/O for the connection to: localhost.");
        }
        //when the submit button is pressed, get the user input text and print it to the console
        submitButton.setOnAction(e -> {
            String input = userInput.getText();
            out.println(input);
            //try to set teh result to be the input stream from the socket
            try {
                result.setText("Response from server: " + in.readLine());
            //throw an error if failure
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        //create a vertical box to house the client side elements
        VBox layout = new VBox(10);
        layout.getChildren().addAll(instructions, userInput, submitButton, result);
        //create a scene
        Scene scene = new Scene(layout, 600, 400);
        //title to the window
        primaryStage.setTitle("Prime Number Client");
        //set and show the scene
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //stop function to close all of the open streams
    @Override
    public void stop(){
        try {
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //main method
    public static void main(String[] args) {
        launch(args);
    }
}
