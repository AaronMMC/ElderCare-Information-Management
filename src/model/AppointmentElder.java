package model;

public class AppointmentElder {

    private int elderID;
    private int appointmentID;

    public AppointmentElder(int elderID, int appointmentID) {
        this.elderID = elderID;
        this.appointmentID = appointmentID;
    }

    public int getElderID() {
        return elderID;
    }

    public void setElderID(int elderID) {
        this.elderID = elderID;
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }
}
