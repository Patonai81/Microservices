package hu.webuni.booking.service;

import hu.webuni.bonus.api.BonusApi;
import hu.webuni.booking.dto.PurchaseData;
import hu.webuni.booking.dto.TicketData;
import hu.webuni.currency.api.CurrencyApi;
import hu.webuni.flights.api.FlightsApi;
import hu.webuni.flights.dto.Airline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Comparator;
import java.util.List;

@Service
public class PurchaseService {

    public static final String USD = "USD";
    @Value("${booking.bonus}")
    double bonusRate;

    @Autowired
    BonusApi bonusApi;
    @Autowired
    CurrencyApi currencyApi;
    @Autowired
    FlightsApi flightsApi;

    @PostMapping("/ticket")
    public PurchaseData buyTicket(@RequestBody TicketData ticketData) {

        PurchaseData purchaseData = new PurchaseData();
        purchaseData.setSuccess(false);

        try {
            Airline airline = getCheepestFlight(ticketData);
            if (airline == null) {
                return purchaseData;
            }
            calculatePriceWithBonus(ticketData, purchaseData, airline);
            calculateEarnedBonus(ticketData,purchaseData,airline);

        }catch (Exception e){
            System.out.println(e);
            return purchaseData;
        }
        purchaseData.setSuccess(true);
        return purchaseData;
    }

    private void calculateEarnedBonus(TicketData ticketData, PurchaseData purchaseData, Airline airline) {
        double realPrice = purchaseData.getPrice();
        double earnedBonus = realPrice * bonusRate;
        bonusApi.addPoints(ticketData.getUser(), earnedBonus);
        purchaseData.setBonusEarned(earnedBonus);
    }


    private void calculatePriceWithBonus(TicketData ticketData, PurchaseData purchaseData, Airline airline) {

        if (ticketData.isUseBonus()) {
            double bounusPoints = getBounsPoint(ticketData.getUser());
            double usedBonus = airline.getPrice() > bounusPoints ? bounusPoints : airline.getPrice();
            bonusApi.addPoints(ticketData.getUser(), usedBonus);
            purchaseData.setBonusUsed(usedBonus);
            purchaseData.setPrice(airline.getPrice() - usedBonus);
        }

    }

    private Airline getCheepestFlight(TicketData ticketData) {
        List<Airline> result = (flightsApi.searchFlight(ticketData.getFrom(), ticketData.getTo()));
        if (null == result || result.size() == 0) {
            return null;
        }

        result.stream().forEach(airline -> {
            double rate = 1.0;
            if (!airline.getCurrency().equals(USD)) {
                rate = getRate(airline.getCurrency(), USD);
            }
            airline.setCurrency(USD);
            airline.setPrice(airline.getPrice() * rate);
        });

        Airline cheapestAirline = result.stream().min(Comparator.comparing(Airline::getPrice)).orElseThrow(RuntimeException::new);
        return cheapestAirline;
    }


    private double getRate(String from, String to) {
        return currencyApi.getRate(from, to);
    }

    private double getBounsPoint(String user) {
        return bonusApi.getPoints(user);
    }

}
