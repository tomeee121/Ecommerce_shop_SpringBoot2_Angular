package pl.project.wwsis.ecommerceshop.DTO;

public class PurchaseResponse {

    private String orderTrackingNumber;


    public PurchaseResponse(String orderTrackingNumber) {
        this.orderTrackingNumber = orderTrackingNumber;
    }

    public String getOrderTrackingNumber() {
        return orderTrackingNumber;
    }
}
