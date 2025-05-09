package model;

public class Service {
    private int serviceID;
    private String category;
    private String serviceName;
    private double price;

    public Service(int serviceID, String category, String serviceName, double price) {
        this.serviceID = serviceID;
        this.category = category;
        this.serviceName = serviceName;
        this.price = price;
    }

    public Service() {
        this.serviceID = -1;
        this.category = "";
        this.serviceName = "";
        this.price = 0;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
