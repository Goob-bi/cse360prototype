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
        return name.matches("\\p{L}*");
    }
    public boolean EmailCheck(String email) {
        return email.matches("\\p{L}*");
    }
    
    public boolean DateCheck(String date) {
        //test with format dd/mm/yyyy
        return date.matches("[0-9]{2}/[0-9]{2}/[0-9]{4}");

    }
    public String ConvertDate(String date) {
        String convertedDate = "00112222";
        if (DateCheck(date)) {
            convertedDate = date.substring(0,2) + date.substring(3,5) + date.substring(6, 9);
        }
        return convertedDate;

    }
    
    public boolean IntCheck(String str) {
        return str.matches("[0-9]+");
    }
}
