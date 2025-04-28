package model;

public class GuardianElder {
    private int guardianID;
    private int elderID;
    private String relationshipType;

    public GuardianElder(int guardianID, int elderID, String relationshipType) {
        this.guardianID = guardianID;
        this.elderID = elderID;
        this.relationshipType = relationshipType;
    }

    public int getGuardianID() {
        return guardianID;
    }

    public void setGuardianID(int guardianID) {
        this.guardianID = guardianID;
    }

    public int getElderID() {
        return elderID;
    }

    public void setElderID(int elderID) {
        this.elderID = elderID;
    }

    public String getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
    }
}