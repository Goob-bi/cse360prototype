/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype.menus;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import prototype.*;
import prototype.data.Authentication;
import prototype.data.InputValidation;
import prototype.data.STAFFCellFactory;
import prototype.obj.Patient;
import prototype.obj.Staff;
import prototype.obj.Visit;

import static javafx.scene.layout.GridPane.setHalignment;

/**
 *
 * @author andreas lees
 */
public class Menus extends Stage{
    private Background bkgrndBlue = new Background(new BackgroundFill(Color.LIGHTSKYBLUE,CornerRadii.EMPTY, Insets.EMPTY));
    private Background bkgrndLBlue = new Background(new BackgroundFill(Color.AQUA,CornerRadii.EMPTY, Insets.EMPTY));
    private Authentication.accountType acctType = Authentication.accountType.NONE;
    
    protected String companyName = "Dr. Hearth's Office";
    protected Stage loginMenu;
    protected Border border = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
    protected String patientID = "";
    protected int width = 800;
    protected int height = 600;
    protected int row = 0;
    protected int column = 0;
    //protected ListView<String> list = new ListView<String>();
    protected ListView<String> visitList = new ListView<String>();
    protected ListView<JSONObject> list = new ListView<JSONObject>();
    //protected ListView<JSONObject> visitList = new ListView<JSONObject>();
    protected Button backBtn = SetupButton("Back", 100);
    protected Button logoutBtn = SetupLogoutButton("Logout", 100);
    
    protected Patient patient;
    protected Scene loginScene;
    protected Scene patientIntakeScene, patientSummaryScene, patientScene, patientInfoScene, ErrorScene;
    protected Visit visit;
    protected JSONObject jo;
    protected JSONArray ja;
    protected String visitNum;
    protected InputValidation check = new InputValidation();
    protected Button backVisitBtn = SetupButton("Back");
    protected StaffMessageMenu msgPortal;
    protected String staffID = "";
    protected String staffName = "";
    protected Label menu = SetupTitleLabel("");
    
    public void setID(String ID) {
        this.patientID = ID;
    }
    protected void closeExtraWindow() {
        //close message portal
        if (!(msgPortal == null)){
            msgPortal.close();
        }
    }
    protected String WORKINGPATH = "";

    protected Button SetupButton(String text) {
        
        Button Btn = new Button();
        Btn.setDisable(false);
        Btn.setText(text);
        Btn.setAlignment(Pos.CENTER);
        Btn.setTextAlignment(TextAlignment.CENTER);
        Btn.setMinHeight(0);
        Btn.setMinWidth(200);
        Btn.setBackground(bkgrndBlue);
        Btn.setBorder(border);
        Btn.setOnMouseEntered(e -> Btn.setBackground(bkgrndLBlue));
        Btn.setOnMouseExited(e -> Btn.setBackground(bkgrndBlue));
        setHalignment(Btn, HPos.CENTER);
        return Btn;
    }
    protected Button SetupButton(String text, int width) {
        
        Button Btn = new Button();
        Btn.setDisable(false);
        Btn.setText(text);
        Btn.setAlignment(Pos.CENTER);
        Btn.setTextAlignment(TextAlignment.CENTER);
        Btn.setMinHeight(0);
        Btn.setMinWidth(width);
        Btn.setBackground(bkgrndBlue);
        Btn.setBorder(border);
        Btn.setOnMouseEntered(e -> Btn.setBackground(bkgrndLBlue));
        Btn.setOnMouseExited(e -> Btn.setBackground(bkgrndBlue));
        setHalignment(Btn, HPos.CENTER);
        return Btn;
    }
    protected Button SetupLogoutButton(String text, int width) {

        Button Btn = new Button();
        Btn.setDisable(false);
        Btn.setText(text);
        Btn.setAlignment(Pos.CENTER);
        Btn.setTextAlignment(TextAlignment.CENTER);
        Btn.setMinHeight(0);
        Btn.setMinWidth(width);
        Btn.setBackground(bkgrndBlue);
        Btn.setBorder(border);
        Btn.setOnMouseEntered(e -> Btn.setBackground(bkgrndLBlue));
        Btn.setOnMouseExited(e -> Btn.setBackground(bkgrndBlue));
        setHalignment(Btn, HPos.CENTER);
        Btn.setOnAction(event -> {
            hideMenu();
            closeExtraWindow();
            LoginMenu authMenu = new LoginMenu(WORKINGPATH);
        });
        return Btn;
    }
    protected Label SetupDataLabel(String text) {
        Label Lbl = new Label();
        Lbl.setText(text);
        //Lbl.setAlignment(Pos.CENTER);
        Lbl.setTextAlignment(TextAlignment.LEFT);
        Lbl.setBorder(border);
        Lbl.setMinHeight(100);
        Lbl.setMinWidth(100);
        Lbl.setMaxWidth(200);
        Lbl.setWrapText(true);  //test
        setHalignment(Lbl, HPos.CENTER);
        return Lbl;
    }
    protected Label SetupTitleLabel(String text) {
        Label Lbl = new Label();
        Lbl.setText(text);
        Lbl.setTextAlignment(TextAlignment.CENTER);
        Lbl.setAlignment(Pos.CENTER);
        //Lbl.setBorder(border);
        Lbl.setMinWidth(100);
        Lbl.setWrapText(true);  //test
        setHalignment(Lbl, HPos.CENTER);
        return Lbl;
    }
    protected TextArea SetupDataInput() {
        TextArea msgBody = new TextArea();
        msgBody.setPromptText("Input data");
        msgBody.setWrapText(true);
        msgBody.setMinHeight(100);
        msgBody.setMinWidth(100);
        return msgBody;
    }
    protected void hideMenu() {
        this.close();
    }
    protected void updatePatientList() {
        if (list == null){
            return;
        }
        //collect list of patients
    //    patient = new Patient(WORKINGPATH);
    //    ObservableList<String> items = FXCollections.observableArrayList (patient.getPatientList());
        //eventually convert to jsonObjects
        //list.setCellFactory(new STAFFCellFactory());

    //    list.setItems(items);
        patient = new Patient(WORKINGPATH);
        list.setCellFactory(new STAFFCellFactory());
        list.setItems(patient.getPatientList());
    }
    protected void updateVisitList() {
        if (visitList == null){
            return;
        }
        //collect list of visits
        ObservableList<String> items = FXCollections.observableArrayList (visit.getVisitList());
        //eventually convert to jsonObjects
        //visitList.setCellFactory(new STAFFCellFactory());
        visitList.setItems(items);
    }
    public Menus() {
        //empty constructor, required for child classes to override
    }
    
    public Menus(Authentication auth, String path) {
        WORKINGPATH = path;
        
        this.hide();
        this.patientID = auth.getID();
        //System.out.println(patientID);
        this.acctType = auth.getType();
        switch (acctType) {
            case NURSE:
                NurseMenu nurse = new NurseMenu(auth.getID(), auth.getName(), WORKINGPATH);
                break;
            
            case DOCTOR:
                DoctorMenu doctor = new DoctorMenu(auth.getID(), auth.getName(), WORKINGPATH);
                break;
            
            case PATIENT:
                System.out.println("ID = " + patientID);
                if (patientID.isEmpty()) {
                    changeTitle("Error Menu");
                    this.setScene(ErrorMenu());
                    this.show();
                }
                PatientMenu pMenu = new PatientMenu(patientID, WORKINGPATH);
                break;
            
            default: 
                changeTitle("Error Menu");
                this.setScene(ErrorMenu());
                this.show();
        }
        
       
    }
    protected void changeTitle(String newTitle) {
        this.setTitle(newTitle);
    }
    protected void changeScene(Scene newScene) {
        this.setScene(newScene);
    }
//--------------------menu creation-------------------
    
    private Scene ErrorMenu() {
        menu.setText("Error Menu");
        GridPane layout = new GridPane();
        layout.setAlignment(Pos.TOP_CENTER);
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        layout.add(menu, 0, 0);
        
        Label scenetitle = new Label("Welcome");
        scenetitle.setAlignment(Pos.CENTER);
        layout.add(scenetitle, 0, 1);
        layout.add(grid, 0, 2);
        
        Label userName = new Label("Test:");
        grid.add(userName, 0, 1);
        grid.add(logoutBtn, 1, 1);
        
        ErrorScene = new Scene(layout, width, height);
        return ErrorScene;

    }
}
