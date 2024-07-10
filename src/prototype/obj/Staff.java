package prototype.obj;


import java.io.BufferedWriter;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner; // Import the Scanner class to read text files

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.*;
import prototype.data.Authentication;

interface StaffInterface {

}
public class Staff implements StaffInterface{
    private String userDir = "./src/prototype/users.json";
    private String staffDir = "./src/prototype/staff/";
    private final String staffListFilename = "staffList.txt";
    private String fileDATA = "";
    private BufferedWriter output = null;
    private JSONObject jo;
    private JSONArray ja;
    private Authentication auth;// = new Authentication(WORKINGPATH);

    private String WORKINGPATH = "";
    public void setWorkingPath(String path) {
        this.WORKINGPATH = path;
        this.userDir = WORKINGPATH + "/users.json";
        this.staffDir = WORKINGPATH + "/staff/";
        buildStaffList();

    }

    public Staff(String path) {
        WORKINGPATH = path;
        auth = new Authentication(WORKINGPATH);
        this.userDir = WORKINGPATH + "/users.json";
        this.staffDir = WORKINGPATH + "/staff/";
        buildStaffList();

    }
    private void buildStaffList() {
        ja = listCheck(staffDir + staffListFilename);
        try {
            File file = new File(userDir);
            Scanner readFile = new Scanner(file);
            fileDATA = "";
            while (readFile.hasNextLine()) {
                fileDATA = fileDATA.concat(readFile.nextLine());
            }
            readFile.close();
            JSONArray ja = new JSONArray(fileDATA);
            String userInfo, userName, userID;

            for (int i = 0; i < ja.length(); i++) {
                userInfo = ja.get(i).toString();
                jo = new JSONObject(userInfo);

                try {
                    userName = jo.get("username").toString();
                    userID = jo.get("patientID").toString();
                    if (getAcctType(jo) == Authentication.accountType.DOCTOR || getAcctType(jo) == Authentication.accountType.NURSE) {
                        addToStaffList(userID, userName, getAcctType(jo));
                    }
                } catch (Exception e) {
                    System.out.println("[Error] bad file");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("[Error]  staff file not found");
        }

    }
    private void addToStaffList(String patientID, String username, Authentication.accountType acct) {
        try {
            jo = new JSONObject();
            jo.put("patientID", patientID);
            jo.put("username", username);
            jo.put("type", acct.toString());

            //System.out.println("Adding to staff list");   //debug
            File file3 = new File(staffDir + staffListFilename);
            Scanner readFile = new Scanner(file3);
            String fileDATA = "";
            while (readFile.hasNextLine()) {
                fileDATA = fileDATA.concat(readFile.nextLine());
            }
            readFile.close();
            ja = new JSONArray(fileDATA);
            for (int i = 0; i < ja.length(); i++) {
                if (ja.getJSONObject(i).getString("patientID").equals(patientID)) {
                    //System.out.println("Already in staff list");  //debug
                    return;
                }
            }
            ja.put(jo);
            output = new BufferedWriter(new FileWriter(file3));
            output.write(ja.toString(2));
            output.close();
            //System.out.println("Added to staff list");    //debug
        } catch (IOException ex) {
            System.out.println("[Error] Couldn't save staff list");
        }

    }

    public ObservableList<JSONObject> getStaffList() {

        ObservableList<JSONObject> items = FXCollections.observableArrayList (); //testing
        ja = listCheck(staffDir + staffListFilename);
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
                items.add(jo);
            } catch (JSONException e) {
                System.out.println("[Error] key doesnt exist");
            }
        }
        return items;
    }

    private JSONArray listCheck(String pathToFile) {
        JSONArray ja = new JSONArray();
        try {
            File rootPathCtrl = new File(staffDir);
            boolean bool = rootPathCtrl.mkdir();
            if(bool){
                System.out.println("[Info] Staff folder is created successfully");  //debug
            }
            File file = new File(pathToFile);
            bool = file.createNewFile();
            if (bool) {
                System.out.println("[Info] New File created");  //debug
            }

            if (file.length() < 1) {
                System.out.println("[Info] Empty File: populating");    //debug
                output = new BufferedWriter(new FileWriter(file));
                output.write("[]");

                output.close();
                return ja;
            }
            
            Scanner readFile = new Scanner(file);
            //read file-----------------------------
            fileDATA = "";
            while (readFile.hasNextLine()) {
                fileDATA = fileDATA.concat(readFile.nextLine());
            }
            readFile.close();
            ja = new JSONArray(fileDATA);
            return ja;
        } catch (FileNotFoundException e) {
            System.out.println("[Error] staff file not found");
        } catch (IOException ex) {
            System.out.println("[Error] Couldn't save staff file");
        }
        return ja;
    }
    private Authentication.accountType getAcctType(JSONObject jo) {
        String type = jo.get("type").toString();
        switch (type) {
            case "PATIENT":
                return Authentication.accountType.PATIENT;

            case "DOCTOR":
                return Authentication.accountType.DOCTOR;

            case "NURSE":
                return Authentication.accountType.NURSE;


            case "ADMIN":
                return Authentication.accountType.ADMIN;

            default:
                return Authentication.accountType.NONE;
        }
    }
}
