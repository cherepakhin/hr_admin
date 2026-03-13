package net.guides.springboot2.freemarker.repository;

import net.guides.springboot2.freemarker.model.Employee;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void whenFindById_thenReturnEmployee() {
        // given
        Employee employee = new Employee("John", "Doe", "john.doe@example.com");
        entityManager.persistAndFlush(employee);

        // when
        Optional<Employee> found = employeeRepository.findById(employee.getId());

        // then
        Assertions.assertThat(found).isPresent();
        Assertions.assertThat(found.get().getFirstName()).isEqualTo("John");
        Assertions.assertThat(found.get().getLastName()).isEqualTo("Doe");
        Assertions.assertThat(found.get().getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    public void whenSaveEmployee_thenEmployeeShouldBePersisted() {
        // given
        Employee employee = new Employee("Jane", "Smith", "jane.smith@example.com");

        // when
        Employee saved = employeeRepository.save(employee);

        // then
        Assertions.assertThat(saved.getId()).isNotNull();
        Assertions.assertThat(saved.getFirstName()).isEqualTo("Jane");
        Assertions.assertThat(saved.getLastName()).isEqualTo("Smith");
        Assertions.assertThat(saved.getEmail()).isEqualTo("jane.smith@example.com");
    }

    @Test
    public void whenDeleteById_thenEmployeeShouldBeRemoved() {
        // given
        Employee employee = new Employee("Alice", "Johnson", "alice.j@example.com");
        entityManager.persistAndFlush(employee);

        // when
        employeeRepository.deleteById(employee.getId());
        Optional<Employee> found = employeeRepository.findById(employee.getId());

        // then
        Assertions.assertThat(found).isEmpty();
    }

    @Test
    public void whenUpdateEmployee_thenEmployeeShouldBeUpdated() {
        // given
        Employee employee = new Employee("Bob", "Brown", "bob.b@example.com");
        entityManager.persistAndFlush(employee);

        // when
        employee.setFirstName("Robert");
        employee.setEmail("robert.brown@example.com");
        Employee updated = employeeRepository.save(employee);

        // then
        Assertions.assertThat(updated.getFirstName()).isEqualTo("Robert");
        Assertions.assertThat(updated.getEmail()).isEqualTo("robert.brown@example.com");
    }
}