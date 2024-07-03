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
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author andreas lees
 */
public class NurseMenu extends Menus {
    private Scene patientSummaryScene, patientScene;
    private JSONObject jo;
    private String visitNum;
    private Visit visit;
    private InputValidation check = new InputValidation();
    private Button backVisitBtn = SetupButton("Back");
    
//--------------------------------------------------------------------  
    private void updateList() {
        //collect list of patients
        patient = new Patient();
        ObservableList<String> items = FXCollections.observableArrayList (patient.getPatientList());
        list.setItems(items);
        
    }
    private void updateVisitList() {
        ObservableList<String> items = FXCollections.observableArrayList (visit.getVisitList());
        visitList.setItems(items);        
    }
//--------------------------------------------------------------------  
    NurseMenu() {
        this.setTitle("Main Menu");
        
        GridPane layout = new GridPane();
        layout.setAlignment(Pos.CENTER);
        //layout.setGridLinesVisible(true);     //debug
        
        Label scenetitle = new Label("Welcome to " + companyName);
        layout.setHalignment(scenetitle, HPos.CENTER);
        layout.add(scenetitle, 0, 0);
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setHgap(5);
        grid.setVgap(15);
        layout.add(grid, 1, 1);
        
        Button pIntakeBtn = SetupButton("New Patient");
        Button patientBtn = SetupButton("Goto Visits");
        Button patientDelBtn = SetupButton("Delete Patient");
        
        //collect list of patients
        updateList();
        list.setPrefWidth(200);
        list.setPrefHeight(300);
        layout.add(list, 0, 1);
        
        column= 0;
        row = 0;
        grid.add(pIntakeBtn, column, row); 
        row++;
        grid.add(patientBtn, column, row);
        row++;
        grid.add(patientDelBtn, column, row);
        
        
        pIntakeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    changeTitle("Patient Intake");
                    changeScene(IntakeMenu());
            }
            
        });
        patientBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    patientID = list.getSelectionModel().getSelectedItem();
                    if (patientID == null || patientID.isEmpty()) {
                        return;
                    }
                    visit = new Visit(patientID);
                    changeTitle("Patient Health");
                    changeScene(patientMenu());
            }
        });
        patientDelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    patientID = list.getSelectionModel().getSelectedItem();
                    if (patientID == null || patientID.isEmpty()) {
                        return;
                    }
                    patient = new Patient(patientID); //create patient
                    patient.deletePatient();
                    visit = null;
                    updateList();
            }
        });
        backBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                changeTitle("Main Menu");
                updateList();
                changeScene(loginScene);
            }
        });
        loginScene = new Scene(layout, width, height);
        this.setScene(loginScene);
        this.show();
    }
//--------------------------------------------------------------------  
    private Scene patientMenu() {
        this.setTitle("Patient Menu");
               
        GridPane layout = new GridPane();
        layout.setAlignment(Pos.CENTER);
        //layout.setGridLinesVisible(true);     //debug
        
        Label scenetitle = new Label("Visits:");
        layout.setHalignment(scenetitle, HPos.CENTER);
        layout.add(scenetitle, 0, 0);
        
        Label visitLabel = new Label("Latest visit: " + visit.getCurrentVisit());
        layout.setHalignment(visitLabel, HPos.CENTER);
        layout.add(visitLabel, 1, 0);
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setHgap(5);
        grid.setVgap(15);
        layout.add(grid, 1, 1);
        
        Button vitalsViewBtn = SetupButton("Vitals");
        Button healthViewBtn = SetupButton("Health");
        Button patientSumBtn = SetupButton("Patient Summary");
        Button newVisitBtn = SetupButton("New Visit");
        
        //collect list of visits
        updateVisitList();
        visitList.setPrefWidth(200);
        visitList.setPrefHeight(300);
        layout.add(visitList, 0, 1);
        
        column = 0;
        row = 0;
        grid.add(vitalsViewBtn, column, row);
        row++;
        grid.add(healthViewBtn, column, row);
        row++;
        grid.add(patientSumBtn, column, row);
        row++;
        grid.add(newVisitBtn, column, row);
        row++;
        grid.add(backBtn, column, row);
        
        
        vitalsViewBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    visitNum = visitList.getSelectionModel().getSelectedItem();
                    if (visitNum == null || !check.IntCheck(visitNum)) {
                        return;
                    }
                    changeTitle("Patient Vitals");
                    changeScene(VitalsMenu());
            }
        });
        healthViewBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    visitNum = visitList.getSelectionModel().getSelectedItem();
                    if (visitNum == null || !check.IntCheck(visitNum)) {
                        return;
                    }
                    changeTitle("Patient Health");
                    changeScene(HealthMenu());
            }
        });
        patientSumBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    visitNum = visitList.getSelectionModel().getSelectedItem();
                    if (visitNum == null || !check.IntCheck(visitNum)) {
                        return;
                    }
                    visit.setVisit(Integer.parseInt(visitNum));
                    changeTitle("Patient Health");
                    changeScene(SummaryMenu()); 
            }
        });
        newVisitBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    visit.incrementVisit();
                    updateVisitList();
                    visitLabel.setText("Latest visit: " + visit.getCurrentVisit());
            }
        });
        
        backVisitBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                changeTitle("Main Menu");
                updateVisitList();
                changeScene(patientScene);
            }
        });
        patientScene = new Scene(layout, width, height);
        return patientScene;
    }
//--------------------------------------------------------------------  
    private Scene IntakeMenu() {
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
        firstNameInput.setText("");
        grid.add(firstNameInput, 1, 1);
        
        Label errorLabel = new Label();  
        errorLabel.setAlignment(Pos.CENTER);
        layout.setHalignment(errorLabel, HPos.CENTER);
        errorLabel.setText("Error: Missing input"); 
        errorLabel.setVisible(false);
        layout.add(errorLabel, 0, 2);
        
        Label lastName = new Label("Last Name:");
        scenetitle.setAlignment(Pos.CENTER);
        grid.add(lastName, 0, 2);
        TextField lastNameInput = new TextField ();
        lastNameInput.setText("");
        grid.add(lastNameInput, 1, 2);
        
        Label email = new Label("Email:");
        scenetitle.setAlignment(Pos.CENTER);
        grid.add(email, 0, 3);
        TextField emailInput = new TextField ();
        emailInput.setText("");
        grid.add(emailInput, 1, 3);
        
        Label phone = new Label("Phone Number:");
        scenetitle.setAlignment(Pos.CENTER);
        grid.add(phone, 0, 4);
        TextField phoneInput = new TextField ();
        phoneInput.setText("");
        grid.add(phoneInput, 1, 4);
        
        Label bDay = new Label("Birthday (DDMMYYYY):");
        scenetitle.setAlignment(Pos.CENTER);
        grid.add(bDay, 0, 5);
        TextField bDayInput = new TextField ();
        bDayInput.setText("");
        grid.add(bDayInput, 1, 5);
        
        Label insurance = new Label("Insurance ID:");
        scenetitle.setAlignment(Pos.CENTER);
        grid.add(insurance, 0, 6);
        TextField insuranceInput = new TextField ();
        insuranceInput.setText("");
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
                    saveBtn.setDisable(true);
                    firstNameInput.setDisable(true);
                    lastNameInput.setDisable(true);
                    emailInput.setDisable(true);
                    phoneInput.setDisable(true);
                    bDayInput.setDisable(true);
                    insuranceInput.setDisable(true);
                    patientID = firstNameInput.getText() + lastNameInput.getText() + bDayInput.getText();
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
        
        patientIntakeScene = new Scene(layout, width, height);
        return patientIntakeScene;

    }    
//--------------------------------------------------------------------  
    private Scene VitalsMenu() {
        menu.setText("Patient Vitals");        
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
        
        Label weight = new Label("Weight:");
        grid.add(weight, 0, 2);
        TextField weightInput = new TextField ();
        weightInput.setText("");
        grid.add(weightInput, 1, 2);
        
        Label height = new Label("Height:");
        grid.add(height, 0, 3);
        TextField heightInput = new TextField ();
        heightInput.setText("");
        grid.add(heightInput, 1, 3);
        
        Label bodyTemp = new Label("Body Temp:");
        grid.add(bodyTemp, 0, 4);
        TextField bodyTempInput = new TextField ();
        bodyTempInput.setText("");
        grid.add(bodyTempInput, 1, 4);
        
        Label bloodP = new Label("Blood Pressure:");
        grid.add(bloodP, 0, 5);
        TextField bloodPInput = new TextField ();
        bloodPInput.setText("");
        grid.add(bloodPInput, 1, 5);

        RadioButton  rb = new RadioButton ();
        rb.setText("Over 12?");
        rb.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (rb.isSelected()) {
                    weightInput.setDisable(true);
                    heightInput.setDisable(true);
                    bodyTempInput.setDisable(true);
                    bloodPInput.setDisable(true);
                } else {
                    weightInput.setDisable(false);
                    heightInput.setDisable(false);
                    bodyTempInput.setDisable(false);
                    bloodPInput.setDisable(false);
                }
            }
        });
        grid.setHalignment(rb, HPos.LEFT);
        grid.add(rb, 0, 7);
        
        Button saveBtn = SetupButton("Save");
        saveBtn.setMinWidth(100);
        
        grid.setHalignment(saveBtn, HPos.CENTER);
        grid.add(saveBtn, 2, 7);
        saveBtn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                if (!rb.isSelected() && (weightInput.getText().isEmpty() || heightInput.getText().isEmpty() || 
                        bodyTempInput.getText().isEmpty() || bloodPInput.getText().isEmpty()) ) {
                    
                    System.out.println("missing input"); //debug
                    errorLabel.setText("Error: Missing input"); 
                    errorLabel.setVisible(true);
                } else {
                    saveBtn.setDisable(true);
                    //System.out.println(patientID);  //debug
                    if (patientID.isEmpty()) {
                        System.out.println("no ID");  //debug
                        errorLabel.setText("Error: No ID"); 
                        errorLabel.setVisible(true);
                        return;
                    }
                    
                    errorLabel.setText("PatientID: " + patientID); 
                    errorLabel.setVisible(true);
                    visit.setVisit(visit.getCurrentVisit());
                    if (rb.isSelected()) {
                        visit.saveVisitVitals("no", "n/a", "n/a", 
                                "n/a", "n/a");
                    } else {
                        visit.saveVisitVitals("yes", weightInput.getText(), heightInput.getText(), 
                                bodyTempInput.getText(), bloodPInput.getText());
                    }
                }
            }
        });
        
        backVisitBtn.setAlignment(Pos.CENTER);
        grid.setHalignment(backVisitBtn, HPos.CENTER);
        grid.add(backVisitBtn, 1, 7);
        
        layout.add(grid, 0, 1);
        
        patientIntakeScene = new Scene(layout, this.width, this.height);
        return patientIntakeScene;

    }    
//--------------------------------------------------------------------  
    private Scene HealthMenu() {
        menu.setText("Patient Health");        
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
        
        Label errorLabel = new Label();  
        errorLabel.setAlignment(Pos.CENTER);
        layout.setHalignment(errorLabel, HPos.CENTER);
        errorLabel.setText("Error: Missing input"); 
        errorLabel.setVisible(false);
        layout.add(errorLabel, 0, 2);
        
        Label allergies = new Label("Allergies:");
        grid.add(allergies, 0, 2);
        TextField allergiesInput = new TextField ();
        allergiesInput.setText("");
        grid.add(allergiesInput, 1, 2);
        
        Label healthConc = new Label("Health Concerns:");
        grid.add(healthConc, 0, 3);
        TextField healthConcInput = new TextField ();
        healthConcInput.setText("");
        grid.add(healthConcInput, 1, 3);
        
        Button saveBtn = SetupButton("Save");
        saveBtn.setMinWidth(100);
        
        grid.setHalignment(saveBtn, HPos.CENTER);
        grid.add(saveBtn, 2, 7);
        saveBtn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                if (allergiesInput.getText().isEmpty() || healthConcInput.getText().isEmpty()) {
                    
                    System.out.println("missing input"); //debug
                    errorLabel.setText("Error: Missing input"); 
                    errorLabel.setVisible(true);
                } else {
                    saveBtn.setDisable(true);
                    //System.out.println(patientID);  //debug
                    if (patientID.isEmpty()) {
                        System.out.println("no ID");  //debug
                        errorLabel.setText("Error: No ID"); 
                        errorLabel.setVisible(true);
                        return;
                    }
                    
                    errorLabel.setText("PatientID: " + patientID); 
                    errorLabel.setVisible(true);
                    visit.setVisit(visit.getCurrentVisit());
                        visit.saveVisitHealth(allergiesInput.getText(), healthConcInput.getText());
                    
                }
            }
        });
        
        backVisitBtn.setAlignment(Pos.CENTER);
        grid.setHalignment(backVisitBtn, HPos.CENTER);
        grid.add(backVisitBtn, 1, 7);
        
        layout.add(grid, 0, 1);
        
        patientIntakeScene = new Scene(layout, this.width, this.height);
        return patientIntakeScene;

    }    
//--------------------------------------------------------------------  
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
      
            errorLabel.setText("PatientID: " + patientID); 
            errorLabel.setVisible(true);
            jo = visit.loadVisit(visit.getHealthFile());
            System.out.println(jo.toString());
                    
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
        grid.add(allergiesInput, 1, 2);
        
        Label healthConc = new Label("Health Concerns:");
        grid.add(healthConc, 0, 3);
        Label healthConcInput = new Label();
        healthConcInput.setText(healthConcernsInfo);
        healthConcInput.setBorder(border);
        healthConcInput.setMinHeight(100);
        healthConcInput.setMinWidth(100);
        grid.add(healthConcInput, 1, 3);
        
        backVisitBtn.setAlignment(Pos.CENTER);
        grid.setHalignment(backVisitBtn, HPos.CENTER);
        grid.add(backVisitBtn, 1, 7);
        
        layout.add(grid, 0, 1);
        
        patientSummaryScene = new Scene(layout, this.width, this.height);
        return patientSummaryScene;
        
    }
    
}
