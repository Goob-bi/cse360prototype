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
    private Scene patientSummaryScene, patientInfoScene;
    private JSONObject jo;
    private Visit visit;
    private InputValidation check = new InputValidation();
    //---------------Patient Menu-------------------
    PatientMenu(String ID) {
        this.patientID = ID;
        patient = new Patient(patientID);
        visit = new Visit(patientID);
        visit.setVisit(visit.getCurrentVisit());
        
        System.out.println("ID: " + patientID);
        String patientName = patient.getName();
        menu.setText("Hello, " + patientName);   
        this.setTitle("Main Menu");
        
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
        row++;
        grid.add(logoutBtn, column, row);
        
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
        String pID, fname, lname, email, phone, healthHis, insID, bDay;
        pID = fname = lname = email = phone = healthHis = insID = bDay = "";
        try {
            jo = patient.loadPatientFile();
            pID = jo.getString("patientID");
            fname = jo.getString("firstName");
            lname = jo.getString("lastName");
            email = jo.getString("email");
            phone = jo.getString("phone");
            healthHis = jo.getString("healthHist");
            insID = jo.getString("insID");
            bDay = jo.getString("birthday");

        } catch (JSONException e) {
            System.out.println("bad file");
        }

        menu.setText("Patient Intake Form");
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

        Label scenetitle = new Label("First Name:");
        scenetitle.setAlignment(Pos.CENTER);
        grid.add(scenetitle, 0, 1);
        TextField firstNameInput = new TextField ();
        firstNameInput.setText(fname);
        grid.add(firstNameInput, 1, 1);

        Label errorLabel = new Label();
        errorLabel.setAlignment(Pos.CENTER);
        layout.setHalignment(errorLabel, HPos.CENTER);
        errorLabel.setText("Error: Missing input");
        errorLabel.setVisible(false);
        layout.add(errorLabel, 0, 2);

        Label lastNameLbl = new Label("Last Name:");
        scenetitle.setAlignment(Pos.CENTER);
        grid.add(lastNameLbl, 0, 2);
        TextField lastNameInput = new TextField ();
        lastNameInput.setText(lname);
        grid.add(lastNameInput, 1, 2);

        Label emailLbl = new Label("Email:");
        scenetitle.setAlignment(Pos.CENTER);
        grid.add(emailLbl, 0, 3);
        TextField emailInput = new TextField ();
        emailInput.setText(email);
        grid.add(emailInput, 1, 3);

        Label phoneLbl = new Label("Phone Number:");
        scenetitle.setAlignment(Pos.CENTER);
        grid.add(phoneLbl, 0, 4);
        TextField phoneInput = new TextField ();
        phoneInput.setText(phone);
        grid.add(phoneInput, 1, 4);

        Label bDayLbl = new Label("Birthday (DDMMYYYY):");
        scenetitle.setAlignment(Pos.CENTER);
        grid.add(bDayLbl, 0, 5);
        TextField bDayInput = new TextField ();
        bDayInput.setText(bDay);
        grid.add(bDayInput, 1, 5);

        Label insurance = new Label("Insurance ID:");
        scenetitle.setAlignment(Pos.CENTER);
        grid.add(insurance, 0, 6);
        TextField insuranceInput = new TextField ();
        insuranceInput.setText(insID);
        grid.add(insuranceInput, 1, 6);

        Button saveBtn = SetupButton("Save");
        saveBtn.setMinWidth(100);

        grid.setHalignment(saveBtn, HPos.CENTER);
        grid.add(saveBtn, 2, 7);
        saveBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (firstNameInput.getText().isEmpty() || lastNameInput.getText().isEmpty() || emailInput.getText().isEmpty() ||
                        phoneInput.getText().isEmpty() || bDayInput.getText().isEmpty() || insuranceInput.getText().isEmpty()) {

                    System.out.println("missing input"); //debug
                    errorLabel.setText("Error: Missing input");
                    errorLabel.setVisible(true);
                } else {
                    //patientID is fistName, lastName, birthday
                    if (!check.DateCheck(bDayInput.getText()) || !check.NameCheck(firstNameInput.getText()) || !check.NameCheck(lastNameInput.getText())) {
                        System.out.println("Bad format input");
                        errorLabel.setVisible(true);
                        errorLabel.setText("Error: Bad input");
                        return;
                    }
                    //patient can edit these fields
                    //saveBtn.setDisable(true);
                    //firstNameInput.setDisable(true);
                    //lastNameInput.setDisable(true);
                    //emailInput.setDisable(true);
                    //phoneInput.setDisable(true);
                    //bDayInput.setDisable(true);
                    //insuranceInput.setDisable(true);

                    //need to only alter current patient file
                    //patientID = firstNameInput.getText() + lastNameInput.getText() + bDayInput.getText();
                    //System.out.println(patientID);  //debug

                    errorLabel.setText("PatientID: " + patientID);
                    errorLabel.setVisible(true);

                    patient = new Patient(patientID); //create patient
                    patient.savePatient(firstNameInput.getText(), lastNameInput.getText(), emailInput.getText(),
                            phoneInput.getText(), bDayInput.getText(), insuranceInput.getText(), bDayInput.getText());

                }
            }
        });

        backBtn.setAlignment(Pos.CENTER);
        grid.setHalignment(backBtn, HPos.CENTER);
        grid.add(backBtn, 1, 7);

        layout.add(grid, 0, 1);
        patientInfoScene = new Scene(layout, this.width, this.height);
        return patientInfoScene;
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
    //    jo = patient.loadPatientFile(patient.getHealthFile());
        jo = visit.loadVisit(visit.getHealthFile());
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
