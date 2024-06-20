/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


/**
 *
 * @author andreas lees
 */
public class MainMenu extends Stage{
    Label menu = new Label();
    VBox box = new VBox();
    public boolean login = true;

    MainMenu(Authentication.accountType type){
        switch (type) {
            case PATIENT:
                // code block
                PatientMenu();
            break;
            
            case DOCTOR:
                // code block
                DoctorMenu();
            break;
            
            case NURSE:
                // code block
                NurseMenu();
            break;
            
            
            case ADMIN:
                // code block
                AdminMenu();
            break;
            
            default: 
                // code block
                ErrorMenu();
    }
        this.show();
       }   
    
    
    private void PatientMenu() {
        menu.setText("Patient Menu");        
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
        Button btn = new Button();
        btn.setText("Logout");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                Logout();
            }
        });
        grid.add(btn, 1, 1);
        
        this.setScene(new Scene(layout, 300, 300));

    }    
    private void DoctorMenu() {
        menu.setText("Doctor Menu");
        box.getChildren().add(menu);
        
        this.setScene(new Scene(box, 300, 300));

    }    
    private void NurseMenu() {
        menu.setText("Receptionist Menu");
        box.getChildren().add(menu);
        
        this.setScene(new Scene(box, 300, 300));

    }    
    private void AdminMenu() {
        menu.setText("Admin Menu");
        box.getChildren().add(menu);
        
        this.setScene(new Scene(box, 300, 300));

    }    
    private void ErrorMenu() {
        menu.setText("Error Menu");
        box.getChildren().add(menu);
        
        this.setScene(new Scene(box, 300, 300));

    }
    
    public void Logout() {
        this.hide();
        login = false;
    }
}

