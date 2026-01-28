package com.os467.pokemonhelper.tracker.config;

public class Config {

    public static final int DEFAULT_RANGE = 58;
    private static int searchRange = DEFAULT_RANGE;
    private static boolean trackingEnabled = true;
    private static String trackedEntityName = "";

    public static String[] getTrackedEntityNames() {
        String[] trackedEntityNames = trackedEntityName.split(",");
        return trackedEntityNames;
    }

    public static String getTrackedEntityName() {
        return trackedEntityName;
    }

    public static void setTrackedEntityName(String name) {
        trackedEntityName = name;
    }
    public static int getSearchRange() {
        return searchRange;
    }

    public static void setSearchRange(int range) {
        searchRange = range;
    }

    public static boolean isTrackingEnabled() {
        return trackingEnabled;
    }

    public static void setTrackingEnabled(boolean enabled) {
        trackingEnabled = enabled;
    }


}