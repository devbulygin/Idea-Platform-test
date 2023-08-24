package ru.devbulygin.service;

import ru.devbulygin.dto.Ticket;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlightTimeCalculator {
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


    public static String getMinimalFlyTimeForEveryoneCarrier(List<Ticket> tickets) {
        Map<String, Duration> minimalFlyByCarriers = new HashMap<>();

        for (Ticket ticket : tickets) {

            String originAirport = ticket.getOrigin();
            String destinationAirport = ticket.getDestination();


            if (originAirport.equals(VVO) && destinationAirport.equals(TLV)) {
                String carrier = ticket.getCarrier();
                Duration duration = getFlyTime(ticket);
                int price = ticket.getPrice();

                if (minimalFlyByCarriers.containsKey(carrier)) {
                    if (minimalFlyByCarriers.get(carrier).compareTo(duration) > 0) {
                        minimalFlyByCarriers.put(carrier, duration);
                    }
                } else {
                    minimalFlyByCarriers.put(carrier, duration);
                }
            }
        }
        StringBuffer result = new StringBuffer();

        minimalFlyByCarriers.forEach((k, v) -> {
            result.append("Минимальное время полета между городами Владивосток и Тель-Авив для авиаперевозчика ");
            result.append(k);
            result.append(" cоставляет ");
            result.append(v.toHours());
            result.append(" ч ");
            result.append(v.toMinutes() % 60);
            result.append(" мин. \n");
        });

        return result.toString();
    }
}
