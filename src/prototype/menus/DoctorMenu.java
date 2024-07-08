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
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.WindowEvent;
import org.json.JSONException;
import prototype.data.InputValidation;
import prototype.data.STAFFCellFactory;
import prototype.obj.Patient;
import prototype.obj.Visit;

import static javafx.scene.layout.GridPane.setHalignment;

/**
 *
 * @author andreas lees
 */
public class DoctorMenu extends Menus{
    protected String userName = "";
    @Override
    protected void updatePatientList() {
        patient = new Patient(WORKINGPATH);
        list.setCellFactory(new STAFFCellFactory());
        list.setItems(patient.getPatientList());
    }
    //--------------------------------------------------------------------
    DoctorMenu() {
        //empty constructor, required for child classes to override
    }
    DoctorMenu(String ID, String name, String path) {
        WORKINGPATH = path;
        this.staffID = ID;
        this.staffName = name;

        updatePatientList();
        msgPortal = new StaffMessageMenu(staffID, staffName, WORKINGPATH);
        this.setTitle("Main Menu");

        GridPane layout = new GridPane();
        layout.setAlignment(Pos.CENTER);

        Label scenetitle = SetupTitleLabel("Welcome to " + companyName);
        layout.add(scenetitle, 0, 0);

        this.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                closeExtraWindow();
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
        Button messageBtn = SetupButton("Message Portal");

        //collect list of patients
        updatePatientList();
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
        row++;
        grid.add(messageBtn, column, row);
        row++;
        grid.add(logoutBtn, column, row);


        pIntakeBtn.setOnAction(event -> {
            changeTitle("Patient Intake");
            changeScene(IntakeMenu());
        });
        patientBtn.setOnAction(event -> {
            if (list.getSelectionModel().getSelectedItem() == null) {
                return;
            }
            patientID = list.getSelectionModel().getSelectedItem().getString("patientID");
            if (patientID == null || patientID.isEmpty()) {
                return;
            }
            patient = new Patient(patientID, WORKINGPATH);
            visit = new Visit(patientID, WORKINGPATH);
            visit.checkMissingData();
            changeTitle("Patient Health");
            changeScene(patientVisitMenu());
        });
        patientDelBtn.setOnAction(event -> {
            if (list.getSelectionModel().getSelectedItem() == null) {
                return;
            }
            patientID = list.getSelectionModel().getSelectedItem().getString("patientID");
            userName = list.getSelectionModel().getSelectedItem().getString("username");
            if (patientID == null || patientID.isEmpty()) {
                return;
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + userName + " ?\nID: " + patientID, ButtonType.NO, ButtonType.YES);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                patient = new Patient(patientID, WORKINGPATH); //create patient
                patient.deletePatient();
                visit = null;
                updatePatientList();
            }
        });
        messageBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                msgPortal.showMenu();

            }
        });
       backBtn.setOnAction(event -> {
           changeTitle("Main Menu");
           changeScene(loginScene);
           updatePatientList();
       });

        loginScene = new Scene(layout, width, height);
        this.setScene(loginScene);
        this.show();
    }
    protected Scene IntakeMenu() {
        Scene patientIntakeScene;


        Label menu = SetupTitleLabel("Patient Intake Form");
        GridPane layout = new GridPane();
        layout.setAlignment(Pos.CENTER);

        layout.add(menu, 0, 0);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));



        Label scenetitle = SetupTitleLabel("First Name:");
        grid.add(scenetitle, 0, 1);
        TextField firstNameInput = new TextField();
        firstNameInput.setText("");
        grid.add(firstNameInput, 1, 1);

        Label errorLabel = SetupTitleLabel("Error: Missing input");
        errorLabel.setVisible(false);
        layout.add(errorLabel, 0, 2);



        Label lastName = SetupTitleLabel("Last Name:");
        grid.add(lastName, 0, 2);
        TextField lastNameInput = new TextField();
        lastNameInput.setText("");
        grid.add(lastNameInput, 1, 2);

        Label email = SetupTitleLabel("Email:");
        grid.add(email, 0, 3);
        TextField emailInput = new TextField();
        emailInput.setText("");
        grid.add(emailInput, 1, 3);

        Label phone = SetupTitleLabel("Phone Number:");
        grid.add(phone, 0, 4);
        TextField phoneInput = new TextField();
        phoneInput.setText("");
        grid.add(phoneInput, 1, 4);

        Label bDay = SetupTitleLabel("Birthday (DD/MM/YYYY):");
        grid.add(bDay, 0, 5);
        TextField bDayInput = new TextField();
        bDayInput.setText("");
        grid.add(bDayInput, 1, 5);

        Label insurance = SetupTitleLabel("Insurance ID:");
        grid.add(insurance, 0, 6);
        TextField insuranceInput = new TextField();
        insuranceInput.setText("");
        grid.add(insuranceInput, 1, 6);

        Button saveBtn = SetupButton("Save");
        saveBtn.setMinWidth(100);

        grid.add(saveBtn, 2, 7);
        saveBtn.setOnAction(event -> {
            if (firstNameInput.getText().isEmpty() || lastNameInput.getText().isEmpty() || emailInput.getText().isEmpty() ||
                    phoneInput.getText().isEmpty() || bDayInput.getText().isEmpty() || insuranceInput.getText().isEmpty()) {

                errorLabel.setText("Error: Missing input");
                errorLabel.setVisible(true);
            } else {
                //patientID is fistName, lastName, birthday
                InputValidation check = new InputValidation();
                if (!check.DateCheck(bDayInput.getText()) || !check.NameCheck(firstNameInput.getText()) || !check.NameCheck(lastNameInput.getText())) {

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
                patientID = firstNameInput.getText() + lastNameInput.getText() + check.ConvertDate(bDayInput.getText());


                errorLabel.setText("Password: " + patientID);
                errorLabel.setVisible(true);

                patient = new Patient(patientID, WORKINGPATH); //create patient
                patient.savePatient(firstNameInput.getText(), lastNameInput.getText(), emailInput.getText(),
                        phoneInput.getText(), bDayInput.getText(), insuranceInput.getText(), bDayInput.getText());

            }
        });

        grid.add(backBtn, 1, 7);

        layout.add(grid, 0, 1);

        patientIntakeScene = new Scene(layout, width, height);
        return patientIntakeScene;

    }
    //--------------------------------------------------------------------
    protected Scene patientVisitMenu() {
        this.setTitle("Patient Menu");

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

        Button physicalViewBtn = SetupButton("Enter Physical Data");
        Button medViewBtn = SetupButton("Enter Medications");
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
        grid.add(physicalViewBtn, column, row);
        row++;
        grid.add(medViewBtn, column, row);
        row++;
        row++;
        grid.add(patientSumBtn, column, row);
        row++;
        grid.add(patientHistBtn, column, row);
        row++;
        grid.add(patientInfoBtn, column, row);
        row++;
        grid.add(backBtn, column, row);


        physicalViewBtn.setOnAction(event -> {
            //edit any visit
            //        visitNum = visitList.getSelectionModel().getSelectedItem();
            //       if (visitNum == null || !check.IntCheck(visitNum)) {
            //            return;
            //        }

            //edit only current visit
            visitNum = Integer.toString(visit.getCurrentVisit());
            changeTitle("Patient Physical");
            changeScene(PhysicalMenu());
        });
        medViewBtn.setOnAction(event -> {
            //edit any visit
            //        visitNum = visitList.getSelectionModel().getSelectedItem();
            //       if (visitNum == null || !check.IntCheck(visitNum)) {
            //            return;
            //        }

            //edit only current visit
            visitNum = Integer.toString(visit.getCurrentVisit());
            changeTitle("Patient Medications");
            changeScene(MedicationMenu());
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
    protected Scene InfoMenu() {
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
            System.out.println("[Patient Info] Bad file");
        }

        menu.setText("Patient Contact Information");
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

        Label scenetitle = SetupTitleLabel("First Name:");
        grid.add(scenetitle, 0, 1);
        Label firstNameInput = SetupTitleLabel(fname);
        grid.add(firstNameInput, 1, 1);

        Label errorLabel = SetupTitleLabel("Error: Missing input");
        errorLabel.setVisible(false);
        layout.add(errorLabel, 0, 2);

        Label lastNameLbl = SetupTitleLabel("Last Name:");
        grid.add(lastNameLbl, 0, 2);
        Label lastNameInput = SetupTitleLabel(lname);
        grid.add(lastNameInput, 1, 2);

        Label emailLbl = SetupTitleLabel("Email:");
        grid.add(emailLbl, 0, 3);
        Label emailInput = SetupTitleLabel(email);
        grid.add(emailInput, 1, 3);

        Label phoneLbl = SetupTitleLabel("Phone Number:");
        grid.add(phoneLbl, 0, 4);
        Label phoneInput = SetupTitleLabel(phone);
        grid.add(phoneInput, 1, 4);

        Label bDayLbl = SetupTitleLabel("Birthday (DDMMYYYY):");
        grid.add(bDayLbl, 0, 5);
        Label bDayInput = SetupTitleLabel(bDay);
        grid.add(bDayInput, 1, 5);

        Label insurance = SetupTitleLabel("Insurance ID:");
        grid.add(insurance, 0, 6);
        Label insuranceInput = SetupTitleLabel(insID);
        grid.add(insuranceInput, 1, 6);


        grid.add(backVisitBtn, 1, 7);

        layout.add(grid, 0, 1);
        patientInfoScene = new Scene(layout, this.width, this.height);
        return patientInfoScene;
    }
    //--------------------------------------------------------------------
    private Scene PhysicalMenu() {
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

        Label errorLabel = SetupTitleLabel("Error: Missing input");
        errorLabel.setVisible(false);
        layout.add(errorLabel, 0, 2);

        Label physical = SetupTitleLabel("Physical Results:");
        grid.add(physical, 0, 2);
        TextArea physicalInput = SetupDataInput();
        physicalInput.setText("");
        grid.add(physicalInput, 1, 2);

        Label physicalConc = SetupTitleLabel("Physical Concerns:");
        grid.add(physicalConc, 0, 3);
        TextArea physicalConcInput = SetupDataInput();
        physicalConcInput.setText("");
        grid.add(physicalConcInput, 1, 3);

        Button saveBtn = SetupButton("Save");
        saveBtn.setMinWidth(100);

        setHalignment(saveBtn, HPos.CENTER);
        grid.add(saveBtn, 2, 7);
        saveBtn.setOnAction(event -> {
            if (physicalInput.getText().isEmpty() || physicalConcInput.getText().isEmpty()) {
                errorLabel.setText("Error: Missing input");
                errorLabel.setVisible(true);
            } else {
                saveBtn.setDisable(true);
                if (patientID.isEmpty()) {
                    errorLabel.setText("Error: No ID");
                    errorLabel.setVisible(true);
                    return;
                }

                errorLabel.setText("PatientID: " + patientID);
                errorLabel.setVisible(true);
                visit.setVisit(visit.getCurrentVisit());
                visit.saveVisitPhys(physicalInput.getText(), physicalConcInput.getText());

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
    private Scene MedicationMenu() {
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

        Label errorLabel = SetupTitleLabel("Error: Missing input");
        errorLabel.setVisible(false);
        layout.add(errorLabel, 0, 2);

        Label immunization = SetupTitleLabel("Immunization performed:");
        grid.add(immunization, 0, 2);
        TextArea immunizationInput = SetupDataInput();
        immunizationInput.setText("");
        grid.add(immunizationInput, 1, 2);
        //maybe add a text field for date of immunization?

        Label Perscription = SetupTitleLabel("Perscriptions:");
        grid.add(Perscription, 0, 3);
        TextArea PerscriptionInput = SetupDataInput();
        PerscriptionInput.setText("");
        grid.add(PerscriptionInput, 1, 3);

        Button saveBtn = SetupButton("Save");
        saveBtn.setMinWidth(100);

        setHalignment(saveBtn, HPos.CENTER);
        grid.add(saveBtn, 2, 7);
        saveBtn.setOnAction(event -> {
            if (immunizationInput.getText().isEmpty() || PerscriptionInput.getText().isEmpty()) {
                errorLabel.setText("Error: Missing input");
                errorLabel.setVisible(true);
            } else {
                saveBtn.setDisable(true);
                if (patientID.isEmpty()) {
                    errorLabel.setText("Error: No ID");
                    errorLabel.setVisible(true);
                    return;
                }

                errorLabel.setText("PatientID: " + patientID);
                errorLabel.setVisible(true);
                visit.setVisit(visit.getCurrentVisit());
                visit.saveVisitMeds(immunizationInput.getText(), PerscriptionInput.getText());

            }
        });

        backVisitBtn.setAlignment(Pos.CENTER);
        setHalignment(backVisitBtn, HPos.CENTER);
        grid.add(backVisitBtn, 1, 7);

        layout.add(grid, 0, 1);

        patientIntakeScene = new Scene(layout, this.width, this.height);
        return patientIntakeScene;

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
            System.out.println("[Loading Health File] No data");  //debug
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
            System.out.println("[Loading Physical File] No data");  //debug
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
            System.out.println("[Loading Medication File] No data");  //debug
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
            System.out.println("[Loading Vitals File] No data");  //debug
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
    //--------------------------------------------------------------------
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
            System.out.println("[Loading Health History] No data");  //debug
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
            System.out.println("[Loading Physical History] No data");  //debug
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
            System.out.println("[Loading Medication History] No data");  //debug
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


}
