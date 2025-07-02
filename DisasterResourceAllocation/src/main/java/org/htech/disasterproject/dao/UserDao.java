package org.htech.disasterproject.dao;


import org.htech.disasterproject.database.DBConnection;
import org.htech.disasterproject.modal.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDao {

    public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String passwordFromDb = rs.getString("password");
                if (password.equals(passwordFromDb)) {
                    return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                            passwordFromDb,
                        User.Role.valueOf(rs.getString("role")),
                        rs.getInt("barangay_id")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void add(String username, String password, String role, Integer barangayId) {
        String sql = "INSERT INTO users (username, password, role, barangay_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);
            if (barangayId != null)
                stmt.setInt(4, barangayId);
            else
                stmt.setNull(4, Types.INTEGER);
            stmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void update(int id, String username, String password, String fullName, String role, Integer barangayId) throws SQLException {
        String sql = "UPDATE users SET username=?, password=?, full_name=?, role=?, barangay_id=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, fullName);
            stmt.setString(4, role);
            if (barangayId != null)
                stmt.setInt(5, barangayId);
            else
                stmt.setNull(5, Types.INTEGER);
            stmt.setInt(6, id);
            stmt.executeUpdate();
        }
    }

    public void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Map<String, Object> getById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("id", rs.getInt("id"));
                row.put("username", rs.getString("username"));
                row.put("password", rs.getString("password"));
                row.put("role", rs.getString("role"));
                row.put("barangay_id", rs.getInt("barangay_id"));
                row.put("created_at", rs.getTimestamp("created_at"));
                return row;
            }
        }
        return null;
    }

    public List<Map<String, Object>> getAll() throws SQLException {
        String sql = "SELECT * FROM users";
        List<Map<String, Object>> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("id", rs.getInt("id"));
                row.put("username", rs.getString("username"));
                row.put("password", rs.getString("password"));
                row.put("role", rs.getString("role"));
                row.put("barangay_id", rs.getInt("barangay_id"));
                row.put("created_at", rs.getTimestamp("created_at"));
                list.add(row);
            }
        }
        return list;
    }
}