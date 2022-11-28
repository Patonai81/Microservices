package hu.webuni.booking.web;

import hu.webuni.booking.dto.PurchaseData;
import hu.webuni.booking.dto.TicketData;
import hu.webuni.booking.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class BookingController {

    @Autowired
    PurchaseService purchaseService;

    @PostMapping("/ticket")
    public PurchaseData buyTicket(@RequestBody TicketData ticketData) {
        return purchaseService.buyTicket(ticketData);
    }
}
