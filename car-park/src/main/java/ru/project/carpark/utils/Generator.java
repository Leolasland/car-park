package ru.project.carpark.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Random;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Generator {

    private static final Random random = new Random();
    public static String generateString() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;

        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static Long generateLong() {
        return random.nextLong();
    }

    public static Integer generateInt() {
        return random.nextInt();
    }
}
