package com.hudlow.familyfinder.server;

import com.google.appengine.repackaged.com.google.type.LatLng;

import java.util.HashMap;
import java.util.Map;

public class LocationRegistry {
    private Map<String, LatLng> locationMap = new HashMap<String, LatLng>();

    public LatLng getLocation(String userId) {
        return locationMap.get(userId);
    }

    public void setLocation(String userId, LatLng location) {
        locationMap.put(userId, location);
    }
}
