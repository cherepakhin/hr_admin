package ru.perm.v.hradmin.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Создано OpenCode
 *
 * @author OpenCode
 */
public class EmployeeDtoTest {

    @Test
    public void testDefaultConstructor() {
        EmployeeDto dto = new EmployeeDto();

        assertNull(dto.getId());
        assertEquals("", dto.getFirstName());
        assertEquals("", dto.getLastName());
        assertEquals("", dto.getEmail());
        assertEquals(new PositionDto(-1L, ""), dto.getPosition());
    }

    @Test
    public void testConstructorAndGetters() {
        PositionDto positionDto = new PositionDto(1L, "Developer");
        EmployeeDto dto = new EmployeeDto(1L, "John", "Doe", "john@example.com", positionDto);

        assertEquals(1L, dto.getId());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("john@example.com", dto.getEmail());
        assertEquals(positionDto, dto.getPosition());
    }

    @Test
    public void testSetters() {
        PositionDto positionDto = new PositionDto(2L, "Manager");
        EmployeeDto dto = new EmployeeDto();
        dto.setId(2L);
        dto.setFirstName("Jane");
        dto.setLastName("Smith");
        dto.setEmail("jane@example.com");
        dto.setPosition(positionDto);

        assertEquals(2L, dto.getId());
        assertEquals("Jane", dto.getFirstName());
        assertEquals("Smith", dto.getLastName());
        assertEquals("jane@example.com", dto.getEmail());
        assertEquals(positionDto, dto.getPosition());
    }

    @Test
    public void testEqualsEqualValues() {
        PositionDto positionDto = new PositionDto(1L, "Developer");
        EmployeeDto dto1 = new EmployeeDto(1L, "John", "Doe", "john@example.com", positionDto);
        EmployeeDto dto2 = new EmployeeDto(1L, "John", "Doe", "john@example.com", positionDto);

        assertEquals(dto1, dto2);
    }

    @Test
    public void testNotEqualsDifferentId() {
        PositionDto positionDto = new PositionDto(1L, "Developer");
        EmployeeDto dto1 = new EmployeeDto(1L, "John", "Doe", "john@example.com", positionDto);
        EmployeeDto dto2 = new EmployeeDto(2L, "John", "Doe", "john@example.com", positionDto);

        assertNotEquals(dto1, dto2);
    }

    @Test
    public void testNotEqualsDifferentFirstName() {
        PositionDto positionDto = new PositionDto(1L, "Developer");
        EmployeeDto dto1 = new EmployeeDto(1L, "John", "Doe", "john@example.com", positionDto);
        EmployeeDto dto2 = new EmployeeDto(1L, "Jack", "Doe", "john@example.com", positionDto);

        assertNotEquals(dto1, dto2);
    }

    @Test
    public void testNotEqualsDifferentLastName() {
        PositionDto positionDto = new PositionDto(1L, "Developer");
        EmployeeDto dto1 = new EmployeeDto(1L, "John", "Doe", "john@example.com", positionDto);
        EmployeeDto dto2 = new EmployeeDto(1L, "John", "Brown", "john@example.com", positionDto);

        assertNotEquals(dto1, dto2);
    }

    @Test
    public void testNotEqualsDifferentEmail() {
        PositionDto positionDto = new PositionDto(1L, "Developer");
        EmployeeDto dto1 = new EmployeeDto(1L, "John", "Doe", "john@example.com", positionDto);
        EmployeeDto dto2 = new EmployeeDto(1L, "John", "Doe", "jack@example.com", positionDto);

        assertNotEquals(dto1, dto2);
    }

    @Test
    public void testNotEqualsDifferentPosition() {
        EmployeeDto dto1 = new EmployeeDto(1L, "John", "Doe", "john@example.com", new PositionDto(1L, "Developer"));
        EmployeeDto dto2 = new EmployeeDto(1L, "John", "Doe", "john@example.com", new PositionDto(2L, "Manager"));

        assertNotEquals(dto1, dto2);
    }

    @Test
    public void testNotEqualsNull() {
        EmployeeDto dto = new EmployeeDto(1L, "John", "Doe", "john@example.com", new PositionDto(1L, "Developer"));

        assertNotEquals(null, dto);
    }

    @Test
    public void testNotEqualsDifferentClass() {
        EmployeeDto dto = new EmployeeDto(1L, "John", "Doe", "john@example.com", new PositionDto(1L, "Developer"));

        assertNotEquals(new Object(), dto);
    }

    @Test
    public void testEqualsHashCode() {
        PositionDto positionDto = new PositionDto(1L, "Developer");
        EmployeeDto dto1 = new EmployeeDto(1L, "John", "Doe", "john@example.com", positionDto);
        EmployeeDto dto2 = new EmployeeDto(1L, "John", "Doe", "john@example.com", positionDto);

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    public void testNotEqualsHashCodeByField() {
        EmployeeDto dto1 = new EmployeeDto(1L, "John", "Doe", "john@example.com", new PositionDto(1L, "Developer"));
        EmployeeDto dto2 = new EmployeeDto(2L, "John", "Doe", "john@example.com", new PositionDto(1L, "Developer"));

        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    public void testEqualsSameObject() {
        EmployeeDto dto = new EmployeeDto(1L, "John", "Doe", "john@example.com", new PositionDto(1L, "Developer"));

        assertEquals(dto, dto);
    }

    @Test
    public void testToString() {
        PositionDto positionDto = new PositionDto(1L, "Developer");
        EmployeeDto dto = new EmployeeDto(1L, "John", "Doe", "john@example.com", positionDto);
        String str = dto.toString();

        assertTrue(str.contains("id=1"));
        assertTrue(str.contains("firstName='John'"));
        assertTrue(str.contains("lastName='Doe'"));
        assertTrue(str.contains("email='john@example.com'"));
        assertTrue(str.contains("position=PositionDto"));
    }
}