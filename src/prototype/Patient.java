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
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.*;

public class Patient {
    
    private String filePath = "./src/hw4/patients/";
    private String fileDATA = "";
    private final String patientID;
    private BufferedWriter output = null;
    private JSONObject jo;
    
    Patient(String patientID) {
        this.patientID = patientID;
        System.out.println(this.patientID);
        filePath = filePath + this.patientID;
    }
    
    public boolean savePatient(String firstName, String lastName,String email,String phone,String healthHist,String insID) {
        
        jo = fileCheck();
        if (jo.isEmpty()) {
            System.out.println("bad file");
            return false;
        }
        
        File file = new File(filePath + "/" + patientID + "_PatientInfo.txt");
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
            return true;
        } catch (IOException ex) {
            System.out.println("Error saving file"); 
        }
        
        return false;
    }
    
    public boolean loadPatient() {
        jo = fileCheck();
        if (jo.isEmpty()) {
            System.out.println("bad file");
            return false;
        }
            //---------------------------------------
            
            //JSONArray ja = new JSONArray(fileDATA);
            //----------
            
/*            String userInfo, userNameFile, userPassFile;
            
            for (int i = 0; i < ja.length(); i++) {
                System.out.println(ja.get(i));
                userInfo = ja.get(i).toString();
                JSONObject jo = new JSONObject(userInfo);
                
                try {
                    userNameFile = jo.get("username").toString();
                    userPassFile = jo.get("pass").toString();
                    //move auth logic to control-----------------------------------------
                    if (userNameFile.matches(username) && userPassFile.matches(password)) {
                        
                        System.out.println("Match Found");
                        //-------------------
                        //check what account type it is and set it
                        this.type = setAcctType(jo);
                        System.out.println("Account Type: " + this.type.toString());
                        return true;
                    }   
                    //----------------------------------------------
                } catch (Exception e) {
                    System.out.println("Bad File");
                }
            }
*/        
        //    System.out.println("Account Type: " + this.type.toString());
        return true;
    }
    
    private JSONObject fileCheck() {
        try {
            // check/create folder--------------------
            File filePathCtrl = new File(filePath);
            boolean bool = filePathCtrl.mkdir();  
            if(bool){  
               System.out.println("Folder is created successfully");  
            }else{  
               System.out.println("Error Found: Does the folder already exist?");  
            }  
            //---------------------------------------
            // check/create file---------------------
            File file = new File(filePath + "/" + patientID + "_PatientInfo.txt"); //+ "_PatientInfo.txt" 
            bool = file.createNewFile();
            if (bool) {
                System.out.println("New File created"); 
            }
            
            if (file.length() < 1) {
                System.out.println("Empty File: populating"); 
                JSONObject jo = new JSONObject();
                jo.put("patientID", patientID);
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
