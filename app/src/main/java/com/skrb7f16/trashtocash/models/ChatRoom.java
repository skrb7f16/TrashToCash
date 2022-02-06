package com.skrb7f16.trashtocash.models;

import java.util.ArrayList;

public class ChatRoom {
    String id;
    ArrayList<MessageModels>messages;
    String productId;
    String productName;
    String recieverName;
    String recieverId;
    public ChatRoom() {

    }

    public ChatRoom(String id, ArrayList<MessageModels> messages, String productId, String productName, String recieverName, String recieverId) {
        this.id = id;
        this.messages = messages;
        this.productId = productId;
        this.productName = productName;
        this.recieverName = recieverName;
        this.recieverId = recieverId;
    }

    public ChatRoom(String id, String productId, String productName, String recieverName, String recieverId) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.recieverName = recieverName;
        this.recieverId = recieverId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<MessageModels> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<MessageModels> messages) {
        this.messages = messages;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getRecieverName() {
        return recieverName;
    }

    public void setRecieverName(String recieverName) {
        this.recieverName = recieverName;
    }

    public String getRecieverId() {
        return recieverId;
    }

    public void setRecieverId(String recieverId) {
        this.recieverId = recieverId;
    }
}
