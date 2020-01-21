package CatalogService;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

//@Entity
//@Table(name="PEOPLE")
public class Person implements Comparable<Person>{
	private String id;
	private Name name;
	private Date birthdate;
	private Set<Person> parents;
	private Set<Person> children;

	public Person() {
		this.parents = new HashSet<>();
		this.children = new HashSet<>();
	}

	public Person(String id, Name name, Date birthdate) {
		this();
		this.id = id;
		this.name = name;
		this.birthdate = birthdate;
	}

	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Embedded
	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	@Temporal(TemporalType.DATE)
	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

//	@Transient
	@ManyToMany(fetch = FetchType.LAZY)
	public Set<Person> getParents() {
		return parents;
	}

//	@Transient
	public void setParents(Set<Person> parents) {
		this.parents = parents;
	}

//	@Transient
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "parents")
	public Set<Person> getChildren() {
		return children;
	}

//	@Transient
	public void setChildren(Set<Person> children) {
		this.children = children;
	}

	public void addChild (Person child) {
		this.children.add(child);
		child.parents.add(this);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int compareTo(Person other) {
		return this.id.compareTo(other.id);
	}

}
