package com.epam.trainings.spring.core.dm.ui;

import com.epam.trainings.spring.core.dm.model.Rating;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

public class ConsoleReader {

    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public Rating readRating() {
        while (true) {
            try {
                return Rating.valueOf(reader.readLine().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Please enter correct rating: " + Arrays.toString(Rating.values()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public double readDouble() {
        while (true) {
            try {
                return Double.parseDouble(reader.readLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter correct double value!");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Long readLong() {
        while (true) {
            try {
                return Long.parseLong(reader.readLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter correct int value!");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public int readInt() {
        while (true) {
            try {
                return Integer.parseInt(reader.readLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter correct int value!");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean readBoolean() {
        try {
            return Boolean.parseBoolean(reader.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String readString() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public LocalDateTime readLocalDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        while (true) {
            try {
                return LocalDateTime.parse(reader.readLine(), formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Please enter correct date time value, example: 1986-04-08 12:30");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public LocalDate readLocalDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while (true) {
            try {
                return LocalDate.parse(reader.readLine(), formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Please enter correct date value, example: 1986-04-08");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
