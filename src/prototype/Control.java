/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author andreas lees
 */
public class Control extends Application {
    private String workingDir;
    private BufferedWriter output = null;


    @Override
    public void start(Stage primaryStage) {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        workingDir = System.getProperty("user.dir");
        File file = new File(workingDir + "/storage/");
        file.mkdirs();
        workingDir = file.getPath();
        file = new File(workingDir + "/users.json");
        try {
            if (file.createNewFile()) {
                output = new BufferedWriter(new FileWriter(file));
                output.write("[{\"pass\":\"doc\",\"patientID\":\"1\",\"type\":\"DOCTOR\",\"username\":\"doc\"}," +
                        "{\"pass\":\"nurse\",\"patientID\":\"2\",\"type\":\"NURSE\",\"username\":\"nurse\"}," +
                        "{\"pass\":\"patient\",\"patientID\":\"3\",\"type\":\"PATIENT\",\"username\":\"patient\"}," +
                        "{\"pass\":\"doc\",\"patientID\":\"4\",\"type\":\"DOCTOR\",\"username\":\"Lees\"}]");

                output.close();
                System.out.println("File Saved");

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        LoginMenu authMenu = new LoginMenu(workingDir);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
