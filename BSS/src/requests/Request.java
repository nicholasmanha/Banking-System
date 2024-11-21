package requests;

import enums.RequestType;
import enums.Requester;
import enums.Status;
import java.io.Serializable;
import java.util.ArrayList;

// must implement Serializable in order to be sent
public class Request implements Serializable{
    private static int count = 0;
    private final int id;
    private final ArrayList<String> texts;
    private final RequestType type;
    private final Status status;
    private final Requester requester;
    private final double amount;

    public Request(){
    	this.amount = -1;
    	this.requester = Requester.UNDEFINED;
    	this.type = RequestType.UNDEFINED;
    	this.status = Status.UNDEFINED;
    	this.texts = null;
    	this.id = ++count;
    }
    public Request(Requester requester, RequestType type, Status status) {
    	this.amount = -1;
    	this.requester = requester;
    	this.texts = null;
        this.type = type;
        this.status = status;
        this.id = ++count;
    }
    public Request(ArrayList<String> texts, RequestType type, Status status) {
    	this.amount = -1;
    	this.requester = Requester.UNDEFINED;
    	this.texts = texts;
        this.type = type;
        this.status = status;
        this.id = ++count;
    }
    public Request(Requester requester, ArrayList<String> texts, RequestType type, Status status) {
    	this.amount = -1;
    	this.requester = requester;
        this.texts = texts;
        this.type = type;
        this.status = status;
        this.id = ++count;
    }
    public Request(double amt, RequestType type, Status status) {
    	this.amount = amt;
    	this.requester = Requester.UNDEFINED;
        this.texts = null;
        this.type = type;
        this.status = status;
        this.id = ++count;
    }
    public Request(RequestType type, Status status) {
    	this.amount = -1;
    	this.requester = Requester.UNDEFINED;
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

