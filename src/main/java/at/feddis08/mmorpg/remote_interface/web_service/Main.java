package at.feddis08.mmorpg.remote_interface.web_service;

import at.feddis08.mmorpg.MMORPG;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Server;

import java.io.IOException;

import org.eclipse.jetty.server.session.DefaultSessionIdManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
    private static Server server;
    public static Integer port = 8080;

    public static ArrayList<Web_user> web_users = new ArrayList<>();
    public static void start() throws Exception {
        MMORPG.consoleLog("Starting WebService at port: " + port);
        server = new Server(port);

        ServletContextHandler handler = new ServletContextHandler();
        SessionHandler sessionHandler = new SessionHandler();

// Add the session handler to the server
        server.setHandler(sessionHandler);
        handler.setContextPath("/");
        server.setHandler(handler);





        HashSessionIdManager sessionIdManager = new HashSessionIdManager();
        DefaultSessionIdManager defaultSessionIdManager = new DefaultSessionIdManager(server);
        sessionIdManager.setWorkerName(defaultSessionIdManager.getWorkerName());
        server.setSessionIdManager(sessionIdManager);

        HashSessionManager sessionManager = new HashSessionManager();
        SessionHandler sessionHandler = new SessionHandler(sessionManager);
        server.setHandler(sessionHandler);







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

        MMORPG.consoleLog("done...");
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
        server.stop();
    }
}
