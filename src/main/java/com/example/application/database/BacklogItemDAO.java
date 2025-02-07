package com.example.application.database;

import com.example.application.data.BacklogItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BacklogItemDAO {


    public void addBacklogItem(BacklogItem item) {
        String sql = "INSERT INTO backlog (description, priority) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item.getDescription());
            stmt.setString(2, Integer.toString(item.getPriority()));
            stmt.executeUpdate();
            System.out.println("Backlog-Item wurde hinzugefügt.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<BacklogItem> getAllBacklogItems() {
        List<BacklogItem> items = new ArrayList<>();
        String sql = "SELECT * FROM backlog";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                BacklogItem item = new BacklogItem();
                item.setId(rs.getInt("id"));
                item.setDescription(rs.getString("description"));
                item.setPriority(Integer.parseInt(rs.getString("priority")));
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public void updateBacklogItem(BacklogItem item) {
        String sql = "UPDATE backlog SET description = ?, priority = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, item.getDescription());
            stmt.setString(2, Integer.toString(item.getPriority()));
            stmt.setInt(3, item.getId());
            stmt.executeUpdate();
            System.out.println("Backlog-Item wurde aktualisiert.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBacklogItem(int id) {
        String sql = "DELETE FROM backlog WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Backlog-Item wurde gelöscht.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
