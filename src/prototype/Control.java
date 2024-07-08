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

        String[] argList = this.getParameters().getRaw().toArray(new String[0]);
        if (argList.length < 1) {
            System.out.println("[Debug] No args");
        } else {
            for (int i=0; i < argList.length; i++) {
                System.out.println("[Debug] Arg "+i + " : " + argList[i]);
            }
        }
        if (!(argList.length < 1) && argList[0].equals("-test")) {
            System.out.println("[Info] Creating test storage");
            setupStorage("[{\"pass\":\"doc\",\"patientID\":\"1\",\"type\":\"DOCTOR\",\"username\":\"doc\"}," +
                    "{\"pass\":\"nurse\",\"patientID\":\"2\",\"type\":\"NURSE\",\"username\":\"nurse\"}," +
                    "{\"pass\":\"patient\",\"patientID\":\"3\",\"type\":\"PATIENT\",\"username\":\"patient\"}," +
                    "{\"pass\":\"admin\",\"patientID\":\"4\",\"type\":\"ADMIN\",\"username\":\"admin\"}]");
        } else {
            System.out.println("[Info] Creating storage");
            setupStorage("[{\"pass\":\"admin\",\"patientID\":\"4\",\"type\":\"ADMIN\",\"username\":\"admin\"}]");
        }
    }
    private void setupStorage(String users) {
        System.out.println("[Debug] Working Directory = " + System.getProperty("user.dir"));
        workingDir = System.getProperty("user.dir");
        File file = new File(workingDir + "/storage/");
        file.mkdirs();
        workingDir = file.getPath();
        file = new File(workingDir + "/users.json");
        try {
            if (file.createNewFile()) {
                System.out.println("[Info] Creating user list");
                output = new BufferedWriter(new FileWriter(file));
                output.write(users);

                output.close();
                //System.out.println("File Saved");

            } else {
                System.out.println("[Info] Data exists!");
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
