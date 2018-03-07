package com.hudlow.familyfinder.server.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hudlow.familyfinder.server.FriendRegistry;
import com.hudlow.familyfinder.server.LocationRegistry;
import com.hudlow.familyfinder.server.data.FriendshipException;
import com.hudlow.familyfinder.server.data.LatLng;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;

public class GetLocationServlet extends HttpServlet {

    private static int MAX_ATTEMPTS = 10;
    private static int TIMEOUT_MILLISECOND = 200;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        int status = 200;
        try {
            int contentLength = request.getContentLength();
            Reader reader = request.getReader();
            StringBuilder buffer = new StringBuilder();

            int lengthRead = 0;
            for (int attempts = 0; attempts < MAX_ATTEMPTS; attempts++) {
                while (reader.ready()) {
                    buffer.append((char)reader.read());
                    lengthRead++;
                }
                if (lengthRead == contentLength) {
                    break;
                }
                try {
                    Thread.sleep(TIMEOUT_MILLISECOND);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (contentLength != lengthRead) {
                throw new IOException("Content ended unexpectedly.");
            }
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode json = mapper.readValue(buffer.toString(), ObjectNode.class);
            String myUserId = json.get("myUserId").textValue();
            String friendUserId = json.get("friendUserId").textValue();
            if (!FriendRegistry.getRegistry().isFriend(friendUserId, myUserId)) {
                throw new FriendshipException();
            }
            LatLng location = LocationRegistry.getRegistry().getLocation(friendUserId);

            response.setContentType("application/json");
            if (location != null) {
                response.getWriter().println("{ \"success\": true, \"lat\": " + location.lat + ", \"lng\": " + location.lng + " }");
            } else {
                response.getWriter().println("{ \"success\": false }");
            }
        } catch (IOException ex) {
            status = 500;
        } catch (FriendshipException ex) {
            status = 401;
        }

        response.setStatus(status);
    }

}
