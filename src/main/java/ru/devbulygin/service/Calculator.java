package ru.devbulygin.service;

import ru.devbulygin.dto.Ticket;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Calculator {
    private static final String VVO = "VVO";
    private static final String TLV = "TLV";
    private static final String VLADIVOSTOK_ZONE = "+10";
    private static final String TEL_AVIV_ZONE = "+3";
    public static final String DEFAULT_ZONE = "+0";

    public static ZonedDateTime getTime(String formattedDate, String formattedTime, String airport) {

        ZoneId zone = ZoneOffset.of(getOffset(airport));
        String unitedDate = formattedDate + "T" + formattedTime;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy'T'H:mm").withZone(zone);

        return ZonedDateTime.parse(unitedDate, formatter);
    }

    public static String getOffset(String airport) {

        switch (airport) {
            case (VVO):
                return VLADIVOSTOK_ZONE;
            case (TLV):
                return TEL_AVIV_ZONE;
            default:
                return DEFAULT_ZONE;
        }
    }


    public static Duration getFlyTime(Ticket ticket) {
        ZonedDateTime departureTime = getTime(
                ticket.getDeparture_date(),
                ticket.getDeparture_time(),
                ticket.getOrigin());
        ZonedDateTime arrivalTime = getTime(
                ticket.getArrival_date(),
                ticket.getArrival_time(),
                ticket.getDestination());

        return Duration.between(departureTime, arrivalTime);
    }


    public static Map getMinimalFlyTimeForEveryoneCarrier(List<Ticket> tickets) {
        Map<String, Duration> minimalFlightsByCarriers = new HashMap<>();

        for (Ticket ticket : tickets) {

            String originAirport = ticket.getOrigin();
            String destinationAirport = ticket.getDestination();


            if (originAirport.equals(VVO) && destinationAirport.equals(TLV)) {
                String carrier = ticket.getCarrier();
                Duration duration = getFlyTime(ticket);
                int price = ticket.getPrice();

                if (minimalFlightsByCarriers.containsKey(carrier)) {
                    if (minimalFlightsByCarriers.get(carrier).compareTo(duration) > 0) {
                        minimalFlightsByCarriers.put(carrier, duration);
                    }
                } else {
                    minimalFlightsByCarriers.put(carrier, duration);
                }
            }
        }
        return minimalFlightsByCarriers;
    }


    public static Double getAveragePrice(List<Ticket> tickets) {
        List<Ticket> vvoAndTlvTickets = getVVOAndTLVTickets(tickets);
        return vvoAndTlvTickets.stream()
                .collect(Collectors.averagingInt(Ticket::getPrice));
    }

    public static Double getMedianPrice(List<Ticket> tickets) {
        List<Ticket> vvoAndTlvTickets = getVVOAndTLVTickets(tickets);

        double median = vvoAndTlvTickets.get(vvoAndTlvTickets.size() / 2).getPrice();
        if (vvoAndTlvTickets.size() % 2 == 0) {
            return (median + vvoAndTlvTickets.get(vvoAndTlvTickets.size() / 2 - 1).getPrice()) / 2;
        }

        return median;
    }

    public static List<Ticket> getVVOAndTLVTickets(List<Ticket> tickets) {
        return tickets.stream()
                .filter(ticket -> ticket.getOrigin().equals(VVO))
                .filter(ticket -> ticket.getDestination().equals(TLV))
                .sorted(Comparator.comparingDouble(Ticket::getPrice))
                .toList();
    }

    public static String getResult(List<Ticket> tickets) {

        StringBuffer result = new StringBuffer();

        Map<String, Duration> minimalFlightsByCarriers = getMinimalFlyTimeForEveryoneCarrier(tickets);

        minimalFlightsByCarriers.forEach((k, v) -> {
            result.append("Минимальное время полета между городами Владивосток и Тель-Авив для авиаперевозчика ");
            result.append(k);
            result.append(" cоставляет ");
            result.append(v.toHours());
            result.append(" ч ");
            result.append(v.toMinutes() % 60);
            result.append(" мин. \n");
        });

        Double averagePrice = getAveragePrice(tickets);
        Double medianPrice = getMedianPrice(tickets);

        result.append("\n");
        result.append("Разница между средней ценой и медианой для полета между городами Владивосток и Тель-Авив - ");
        result.append(averagePrice - medianPrice);

        return result.toString();
    }
}
