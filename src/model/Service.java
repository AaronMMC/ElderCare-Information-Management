package model;

public class Service {
    private int serviceID;
    private String category;
    private String serviceName;
    private int minimumHourDuration;

    public Service(int serviceID, String category, String serviceName, int minimumHourDuration) {
        this.serviceID = serviceID;
        this.category = category;
        this.serviceName = serviceName;
        this.minimumHourDuration = minimumHourDuration;
    }


    public Service(String category, String serviceName, int minimumHourDuration) {
        this.category = category;
        this.serviceName = serviceName;
        this.minimumHourDuration = minimumHourDuration;
    }


    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getMinimumHourDuration() {
        return minimumHourDuration;
    }

    public void setMinimumHourDuration(int minimumHourDuration) {
        this.minimumHourDuration = minimumHourDuration;
    }
}
