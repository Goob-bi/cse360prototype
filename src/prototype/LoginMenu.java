
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
import javafx.scene.control.PasswordField;
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

/**
 *
 * @author andreas lees
 */
public class LoginMenu extends Stage{
    private String user = "";
    private String pass = "";
    private Scene loginScene;
    private Border border = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
    private Background bkgrndBlue = new Background(new BackgroundFill(Color.LIGHTSKYBLUE,CornerRadii.EMPTY, Insets.EMPTY));
    private Background bkgrndLBlue = new Background(new BackgroundFill(Color.AQUA,CornerRadii.EMPTY, Insets.EMPTY));
    private String patientID = "";
    private Button backBtn = new Button(); 
    private Patient patient;
    private Scene patientIntakeScene;
    private int width = 600;
    private int height = 400;
    
    private Button BlueButton(String text) {
        Button blueBtn = new Button();
        blueBtn.setText(text);
        blueBtn.setMinHeight(0);
        blueBtn.setMinWidth(100);
        blueBtn.setBackground(bkgrndBlue);
        blueBtn.setBorder(border);
        blueBtn.setOnMouseEntered(e -> blueBtn.setBackground(bkgrndLBlue));
        blueBtn.setOnMouseExited(e -> blueBtn.setBackground(bkgrndBlue));
        blueBtn.setAlignment(Pos.CENTER);
        blueBtn.setDisable(false);
        return blueBtn;
    }
//-------------------------------------------------------------------- 
    LoginMenu() {
       Authentication authenticate = new Authentication();
//---------------------grid-----------------------------------------------         
        GridPane layout = new GridPane();
        layout.setAlignment(Pos.CENTER);
        //layout.setGridLinesVisible(true);     //debug
        
        Label scenetitle = new Label("Login");
        layout.setHalignment(scenetitle, HPos.CENTER);
        layout.add(scenetitle, 0, 0);
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setHgap(5);
        grid.setVgap(5);
        //grid.setGridLinesVisible(true);       //debug
        
        layout.add(grid, 0, 1);
//-----------------------------labels----------------------------------
        Label failed = new Label("Incorrect user/pass");
        failed.setVisible(false);
        grid.add(failed, 0, 3);
//---------------------text boxes------------------------------------------
        TextField userInput = new TextField ();
        userInput.setPromptText("username");
        PasswordField passInput = new PasswordField();
        passInput.setPromptText("password");
        grid.add(userInput, 0, 0);
        grid.add(passInput, 0, 1);
//---------------------buttons----------------------------------------------- 
        
        Button confirmBtn = BlueButton("Confirm");
        grid.setHalignment(confirmBtn, HPos.CENTER);
        grid.add(confirmBtn, 0, 2);
        
        Button createAcct = BlueButton("Create Account");
        grid.setHalignment(createAcct, HPos.CENTER);
        grid.add(createAcct, 0, 3);
        
        loginScene = new Scene(layout, 500, 300);
        this.setScene(loginScene);
        
        this.setTitle("Prototype");
        this.show();
        
        confirmBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                failed.setVisible(false);
                user = userInput.getText();
                pass = passInput.getText();
                confirmBtn.setDisable(true);
                if (authenticate.auth(user, pass)) {
                   // System.out.println("authenticated");
                   // System.out.println(authenticate.getType());
                    //load up main menu
                    hideScene();
                    //loadup menus, loadup different using 
                    //      authenticate.getType()
                    //need to build constructors
                 //   Menus login = new Menus();
                    Menus login = new Menus(authenticate);
                    login.setID(authenticate.getID());
                    //MainMenu menu = new MainMenu(authenticate.getType());
                } else {
                    System.out.println("authentication failed");
                    //reset page with error
                    failed.setVisible(true);
                    confirmBtn.setDisable(false);
                    
                }
            }
        });

        createAcct.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                    //load up main menu
                    changeTitle("Patient Intake");
                    changeScene(IntakeMenu());
            }
        });
    }
//-------------------------------------------------------------------- 
    public void hideScene() {
        this.hide();
    }
    public void showScene() {
        this.show();
    }
//-------------------------------------------------------------------- 
    
    
    private Scene IntakeMenu() {
        
        backBtn.setText("Back");
        backBtn.setMinHeight(0);
        backBtn.setMinWidth(100);
        backBtn.setBackground(bkgrndBlue);
        backBtn.setBorder(border);
        backBtn.setOnMouseEntered(e -> backBtn.setBackground(bkgrndLBlue));
        backBtn.setOnMouseExited(e -> backBtn.setBackground(bkgrndBlue));
        
        Label menu = new Label();
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
                    InputValidation check = new InputValidation();
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
                    System.out.println(patientID);  //debug
                    
                    errorLabel.setText("Password: " + patientID); 
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
        backBtn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                changeTitle("Main Menu");
                changeScene(loginScene);
            }
        });
        
        layout.add(grid, 0, 1);
        
        patientIntakeScene = new Scene(layout, width, height);
        return patientIntakeScene;

    }    
    protected void changeTitle(String newTitle) {
        this.setTitle(newTitle);
    }
    protected void changeScene(Scene newScene) {
        this.setScene(newScene);
    }
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
        return Btn;
    }
}
