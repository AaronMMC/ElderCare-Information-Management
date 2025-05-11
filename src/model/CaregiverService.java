package model;

public class CaregiverService {
    private int caregiverId;
    private int serviceId;
    private int experienceYears;
    private double hourlyRate;

    public CaregiverService(int caregiverId, int serviceId, int experienceYears, double hourlyRate) {
        this.caregiverId = caregiverId;
        this.serviceId = serviceId;
        this.experienceYears = experienceYears;
        this.hourlyRate = hourlyRate;
    }

    public CaregiverService(int experienceYears, double hourlyRate, int serviceId) {
        this.experienceYears = experienceYears;
        this.hourlyRate = hourlyRate;
        this.serviceId = serviceId;
    }

    public CaregiverService(int serviceId, int caregiverId) {
        this.serviceId = serviceId;
        this.caregiverId = caregiverId;
    }

    public int getCaregiverId() { return caregiverId; }

    public void setCaregiverId(int caregiverId) { this.caregiverId = caregiverId; }

    public int getServiceId() { return serviceId; }

    public void setServiceId(int serviceId) { this.serviceId = serviceId; }

    public int getExperienceYears() { return experienceYears; }

    public void setExperienceYears(int experienceYears) { this.experienceYears = experienceYears; }

    public double getHourlyRate() { return hourlyRate; }

    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }
}
