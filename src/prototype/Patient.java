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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.*;

public class Patient {
    
    private String filePath = "./src/prototype/patients/";
    private String vitalName = "_PatientVitals.txt";
    private String healthName = "_PatientHealth.txt";
    private String infoName = "_PatientInfo.txt";
    private String patientListFilename = "patientList.txt";
    private String fileDATA = "";
    private final String patientID;
    private BufferedWriter output = null;
    private JSONObject jo;
    
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
            removePatient();
            removeUser();
    }
    private void delSubFiles(File file) {
        File[] subFiles = file.listFiles();
        for (int i=0; i < subFiles.length; i++) {
            System.out.println("Deleting: " + subFiles[i].getPath());   //debug
            if (!subFiles[i].delete()) {
                System.out.println("Couldnt delete, non-empty dir");   //debug
                delSubFiles(subFiles[i]);
                subFiles[i].delete();
            }
        }
        
    }
    public String getVitalFile() {
        return this.vitalName;
    }
    public String getHealthFile() {
        return this.healthName;
    }
    public String getInfoFile() {
        return this.infoName;
    }
    Patient(String patientID) {
        this.patientID = patientID;
        System.out.println(this.patientID);
        //filePath = filePath + this.patientID;
    }
    
    Patient() {
        this.patientID = "";
        //System.out.println(this.patientID);
        //filePath = filePath + this.patientID;
    }
    
    public String getName() {
        loadPatient(infoName);
        return jo.getString("firstName") + " " + jo.getString("lastName");
    }
    
    public String getFirstName() {
        loadPatient(infoName);
        return jo.getString("firstName");
    }
    
    public boolean savePatient(String firstName, String lastName,String email,String phone,String healthHist,String insID) {
        
        jo = fileCheck(infoName);
        //if (jo.isEmpty()) {
        //    System.out.println("bad file");
       //     return false;
       // }
        
        File file = new File(filePath + patientID + "/" + patientID + infoName);
        jo.put("patientID", patientID);
        jo.put("firstName", firstName);
        jo.put("lastName", lastName);
        jo.put("email", email);
        jo.put("phone", phone);
        jo.put("healthHist", healthHist);
        jo.put("insID", insID);
        //save to file
        System.out.println("Saving file"); 
        System.out.println(jo.toString()); 

        try {
            output = new BufferedWriter(new FileWriter(file));
            output.write(jo.toString());

            output.close();
            System.out.println("File Saved"); 
            //add to patientList
            System.out.println("Adding to patient list"); 
            addPatient(jo.get("patientID").toString());
            
            //----------
            //add to userList
            System.out.println("adding to user list"); 
            addUser();
            return true;
        } catch (IOException ex) {
            System.out.println("Error saving file"); 
        }
        
        return false;
    }
    private void addPatient(String patientID) {
        try {
            JSONObject ja = new JSONObject();
            ja = listCheck(filePath + patientListFilename);
            System.out.println(ja.toString());
            ja.accumulate("patientID", patientID);
            File file2 = new File(filePath + patientListFilename);
            output = new BufferedWriter(new FileWriter(file2));
            output.write(ja.toString());

            output.close();
            System.out.println("Added to patient list"); 
            
            
        } catch (IOException ex) {
            System.out.println("Error saving file"); 
        }
        
    }
    private void removePatient() {
        try {
            
            //JSONArray ja = new JSONArray();
            System.out.println("Removing from patient list"); 
            File file3 = new File(filePath + patientListFilename);
            Scanner readFile = new Scanner(file3);
            String fileDATA = "";
            while (readFile.hasNextLine()) {
                fileDATA = fileDATA.concat(readFile.nextLine());
            }
            System.out.println(fileDATA);
            JSONObject jo2 = new JSONObject(fileDATA);
            JSONArray ja = new JSONArray(jo2.getJSONArray("patientID"));
            System.out.println("test"); 
            System.out.println(this.patientID);
            
            for (int i=0; i < ja.length(); i++) {
                    System.out.println(ja.get(i).toString()); 
                    if (ja.get(i).toString().matches(patientID)) {
                        ja.remove(i);
                        jo2.put("patientID", ja);
                    }
                //jo2 = new JSONObject(ja.get(i));
           //     if (jo2.has("patientID") && jo2.getString("patientID").matches(patientID)) {
            //        System.out.println("Match found"); 
                    //ja.remove(i);
            //        break;
            //    }
            }
            output = new BufferedWriter(new FileWriter(file3));
            output.write(jo2.toString());

            output.close();
            System.out.println("Removed from user list"); 
            
        } catch (IOException ex) {
            System.out.println("Error saving file"); 
        }
        
    }
    private void addUser() {
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
            System.out.println(fileDATA);
            JSONArray ja = new JSONArray(fileDATA);
            System.out.println("test"); 
            System.out.println(this.patientID);
            ja.put(jo2);
            System.out.println("test2"); 
            output = new BufferedWriter(new FileWriter(file3));
            output.write(ja.toString());

            output.close();
            System.out.println("Added to user list"); 
            
            
        } catch (IOException ex) {
            System.out.println("Error saving file"); 
        }
        
    }
    private void removeUser() {
        try {
            JSONObject jo2 = new JSONObject();
            
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
            System.out.println(ja.toString());
            output = new BufferedWriter(new FileWriter(file3));
            output.write(ja.toString());

            output.close();
            System.out.println("Removed from user list"); 
            
            
        } catch (IOException ex) {
            System.out.println("Error saving file"); 
        }
        
    }
    public boolean savePatientVitals(String over12, String weight, String height,String bodyTemp,String bloodP) {
        
        jo = fileCheck(vitalName);
        //if (jo.isEmpty()) {
        //    System.out.println("bad file");
       //     return false;
       // }
        
        File file = new File(filePath + patientID + "/" + patientID + vitalName);
        jo.put("patientID", patientID);
        jo.put("over12", over12);
        jo.put("weight", weight);
        jo.put("height", height);
        jo.put("temp", bodyTemp);
        jo.put("bp", bloodP);
        //save to file
        System.out.println("Saving file"); 
        System.out.println(jo.toString()); 

        try {
            output = new BufferedWriter(new FileWriter(file));
            output.write(jo.toString());

            output.close();
            System.out.println("File Saved"); 
            
            return true;
        } catch (IOException ex) {
            System.out.println("Error saving file"); 
        }
        
        return false;
    }
    public boolean savePatientHealth(String allergies, String healthConcerns) {
        
        jo = fileCheck(healthName);
        //if (jo.isEmpty()) {
        //    System.out.println("bad file");
       //     return false;
       // }
        
        File file = new File(filePath + patientID + "/" + patientID + healthName);
        jo.put("patientID", patientID);
        jo.put("allergies", allergies);
        jo.put("healthConc", healthConcerns);
        //save to file
        System.out.println("Saving file"); 
        System.out.println(jo.toString()); 

        try {
            output = new BufferedWriter(new FileWriter(file));
            output.write(jo.toString());

            output.close();
            System.out.println("File Saved"); 
            
            return true;
        } catch (IOException ex) {
            System.out.println("Error saving file"); 
        }
        
        return false;
    }
    public List getPList() {
        JSONArray ja = new JSONArray();
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
            //System.out.println("converting to array");    //debug
            ja = jo.toJSONArray(new JSONArray("patientID"));
        }
        return ja.toList();
    }
    
    public JSONObject loadPatient(String fileName) {
        if (patientID.isEmpty()) {
            jo = new JSONObject();
            return jo;
            
        }
        jo = fileCheck(fileName);
        if (jo.isEmpty()) {
            System.out.println("bad file");
        }
        return jo;
    }
    private JSONObject listCheck(String pathToFile) {
        JSONObject jo = new JSONObject();
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
            while (readFile.hasNextLine()) {
                fileDATA = fileDATA.concat(readFile.nextLine());
            }
            jo = new JSONObject(fileDATA);
            //jo.accumulate("patientID", "");
            //System.out.println(jo.toString());    //debug
            return jo;
        } catch (FileNotFoundException e) {
            System.out.println("Error opening file");
        } catch (IOException ex) {
            System.out.println("Error opening file");
        }
    //    JSONObject jo = new JSONObject();
        return jo;
    }
    private JSONObject fileCheck(String fileName) {
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
            File file = new File(filePath + patientID + "/" + patientID + fileName); //+ "_PatientInfo.txt" or vitals
            System.out.println(filePath + patientID + "/" + patientID + fileName);
            bool = file.createNewFile();
            if (bool) {
                System.out.println("New File created"); 
            }
            
            if (file.length() < 1) {
                System.out.println("Empty File: populating"); 
                JSONObject jo = new JSONObject();
               // jo.put("patientID", patientID);   //test
                System.out.println(jo.toString()); 
                
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
            System.out.println("data: " + fileDATA);
            JSONObject jo = new JSONObject(fileDATA);
            System.out.println(jo.toString());
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
