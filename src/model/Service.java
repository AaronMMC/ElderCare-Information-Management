package model;

public class Service {
    private int serviceID;
    private String category;
    private String serviceName;
    private String description;

    public Service(int serviceID, String category, String serviceName, String description) {
        this.serviceID = serviceID;
        this.category = category;
        this.serviceName = serviceName;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
