package net.guides.springboot2.freemarker.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class EmployeeTest {
	@Test
	public void testEquals() {
		Position position1 = new Position(1L, "Postion 1");

		Employee employee1 = new Employee(10L, "Firstame", "LastName", "Email", position1);
		Employee employee2 = new Employee(10L, "Firstame", "LastName", "Email", position1);

		assertEquals(employee1, employee2);
	}

	@Test
	public void testHashCode() {
		Position position1 = new Position(1L, "Postion 1");

		Employee employee1 = new Employee(10L, "Firstame", "LastName", "Email", position1);
		Employee employee2 = new Employee(10L, "Firstame", "LastName", "Email", position1);

		assertEquals(employee1.hashCode(), employee2.hashCode());
	}

	@Test
	public void testNotEquals() {
		Position position1 = new Position(1L, "Postion 1");

		Employee employee1 = new Employee(10L, "Firstame", "LastName", "Email", position1);
		Employee employee2 = new Employee(11L, "Firstame", "LastName", "Email", position1);

		assertNotEquals(employee1, employee2);
	}
}
