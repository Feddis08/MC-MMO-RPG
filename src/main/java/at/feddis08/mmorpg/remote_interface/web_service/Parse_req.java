package at.feddis08.mmorpg.remote_interface.web_service;

import at.feddis08.mmorpg.MMORPG;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Objects;

public class Parse_req {

    public static String do_req(HttpServletRequest req) throws IOException {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = req.getReader();
        String response = "";
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String data = buffer.toString();
        if (!data.isBlank() || !data.isEmpty()) {
            JSONObject json_data = new JSONObject(data);
            if (Objects.equals(json_data.get("req").toString(), "get server data")) {
                JSONObject jsonObject = new JSONObject();
                RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
                long uptime = rb.getUptime();
                jsonObject.put("server_name", MMORPG.Server.getMotd());
                jsonObject.put("server_type_name", MMORPG.Server.getName());
                jsonObject.put("online_players", MMORPG.Server.getOnlinePlayers().size());
                jsonObject.put("server_up_time", uptime);
                response = jsonObject.toString();
            }
        }
        return response;
    }
}
