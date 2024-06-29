/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author andreas lees
 */
public class PatientMenu extends Menus {
    private Scene patientSummaryScene;
    private JSONObject jo;
    //---------------Patient Menu-------------------
    PatientMenu(String ID) {
        this.patientID = ID;
        Patient patient = new Patient(patientID);
        System.out.println("ID: " + patientID);
        String patientName = patient.getName();
        menu.setText("Hello, " + patientName);   
        this.setTitle("Main Menu");
        
        backBtn.setText("Back");
        backBtn.setMinHeight(0);
        backBtn.setMinWidth(100);
        backBtn.setBackground(bkgrndBlue);
        backBtn.setBorder(border);
        backBtn.setOnMouseEntered(e -> backBtn.setBackground(bkgrndLBlue));
        backBtn.setOnMouseExited(e -> backBtn.setBackground(bkgrndBlue));
//---------------------grid-----------------------------------------------         
        GridPane layout = new GridPane();
        layout.setAlignment(Pos.CENTER);
        //layout.setGridLinesVisible(true);     //debug
        
        Label scenetitle = new Label("Welcome to []");
        layout.setHalignment(scenetitle, HPos.CENTER);
        layout.add(scenetitle, 0, 0);
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setHgap(5);
        grid.setVgap(15);
        //grid.setGridLinesVisible(true);       //debug
        
        layout.add(grid, 1, 1);
        
//---------------------buttons----------------------------------------------- 
        
        
//Color.LIGHTSKYBLUE
        Button pInfoBtn = SetupButton("Contact Info");
        
        
        Button patientSumBtn = SetupButton("Visit Summary");
        
        //collect patient info
        patient = new Patient(patientID);
        
        column= 0;
        grid.add(pInfoBtn, column, row); 
        row++;
        grid.add(patientSumBtn, column, row);
        
        pInfoBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    //load up main menu
                    changeTitle("Patient Intake");
                    changeScene(InfoMenu());
                    
                    
            }
            
        });
        patientSumBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    if (patientID == null) {
                        patientID = "";
                    }
                    changeTitle("Patient Health");
                    changeScene(SummaryMenu());
                    
            }
        });
        backBtn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                changeTitle("Main Menu");
                changeScene(loginScene);
            }
        });
        loginScene = new Scene(layout, width, height);
        this.setScene(loginScene);
        this.show();

    }    
    private Scene InfoMenu() {
        //populate with contact info
        return loginScene;
    }
    private Scene SummaryMenu() {
        
        menu.setText("Patient Summary");        
        GridPane layout = new GridPane();
        menu.setAlignment(Pos.CENTER);
        layout.setHalignment(menu, HPos.CENTER);
        layout.setAlignment(Pos.CENTER);
        
        layout.add(menu, 0, 0);
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        //grid.setGridLinesVisible(true);       //debug
        

        

        
        Label errorLabel = new Label();  
        errorLabel.setAlignment(Pos.CENTER);
        layout.setHalignment(errorLabel, HPos.CENTER);
        errorLabel.setText("Error: Missing input"); 
        errorLabel.setVisible(false);
        layout.add(errorLabel, 0, 2);
        

        System.out.println(patientID);  //debug
        if (patientID.isEmpty()) {
            System.out.println("no ID");  //debug
            errorLabel.setText("Error: No ID"); 
            errorLabel.setVisible(true);
        }

        errorLabel.setText("PatientID: " + patientID); 
        errorLabel.setVisible(true);
        //create patient
        patient = new Patient(patientID); 
        //load patient health
        jo = patient.loadPatient(patient.getHealthFile());
        //load patient info
    //    jo = patient.loadPatient(patient.getInfoFile());
        //load patient vitals
    //    jo = patient.loadPatient(patient.getVitalFile());
        System.out.println(jo.toString()); //debug
                    
        String allergiesInfo = "";
        String healthConcernsInfo = "";
        try {
            allergiesInfo = jo.getString("allergies");
            healthConcernsInfo = jo.getString("healthConc");
            
        } catch (JSONException e) {
            System.out.println("no data");  //debug
            
        }
        
        Label allergies = new Label("Allergies:");
        grid.add(allergies, 0, 2);
        Label allergiesInput = new Label();
        allergiesInput.setText(allergiesInfo);
        allergiesInput.setBorder(border);
        allergiesInput.setMinHeight(100);
        allergiesInput.setMinWidth(100);
        //allergiesInput.setDisable(true);
        grid.add(allergiesInput, 1, 2);
        
        Label healthConc = new Label("Health Concerns:");
        grid.add(healthConc, 0, 3);
        Label healthConcInput = new Label();
        healthConcInput.setText(healthConcernsInfo);
        healthConcInput.setBorder(border);
        healthConcInput.setMinHeight(100);
        healthConcInput.setMinWidth(100);
        grid.add(healthConcInput, 1, 3);
        
        
        
        backBtn.setAlignment(Pos.CENTER);
        grid.setHalignment(backBtn, HPos.CENTER);
        grid.add(backBtn, 1, 7);
        
        layout.add(grid, 0, 1);
        
        patientSummaryScene = new Scene(layout, this.width, this.height);
        return patientSummaryScene;
        
    }
    
}
