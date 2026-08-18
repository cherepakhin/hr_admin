package ru.perm.v.hr_admin.model;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EmployeeGigaTest {

    private final Position position = new Position(1L, "Developer");

    @Test
    public void shouldCreateEmployeeWithDefaultConstructor() {
        Employee employee = new Employee();
        assertNotNull(employee);
    }

    @Test
    public void shouldCreateEmployeeWithIdFirstNameLastNameEmailAndPosition() {
        Employee employee = new Employee(1L, "John", "Doe", "john@example.com", position);
        assertEquals(1L, employee.getId());
        assertEquals("John", employee.getFirstName());
        assertEquals("Doe", employee.getLastName());
        assertEquals("john@example.com", employee.getEmail());
        assertEquals(position, employee.getPosition());
    }

    @Test
    public void shouldCreateEmployeeWithFirstNameLastNameEmailAndPosition() {
        Employee employee = new Employee("Jane", "Smith", "jane@example.com", position);
        assertNull(employee.getId());
        assertEquals("Jane", employee.getFirstName());
        assertEquals("Smith", employee.getLastName());
        assertEquals("jane@example.com", employee.getEmail());
        assertEquals(position, employee.getPosition());
    }

    @Test
    public void shouldSetAndGetId() {
        Employee employee = new Employee();
        employee.setId(1L);
        assertEquals(1L, employee.getId());
    }

    @Test
    public void shouldSetAndGetFirstName() {
        Employee employee = new Employee();
        employee.setFirstName("John");
        assertEquals("John", employee.getFirstName());
    }

    @Test
    public void shouldSetAndGetLastName() {
        Employee employee = new Employee();
        employee.setLastName("Doe");
        assertEquals("Doe", employee.getLastName());
    }

    @Test
    public void shouldSetAndGetEmail() {
        Employee employee = new Employee();
        employee.setEmail("john@example.com");
        assertEquals("john@example.com", employee.getEmail());
    }

    @Test
    public void shouldSetAndGetPosition() {
        Employee employee = new Employee();
        Position newPosition = new Position(2L, "Manager");
        employee.setPosition(newPosition);
        assertEquals(newPosition, employee.getPosition());
    }

    @Test
    public void shouldReturnCorrectToString() {
        Employee employee = new Employee(1L, "John", "Doe", "john@example.com", position);
        String expected = "Employee{id=1, firstName='John', lastName='Doe', email='john@example.com', position=" + position + "}";
        assertEquals(expected, employee.toString());
    }

    @Test
    public void shouldEqualSameObject() {
        Employee employee = new Employee(1L, "John", "Doe", "john@example.com", position);
        assertTrue(employee.equals(employee));
    }

    @Test
	public void shouldNotEqualNull() {
        Employee employee = new Employee(1L, "John", "Doe", "john@example.com", position);
        assertFalse(employee.equals(null));
    }

    @Test
	public void shouldNotEqualDifferentClass() {
        Employee employee = new Employee(1L, "John", "Doe", "john@example.com", position);
        assertFalse(employee.equals(new Object()));
    }

    @Test
	public void shouldEqualWhenAllFieldsAreEqual() {
        Employee emp1 = new Employee(1L, "John", "Doe", "john@example.com", position);
        Employee emp2 = new Employee(1L, "John", "Doe", "john@example.com", position);
        assertTrue(emp1.equals(emp2));
        assertEquals(emp1.hashCode(), emp2.hashCode());
    }

    @Test
	public void shouldNotEqualWhenIdDiffers() {
        Employee emp1 = new Employee(1L, "John", "Doe", "john@example.com", position);
        Employee emp2 = new Employee(2L, "John", "Doe", "john@example.com", position);
        assertFalse(emp1.equals(emp2));
    }

    @Test
	public void shouldNotEqualWhenFirstNameDiffers() {
        Employee emp1 = new Employee(1L, "John", "Doe", "john@example.com", position);
        Employee emp2 = new Employee(1L, "Jane", "Doe", "john@example.com", position);
        assertFalse(emp1.equals(emp2));
    }

    @Test
	public void shouldNotEqualWhenLastNameDiffers() {
        Employee emp1 = new Employee(1L, "John", "Doe", "john@example.com", position);
        Employee emp2 = new Employee(1L, "John", "Smith", "john@example.com", position);
        assertFalse(emp1.equals(emp2));
    }

    @Test
	public void shouldNotEqualWhenEmailDiffers() {
        Employee emp1 = new Employee(1L, "John", "Doe", "john@example.com", position);
        Employee emp2 = new Employee(1L, "John", "Doe", "jane@example.com", position);
        assertFalse(emp1.equals(emp2));
    }

    @Test
	public void shouldNotEqualWhenPositionDiffers() {
        Position pos1 = new Position(1L, "Developer");
        Position pos2 = new Position(2L, "Manager");
        Employee emp1 = new Employee(1L, "John", "Doe", "john@example.com", pos1);
        Employee emp2 = new Employee(1L, "John", "Doe", "john@example.com", pos2);
        assertFalse(emp1.equals(emp2));
    }
}