package prototype;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.WindowEvent;
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
    //--------------------------------------------------------------------
    private void updateList() {
        //collect list of patients
        staff = new Staff();
        ObservableList<String> items = FXCollections.observableArrayList (staff.getStaffList());
        list.setItems(items);

    }
    //--------------------------------------------------------------------
    MessageMenu(String ID) {

        width = 600;
        this.hide();
        this.patientID = ID;
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
        Button patientBtn = SetupButton("Goto Visits");
        Button patientDelBtn = SetupButton("Delete Patient");

        //collect list of patients
        updateList();
        list.setPrefWidth(200);
        list.setPrefHeight(300);
        layout.add(list, 0, 1);

        column= 0;
        row = 0;
        //grid.add(pIntakeBtn, column, row);
        row++;
        //grid.add(patientBtn, column, row);
        row++;
        //grid.add(patientDelBtn, column, row);
        row++;
        grid.add(backBtn, column, row);


        pIntakeBtn.setOnAction(event -> {
            changeTitle("Patient Intake");
            //changeScene(IntakeMenu());
        });
        patientBtn.setOnAction(event -> {
            patientID = list.getSelectionModel().getSelectedItem();
            if (patientID == null || patientID.isEmpty()) {
                return;
            }
            patient = new Patient(patientID);
            visit = new Visit(patientID);
            changeTitle("Patient Health");
            //changeScene(patientMenu());
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
}
