package bss;

public class User {
	
	protected int id;
//	private DateTime created;
	static int count = 0;
	
	public User() {
		this.id = count++;
//		this.created = new DateTime();
  }
	public int getId() {
		return id;
	}
	public void setID(int in_id) {
		this.id = in_id;
	}
	
//	public DateTime getCreated() {
//		return created;
//	}

}