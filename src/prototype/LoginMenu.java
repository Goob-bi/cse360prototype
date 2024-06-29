
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype;

import javafx.stage.Stage;
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
import javafx.stage.Stage;

/**
 *
 * @author andreas lees
 */
public class LoginMenu extends Stage{
        private String user = "";
    private String pass = "";
    private Stage loginMenu;
    //Control() {
       // launch();
    //}
    public Scene loginScene;
    
    private Border border = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
    private Background bkgrndBlue = new Background(new BackgroundFill(Color.LIGHTSKYBLUE,CornerRadii.EMPTY, Insets.EMPTY));
    private Background bkgrndLBlue = new Background(new BackgroundFill(Color.AQUA,CornerRadii.EMPTY, Insets.EMPTY));
    
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
    LoginMenu() {
        
       // launch();
       System.out.println("dees");
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
                    System.out.println("authenticated");
                    System.out.println(authenticate.getType());
                    //load up main menu
                    hideScene();
                    //loadup menus, loadup different using 
                    //      authenticate.getType()
                    //need to build constructors
                 //   Menus login = new Menus();
                    Menus login = new Menus(authenticate.getType());
                    //MainMenu menu = new MainMenu(authenticate.getType());
                } else {
                    System.out.println("authentication failed");
                    //reset page with error
                    failed.setVisible(true);
                    confirmBtn.setDisable(false);
                    
                }
            }
        });
    }
    public void hideScene() {
        this.hide();
    }
    public void showScene() {
        this.show();
    }
}
