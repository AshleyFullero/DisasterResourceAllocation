package org.htech.disasterproject.dao;

import org.htech.disasterproject.database.DBConnection;
import org.htech.disasterproject.modal.Allocation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class AllocationDao {

    public void saveAllocations(List<Allocation> allocations) {
        String sql = "INSERT INTO allocations (distribution_event_id, family_id, resource_id, quantity_allocated) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                for (Allocation alloc : allocations) {
                    pstmt.setInt(1, alloc.getDistributionEventId());
                    pstmt.setInt(2, alloc.getFamilyId());
                    pstmt.setInt(3, alloc.getResourceId());
                    pstmt.setInt(4, alloc.getQuantityAllocated());
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }
            
            conn.commit();
            
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}