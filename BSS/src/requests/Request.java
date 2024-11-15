package requests;

import enums.RequestType;
import enums.Requester;
import enums.Status;
import java.io.Serializable;

// must implement Serializable in order to be sent
public class Request implements Serializable{
    private static int count = 0;
    private final int id;
    private final String text;
    private final RequestType type;
    private final Status status;
    private final Requester requester;

    public Request(){
    	this.requester = Requester.UNDEFINED;
    	this.type = RequestType.UNDEFINED;
    	this.status = Status.UNDEFINED;
    	this.text = "Undefined";
    	this.id = ++count;
    }
    public Request(Requester requester, RequestType type, Status status) {
    	this.requester = requester;
    	this.text = "";
        this.type = type;
        this.status = status;
        this.id = ++count;
    }
    public Request(Requester requester, String text, RequestType type, Status status) {
    	this.requester = requester;
        this.text = text;
        this.type = type;
        this.status = status;
        this.id = ++count;
    }

    public String getText() {
        return text;
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
}

