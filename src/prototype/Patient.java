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
    
    public String getVitalName() {
        return this.vitalName;
    }
    public String getHealthName() {
        return this.healthName;
    }
    public String getInfoName() {
        return this.infoName;
    }
    Patient(String patientID) {
        this.patientID = patientID;
        System.out.println(this.patientID);
        //filePath = filePath + this.patientID;
    }
    
    Patient() {
        this.patientID = "BadID";
        System.out.println(this.patientID);
        //filePath = filePath + this.patientID;
    }
    
    public String getName() {
        loadPatient(infoName);
        return jo.getString("firstName") + " " + jo.getString("lastName");
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
            jo = listCheck();
            jo.accumulate("patientID", patientID);
            File file2 = new File(filePath + patientListFilename);
            output = new BufferedWriter(new FileWriter(file2));
            output.write(jo.toString());

            output.close();
            System.out.println("Added to patient list"); 
            
            return true;
        } catch (IOException ex) {
            System.out.println("Error saving file"); 
        }
        
        return false;
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
        jo = listCheck();
        if (jo.isEmpty()) {
            System.out.println("empty list");
        }
        JSONArray ja = jo.getJSONArray("patientID");
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
    private JSONObject listCheck() {
        try {
            File file = new File(filePath + patientListFilename);  
            boolean bool = file.createNewFile();
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
            while (readFile.hasNextLine()) {
                fileDATA = fileDATA.concat(readFile.nextLine());
            }
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
            while (readFile.hasNextLine()) {
                fileDATA = fileDATA.concat(readFile.nextLine());
            }
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
