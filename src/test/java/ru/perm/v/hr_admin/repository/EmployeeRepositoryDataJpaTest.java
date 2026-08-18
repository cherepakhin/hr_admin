package ru.perm.v.hr_admin.repository;

import ru.perm.v.hr_admin.model.Employee;
import ru.perm.v.hr_admin.model.Position;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
/*
С DataJpaTest программа будет загружена полностью
 */
/*
DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD
При таком режиме контекст приложения помечается как грязный после выполнения каждого
тестового метода в классе. Это означает, что после каждого тестового метода контекст
будет удалён из кэша и закрыт, а для последующих тестов с той же конфигурацией будет
создан новый контекст.
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class EmployeeRepositoryDataJpaTest {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private PositionRepository positionRepository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void whenFindById_thenReturnEmployee() {
		Position position = new Position();
		position.setId(-100L);
		position.setName("Developer");
		position = positionRepository.save(position); // Сохраняем и получаем ID

		Employee employee = new Employee("FirstName", "LastName", "mail@example.com", position);
		entityManager.persistAndFlush(employee);

		// When
		Optional<Employee> foundEmployee = employeeRepository.findById(employee.getId());

		// Then
		assertThat(foundEmployee).isPresent();
		assertThat(foundEmployee.get().getFirstName()).isEqualTo("FirstName");
		assertThat(foundEmployee.get().getLastName()).isEqualTo("LastName");
		assertThat(foundEmployee.get().getEmail()).isEqualTo("mail@example.com");
		assertThat(foundEmployee.get().getPosition()).isEqualTo(position);
	}

	@Test
	public void whenFindById_thenReturnEmployee11() {
		Position position = new Position(2L, "Position 2");
		position = positionRepository.save(position);

		// given
		Employee employee = new Employee("FirstName", "LastName", "mail@example.com", position);
		employeeRepository.save(employee);

		// when
		Optional<Employee> foundEmployee = employeeRepository.findById(employee.getId());

		// then
		assertThat(foundEmployee).isPresent();
		assertThat(foundEmployee.get().getFirstName()).isEqualTo("FirstName");
		assertThat(foundEmployee.get().getLastName()).isEqualTo("LastName");
		assertThat(foundEmployee.get().getEmail()).isEqualTo("mail@example.com");
		assertThat(foundEmployee.get().getPosition()).isEqualTo(position);
	}

	@Test
	public void whenSaveEmployee_thenEmployeeShouldBePersisted() {
		Position position = new Position(4L, "Position 4");
		position = positionRepository.save(position);
		// given
		Employee employee = new Employee("FirstName", "LastName", "mail@example.com", position);

		// when
		Employee savedEmployee = employeeRepository.save(employee);

		// then
		assertThat(savedEmployee.getId()).isNotNull();
		assertThat(savedEmployee.getFirstName()).isEqualTo("FirstName");
		assertThat(savedEmployee.getLastName()).isEqualTo("LastName");
		assertThat(savedEmployee.getEmail()).isEqualTo("mail@example.com");
		assertThat(savedEmployee.getPosition()).isEqualTo(position);
	}

	@Test
	public void whenDeleteById_thenEmployeeShouldBeRemoved() {
		Position position = new Position(3L, "Position 3");
		position = positionRepository.save(position);
		// given
		Employee employee = new Employee("FirstName", "LastName", "mail@example.com", position);

		// when
		Employee savedEmployee = employeeRepository.save(employee);

		// when
		employeeRepository.deleteById(savedEmployee.getId());
		Optional<Employee> optionalEmployee = employeeRepository.findById(savedEmployee.getId());

		// then
		assertThat(optionalEmployee).isEmpty();
	}

	@Test
	public void whenUpdateEmployee_thenEmployeeShouldBeUpdated() {
		Position position = new Position(3L, "Position 3");
		position = positionRepository.save(position);
		// given
		Employee employee = new Employee("FirstName", "LastName", "mail@example.com", position);

		// when
		Employee savedEmployee = employeeRepository.save(employee);

		assertThat(savedEmployee.getFirstName()).isEqualTo("FirstName");
		assertThat(savedEmployee.getEmail()).isEqualTo("mail@example.com");

		// when
		savedEmployee.setFirstName("New FirstName");
		savedEmployee.setEmail("new@example.com");
		Employee updatedEmployee = employeeRepository.save(savedEmployee);

		// then
		assertThat(updatedEmployee.getFirstName()).isEqualTo("New FirstName");
		assertThat(updatedEmployee.getEmail()).isEqualTo("new@example.com");
	}

	@Test
	public void shouldFindAllByPositionWhenPositionExists() {
		// Given
		Position developer = new Position();
		developer.setId(100L);
		developer.setName("Developer");
		developer = positionRepository.save(developer);

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
		manager.setId(10L);
		manager.setName("Manager");
		manager = positionRepository.save(manager);

		Position developer = new Position();
		developer.setId(20L);
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
	public void shouldReturnEmptyListWhenPositionHasNoEmployees() {
		Position tester = new Position();
		tester.setId(employeeRepository.getNextId());
		tester.setName("Tester");
		tester = positionRepository.save(tester);

		// When
		List<Employee> result = employeeRepository.findAllByPosition(tester.getId());

		// Then
		assertThat(result).isEmpty();
	}

}