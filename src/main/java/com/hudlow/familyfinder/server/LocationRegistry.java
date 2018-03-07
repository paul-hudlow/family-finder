package com.hudlow.familyfinder.server;

import com.hudlow.familyfinder.server.data.LatLng;

import java.util.HashMap;
import java.util.Map;

public class LocationRegistry {

    private static LocationRegistry registry = new LocationRegistry();

    public static LocationRegistry getRegistry() {
        return registry;
    }

    private Map<String, LatLng> locationMap = new HashMap<String, LatLng>();

    public LatLng getLocation(String userId) {
        return locationMap.get(userId);
    }

    public void setLocation(String userId, LatLng location) {
        locationMap.put(userId, location);
    }
}
