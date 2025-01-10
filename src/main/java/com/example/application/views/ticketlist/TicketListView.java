package com.example.application.views.ticketlist;

import com.example.application.data.Ticket;
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

import org.vaadin.lineawesome.LineAwesomeIconUrl;

//import java.awt.TextArea;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@PageTitle("TicketList")
@Route("")
@Menu(order = 0, icon = LineAwesomeIconUrl.PENCIL_RULER_SOLID)
public class TicketListView extends VerticalLayout {

    private final List<Ticket> tickets = new ArrayList<>();
    private final Grid<Ticket> grid = new Grid<>(Ticket.class);

    public TicketListView() {
    	 //loadTicketsFromDatabase(); // Tickets aus der Datenbank laden
        // Beispiel-Daten
        tickets.add(new Ticket(1, "", "", "", ""));
        tickets.add(new Ticket(2, " ", " ", " ", ""));
        tickets.add(new Ticket(3, "", "", "", ""));

        // Grid erstellen
        configureGrid();

        // Button erstellen
        Button addTicketButton = new Button("Ticket erstellen", e -> openAddTicketDialog());

        // Button und Grid hinzufügen
        add(addTicketButton, grid);
    }

    private Ticket draggedTicket = null; // Temporär gezogenes Ticket

    /*private void recalculatePriorities() {
        for (int i = 0; i < tickets.size(); i++) {
            tickets.get(i).setPrioritaet(i + 1); // Setze die Prioritäten von 1 bis n
        }
    }*/

    
    //private Ticket draggedTicket = null; // Temporär gespeichertes gezogenes Ticket
    
   /* private void recalculatePriorities() {
        for (int i = 0; i < tickets.size(); i++) {
            tickets.get(i).setPrioritaet(i + 1); // Setze die Prioritäten von 1 bis n
        }
    }*/

/*
    private void configureGrid() {
        grid.removeAllColumns(); // Automatisch generierte Spalten entfernen
        grid.addColumn(Ticket::getPrioritaet).setHeader("Priorität");
        grid.addColumn(Ticket::getItem).setHeader("Item");
        grid.addColumn(Ticket::getBeschreibung).setHeader("Beschreibung");
        grid.addColumn(Ticket::getZeitschaetzung).setHeader("Zeitschätzung");
        grid.setItems(tickets);

        // Drag-and-Drop aktivieren
        grid.setRowsDraggable(true);
        grid.setDropMode(GridDropMode.BETWEEN); // Tickets können zwischen anderen abgelegt werden

        // Drag-Start-Listener
        grid.addDragStartListener(event -> {
            draggedTicket = event.getDraggedItems().get(0); // Ziehendes Ticket speichern
        });

        // Drop-Listener
        grid.addDropListener(event -> {
            if (draggedTicket == null) return; // Kein Ticket zum Ziehen vorhanden

            Ticket targetTicket = event.getDropTargetItem().orElse(null); // Ziel-Ticket ermitteln

            if (targetTicket != null && draggedTicket != null) {
                int oldIndex = tickets.indexOf(draggedTicket);
                int newIndex = tickets.indexOf(targetTicket);

                // Bei DropMode.BETWEEN: Platzierung oberhalb oder unterhalb des Ziels
                GridDropLocation dropLocation = event.getDropLocation();
                if (dropLocation == GridDropLocation.BELOW) {
                    newIndex++; // Ziel verschiebt sich um eins nach unten
                }

                // Positionen aktualisieren
                tickets.remove(oldIndex);
                tickets.add(newIndex, draggedTicket);

                // Prioritäten neu berechnen
                recalculatePriorities();

                // Grid-Daten aktualisieren
                grid.setItems(tickets);

                // Temporäre Referenz zurücksetzen
                draggedTicket = null;
            }
        });*/

        // Drag-End-Listener: Ticket zurücksetzen, falls Drag abgebrochen wird
  /*      grid.addDragEndListener(event -> {
            draggedTicket = null;
        });
    }*/
    private void deleteTicket(Ticket ticket) {
        tickets.remove(ticket);         // Ticket aus der Liste entfernen
       // deleteTicketFromDatabase(ticket); // Aus der Datenbank löschen
        recalculatePriorities();        // Prioritäten neu berechnen
        grid.setItems(tickets);         // Grid-Daten aktualisieren
    }

    
    private void configureGrid() {
        grid.removeAllColumns(); // Entferne automatisch generierte Spalten

        grid.addColumn(Ticket::getPrioritaet).setHeader("Priorität").setAutoWidth(true);
        grid.addColumn(Ticket::getItem).setHeader("Item").setAutoWidth(true);
        grid.addColumn(Ticket::getBeschreibung).setHeader("Beschreibung").setAutoWidth(true);
        grid.addColumn(Ticket::getZeitschaetzung).setHeader("Story Point").setAutoWidth(true);
        grid.addColumn(Ticket::getSprint).setHeader("Sprint").setAutoWidth(true);

        // Spalte für Aktionen
        grid.addComponentColumn(ticket -> {
            Button deleteButton = new Button("Löschen", e -> deleteTicket(ticket));
            Button editButton = new Button("Bearbeiten", e -> openEditDialog(ticket));

            HorizontalLayout actionsLayout = new HorizontalLayout(editButton, deleteButton);
            actionsLayout.setSpacing(true);

            return actionsLayout;
        }).setHeader("Aktionen");

        grid.setItems(tickets); // Initiale Daten setzen
    }

    private void openEditDialog(Ticket ticket) {
        Dialog editDialog = new Dialog();

        // Formularfelder
        TextField prioritaetField = new TextField("Priorität", String.valueOf(ticket.getPrioritaet()));
        TextField itemField = new TextField("Item", ticket.getItem());
        TextArea beschreibungField = new TextArea("Beschreibung", ticket.getBeschreibung()); // TextArea für mehrzeilige Texteingabe
        TextField zeitschaetzungField = new TextField("Storypoints", ticket.getZeitschaetzung());
        TextField sprintField = new TextField("Sprint", ticket.getSprint());
        
        
        // Layout für das Formular
        FormLayout formLayout = new FormLayout();
        formLayout.add(prioritaetField, itemField, beschreibungField, zeitschaetzungField, sprintField);

        // Speichern-Button
        Button saveButton = new Button("Speichern", e -> {
            ticket.setItem(itemField.getValue());
            ticket.setBeschreibung(beschreibungField.getValue());
            ticket.setZeitschaetzung(zeitschaetzungField.getValue());
            ticket.setSprint(sprintField.getValue());

          //  updateTicketInDatabase(ticket); // Datenbank aktualisieren
            grid.setItems(tickets);         // Grid-Daten aktualisieren
            editDialog.close();
        });


        // Abbrechen-Button
        Button cancelButton = new Button("Abbrechen", e -> editDialog.close());

        // Buttons und Layout hinzufügen
        HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, cancelButton);
        VerticalLayout dialogLayout = new VerticalLayout(formLayout, buttonsLayout);

        editDialog.add(dialogLayout);
        editDialog.open();
    }



    
    private void openAddTicketDialog() {
        Dialog dialog = new Dialog();

        // Formularfelder
        TextField itemField = new TextField("Item");
        TextArea beschreibungField = new TextArea("Beschreibung"); // TextArea für mehrzeiligen Text
        TextField prioritaetField = new TextField("Priorität");
        TextField zeitschaetzungField = new TextField("Storypoints");
        TextField sprintField = new TextField("Sprint");

        // Automatisch die nächste Priorität berechnen
        int nextPrioritaet = tickets.stream()
                                    .mapToInt(Ticket::getPrioritaet)
                                    .max()
                                    .orElse(0) + 1;

        prioritaetField.setValue(String.valueOf(nextPrioritaet));
        //prioritaetField.setReadOnly(true); // Priorität ist nur lesbar

        // Layout für das Formular
        FormLayout formLayout = new FormLayout();
        formLayout.add(itemField, beschreibungField, prioritaetField, zeitschaetzungField, sprintField);

        Button saveButton = new Button("Speichern", e -> {
            String item = itemField.getValue();
            String beschreibung = beschreibungField.getValue();
            String zeitschaetzung = zeitschaetzungField.getValue();
            String sprint = sprintField.getValue();

            Ticket newTicket = new Ticket(nextPrioritaet, item, beschreibung, zeitschaetzung, sprint);
            tickets.add(newTicket);
           // addTicketToDatabase(newTicket); // In die Datenbank speichern

            recalculatePriorities(); // Prioritäten sicherheitshalber neu berechnen
            grid.setItems(tickets);  // Grid-Daten aktualisieren

            dialog.close();
        });



        Button cancelButton = new Button("Abbrechen", e -> dialog.close());
        HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, cancelButton);

        // Dialog zusammenstellen
        VerticalLayout dialogLayout = new VerticalLayout(formLayout, buttonsLayout);
        dialog.add(dialogLayout);

        dialog.open();
    }


   private void recalculatePriorities() {
        // Tickets nach der Priorität sortieren und neu durchnummerieren
     //   tickets.sort(Comparator.comparingInt(Ticket::getPrioritaet));
        for (int i = 0; i < tickets.size(); i++) {
            tickets.get(i).setPrioritaet(i + 1);
        }
    }
   

}
