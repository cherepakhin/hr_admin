package net.guides.springboot2.freemarker.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class PositionTest {
	@Test
	public void testEquals() {
		Position position1 = new Position(1L, "Postion 1");
		Position position2 = new Position(1L, "Postion 1");

		assertEquals(position1, position2);
	}

	@Test
	public void testNotEqualsById() {
		Position position1 = new Position(1L, "Postion 1");
		Position position2 = new Position(2L, "Postion 1");

		assertNotEquals(position1, position2);
	}

	@Test
	public void testNotEqualsByName() {
		Position position1 = new Position(1L, "Postion 1");
		Position position2 = new Position(1L, "Postion 2");

		assertNotEquals(position1, position2);
	}

	@Test
	public void testEqualsHashCode() {
		Position position1 = new Position(1L, "Postion 1");
		Position position2 = new Position(1L, "Postion 1");

		assertEquals(position1.hashCode(), position2.hashCode());
	}

	@Test
	public void testNotEqualsHashCodeByName() {
		Position position1 = new Position(1L, "Postion 1");
		Position position2 = new Position(1L, "Postion 2");

		assertNotEquals(position1.hashCode(), position2.hashCode());
	}
}
