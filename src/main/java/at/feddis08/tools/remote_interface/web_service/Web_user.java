package at.feddis08.tools.remote_interface.web_service;

public class Web_user {

    public String id;
    public String session_id;
    public double last_resp;
    public double timeout = 10000;

    Web_user(String id, String session_id, double last_resp){
        this.id = id;
        this.session_id = session_id;
        this.last_resp = last_resp;

    }

}
