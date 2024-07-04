/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.json.JSONException;

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
    protected ListView<String> list = new ListView<String>();
    protected ListView<String> visitList = new ListView<String>();
    protected Button backBtn = SetupButton("Back", 100);
    
    protected Patient patient;
    protected Scene loginScene;
    protected Scene patientIntakeScene, TechView, PatientView, ErrorScene;
    
    public void setID(String ID) {
        this.patientID = ID;
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
        return Btn;
    }
    Menus() {
        //empty constructor, required for child classes to override
    }
    
    Menus(Authentication auth) {
        
        this.hide();
        this.patientID = auth.getID();
        //System.out.println(patientID);
        this.acctType = auth.getType();
        switch (acctType) {
            case NURSE:
                NurseMenu nurse = new NurseMenu();
                break;
            
            case DOCTOR:
                DoctorMenu doctor = new DoctorMenu();
                break;
            
            case PATIENT:
                try {
                    PatientMenu pMenu = new PatientMenu(patientID);
                    break;
                } catch (JSONException e) {
                    System.out.println("no patient found");
                }
            
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
    Label menu = new Label();
    
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

    }
}
