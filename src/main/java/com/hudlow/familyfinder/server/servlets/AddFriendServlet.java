package com.hudlow.familyfinder.server.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.appengine.api.utils.SystemProperty;
import com.hudlow.familyfinder.server.FriendRegistry;

import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// With @WebServlet annotation the webapp/WEB-INF/web.xml is no longer required.
@WebServlet(name = "HelloAppEngine", value = "/hello")
public class AddFriendServlet extends HttpServlet {

    private static int MAX_ATTEMPTS = 10;
    private static int TIMEOUT_MILLISECOND = 200;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Properties properties = System.getProperties();

        int contentLength = request.getContentLength();
        Reader reader = request.getReader();
        StringBuilder buffer = new StringBuilder();

        for (int attempts = 0; attempts < MAX_ATTEMPTS; attempts++) {
            while (reader.ready()) {
                buffer.append(reader.read());
            }
            try {
                Thread.sleep(TIMEOUT_MILLISECOND);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.readValue(buffer.toString(), ObjectNode.class);
        String myUserId = json.get("myUserId").textValue();
        String friendUserId = json.get("friendUserId").textValue();
        FriendRegistry.getRegistry().addFriend(myUserId, friendUserId);

        response.setContentType("application/json");
        response.getWriter().println("Hello App Engine - Standard using "
                + SystemProperty.version.get() + " Java "
                + properties.get("java.specification.version"));
    }

    public static String getInfo() {
        return "Version: " + System.getProperty("java.version")
                + " OS: " + System.getProperty("os.name")
                + " User: " + System.getProperty("user.name");
    }

}
