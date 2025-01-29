package com.example.application.views.ticketlist;

import com.example.application.data.Ticket;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
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

import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Product Backlog List")
@Route("")
@Menu(order = 0, icon = LineAwesomeIconUrl.PENCIL_RULER_SOLID)
public class TicketListView extends VerticalLayout {

    private final List<Ticket> tickets = new ArrayList<>();
    private final Grid<Ticket> grid = new Grid<>(Ticket.class);
    private Ticket draggedTicket;

    public TicketListView() {
        configureGrid();
        configureDragAndDrop();

        Button addTicketButton = new Button("Create Item", e -> openAddTicketDialog());

        add(addTicketButton, grid);
    }

    private void deleteTicket(Ticket ticket) {
        tickets.remove(ticket);
        recalculatePriorities();
        grid.setItems(tickets);
    }

    private void configureGrid() {
        grid.removeAllColumns();

        grid.addColumn(Ticket::getPriority).setHeader("Priority").setAutoWidth(true);
        grid.addColumn(Ticket::getItem).setHeader("Item").setAutoWidth(true);
        grid.addColumn(Ticket::getDescription).setHeader("Description").setAutoWidth(true);
        grid.addColumn(Ticket::getSprint).setHeader("Sprint").setAutoWidth(true);

        grid.addComponentColumn(ticket -> {
            Button deleteButton = new Button("Delete", e -> deleteTicket(ticket));
            Button editButton = new Button("Edit", e -> openEditDialog(ticket));

            HorizontalLayout actionsLayout = new HorizontalLayout(editButton, deleteButton);
            actionsLayout.setSpacing(true);

            return actionsLayout;
        }).setHeader("Actions");

        grid.setItems(tickets);
    }

    private void configureDragAndDrop() {
        grid.setRowsDraggable(true); // Aktiviert das Ziehen von Zeilen
        grid.setDropMode(GridDropMode.ON_TOP); // Festlegen des Drop-Modes auf "ON_TOP"

        grid.addDragStartListener(event -> {
            // Das gezogene Ticket speichern
            draggedTicket = event.getDraggedItems().get(0);
        });

        grid.addDropListener(event -> {
            Ticket targetTicket = event.getDropTargetItem().orElse(null); // Ziel-Ticket
            GridDropLocation dropLocation = event.getDropLocation(); // Position des Drops

            if (draggedTicket != null && targetTicket != null && draggedTicket != targetTicket) {
                tickets.remove(draggedTicket); // Entfernen des gezogenen Tickets aus der Liste

                // Zielindex bestimmen
                int targetIndex = tickets.indexOf(targetTicket) + (dropLocation == GridDropLocation.BELOW ? 1 : 0);
                tickets.add(targetIndex, draggedTicket); // An der neuen Position hinzufügen

                recalculatePriorities(); // Prioritäten neu berechnen
                grid.setItems(tickets); // Grid aktualisieren
            }

            // Gezogene Zeile zurücksetzen
            draggedTicket = null;
        });

        grid.addDragEndListener(event -> {
            draggedTicket = null; // Sicherstellen, dass kein altes gezogenes Element bleibt
        });
    }



    private void openEditDialog(Ticket ticket) {
        Dialog editDialog = new Dialog();

        TextField priorityField = new TextField("Priority", String.valueOf(ticket.getPriority()));
        TextField itemField = new TextField("Item", ticket.getItem());
        TextArea descriptionField = new TextArea("Description", ticket.getDescription());
        TextField sprintField = new TextField("Sprint", ticket.getSprint());

        FormLayout formLayout = new FormLayout();
        formLayout.add(priorityField, itemField, descriptionField, sprintField);

        Button saveButton = new Button("Save", e -> {
            try {
                int newPriority = Integer.parseInt(priorityField.getValue());
                ticket.setPriority(newPriority);
                ticket.setItem(itemField.getValue());
                ticket.setDescription(descriptionField.getValue());
                ticket.setSprint(sprintField.getValue());

                recalculatePriorities();
                grid.setItems(tickets);
                editDialog.close();
            } catch (NumberFormatException ex) {
                priorityField.setInvalid(true);
                priorityField.setErrorMessage("Priority must be a number.");
            }
        });

        Button cancelButton = new Button("Cancel", e -> editDialog.close());

        HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, cancelButton);
        VerticalLayout dialogLayout = new VerticalLayout(formLayout, buttonsLayout);

        editDialog.add(dialogLayout);
        editDialog.open();
    }

    private void openAddTicketDialog() {
        Dialog dialog = new Dialog();

        TextField itemField = new TextField("Item");
        TextArea descriptionField = new TextArea("Description");
        TextField priorityField = new TextField("Priority");
        TextField sprintField = new TextField("Sprint");

        int nextPriority = tickets.stream()
                .mapToInt(Ticket::getPriority)
                .max()
                .orElse(0) + 1;

        priorityField.setValue(String.valueOf(nextPriority));

        FormLayout formLayout = new FormLayout();
        formLayout.add(itemField, descriptionField, priorityField, sprintField);

        Button saveButton = new Button("Save", e -> {
            try {
                int priority = Integer.parseInt(priorityField.getValue());
                String item = itemField.getValue();
                String description = descriptionField.getValue();
                String sprint = sprintField.getValue();

                Ticket newTicket = new Ticket(priority, item, description, sprint);
                tickets.add(newTicket);

                recalculatePriorities();
                grid.setItems(tickets);
                dialog.close();
            } catch (NumberFormatException ex) {
                priorityField.setInvalid(true);
                priorityField.setErrorMessage("Priority must be a number.");
            }
        });

        Button cancelButton = new Button("Cancel", e -> dialog.close());
        HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, cancelButton);

        VerticalLayout dialogLayout = new VerticalLayout(formLayout, buttonsLayout);
        dialog.add(dialogLayout);

        dialog.open();
    }

    private void recalculatePriorities() {
        tickets.sort((t1, t2) -> Integer.compare(t1.getPriority(), t2.getPriority()));
        for (int i = 0; i < tickets.size(); i++) {
            tickets.get(i).setPriority(i + 1);
        }
    }
}
