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
import org.json.JSONException;
import org.json.JSONObject;

import static javafx.scene.layout.GridPane.setHalignment;

public class MessageMenu extends Menus {
    private Scene patientSummaryScene, patientScene, patientInfoScene;
    private JSONObject jo;
    private String visitNum;
    private Visit visit;
    private InputValidation check = new InputValidation();
    private Button backVisitBtn = SetupButton("Back");
    private Staff staff;
    protected double x, y;
    protected ListView<JSONObject> list = new ListView<JSONObject>();
    protected ListView<JSONObject> msgList = new ListView<JSONObject>();
    private String staffID;
    private Message msg;
    private JSONObject patientO;
    private Label recentMsg = new Label();
    //--------------------------------------------------------------------
    private void updateStaffList() {
        //collect list of patients
        staff = new Staff();
        //JSONCellFactory jitems = = FXCollections.observableArrayList (staff.getStaffList());
        //ObservableList<JSONObject> items = FXCollections.observableArrayList (staff.getStaffList());
        list.setCellFactory(new STAFFCellFactory());
        list.setItems(staff.getStaffList());

        //ObservableList<String> items = FXCollections.observableArrayList (staff.getStaffList());
        //list.setItems(items);

    }
    private void updateMsgList() {
        ObservableList<JSONObject> list = msg.getMessageList();
        msgList.setCellFactory(new MSGCellFactory());
        msgList.setItems(list);

    }
    private void updateRecentMessage() {
        recentMsg.setBorder(border);
        setHalignment(recentMsg, HPos.CENTER);
        Message recentMsgO = new Message(patientO);
        JSONObject jo1 = recentMsgO.getRecentMessage();
        System.out.println(jo1.toString());
        String recentMsgTxt = "";
        try {
            recentMsgTxt =
                    jo1.getString("from") + "->" + jo1.getString("to")
                            + "\n\t" + jo1.getString("msgTitle")
                            + "\n\t" + jo1.getString("msgBody")
            ;

        } catch (JSONException e) {
            recentMsgTxt = "no recent messages";
        }
        recentMsg.setText(recentMsgTxt);

    }
    //--------------------------------------------------------------------
    MessageMenu(String patientID, String patientName) {
        width = 600;
        this.hide();
        this.patientID = patientID;
        patientO = new JSONObject();
        patientO.put("patientID", patientID);
        patientO.put("patientName", patientName);
        this.setTitle("Main Menu");

        GridPane layout = new GridPane();
        layout.setAlignment(Pos.CENTER);
        //layout.setGridLinesVisible(true);     //debug

        Label scenetitle = new Label("Welcome to " + companyName);
        setHalignment(scenetitle, HPos.CENTER);
        layout.add(scenetitle, 0, 0);

        Label recentMsgTitle = new Label("Recent Message:");
        updateRecentMessage();
        layout.add(recentMsgTitle, 2, 0);
        layout.add(recentMsg, 2, 1);

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
        updateStaffList();
        list.setPrefWidth(200);
        list.setPrefHeight(300);
        layout.add(list, 0, 1);

        column= 0;
        row = 0;
        //grid.add(pIntakeBtn, column, row);
        row++;
        grid.add(patientBtn, column, row);
        row++;
        //grid.add(patientDelBtn, column, row);
        row++;
        grid.add(backBtn, column, row);


        pIntakeBtn.setOnAction(event -> {
            changeTitle("Patient Intake");
            if (!list.getSelectionModel().isEmpty()) {
                System.out.println(list.getSelectionModel().getSelectedItem().getString("patientID"));
            }
            //changeScene(IntakeMenu());
        });
        patientBtn.setOnAction(event -> {
            //update to staffID
            if (!list.getSelectionModel().isEmpty()) {
                System.out.println(list.getSelectionModel().getSelectedItem().getString("patientID"));
                String staffID = list.getSelectionModel().getSelectedItem().getString("patientID");
                System.out.println("staff id= " + staffID); //debug
                msg = new Message(list.getSelectionModel().getSelectedItem(), patientO);
                msg.setFromPatient();
                //msg = new Message(staffID, patientID);
                updateMsgList();
                changeScene(ViewMessageMenu());
            }
            //patient = new Patient(patientID);
            //visit = new Visit(patientID);
            //changeTitle("Patient Health");
            //changeScene(patientMenu());
        });
        patientDelBtn.setOnAction(event -> {
            staffID = list.getSelectionModel().getSelectedItem().getString("patientID");
            if (staffID == null || staffID.isEmpty()) {
                return;
            }
            //patient = new Patient(patientID); //create patient
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
                updateRecentMessage();
            }
            //changeScene(IntakeMenu());
        });
        patientBtn.setOnAction(event -> {

            //patient = new Patient(patientID);
            //visit = new Visit(patientID);
            //changeTitle("Patient Health");
            //changeScene(patientMenu());
        });
        patientDelBtn.setOnAction(event -> {

            //patient = new Patient(patientID); //create patient
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
