package prototype;
/*
    Thanks to https://www.baeldung.com/javafx-listview-display-custom-items
    Allows to display only specific attributes of object in ListView
    while allowing us to access the underlying object
 */
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import org.json.JSONObject;

import javafx.util.Callback;

public class STAFFCellFactory implements Callback<ListView<JSONObject>, ListCell<JSONObject>> {
    @Override
    public ListCell<JSONObject> call(ListView<JSONObject> param) {
        return new ListCell<>(){
            @Override
            public void updateItem(JSONObject jo, boolean empty) {
                super.updateItem(jo, empty);
                if (empty || jo == null) {
                    setText(null);
                } else if (jo.getString("type").matches("DOCTOR")){
                    setText("Dr. " + jo.getString("username"));
                } else if (jo.getString("type").matches("NURSE")){
                    setText("Nurse " + jo.getString("username"));
                } else {
                    setText(jo.getString("patientID") + " " + jo.getString("username"));
                }
            }
        };
    }
}
