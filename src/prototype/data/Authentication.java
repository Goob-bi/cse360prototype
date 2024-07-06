/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype.data;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import org.json.*;
/**
 *
 * @author andreas lees
 */

public class Authentication {
    
    private String fileDATA ="";
    private accountType type;
    private String userID = "0";
    private String userName = "";
    private String userPass = "";
    private String userFile = "./src/prototype/users.json";
    private JSONArray ja;
    private String WORKINGPATH = "";
    
    public enum accountType {
      PATIENT,
      DOCTOR,
      NURSE,
      ADMIN,
      NONE
    }

//--------------------------------------------------------------------  
public Authentication(String path){
        WORKINGPATH = path;
        this.userFile = WORKINGPATH + "/users.json";
        this.type = accountType.NONE;
    }
//--------------------------------------------------------------------  
    public accountType getType(){
        return this.type;
    }
    public String getID(){
        return this.userID;
    }
    public String getName(){
        return this.userName;
    }
    public boolean auth(String username, String password) {
        try {
            File file = new File(userFile);
            Scanner readFile = new Scanner(file);
            while (readFile.hasNextLine()) {
                fileDATA = fileDATA.concat(readFile.nextLine());
            }
            ja = new JSONArray(fileDATA);
            String userInfo, userNameFile, userPassFile;
            
            for (int i = 0; i < ja.length(); i++) {
                userInfo = ja.get(i).toString();
                JSONObject jo = new JSONObject(userInfo);
                
                try {
                    userName = jo.get("username").toString();
                    userPass = jo.get("pass").toString();
                    userID = jo.get("patientID").toString();
                    if (userName.equals(username) && userPass.equals(password)) {
                        System.out.println("Match Found");
                        //check what account type it is and set it
                        this.type = getAcctType(jo);
                        System.out.println("Account Type: " + this.type.toString());
                        return true;
                    }
                } catch (Exception e) {
                    System.out.println("Bad File");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error opening user file");
            System.out.println(userFile);   //debug
        }
        return false;
    }

    private accountType getAcctType(JSONObject jo) {
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
