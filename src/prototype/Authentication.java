/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.*;
/**
 *
 * @author andreas lees
 */

public class Authentication {
    
    private String fileDATA ="";
    public accountType type;
    public enum accountType {
      PATIENT,
      DOCTOR,
      NURSE,
      ADMIN,
      NONE
    }
    private String userID = "0";
    Authentication(){
        this.type = accountType.NONE;
    }
    public accountType getType(){
        return this.type;
    }
    public String getID(){
        return this.userID;
    }
    public boolean auth(String username, String password) {
        
        try {
            File file = new File("./src/prototype/users.json");
            Scanner readFile = new Scanner(file);
            while (readFile.hasNextLine()) {
                fileDATA = fileDATA.concat(readFile.nextLine());
            }
            JSONArray ja = new JSONArray(fileDATA);
            //----------
            
            String userInfo, userNameFile, userPassFile;
            
            for (int i = 0; i < ja.length(); i++) {
                System.out.println(ja.get(i));
                userInfo = ja.get(i).toString();
                JSONObject jo = new JSONObject(userInfo);
                
                try {
                    userNameFile = jo.get("username").toString();
                    userPassFile = jo.get("pass").toString();
                    userID = jo.get("patientID").toString();
                    if (userNameFile.matches(username) && userPassFile.matches(password)) {
                        
                        System.out.println("Match Found");
                        //-------------------
                        //check what account type it is and set it
                        this.type = setAcctType(jo);
                        System.out.println("Account Type: " + this.type.toString());
                        return true;
                    }
                } catch (Exception e) {
                    System.out.println("Bad File");
                }
            }
            
            System.out.println("Account Type: " + this.type.toString());
            return false;
            
        } catch (FileNotFoundException e) {
            System.out.println("Error opening file");
            return false;
        }
        
    }
    
    private accountType setAcctType(JSONObject jo) {
        String type = jo.get("type").toString();
        switch (type) {
            case "PATIENT":
                // code block
                return accountType.PATIENT;

            case "DOCTOR":
                // code block
                return accountType.DOCTOR;

            case "NURSE":
                // code block
                return accountType.NURSE;


            case "ADMIN":
                // code block
                return accountType.ADMIN;

            default: 
                // code block
                return accountType.NONE;
    }
        
    }
    
    
    
    
}
