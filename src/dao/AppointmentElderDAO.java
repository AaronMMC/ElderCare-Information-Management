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
}
