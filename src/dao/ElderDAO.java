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

    public void insertElder(Elder elder) throws SQLException {
        String sql = "{CALL InsertElder(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, elder.getFirstName());
            stmt.setString(2, elder.getLastName());
            stmt.setTimestamp(3, Timestamp.valueOf(elder.getDateOfBirth()));
            stmt.setString(4, elder.getContactNumber());
            stmt.setString(5, elder.getEmail());
            stmt.setString(6, elder.getAddress());
            stmt.setInt(7, elder.getGuardianId());
            stmt.setString(8, elder.getRelationship());
            stmt.execute();

            int generatedId = stmt.getInt(9); // Retrieve the generated ID
            elder.setElderID(generatedId); // Set the elderID in the Elder object
        } catch (SQLException e) {
            throw e;
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
                    Elder elder = mapResultSetToElder(rs);
                    elders.add(elder);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return elders;
    }

    public void updateElder(Elder elder) {
        String sql = "{CALL UpdateElder(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, elder.getElderID());
            stmt.setString(2, elder.getFirstName());
            stmt.setString(3, elder.getLastName());
            stmt.setTimestamp(4, Timestamp.valueOf(elder.getDateOfBirth()));
            stmt.setString(5, elder.getContactNumber());
            stmt.setString(6, elder.getEmail());
            stmt.setString(7, elder.getAddress());
            stmt.setInt(8, elder.getGuardianId());
            stmt.setString(9, elder.getRelationship());
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
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getTimestamp("dateOfBirth").toLocalDateTime(),
                rs.getString("contactNumber"),
                rs.getString("email"),
                rs.getString("address"),
                rs.getInt("guardian_id"),
                rs.getString("relationshipToGuardian")
        );
    }

    public List<Elder> retrieveEldersWithGuardianId(int guardianId) {
        List<Elder> elders = new ArrayList<>();
        String sql = "{CALL RetrieveAllEldersWithGuardianId(?)}";
        try (CallableStatement statement = conn.prepareCall(sql)) {
            statement.setInt(1, guardianId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Elder elder = mapResultSetToElder(resultSet);
                    elders.add(elder);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving elders with guardianId: " + guardianId, e);
        }
        return elders;
    }

    public String getRelationshipByGuardianId(int guardianID) {
        String relationship = null; //
        String sql = "SELECT relationshipToGuardian FROM elder WHERE guardian_id = ? LIMIT 1"; // Assuming only one relationship per guardian for now.
        // this is not a stored procedure but rather, a straight query from the code
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, guardianID);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                    relationship = resultSet.getString("relationshipToGuardian");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return relationship; // Will return null if no relationship found for the given guardianID.
    }

}