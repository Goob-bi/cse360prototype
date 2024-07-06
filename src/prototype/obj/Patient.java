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
import java.util.List;
import java.util.Scanner; // Import the Scanner class to read text files

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.*;
import prototype.data.Authentication;

interface PatientInterface {
    
    void deletePatient();
    String getInfoFile();
    String getName();
    String getFirstName();
    boolean savePatient(String firstName, String lastName,String email,String phone,String healthHist,String insID, String bDay);
    //returns a list of patients
    ObservableList<JSONObject> getPatientList();
    JSONObject loadPatientFile();
}

public class Patient implements PatientInterface{
    
    private String filePath = "./src/prototype/patients/";
    private final String infoName = "_PatientInfo.txt";
    private final String patientListFilename = "patientList.txt";
    private String fileDATA = "";
    private final String patientID;
    private BufferedWriter output = null;
    private JSONObject jo;
    private JSONArray ja;
    
//--------------------constructors------------------------------------------------
public Patient(String patientID, String path) {
        System.out.println("double \n"+path);
        this.patientID = patientID;
        setWorkingPath(path);
        buildPatientList();
    }
    
    public Patient(String path) {
        this.patientID = "";
        System.out.println("single \n"+path);
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
                System.out.println("Couldnt delete, non-empty dir");   //debug
                delSubFiles(file);
                file.delete();
            }
        }
        System.out.println("Deleted!");   //debug
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
        return jo.getString("firstName");
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
        System.out.println("Saving file");

        try {
            output = new BufferedWriter(new FileWriter(file));
            output.write(jo.toString());

            output.close();
            System.out.println("File Saved"); 
            //add to patientList
            addToPatientList(patientID, firstName, Authentication.accountType.PATIENT);
            //add to userList
            addToUserList();
            return true;
        } catch (IOException ex) {
            System.out.println("Error saving file"); 
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
                System.out.println("key doesnt exist");
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
    private void buildPatientList() {
        //ja = listCheck(filePath + patientListFilename);
        try {
            File file = new File(filePath + patientListFilename);
            output = new BufferedWriter(new FileWriter(file));
            output.write("[]");

            output.close();

            file = new File(WORKINGPATH + "/users.json");
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

                        System.out.println("Match Found");
                        addToPatientList(userID, userName, getAcctType(jo));
                    }
                } catch (Exception e) {
                    //System.out.println("bad file");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error opening staff file");
        } catch (IOException e) {
            System.out.println("Error opening staff file");
        }

    }
    
    private void addToPatientList(String patientID, String username, Authentication.accountType acct) {
        try {
            jo = new JSONObject();
            jo.put("patientID", patientID);
            jo.put("username", username);
            jo.put("type", acct.toString());

            System.out.println("Adding to patient list");
            File file3 = new File(filePath + patientListFilename);
            Scanner readFile = new Scanner(file3);
            String fileDATA = "";
            while (readFile.hasNextLine()) {
                fileDATA = fileDATA.concat(readFile.nextLine());
            }
            ja = new JSONArray(fileDATA);
            for (int i = 0; i < ja.length(); i++) {
                if (ja.getJSONObject(i).getString("patientID").matches(patientID)) {
                    System.out.println("Already in patient list");
                    return;
                }
            }
            ja.put(jo);
            output = new BufferedWriter(new FileWriter(file3));
            output.write(ja.toString());
            output.close();
            System.out.println("Added to patient list");
        } catch (IOException ex) {
            System.out.println("Error saving file");
        }

    }
    //need to update to json array format
    private void removeFromPatientList() {
        try {
            JSONObject jo2;

            //JSONArray ja = new JSONArray();
            System.out.println("Removing from user list");
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
    private void addToUserList() {
        try {
            JSONObject jo2 = new JSONObject();
            jo2.put("patientID", this.patientID);
            jo2.put("username", this.getFirstName());
            jo2.put("pass", this.patientID);
            jo2.put("type", "PATIENT");
            
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
    private JSONArray listCheck(String pathToFile) {
        JSONArray ja = new JSONArray();
        try {
            File rootPathCtrl = new File(filePath);
            boolean bool = rootPathCtrl.mkdir();
            if(bool){
                System.out.println("Staff folder is created successfully");
            }
            File file = new File(pathToFile);
            bool = file.createNewFile();
            if (bool) {
                System.out.println("New File created");
            }

            if (file.length() < 1) {
                System.out.println("Empty File: populating");
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
            System.out.println("Error opening staff file");
        } catch (IOException ex) {
            System.out.println("Error opening staff file");
        } catch (JSONException ex) {
            System.out.println("Error opening staff file (JSON)");
        }
        return ja;
    }
    private JSONObject fileCheck() {
        try {
            // check/create folder--------------------
            File filePathCtrl = new File(filePath + patientID);
            boolean bool = filePathCtrl.mkdir();  
            if(bool){  
               System.out.println("Folder is created successfully");  
            }
            // check/create file---------------------
            File file = new File(filePath + patientID + "/" + patientID + infoName);
            bool = file.createNewFile();
            if (bool) {
                System.out.println("New File created"); 
            }

            if (file.length() < 1) {
                System.out.println("Empty File: populating");
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
                output.write(jo.toString());
                
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
                output.write(jo.toString());

                output.close();
                return jo;

            }
            return jo;
            
        } catch (FileNotFoundException e) {
            System.out.println("Error opening patient file3");
        } catch (IOException ex) {
            System.out.println("Error opening patient file4");
        } catch (JSONException ex) {
            System.out.println("Error opening staff file (JSON)");
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
                output.write(jo.toString());
                output.close();
            } catch (IOException e) {
                System.out.println("ewwie");
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
