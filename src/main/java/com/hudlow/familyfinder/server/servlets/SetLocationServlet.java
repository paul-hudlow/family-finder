package com.hudlow.familyfinder.server.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hudlow.familyfinder.server.FriendRegistry;
import com.hudlow.familyfinder.server.LocationRegistry;
import com.hudlow.familyfinder.server.data.LatLng;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;

public class SetLocationServlet extends HttpServlet {

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
            Float lat = json.get("lat").floatValue();
            Float lng = json.get("lng").floatValue();
            LocationRegistry.getRegistry().setLocation(myUserId, new LatLng(lat, lng));

            response.setContentType("application/json");
            response.getWriter().println("{ \"success\": true }");
        } catch (IOException ex) {
            status = 500;
        }

        response.setStatus(status);
    }

}
