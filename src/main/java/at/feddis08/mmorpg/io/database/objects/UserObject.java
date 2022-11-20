package at.feddis08.mmorpg.io.database.objects;

import at.feddis08.mmorpg.MMORPG;
import org.json.JSONObject;

public class UserObject {
    public String first_name = "";
    public String last_name = "";
    public String hash = "";
    public String time_created = "";
    public String id = "";
    public String data_json = "";
    public JSONObject jsonObject = new JSONObject();
    public UserObject(){
        create_json();
    }

    public void create_json(){
       jsonObject.put("send_message_on_login", "false");
        jsonObject.put("login_message", "Hi, I am online!");
       jsonObject.put("email_address", "");
       jsonObject.put("last_online", "");
       jsonObject.put("download_path", "");
       data_json = jsonObject.toString();
    }
    public void update_json(){
        if (data_json == null){
            create_json();
        }
        this.jsonObject = new JSONObject(this.data_json);
    }

}
