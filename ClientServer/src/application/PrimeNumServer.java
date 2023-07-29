//server portion of the application
package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

//server class
//this takes a number passed in from the client and determines if it is prime or not.
//If yes, a 'yes' is returned, otherwise a 'no' is returned
public class PrimeNumServer extends Application {

	//initializing sockets and streams for use
    private ServerSocket serverSocket = null;
    private Socket clientSocket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private TextArea statusArea = new TextArea();

    //isPrime function sees  if number is 2 or less, and returns a boolean
    //if the input number is 3 or greater, run for loop. If for loop modulus operator returns
    //zero, return 'no', otherwise return 'yes'
    private static boolean isPrime(int n) {
        if (n <= 1) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }

    //start function (req by javaFX)
    //start is called by launch() which is located in main()
    //For all intensive purposes, start() is the new main()
    @Override
    public void start(Stage primaryStage) throws IOException {

    	
        try {
            serverSocket = new ServerSocket(4444);
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            statusArea.appendText("Connection successful\n");
            statusArea.appendText("Waiting for input.....\n");
        } catch (IOException e) {
            statusArea.appendText("Could not listen on port: 4444.\n");
            System.exit(1);
        }
        //create a new thread to handle input
        new Thread(() -> {
            String inputLine;
            try {
            	//if the user enters a non-null input
                while ((inputLine = in.readLine()) != null) {
                	//append it to the textarea
                    statusArea.appendText("Server: " + inputLine + "\n");
                    //parse teh integer from the string
                    int number = Integer.parseInt(inputLine);
                    //print out if it is prime or not
                    out.println(isPrime(number) ? "yes" : "no");
                    //if user types exit, program halts
                    if (inputLine.equals("exit")) break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        
        //layout and vertical box to hold all the server components
        VBox layout = new VBox(10);
        layout.getChildren().addAll(new Label("Server Status"), statusArea);
        //create scene and title, and show
        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setTitle("Prime Number Server");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //stop function to close all of the open streams
    @Override
    public void stop() {
        try {
            out.close();
            in.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //main method
    public static void main(String[] args) {
        launch(args);
    }
}