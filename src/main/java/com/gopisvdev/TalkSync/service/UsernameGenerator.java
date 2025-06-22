package com.gopisvdev.TalkSync.service;

import com.github.javafaker.Faker;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UsernameGenerator {
    private final Faker faker = new Faker();
    private final Random random = new Random();

    public String generateUsername() {
        String adjective = cleanWord(faker.company().buzzword());
        String animal = cleanWord(faker.animal().name());
        int number = 100 + random.nextInt(9000);

        return capitalize(adjective) + capitalize(animal) + number;
    }

    private String cleanWord(String input) {
        return input.replaceAll("[^a-zA-Z0-9]", "");
    }

    private String capitalize(String input) {
        if (input == null || input.isEmpty()) return "";

        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
}
