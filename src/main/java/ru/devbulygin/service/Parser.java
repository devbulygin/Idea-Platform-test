package ru.devbulygin.service;

import com.google.gson.Gson;
import ru.devbulygin.dto.Ticket;
import ru.devbulygin.dto.TicketsCollections;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public abstract class Parser {

    public static String readJson(String pathToJson) {
        try {
            return Files.readString(Path.of(pathToJson)).toString().trim().replaceFirst("\ufeff", "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Ticket> jsonToTickets(String json) {
        TicketsCollections ticketsCollections = new Gson().fromJson(json, TicketsCollections.class);
        return ticketsCollections.getTickets();
    }


}
