package ru.perm.v.hr_admin.mapper;

import ru.perm.v.hr_admin.dto.EmployeeDto;
import ru.perm.v.hr_admin.dto.PositionDto;
import ru.perm.v.hr_admin.model.Employee;
import ru.perm.v.hr_admin.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeMapperTest {

    private EmployeeMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new EmployeeMapper();
    }

    // ==================== toDTO tests ====================

    @Test
    void toDTO_withValidEmployee_returnsCorrectDto() {
        // Given
        Position position = new Position(1L, "Developer");
        Employee employee = new Employee(1L, "John", "Doe", "john@example.com", position);

        // When
        EmployeeDto dto = mapper.toDTO(employee);

        // Then
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("john@example.com", dto.getEmail());
        assertNotNull(dto.getPosition());
        assertEquals(1L, dto.getPosition().getId());
        assertEquals("Developer", dto.getPosition().getName());
    }

    @Test
    void toDTO_withNullEmployee_returnsNull() {
        // When & Then
        assertNull(mapper.toDTO(null));
    }

    @Test
    void toDTO_withAllNullFields_createsDtoWithNulls() {
        // Given
        Employee employee = new Employee();

        // When
        EmployeeDto dto = mapper.toDTO(employee);

        // Then
        assertNotNull(dto);
        assertNull(dto.getId());
        assertEquals("", dto.getFirstName());
        assertEquals("",dto.getFirstName());
        assertEquals("",dto.getLastName());
        assertEquals("",dto.getEmail());
        assertEquals(new PositionDto(-1L, ""),dto.getPosition());
    }

    @Test
    void toDTO_withEmptyStrings_createsDtoWithEmptyStrings() {
        // Given
        Position position = new Position(1L, "");
        Employee employee = new Employee(1L, "", "", "", position);

        // When
        EmployeeDto dto = mapper.toDTO(employee);

        // Then
        assertNotNull(dto);
        assertEquals("", dto.getFirstName());
        assertEquals("", dto.getLastName());
        assertEquals("", dto.getEmail());
        assertNotNull(dto.getPosition());
        assertEquals("", dto.getPosition().getName());
    }

    @Test
    void toDTO_withNullPosition_setsNullInDto() {
        // Given
        Employee employee = new Employee(1L, "John", "Doe", "john@example.com", null);

        // When
        EmployeeDto dto = mapper.toDTO(employee);

        // Then
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("John", dto.getFirstName());
        assertNull(dto.getPosition());
    }

    @Test
    void toDTO_withZeroId_createsDtoWithZeroId() {
        // Given
        Position position = new Position(0L, "Tester");
        Employee employee = new Employee(0L, "Jane", "Smith", "jane@example.com", position);

        // When
        EmployeeDto dto = mapper.toDTO(employee);

        // Then
        assertNotNull(dto);
        assertEquals(0L, dto.getId());
        assertEquals(0L, dto.getPosition().getId());
    }

    @Test
    void toDTO_withNegativeId_createsDtoWithNegativeId() {
        // Given
        Position position = new Position(-1L, "Contractor");
        Employee employee = new Employee(-1L, "Bob", "Jones", "bob@example.com", position);

        // When
        EmployeeDto dto = mapper.toDTO(employee);

        // Then
        assertNotNull(dto);
        assertEquals(-1L, dto.getId());
        assertEquals(-1L, dto.getPosition().getId());
    }

    @Test
    void toDTO_withMaxLongId_preservesId() {
        // Given
        Position position = new Position(Long.MAX_VALUE, "Senior");
        Employee employee = new Employee(Long.MAX_VALUE, "Alice", "Wonderland", "alice@example.com", position);

        // When
        EmployeeDto dto = mapper.toDTO(employee);

        // Then
        assertNotNull(dto);
        assertEquals(Long.MAX_VALUE, dto.getId());
        assertEquals(Long.MAX_VALUE, dto.getPosition().getId());
    }

    @Test
    void toDTO_roundTrip_toEntity_consistentData() {
        // Given
        Position position = new Position(5L, "Manager");
        Employee original = new Employee(5L, "Test", "User", "test@example.com", position);

        // When
        EmployeeDto dto = mapper.toDTO(original);
        Employee back = mapper.toEntity(dto);

        // Then
        assertNotNull(back);
        assertEquals(original.getId(), back.getId());
        assertEquals(original.getFirstName(), back.getFirstName());
        assertEquals(original.getLastName(), back.getLastName());
        assertEquals(original.getEmail(), back.getEmail());
        assertNotNull(back.getPosition());
        assertEquals(original.getPosition().getId(), back.getPosition().getId());
        assertEquals(original.getPosition().getName(), back.getPosition().getName());
    }

    // ==================== toEntity tests ====================

    @Test
    void toEntity_withValidDto_returnsCorrectEmployee() {
        // Given
        PositionDto positionDto = new PositionDto(1L, "Developer");
        EmployeeDto dto = new EmployeeDto();
        dto.setId(1L);
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("john@example.com");
        dto.setPosition(positionDto);

        // When
        Employee employee = mapper.toEntity(dto);

        // Then
        assertNotNull(employee);
        assertEquals(1L, employee.getId());
        assertEquals("John", employee.getFirstName());
        assertEquals("Doe", employee.getLastName());
        assertEquals("john@example.com", employee.getEmail());
        assertNotNull(employee.getPosition());
        assertEquals(1L, employee.getPosition().getId());
        assertEquals("Developer", employee.getPosition().getName());
    }

    @Test
    void toEntity_withNullDto_returnsNull() {
        // When & Then
        assertNull(mapper.toEntity(null));
    }

    @Test
    void toEntity_withAllNullFields_createsEmployeeWithNulls() {
        // Given
        EmployeeDto dto = new EmployeeDto();

        // When
        Employee employee = mapper.toEntity(dto);

        // Then
        assertNotNull(employee);
        assertNull(employee.getId());
        assertEquals("", employee.getFirstName());
        assertEquals("", employee.getLastName());
        assertEquals("", employee.getEmail());
        assertEquals(new Position(-1L, ""), employee.getPosition());
    }

    @Test
    void toEntity_withNullPosition_setsNullInEntity() {
        // Given
        EmployeeDto dto = new EmployeeDto();
        dto.setId(1L);
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("john@example.com");
        dto.setPosition(null);

        // When
        Employee employee = mapper.toEntity(dto);

        // Then
        assertNotNull(employee);
        assertEquals(1L, employee.getId());
        assertEquals("John", employee.getFirstName());
        assertNull(employee.getPosition());
    }

    @Test
    void toEntity_withEmptyStrings_createsEmployeeWithEmptyStrings() {
        // Given
        PositionDto positionDto = new PositionDto(1L, "");
        EmployeeDto dto = new EmployeeDto();
        dto.setId(1L);
        dto.setFirstName("");
        dto.setLastName("");
        dto.setEmail("");
        dto.setPosition(positionDto);

        // When
        Employee employee = mapper.toEntity(dto);

        // Then
        assertNotNull(employee);
        assertEquals("", employee.getFirstName());
        assertEquals("", employee.getLastName());
        assertEquals("", employee.getEmail());
        assertNotNull(employee.getPosition());
        assertEquals("", employee.getPosition().getName());
    }

    @Test
    void toEntity_withZeroId_preservesZeroId() {
        // Given
        PositionDto positionDto = new PositionDto(0L, "Tester");
        EmployeeDto dto = new EmployeeDto();
        dto.setId(0L);
        dto.setFirstName("Jane");
        dto.setLastName("Smith");
        dto.setEmail("jane@example.com");
        dto.setPosition(positionDto);

        // When
        Employee employee = mapper.toEntity(dto);

        // Then
        assertNotNull(employee);
        assertEquals(0L, employee.getId());
        assertEquals(0L, employee.getPosition().getId());
    }

    @Test
    void toEntity_withNegativeId_preservesNegativeId() {
        // Given
        PositionDto positionDto = new PositionDto(-1L, "Contractor");
        EmployeeDto dto = new EmployeeDto();
        dto.setId(-1L);
        dto.setFirstName("Bob");
        dto.setLastName("Jones");
        dto.setEmail("bob@example.com");
        dto.setPosition(positionDto);

        // When
        Employee employee = mapper.toEntity(dto);

        // Then
        assertNotNull(employee);
        assertEquals(-1L, employee.getId());
        assertEquals(-1L, employee.getPosition().getId());
    }

    @Test
    void toEntity_withMaxLongId_preservesMaxLongId() {
        // Given
        PositionDto positionDto = new PositionDto(Long.MAX_VALUE, "Senior");
        EmployeeDto dto = new EmployeeDto();
        dto.setId(Long.MAX_VALUE);
        dto.setFirstName("Alice");
        dto.setLastName("Wonderland");
        dto.setEmail("alice@example.com");
        dto.setPosition(positionDto);

        // When
        Employee employee = mapper.toEntity(dto);

        // Then
        assertNotNull(employee);
        assertEquals(Long.MAX_VALUE, employee.getId());
        assertEquals(Long.MAX_VALUE, employee.getPosition().getId());
    }

    @Test
    void toEntity_roundTrip_toDTO_consistentData() {
        // Given
        PositionDto positionDto = new PositionDto(5L, "Manager");
        EmployeeDto original = new EmployeeDto();
        original.setId(5L);
        original.setFirstName("Test");
        original.setLastName("User");
        original.setEmail("test@example.com");
        original.setPosition(positionDto);

        // When
        Employee employee = mapper.toEntity(original);
        EmployeeDto back = mapper.toDTO(employee);

        // Then
        assertNotNull(back);
        assertEquals(original.getId(), back.getId());
        assertEquals(original.getFirstName(), back.getFirstName());
        assertEquals(original.getLastName(), back.getLastName());
        assertEquals(original.getEmail(), back.getEmail());
        assertNotNull(back.getPosition());
        assertEquals(original.getPosition().getId(), back.getPosition().getId());
        assertEquals(original.getPosition().getName(), back.getPosition().getName());
    }

    @Test
    void toDTO_toEntity_toDTO_identity() {
        // Given
        Position position = new Position(10L, "Architect");
        Employee original = new Employee(10L, "Charlie", "Brown", "charlie@example.com", position);

        // When
        EmployeeDto dto1 = mapper.toDTO(original);
        Employee employee = mapper.toEntity(dto1);
        EmployeeDto dto2 = mapper.toDTO(employee);

        // Then
        assertNotNull(dto2);
        assertEquals(dto1.getId(), dto2.getId());
        assertEquals(dto1.getFirstName(), dto2.getFirstName());
        assertEquals(dto1.getLastName(), dto2.getLastName());
        assertEquals(dto1.getEmail(), dto2.getEmail());
        assertNotNull(dto2.getPosition());
        assertEquals(dto1.getPosition().getId(), dto2.getPosition().getId());
        assertEquals(dto1.getPosition().getName(), dto2.getPosition().getName());
    }

    @Test
    void toEntity_toDTO_toEntity_identity() {
        // Given
        PositionDto positionDto = new PositionDto(10L, "Architect");
        EmployeeDto original = new EmployeeDto();
        original.setId(10L);
        original.setFirstName("Charlie");
        original.setLastName("Brown");
        original.setEmail("charlie@example.com");
        original.setPosition(positionDto);

        // When
        Employee employee = mapper.toEntity(original);
        EmployeeDto dto1 = mapper.toDTO(employee);
        Employee back = mapper.toEntity(dto1);

        // Then
        assertNotNull(back);
        assertEquals(original.getId(), back.getId());
        assertEquals(original.getFirstName(), back.getFirstName());
        assertEquals(original.getLastName(), back.getLastName());
        assertEquals(original.getEmail(), back.getEmail());
        assertNotNull(back.getPosition());
        assertEquals(original.getPosition().getId(), back.getPosition().getId());
        assertEquals(original.getPosition().getName(), back.getPosition().getName());
    }
}