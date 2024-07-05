package prototype;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.json.JSONObject;

import static javafx.scene.layout.GridPane.setHalignment;

public class StaffMessageMenu extends Menus {
    private Scene patientSummaryScene, patientScene, patientInfoScene;
    private JSONObject patientO;
    private String visitNum;
    private Visit visit;
    private InputValidation check = new InputValidation();
    private Button backVisitBtn = SetupButton("Back");
    private Staff staff;
    protected double x, y;
    //protected ListView<JSONObject> list = new ListView<JSONObject>();
    protected ListView<JSONObject> msgList = new ListView<JSONObject>();
    private String staffID;
    private Message msg;
    private JSONObject staffO;
    private String patientID = "";
    //--------------------------------------------------------------------
    private void updatePatientList() {
        //collect list of patients
        patient = new Patient();
        ObservableList<String> items = FXCollections.observableArrayList (patient.getPatientList());
        list.setItems(items);

    }
    private void updateMsgList() {
        ObservableList<JSONObject> list = msg.getMessageList();
        msgList.setCellFactory(new MSGCellFactory());
        msgList.setItems(list);

    }
    //--------------------------------------------------------------------
    StaffMessageMenu(String staffID, String staffName) {
        width = 600;
        this.hide();
        this.staffID = staffID;
        staffO = new JSONObject();
        staffO.put("patientID", staffID);
        staffO.put("username", staffName);
        this.setTitle("Main Menu");

        GridPane layout = new GridPane();
        layout.setAlignment(Pos.CENTER);
        //layout.setGridLinesVisible(true);     //debug

        Label scenetitle = new Label("Welcome to " + companyName);
        setHalignment(scenetitle, HPos.CENTER);
        layout.add(scenetitle, 0, 0);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setHgap(5);
        grid.setVgap(15);
        layout.add(grid, 1, 1);

        Button pIntakeBtn = SetupButton("New Patient");
        Button patientBtn = SetupButton("Goto Messages");
        Button patientDelBtn = SetupButton("Delete Patient");

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
        //grid.add(patientDelBtn, column, row);
        row++;
        grid.add(backBtn, column, row);


        pIntakeBtn.setOnAction(event -> {
            changeTitle("Patient Intake");
            if (!list.getSelectionModel().isEmpty()) {
                System.out.println(list.getSelectionModel().getSelectedItem());
            }
            //changeScene(IntakeMenu());
        });
        patientBtn.setOnAction(event -> {
            //update to staffID
            if (!list.getSelectionModel().isEmpty()) {
                System.out.println(list.getSelectionModel().getSelectedItem());
                patientID = list.getSelectionModel().getSelectedItem();
                System.out.println("patient id= " + patientID); //debug
                Patient patient = new Patient(patientID);
                patientO = new JSONObject();
                patientO.put("patientID", patient.getID());
                patientO.put("patientName", patient.getFirstName());
                System.out.println("staff id= " + staffO.getString("patientID")); //debug
                msg = new Message(staffO, patientO);
                msg.setFromStaff();
                //msg = new Message(staffID, staffID);
                updateMsgList();
                changeScene(ViewMessageMenu());
            }
            //patient = new Patient(staffID);
            //visit = new Visit(staffID);
            //changeTitle("Patient Health");
            //changeScene(patientMenu());
        });
        patientDelBtn.setOnAction(event -> {
            //patient = new Patient(staffID); //create patient
            //patient.deletePatient();
            //visit = null;
            //updateList();
        });
        backBtn.setOnAction(event -> {
            //changeTitle("Main Menu");
            //updateList();
            //changeScene(loginScene);
            this.hideMenu();
        });
        loginScene = new Scene(layout, width, height);
        this.setScene(loginScene);
        this.show();
        x = this.getX() + 50;
        y = this.getY() - 50;
        this.hide();
    }
    public void showMenu() {
        //update x, y if already showing
        x = this.getX();
        y = this.getY();
        //show menu
        this.show();
        this.setX(x);
        this.setY(y);
        this.requestFocus();
    }
    public void hideMenu() {
        //save x, y so it gets shown in the same spot
        x = this.getX();
        y = this.getY();
        this.hide();
    }
    //--------------------------------------------------------------------
    private Scene ViewMessageMenu() {

        this.setTitle("View Menu");

        GridPane layout = new GridPane();
        layout.setAlignment(Pos.CENTER);
        //layout.setGridLinesVisible(true);     //debug

        Label scenetitle = new Label("Welcome to " + companyName);
        setHalignment(scenetitle, HPos.CENTER);
        layout.add(scenetitle, 0, 0);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setHgap(5);
        grid.setVgap(15);
        layout.add(grid, 1, 1);

        TextField msgTitle = new TextField();
        msgTitle.setPromptText("Title");
        TextField msgBody = new TextField();
        msgBody.setPromptText("Message");

        Button pIntakeBtn = SetupButton("Send Message");
        Button patientBtn = SetupButton("Goto Visits");
        Button patientDelBtn = SetupButton("Delete Patient");

        //collect list of messages
        updateMsgList();
        msgList.setPrefWidth(200);
        msgList.setPrefHeight(300);
        layout.add(msgList, 0, 1);

        column= 0;
        row = 0;
        grid.add(msgTitle, column, row);
        row++;
        grid.add(msgBody, column, row);
        row++;
        grid.add(pIntakeBtn, column, row);
        row++;
        //grid.add(patientBtn, column, row);
        row++;
        //grid.add(patientDelBtn, column, row);
        row++;
        grid.add(backBtn, column, row);


        pIntakeBtn.setOnAction(event -> {
            if (!msgTitle.getText().isEmpty() || !msgBody.getText().isEmpty()) {
                msg.saveMessage(msgTitle.getText(), msgBody.getText());
                msgTitle.setText("");
                msgBody.setText("");
                updateMsgList();
            }
            //changeScene(IntakeMenu());
        });
        patientBtn.setOnAction(event -> {

            //patient = new Patient(staffID);
            //visit = new Visit(staffID);
            //changeTitle("Patient Health");
            //changeScene(patientMenu());
        });
        patientDelBtn.setOnAction(event -> {

            //patient = new Patient(staffID); //create patient
            //patient.deletePatient();
            //visit = null;
            //updateList();
        });
        backBtn.setOnAction(event -> {
            //changeTitle("Main Menu");
            //updateList();
            changeScene(loginScene);
        });
        Scene viewMsgScene = new Scene(layout, width, height);
        return viewMsgScene;
    }
}
