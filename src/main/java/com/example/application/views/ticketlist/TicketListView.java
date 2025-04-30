package com.example.application.views.ticketlist;

import com.example.application.data.Ticket;
import com.example.application.service.TicketService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dnd.GridDropLocation;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.List;

@PageTitle("Product Backlog List")
@Route("")
@Menu(order = 0, icon = LineAwesomeIconUrl.PENCIL_RULER_SOLID)
public class TicketListView extends VerticalLayout {

    private final TicketService ticketService;
    private final Grid<Ticket> grid = new Grid<>(Ticket.class);
    private Ticket draggedTicket;
    private List<Ticket> tickets;

    @Autowired
    public TicketListView(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostConstruct
    private void init() {
        this.tickets = ticketService.findAll();
        configureGrid();
        configureDragAndDrop();
        add(createAddButton(), grid);
    }

    private Button createAddButton() {
        return new Button("Create Item", e -> {
            Ticket newTicket = new Ticket();
            openTicketDialog(newTicket, true);
        });
    }

    private void configureGrid() {
        grid.removeAllColumns();
        grid.addColumn(Ticket::getPriority).setHeader("Priority").setAutoWidth(true);
        grid.addColumn(Ticket::getItem).setHeader("Title").setAutoWidth(true);
        grid.addColumn(Ticket::getDescription).setHeader("Description").setAutoWidth(true);
        grid.addColumn(Ticket::getSprint).setHeader("Sprint").setWidth("50px");

        grid.addComponentColumn(ticket -> {
            Button up = new Button("↑", e -> move(ticket, -1));
            Button down = new Button("↓", e -> move(ticket, +1));
            Button edit = new Button("Edit", e -> openTicketDialog(ticket, false));
            Button del = new Button("Delete", e -> deleteTicket(ticket));
            return new HorizontalLayout(up, down, edit, del);
        }).setHeader("Actions");

        grid.setItems(tickets);
    }

    private void configureDragAndDrop() {
        grid.setRowsDraggable(true);
        grid.setDropMode(GridDropMode.ON_TOP);

        grid.addDragStartListener(e -> draggedTicket = e.getDraggedItems().getFirst());
        grid.addDropListener(e -> {
            Ticket target = e.getDropTargetItem().orElse(null);
            if (draggedTicket != null && target != null && draggedTicket != target) {
                tickets.remove(draggedTicket);
                int idx = tickets.indexOf(target)
                        + (e.getDropLocation() == GridDropLocation.BELOW ? 1 : 0);
                if (idx < 0) idx = 0;
                if (idx > tickets.size()) idx = tickets.size();
                tickets.add(idx, draggedTicket);
                rebuildTicketList();
            }
            draggedTicket = null;
        });
        grid.addDragEndListener(e -> draggedTicket = null);
    }

    private void move(Ticket ticket, int offset) {
        int i = tickets.indexOf(ticket);
        int j = i + offset;
        if (j >= 0 && j < tickets.size()) {
            tickets.remove(i);
            tickets.add(j, ticket);
            rebuildTicketList();
        }
    }

    private void deleteTicket(Ticket ticket) {
        tickets.remove(ticket);
        ticketService.delete(ticket);
        rebuildTicketList();
    }

    private void openTicketDialog(Ticket ticket, boolean isNew) {
        Dialog dlg = new Dialog();
        TextField itemField = new TextField("Title", ticket.getItem());
        TextArea descField = new TextArea("Description", ticket.getDescription());
        TextField sprintField = new TextField("Sprint", ticket.getSprint());

        int nextPrio = tickets.isEmpty() ? 1
                : tickets.stream().mapToInt(Ticket::getPriority).max().orElse(0) + 1;
        TextField prioField = new TextField("Priority",
                ticket.getPriority() > 0 ? String.valueOf(ticket.getPriority()) : String.valueOf(nextPrio));

        FormLayout form = new FormLayout(itemField, descField, prioField, sprintField);
        Button save = new Button("Save", e -> {
            ticket.setItem(itemField.getValue().trim());
            ticket.setDescription(descField.getValue().trim());
            ticket.setSprint(sprintField.getValue().trim());

            int p;
            try {
                p = Integer.parseInt(prioField.getValue().trim());
            } catch (NumberFormatException ex) {
                p = nextPrio;
            }
            ticket.setPriority(p);

            Ticket saved = ticketService.save(ticket);
            if (isNew) {
                tickets.add(saved);
            }
            rebuildTicketList();
            dlg.close();
        });
        Button cancel = new Button("Cancel", e -> dlg.close());

        dlg.add(new VerticalLayout(form, new HorizontalLayout(save, cancel)));
        dlg.open();
    }

    private void rebuildTicketList() {
        for (int i = 0; i < tickets.size(); i++) {
            Ticket t = tickets.get(i);
            t.setPriority(i + 1);
            ticketService.save(t);
        }
        grid.setItems(tickets);
    }
}
