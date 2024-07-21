package ru.project.carparkgenerator.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Random;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Generator {

    private static final Random random = new Random();

    public static Integer generateInt() {
        return random.nextInt();
    }
}
