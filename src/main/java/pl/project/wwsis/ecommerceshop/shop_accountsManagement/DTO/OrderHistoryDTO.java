package pl.project.wwsis.ecommerceshop.shop_accountsManagement.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class OrderHistoryDTO {
    private String order_tracking_number;
    private int quantity;
    private String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss", timezone = "Europe/Warsaw")
    private Date date_created;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss", timezone = "Europe/Warsaw")
    private Date date_updated;
    private String country;
    private String zipCode;
    private String City;
    private String State;
    private String Street;



    public OrderHistoryDTO(String order_tracking_number, int quantity, String status, Date date_created, Date date_updated, String country, String zipCode, String city, String state, String street) {
        this.order_tracking_number = order_tracking_number;
        this.quantity = quantity;
        this.status = status;
        this.date_created = date_created;
        this.date_updated = date_updated;
        this.country = country;
        this.zipCode = zipCode;
        City = city;
        State = state;
        Street = street;
    }

    public String getOrder_tracking_number() {
        return order_tracking_number;
    }

    public void setOrder_tracking_number(String order_tracking_number) {
        this.order_tracking_number = order_tracking_number;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public Date getDate_updated() {
        return date_updated;
    }

    public void setDate_updated(Date date_updated) {
        this.date_updated = date_updated;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }
}
