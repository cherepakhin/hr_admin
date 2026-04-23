package net.guides.springboot2.freemarker.repository;

import net.guides.springboot2.freemarker.model.Employee;
import net.guides.springboot2.freemarker.model.Position;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class EmployeeRepositoryGigaTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Test
    public void shouldFindAllByPositionWhenPositionExists() {
        // Given
        Position developer = new Position();
		developer.setId(1L);
        developer.setName("Developer");
        positionRepository.save(developer);

        Employee emp1 = new Employee("John", "Doe", "john@example.com", developer);
        Employee emp2 = new Employee("Jane", "Smith", "jane@example.com", developer);
        employeeRepository.save(emp1);
        employeeRepository.save(emp2);

        // When
        List<Employee> result = employeeRepository.findAllByPosition(developer.getId());

        // Then
        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting(Employee::getEmail)
                .containsExactlyInAnyOrder("john@example.com", "jane@example.com");
    }

    @Test
    public void shouldReturnEmptyListWhenNoEmployeesForPosition() {
        // Given
        Position manager = new Position();
        manager.setName("Manager");
		manager = positionRepository.save(manager);

        Position developer = new Position();
        developer.setName("Developer");
		developer = positionRepository.save(developer);

        Employee emp = new Employee("John", "Doe", "john@example.com", manager);
        employeeRepository.save(emp);

        // When
        List<Employee> result = employeeRepository.findAllByPosition(developer.getId());

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldReturnNextIdAsOneWhenTableIsEmpty() {
        // When
        Long nextId = employeeRepository.getNextId();

        // Then
        assertThat(nextId).isEqualTo(1L);
    }

    @Test
    public void shouldReturnNextIdBasedOnMaxId() {
        // Given
        Position pos = new Position();
		pos.setId(1L);
		pos.setName("Developer");
		positionRepository.save(pos);
        Employee e1 = new Employee("FirstName1", "LastName1", "a@b.com", pos);
		employeeRepository.save(e1);

		Employee e2 = new Employee("FirstName2", "LastName2", "c@d.com", pos);
		employeeRepository.save(e2);

		Employee e3 = new Employee("FirstName3", "LastName3", "e@f.com", pos);
		employeeRepository.save(e3);

        // When
        Long nextId = employeeRepository.getNextId();

        // Then
        assertThat(nextId).isEqualTo(4L); // ID autoincrement from 1, max(id) + 1
    }

    @Test
    public void shouldFindByFirstNameAndLastName() {
		Position developer = positionRepository.save(new Position(1L, "Dev"));
        // Given
        Employee emp = new Employee("Alice", "Brown", "alice@example.com", developer);
        employeeRepository.save(emp);

        // When
        Optional<Employee> result = employeeRepository.findByFirstNameAndLastName("Alice", "Brown");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("alice@example.com");
    }

    @Test
    public void shouldNotFindByFirstNameAndLastName_WhenNotFound() {
        // When
        Optional<Employee> result = employeeRepository.findByFirstNameAndLastName("Non", "Existent");

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldFindByEmail() {
		Position developer = positionRepository.save(new Position(1L, "Dev"));
        // Given
        Employee emp = new Employee("Bob", "Lee", "bob@lee.com", developer);
        employeeRepository.save(emp);

        // When
        Optional<Employee> result = employeeRepository.findByEmail("bob@lee.com");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getFirstName()).isEqualTo("Bob");
    }

    @Test
    public void shouldNotFindByEmail_WhenNotFound() {
        // When
        Optional<Employee> result = employeeRepository.findByEmail("unknown@example.com");

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldFilterByFirstName() {
		Position developer = positionRepository.save(new Position(1L, "Dev"));
        // Given
        Employee emp1 = new Employee("John", "Doe", "john@example.com", developer);
        Employee emp2 = new Employee("Johnny", "Smith", "johnny@example.com", developer);
        Employee emp3 = new Employee("Alice", "Cooper", "alice@example.com", developer);
        employeeRepository.saveAll(Arrays.asList(emp1, emp2, emp3));

        // When
        Page<Employee> result = employeeRepository.findByFiltersAndSort(
                "John", null, null, null, PageRequest.of(0, 10));

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.getContent())
                .extracting(Employee::getFirstName)
                .containsOnly("John", "Johnny");
    }

    @Test
    public void shouldFilterByPositionIds() {
        // Given
        Position dev = new Position();
		dev.setName("Developer");
		positionRepository.save(dev);

		Position mgr = new Position();
		mgr.setName("Manager");
		positionRepository.save(mgr);

        Employee emp1 = new Employee("John", "Doe", "john@example.com", dev);
        Employee emp2 = new Employee("Jane", "Smith", "jane@example.com", mgr);
        employeeRepository.saveAll(Arrays.asList(emp1, emp2));

        // When
        Page<Employee> result = employeeRepository.findByFiltersAndSort(
                null, null, Arrays.asList(dev.getId()), null, PageRequest.of(0, 10));

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.getContent().get(0).getFirstName()).isEqualTo("John");
    }

    @Test
    public void shouldFilterByEmail() {
		Position position = new Position();
		position.setName("Developer");
		positionRepository.save(position);
        // Given
        Employee emp1 = new Employee("John", "Doe", "john.doe@company.com", position);
        Employee emp2 = new Employee("Jane", "Smith", "jane@test.com", position);
        employeeRepository.saveAll(Arrays.asList(emp1, emp2));

        // When
        Page<Employee> result = employeeRepository.findByFiltersAndSort(
                null, null, null, "company", PageRequest.of(0, 10));

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.getContent().get(0).getEmail()).isEqualTo("john.doe@company.com");
    }

    @Test
    public void shouldReturnAllWhenNoFiltersProvided() {
		Position position = new Position();
		position.setName("Developer");
		positionRepository.save(position);
        // Given
        Employee emp1 = new Employee("John", "Doe", "john@example.com", position);
        Employee emp2 = new Employee("Jane", "Smith", "jane@example.com", position);
        employeeRepository.saveAll(Arrays.asList(emp1, emp2));

        // When
        Page<Employee> result = employeeRepository.findByFiltersAndSort(
                null, null, null, null, PageRequest.of(0, 10));

        // Then
        assertThat(result).hasSize(2);
    }
}