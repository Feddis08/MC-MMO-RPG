package at.feddis08.mmorpg.remote_interface.web_service;

public class Web_user {

    public String id;
    public String session_id;

    Web_user(String id, String session_id){
        this.id = id;
        this.session_id = session_id;
    }

}
