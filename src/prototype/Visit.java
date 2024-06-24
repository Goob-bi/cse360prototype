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

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import org.json.*;

public class Visit {
    
    private String fileDATA;
    
    public boolean saveVisit() {
        
        return false;
    }
    
    public boolean loadVisit() {

        try {
            File file = new File("./src/hw4/users.json");
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
                    //move auth logic to control-----------------------------------------
                /*    if (userNameFile.matches(username) && userPassFile.matches(password)) {
                        
                        System.out.println("Match Found");
                        //-------------------
                        //check what account type it is and set it
                        this.type = setAcctType(jo);
                        System.out.println("Account Type: " + this.type.toString());
                        return true;
                    }   */
                    //----------------------------------------------
                } catch (Exception e) {
                    System.out.println("Bad File");
                }
            }
            
        //    System.out.println("Account Type: " + this.type.toString());
            return false;
            
        } catch (FileNotFoundException e) {
            System.out.println("Error opening file");
            return false;
        }
        
    }
}
