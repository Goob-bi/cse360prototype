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

public class Visit {
    
    
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
    
    Visit(String ID) {
        this.patientID = ID;
        this.patientDir = rootPath + patientID + "/";
    }
    
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
    public void setVisit(int num) {
        if (dirCheck(num)) {
            System.out.println("dir exists");
            this.visitNum = num;
        }
    }
    public List getVisitList() {
        File file = new File(rootPath + patientID + "/");
        int[] validDirs = new int[1];
        int temp = -1;
        int dir;
        List list = java.util.Collections.emptyList();
        if ( !patientID.isEmpty() && file.exists()) {
            String[] files = file.list(new FilenameFilter() {
                @Override
                public boolean accept(File current, String name) {
                  return name.matches("[0-9]+");
                  //return new File(current, name).isDirectory();
                }
            });
            list = Arrays.asList(files);
            return list;
            
        }
        return list;
    }
    public int getCurrentVisit() {
        File file = new File(rootPath + patientID + "/");
        int[] validDirs = new int[1];
        int temp = -1;
        int dir;
        if ( !patientID.isEmpty() && file.exists()) {
            String[] files = file.list(new FilenameFilter() {
                @Override
                public boolean accept(File current, String name) {
                  return new File(current, name).isDirectory();
                }
            });
            for (int i = 0; i < files.length; i++) {
            //    System.out.println(files[i]);
             //   System.out.println("[parsing name]");
                for (int z = 0; z < files[i].length(); z++) {
                    if (!Character.isDigit(files[i].charAt(z))) {
                        //System.out.println("not digit");
                        break;
                    }
                    if (z == files[i].length() - 1) {
                        dir = Integer.parseInt(files[i]);
                        System.out.println("found valid dir " + dir);
                        if (dir > temp) {
                            temp = dir;
                        }
                    }
                }
            }
            System.out.println("largest dir(curr visit) " + temp);
        }
        return temp;
        
    }
    public void getVisit(int num) {
        if (dirCheck(num)) {
            System.out.println("dir exists");
            
        }
        
    }
    
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
    public boolean saveVisitVitals(String over12, String weight, String height,String bodyTemp,String bloodP) {
        
        jo = fileCheck(vitalName);
        visitDir = patientDir +  visitNum + "/";
        //if (jo.isEmpty()) {
        //    System.out.println("bad file");
       //     return false;
       // }
        
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
    public boolean saveVisitHealth(String allergies, String healthConcerns) {
        
        jo = fileCheck(healthName);
        
        visitDir = patientDir +  visitNum + "/";
        //if (jo.isEmpty()) {
        //    System.out.println("bad file");
       //     return false;
       // }
        
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
    private boolean dirCheck(int num) {
        visitDir = patientDir +  num + "/";
        File file = new File(visitDir);
        return file.exists();
        
    }
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
