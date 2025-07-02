package org.htech.disasterproject.dao;


import org.htech.disasterproject.database.DBConnection;
import org.htech.disasterproject.modal.Resource;

import java.io.ByteArrayInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceDao {

    public List<Resource> getAllResources() {
        List<Resource> resources = new ArrayList<>();
        String sql = "SELECT * FROM resources";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Resource resource = new Resource(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("unit_type"),
                    rs.getDouble("weight_kg"),
                    rs.getInt("importance_score"),
                    rs.getInt("total_quantity"),
                    rs.getBytes("resource_image")
                );
                resources.add(resource);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resources;
    }
    
    public void updateResourceQuantity(int resourceId, int quantityToDecrement) {
        String sql = "UPDATE resources SET total_quantity = total_quantity - ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quantityToDecrement);
            pstmt.setInt(2, resourceId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean add(Resource resource) {
        String sql = "INSERT INTO resources (name, unit_type, weight_kg, importance_score, total_quantity,resource_image) VALUES (?, ?, ?, ?, ? ,?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, resource.getName());
            stmt.setString(2, resource.getUnitType());
            stmt.setDouble(3, resource.getWeightKg());
            stmt.setInt(4, resource.getImportanceScore());
            stmt.setInt(5, resource.getTotalQuantity());
            if(resource.getImageBytes()!=null){
                ByteArrayInputStream bais = new ByteArrayInputStream(resource.getImageBytes());
                stmt.setBinaryStream(6,bais);
            }else{
                stmt.setNull(6,java.sql.Types.BLOB);
            }
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public void update(int id, String name, String unitType, double weightKg, int importanceScore, int totalQuantity) throws SQLException {
        String sql = "UPDATE resources SET name=?, unit_type=?, weight_kg=?, importance_score=?, total_quantity=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, unitType);
            stmt.setDouble(3, weightKg);
            stmt.setInt(4, importanceScore);
            stmt.setInt(5, totalQuantity);
            stmt.setInt(6, id);
            stmt.executeUpdate();
        }
    }

    public void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM resources WHERE id=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Map<String, Object> getById(int id) throws SQLException {
        String sql = "SELECT *, (importance_score / weight_kg) AS value_per_weight FROM resources WHERE id=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("id", rs.getInt("id"));
                row.put("name", rs.getString("name"));
                row.put("unit_type", rs.getString("unit_type"));
                row.put("weight_kg", rs.getDouble("weight_kg"));
                row.put("importance_score", rs.getInt("importance_score"));
                row.put("total_quantity", rs.getInt("total_quantity"));
                row.put("value_per_weight", rs.getDouble("value_per_weight"));
                return row;
            }
        }
        return null;
    }

    public List<Map<String, Object>> getAll() throws SQLException {
        String sql = "SELECT *, (importance_score / weight_kg) AS value_per_weight FROM resources";
        List<Map<String, Object>> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("id", rs.getInt("id"));
                row.put("name", rs.getString("name"));
                row.put("unit_type", rs.getString("unit_type"));
                row.put("weight_kg", rs.getDouble("weight_kg"));
                row.put("importance_score", rs.getInt("importance_score"));
                row.put("total_quantity", rs.getInt("total_quantity"));
                row.put("value_per_weight", rs.getDouble("value_per_weight"));
                list.add(row);
            }
        }
        return list;
    }

    public boolean updateResource(Resource resource) {
        String sql = "UPDATE resources SET name = ?, unit_type = ?, weight_kg = ?, importance_score = ?, total_quantity = ?, resource_image = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, resource.getName());
            pstmt.setString(2, resource.getUnitType());
            pstmt.setDouble(3, resource.getWeightKg());
            pstmt.setInt(4, resource.getImportanceScore());
            pstmt.setInt(5, resource.getTotalQuantity());

            if (resource.getImageBytes() != null) {
                pstmt.setBytes(6, resource.getImageBytes());
            } else {
                pstmt.setNull(6, java.sql.Types.BLOB);
            }

            pstmt.setInt(7, resource.getId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error updating resource: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteResource(int resourceId) {
        String sql = "DELETE FROM resources WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, resourceId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting resource: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}