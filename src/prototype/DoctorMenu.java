/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype;

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
import org.json.JSONArray;
import org.json.JSONException;

import static javafx.scene.layout.GridPane.setHalignment;

/**
 *
 * @author andreas lees
 */
public class DoctorMenu extends Menus{

    //--------------------------------------------------------------------
    DoctorMenu() {
        //empty constructor, required for child classes to override
    }
    DoctorMenu(String ID, String name, String path) {
        WORKINGPATH = path;
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
            patient = new Patient(patientID, WORKINGPATH);
            patient.setWorkingPath(WORKINGPATH);
            visit = new Visit(patientID, WORKINGPATH);
            visit.setWorkingPath(WORKINGPATH);
            changeTitle("Patient Health");
            changeScene(patientMenu());
        });
        patientDelBtn.setOnAction(event -> {
            patientID = list.getSelectionModel().getSelectedItem();
            if (patientID == null || patientID.isEmpty()) {
                return;
            }
            patient = new Patient(patientID, WORKINGPATH); //create patient
            patient.setWorkingPath(WORKINGPATH);
            patient.deletePatient();
            visit = null;
            updatePatientList();
        });
        messageBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                msgPortal = new StaffMessageMenu(staffID, staffName, WORKINGPATH);
                msgPortal.setWorkingPath(WORKINGPATH);
                msgPortal.showMenu();
                //changeTitle("Patient Health");
                //changeScene(new MessageMenu().loginScene);


            }
        });
        backBtn.setOnAction(event -> {
            changeTitle("Main Menu");
            updatePatientList();
            changeScene(loginScene);
        });

        loginScene = new Scene(layout, width, height);
        this.setScene(loginScene);
        this.show();
    }
    protected Scene IntakeMenu() {
        Scene patientIntakeScene;


        Label menu = new Label();
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
                patientID = firstNameInput.getText() + lastNameInput.getText() + bDayInput.getText();


                errorLabel.setText("Password: " + patientID);
                errorLabel.setVisible(true);

                patient = new Patient(patientID, WORKINGPATH); //create patient
                patient.setWorkingPath(WORKINGPATH);
                patient.savePatient(firstNameInput.getText(), lastNameInput.getText(), emailInput.getText(),
                        phoneInput.getText(), bDayInput.getText(), insuranceInput.getText(), bDayInput.getText());

            }
        });

        backBtn.setAlignment(Pos.CENTER);
        setHalignment(backBtn, HPos.CENTER);
        grid.add(backBtn, 1, 7);
        backBtn.setOnAction(event -> {
            System.out.print("updating patient list");
            updatePatientList();
            changeTitle("Main Menu");
            changeScene(loginScene);
        });

        layout.add(grid, 0, 1);

        patientIntakeScene = new Scene(layout, width, height);
        return patientIntakeScene;

    }
    //--------------------------------------------------------------------
    protected Scene patientMenu() {
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

        Button physicalViewBtn = SetupButton("Physical");
        Button medViewBtn = SetupButton("Medications");
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
        grid.add(patientSumBtn, column, row);
        row++;
        grid.add(patientInfoBtn, column, row);
        row++;
        grid.add(patientHistBtn, column, row);
        row++;
        grid.add(backBtn, column, row);


        physicalViewBtn.setOnAction(event -> {
            visitNum = visitList.getSelectionModel().getSelectedItem();
            if (visitNum == null || !check.IntCheck(visitNum)) {
                return;
            }
            changeTitle("Patient Physical");
            changeScene(PhysicalMenu());
        });
        medViewBtn.setOnAction(event -> {
            visitNum = visitList.getSelectionModel().getSelectedItem();
            if (visitNum == null || !check.IntCheck(visitNum)) {
                return;
            }
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

        Label errorLabel = new Label();
        errorLabel.setAlignment(Pos.CENTER);
        setHalignment(errorLabel, HPos.CENTER);
        errorLabel.setText("Error: Missing input");
        errorLabel.setVisible(false);
        layout.add(errorLabel, 0, 2);

        Label physical = new Label("Physical Results:");
        grid.add(physical, 0, 2);
        TextField physicalInput = new TextField ();
        physicalInput.setText("");
        grid.add(physicalInput, 1, 2);

        Label physicalConc = new Label("Physical Concerns:");
        grid.add(physicalConc, 0, 3);
        TextField physicalConcInput = new TextField ();
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

        Label errorLabel = new Label();
        errorLabel.setAlignment(Pos.CENTER);
        setHalignment(errorLabel, HPos.CENTER);
        errorLabel.setText("Error: Missing input");
        errorLabel.setVisible(false);
        layout.add(errorLabel, 0, 2);

        Label immunization = new Label("Immunization performed:");
        grid.add(immunization, 0, 2);
        TextField immunizationInput = new TextField ();
        immunizationInput.setText("");
        grid.add(immunizationInput, 1, 2);
        //maybe add a text field for date of immunization?

        Label Perscription = new Label("Perscriptions:");
        grid.add(Perscription, 0, 3);
        TextField PerscriptionInput = new TextField ();
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
    //--------------------------------------------------------------------
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

        Label vitalLblInput = new Label();
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
        Label vitalLbl = new Label("Vitals:");
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
