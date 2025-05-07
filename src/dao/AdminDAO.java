package dao;

import model.Admin;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {

    private final Connection conn;

    public AdminDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Admin> getAllAdmin(){
        String sql = "{CALL GetAllAdmin()}";
        List<Admin> admins = new ArrayList<>();
        try (CallableStatement stmt = conn.prepareCall(sql)){
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                admins.add(mapResultSetToAdmin(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admins;
    }

    public Admin findByUsername(String username) {
        String sql = "{CALL FindAdminByUsername(?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)){
            stmt.setString(1, username);

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
    public Admin findByUsernameAndPassword(String username, String password) {
        String sql = "{CALL FindAdminByUsernameAndPassword(?,?)}";
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
    public Admin getAdminById(int id) {
        String sql = "{CALL GetAdminById(?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)){
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToAdmin(rs);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    private Admin mapResultSetToAdmin(ResultSet rs) throws SQLException {
        return new Admin(
                rs.getString("username"),
                rs.getString("password")
        );
    }
}
