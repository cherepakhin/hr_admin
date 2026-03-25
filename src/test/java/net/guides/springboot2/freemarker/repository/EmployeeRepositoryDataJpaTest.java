package net.guides.springboot2.freemarker.repository;

import net.guides.springboot2.freemarker.model.Employee;
import net.guides.springboot2.freemarker.model.Position;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
/*
С DataJpaTest программа будет загружена полностью
 */
public class EmployeeRepositoryDataJpaTest {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private PositionRepository positionRepository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void whenFindById_thenReturnEmployee() {
		// Given: Сначала сохраняем позицию, чтобы избежать JpaObjectRetrievalFailureException
		Position position = new Position();
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
		positionRepository.save(position);

		// given
		Employee employee = new Employee("FirstName", "LastName", "mail@example.com", position);
		entityManager.persistAndFlush(employee);

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
		positionRepository.save(position);
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
		positionRepository.save(position);
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
}