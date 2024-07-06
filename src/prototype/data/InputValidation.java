/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype.data;

/**
 *
 * @author andreas lees
 */
public class InputValidation {
    public boolean NameCheck(String name) {
        if (name.isEmpty()) {
            return false;
        }
        for (int i = 0; i < name.length(); i++) {
            if (!Character.isLetter(name.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    public boolean DateCheck(String date) {
        /*
        //format ddmmyyyy
        if (date.length() != 8) {
            return false;
        }
        for (int i = 0; i < date.length(); i++) {
            if (!Character.isDigit(date.charAt(i))) {
                return false;
            }
        }
        return true;

         */
        //test with format dd/mm/yyyy
        if (date.length() != 10) {
            System.out.println("wrong length");
            return false;
        }
        for (int i = 0; i < date.length(); i++) {
            if ((i == 2 || i ==5) && !(date.charAt(i) == '/')){
                System.out.println("/ in wrong spot");
                return false;
            } else if (i != 2 && i !=5 && !Character.isDigit(date.charAt(i))) {

                System.out.println("not number: " + date.charAt(i));
                return false;
            }
        }
        return true;

    }
    public String ConvertDate(String date) {
        String convertedDate = date.substring(0,2) + date.substring(3,5) + date.substring(6, 9);
        return convertedDate;

    }
    
    public boolean IntCheck(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
