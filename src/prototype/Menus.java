/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype;

import java.util.Random;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 *
 * @author andreas lees
 */
public class Menus extends Stage{
        private String user = "";
    private String pass = "";
    private Stage loginMenu;
    private Button backBtn = new Button();
    private Border border = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
    private Background bkgrndBlue = new Background(new BackgroundFill(Color.LIGHTSKYBLUE,CornerRadii.EMPTY, Insets.EMPTY));
    private Background bkgrndLBlue = new Background(new BackgroundFill(Color.AQUA,CornerRadii.EMPTY, Insets.EMPTY));
    private String patientID = "badID";
    public int width = 600;
    public int height = 400;
    
    private Patient patient;
    //Control() {
       // launch();
    //}
    public Scene loginScene;
    private Scene patientIntakeScene, TechView, PatientView, ErrorScene;

    Menus() {
        //testing!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        long generator = System.currentTimeMillis();
        System.out.println(generator);
        
        Random rand = new Random();
        int max = 99;
        int min = 10;
        int randInt = rand.nextInt(max - min + 1) + min;
        System.out.println(randInt);
        
        patientID = Long.toString(generator).substring(10, 13) + Integer.toString(randInt);
        System.out.println(patientID);
        
    /*    for (int i = 0; i < 10; i++) {
            generator = System.currentTimeMillis();
            System.out.println(generator);
            max = 99;
            min = 10;
            randInt = rand.nextInt(max - min + 1) + min;
            System.out.println(randInt);

            patientID = Long.toString(generator).substring(10, 13) + Integer.toString(randInt);
            System.out.println(patientID);
        } */
    //    System.exit(0);       //test end program early
        //String patientID = "1234"; //test using hardcoded id
        
        //testing!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //    patient = new Patient(patientID); //create patient
        //testing!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //    patient.loadPatient(); //test loading patient
        
       
//---------------------grid-----------------------------------------------         
        GridPane layout = new GridPane();
        layout.setAlignment(Pos.CENTER);
        //layout.setGridLinesVisible(true);     //debug
        
        Label scenetitle = new Label("Welcome to Heart Health Imaging and Recording System");
        layout.setHalignment(scenetitle, HPos.CENTER);
        layout.add(scenetitle, 0, 0);
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setHgap(5);
        grid.setVgap(15);
        //grid.setGridLinesVisible(true);       //debug
        
        layout.add(grid, 0, 1);
        
//---------------------buttons----------------------------------------------- 
        backBtn.setText("Back");
        backBtn.setMinHeight(0);
        backBtn.setMinWidth(100);
        backBtn.setBackground(bkgrndBlue);
        backBtn.setBorder(border);
        backBtn.setOnMouseEntered(e -> backBtn.setBackground(bkgrndLBlue));
        backBtn.setOnMouseExited(e -> backBtn.setBackground(bkgrndBlue));
        
        
//Color.LIGHTSKYBLUE
        Button pIntakeBtn = new Button();
        pIntakeBtn.setDisable(false);
        pIntakeBtn.setText("Patient Intake");
        pIntakeBtn.setAlignment(Pos.CENTER);
        pIntakeBtn.setTextAlignment(TextAlignment.CENTER);
        pIntakeBtn.setMinHeight(0);
        pIntakeBtn.setMinWidth(200);
        pIntakeBtn.setBackground(bkgrndBlue);
        pIntakeBtn.setBorder(border);
        pIntakeBtn.setOnMouseEntered(e -> pIntakeBtn.setBackground(bkgrndLBlue));
        pIntakeBtn.setOnMouseExited(e -> pIntakeBtn.setBackground(bkgrndBlue));
        grid.add(pIntakeBtn, 0, 0);    
        

        
        Button ctTechViewBtn = new Button();
        ctTechViewBtn.setDisable(false);
        ctTechViewBtn.setText("Doctor View");
        ctTechViewBtn.setAlignment(Pos.CENTER);
        ctTechViewBtn.setTextAlignment(TextAlignment.CENTER);
        ctTechViewBtn.setMinHeight(0);
        ctTechViewBtn.setMinWidth(200);
        ctTechViewBtn.setBackground(bkgrndBlue);
        ctTechViewBtn.setBorder(border);
        ctTechViewBtn.setOnMouseEntered(e -> ctTechViewBtn.setBackground(bkgrndLBlue));
        ctTechViewBtn.setOnMouseExited(e -> ctTechViewBtn.setBackground(bkgrndBlue));
        grid.add(ctTechViewBtn, 0, 1);
        
        Button pViewBtn = new Button();
        pViewBtn.setDisable(false);
        pViewBtn.setText("Patient View");
        pViewBtn.setAlignment(Pos.CENTER);
        pViewBtn.setTextAlignment(TextAlignment.CENTER);
        pViewBtn.setMinHeight(0);
        pViewBtn.setMinWidth(200);
        pViewBtn.setBackground(bkgrndBlue);
        pViewBtn.setBorder(border);
        pViewBtn.setOnMouseEntered(e -> pViewBtn.setBackground(bkgrndLBlue));
        pViewBtn.setOnMouseExited(e -> pViewBtn.setBackground(bkgrndBlue));
        grid.add(pViewBtn, 0, 2);
        
        loginScene = new Scene(layout, width, height);
        this.setTitle("Main Menu");
        this.setScene(loginScene);
        
        this.show();
        //patientIntakeScene, TechView, PatientView
        pIntakeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    //load up main menu
                    changeScene(MainMenu(1));
                    
                    
            }
            
        });
        ctTechViewBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
                    //load up main menu
                    changeScene(MainMenu(2));
                    
            }
        });
        pViewBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
                    //load up main menu
                    changeScene(MainMenu(3));
                    
            }
        });
        backBtn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                changeTitle("Main Menu");
                changeScene(loginScene);
            }
        });
    }
    private void changeTitle(String newTitle) {
        this.setTitle(newTitle);
    }
    private void changeScene(Scene newScene) {
        this.setScene(newScene);
    }
//--------------------menu creation-------------------    
    Label menu = new Label();
    VBox box = new VBox();
    public boolean login = true;

    private Scene MainMenu(int type){
        switch (type) {
            case 1:
                // code block
                changeTitle("Patient Intake");
                return IntakeMenu();
            
            case 2:
                // code block
                changeTitle("Tech View");
                return DocMenu();
            
            case 3:
                // code block
                changeTitle("Patient View");
                return PatientMenu();
            
            default: 
                // code block
                changeTitle("Error Menu");
                return ErrorMenu();
    }
        
       }   
    
    
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
        
        RadioButton  rb = new RadioButton ();
        rb.setText("Over 12?");
        rb.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (rb.isSelected()) {
                firstNameInput.setDisable(true);
                //System.out.println(drinkList[0]); //debug
                } else {
                    
                firstNameInput.setDisable(false);
                }
            }
        });
        grid.setHalignment(rb, HPos.LEFT);
        grid.add(rb, 0, 7);
        
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
        
        Label healthHist = new Label("Health History:");
        scenetitle.setAlignment(Pos.CENTER);
        grid.add(healthHist, 0, 5);
        TextField healthHistInput = new TextField ();
        healthHistInput.setText("");
        grid.add(healthHistInput, 1, 5);
        
        Label insurance = new Label("Insurance ID:");
        scenetitle.setAlignment(Pos.CENTER);
        grid.add(insurance, 0, 6);
        TextField insuranceInput = new TextField ();
        insuranceInput.setText("");
        grid.add(insuranceInput, 1, 6);
        
        Button saveBtn = new Button();
        saveBtn.setText("Save");
        saveBtn.setMinHeight(0);
        saveBtn.setMinWidth(100);
        saveBtn.setBackground(bkgrndBlue);
        saveBtn.setBorder(border);
        saveBtn.setAlignment(Pos.CENTER);
        saveBtn.setOnMouseEntered(e -> saveBtn.setBackground(bkgrndLBlue));
        saveBtn.setOnMouseExited(e -> saveBtn.setBackground(bkgrndBlue));
        grid.setHalignment(saveBtn, HPos.CENTER);
        grid.add(saveBtn, 2, 7);
        saveBtn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                patient = new Patient(patientID); //create patient
                patient.savePatient(firstNameInput.getText(), lastNameInput.getText(), emailInput.getText(), 
                        phoneInput.getText(), healthHistInput.getText(), insuranceInput.getText());
            }
        });
        
        backBtn.setAlignment(Pos.CENTER);
        grid.setHalignment(backBtn, HPos.CENTER);
        grid.add(backBtn, 1, 7);
        
        layout.add(grid, 0, 1);
        
        patientIntakeScene = new Scene(layout, width, height);
        return patientIntakeScene;

    }    
    private Scene DocMenu() {
        
        GridPane layout = new GridPane();
        layout.setHalignment(menu, HPos.CENTER);
        layout.setAlignment(Pos.CENTER);
        layout.setHgap(10);
        layout.setVgap(10);
        layout.setPadding(new Insets(25, 25, 25, 25));
        //layout.setGridLinesVisible(true);     //debug
        
        Label patientID = new Label("Patient ID:");
        patientID.setAlignment(Pos.CENTER);
        layout.add(patientID, 0, 0);
        TextField patientIDInput = new TextField ();
        patientIDInput.setText("");
        layout.add(patientIDInput, 1, 0);
        
        Label totalCAC = new Label("The total Agatston CAC score:");
        totalCAC.setAlignment(Pos.CENTER);
        layout.add(totalCAC, 0, 1);
        TextField totalCACInput = new TextField ();
        totalCACInput.setText("");
        layout.add(totalCACInput, 1, 1);
        
        Label vesselCAC = new Label("Vessel level Agatston CAC score:");
        vesselCAC.setAlignment(Pos.CENTER);
        layout.add(vesselCAC, 0, 2);
        TextField vesselCACInput = new TextField ();
        vesselCACInput.setText("");
        layout.add(vesselCACInput, 1, 2);
        
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        //grid.setPadding(new Insets(25, 25, 25, 25));
        //grid.setGridLinesVisible(true);       //debug
        
        layout.add(grid, 0, 3);
        
        
        Label lm = new Label("LM:");
        lm.setAlignment(Pos.CENTER);
        grid.add(lm, 0, 0);
        TextField lmInput = new TextField ();
        lmInput.setText("");
        grid.add(lmInput, 1, 0);
        
        Label lad = new Label("LAD:");
        lad.setAlignment(Pos.CENTER);
        grid.add(lad, 0, 1);
        TextField ladInput = new TextField ();
        ladInput.setText("");
        grid.add(ladInput, 1, 1);
        
        Label lcx = new Label("LCX:");
        lcx.setAlignment(Pos.CENTER);
        grid.add(lcx, 0, 2);
        TextField lcxInput = new TextField ();
        lcxInput.setText("");
        grid.add(lcxInput, 1, 2);
        
        Label rca = new Label("RCA:");
        rca.setAlignment(Pos.CENTER);
        grid.add(rca, 0, 3);
        TextField rcaInput = new TextField ();
        rcaInput.setText("");
        grid.add(rcaInput, 1, 3);
        
        Label pda = new Label("PDA:");
        pda.setAlignment(Pos.CENTER);
        grid.add(pda, 0, 4);
        TextField pdaInput = new TextField ();
        pdaInput.setText("");
        grid.add(pdaInput, 1, 4);
        
        Button saveBtn = new Button();
        saveBtn.setText("Save");
        saveBtn.setMinHeight(0);
        saveBtn.setMinWidth(100);
        saveBtn.setBackground(bkgrndBlue);
        saveBtn.setBorder(border);
        saveBtn.setAlignment(Pos.CENTER);
        saveBtn.setOnMouseEntered(e -> saveBtn.setBackground(bkgrndLBlue));
        saveBtn.setOnMouseExited(e -> saveBtn.setBackground(bkgrndBlue));
        layout.setHalignment(saveBtn, HPos.CENTER);
        layout.add(saveBtn, 1, 4);
        
        backBtn.setAlignment(Pos.CENTER);
        layout.setHalignment(backBtn, HPos.CENTER);
        layout.add(backBtn, 0, 4);
        
        
        TechView = new Scene(layout, width, height);
        return TechView;

    }   
    //---------------Patient Menu-------------------
    private Scene PatientMenu() {
        String patientName = "TestName";
        menu.setText("Hello " + patientName);   
        
        GridPane topLayout = new GridPane();
        topLayout.setHalignment(menu, HPos.CENTER);
        topLayout.setAlignment(Pos.CENTER);
        topLayout.setHgap(10);
        topLayout.setVgap(10);
        topLayout.setPadding(new Insets(25, 25, 25, 25));
        topLayout.add(menu, 0, 0);
        
        GridPane layout = new GridPane();
        layout.setHalignment(menu, HPos.CENTER);
        layout.setAlignment(Pos.CENTER);
        layout.setHgap(10);
        layout.setVgap(10);
        layout.setPadding(new Insets(25, 25, 25, 25));
        //layout.setGridLinesVisible(true);     //debug
        topLayout.add(layout, 0, 1);
        
        
        Label totalCAC = new Label("The total Agatston CAC score:");
        totalCAC.setAlignment(Pos.CENTER);
        layout.add(totalCAC, 0, 0);
        TextField totalCACInput = new TextField ();
        totalCACInput.setText("");
        layout.add(totalCACInput, 1, 0);
        
        
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        //grid.setPadding(new Insets(25, 25, 25, 25));
        //grid.setGridLinesVisible(true);       //debug
        
        layout.add(grid, 0, 3);
        
        
        Label lm = new Label("LM:");
        lm.setAlignment(Pos.CENTER);
        grid.add(lm, 0, 0);
        TextField lmInput = new TextField ();
        lmInput.setText("");
        grid.add(lmInput, 1, 0);
        
        Label lad = new Label("LAD:");
        lad.setAlignment(Pos.CENTER);
        grid.add(lad, 0, 1);
        TextField ladInput = new TextField ();
        ladInput.setText("");
        grid.add(ladInput, 1, 1);
        
        Label lcx = new Label("LCX:");
        lcx.setAlignment(Pos.CENTER);
        grid.add(lcx, 0, 2);
        TextField lcxInput = new TextField ();
        lcxInput.setText("");
        grid.add(lcxInput, 1, 2);
        
        Label rca = new Label("RCA:");
        rca.setAlignment(Pos.CENTER);
        grid.add(rca, 0, 3);
        TextField rcaInput = new TextField ();
        rcaInput.setText("");
        grid.add(rcaInput, 1, 3);
        
        Label pda = new Label("PDA:");
        pda.setAlignment(Pos.CENTER);
        grid.add(pda, 0, 4);
        TextField pdaInput = new TextField ();
        pdaInput.setText("");
        grid.add(pdaInput, 1, 4);
        
        
        backBtn.setAlignment(Pos.CENTER);
        layout.setHalignment(backBtn, HPos.CENTER);
        layout.add(backBtn, 1, 4);
        
        PatientView = new Scene(topLayout, width, height);
        return PatientView;

    }    
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
        grid.add(backBtn, 1, 1);
        
        ErrorScene = new Scene(layout, width, height);
        return ErrorScene;
        /*backBtn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                changeScene(ErrorScene);
            }
        });
        */

    }
}
