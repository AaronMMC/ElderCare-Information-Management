package dao;

import model.Admin;
import model.Caregiver;
import model.Guardian;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuardianDAO {

    private final Connection conn;

    public GuardianDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Guardian> getAllGuardians() {
        String sql = "{CALL GetAllGuardians()}";
        List<Guardian> guardians = new ArrayList<>();
        try (CallableStatement stmt = conn.prepareCall(sql)){
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                guardians.add(mapResultSetToGuardian(rs));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return guardians;
    }

    public void insertGuardian(Guardian guardian) {
        String sql = "{CALL InsertGuardian(?, ?, ?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, guardian.getUsername());
            stmt.setString(2, guardian.getPassword());
            stmt.setString(3, guardian.getFirstName());
            stmt.setString(4, guardian.getLastName());
            stmt.setString(5, guardian.getContactNumber());
            stmt.setString(6, guardian.getEmail());
            stmt.setString(7, guardian.getAddress());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to insert guardian.");
        }
    }

    public Guardian getGuardianById(int id) {
        String sql = "{CALL GetGuardianById(?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToGuardian(rs);
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

    public Guardian findByUsername(String username) {
        String sql = "{CALL FindGuardianByUsername(?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, username);

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

    public Guardian findByUsernameAndPassword(String username, String password) {
        String sql = "{CALL FindGuardianByUsernameAndPassword(?,?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)){

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToGuardian(rs);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
