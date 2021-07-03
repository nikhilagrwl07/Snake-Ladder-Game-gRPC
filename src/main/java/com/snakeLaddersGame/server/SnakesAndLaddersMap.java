package com.snakeLaddersGame.server;

import java.util.HashMap;
import java.util.Map;

public class SnakesAndLaddersMap {
    private static final Map<Integer, Integer> MAP = new HashMap<>();

    static {
        // ladders
        MAP.put(1, 38);
        MAP.put(4, 14);
        MAP.put(8, 30);
        MAP.put(21, 42);
        MAP.put(28, 76);


        // snakes
        MAP.put(32, 10);
        MAP.put(36, 6);
        MAP.put(48, 26);
        MAP.put(62, 18);
    }

    public static int getPosition(int position) {
        return MAP.getOrDefault(position, position);
    }
}
