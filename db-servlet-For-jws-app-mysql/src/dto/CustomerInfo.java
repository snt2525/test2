package dto;

public class CustomerInfo {
	String id;
	String email;
	int gender;
	int age;
	public CustomerInfo(String id, String email, String gender, String age){
		this.id = id;
		this.email = email;
		if(gender.equals("M"))
			this.gender = 1; 
		else
			this.gender = 0; 
		String[] tmp = age.split("-");
		this.age = Integer.parseInt(tmp[0]);
	}
	public String getId() {
		return id;
	}
	public String getEmail() {
		return email;
	}
	public int getGender() {
		return gender;
	}
	public int getAge() {
		return age;
	}
}
