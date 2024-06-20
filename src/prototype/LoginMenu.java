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
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
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
        userInput.setText("username");
        TextField passInput = new TextField ();
        passInput.setText("password");
        grid.add(userInput, 0, 0);
        grid.add(passInput, 0, 1);
//---------------------buttons----------------------------------------------- 
        
        Button confirmBtn = new Button();
        confirmBtn.setDisable(false);
        confirmBtn.setText("Confirm");
        confirmBtn.setAlignment(Pos.CENTER);
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
                    MainMenu menu = new MainMenu(authenticate.getType());
                } else {
                    System.out.println("authentication failed");
                    //reset page with error
                    failed.setVisible(true);
                    confirmBtn.setDisable(false);
                    
                }
            }
        });
    }
    private void hideScene() {
        this.hide();
    }
}
