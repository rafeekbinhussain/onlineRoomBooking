package com.conference.online.utils;

import java.util.Map;

public class ConferenceRoomMapUtil {

    public static final Map<String, Integer> CONFERENCE_MAP = Map.ofEntries(
            Map.entry("Amaze", 3),
            Map.entry("Beauty", 7),
            Map.entry("Inspire", 12),
            Map.entry("Strive", 20)
    );

    private ConferenceRoomMapUtil() {
    }
}
