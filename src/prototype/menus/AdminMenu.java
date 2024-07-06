package prototype.menus;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.WindowEvent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import prototype.data.Authentication;
import prototype.data.InputValidation;
import prototype.data.STAFFCellFactory;
import prototype.obj.Patient;
import prototype.obj.Visit;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class AdminMenu extends Menus{

    private String userName = "";
    private String fileDATA = "";
    private BufferedWriter output = null;
    private JSONObject jo;
    private JSONArray ja;
    private InputValidation check = new InputValidation();
    ObservableList<String> options =
            FXCollections.observableArrayList(
                    "DOCTOR",
                    "PATIENT",
                    "NURSE"
            );
    final ComboBox comboBox = new ComboBox(options);

    protected void updateUserList() {
        list.setCellFactory(new STAFFCellFactory());
        list.setItems(getUserList());
    }
    public ObservableList<JSONObject> getUserList() {

        ObservableList<JSONObject> items = FXCollections.observableArrayList (); //testing
        File file3 = new File(WORKINGPATH + "/users.json");
        Scanner readFile = null;
        try {
            readFile = new Scanner(file3);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String fileDATA = "";
        while (readFile.hasNextLine()) {
            fileDATA = fileDATA.concat(readFile.nextLine());
        }
        ja = new JSONArray(fileDATA);
        if (ja.isEmpty()) {
            return items;
        }
        ArrayList<String> test = new ArrayList<String>();
        for (int i=0; i < ja.length(); i++) {
            jo = ja.getJSONObject(i);
            try {
                jo.getString("patientID");
                jo.getString("username");
                jo.getString("type");
                if (!getAcctType(jo.getString("type")).equals(Authentication.accountType.ADMIN)) {
                    items.add(jo);
                }
            } catch (JSONException e) {
                System.out.println("key doesnt exist");
            }
        }
        return items;
    }
    private void addToUserList(String ID, String username, String pass, Authentication.accountType type) {
        if (!check.NameCheck(username) || !check.IntCheck(ID)) {
            return;
        }
        if (ID.isEmpty() || username.isEmpty() || pass.isEmpty()) {
            return;
        }
        try {
            JSONObject jo2 = new JSONObject();
            jo2.put("patientID", ID);
            jo2.put("username", username);
            jo2.put("pass", pass);
            jo2.put("type", type);

            //JSONArray ja = new JSONArray();
            System.out.println("Adding to user list");
            File file3 = new File(WORKINGPATH + "/users.json");
            Scanner readFile = new Scanner(file3);
            String fileDATA = "";
            while (readFile.hasNextLine()) {
                fileDATA = fileDATA.concat(readFile.nextLine());
            }
            JSONArray ja = new JSONArray(fileDATA);
            for (int i = 0; i < ja.length(); i++) {
                if (ja.getJSONObject(i).getString("patientID").matches(patientID)) {
                    System.out.println("Already in user list");
                    return;
                }
            }
            ja.put(jo2);
            output = new BufferedWriter(new FileWriter(file3));
            output.write(ja.toString());

            output.close();
            System.out.println("Added to user list");


        } catch (IOException ex) {
            System.out.println("Error saving file");
        }

    }
    private void removeFromUserList() {
        try {
            JSONObject jo2;

            //JSONArray ja = new JSONArray();
            System.out.println("Removing from user list");
            File file3 = new File(WORKINGPATH + "/users.json");
            Scanner readFile = new Scanner(file3);
            String fileDATA = "";
            while (readFile.hasNextLine()) {
                fileDATA = fileDATA.concat(readFile.nextLine());
            }
            readFile.close();
            JSONArray ja = new JSONArray(fileDATA);

            for (int i=0; i < ja.length(); i++) {
                jo2 = new JSONObject(ja.get(i).toString());
                if (jo2.has("patientID") && jo2.getString("patientID").matches(patientID)) {
                    System.out.println("Match found");
                    ja.remove(i);
                    break;
                }
            }
            output = new BufferedWriter(new FileWriter(file3));
            output.write(ja.toString());

            output.close();
            System.out.println("Removed from user list");


        } catch (IOException ex) {
            System.out.println("Error saving file");
        }

    }

    AdminMenu(String path) {
        WORKINGPATH = path;

        updateUserList();
        this.setTitle("Main Menu");

        GridPane layout = new GridPane();
        layout.setAlignment(Pos.CENTER);

        Label scenetitle = SetupTitleLabel("Welcome to " + companyName);
        layout.add(scenetitle, 0, 0);

        this.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                closeExtraWindow();
                System.out.println("bye bye");
            }
        });

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setHgap(5);
        grid.setVgap(15);
        layout.add(grid, 1, 1);

        Button pIntakeBtn = SetupButton("New User");
        Button patientDelBtn = SetupButton("Delete User");
        Button messageBtn = SetupButton("Message Portal");

        //collect list of users
        updateUserList();
        list.setPrefWidth(200);
        list.setPrefHeight(300);
        layout.add(list, 0, 1);

        column= 0;
        row = 0;
        grid.add(pIntakeBtn, column, row);
        row++;
        grid.add(patientDelBtn, column, row);
        row++;
        row++;
        grid.add(logoutBtn, column, row);


        pIntakeBtn.setOnAction(event -> {
            changeTitle("Patient Intake");
            changeScene(IntakeMenu());
        });
        patientDelBtn.setOnAction(event -> {
            if (list.getSelectionModel().getSelectedItem() == null) {
                return;
            }
            patientID = list.getSelectionModel().getSelectedItem().getString("patientID");
            userName = list.getSelectionModel().getSelectedItem().getString("username");
            if (patientID == null || patientID.isEmpty()) {
                return;
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + userName + " ?\nID: " + patientID, ButtonType.NO, ButtonType.YES);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                removeFromUserList();
                updateUserList();
            }
        });
        backBtn.setOnAction(event -> {
            changeTitle("Main Menu");
            changeScene(loginScene);
            updateUserList();
        });

        loginScene = new Scene(layout, width, height);
        this.setScene(loginScene);
        this.show();
    }
    protected Scene IntakeMenu() {
        Scene patientIntakeScene;


        Label menu = SetupTitleLabel("User Intake Form");
        GridPane layout = new GridPane();
        layout.setAlignment(Pos.CENTER);

        layout.add(menu, 0, 0);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));



        Label scenetitle = SetupTitleLabel("Username:");
        grid.add(scenetitle, 0, 1);
        TextField usernameInput = new TextField();
        usernameInput.setText("");
        grid.add(usernameInput, 1, 1);

        Label errorLabel = SetupTitleLabel("Error: Missing input");
        errorLabel.setVisible(false);
        layout.add(errorLabel, 0, 2);



        Label passLbl = SetupTitleLabel("Password:");
        grid.add(passLbl, 0, 2);
        PasswordField passInput = new PasswordField();
        passInput.setText("");
        grid.add(passInput, 1, 2);

        Label idLbl = SetupTitleLabel("ID:");
        grid.add(idLbl, 0, 3);
        TextField idInput = new TextField();
        idInput.setText("");
        grid.add(idInput, 1, 3);

        Label typeLbl = SetupTitleLabel("Account type:");
        grid.add(typeLbl, 0, 4);
        grid.add(comboBox, 1, 4);
        comboBox.getValue();

        Button saveBtn = SetupButton("Save");
        saveBtn.setMinWidth(100);

        grid.add(saveBtn, 2, 7);
        saveBtn.setOnAction(event -> {

            if (usernameInput.getText().isEmpty() || passInput.getText().isEmpty() || idInput.getText().isEmpty() || comboBox.getValue() == null) {

                errorLabel.setText("Error: Missing input");
                errorLabel.setVisible(true);
            } else {
                //patientID is fistName, lastName, birthday
                InputValidation check = new InputValidation();
                if (!check.IntCheck(idInput.getText()) || !check.NameCheck(usernameInput.getText())) {

                    errorLabel.setVisible(true);
                    errorLabel.setText("Error: Bad input");
                    return;
                }
                saveBtn.setDisable(true);
                usernameInput.setDisable(true);
                passInput.setDisable(true);
                idInput.setDisable(true);

                patientID = idInput.getText();


                errorLabel.setText("Password: " + passInput.getText());
                errorLabel.setVisible(true);

                addToUserList(patientID, usernameInput.getText(), passInput.getText(), getAcctType(comboBox.getValue().toString()));

            }
        });

        grid.add(backBtn, 1, 7);

        layout.add(grid, 0, 1);

        patientIntakeScene = new Scene(layout, width, height);
        return patientIntakeScene;

    }
    private Authentication.accountType getAcctType(String type) {
        switch (type) {
            case "PATIENT":
                // code block
                return Authentication.accountType.PATIENT;

            case "DOCTOR":
                // code block
                return Authentication.accountType.DOCTOR;

            case "NURSE":
                // code block
                return Authentication.accountType.NURSE;


            case "ADMIN":
                // code block
                return Authentication.accountType.ADMIN;

            default:
                // code block
                return Authentication.accountType.NONE;
        }
    }
}
