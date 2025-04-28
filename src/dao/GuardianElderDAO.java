package dao;

import model.Elder;
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

    public List<Elder> getEldersByGuardianId(int guardianId) {
        List<Elder> elders = new ArrayList<>();
        String sql = "{CALL GetEldersByGuardianId(?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, guardianId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Elder elder = new Elder(
                            rs.getInt("elder_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getTimestamp("date_of_birth").toLocalDateTime(),
                            rs.getString("contact_number"),
                            rs.getString("email"),
                            rs.getString("address")
                    );
                    elders.add(elder);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return elders;
    }
}