package com.example.application.views.ticketlist;

import com.example.application.data.BacklogItem;
import com.example.application.database.BacklogItemDAO;
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

import java.util.List;

@PageTitle("Product Backlog List")
@Route("")
@Menu(order = 0, icon = LineAwesomeIconUrl.PENCIL_RULER_SOLID)
public class TicketListView extends VerticalLayout {

    // DAO zur Interaktion mit der Datenbank über BacklogItems
    private final BacklogItemDAO backlogItemDAO = new BacklogItemDAO();
    // BacklogItems werden aus der Datenbank geladen
    private List<BacklogItem> backlogItems = backlogItemDAO.getAllBacklogItems();
    private final Grid<BacklogItem> grid = new Grid<>(BacklogItem.class);
    private BacklogItem draggedItem;

    public TicketListView() {
        configureGrid();
        configureDragAndDrop();

        Button addItemButton = new Button("Create Item", e -> openAddBacklogItemDialog());
        add(addItemButton, grid);
    }

    private void deleteBacklogItem(BacklogItem item) {
        backlogItemDAO.deleteBacklogItem(item.getId());
        refreshBacklogItems();
    }

    private void configureGrid() {
        grid.removeAllColumns();

        grid.addColumn(BacklogItem::getPriority).setHeader("Priority").setAutoWidth(true);
        grid.addColumn(BacklogItem::getItem).setHeader("Title").setAutoWidth(true);
        grid.addColumn(BacklogItem::getDescription).setHeader("Description").setAutoWidth(true);
        grid.addColumn(BacklogItem::getSprint).setHeader("Sprint").setAutoWidth(true);

        grid.addComponentColumn(item -> {
            Button deleteButton = new Button("Delete", e -> deleteBacklogItem(item));
            Button editButton = new Button("Edit", e -> openEditDialog(item));

            HorizontalLayout actionsLayout = new HorizontalLayout(editButton, deleteButton);
            actionsLayout.setSpacing(true);
            return actionsLayout;
        }).setHeader("Actions");

        grid.setItems(backlogItems);
    }

    private void configureDragAndDrop() {
        grid.setRowsDraggable(true);
        grid.setDropMode(GridDropMode.ON_TOP);

        grid.addDragStartListener(event -> draggedItem = event.getDraggedItems().get(0));

        grid.addDropListener(event -> {
            BacklogItem targetItem = event.getDropTargetItem().orElse(null);
            GridDropLocation dropLocation = event.getDropLocation();

            if (draggedItem != null && targetItem != null && draggedItem != targetItem) {
                backlogItems.remove(draggedItem);
                int targetIndex = backlogItems.indexOf(targetItem) + (dropLocation == GridDropLocation.BELOW ? 1 : 0);
                backlogItems.add(targetIndex, draggedItem);
                recalculatePriorities();
                grid.setItems(backlogItems);
                backlogItems.forEach(backlogItemDAO::updateBacklogItem);
            }
            draggedItem = null;
        });

        grid.addDragEndListener(event -> draggedItem = null);
    }

    private void openEditDialog(BacklogItem item) {
        Dialog editDialog = new Dialog();

        TextField priorityField = new TextField("Priority", String.valueOf(item.getPriority()));
        TextField titleField = new TextField("Title", item.getItem());
        TextArea descriptionField = new TextArea("Description", item.getDescription());
        TextField sprintField = new TextField("Sprint", item.getSprint());

        FormLayout formLayout = new FormLayout();
        formLayout.add(priorityField, titleField, descriptionField, sprintField);

        Button saveButton = new Button("Save", e -> {
            try {
                int newPriority = Integer.parseInt(priorityField.getValue());
                item.setPriority(newPriority);
                item.setItem(titleField.getValue());
                item.setDescription(descriptionField.getValue());
                item.setSprint(sprintField.getValue());

                recalculatePriorities();
                backlogItemDAO.updateBacklogItem(item);
                refreshBacklogItems();
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

    private void openAddBacklogItemDialog() {
        Dialog dialog = new Dialog();

        TextField titleField = new TextField("Title");
        TextArea descriptionField = new TextArea("Description");
        TextField priorityField = new TextField("Priority");
        TextField sprintField = new TextField("Sprint");

        int nextPriority = backlogItems.stream()
                .mapToInt(BacklogItem::getPriority)
                .max().orElse(0) + 1;
        priorityField.setValue(String.valueOf(nextPriority));

        FormLayout formLayout = new FormLayout();
        formLayout.add(titleField, descriptionField, priorityField, sprintField);

        Button saveButton = new Button("Save", e -> {
            try {
                int priority = Integer.parseInt(priorityField.getValue());
                String title = titleField.getValue();
                String description = descriptionField.getValue();
                String sprint = sprintField.getValue();

                BacklogItem newItem = new BacklogItem(priority, title, description, sprint);
                backlogItemDAO.addBacklogItem(newItem);
                refreshBacklogItems();
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
        backlogItems.sort((i1, i2) -> Integer.compare(i1.getPriority(), i2.getPriority()));
        for (int i = 0; i < backlogItems.size(); i++) {
            backlogItems.get(i).setPriority(i + 1);
        }
    }

    private void refreshBacklogItems() {
        backlogItems = backlogItemDAO.getAllBacklogItems();
        grid.setItems(backlogItems);
    }
}
