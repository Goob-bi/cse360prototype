/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype.menus;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import prototype.obj.Patient;

import static javafx.scene.layout.GridPane.*;

/**
 *
 * @author andreas lees
 */
public class NurseMenu extends DoctorMenu {

//--------------------------------------------------------------------
    public NurseMenu(String ID, String name, String path){
        super(ID, name, path);
    }
//--------------------------------------------------------------------
    @Override
    protected Scene patientMenu() {
        this.setTitle("Patient Menu");
               
        GridPane layout = new GridPane();
        layout.setAlignment(Pos.CENTER);
        //layout.setGridLinesVisible(true);     //debug
        
        Label scenetitle = new Label("Visits:");
        setHalignment(scenetitle, HPos.CENTER);
        layout.add(scenetitle, 0, 0);
        
        Label visitLabel = new Label("Latest visit: " + visit.getCurrentVisit());
        setHalignment(visitLabel, HPos.CENTER);
        layout.add(visitLabel, 1, 0);
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setHgap(5);
        grid.setVgap(15);
        layout.add(grid, 1, 1);
        
        Button vitalsViewBtn = SetupButton("Enter Vitals");
        Button healthViewBtn = SetupButton("Enter Health Data");
        Button patientSumBtn = SetupButton("Visit Summary");
        Button newVisitBtn = SetupButton("New Visit");
        Button patientInfoBtn = SetupButton("Patient Info");
        Button patientHistBtn = SetupButton("Patient History");
        
        //collect list of visits
        updateVisitList();
        visitList.setPrefWidth(200);
        visitList.setPrefHeight(300);
        layout.add(visitList, 0, 1);
        
        column = 0;
        row = 0;
        grid.add(newVisitBtn, column, row);
        row++;
        grid.add(vitalsViewBtn, column, row);
        row++;
        grid.add(healthViewBtn, column, row);
        row++;
        row++;
        grid.add(patientSumBtn, column, row);
        row++;
        grid.add(patientHistBtn, column, row);
        row++;
        grid.add(patientInfoBtn, column, row);
        row++;
        grid.add(backBtn, column, row);
        
        
        vitalsViewBtn.setOnAction(event -> {
                //edit any visit
        //        visitNum = visitList.getSelectionModel().getSelectedItem();
        //       if (visitNum == null || !check.IntCheck(visitNum)) {
        //            return;
        //        }

            //edit only current visit
                visitNum = Integer.toString(visit.getCurrentVisit());
                changeTitle("Patient Vitals");
                changeScene(VitalsMenu());
        });
        healthViewBtn.setOnAction(event -> {
                    //edit any visit
            //        visitNum = visitList.getSelectionModel().getSelectedItem();
            //       if (visitNum == null || !check.IntCheck(visitNum)) {
            //            return;
            //        }

                //edit only current visit
            visitNum = Integer.toString(visit.getCurrentVisit());
                changeTitle("Patient Health");
                changeScene(HealthMenu());
        });
        patientSumBtn.setOnAction(event -> {
                visitNum = visitList.getSelectionModel().getSelectedItem();
                if (visitNum == null || !check.IntCheck(visitNum)) {
                    return;
                }
                visit.setVisit(Integer.parseInt(visitNum));
                changeTitle("Patient Health");
                changeScene(SummaryMenu());
        });
        patientHistBtn.setOnAction(event -> {
            changeTitle("Patient History");
            changeScene(HistoryMenu());
        });
        newVisitBtn.setOnAction(event -> {
            //stop creating new visit if missing data
            if (!visit.checkMissingData()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Data missing! Visit may still be in progress.", ButtonType.OK);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.OK) { }

            } else {
                visit.incrementVisit();
                updateVisitList();
                visitLabel.setText("Latest visit: " + visit.getCurrentVisit());

            }
        });
        patientInfoBtn.setOnAction(event -> {
            changeTitle("Patient Info");
            changeScene(InfoMenu());
        });
        
        backVisitBtn.setOnAction(event -> {
            changeTitle("Main Menu");
            updateVisitList();
            changeScene(patientScene);
        });
        patientScene = new Scene(layout, width, height);
        return patientScene;
    }
//--------------------------------------------------------------------
    private Scene VitalsMenu() {
        menu.setText("Patient Vitals");        
        GridPane layout = new GridPane();
        menu.setAlignment(Pos.CENTER);
        setHalignment(menu, HPos.CENTER);
        layout.setAlignment(Pos.CENTER);
        layout.add(menu, 0, 0);
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        //grid.setGridLinesVisible(true);       //debug
       
        Label errorLabel = new Label();  
        errorLabel.setAlignment(Pos.CENTER);
        setHalignment(errorLabel, HPos.CENTER);
        errorLabel.setText("Error: Missing input"); 
        errorLabel.setVisible(false);
        layout.add(errorLabel, 0, 2);
        
        Label weight = new Label("Weight:");
        grid.add(weight, 0, 2);
        TextField weightInput = new TextField();
        weightInput.setText("");
        grid.add(weightInput, 1, 2);
        
        Label height = new Label("Height:");
        grid.add(height, 0, 3);
        TextField heightInput = new TextField();
        heightInput.setText("");
        grid.add(heightInput, 1, 3);
        
        Label bodyTemp = new Label("Body Temp:");
        grid.add(bodyTemp, 0, 4);
        TextField bodyTempInput = new TextField();
        bodyTempInput.setText("");
        grid.add(bodyTempInput, 1, 4);
        
        Label bloodP = new Label("Blood Pressure:");
        grid.add(bloodP, 0, 5);
        TextField bloodPInput = new TextField();
        bloodPInput.setText("");
        grid.add(bloodPInput, 1, 5);

        RadioButton  rb = new RadioButton ();
        rb.setText("Over 12?");
        rb.setOnAction(event -> {
            if (rb.isSelected()) {
                weightInput.setDisable(true);
                heightInput.setDisable(true);
                bodyTempInput.setDisable(true);
                bloodPInput.setDisable(true);
            } else {
                weightInput.setDisable(false);
                heightInput.setDisable(false);
                bodyTempInput.setDisable(false);
                bloodPInput.setDisable(false);
            }
        });
        setHalignment(rb, HPos.LEFT);
        grid.add(rb, 0, 7);
        
        Button saveBtn = SetupButton("Save");
        saveBtn.setMinWidth(100);
        
        setHalignment(saveBtn, HPos.CENTER);
        grid.add(saveBtn, 2, 7);
        saveBtn.setOnAction(event -> {
            if (!rb.isSelected() && (weightInput.getText().isEmpty() || heightInput.getText().isEmpty() ||
                    bodyTempInput.getText().isEmpty() || bloodPInput.getText().isEmpty()) ) {

                errorLabel.setText("Error: Missing input");
                errorLabel.setVisible(true);
            } else {
                saveBtn.setDisable(true);
                if (patientID.isEmpty()) {
                    errorLabel.setText("Error: No ID");
                    errorLabel.setVisible(true);
                    return;
                }

                errorLabel.setText("PatientID: " + patientID);
                errorLabel.setVisible(true);
                visit.setVisit(visit.getCurrentVisit());
                if (rb.isSelected()) {
                    visit.saveVisitVitals("no", "n/a", "n/a",
                            "n/a", "n/a");
                } else {
                    visit.saveVisitVitals("yes", weightInput.getText(), heightInput.getText(),
                            bodyTempInput.getText(), bloodPInput.getText());
                }
            }
        });
        
        backVisitBtn.setAlignment(Pos.CENTER);
        setHalignment(backVisitBtn, HPos.CENTER);
        grid.add(backVisitBtn, 1, 7);
        
        layout.add(grid, 0, 1);
        
        patientIntakeScene = new Scene(layout, this.width, this.height);
        return patientIntakeScene;

    }    
//--------------------------------------------------------------------  
    private Scene HealthMenu() {
        menu.setText("Patient Health");        
        GridPane layout = new GridPane();
        menu.setAlignment(Pos.CENTER);
        setHalignment(menu, HPos.CENTER);
        layout.setAlignment(Pos.CENTER);
        layout.add(menu, 0, 0);
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        Label errorLabel = new Label();  
        errorLabel.setAlignment(Pos.CENTER);
        setHalignment(errorLabel, HPos.CENTER);
        errorLabel.setText("Error: Missing input"); 
        errorLabel.setVisible(false);
        layout.add(errorLabel, 0, 2);
        
        Label allergies = new Label("Allergies:");
        grid.add(allergies, 0, 2);
        TextArea allergiesInput = SetupDataInput();
        allergiesInput.setText("");
        grid.add(allergiesInput, 1, 2);
        
        Label healthConc = new Label("Health Concerns:");
        grid.add(healthConc, 0, 3);
        TextArea healthConcInput = SetupDataInput();
        healthConcInput.setText("");
        grid.add(healthConcInput, 1, 3);
        
        Button saveBtn = SetupButton("Save");
        saveBtn.setMinWidth(100);
        
        setHalignment(saveBtn, HPos.CENTER);
        grid.add(saveBtn, 2, 7);
        saveBtn.setOnAction(event -> {
            if (allergiesInput.getText().isEmpty() || healthConcInput.getText().isEmpty()) {
                errorLabel.setText("Error: Missing input");
                errorLabel.setVisible(true);
            } else {
                saveBtn.setDisable(true);
                if (patientID.isEmpty()) {
                    errorLabel.setText("Error: No ID");
                    errorLabel.setVisible(true);
                    return;
                }

                errorLabel.setText("PatientID: " + patientID);
                errorLabel.setVisible(true);
                visit.setVisit(visit.getCurrentVisit());
                visit.saveVisitHealth(allergiesInput.getText(), healthConcInput.getText());

            }
        });
        
        backVisitBtn.setAlignment(Pos.CENTER);
        setHalignment(backVisitBtn, HPos.CENTER);
        grid.add(backVisitBtn, 1, 7);
        
        layout.add(grid, 0, 1);
        
        patientIntakeScene = new Scene(layout, this.width, this.height);
        return patientIntakeScene;

    }
}
