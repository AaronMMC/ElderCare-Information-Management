package dao;

import model.Elder;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AppointmentElderDAO {

    private final Connection conn;

    public AppointmentElderDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Elder> getEldersByAppointmentId(int appointmentId) {
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
}
