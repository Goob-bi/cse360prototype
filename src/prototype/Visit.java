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
import org.json.*;

interface VisitInterface {
    //adds a new visit and increments visitNum
    void incrementVisit();
    //sets visitNum to num if directory num exists
    void setVisit(int num);
    //returns latest visit
    int getCurrentVisit();
    //returns the suffix for the health info file
    String getHealthFile();
    //loads data from visit number {visitNum} into a JSONObject
    JSONObject loadVisit(String fileName);
    //returns a list of visits
    List getVisitList();
    boolean saveVisitVitals(String over12, String weight, String height,String bodyTemp,String bloodP);
    boolean saveVisitHealth(String allergies, String healthConcerns);
}

public class Visit implements VisitInterface{
    
    
    private String rootPath = "./src/prototype/patients/";
    private String patientDir = "";
    private String visitDir = "";
    private String vitalName = "_PatientVitals.txt";
    private String healthName = "_PatientHealth.txt";
    private String infoName = "_PatientInfo.txt";
    private String patientListFilename = "patientList.txt";
    private String fileDATA = "";
    private String patientID = "";
    private BufferedWriter output = null;
    private int visitNum = 0;
    private JSONObject jo;

//-------------------constructors-------------------------------------------------
    Visit(String ID) {
        this.patientID = ID;
        this.patientDir = rootPath + patientID + "/";
    }
//------------------public methods--------------------------------------------------

    @Override
    public void incrementVisit() {
        this.visitNum = getCurrentVisit();
        visitNum++;
        if (dirCheck(visitNum)) {
            System.out.println("dir exists");
            
        } else {
            File rootPathCtrl = new File(rootPath + patientID + "/" + visitNum + "/");
            boolean bool = rootPathCtrl.mkdir();  
            if(bool){  
               System.out.println("New Visit folder is created successfully");  
            }else{  
               System.out.println("Error Found: Does the folder already exist?");  
            }  
            
        }
    }
    @Override
    public void setVisit(int num) {
        if (dirCheck(num)) {
            System.out.println("dir exists");
            this.visitNum = num;
        }
    }
    @Override
    public List getVisitList() {
        File file = new File(rootPath + patientID + "/");
        List list = java.util.Collections.emptyList();
        if ( !patientID.isEmpty() && file.exists()) {
            String[] files = file.list(new FilenameFilter() {
                //check that the directory name is only numeric, might replace
                // getCurrentVisit() code with this
                @Override
                public boolean accept(File current, String name) {
                  return name.matches("[0-9]+");
                }
            });
            list = Arrays.asList(files);
            return list;
            
        }
        return list;
    }
    @Override
    public int getCurrentVisit() {
        File file = new File(rootPath + patientID + "/");
        int temp = 0;
        int dir;
        //if there is a patientID and the directory exists
        if ( !patientID.isEmpty() && file.exists()) {
            //collect a list of directories in the root path
            String[] files = file.list(new FilenameFilter() {
                @Override
                public boolean accept(File current, String name) {
                  return new File(current, name).isDirectory();
                }
            });
            //parse through the directory list
            for (int i = 0; i < files.length; i++) {
                //check each dir name is only numeric
                for (int z = 0; z < files[i].length(); z++) {
                    if (!Character.isDigit(files[i].charAt(z))) {
                        //if something other than a digit is found, try the next
                        //dir in the list
                        break;
                    }
                    //if directory name is all numbers, convert from string to an
                    //integer and set variable dir to that int
                    if (z == files[i].length() - 1) {
                        dir = Integer.parseInt(files[i]);
                        
                        //if dir > temp, set temp to dir, this way we can find the
                        //largest number (current visit)
                        if (dir > temp) {
                            temp = dir;
                        }
                    }
                }
            }
            System.out.println("largest dir(curr visit) " + temp);
        }
        //if patient ID is empty or no files exist, return 0, there are no visits
        return temp;
        
    }
    @Override
    public String getHealthFile() {
        return this.healthName;
    }

    @Override
    public JSONObject loadVisit(String fileName) {
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
    @Override
    public boolean saveVisitVitals(String over12, String weight, String height,String bodyTemp,String bloodP) {
        
        jo = fileCheck(vitalName);
        visitDir = patientDir +  visitNum + "/";
        
        
        File file = new File(visitDir + patientID + vitalName);
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
    @Override
    public boolean saveVisitHealth(String allergies, String healthConcerns) {
        
        jo = fileCheck(healthName);
        visitDir = patientDir +  visitNum + "/";
        
        File file = new File(visitDir + patientID + healthName);
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
//-------------------------private methods-------------------------------------------
    private boolean dirCheck(int num) {
        visitDir = patientDir +  num + "/";
        File file = new File(visitDir);
        return file.exists();
        
    }
    /**
     * Checks that a specific visit file exists for visitNum. If not, creates the necessary
     * directories and file, returning an empty JSONObject. If the file is found,
     * returns a JSONObject containing the file contents.
     * @param fileName the ending of tthe filename
     * @return a JSONObject containing the file contents
     */
    private JSONObject fileCheck(String fileName) {
        
        visitDir = patientDir +  visitNum + "/";
        try {
            // check/create folder--------------------
            File rootPathCtrl = new File(patientDir);
            JSONObject jo = new JSONObject();
            if (patientID.isEmpty()) {
                return jo;
            }
            boolean bool = rootPathCtrl.mkdir();  
            if(bool){  
               System.out.println("Patient folder is created successfully");  
            }else{  
               System.out.println("Error creating patient folder: Does it already exist?");  
            }  
            rootPathCtrl = new File(visitDir);
            bool = rootPathCtrl.mkdir();  
            if(bool){  
               System.out.println("Patient folder is created successfully");  
            }else{  
               System.out.println("Error creating visit folder: Does it already exist?");  
            }  
            //---------------------------------------
            // check/create file---------------------
            File file = new File(visitDir + patientID + fileName); //+ "_PatientInfo.txt" or vitals
            System.out.println(visitDir + patientID + fileName);
            bool = file.createNewFile();
            if (bool) {
                System.out.println("New File created"); 
            }
            
            if (file.length() < 1) {
                System.out.println("Empty File: populating"); 
                jo = new JSONObject();
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
            jo = new JSONObject(fileDATA);
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
