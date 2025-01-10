package com.example.application.views.ticketcard;

import com.example.application.data.Ticket;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.map.Map;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.router.RouteParameters;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;

import java.util.List;
import java.util.Optional;

@Route("ticket")
public class TicketCard extends VerticalLayout {

  /*  public TicketDetailView(QueryParameters queryParameters) {
        // Header
        add(new H1("Ticket Details"));

        // TicketNr aus den Query-Parametern abrufen
        Map<String, List<String>> parameters = queryParameters.getParameters();
        int ticketNr = Integer.parseInt(parameters.get("id").get(0));

        // Ticket-Details abrufen
        Optional<Ticket> optionalTicket = TicketDAO.getById(ticketNr);

        if (optionalTicket.isPresent()) {
            Ticket ticket = optionalTicket.get();

            // Details anzeigen
            add(new Paragraph("TicketNr: " + ticket.getTicketNr()));
            add(new Paragraph("Beschreibung: " + ticket.getBeschreibung()));
            add(new Paragraph("Priorität: " + ticket.getPrioritaet()));
            add(new Paragraph("Zugewiesene Person: " + ticket.getPerson()));
        } else {
            add(new Paragraph("Ticket nicht gefunden."));
        }
    }*/
}


