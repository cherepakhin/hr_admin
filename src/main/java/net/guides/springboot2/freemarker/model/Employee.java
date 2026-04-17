package net.guides.springboot2.freemarker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.util.Objects;

@Entity
@Table(name = "employees")
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Size(min = 3, max = 15, message = "First name must be between 3 to 20 characters long.")
	private String firstName;
	private String lastName;
	private String email;
	@ManyToOne(fetch = FetchType.EAGER) // EAGER!!! - атрибут нужен всегда, нет смысла его делать lazy
	@JoinColumn(name = "position_id", nullable = false)
	private Position position = new Position(-1L, "");

	public Employee() {
	}

	public Employee(Long id, String firstName, String lastName, String email, Position position) {
		this(firstName, lastName, email, position);
		this.id = id;
	}

	public Employee(String firstName, String lastName, String email, Position position) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.position = position;
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Employee employee)) return false;
		return Objects.equals(id, employee.id) && Objects.equals(firstName, employee.firstName) && Objects.equals(lastName, employee.lastName) && Objects.equals(email, employee.email) && Objects.equals(position, employee.position);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, firstName, lastName, email, position);
	}

	@Override
	public String toString() {
		return "Employee{" +
				"id=" + id +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", email='" + email + '\'' +
				", position=" + position +
				'}';
	}
}
