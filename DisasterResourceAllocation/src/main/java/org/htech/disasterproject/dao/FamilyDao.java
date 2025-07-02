package org.htech.disasterproject.dao;

import org.htech.disasterproject.database.DBConnection;
import org.htech.disasterproject.modal.Family;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FamilyDao {


    public int addFamily(Family family) {
        String sql = "INSERT INTO families (family_head_name, family_size, address, notes, barangay_id,family_image) VALUES (?, ?, ?, ?, ?, ?)";
        int generatedId = -1;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, family.getFamilyHeadName());
            pstmt.setInt(2, family.getFamilySize());
            pstmt.setString(3, family.getAddress());
            pstmt.setString(4, family.getNotes());
            pstmt.setInt(5, family.getBarangayId());
            if(family.getImageBytes()!=null){
                ByteArrayInputStream bais = new ByteArrayInputStream(family.getImageBytes());
            pstmt.setBinaryStream(6,bais);
            }else{
                pstmt.setNull(6,java.sql.Types.BLOB);
            }

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedId = generatedKeys.getInt(1);
                        family.setId(generatedId);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error adding family: " + e.getMessage());
            e.printStackTrace();
        }
        return generatedId;
    }

    public List<Family> getAllFamiliesWithBarangay() {
        List<Family> families = new ArrayList<>();
        String sql = "SELECT f.id, f.family_head_name, f.family_size, f.barangay_id, f.notes , f.family_image, b.name AS barangay_name " +
                "FROM families f " +
                "JOIN barangays b ON f.barangay_id = b.id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Family family = new Family();
                family.setId(rs.getInt("id"));
                family.setFamilyHeadName(rs.getString("family_head_name"));
                family.setFamilySize(rs.getInt("family_size"));
                family.setBarangayId(rs.getInt("barangay_id"));
                family.setBarangayName(rs.getString("barangay_name"));
                family.setNotes(rs.getString("notes"));
                family.setImageBytes(rs.getBytes("family_image"));
                families.add(family);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return families;
    }


    public Family getFamilyById(int familyId) {
        String sql = "SELECT * FROM families WHERE id = ?";
        Family family = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, familyId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    family = mapResultSetToFamily(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching family by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return family;
    }


    public List<Family> getAllFamiliesByBarangay(int barangayId) {
        List<Family> families = new ArrayList<>();
        String sql = "SELECT * FROM families WHERE barangay_id = ? ORDER BY family_head_name";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, barangayId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    families.add(mapResultSetToFamily(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching families by barangay: " + e.getMessage());
            e.printStackTrace();
        }
        return families;
    }


    public List<Family> getAllFamilies() {
        List<Family> families = new ArrayList<>();
        String sql = "SELECT * FROM families ORDER BY id";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                families.add(mapResultSetToFamily(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching all families: " + e.getMessage());
            e.printStackTrace();
        }
        return families;
    }


    public boolean updateFamily(Family family) {
        String sql = "UPDATE families SET family_head_name = ?, family_size = ?, address = ?, notes = ?, barangay_id = ?, family_image = ? WHERE id = ?";
        boolean isUpdated = false;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, family.getFamilyHeadName());
            pstmt.setInt(2, family.getFamilySize());
            pstmt.setString(3, family.getAddress());
            pstmt.setString(4, family.getNotes());
            pstmt.setInt(5, family.getBarangayId());

            if (family.getImageBytes() != null) {
                pstmt.setBytes(6, family.getImageBytes());
            } else {
                pstmt.setNull(6, java.sql.Types.BLOB);
            }

            pstmt.setInt(7, family.getId());

            int affectedRows = pstmt.executeUpdate();
            isUpdated = (affectedRows > 0);

        } catch (SQLException e) {
            System.out.println("Error updating family: " + e.getMessage());
            e.printStackTrace();
        }
        return isUpdated;
    }


    public boolean deleteFamily(int familyId) {
        String sql = "DELETE FROM families WHERE id = ?";
        boolean isDeleted = false;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, familyId);
            int affectedRows = pstmt.executeUpdate();
            isDeleted = (affectedRows > 0);

        } catch (SQLException e) {
            System.out.println("Error deleting family: " + e.getMessage());
            e.printStackTrace();
        }
        return isDeleted;
    }


    private Family mapResultSetToFamily(ResultSet rs) throws SQLException {
        Family family = new Family();
        family.setId(rs.getInt("id"));
        family.setFamilyHeadName(rs.getString("family_head_name"));
        family.setFamilySize(rs.getInt("family_size"));
        family.setAddress(rs.getString("address"));
        family.setNotes(rs.getString("notes"));
        family.setBarangayId(rs.getInt("barangay_id"));
        family.setImageBytes(rs.getBytes("family_image"));
        return family;
    }
}