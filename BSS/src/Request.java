
public class Request {
	private static int count = 0;
	private final int id;
	private final String text;
	private final RequestType type;
	private final Status status;

	public Request(){
		this.type = RequestType.UNDEFINED;
    	this.status = Status.UNDEFINED;
    	this.text = "Undefined";
    	this.id = ++count;
    }

	public Request(RequestType type, Status status) {
		this.type = type;
    	this.text = "";
        this.status = status;
        this.id = ++count;
    }

	public Request(String text, RequestType type, Status status) {
		this.type = type;
        this.text = text;
        this.status = status;
        this.id = ++count;
    }
}
