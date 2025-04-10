package diet;


public class Customer {
	
	private String lastName;
	private String firstName;
	private String email;
	private String phone;

	public Customer(String lastName, String firstName, String email, String phone) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.email = email;
		this.phone = phone;
	}

	public String getLastName() {
		return lastName;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void SetEmail(String email) {
		this.email = email;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
