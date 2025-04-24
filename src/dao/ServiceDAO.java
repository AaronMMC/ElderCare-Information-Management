package dao;

import model.Service;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {
    private Connection conn;

    public ServiceDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertService(Service service) throws SQLException {
        CallableStatement stmt = conn.prepareCall("{call insert_service(?, ?, ?)}");
        stmt.setString(1, service.getCategory());
        stmt.setString(2, service.getServiceName());
        stmt.setString(3, service.getDescription());
        stmt.executeUpdate();
    }

    public Service getServiceByID(int serviceID) throws SQLException {
        CallableStatement stmt = conn.prepareCall("{call get_service_by_id(?)}");
        stmt.setInt(1, serviceID);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return new Service(
                    rs.getInt("service_id"),
                    rs.getString("category"),
                    rs.getString("service_name"),
                    rs.getString("description")
            );
        }
        return null;
    }

    public List<Service> getAllServices() throws SQLException {
        CallableStatement stmt = conn.prepareCall("{call get_all_services()}");
        ResultSet rs = stmt.executeQuery();
        List<Service> services = new ArrayList<>();
        while (rs.next()) {
            services.add(new Service(
                    rs.getInt("service_id"),
                    rs.getString("category"),
                    rs.getString("service_name"),
                    rs.getString("description")
            ));
        }
        return services;
    }

    public void updateService(Service service) throws SQLException {
        CallableStatement stmt = conn.prepareCall("{call update_service(?, ?, ?, ?)}");
        stmt.setInt(1, service.getServiceID());
        stmt.setString(2, service.getCategory());
        stmt.setString(3, service.getServiceName());
        stmt.setString(4, service.getDescription());
        stmt.executeUpdate();
    }

    public void deleteService(int serviceID) throws SQLException {
        CallableStatement stmt = conn.prepareCall("{call delete_service(?)}");
        stmt.setInt(1, serviceID);
        stmt.executeUpdate();
    }
}