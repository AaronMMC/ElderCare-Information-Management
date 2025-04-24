package model;

public class AppointmentEntry {
    private final String guardianName;
    private final int eldersCount;
    private final String appointmentTime;

    public AppointmentEntry(String guardianName, int eldersCount, String appointmentTime) {
        this.guardianName = guardianName;
        this.eldersCount = eldersCount;
        this.appointmentTime = appointmentTime;
    }

    public String getGuardianName() { return guardianName; }
    public int getEldersCount() { return eldersCount; }
    public String getAppointmentTime() { return appointmentTime; }
}

