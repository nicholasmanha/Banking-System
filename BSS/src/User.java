
public class User {
	
	private int id;
	private DateTime created;
	private static int count = 0;
	
	public User() {
		this.id = count++;
		this.created = new DateTime();
	}
	
	
	public int getId() {
		return id;
	}
	
	public DateTime getCreated() {
		return created;
	}
	
	
	
	
	
	
	
	
	
}