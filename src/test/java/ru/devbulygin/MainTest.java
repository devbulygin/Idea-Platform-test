package ru.devbulygin;

import org.junit.jupiter.api.Test;
import ru.devbulygin.dto.Ticket;
import ru.devbulygin.service.Calculator;
import ru.devbulygin.service.Parser;

import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MainTest {

    private static final String RESULT =
            "Минимальное время полета между городами Владивосток и Тель-Авив "
            + "для авиаперевозчика SU cоставляет 13 ч 0 мин. \n"
            + "Минимальное время полета между городами Владивосток и Тель-Авив "
            + "для авиаперевозчика S7 cоставляет 13 ч 30 мин. \n"
            + "Минимальное время полета между городами Владивосток и Тель-Авив "
            + "для авиаперевозчика TK cоставляет 13 ч 10 мин. \n"
            + "\n"
            + "Разница между средней ценой и медианой для полета между городами Владивосток и Тель-Авив - 200.0";


    public static List<Ticket> getExpectedTickets() {

        return List.of(
                new Ticket("VVO", "Владивосток", "TLV", "Тель-Авив",
                        "12.05.18", "16:00", "12.05.18", "22:10",
                        "TK", 3, 12400),
                new Ticket("VVO", "Владивосток", "TLV", "Тель-Авив",
                        "12.05.18", "17:20", "12.05.18", "23:50",
                        "S7", 1, 13100),
                new Ticket("VVO", "Владивосток", "TLV", "Тель-Авив",
                        "12.05.18", "12:10", "12.05.18", "18:10",
                        "SU", 0, 15300),
                new Ticket("VVO", "Владивосток", "TLV", "Тель-Авив",
                        "12.05.18", "17:00", "12.05.18", "23:30",
                        "TK", 2, 11000),
                new Ticket("VVO", "Владивосток", "UFA", "Уфа",
                        "12.05.18", "15:15", "12.05.18", "17:45",
                        "TK", 1, 33400)
        );

    }

    private static String getFixturePath(String fileName) {
        return Paths.get("src", "test", "resources", fileName)
                .toAbsolutePath().normalize().toString();
    }

    @Test
    void parseToObject() {
        assertThat(Parser.jsonToTickets(Parser.readJson(getFixturePath("tickets.json"))))
                .isEqualTo(getExpectedTickets());
    }

    @Test
    void resultTest() {
        assertThat(Calculator.getResult(getExpectedTickets()))
                .isEqualTo(RESULT);

    }
}

