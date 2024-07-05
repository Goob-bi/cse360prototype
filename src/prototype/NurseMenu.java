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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.WindowEvent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static javafx.scene.layout.GridPane.*;

/**
 *
 * @author andreas lees
 */
public class NurseMenu extends Menus {
    private Scene patientSummaryScene, patientScene, patientInfoScene;
    private JSONObject jo;
    private String visitNum;
    private Visit visit;
    private InputValidation check = new InputValidation();
    private Button backVisitBtn = SetupButton("Back");
    private StaffMessageMenu msgPortal;
    private String staffID = "";
    private String staffName = "";

    
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
    @Override
    protected void closeExtraWindow() {
        msgPortal.close();
    }
//--------------------------------------------------------------------  
    NurseMenu(String ID, String name) {
        this.staffID = ID;
        this.staffName = name;
        this.setTitle("Main Menu");
        
        GridPane layout = new GridPane();
        layout.setAlignment(Pos.CENTER);
        //layout.setGridLinesVisible(true);     //debug
        
        Label scenetitle = new Label("Welcome to " + companyName);
        setHalignment(scenetitle, HPos.CENTER);
        layout.add(scenetitle, 0, 0);

        this.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                msgPortal.close();
                System.out.println("bye bye");
            }
        });

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setHgap(5);
        grid.setVgap(15);
        layout.add(grid, 1, 1);
        
        Button pIntakeBtn = SetupButton("New Patient");
        Button patientBtn = SetupButton("Goto Visits");
        Button patientDelBtn = SetupButton("Delete Patient");
        Button messageBtn = SetupButton("Message");
        
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
        row++;
        grid.add(messageBtn, column, row);
        row++;
        grid.add(logoutBtn, column, row);
        
        
        pIntakeBtn.setOnAction(event -> {
                changeTitle("Patient Intake");
                changeScene(IntakeMenu());
        });
        patientBtn.setOnAction(event -> {
                patientID = list.getSelectionModel().getSelectedItem();
                if (patientID == null || patientID.isEmpty()) {
                    return;
                }
                patient = new Patient(patientID);
                visit = new Visit(patientID);
                changeTitle("Patient Health");
                changeScene(patientMenu());
        });
        patientDelBtn.setOnAction(event -> {
                patientID = list.getSelectionModel().getSelectedItem();
                if (patientID == null || patientID.isEmpty()) {
                    return;
                }
                patient = new Patient(patientID); //create patient
                patient.deletePatient();
                visit = null;
                updateList();
        });
        messageBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                msgPortal = new StaffMessageMenu(staffID, staffName);
                msgPortal.showMenu();
                //changeTitle("Patient Health");
                //changeScene(new MessageMenu().loginScene);


            }
        });
        backBtn.setOnAction(event -> {
            changeTitle("Main Menu");
            updateList();
            changeScene(loginScene);
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
        setHalignment(scenetitle, HPos.CENTER);
        layout.add(scenetitle, 0, 0);
        
        Label visitLabel = new Label("Latest visit: " + visit.getCurrentVisit());
        setHalignment(visitLabel, HPos.CENTER);
        layout.add(visitLabel, 1, 0);
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setHgap(5);
        grid.setVgap(15);
        layout.add(grid, 1, 1);
        
        Button vitalsViewBtn = SetupButton("Vitals");
        Button healthViewBtn = SetupButton("Health");
        Button patientSumBtn = SetupButton("Visit Summary");
        Button newVisitBtn = SetupButton("New Visit");
        Button patientInfoBtn = SetupButton("Patient Info");
        Button patientHistBtn = SetupButton("Patient History");
        
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
        grid.add(newVisitBtn, column, row);
        row++;
        grid.add(patientSumBtn, column, row);
        row++;
        grid.add(patientInfoBtn, column, row);
        row++;
        grid.add(patientHistBtn, column, row);
        row++;
        grid.add(backBtn, column, row);
        
        
        vitalsViewBtn.setOnAction(event -> {
                visitNum = visitList.getSelectionModel().getSelectedItem();
                if (visitNum == null || !check.IntCheck(visitNum)) {
                    return;
                }
                changeTitle("Patient Vitals");
                changeScene(VitalsMenu());
        });
        healthViewBtn.setOnAction(event -> {
                visitNum = visitList.getSelectionModel().getSelectedItem();
                if (visitNum == null || !check.IntCheck(visitNum)) {
                    return;
                }
                changeTitle("Patient Health");
                changeScene(HealthMenu());
        });
        patientSumBtn.setOnAction(event -> {
                visitNum = visitList.getSelectionModel().getSelectedItem();
                if (visitNum == null || !check.IntCheck(visitNum)) {
                    return;
                }
                visit.setVisit(Integer.parseInt(visitNum));
                changeTitle("Patient Health");
                changeScene(SummaryMenu());
        });
        patientHistBtn.setOnAction(event -> {
            changeTitle("Patient History");
            changeScene(HistoryMenu());
        });
        newVisitBtn.setOnAction(event -> {
                visit.incrementVisit();
                updateVisitList();
                visitLabel.setText("Latest visit: " + visit.getCurrentVisit());
        });
        patientInfoBtn.setOnAction(event -> {
            changeTitle("Patient Info");
            changeScene(InfoMenu());
        });
        
        backVisitBtn.setOnAction(event -> {
            changeTitle("Main Menu");
            updateVisitList();
            changeScene(patientScene);
        });
        patientScene = new Scene(layout, width, height);
        return patientScene;
    }
//--------------------------------------------------------------------  
    private Scene IntakeMenu() {
        menu.setText("Patient Intake Form");        
        GridPane layout = new GridPane();
        menu.setAlignment(Pos.CENTER);
        setHalignment(menu, HPos.CENTER);
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
        setHalignment(errorLabel, HPos.CENTER);
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
        
        setHalignment(saveBtn, HPos.CENTER);
        grid.add(saveBtn, 2, 7);
        saveBtn.setOnAction(event -> {
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
        });
        
        backBtn.setAlignment(Pos.CENTER);
        setHalignment(backBtn, HPos.CENTER);
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
        setHalignment(menu, HPos.CENTER);
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
        setHalignment(errorLabel, HPos.CENTER);
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
        rb.setOnAction(event -> {
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
        });
        setHalignment(rb, HPos.LEFT);
        grid.add(rb, 0, 7);
        
        Button saveBtn = SetupButton("Save");
        saveBtn.setMinWidth(100);
        
        setHalignment(saveBtn, HPos.CENTER);
        grid.add(saveBtn, 2, 7);
        saveBtn.setOnAction(event -> {
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
        });
        
        backVisitBtn.setAlignment(Pos.CENTER);
        setHalignment(backVisitBtn, HPos.CENTER);
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
        setHalignment(menu, HPos.CENTER);
        layout.setAlignment(Pos.CENTER);
        layout.add(menu, 0, 0);
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        Label errorLabel = new Label();  
        errorLabel.setAlignment(Pos.CENTER);
        setHalignment(errorLabel, HPos.CENTER);
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
        
        setHalignment(saveBtn, HPos.CENTER);
        grid.add(saveBtn, 2, 7);
        saveBtn.setOnAction(event -> {
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
        });
        
        backVisitBtn.setAlignment(Pos.CENTER);
        setHalignment(backVisitBtn, HPos.CENTER);
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
        setHalignment(menu, HPos.CENTER);
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
        setHalignment(errorLabel, HPos.CENTER);
        errorLabel.setText("Error: Missing input");
        errorLabel.setVisible(false);
        layout.add(errorLabel, 0, 2);

        errorLabel.setText("PatientID: " + patientID);
        errorLabel.setVisible(true);

        //load health file
        jo = visit.loadVisit(visit.getHealthFile());
        //System.out.println(jo.toString());
        String allergiesInfo = "";
        String healthConcernsInfo = "";
        try {
            allergiesInfo = jo.getString("allergies");
            healthConcernsInfo = jo.getString("healthConc");

        } catch (JSONException e) {
            System.out.println("no data");  //debug
        }
        column = 0;
        row = 0;
        Label allergies = new Label("Allergies:");
        grid.add(allergies, column, row);
        Label allergiesInput = new Label();
        allergiesInput.setText(allergiesInfo);
        allergiesInput.setBorder(border);
        allergiesInput.setMinHeight(100);
        allergiesInput.setMinWidth(200);
        row++;
        grid.add(allergiesInput, column, row);

        Label healthConc = new Label("Health Concerns:");
        row++;
        grid.add(healthConc, column, row);
        Label healthConcInput = new Label();
        healthConcInput.setText(healthConcernsInfo);
        healthConcInput.setBorder(border);
        healthConcInput.setMinHeight(100);
        healthConcInput.setMinWidth(200);
        row++;
        grid.add(healthConcInput, column, row);

        //load physical file
        jo = visit.loadVisit(visit.getPhysicalFile());
        //System.out.println(jo.toString());
        String physicalInfo = "";
        String physicalConcernsInfo = "";
        try {
            physicalInfo = jo.getString("phyRes");
            physicalConcernsInfo = jo.getString("phyConcerns");

        } catch (JSONException e) {
            System.out.println("no data");  //debug
        }
        column++;
        row = 0;
        Label physLbl = new Label("Physical Results:");
        grid.add(physLbl, column, row);
        Label physLblInput = new Label();
        physLblInput.setText(physicalInfo);
        physLblInput.setBorder(border);
        physLblInput.setMinHeight(100);
        physLblInput.setMinWidth(200);
        row++;
        grid.add(physLblInput, column, row);

        Label physConcLbl = new Label("Physical Concerns:");
        row++;
        grid.add(physConcLbl, column, row);
        Label physConcLblInput = new Label();
        physConcLblInput.setText(physicalConcernsInfo);
        physConcLblInput.setBorder(border);
        physConcLblInput.setMinHeight(100);
        physConcLblInput.setMinWidth(200);
        row++;
        grid.add(physConcLblInput, column, row);

        //load medication file
        jo = visit.loadVisit(visit.getMedFile());
        //System.out.println(jo.toString());
        String immunizationInfo = "";
        String prescriptionInfo = "";
        try {
            immunizationInfo = jo.getString("immunization");
            prescriptionInfo = jo.getString("perscription");

        } catch (JSONException e) {
            System.out.println("no data");  //debug
        }
        column++;
        row = 0;
        Label immmunLbl = new Label("Physical Results:");
        grid.add(immmunLbl, column, row);
        Label immmunLblInput = new Label();
        immmunLblInput.setText(immunizationInfo);
        immmunLblInput.setBorder(border);
        immmunLblInput.setMinHeight(100);
        immmunLblInput.setMinWidth(200);
        row++;
        grid.add(immmunLblInput, column, row);

        Label medLbl = new Label("Physical Concerns:");
        row++;
        grid.add(medLbl, column, row);
        Label medLblInput = new Label();
        medLblInput.setText(prescriptionInfo);
        medLblInput.setBorder(border);
        medLblInput.setMinHeight(100);
        medLblInput.setMinWidth(200);
        row++;
        grid.add(medLblInput, column, row);

        //load vitals file
        jo = visit.loadVisit(visit.getVitalFile());
        //System.out.println(jo.toString());

        String over12 =  "";
        String weight = "";
        String pHeight = "";
        String bodyTemp = "";
        String bloodP = "";
        try {
            over12 = jo.getString("over12");
            weight =  jo.getString("weight");
            pHeight = jo.getString("height");
            bodyTemp = jo.getString("temp");
            bloodP = jo.getString("bp");

        } catch (JSONException e) {
            System.out.println("no data");  //debug
        }
        column++;
        row = 0;
        Label vitalLbl = new Label("Vitals:");
        grid.add(vitalLbl, column, row);
        Label vitalLblInput = new Label();
        vitalLblInput.setText(
                "Over 12: " + over12 + "\n" +
                "Weight: " + weight + "\n" +
                "Height: " + pHeight + "\n" +
                "Body Temp: " + bodyTemp + "\n" +
                "Blood Pressure: " + bloodP);
        vitalLblInput.setBorder(border);
        vitalLblInput.setMinHeight(100);
        vitalLblInput.setMinWidth(200);
        row++;
        grid.add(vitalLblInput, column, row);



        backVisitBtn.setAlignment(Pos.CENTER);
        setHalignment(backVisitBtn, HPos.CENTER);
        layout.add(backVisitBtn, 0, 2);

        layout.add(grid, 0, 1);

        patientSummaryScene = new Scene(layout, this.width + 100, this.height);
        return patientSummaryScene;

    }
    private Scene InfoMenu() {
        //populate with contact info
        String pID, fname, lname, email, phone, healthHis, insID, bDay;
        pID = fname = lname = email = phone = healthHis = insID = bDay = "";
        try {
            jo = patient.loadPatientFile();
            //pID = jo.getString("patientID");
            fname = jo.getString("firstName");
            lname = jo.getString("lastName");
            email = jo.getString("email");
            phone = jo.getString("phone");
            //healthHis = jo.getString("healthHist");
            insID = jo.getString("insID");
            bDay = jo.getString("birthday");

        } catch (JSONException e) {
            System.out.println("bad file");
        }

        menu.setText("Patient Intake Form");
        GridPane layout = new GridPane();
        menu.setAlignment(Pos.CENTER);
        setHalignment(menu, HPos.CENTER);
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
        Label firstNameInput = new Label ();
        firstNameInput.setText(fname);
        grid.add(firstNameInput, 1, 1);

        Label errorLabel = new Label();
        errorLabel.setAlignment(Pos.CENTER);
        setHalignment(errorLabel, HPos.CENTER);
        errorLabel.setText("Error: Missing input");
        errorLabel.setVisible(false);
        layout.add(errorLabel, 0, 2);

        Label lastNameLbl = new Label("Last Name:");
        scenetitle.setAlignment(Pos.CENTER);
        grid.add(lastNameLbl, 0, 2);
        Label lastNameInput = new Label ();
        lastNameInput.setText(lname);
        grid.add(lastNameInput, 1, 2);

        Label emailLbl = new Label("Email:");
        scenetitle.setAlignment(Pos.CENTER);
        grid.add(emailLbl, 0, 3);
        Label emailInput = new Label ();
        emailInput.setText(email);
        grid.add(emailInput, 1, 3);

        Label phoneLbl = new Label("Phone Number:");
        scenetitle.setAlignment(Pos.CENTER);
        grid.add(phoneLbl, 0, 4);
        Label phoneInput = new Label ();
        phoneInput.setText(phone);
        grid.add(phoneInput, 1, 4);

        Label bDayLbl = new Label("Birthday (DDMMYYYY):");
        scenetitle.setAlignment(Pos.CENTER);
        grid.add(bDayLbl, 0, 5);
        Label bDayInput = new Label ();
        bDayInput.setText(bDay);
        grid.add(bDayInput, 1, 5);

        Label insurance = new Label("Insurance ID:");
        scenetitle.setAlignment(Pos.CENTER);
        grid.add(insurance, 0, 6);
        Label insuranceInput = new Label ();
        insuranceInput.setText(insID);
        grid.add(insuranceInput, 1, 6);


        backVisitBtn.setAlignment(Pos.CENTER);
        setHalignment(backVisitBtn, HPos.CENTER);
        grid.add(backVisitBtn, 1, 7);

        layout.add(grid, 0, 1);
        patientInfoScene = new Scene(layout, this.width, this.height);
        return patientInfoScene;
    }

    //--------------------------------------------------------------------
    private Scene HistoryMenu() {

        menu.setText("Patient Summary");
        GridPane layout = new GridPane();
        menu.setAlignment(Pos.CENTER);
        setHalignment(menu, HPos.CENTER);
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
        setHalignment(errorLabel, HPos.CENTER);
        errorLabel.setText("Error: Missing input");
        errorLabel.setVisible(false);
        layout.add(errorLabel, 0, 2);

        errorLabel.setText("PatientID: " + patientID);
        errorLabel.setVisible(true);

        //load health file
        jo = visit.loadVisitHistory();
        JSONArray ja = new JSONArray();
        //jo = visit.loadVisit(visit.getPhysicalFile());
        //System.out.println(jo.toString());
        String allergiesInfo = "";
        String healthConcernsInfo = "";
        try {
            ja = jo.getJSONArray("allergies");
            for(int i=0; i < ja.length(); i++) {
                allergiesInfo = allergiesInfo.concat(ja.getString(i) + "\n");
            }
            ja = jo.getJSONArray("healthConc");
            for(int i=0; i < ja.length(); i++) {
                healthConcernsInfo = healthConcernsInfo.concat(ja.getString(i) + "\n");
            }

        } catch (JSONException e) {
            System.out.println("no data");  //debug
        }
        column = 0;
        row = 0;
        Label allergies = new Label("Allergies:");
        grid.add(allergies, column, row);
        Label allergiesInput = new Label();
        allergiesInput.setText(allergiesInfo);
        allergiesInput.setBorder(border);
        allergiesInput.setMinHeight(100);
        allergiesInput.setMinWidth(200);
        row++;
        grid.add(allergiesInput, column, row);

        Label healthConc = new Label("Health Concerns:");
        row++;
        grid.add(healthConc, column, row);
        Label healthConcInput = new Label();
        healthConcInput.setText(healthConcernsInfo);
        healthConcInput.setBorder(border);
        healthConcInput.setMinHeight(100);
        healthConcInput.setMinWidth(200);
        row++;
        grid.add(healthConcInput, column, row);

        //load physical file
        //jo = visit.loadVisit(visit.getPhysicalFile());
        //System.out.println(jo.toString());
        String physicalInfo = "";
        String physicalConcernsInfo = "";
        try {
            ja = jo.getJSONArray("phyRes");
            for(int i=0; i < ja.length(); i++) {
                physicalInfo = physicalInfo.concat(ja.getString(i) + "\n");
            }
            ja = jo.getJSONArray("phyConcerns");
            for(int i=0; i < ja.length(); i++) {
                physicalConcernsInfo = physicalConcernsInfo.concat(ja.getString(i) + "\n");
            }

        } catch (JSONException e) {
            System.out.println("no data");  //debug
        }
        column++;
        row = 0;
        Label physLbl = new Label("Physical Results:");
        grid.add(physLbl, column, row);
        Label physLblInput = new Label();
        physLblInput.setText(physicalInfo);
        physLblInput.setBorder(border);
        physLblInput.setMinHeight(100);
        physLblInput.setMinWidth(200);
        row++;
        grid.add(physLblInput, column, row);

        Label physConcLbl = new Label("Physical Concerns:");
        row++;
        grid.add(physConcLbl, column, row);
        Label physConcLblInput = new Label();
        physConcLblInput.setText(physicalConcernsInfo);
        physConcLblInput.setBorder(border);
        physConcLblInput.setMinHeight(100);
        physConcLblInput.setMinWidth(200);
        row++;
        grid.add(physConcLblInput, column, row);

        //load medication file
        //jo = visit.loadVisit(visit.getMedFile());
        //System.out.println(jo.toString());
        String immunizationInfo = "";
        String prescriptionInfo = "";
        try {
            ja = jo.getJSONArray("immunization");
            for(int i=0; i < ja.length(); i++) {
                immunizationInfo = immunizationInfo.concat(ja.getString(i) + "\n");
            }
            ja = jo.getJSONArray("perscription");
            for(int i=0; i < ja.length(); i++) {
                prescriptionInfo = prescriptionInfo.concat(ja.getString(i) + "\n");
            }

        } catch (JSONException e) {
            System.out.println("no data");  //debug
        }
        column++;
        row = 0;
        Label immmunLbl = new Label("Immunizations:");
        grid.add(immmunLbl, column, row);
        Label immmunLblInput = new Label();
        immmunLblInput.setText(immunizationInfo);
        immmunLblInput.setBorder(border);
        immmunLblInput.setMinHeight(100);
        immmunLblInput.setMinWidth(200);
        row++;
        grid.add(immmunLblInput, column, row);

        Label medLbl = new Label("Perscriptions:");
        row++;
        grid.add(medLbl, column, row);
        Label medLblInput = new Label();
        medLblInput.setText(prescriptionInfo);
        medLblInput.setBorder(border);
        medLblInput.setMinHeight(100);
        medLblInput.setMinWidth(200);
        row++;
        grid.add(medLblInput, column, row);




        backVisitBtn.setAlignment(Pos.CENTER);
        setHalignment(backVisitBtn, HPos.CENTER);
        layout.add(backVisitBtn, 0, 2);

        layout.add(grid, 0, 1);

        patientSummaryScene = new Scene(layout, this.width + 100, this.height);
        return patientSummaryScene;

    }
}
