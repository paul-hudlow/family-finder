package com.hudlow.familyfinder.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FriendRegistry {

    private static FriendRegistry registry = new FriendRegistry();

    public static FriendRegistry getRegistry() {
        return registry;
    }

    private Map<String, Set<String>> friendMap = new HashMap<String, Set<String>>();

    public void addFriend(String myUserId, String friendUserId) {
        if (!friendMap.containsKey(myUserId)) {
            friendMap.put(myUserId, new HashSet<String>());
        }
        friendMap.get(myUserId).add(friendUserId);
    }

    public void removeFriend(String myUserId, String friendUserId) {
        if (!friendMap.containsKey(myUserId)) {
            return; // No friends.
        }
        friendMap.get(myUserId).remove(friendUserId);
    }

    public boolean isFriend(String myUserId, String friendUserId) {
        if (!friendMap.containsKey(myUserId)) {
            return false; // No friends.
        }
        return friendMap.get(myUserId).contains(friendUserId);
    }
}
