/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype.obj;

/**
 *
 * @author andreas lees
 */

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

@SuppressWarnings("ALL")
interface PatientInterface {
    
    void deletePatient();
    String getInfoFile();
    String getName();
    String getFirstName();
    boolean savePatient(String firstName, String lastName,String email,String phone,String healthHist,String insID, String bDay);
    void setWorkingPath(String path);
    String getID();
    //returns a list of patients
    ObservableList<JSONObject> getPatientList();
    JSONObject loadPatientFile();
    /*
    Private methods:
        JSONArray listCheck(String pathToFile);
        JSONObject fileCheck();
        Authentication.accountType getAcctType(JSONObject jo);
        void delSubFiles(File file);
        void resetPatientListFile();
        buildPatientList();
        void addToPatientList(String patientID, String username, Authentication.accountType acct);
        void removeFromPatientList();
        void addToUserList();
        void removeFromUserList();
     */
}

@SuppressWarnings("ALL")
public class Patient implements PatientInterface{
    
    private String filePath = "";   // "./src/prototype/patients/";
    private final String infoName = "_PatientInfo.txt";
    private final String patientListFilename = "patientList.txt";
    private String fileDATA = "";
    private final String patientID;
    private BufferedWriter output = null;
    private JSONObject jo;
    private JSONArray ja;
    
//--------------------constructors------------------------------------------------
    public Patient(String patientID, String path) {
        this.patientID = patientID;
        setWorkingPath(path);
        buildPatientList();
    }
    
    public Patient(String path) {
        this.patientID = "";
        setWorkingPath(path);
        buildPatientList();
    }
//-------------------public methods-------------------------------------------------
    protected String WORKINGPATH = "";
    public void setWorkingPath(String path) {
        this.WORKINGPATH = path;
        this.filePath = WORKINGPATH + "/patients/";
    }
    @Override
    public void deletePatient() {
        if (patientID == null || patientID.isEmpty()){
            return;
        }
        File file = new File(filePath + patientID);
        if (file.exists()) {
            //check file/dir exists then delete it
            if (!file.delete()) {
                //if dir contains files
                System.out.println("[Info] Non-empty dir, deleting sub-files");   //debug
                delSubFiles(file);
                file.delete();
            }
        }
        System.out.println("[Info] Deleted patient!");   //debug
            removeFromPatientList();
            removeFromUserList();
    }
    
    @Override
    public String getInfoFile() {
        return this.infoName;
    }
    @Override
    public String getName() {
        loadPatientFile();
        return jo.getString("firstName") + " " + jo.getString("lastName");
    }
    @Override
    public String getFirstName() {
        loadPatientFile();
        try {
            return jo.getString("firstName");
        } catch (JSONException e) {
            System.out.println("[Error] bad file");
            return "";
        }
    }
    public String getID() {
        return patientID;
    }
    @Override
    public boolean savePatient(String firstName, String lastName,String email,String phone,String healthHist,String insID, String bDay) {
        
        jo = fileCheck();
        
        File file = new File(filePath + patientID + "/" + patientID + infoName);
        jo.put("patientID", patientID);
        jo.put("firstName", firstName);
        jo.put("lastName", lastName);
        jo.put("email", email);
        jo.put("phone", phone);
        jo.put("healthHist", healthHist);
        jo.put("insID", insID);
        jo.put("birthday", bDay);
        //save to file
        System.out.println("[Info] Saving patient file");   //debug

        try {
            output = new BufferedWriter(new FileWriter(file));
            output.write(jo.toString(2));

            output.close();
            //System.out.println("[Info] File Saved"); //debug
            //add to patientList
            addToPatientList(patientID, firstName, Authentication.accountType.PATIENT);
            //add to userList
            addToUserList();
            return true;
        } catch (IOException ex) {
            System.out.println("[Error] Error saving patient file");
        }
        
        return false;
    }
    @Override
    public ObservableList<JSONObject> getPatientList() {

        ObservableList<JSONObject> items = FXCollections.observableArrayList (); //testing
        ja = listCheck(filePath + patientListFilename);
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
    
    @Override
    public JSONObject loadPatientFile() {
        if (patientID.isEmpty()) {
            jo = new JSONObject();
            return jo;
            
        }
        jo = fileCheck();
        if (jo.isEmpty()) {
            //System.out.println("bad file");
        }
        return jo;
    }
    
//------------private methods--------------------------------------------------------
    private void delSubFiles(File file) {
        File[] subFiles = file.listFiles();
        for (int i=0; i < subFiles.length; i++) {
            if (!subFiles[i].delete()) {
                delSubFiles(subFiles[i]);
                subFiles[i].delete();
            }
        }
        
    }
    private void resetPatientListFile() {
        try {
            File file = new File(filePath + patientListFilename);
            file.createNewFile();
            output = new BufferedWriter(new FileWriter(file));
            output.write("[]");
            output.close();
            buildPatientList();

        } catch (IOException e) {
            System.out.println("[Error] Error opening patient file");
        }
    }
    private void buildPatientList() {
        try {

            File file = new File(WORKINGPATH + "/users.json");
            Scanner readFile = new Scanner(file);
            fileDATA = "";
            while (readFile.hasNextLine()) {
                fileDATA = fileDATA.concat(readFile.nextLine());
            }
            JSONArray ja = new JSONArray(fileDATA);
            String userInfo, userName, userID;

            for (int i = 0; i < ja.length(); i++) {
                userInfo = ja.get(i).toString();
                jo = new JSONObject(userInfo);

                try {
                    userName = jo.get("username").toString();
                    userID = jo.get("patientID").toString();
                    if (getAcctType(jo) == Authentication.accountType.PATIENT) {
                        //System.out.println("Match Found");    //debug
                        addToPatientList(userID, userName, getAcctType(jo));
                    }
                } catch (Exception e) {
                    System.out.println("[Error] Malformed file, rebuilding");
                    resetPatientListFile();
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("[Error] Error patient file not found");
        }

    }
    
    private void addToPatientList(String patientID, String username, Authentication.accountType acct) {
        try {
            jo = new JSONObject();
            jo.put("patientID", patientID);
            jo.put("username", username);
            jo.put("type", acct.toString());

            //System.out.println("[Info] Adding to patient list"); //debug
            File file3 = new File(filePath + patientListFilename);
            Scanner readFile = new Scanner(file3);
            String fileDATA = "";
            while (readFile.hasNextLine()) {
                fileDATA = fileDATA.concat(readFile.nextLine());
            }
            ja = new JSONArray(fileDATA);
            for (int i = 0; i < ja.length(); i++) {
                if (ja.getJSONObject(i).getString("patientID").equals(patientID)) {
                    //System.out.println("[Info] Already in patient list");    //debug
                    return;
                }
            }
            ja.put(jo);
            output = new BufferedWriter(new FileWriter(file3));
            output.write(ja.toString(2));
            output.close();
            //System.out.println("[Info] Added to patient list");  //debug
        } catch (IOException ex) {
            System.out.println("[Error] Error saving patient list");
        }

    }
    //need to update to json array format
    private void removeFromPatientList() {
        try {
            JSONObject jo2;

            System.out.println("[Info] Removing from patient list");   //debug
            File file3 = new File(filePath + patientListFilename);
            Scanner readFile = new Scanner(file3);
            String fileDATA = "";
            while (readFile.hasNextLine()) {
                fileDATA = fileDATA.concat(readFile.nextLine());
            }
            readFile.close();
            JSONArray ja = new JSONArray(fileDATA);

            for (int i=0; i < ja.length(); i++) {
                jo2 = new JSONObject(ja.get(i).toString());
                if (jo2.has("patientID") && jo2.getString("patientID").equals(patientID)) {
                    //System.out.println("[Info] Match found"); //debug
                    ja.remove(i);
                    break;
                }
            }
            output = new BufferedWriter(new FileWriter(file3));
            output.write(ja.toString(2));

            output.close();
            //System.out.println("[Info] Removed from patient list");  //debug


        } catch (IOException ex) {
            System.out.println("[Error] Error saving patient list");
        }
        
    }
    private void addToUserList() {
        try {
            JSONObject jo2 = new JSONObject();
            jo2.put("patientID", this.patientID);
            jo2.put("username", this.getFirstName());
            jo2.put("pass", this.patientID);
            jo2.put("type", "PATIENT");
            
            //JSONArray ja = new JSONArray();
            //System.out.println("[Info] Adding to user list");   //debug
            File file3 = new File(WORKINGPATH + "/users.json");
            Scanner readFile = new Scanner(file3);
            String fileDATA = "";
            while (readFile.hasNextLine()) {
                fileDATA = fileDATA.concat(readFile.nextLine());
            }
            JSONArray ja = new JSONArray(fileDATA);
            for (int i = 0; i < ja.length(); i++) {
                if (ja.getJSONObject(i).getString("patientID").equals(patientID)) {
                    //System.out.println("[Info] Already in user list");    //debug
                    return;
                }
            }
            ja.put(jo2);
            output = new BufferedWriter(new FileWriter(file3));
            output.write(ja.toString(2));

            output.close();
            //System.out.println("[Info] Added to user list"); //debug
            
            
        } catch (IOException ex) {
            System.out.println("[Error] Error saving user list");
        }
        
    }
    private void removeFromUserList() {
        try {
            JSONObject jo2;
            
            //JSONArray ja = new JSONArray();
            System.out.println("[Info] Removing from user list");
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
                if (jo2.has("patientID") && jo2.getString("patientID").equals(patientID)) {
                    //System.out.println("[Info] Match found");
                    ja.remove(i);
                    break;
                }
            }
            output = new BufferedWriter(new FileWriter(file3));
            output.write(ja.toString(2));

            output.close();
            //System.out.println("[Info] Removed from user list");
            
            
        } catch (IOException ex) {
            System.out.println("[Error] Error saving user list");
        }
        
    }
    private JSONArray listCheck(String pathToFile) {
        JSONArray ja = new JSONArray();
        try {
            File rootPathCtrl = new File(filePath);
            //System.out.println(filePath); //debug
            boolean bool = rootPathCtrl.mkdir();
            if(bool){
                System.out.println("[Info] Patient folder is created successfully");
            }
            File file = new File(pathToFile);
            bool = file.createNewFile();
            if (bool) {
                System.out.println("[Info] New File created");
            }

            if (file.length() < 1) {
                System.out.println("[Info] Empty File: populating");
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
            System.out.println("[Error] patient file not found");
        } catch (IOException ex) {
            System.out.println("[Error] Error saving patient file");
        } catch (JSONException ex) {
            System.out.println("[Error] bad file (JSON)");
        }
        return ja;
    }
    private JSONObject fileCheck() {
        System.out.println(filePath+ " id: " + patientID);
        try {
            // check/create folder--------------------
            File filePathCtrl = new File(filePath);
            boolean bool = filePathCtrl.mkdir();
            if(bool){
               System.out.println("[Info] Folder is created successfully");
            }
            filePathCtrl = new File(filePath + patientID);
            bool = filePathCtrl.mkdir();
            if(bool){
                System.out.println("[Info] Folder is created successfully");
            }
            // check/create file---------------------
            File file = new File(filePath + patientID + "/" + patientID + infoName);
            bool = file.createNewFile();
            if (bool) {
                System.out.println("[Info] New File created");
            }

            if (file.length() < 1) {
                System.out.println("[Info] Empty File: populating");
                JSONObject jo = new JSONObject();
                jo.put("patientID", patientID);
                jo.put("firstName", "err");
                jo.put("lastName", "err");
                jo.put("email", "err");
                jo.put("phone", "err");
                jo.put("healthHist", "err");
                jo.put("insID", "err");
                jo.put("birthday", "00/00/0000");
                
                output = new BufferedWriter(new FileWriter(file));
                output.write(jo.toString(2));
                
                output.close();
                return jo;
            }
            
            Scanner readFile = new Scanner(file);
            //---------------------------------------
            //read file-----------------------------
            fileDATA = "";
            while (readFile.hasNextLine()) {
                fileDATA = fileDATA.concat(readFile.nextLine());
            }
            //CLOSE FILES WHEN DONE WITH THEM DUMMY
            readFile.close();
            JSONObject jo = new JSONObject(fileDATA);
            if (jo.isEmpty()) {
                jo.put("patientID", patientID);
                jo.put("firstName", "err");
                jo.put("lastName", "err");
                jo.put("email", "err");
                jo.put("phone", "err");
                jo.put("healthHist", "err");
                jo.put("insID", "err");
                jo.put("birthday", "00/00/0000");

                output = new BufferedWriter(new FileWriter(file));
                output.write(jo.toString(2));

                output.close();
                return jo;

            }
            return jo;
            
        } catch (FileNotFoundException e) {
            System.out.println("[Error] patient file not found");
        } catch (IOException ex) {
            System.out.println("[Error] Error saving patient file (filecheck)");
        } catch (JSONException ex) {
            System.out.println("[Error] bad file (JSON)");
            File file = new File(filePath + patientID + "/" + patientID + infoName);
            JSONObject jo = new JSONObject();
            jo.put("patientID", patientID);
            jo.put("firstName", "err");
            jo.put("lastName", "err");
            jo.put("email", "err");
            jo.put("phone", "err");
            jo.put("healthHist", "err");
            jo.put("insID", "err");
            jo.put("birthday", "00/00/0000");

            try {
                output = new BufferedWriter(new FileWriter(file));
                output.write(jo.toString(2));
                output.close();
            } catch (IOException e) {
                System.out.println("[Error] ewwie");
            }

            return jo;
        }
            JSONObject jo = new JSONObject();
            return jo;
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
