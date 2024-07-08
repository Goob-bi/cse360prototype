/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype.menus;

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
import javafx.stage.WindowEvent;
import org.json.JSONException;
import org.json.JSONObject;
import prototype.data.InputValidation;
import prototype.obj.Patient;
import prototype.obj.Visit;

import static javafx.scene.layout.GridPane.setHalignment;

/**
 *
 * @author andreas lees
 */
public class PatientMenu extends Menus {
    private MessageMenu msgPortal;

    PatientMenu(String ID, String path) {
        WORKINGPATH = path;
        this.patientID = ID;
        patient = new Patient(patientID, WORKINGPATH);
        patient.loadPatientFile();
        visit = new Visit(patientID, WORKINGPATH);


        msgPortal = new MessageMenu(patientID, patient.getFirstName(), WORKINGPATH);
        this.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                msgPortal.close();
                System.out.println("[Info] bye bye");
            }
        });
        
        //System.out.println("ID: " + patientID);
        String patientName = patient.getName();
        menu.setText("Hello, " + patientName);   
        this.setTitle("Main Menu");
        
//---------------------grid-----------------------------------------------         
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
        //grid.setGridLinesVisible(true);       //debug
        
        layout.add(grid, 0, 1);

        Button pInfoBtn = SetupButton("Contact Info");
        Button patientSumBtn = SetupButton("Visits");
        Button messageBtn = SetupButton("Message");

        column= 0;
        grid.add(pInfoBtn, column, row); 
        row++;
        grid.add(patientSumBtn, column, row);
        row++;
        grid.add(messageBtn, column, row);
        row++;
        grid.add(logoutBtn, column, row);
        
        pInfoBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                changeTitle("Contact Information");
                changeScene(InfoMenu());
            }
            
        });
        patientSumBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (patientID == null || patientID.isEmpty()) {
                    return;
                }
                visit.checkMissingData();
                changeTitle("Patient Health");
                changeScene(patientVisitMenu());
                    
            }
        });
        messageBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (patientID == null) {
                    patientID = "";
                }
                msgPortal.showMenu();
            }
        });
        backBtn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                changeTitle("Main Menu");
                changeScene(loginScene);
            }
        });
        //
        backVisitBtn.setOnAction(new EventHandler<ActionEvent>() {

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
    protected Scene patientVisitMenu() {
        this.setTitle("Patient Visit Menu");

        GridPane layout = new GridPane();
        layout.setAlignment(Pos.CENTER);
        //layout.setGridLinesVisible(true);     //debug

        Label scenetitle = SetupTitleLabel("Visits:");
        layout.add(scenetitle, 0, 0);

        Label visitLabel = SetupTitleLabel("Latest visit: " + visit.getCurrentVisit());
        layout.add(visitLabel, 1, 0);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setHgap(5);
        grid.setVgap(15);
        layout.add(grid, 1, 1);

        //Button physicalViewBtn = SetupButton("Enter Physical Data");
        //Button medViewBtn = SetupButton("Enter Medications");
        Button patientSumBtn = SetupButton("Visit Summary");
        //Button newVisitBtn = SetupButton("New Visit");
        //Button patientInfoBtn = SetupButton("Patient Info");
        Button patientHistBtn = SetupButton("Visit History");

        //collect list of visits
        updateVisitList();
        visitList.setPrefWidth(200);
        visitList.setPrefHeight(300);
        layout.add(visitList, 0, 1);

        column = 0;
        row = 0;
        //grid.add(physicalViewBtn, column, row);
        row++;
        //grid.add(medViewBtn, column, row);
        row++;
        row++;
        grid.add(patientSumBtn, column, row);
        row++;
        grid.add(patientHistBtn, column, row);
        row++;
        //grid.add(patientInfoBtn, column, row);
        row++;
        grid.add(backBtn, column, row);


        patientSumBtn.setOnAction(event -> {
            visitNum = visitList.getSelectionModel().getSelectedItem();
            if (visitNum == null || !check.IntCheck(visitNum)) {
                return;
            }
            visit.setVisit(Integer.parseInt(visitNum));
            System.out.println("[Info] loading visit #" + visitNum);
            changeTitle("Patient Health");
            changeScene(SummaryMenu());
        });
        patientHistBtn.setOnAction(event -> {
            changeTitle("Patient History");
            changeScene(HistoryMenu());
        });

        backVisitBtn.setOnAction(event -> {
            changeTitle("Main Menu");
            updateVisitList();
            changeScene(patientScene);
        });
        patientScene = new Scene(layout, width, height);
        return patientScene;
    }
    protected Scene HistoryMenu() {

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

        Label errorLabel = SetupTitleLabel("PatientID: " + patientID);
        layout.add(errorLabel, 0, 2);


        //load health file
        jo = visit.loadVisitHistory();
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
            System.out.println("[Error] Loading Health History: No data");  //debug
        }
        column = 0;
        row = 0;
        Label allergies = SetupTitleLabel("Allergies:");
        grid.add(allergies, column, row);
        Label allergiesInput = SetupDataLabel(allergiesInfo);
        row++;
        grid.add(allergiesInput, column, row);

        Label healthConc = SetupTitleLabel("Health Concerns:");
        row++;
        grid.add(healthConc, column, row);
        Label healthConcInput = SetupDataLabel(healthConcernsInfo);
        row++;
        grid.add(healthConcInput, column, row);

        //load physical file
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
            System.out.println("[Error] Loading Physical History: No data");  //debug
        }
        column++;
        row = 0;
        Label physLbl = SetupTitleLabel("Physical Results:");
        grid.add(physLbl, column, row);
        Label physLblInput = SetupDataLabel(physicalInfo);
        row++;
        grid.add(physLblInput, column, row);

        Label physConcLbl = SetupTitleLabel("Physical Concerns:");
        row++;
        grid.add(physConcLbl, column, row);
        Label physConcLblInput = SetupDataLabel(physicalConcernsInfo);
        row++;
        grid.add(physConcLblInput, column, row);

        //load medication file
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
            System.out.println("[Error] Loading Medication History: No data");  //debug
        }
        column++;
        row = 0;
        Label immmunLbl = SetupTitleLabel("Immunizations:");
        grid.add(immmunLbl, column, row);
        Label immmunLblInput = SetupDataLabel(immunizationInfo);
        row++;
        grid.add(immmunLblInput, column, row);

        Label medLbl = SetupTitleLabel("Perscriptions:");
        row++;
        grid.add(medLbl, column, row);
        Label medLblInput = SetupDataLabel(prescriptionInfo);
        row++;
        grid.add(medLblInput, column, row);

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
            pID = jo.getString("patientID");
            fname = jo.getString("firstName");
            lname = jo.getString("lastName");
            email = jo.getString("email");
            phone = jo.getString("phone");
            healthHis = jo.getString("healthHist");
            insID = jo.getString("insID");
            bDay = jo.getString("birthday");

        } catch (JSONException e) {
            System.out.println("[Error] Bad file");
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

        Label bDayLbl = new Label("Birthday (DD/MM/YYYY):");
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

                    System.out.println("[Error] Missing input"); //debug
                    errorLabel.setText("Error: Missing input");
                    errorLabel.setVisible(true);
                } else {
                    //patientID is fistName, lastName, birthday
                    if (!check.DateCheck(bDayInput.getText()) || !check.NameCheck(firstNameInput.getText()) || !check.NameCheck(lastNameInput.getText())) {
                        System.out.println("[Error] Bad format input");
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

                    patient = new Patient(patientID, WORKINGPATH); //create patient
                    patient.setWorkingPath(WORKINGPATH);
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
    protected Scene SummaryMenu() {

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

        Label errorLabel = SetupTitleLabel("Error: Missing input");
        errorLabel.setVisible(false);
        layout.add(errorLabel, 0, 2);

        errorLabel.setText("PatientID: " + patientID);
        errorLabel.setVisible(true);

        //load health file
        jo = visit.loadVisit(visit.getHealthFile());
        String allergiesInfo = "";
        String healthConcernsInfo = "";
        try {
            allergiesInfo = jo.getString("allergies");
            healthConcernsInfo = jo.getString("healthConc");

        } catch (JSONException e) {
            System.out.println("[Error] Loading Health File: No data");  //debug
        }
        column = 0;
        row = 0;
        Label allergies = SetupTitleLabel("Allergies:");
        grid.add(allergies, column, row);
        Label allergiesInput = SetupDataLabel(allergiesInfo);
        row++;
        grid.add(allergiesInput, column, row);

        Label healthConc = SetupTitleLabel("Health Concerns:");
        row++;
        grid.add(healthConc, column, row);
        Label healthConcInput = SetupDataLabel(healthConcernsInfo);
        row++;
        grid.add(healthConcInput, column, row);

        //load physical file
        jo = visit.loadVisit(visit.getPhysicalFile());
        String physicalInfo = "";
        String physicalConcernsInfo = "";
        try {
            physicalInfo = jo.getString("phyRes");
            physicalConcernsInfo = jo.getString("phyConcerns");

        } catch (JSONException e) {
            System.out.println("[Error] Loading Physical File: No data");  //debug
        }
        column++;
        row = 0;
        Label physLbl = SetupTitleLabel("Physical Results:");
        grid.add(physLbl, column, row);
        Label physLblInput = SetupDataLabel(physicalInfo);
        row++;
        grid.add(physLblInput, column, row);

        Label physConcLbl = SetupTitleLabel("Physical Concerns:");
        row++;
        grid.add(physConcLbl, column, row);
        Label physConcLblInput = SetupDataLabel(physicalConcernsInfo);
        row++;
        grid.add(physConcLblInput, column, row);

        //load medication file
        jo = visit.loadVisit(visit.getMedFile());
        String immunizationInfo = "";
        String prescriptionInfo = "";
        try {
            immunizationInfo = jo.getString("immunization");
            prescriptionInfo = jo.getString("perscription");

        } catch (JSONException e) {
            System.out.println("[Error] Loading Medication File: No data");  //debug
        }
        column++;
        row = 0;
        Label immmunLbl = SetupTitleLabel("Immunizations:");
        grid.add(immmunLbl, column, row);
        Label immmunLblInput = SetupDataLabel(immunizationInfo);
        row++;
        grid.add(immmunLblInput, column, row);

        Label medLbl = SetupTitleLabel("Prescriptions:");
        row++;
        grid.add(medLbl, column, row);
        Label medLblInput = SetupDataLabel(prescriptionInfo);
        row++;
        grid.add(medLblInput, column, row);

        //load vitals file

        Label vitalLblInput = SetupDataLabel("");
        jo = visit.loadVisit(visit.getVitalFile());

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

            vitalLblInput.setText(
                    "Over 12: " + over12 + "\n" +
                            "Weight: " + weight + "\n" +
                            "Height: " + pHeight + "\n" +
                            "Body Temp: " + bodyTemp + "\n" +
                            "Blood Pressure: " + bloodP);

        } catch (JSONException e) {
            System.out.println("[Error] Loading Vitals File: No data");  //debug
        }
        column++;
        row = 0;
        Label vitalLbl = SetupTitleLabel("Vitals:");
        grid.add(vitalLbl, column, row);
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
    
}
