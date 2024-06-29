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
        if (date.length() != 8) {
            return false;
        }
        for (int i = 0; i < date.length(); i++) {
            if (!Character.isDigit(date.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
