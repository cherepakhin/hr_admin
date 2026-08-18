package ru.perm.v.hr_admin.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class EmployeeTest {
	@Test
	public void testEquals() {
		Position position1 = new Position(1L, "Position 1");

		Employee employee1 = new Employee(10L, "FirstName", "LastName", "Email", position1);
		Employee employee2 = new Employee(10L, "FirstName", "LastName", "Email", position1);

		assertEquals(employee1, employee2);
	}

	@Test
	public void testHashCode() {
		Position position1 = new Position(1L, "Position 1");

		Employee employee1 = new Employee(10L, "FirstName", "LastName", "Email", position1);
		Employee employee2 = new Employee(10L, "FirstName", "LastName", "Email", position1);

		assertEquals(employee1.hashCode(), employee2.hashCode());
	}

	@Test
	public void testNotEquals() {
		Position position1 = new Position(1L, "Position 1");

		Employee employee1 = new Employee(10L, "FirstName", "LastName", "Email", position1);
		Employee employee2 = new Employee(11L, "FirstName", "LastName", "Email", position1);

		assertNotEquals(employee1, employee2);
	}

	@Test
	void setPositionTest() {
		Position position = new Position(1L, "POSITION");
		Employee employee = new Employee();
		employee.setPosition(position);

		assertEquals(position, employee.getPosition());
	}
}
