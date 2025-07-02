package org.htech.disasterproject.dao;


import org.htech.disasterproject.database.DBConnection;
import org.htech.disasterproject.modal.Barangay;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BarangayDao {

    public List<Barangay> getAllBarangays() {
        List<Barangay> barangays = new ArrayList<>();
        String sql = "SELECT * FROM barangays";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                barangays.add(new Barangay(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("location_details")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return barangays;
    }

    public int add(String name, String locationDetails) {
        String sql = "INSERT INTO barangays (name, location_details) VALUES (?, ?)";
        int generatedId = -1;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, name);
            stmt.setString(2, locationDetails);
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedId = generatedKeys.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return generatedId;
    }


    public void update(int id, String name, String locationDetails) throws SQLException {
        String sql = "UPDATE barangays SET name=?, location_details=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, locationDetails);
            stmt.setInt(3, id);
            stmt.executeUpdate();
        }
    }

    public void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM barangays WHERE id=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Map<String, Object> getById(int id) {
        String sql = "SELECT * FROM barangays WHERE id=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("id", rs.getInt("id"));
                row.put("name", rs.getString("name"));
                row.put("location_details", rs.getString("location_details"));
                row.put("created_at", rs.getTimestamp("created_at"));
                return row;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Map<String, Object>> getAll() throws SQLException {
        String sql = "SELECT * FROM barangays";
        List<Map<String, Object>> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("id", rs.getInt("id"));
                row.put("name", rs.getString("name"));
                row.put("location_details", rs.getString("location_details"));
                row.put("created_at", rs.getTimestamp("created_at"));
                list.add(row);
            }
        }
        return list;
    }

    public boolean existsByName(String name) {
        String sql = "SELECT COUNT(*) FROM barangays WHERE name = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}