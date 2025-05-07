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
        try (CallableStatement stmt = conn.prepareCall(sql)){
            stmt.setInt(1, guardianID);
            stmt.setInt(2, elderID);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                return new GuardianElder(guardianID, elderID, rs.getString("relationshipType"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateGuardianElderRelationship(GuardianElder guardianElder) {
        String sql = "{CALL UpdateGuardianElderRelationship(?, ?, ?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)){
            stmt.setInt(1, guardianElder.getGuardianID());
            stmt.setInt(2, guardianElder.getElderID());
            stmt.setString(3, guardianElder.getRelationshipType());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Elder> getAllEldersByGuardianId(int guardianId) {
        List<Elder> elders = new ArrayList<>();
        String sql = "{CALL GetAllEldersByGuardianId(?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, guardianId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    elders.add(mapResultSetToElder(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return elders;
    }

    public Guardian getGuardianByElderId(int elderID) {
        String sql = "{CALL getGuardianByElderId(?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)){
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
        return new Guardian(
                rs.getInt("guardian_id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("contact_number"),
                rs.getString("email"),
                rs.getString("address")
        );
    }

    private Elder mapResultSetToElder(ResultSet rs) throws SQLException {
        return new Elder(
                rs.getInt("elder_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getTimestamp("date_of_birth").toLocalDateTime(),
                rs.getString("contact_number"),
                rs.getString("email"),
                rs.getString("address")
        );
    }
}