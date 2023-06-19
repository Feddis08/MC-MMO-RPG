package at.feddis08.tools.io.database.objects;

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
       jsonObject.put("logged_in", "false");
       jsonObject.put("auth_token", "");
       jsonObject.put("auth_token_date_created", "");
       jsonObject.put("ip-address", "");
       jsonObject.put("last_login", "");
       jsonObject.put("did_setup", false);
       data_json = jsonObject.toString();
    }
    public void update_json(){
        if (data_json == null){
            create_json();
        }
        this.jsonObject = new JSONObject(this.data_json);
    }

}
