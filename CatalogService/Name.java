package CatalogService;

import javax.persistence.Embeddable;

@Embeddable
public class Name {
	
	private String firstName;
	private String lastName;

	public Name() {
	}

	public Name(String name) {
		
		this.firstName  = "";
		this.lastName = "";
		
		if (name != null) {
			String[] args = name.split(" ");
			if (args.length >0) {
				this.firstName = args[0];
			}
			if (args.length >1) {
				this.lastName = args[1];
			}
		}
	}

	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return firstName + " " + lastName;
	}

}
