package net.guides.springboot2.freemarker.repository;

import net.guides.springboot2.freemarker.model.Employee;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;


@DataJpaTest
/*
С DataJpaTest программа будет загружена полностью
 */
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
        Optional<Employee> foundEmployee = employeeRepository.findById(employee.getId());

        // then
        Assertions.assertThat(foundEmployee).isPresent();
        Assertions.assertThat(foundEmployee.get().getFirstName()).isEqualTo("John");
        Assertions.assertThat(foundEmployee.get().getLastName()).isEqualTo("Doe");
        Assertions.assertThat(foundEmployee.get().getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    public void whenSaveEmployee_thenEmployeeShouldBePersisted() {
        // given
        Employee employee = new Employee("Jane", "Smith", "jane.smith@example.com");

        // when
        Employee savedEmployee = employeeRepository.save(employee);

        // then
        Assertions.assertThat(savedEmployee.getId()).isNotNull();
        Assertions.assertThat(savedEmployee.getFirstName()).isEqualTo("Jane");
        Assertions.assertThat(savedEmployee.getLastName()).isEqualTo("Smith");
        Assertions.assertThat(savedEmployee.getEmail()).isEqualTo("jane.smith@example.com");
    }

    @Test
    public void whenDeleteById_thenEmployeeShouldBeRemoved() {
        // given
        Employee employee = new Employee("Alice", "Johnson", "alice.j@example.com");
        entityManager.persistAndFlush(employee);

        // when
        employeeRepository.deleteById(employee.getId());
        Optional<Employee> optionalEmployee = employeeRepository.findById(employee.getId());

        // then
        Assertions.assertThat(optionalEmployee).isEmpty();
    }

    @Test
    public void whenUpdateEmployee_thenEmployeeShouldBeUpdated() {
        // given
        Employee employee = new Employee("Bob", "Brown", "bob.b@example.com");
        entityManager.persistAndFlush(employee);

        // when
        employee.setFirstName("Robert");
        employee.setEmail("robert.brown@example.com");
        Employee updatedEmployee = employeeRepository.save(employee);

        // then
        Assertions.assertThat(updatedEmployee.getFirstName()).isEqualTo("Robert");
        Assertions.assertThat(updatedEmployee.getEmail()).isEqualTo("robert.brown@example.com");
    }
}