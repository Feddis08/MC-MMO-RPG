package at.feddis08.tools.io.database.objects;

import org.json.JSONObject;

public class DataObject {
    public int time;
    public String node_name;
    public JSONObject data_json;
    public String data;
    public String data_type;
    public String id;
    public void update_json(){
        this.data_json = new JSONObject(this.data_json);
    }
}
