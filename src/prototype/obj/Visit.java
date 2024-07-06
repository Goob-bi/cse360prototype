/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype.obj;

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
    private String medsName = "_PatientMed.txt";
    private String physicalName = "_PatientPhysical.txt";
    private String infoName = "_PatientInfo.txt";
    private String patientListFilename = "patientList.txt";
    private String fileDATA = "";
    private String patientID = "";
    private BufferedWriter output = null;
    private int visitNum = 1;
    private JSONObject jo;

    private String WORKINGPATH = "";
    public void setWorkingPath(String path) {
        this.WORKINGPATH = path;
        this.rootPath = WORKINGPATH + "/patients/";
        this.patientDir = rootPath + patientID + "/";
    }
//-------------------constructors-------------------------------------------------
public Visit(String ID, String path) {
        this.patientID = ID;
        this.patientDir = rootPath + patientID + "/";
        setWorkingPath(path);
        if (getCurrentVisit() < 1) {
            incrementVisit();
        }

    }
//------------------public methods--------------------------------------------------
    public boolean checkMissingData() {
        jo = loadVisit(getHealthFile());
        try {
            jo.getString("allergies");
            jo.getString("healthConc");

        } catch (JSONException e) {
            System.out.println("[Loading Health File] No data");  //debug
            return false;
        }
        jo = loadVisit(getPhysicalFile());
        try {
            jo.getString("phyRes");
            jo.getString("phyConcerns");

        } catch (JSONException e) {
            System.out.println("[Loading Physical File] No data");  //debug
            return false;
        }
        jo = loadVisit(getMedFile());
        try {
            jo.getString("immunization");
            jo.getString("perscription");

        } catch (JSONException e) {
            System.out.println("[Loading Medication File] No data");  //debug
            return false;
        }
        jo = loadVisit(getVitalFile());
        try {
            jo.getString("over12");
            jo.getString("weight");
            jo.getString("height");
            jo.getString("temp");
            jo.getString("bp");
        } catch (JSONException e) {
            System.out.println("[Loading Vitals File] No data");  //debug
            return false;
        }
        return true;

    }
    @Override
    public void incrementVisit() {
        this.visitNum = getCurrentVisit();
        visitNum++;
        if (!dirCheck(visitNum)) {
            File rootPathCtrl = new File(rootPath + patientID + "/" + visitNum + "/");
            boolean bool = rootPathCtrl.mkdir();  
            if(bool){  
               System.out.println("New Visit folder is created successfully");  
            }
            
        }
    }
    @Override
    public void setVisit(int num) {
        if (dirCheck(num)) {
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
    public String getVitalFile() {
        return this.vitalName;
    }
    public String getPhysicalFile() {
        return this.physicalName;
    }
    public String getMedFile() {
        return this.medsName;
    }

    @Override
    public JSONObject loadVisit(String fileName) {
        if (patientID.isEmpty()) {
            jo = new JSONObject();
            return jo;
            
        }
        jo = fileCheck(fileName);
        if (jo.isEmpty()) {
            //System.out.println("bad file");
        }
        return jo;
        
    }
    public JSONObject loadVisitHistory() {
        JSONObject jo = new JSONObject();

        if (patientID.isEmpty()) {
            return jo;
        }

        //-----------------------------------

        File file = new File(rootPath + patientID + "/");
        if ( !patientID.isEmpty() && file.exists()) {
            String[] files = file.list(new FilenameFilter() {
                @Override
                public boolean accept(File current, String name) {
                    return name.matches("[0-9]+");
                }
            });
            for (int i = 0; i < files.length; i++) {
                setVisit(Integer.parseInt(files[i]));
                try {
                    jo.append("allergies", "[" + visitNum + "] " + loadVisit(healthName).getString("allergies"));
                    jo.append("healthConc", "[" + visitNum + "] " + loadVisit(healthName).getString("healthConc"));
                } catch (JSONException e) {
                    System.out.println("No allergy/health concern data for visit " + visitNum);
                }
                try {
                    jo.append("phyRes", "[" + visitNum + "] " + loadVisit(physicalName).getString("phyRes"));
                    jo.append("phyConcerns", "[" + visitNum + "] " + loadVisit(physicalName).getString("phyConcerns"));
                } catch (JSONException e) {
                    System.out.println("No physical concern data for visit " + visitNum);
                }
                try {
                    jo.append("immunization", "[" + visitNum + "] " + loadVisit(medsName).getString("immunization"));
                    jo.append("perscription", "[" + visitNum + "] " + loadVisit(medsName).getString("perscription"));
                } catch (JSONException e) {
                    System.out.println("No immunization/prescription data for visit " + visitNum);
                }
            }
            return jo;

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
    public boolean saveVisitMeds(String immunization, String perscription) {

        jo = fileCheck(medsName);
        visitDir = patientDir +  visitNum + "/";

        File file = new File(visitDir + patientID + medsName);
        jo.put("patientID", patientID);
        jo.put("immunization", immunization);
        jo.put("perscription", perscription);
        //save to file
        System.out.println("Saving file");

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
    public boolean saveVisitPhys(String phyResult, String PhysConcerns) {

        jo = fileCheck(physicalName);
        visitDir = patientDir +  visitNum + "/";

        File file = new File(visitDir + patientID + physicalName);
        jo.put("patientID", patientID);
        jo.put("phyRes", phyResult);
        jo.put("phyConcerns", PhysConcerns);
        //save to file
        System.out.println("Saving file");

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
            }
            rootPathCtrl = new File(visitDir);
            bool = rootPathCtrl.mkdir();  
            if(bool){  
               System.out.println("Patient folder is created successfully");  
            }
            // check/create file---------------------
            File file = new File(visitDir + patientID + fileName);
            bool = file.createNewFile();
            if (bool) {
                System.out.println("New File created"); 
            }
            
            if (file.length() < 1) {
                System.out.println("Empty File: populating"); 
                jo = new JSONObject();
                
                output = new BufferedWriter(new FileWriter(file));
                output.write(jo.toString());
                
                output.close();
                return jo;
            }
            
            Scanner readFile = new Scanner(file);
            //---------------------------------------
            //read file-----------------------------
            fileDATA = "";
            while (readFile.hasNextLine()) {
                fileDATA = fileDATA.concat(readFile.nextLine());
            }
            //CLOSE FILES WHEN DONE WITH THEM DUMMY
            readFile.close();
            jo = new JSONObject(fileDATA);
            return jo;
            
        } catch (FileNotFoundException e) {
            System.out.println("Error opening visit file");
        } catch (IOException ex) {
            System.out.println("Error opening visit file");
        }
        JSONObject jo = new JSONObject();
        return jo;
    }
}
