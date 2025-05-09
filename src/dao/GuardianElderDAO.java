package dao;

import model.Elder;
import model.Guardian;
import model.GuardianElder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuardianElderDAO {

    private final Connection conn;

    public GuardianElderDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertGuardianElderLink(GuardianElder guardianElder) {
        String sql = "{CALL InsertGuardianElderLink(?, ?, ?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, guardianElder.getGuardianID());
            stmt.setInt(2, guardianElder.getElderID());
            stmt.setString(3, guardianElder.getRelationshipType());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public GuardianElder getGuardianElderRelationshipByIds(int guardianID, int elderID) {
        String sql = "{CALL GetGuardianElderRelationshipByIds(?, ?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, guardianID);
            stmt.setInt(2, elderID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new GuardianElder(guardianID, elderID, rs.getString("relationshipType"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateGuardianElderRelationship(GuardianElder guardianElder) {
        String sql = "{CALL UpdateGuardianElderRelationship(?, ?, ?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, guardianElder.getGuardianID());
            stmt.setInt(2, guardianElder.getElderID());
            stmt.setString(3, guardianElder.getRelationshipType());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Elder> getAllEldersByGuardianId(int guardianIdParam) {
        List<Elder> elders = new ArrayList<>();
        String sql = "{CALL GetAllEldersByGuardianId(?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, guardianIdParam);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    elders.add(mapResultSetToElder(rs, guardianIdParam));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return elders;
    }

    public Guardian getGuardianByElderId(int elderID) {
        String sql = "{CALL getGuardianByElderId(?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, elderID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToGuardian(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Guardian mapResultSetToGuardian(ResultSet rs) throws SQLException {
        return new Guardian(rs.getInt("guardian_id"), rs.getString("username"), rs.getString("password"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("contact_number"), rs.getString("email"), rs.getString("address"));
    }


    private Elder mapResultSetToElder(ResultSet rs, int contextGuardianId) throws SQLException {
        int guardianIdFromRs = contextGuardianId;
        try {


            if (hasColumn(rs, "guardian_id")) {
                guardianIdFromRs = rs.getInt("guardian_id");
            }
        } catch (SQLException e) {
            System.err.println("Warning: 'guardian_id' column not found or invalid in ResultSet for elder " + rs.getInt("elder_id") + ". Using context guardianId " + contextGuardianId);
        }

        return new Elder(rs.getInt("elder_id"), rs.getString("first_name"), rs.getString("last_name"), rs.getTimestamp("date_of_birth").toLocalDateTime(), rs.getString("contact_number"), rs.getString("email"), rs.getString("address"), guardianIdFromRs);
    }


    private boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columns = rsmd.getColumnCount();
        for (int x = 1; x <= columns; x++) {
            if (columnName.equalsIgnoreCase(rsmd.getColumnName(x))) {
                return true;
            }
        }
        return false;
    }
}