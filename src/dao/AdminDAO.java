package dao;

import model.Admin;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO {

    private final Connection conn;

    public AdminDAO(Connection conn) {
        this.conn = conn;
    }

    public Admin findByUsernameAndPassword(String username, String password) {
        String sql = "{CALL FindAdminByUsernameAndPassword(?, ?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)){
            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAdmin(rs);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    private Admin mapResultSetToAdmin(ResultSet rs) throws SQLException {
        return new Admin(
                rs.getInt("adminId"),
                rs.getString("username"),
                rs.getString("password")
        );
    }
}
