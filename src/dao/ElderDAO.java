package dao;

import model.Elder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ElderDAO {

    private final Connection conn;

    public ElderDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertElder(Elder elder) {
        String sql = "{CALL InsertElder(?, ?, ?, ?, ?, ?,?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, elder.getFirstName());
            stmt.setString(2, elder.getLastName());
            stmt.setTimestamp(3, Timestamp.valueOf(elder.getDateOfBirth()));
            stmt.setString(4, elder.getContactNumber());
            stmt.setString(5, elder.getEmail());
            stmt.setString(6, elder.getAddress());
            stmt.setString(7, elder.getAddress());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Elder getElderById(int id) {
        String sql = "{CALL GetElderById(?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToElder(rs);
                }
            } catch (Exception _){}
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Elder> getAllElders() {
        List<Elder> elders = new ArrayList<>();
        String sql = "{CALL GetAllElders()}";
        try (CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                elders.add(mapResultSetToElder(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return elders;
    }

    public List<Elder> getAllEldersByAppointmentId(int appointmentId) {
        List<Elder> elders = new ArrayList<>();
        String sql = "{CALL GetEldersByAppointmentId(?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)){
            stmt.setInt(1, appointmentId);
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

    public void updateElder(Elder elder) {
        String sql = "{CALL UpdateElder(?, ?, ?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, elder.getElderID());
            stmt.setString(2, elder.getFirstName());
            stmt.setString(3, elder.getLastName());
            stmt.setTimestamp(4, Timestamp.valueOf(elder.getDateOfBirth()));
            stmt.setString(5, elder.getContactNumber());
            stmt.setString(6, elder.getEmail());
            stmt.setString(7, elder.getAddress());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteElder(int id) {
        String sql = "{CALL DeleteElder(?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, id);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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