package dao;

import model.Elder;
import model.Guardian;

import java.sql.Connection;
import java.util.List;

public class GuardianDAO {

    private final Connection conn;

    public GuardianDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertGuardian(Guardian guardian){

    };
    public Guardian getGuardianById(int id){
        return null;
    };
    public void updateGuardian(Guardian guardian){

    };
    public void deleteGuardian(int id){

    };
    public void insertGuardianElderLink(int guardianId, int elderId, String relationshipType){

    };
    public List<Elder> getEldersByGuardianId(int guardianId){
        return null;
    };

}
