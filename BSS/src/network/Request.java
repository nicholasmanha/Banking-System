package network;

import enums.RequestType;
import enums.Requester;
import enums.Status;
import enums.UserType;

import java.io.Serializable;
import java.util.ArrayList;

// must implement Serializable in order to be sent
public class Request implements Serializable{
    private static int count = 0;
    private final int id;
    private final ArrayList<String> texts;
    private final RequestType type;
    private final Status status;
    private final UserType userType;
    private final double amount;

    public Request(){
    	this.amount = -1;
    	this.userType = UserType.UNDEFINED;
    	this.type = RequestType.UNDEFINED;
    	this.status = Status.UNDEFINED;
    	this.texts = null;
    	this.id = ++count;
    }
    public Request(UserType userType, RequestType type, Status status) {
    	this.amount = -1;
    	this.userType = userType;
    	this.texts = null;
        this.type = type;
        this.status = status;
        this.id = ++count;
    }
    public Request(ArrayList<String> texts, RequestType type, Status status) {
    	this.amount = -1;
    	this.userType = UserType.UNDEFINED;
    	this.texts = texts;
        this.type = type;
        this.status = status;
        this.id = ++count;
    }
    public Request(ArrayList<String> texts, double amt, RequestType type, Status status) {
    	this.amount = amt;
    	this.userType = UserType.UNDEFINED;
    	this.texts = texts;
        this.type = type;
        this.status = status;
        this.id = ++count;
    }
    public Request(UserType userType, ArrayList<String> texts, RequestType type, Status status) {
    	this.amount = -1;
    	this.userType = userType;
        this.texts = texts;
        this.type = type;
        this.status = status;
        this.id = ++count;
    }
    public Request(double amt, RequestType type, Status status) {
    	this.amount = amt;
    	this.userType = UserType.UNDEFINED;
        this.texts = null;
        this.type = type;
        this.status = status;
        this.id = ++count;
    }
    public Request(RequestType type, Status status) {
    	this.amount = -1;
    	this.userType = UserType.UNDEFINED;
        this.texts = null;
        this.type = type;
        this.status = status;
        this.id = ++count;
    }

    public ArrayList<String> getTexts() {
        return texts;
    }
    public Status getStatus() {
        return status;
    }
    public UserType getUserType() {
    	return this.userType;
    }
    public int getID(){
        return id;
    }

    public RequestType getType(){
        return type;
    }
    public double getAmount() {
    	return amount;
    }
}

