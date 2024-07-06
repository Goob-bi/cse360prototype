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
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner; // Import the Scanner class to read text files

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.*;
/*
Message structure:
All messages stored in patient folder
   .../patients/{patientID}/messages/

messages only happen between patients and doc/nurse
messages stored by doc/nurse ID
    .../messages/{ID}.txt

messages stored in JSONArray
    [{message1Info},{message2Info},...]

messages stored as JSONObject containing:
        ("from", senderID)  who sent it
        ("to", receiverID)  who it was meant for
        ("msgTitle", msgTitle)  title/subject of message
        ("msgBody", msgBody)    message contents
        ("msgID", messageID)    unique ID to identify message
   {from:senderID, to:receiverID, msgTitle:"Title", msgBody:"This is the message", msgID:1}
 */
interface MessageInterface {
    //adds a new visit and increments receiverID
    void incrementVisit();
    //sets receiverID to num if directory num exists
    void setVisit(String ID);
    //returns latest visit
    String getRecentMessage();
    //returns the suffix for the health info file
    String getHealthFile();
    //loads data from visit number {receiverID} into a JSONObject
    JSONObject loadVisit(String fileName);
    //returns a list of visits
    List getVisitList();
    boolean saveVisitVitals(String over12, String weight, String height,String bodyTemp,String bloodP);
    boolean saveVisitHealth(String allergies, String healthConcerns);
}

public class Message {


    private String rootPath = "./src/prototype/patients/";
    private String patientMsgDir = "";
    private String messageFile = "";
    private String fileDATA = "";
    private BufferedWriter output = null;
    private int visitNum = 0;

    private int messageID = 0;
    //private String messageID = "";
    private String patientID = "";
    private String staffID = "";
    private String receiverName = "";
    private String senderName = "";
    private JSONObject staffO, patientO;
    private String msgName = "_Message.txt";
    private JSONObject jo;
    private JSONArray ja;

    private String WORKINGPATH = "";
    public void setWorkingPath(String path) {
        this.WORKINGPATH = path;
        this.rootPath = WORKINGPATH + "/patients/";
        this.patientMsgDir = rootPath + patientID + "/messages/";
        if (patientID.isEmpty()) {
            this.patientMsgDir = rootPath + "badID" + "/messages/";
        }
    }

    //-------------------constructors-------------------------------------------------
    Message(JSONObject patient, String path) {
        setWorkingPath(path);
        this.staffO = new JSONObject();
        this.patientO = patient;
        this.staffID = "";
        this.patientID = patientO.getString("patientID");
        this.patientMsgDir = rootPath + patientID + "/messages/";
        if (patientID.isEmpty()) {
            this.patientMsgDir = rootPath + "badID" + "/messages/";
        }

    }
    Message(JSONObject staff, JSONObject patient, String path) {
    //Message(String staffID, String patientID) {
        setWorkingPath(path);
        this.staffO = staff;
        this.patientO = patient;
        this.staffID = staffO.getString("patientID");
        this.patientID = patientO.getString("patientID");
        this.patientMsgDir = rootPath + patientID + "/messages/";
        if (patientID.isEmpty()) {
            this.patientMsgDir = rootPath + "badID" + "/messages/";
        }
    }
//------------------public methods--------------------------------------------------
    public void setFromPatient() {
        receiverName = staffO.getString("username");
        senderName = patientO.getString("patientName");
    }
    public void setFromStaff() {
        receiverName = patientO.getString("patientName");
        senderName = staffO.getString("username");

    }
    //might need to specify as ObservableList, see example Staff.getStaffList()
    public ObservableList<JSONObject> getMessageList() {
        File file = new File(patientMsgDir);
        List<String> fileList = java.util.Collections.emptyList();
        ObservableList<JSONObject> list = FXCollections.observableArrayList ();
        String ID = "";
        if ( !patientID.isEmpty() && file.exists()) {
            //grab a list of all files in dir
            String[] files = file.list();
            fileList = Arrays.asList(files);

            //get a list of staff (JSONObjects)
            Staff staff = new Staff(WORKINGPATH);
            staff.setWorkingPath(WORKINGPATH);
            List<JSONObject> staffList = staff.getStaffList();
            System.out.println("filelist: "+fileList.toString());
            System.out.println(staffList.size());
            for (int i=0; i < staffList.size(); i++) {
                System.out.println(i);
                //if fileList contains a matching staffID, add it to list
                ID = staffList.get(i).getString("patientID");
                if (fileList.contains(ID + msgName) && staffID.matches(ID)) {
                    File msgFile = new File(patientMsgDir + ID + msgName);
                    System.out.println("found matching message file");
                    Scanner readFile = null;
                    try {
                        readFile = new Scanner(msgFile);
                    } catch (FileNotFoundException e) {
                        System.out.println("file not found");
                        return list;
                    }
                    //---------------------------------------
                    //read file-----------------------------
                    fileDATA = "";
                    while (readFile.hasNextLine()) {
                        fileDATA = fileDATA.concat(readFile.nextLine());
                    }
                    readFile.close();
                    ja = new JSONArray(fileDATA);
                    for (int z=0; z <ja.length(); z++) {
                        list.add(ja.getJSONObject(z));
                    }
                }

            }

        }

        return list;
    }

    //returns the name of last modified file(most recent message)
    public JSONObject getRecentMessage() {
        JSONObject message = new JSONObject();
        File file = new File(patientMsgDir);
        File temp, current;
        int dir;
        //if there is a patientID and the directory exists
        if ( !patientID.isEmpty() && file.exists()) {
            //collect a list of directories in the root path
            String[] files = file.list();
            //parse through the directory list
            if (files.length < 1) {
                System.out.println("\nfile to short\n");    //debug
                return message;
            }
            temp = new File(patientMsgDir + files[0]);
            for (int i = 0; i < files.length; i++) {
                current = new File(patientMsgDir + files[i]);
                //if the current file was modified a shorter
                //time ago than temp
                System.out.println(
                        files[i] + "\n" +
                        "current " + current.getName() + " modif: " + current.lastModified()
                        + "\ntemp " + temp.getName() + " modif: " + temp.lastModified()
                );
                if (current.lastModified() > temp.lastModified()) {
                    temp = new File(patientMsgDir + files[i]);
                }
            }
            System.out.println("last modified message " + temp.getName());
            Scanner readFile = null;
            try {
                readFile = new Scanner(temp);
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                return message;
            }
            //---------------------------------------
            //read file-----------------------------
            fileDATA = "";
            while (readFile.hasNextLine()) {
                fileDATA = fileDATA.concat(readFile.nextLine());
            }
            //CLOSE FILES WHEN DONE WITH THEM DUMMY
            readFile.close();
            System.out.println("data: " + fileDATA);
            ja = new JSONArray(fileDATA);
            int lastMsg = ja.length() - 1;
            message = ja.getJSONObject(lastMsg);

        }
        //if patient ID is empty or no files exist, return 0, there are no visits

        System.out.println("\nempty id or file not exist\n");    //debug
        return message;

    }
    //returns the name of last modified file(most recent message)
    public JSONObject getRecentStaffMessage(String staffID) {
        JSONObject message = new JSONObject();
        File file = new File(WORKINGPATH + "/staff/messages/");
        File temp = null;
        int dir;
        if (!file.exists()) {
            file.mkdir();
        }
        //if there is a patientID and the directory exists
        if ( !patientID.isEmpty()) {
            //collect a list of directories in the root path
            String[] files = file.list();
            //parse through the directory list
            if (files == null || files.length < 1) {
                System.out.println("\nfile to short\n");    //debug
                return message;
            }
            for (int i = 0; i < files.length; i++) {
                if (files[i].matches(staffID + msgName)) {
                    temp = new File(WORKINGPATH + "/staff/messages/" + files[i]);
                    break;
                }
            }
            if (temp == null) {
                //no match found
                return message;
            }
            //System.out.println("last modified message " + temp.getName());
            Scanner readFile = null;
            try {
                readFile = new Scanner(temp);
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                return message;
            }
            //---------------------------------------
            //read file-----------------------------
            fileDATA = "";
            while (readFile.hasNextLine()) {
                fileDATA = fileDATA.concat(readFile.nextLine());
            }
            //CLOSE FILES WHEN DONE WITH THEM DUMMY
            readFile.close();
            ja = new JSONArray(fileDATA);
            int lastMsg = ja.length() - 1;
            message = ja.getJSONObject(lastMsg);

        }
        //if patient ID is empty or no files exist, return 0, there are no visits

        return message;

    }

    public JSONObject loadMessageID(String ID) {
        jo = new JSONObject();
        if (patientID.isEmpty() || staffID.isEmpty()) {
            return jo;

        }
        ja = fileCheck();
        if (ja.isEmpty()) {
            System.out.println("bad file");
            return jo;
        }
        String compareStr = "";
        for (int i=0; i < ja.length(); i++) {
            compareStr = ja.getJSONObject(i).getString("msgID");
            if (compareStr.matches(ID)) {
                return ja.getJSONObject(i);
            }
        }
        return jo;

    }


    public boolean saveMessage(String msgTitle, String msgBody) {
        jo = new JSONObject();
        ja = fileCheck();
        messageFile = patientMsgDir + staffID + msgName;

        //messageID++;

        File file = new File(messageFile);
        jo.put("from", senderName);
        jo.put("to", receiverName);
        jo.put("msgTitle", msgTitle);
        jo.put("msgBody", msgBody);
        jo.put("msgID", messageID);

        for (int i = 0; i < ja.length(); i++) {
            if (ja.getJSONObject(i).getInt("msgID") == messageID) {
                messageID++;
            }
        }


        ja.put(jo);
        //save to file
        System.out.println("Saving file");

        try {
            output = new BufferedWriter(new FileWriter(file));
            output.write(ja.toString());

            output.close();
            System.out.println("File Saved");

            //save to staff message dir
            file = new File(WORKINGPATH + "/staff/");
            file.mkdir();
            file = new File(WORKINGPATH + "/staff/messages/");
            file.mkdir();
            file = new File(WORKINGPATH + "/staff/messages/" + staffID + msgName);
            if (!file.exists()) {
                file.createNewFile();
            }
            output = new BufferedWriter(new FileWriter(file));
            output.write(ja.toString());

            output.close();
            System.out.println("File Saved to staff messages");


            return true;
        } catch (IOException ex) {
            System.out.println("Error saving file");
        }
        return false;
    }
    //-------------------------private methods-------------------------------------------
    
    private JSONArray fileCheck() {

        messageFile = patientMsgDir + staffID + msgName;
        try {
            // check/create folder--------------------
            File rootPathCtrl = new File(patientMsgDir);
            boolean bool = rootPathCtrl.mkdir();
            if(bool){
                System.out.println("Patient message folder is created successfully");
            }
            // check/create file---------------------
            File file = new File(messageFile);
            bool = file.createNewFile();
            if (bool) {
                System.out.println("New File created");
            }
            //try storing messages as a message array
            if (file.length() < 1) {
                System.out.println("Empty File: populating");
                ja = new JSONArray();

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
            //CLOSE FILES WHEN DONE WITH THEM DUMMY
            readFile.close();
            ja = new JSONArray(fileDATA);
            return ja;

        } catch (FileNotFoundException e) {
            System.out.println("Error opening message file");
        } catch (IOException ex) {
            System.out.println("Error opening message file");
        }
        ja = new JSONArray();
        return ja;
    }
}