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

public class MSGCellFactory implements Callback<ListView<JSONObject>, ListCell<JSONObject>> {
    @Override
    public ListCell<JSONObject> call(ListView<JSONObject> param) {
        return new ListCell<>(){
            @Override
            public void updateItem(JSONObject jo, boolean empty) {
                super.updateItem(jo, empty);
                if (empty || jo == null) {
                    setText(null);
                } else {

                    setText(
                            "[" + jo.getString("from") + "->" + jo.getString("to") + "]"
                            + "\n " + jo.getString("msgTitle")
                            + "\n\t" + jo.getString("msgBody")

                    );
                }
            }
        };
    }
}
