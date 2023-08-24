package ru.devbulygin;

/*Задание
Напишите программу на языке программирования java, которая прочитает файл tickets.json и рассчитает:
- Минимальное время полета между городами Владивосток и Тель-Авив для каждого авиаперевозчика
- Разницу между средней ценой и медианой для полета между городами Владивосток и Тель-Авив

Программа должна вызываться из командной строки Linux, результаты должны быть представлены в текстовом виде.
В качестве результата нужно прислать ответы на поставленные вопросы и ссылку на исходный код.
*/

import ru.devbulygin.dto.Ticket;
import lombok.SneakyThrows;
import ru.devbulygin.service.Calculator;
import ru.devbulygin.service.Parser;

import java.util.List;


public class Main {
    public static final String PATH = "src/main/resources/tickets.json";
    @SneakyThrows
    public static void main(String[] args) {

        String json = Parser.readJson(PATH);

        List<Ticket> tickets = Parser.jsonToTickets(json);

        System.out.println(Calculator.getResult(tickets));

    }
}
