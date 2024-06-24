/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author andreas lees
 */
public class Control extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        LoginMenu authMenu = new LoginMenu();
        //obsolete code, handled within LoginMenu
    //    if (!authMenu.isShowing()) {
    //        Menus login = new Menus();
    //    }
        
        
        //primaryStage.setScene(login.getScene());
        //primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
