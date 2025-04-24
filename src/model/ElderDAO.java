package model;

import util.Elder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ElderDAO {

    private final Connection conn;

    public ElderDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertElder(Elder elder) throws SQLException {
        String sql = "{CALL InsertElder(?, ?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, elder.getFirstName());
            stmt.setString(2, elder.getLastName());
            stmt.setTimestamp(3, Timestamp.valueOf(elder.getDateOfBirth()));
            stmt.setString(4, elder.getContactNumber());
            stmt.setString(5, elder.getEmail());
            stmt.setString(6, elder.getAddress());
            stmt.execute();
        }
    }

    public Elder getElderById(int id) throws SQLException {
        String sql = "{CALL GetElderById(?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToElder(rs);
                }
            }
        }
        return null;
    }

    public List<Elder> getAllElders() throws SQLException {
        List<Elder> elders = new ArrayList<>();
        String sql = "{CALL GetAllElders()}";
        try (CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                elders.add(mapResultSetToElder(rs));
            }
        }
        return elders;
    }

    public void updateElder(Elder elder) throws SQLException {
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
        }
    }

    public void deleteElder(int id) throws SQLException {
        String sql = "{CALL DeleteElder(?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, id);
            stmt.execute();
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