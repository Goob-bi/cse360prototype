package prototype.data;
/*
    Thanks to https://www.baeldung.com/javafx-listview-display-custom-items
    Allows to display only specific attributes of object in ListView
    while allowing us to access the underlying object
 */
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import org.json.JSONObject;

import javafx.util.Callback;

public class STAFFCellFactory implements Callback<ListView<JSONObject>, ListCell<JSONObject>>{
    @Override
    public ListCell<JSONObject> call(ListView<JSONObject> param) {
        return new ListCell<>(){
            @Override
            public void updateItem(JSONObject jo, boolean empty) {
                super.updateItem(jo, empty);
                if (empty || jo == null) {
                    setText(null);
                } else if (jo.getString("type").equals("DOCTOR")){
                    setText("Dr. " + jo.getString("username"));
                } else if (jo.getString("type").equals("NURSE")){
                    setText("Nurse " + jo.getString("username"));
                } else {
                    if (jo.getString("patientID").length() < 10) {
                        setText("Name: " + jo.getString("username") + "\t\tID: " + jo.getString("patientID"));
                    }else{
                        setText("Name: " + jo.getString("username") + "\t\tID: " + jo.getString("patientID").substring(0, 9) + "..." );
                    }
                    //need to update how patient list is handled before i make it pretty
                    //setText(jo.getString("patientID") + " " + jo.getString("username"));
                }
            }
        };
    }
}
