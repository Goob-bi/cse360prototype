/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype;

/**
 *
 * @author andreas lees
 */

import java.io.BufferedWriter;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner; // Import the Scanner class to read text files
import org.json.*;

interface PatientInterface {
    
    void deletePatient();
    String getInfoFile();
    String getName();
    String getFirstName();
    boolean savePatient(String firstName, String lastName,String email,String phone,String healthHist,String insID, String bDay);
    //returns a list of patients
    List getPatientList();
    JSONObject loadPatientFile();
}

public class Patient implements PatientInterface{
    
    private final String filePath = "./src/prototype/patients/";
    private final String infoName = "_PatientInfo.txt";
    private final String patientListFilename = "patientList.txt";
    private String fileDATA = "";
    private final String patientID;
    private BufferedWriter output = null;
    private JSONObject jo;
    
//--------------------constructors------------------------------------------------
    Patient(String patientID) {
        this.patientID = patientID;
        System.out.println(this.patientID);
        //filePath = filePath + this.patientID;
    }
    
    Patient() {
        this.patientID = "";
    }
//-------------------public methods-------------------------------------------------
    @Override
    public void deletePatient() {
        if (patientID == null || patientID.isEmpty()){
            return;
        }
        File file = new File(filePath + patientID);
        if (file.exists()) {
            System.out.println("Deleting: " + file.getPath());   //debug
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
        //System.out.println(jo.toString());

        try {
            output = new BufferedWriter(new FileWriter(file));
            output.write(jo.toString());

            output.close();
            System.out.println("File Saved"); 
            //add to patientList
            System.out.println("Adding to patient list"); 
            addToPatientList();
            //add to userList
            System.out.println("adding to user list"); 
            addToUserList();
            return true;
        } catch (IOException ex) {
            System.out.println("Error saving file"); 
        }
        
        return false;
    }
    @Override
    public List getPatientList() {
        JSONArray ja;
        //ja = listCheck(filePath + patientListFilename);
        jo = listCheck(filePath + patientListFilename);
        if (jo.isEmpty()) {
            //System.out.println("empty list"); //debug
            List list = java.util.Collections.emptyList();
            return list;
        }
        try {
            //System.out.println("grabbing array"); //debug
            ja = jo.getJSONArray("patientID");
        } catch (JSONException e) {
            System.out.println("converting to array");    //debug
            ja = jo.toJSONArray(new JSONArray("patientID"));
        }
        return ja.toList();
    }
    
    @Override
    public JSONObject loadPatientFile() {
        if (patientID.isEmpty()) {
            jo = new JSONObject();
            return jo;
            
        }
        jo = fileCheck();
        if (jo.isEmpty()) {
            System.out.println("bad file");
        }
        return jo;
    }
    
//------------private methods--------------------------------------------------------
    private void delSubFiles(File file) {
        File[] subFiles = file.listFiles();
        for (int i=0; i < subFiles.length; i++) {
            //System.out.println("Deleting: " + subFiles[i].getPath());   //debug
            if (!subFiles[i].delete()) {
                //System.out.println("Couldnt delete, non-empty dir");   //debug
                delSubFiles(subFiles[i]);
                subFiles[i].delete();
            }
        }
        
    }
    
    private void addToPatientList() {
        //System.out.println(filePath + patientListFilename);
        jo = listCheck(filePath + patientListFilename);
        //System.out.println(jo.toString());
        JSONArray ja;
        try {
            System.out.println(jo.toString());
            ja = jo.getJSONArray("patientID");
            for (int i = 0; i < ja.length(); i++) {
                if (ja.getString(i).matches(patientID)) {
                    System.out.println("Already in patient list");
                    return;
                }
            }
            jo.accumulate("patientID", patientID);
            File file2 = new File(filePath + patientListFilename);
            output = new BufferedWriter(new FileWriter(file2));
            output.write(jo.toString());
            output.close();
            System.out.println("Added to patient list"); 
        } catch (IOException ex) {
            System.out.println("Error saving file"); 
        } catch (JSONException e) {
            System.out.println("error adding patient");
        }
        
    }
    
    private void removeFromPatientList() {
        try {
            
            //JSONArray ja = new JSONArray();
            System.out.println("Removing from patient list"); 
            File file3 = new File(filePath + patientListFilename);
            Scanner readFile = new Scanner(file3);
            String fileDATA = "";
            while (readFile.hasNextLine()) {
                fileDATA = fileDATA.concat(readFile.nextLine());
            }
            //System.out.println(fileDATA);
            JSONObject jo2 = new JSONObject(fileDATA);
            JSONArray ja = new JSONArray(jo2.getJSONArray("patientID"));
            //System.out.println("test");
            //System.out.println(this.patientID);
            
            for (int i=0; i < ja.length(); i++) {
                //System.out.println(ja.get(i).toString());
                if (ja.get(i).toString().matches(patientID)) {
                    ja.remove(i);
                    jo2.put("patientID", ja);
                }
            }
            output = new BufferedWriter(new FileWriter(file3));
            output.write(jo2.toString());

            output.close();
            System.out.println("Removed from patient list"); 
            
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
            File file3 = new File("./src/prototype/users.json");
            Scanner readFile = new Scanner(file3);
            String fileDATA = "";
            while (readFile.hasNextLine()) {
                fileDATA = fileDATA.concat(readFile.nextLine());
            }
            //System.out.println(fileDATA);
            JSONArray ja = new JSONArray(fileDATA);
            for (int i = 0; i < ja.length(); i++) {
                if (ja.getJSONObject(i).getString("patientID").matches(patientID)) {
                    System.out.println("Already in user list");
                    return;
                }
            }
            //System.out.println("test");
            //System.out.println(this.patientID);
            ja.put(jo2);
            System.out.println(ja.toString());
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
            File file3 = new File("./src/prototype/users.json");
            Scanner readFile = new Scanner(file3);
            String fileDATA = "";
            while (readFile.hasNextLine()) {
                fileDATA = fileDATA.concat(readFile.nextLine());
            }
            readFile.close();
            //System.out.println(fileDATA);
            JSONArray ja = new JSONArray(fileDATA);
           // System.out.println("test"); 
           // System.out.println(this.patientID);
            
            for (int i=0; i < ja.length(); i++) {
                jo2 = new JSONObject(ja.get(i).toString());
            //System.out.println(jo2.toString());
                   // System.out.println(jo2.getString("patientID")); 
                if (jo2.has("patientID") && jo2.getString("patientID").matches(patientID)) {
                    System.out.println("Match found"); 
                    ja.remove(i);
                    break;
                }
            }
            //System.out.println(ja.toString());
            output = new BufferedWriter(new FileWriter(file3));
            output.write(ja.toString());

            output.close();
            System.out.println("Removed from user list"); 
            
            
        } catch (IOException ex) {
            System.out.println("Error saving file"); 
        }
        
    }
    private JSONObject listCheck(String pathToFile) {
        jo = new JSONObject();
        try {
            //File file = new File(filePath + patientListFilename);  
            File file = new File(pathToFile);  
            boolean bool = file.createNewFile();
            if (bool) {
                System.out.println("New File created"); 
            }
            
            if (file.length() < 1) {
                System.out.println("Empty File: populating"); 
               //JSONObject jo = new JSONObject();
             //   jo.accumulate("patientID", "");   //test
             //   System.out.println(jo.toString()); 
                
                output = new BufferedWriter(new FileWriter(file));
            //    output.write(jo.toString());
                output.write("{\"patientID\":[]}");
                
                output.close();
                return jo;
            }
            System.out.println("File Found!");
            Scanner readFile = new Scanner(file);
            //---------------------------------------
            //read file-----------------------------
            fileDATA = "";
            while (readFile.hasNextLine()) {
                fileDATA = fileDATA.concat(readFile.nextLine());
            }
            readFile.close();
            jo = new JSONObject(fileDATA);
            //jo.accumulate("patientID", "");
            //System.out.println(pathToFile);
            //System.out.println(fileDATA);
            //System.out.println("inside  list  check " + jo.toString());    //debug
            return jo;
        } catch (FileNotFoundException e) {
            System.out.println("Error opening file");
        } catch (IOException ex) {
            System.out.println("Error opening file");
        }
    //    JSONObject jo = new JSONObject();
        return jo;
    }
    private JSONObject fileCheck() {
        try {
            // check/create folder--------------------
            File filePathCtrl = new File(filePath + patientID);
            boolean bool = filePathCtrl.mkdir();  
            if(bool){  
               System.out.println("Folder is created successfully");  
            }else{  
               System.out.println("Error Found: Does the folder already exist?");  
            }  
            //---------------------------------------
            // check/create file---------------------
            File file = new File(filePath + patientID + "/" + patientID + infoName); //+ "_PatientInfo.txt" or vitals
            //System.out.println(filePath + patientID + "/" + patientID + infoName);
            bool = file.createNewFile();
            if (bool) {
                System.out.println("New File created"); 
            }
            
            if (file.length() < 1) {
                System.out.println("Empty File: populating"); 
                JSONObject jo = new JSONObject();
               // jo.put("patientID", patientID);   //test
               // System.out.println(jo.toString());
                
                output = new BufferedWriter(new FileWriter(file));
                output.write(jo.toString());
                
                output.close();
                return jo;
            }
            System.out.println("File Found!");
            Scanner readFile = new Scanner(file);
            //---------------------------------------
            //read file-----------------------------
            fileDATA = "";
            while (readFile.hasNextLine()) {
                fileDATA = fileDATA.concat(readFile.nextLine());
            }
            //CLOSE FILES WHEN DONE WITH THEM DUMMY
            readFile.close();
            //System.out.println("data: " + fileDATA);
            JSONObject jo = new JSONObject(fileDATA);
            //System.out.println(jo.toString());
            return jo;
            
        } catch (FileNotFoundException e) {
            System.out.println("Error opening file");
        } catch (IOException ex) {
            System.out.println("Error opening file");
        }
        JSONObject jo = new JSONObject();
        return jo;
    }
    
    
}
