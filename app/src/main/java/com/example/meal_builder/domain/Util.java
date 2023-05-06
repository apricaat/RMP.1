package com.example.meal_builder.domain;

import java.util.concurrent.ThreadLocalRandom;

public class Util {
    static public int random(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
