package com.skrb7f16.trashtocash.models;

public class RequestProduct {
    String requestId;
    String productId;
    String requesterId;
    String requestedToId;
    String requesterUsername;
    String requestedToUsername;
    String message;
    String requestedAt;
    boolean accepted;
    String whatsappNo;


    public String getWhatsappNo() {
        return whatsappNo;
    }

    public void setWhatsappNo(String whatsappNo) {
        this.whatsappNo = whatsappNo;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(String requestedAt) {
        this.requestedAt = requestedAt;
    }



    public RequestProduct(String productId, String requesterId, String requestedToId, String requesterUsername, String requestedToUsername, String message) {
        this.productId = productId;
        this.requesterId = requesterId;
        this.requestedToId = requestedToId;
        this.requesterUsername = requesterUsername;
        this.requestedToUsername = requestedToUsername;
        this.message = message;
    }

    public RequestProduct() {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(String requesterId) {
        this.requesterId = requesterId;
    }

    public String getRequestedToId() {
        return requestedToId;
    }

    public void setRequestedToId(String requestedToId) {
        this.requestedToId = requestedToId;
    }

    public String getRequesterUsername() {
        return requesterUsername;
    }

    public void setRequesterUsername(String requesterUsername) {
        this.requesterUsername = requesterUsername;
    }

    public String getRequestedToUsername() {
        return requestedToUsername;
    }

    public void setRequestedToUsername(String requestedToUsername) {
        this.requestedToUsername = requestedToUsername;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
