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
        Button addTicketButton = new Button("Create Item", e -> openAddTicketDialog());
        add(addTicketButton, grid);
    }

    private void configureGrid() {
        grid.removeAllColumns();

        grid.addColumn(Ticket::getPriority).setHeader("Priority").setAutoWidth(true);
        grid.addColumn(Ticket::getItem).setHeader("Title").setAutoWidth(true);
        grid.addColumn(Ticket::getDescription).setHeader("Description").setAutoWidth(true);
        grid.addColumn(Ticket::getSprint).setHeader("Sprint").setWidth("50px");

        grid.addComponentColumn(ticket -> {
            Button moveUpButton = new Button("↑", e -> moveUp(ticket));
            Button moveDownButton = new Button("↓", e -> moveDown(ticket));
            Button deleteButton = new Button("Delete", e -> deleteTicket(ticket));
            Button editButton = new Button("Edit", e -> openEditDialog(ticket));
            return new HorizontalLayout(moveUpButton, moveDownButton, editButton, deleteButton);
        }).setHeader("Actions");

        grid.setItems(tickets);
    }

    private void configureDragAndDrop() {
        grid.setRowsDraggable(true);
        grid.setDropMode(GridDropMode.ON_TOP);

        grid.addDragStartListener(event -> draggedTicket = event.getDraggedItems().get(0));
        grid.addDropListener(event -> {
            Ticket targetTicket = event.getDropTargetItem().orElse(null);
            GridDropLocation dropLocation = event.getDropLocation();

            if (draggedTicket != null && targetTicket != null && draggedTicket != targetTicket) {
                tickets.remove(draggedTicket);
                int targetIndex = tickets.indexOf(targetTicket) + (dropLocation == GridDropLocation.BELOW ? 1 : 0);
                tickets.add(targetIndex, draggedTicket);
                rebuildTicketList();
            }
            draggedTicket = null;
        });
        grid.addDragEndListener(event -> draggedTicket = null);
    }

    private void moveUp(Ticket ticket) {
        int index = tickets.indexOf(ticket);
        if (index > 0) {
            tickets.remove(index);
            tickets.add(index - 1, ticket);
            rebuildTicketList();
        }
    }

    private void moveDown(Ticket ticket) {
        int index = tickets.indexOf(ticket);
        if (index < tickets.size() - 1) {
            tickets.remove(index);
            tickets.add(index + 1, ticket);
            rebuildTicketList();
        }
    }

    private void rebuildTicketList() {
        for (int i = 0; i < tickets.size(); i++) {
            Ticket t = tickets.get(i);
            t.setPriority(i + 1);
            ticketService.save(t); // DB-Update
        }
        grid.getDataProvider().refreshAll();
    }

    private void deleteTicket(Ticket ticket) {
        tickets.remove(ticket);
        ticketService.delete(ticket);
        rebuildTicketList();
    }

    private void openEditDialog(Ticket ticket) {
        Dialog editDialog = new Dialog();
        TextField itemField = new TextField("Title", ticket.getItem());
        TextArea descriptionField = new TextArea("Description", ticket.getDescription());
        TextField sprintField = new TextField("Sprint", ticket.getSprint());

        FormLayout formLayout = new FormLayout(itemField, descriptionField, sprintField);
        Button saveButton = new Button("Save", e -> {
            if (!itemField.getValue().trim().isEmpty()) {
                ticket.setItem(itemField.getValue().trim());
            }
            if (!descriptionField.getValue().trim().isEmpty()) {
                ticket.setDescription(descriptionField.getValue().trim());
            }
            if (!sprintField.getValue().trim().isEmpty()) {
                ticket.setSprint(sprintField.getValue().trim());
            }

            ticketService.save(ticket);
            grid.getDataProvider().refreshItem(ticket);
            rebuildTicketList();
            editDialog.close();
        });
        Button cancelButton = new Button("Cancel", e -> editDialog.close());
        editDialog.add(new VerticalLayout(formLayout, new HorizontalLayout(saveButton, cancelButton)));
        editDialog.open();
    }

    private void openAddTicketDialog() {
        Dialog dialog = new Dialog();
        TextField itemField = new TextField("Title");
        TextArea descriptionField = new TextArea("Description");
        TextField sprintField = new TextField("Sprint");

        int nextPriority = tickets.isEmpty() ? 1 : tickets.stream().mapToInt(Ticket::getPriority).max().orElse(0) + 1;
        TextField priorityField = new TextField("Priority", String.valueOf(nextPriority));

        FormLayout formLayout = new FormLayout(itemField, descriptionField, priorityField, sprintField);
        Button saveButton = new Button("Save", e -> {
            Ticket newTicket = new Ticket();
            newTicket.setItem(itemField.getValue().trim());
            newTicket.setDescription(descriptionField.getValue().trim());
            newTicket.setSprint(sprintField.getValue().trim());

            String rawPriority = priorityField.getValue().trim();
            int priority;
            try {
                priority = Integer.parseInt(rawPriority);
            } catch (NumberFormatException ex) {
                priority = nextPriority; // fallback
            }
            newTicket.setPriority(priority);

            Ticket saved = ticketService.save(newTicket);
            tickets.add(saved);
            rebuildTicketList();
            dialog.close();
        });

        Button cancelButton = new Button("Cancel", e -> dialog.close());
        dialog.add(new VerticalLayout(formLayout, new HorizontalLayout(saveButton, cancelButton)));
        dialog.open();
    }
}
