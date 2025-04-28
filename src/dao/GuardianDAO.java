package dao;

import model.Guardian;

import java.sql.*;

public class GuardianDAO {

    private final Connection conn;

    public GuardianDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertGuardian(Guardian guardian) {
        String sql = "{CALL InsertGuardian(?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, guardian.getFirstName());
            stmt.setString(2, guardian.getLastName());
            stmt.setString(3, guardian.getContactNumber());
            stmt.setString(4, guardian.getEmail());
            stmt.setString(5, guardian.getAddress());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Guardian getGuardianById(int id) {
        String sql = "{CALL GetGuardianById(?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Guardian guardian = new Guardian();
                guardian.setGuardianID(rs.getInt("guardian_id"));
                guardian.setFirstName(rs.getString("first_name"));
                guardian.setLastName(rs.getString("last_name"));
                guardian.setContactNumber(rs.getString("contact_number"));
                guardian.setEmail(rs.getString("email"));
                guardian.setAddress(rs.getString("address"));
                return guardian;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateGuardian(Guardian guardian) {
        String sql = "{CALL UpdateGuardian(?, ?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, guardian.getGuardianID());
            stmt.setString(2, guardian.getFirstName());
            stmt.setString(3, guardian.getLastName());
            stmt.setString(4, guardian.getContactNumber());
            stmt.setString(5, guardian.getEmail());
            stmt.setString(6, guardian.getAddress());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteGuardian(int id) {
        String sql = "{CALL DeleteGuardian(?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}