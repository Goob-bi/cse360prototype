/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype;

import java.awt.Paint;
import java.text.DecimalFormat;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
/**
 *
 * @author andreas lees
 */
public class Control extends Application{

    @Override
    public void start(Stage primaryStage) {
        LoginMenu login = new LoginMenu();
        
        primaryStage.setScene(login.getScene());
        primaryStage.show();
    }
    
}
