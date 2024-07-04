package prototype;


import java.io.BufferedWriter;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner; // Import the Scanner class to read text files
import org.json.*;

interface StaffInterface {

}
public class Staff implements StaffInterface{
    private final String userDir = "./src/prototype/users.json";
    private final String staffDir = "./src/prototype/staff/";
    private final String staffListFilename = "staffList.txt";
    private String fileDATA = "";
    private BufferedWriter output = null;
    private JSONObject jo;
    private Authentication auth = new Authentication();

    Staff() {
        listCheck(staffDir + staffListFilename);
        try {
            File file = new File(userDir);
            Scanner readFile = new Scanner(file);
            fileDATA = "";
            while (readFile.hasNextLine()) {
                fileDATA = fileDATA.concat(readFile.nextLine());
            }
            JSONArray ja = new JSONArray(fileDATA);
            //----------

            String userInfo, userNameFile, userPassFile, userID;

            for (int i = 0; i < ja.length(); i++) {
                //System.out.println(ja.get(i));    //debug
                userInfo = ja.get(i).toString();
                jo = new JSONObject(userInfo);

                try {
                    userNameFile = jo.get("username").toString();
                    userID = jo.get("patientID").toString();
                    if (getAcctType(jo) == Authentication.accountType.DOCTOR || getAcctType(jo) == Authentication.accountType.NURSE) {

                        System.out.println("Match Found");
                        addToStaffList(userID, userNameFile, getAcctType(jo));
                    }
                } catch (Exception e) {
                    System.out.println("Bad File");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error opening file");
        }

    }
    private void addToStaffList(String patientID, String username, Authentication.accountType acct) {
        try {
            JSONObject jo2 = new JSONObject();
            jo2.put("patientID", patientID);
            jo2.put("username", username);
            jo2.put("type", acct.toString());

            //JSONArray ja = new JSONArray();
            System.out.println("Adding to staff list");
            File file3 = new File(staffDir + staffListFilename);
            Scanner readFile = new Scanner(file3);
            String fileDATA = "";
            while (readFile.hasNextLine()) {
                fileDATA = fileDATA.concat(readFile.nextLine());
            }
            //System.out.println(fileDATA);
            JSONArray ja = new JSONArray(fileDATA);
            for (int i = 0; i < ja.length(); i++) {
                if (ja.getJSONObject(i).getString("patientID").matches(patientID)) {
                    System.out.println("Already in staff list");
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
            System.out.println("Added to staff list");


        } catch (IOException ex) {
            System.out.println("Error saving file");
        }

    }

    public List getStaffList() {
        JSONArray ja;
        //ja = listCheck(filePath + patientListFilename);
        ja = listCheck(staffDir + staffListFilename);
        if (ja.isEmpty()) {
            //System.out.println("empty list"); //debug
            List list = java.util.Collections.emptyList();
            return list;
        }
        System.out.println(ja.toString());
        return ja.toList();
    }

    private JSONArray listCheck(String pathToFile) {
        JSONArray ja = new JSONArray();
        try {
            File rootPathCtrl = new File(staffDir);
            boolean bool = rootPathCtrl.mkdir();
            if(bool){
                System.out.println("Staff folder is created successfully");
            }
            //File file = new File(filePath + patientListFilename);
            File file = new File(pathToFile);
            bool = file.createNewFile();
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
                output.write("[]");

                output.close();
                return ja;
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
            ja = new JSONArray(fileDATA);
            //jo.accumulate("patientID", "");
            //System.out.println(pathToFile);
            //System.out.println(fileDATA);
            //System.out.println("inside  list  check " + jo.toString());    //debug
            return ja;
        } catch (FileNotFoundException e) {
            System.out.println("Error opening file");
        } catch (IOException ex) {
            System.out.println("Error opening file");
        }
        //    JSONObject jo = new JSONObject();
        return ja;
    }
    private Authentication.accountType getAcctType(JSONObject jo) {
        String type = jo.get("type").toString();
        switch (type) {
            case "PATIENT":
                // code block
                return Authentication.accountType.PATIENT;

            case "DOCTOR":
                // code block
                return Authentication.accountType.DOCTOR;

            case "NURSE":
                // code block
                return Authentication.accountType.NURSE;


            case "ADMIN":
                // code block
                return Authentication.accountType.ADMIN;

            default:
                // code block
                return Authentication.accountType.NONE;
        }
    }
}
