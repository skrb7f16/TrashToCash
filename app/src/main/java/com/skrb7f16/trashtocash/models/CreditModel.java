package com.skrb7f16.trashtocash.models;

public class CreditModel {
    String product,at,credId;
    int credValue;
    String productName;
    boolean redeemed;

    public String getCredId() {

        return credId;
    }

    public void setCredId(String credId) {
        this.credId = credId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getAt() {
        return at;
    }

    public void setAt(String at) {
        this.at = at;
    }

    public int getCredValue() {
        return credValue;
    }

    public void setCredValue(int credValue) {
        this.credValue = credValue;
    }

    public boolean isRedeemed() {
        return redeemed;
    }

    public void setRedeemed(boolean redeemed) {
        this.redeemed = redeemed;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public CreditModel() {
    }

    public CreditModel(String product, String at, int credValue, String productName, boolean redeemed) {
        this.product = product;
        this.at = at;
        this.credValue = credValue;
        this.productName = productName;
        this.redeemed = redeemed;
    }
}
