package at.feddis08.tools.remote_interface.web_service;

import at.feddis08.Boot;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Server;

import java.io.IOException;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
    private static Server server;
    public static Integer port = 8080;

    public static boolean stop = false;

    public static ArrayList<Web_user> web_users = new ArrayList<>();
    public static Thread t = new Thread(){
        @Override
      public void run() {
            int i = 0;
            int remove_i = -1;
            while (!stop){
                Web_user web_user = web_users.get(i);
                if (web_users.size() > i) {
                    i = 0;
                    if (remove_i != -1) {
                        web_users.remove(remove_i);
                        remove_i = -1;
                    }
                }
                if (web_user.last_resp + web_user.timeout < System.currentTimeMillis())remove_i = i;
                i++;
            }
        }
    };
    public static void start() throws Exception {
        Boot.consoleLog("Starting WebService at port: " + port);
        server = new Server(port);
        ServletContextHandler handler = new ServletContextHandler();
        handler.setContextPath("/");
        server.setHandler(handler);
        handler.addServlet(ApiServlet.class, "/api");

        handler.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                resp.addHeader("Access-Control-Allow-Origin", "*");
                resp.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
                resp.addHeader("Access-Control-Allow-Headers", "Content-Type");
            }
        }), "/*");
        server.start();
        server.join();
        t.start();
        Boot.consoleLog("done...");
    }

    public static class ApiServlet extends HttpServlet {
            @Override
            protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                response.setHeader("Access-Control-Allow-Origin", "*");
                try {
                    response.getWriter().println(Parse_req.do_req(request));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        @Override
        protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            resp.addHeader("Access-Control-Allow-Origin", "*");
            resp.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
            resp.addHeader("Access-Control-Allow-Headers", "Content-Type");
        }

    }
    public static void stop() throws Exception {
        stop = true;
        server.stop();
    }
}
