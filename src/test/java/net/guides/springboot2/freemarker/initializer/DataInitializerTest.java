package net.guides.springboot2.freemarker.initializer;

import net.guides.springboot2.freemarker.model.Employee;
import net.guides.springboot2.freemarker.model.Position;
import net.guides.springboot2.freemarker.repository.EmployeeRepository;
import net.guides.springboot2.freemarker.repository.PositionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(DataInitializer.class)
public class DataInitializerTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Test
    public void initializePositions() {
        long positionCount = positionRepository.count();

		assertThat(positionCount).isEqualTo(4);

		assertThat(positionRepository.findById(1L)).isPresent();
		assertThat(positionRepository.findById(2L)).isPresent();
		assertThat(positionRepository.findById(3L)).isPresent();

		assertThat(positionRepository.findById(1L).get().getName()).isEqualTo("Директор");
		assertThat(positionRepository.findById(2L).get().getName()).isEqualTo("Бухгалтер");
		assertThat(positionRepository.findById(3L).get().getName()).isEqualTo("Рабочий");

//        assertThat(positionRepository.findById(-1L).get().getName()).isEqualTo("-");
    }

    @Test
    public void shouldInitializeEmployeesWhenEmpty() {
        long employeeCount = employeeRepository.count();
        Iterable<Employee> allEmployees = employeeRepository.findAll();

        assertThat(employeeCount).isEqualTo(54); // 4 + 50

        Employee ivanov = employeeRepository.findByFirstNameAndLastName("Иван", "Иванов").orElse(null);
        Employee petrova = employeeRepository.findByFirstNameAndLastName("Мария", "Петрова").orElse(null);
        Employee sidorov = employeeRepository.findByFirstNameAndLastName("Алексей", "Сидоров").orElse(null);
        Employee kuznecova = employeeRepository.findByFirstNameAndLastName("Елена", "Кузнецова").orElse(null);

        assertThat(ivanov).isNotNull();
        assertThat(petrova).isNotNull();
        assertThat(sidorov).isNotNull();
        assertThat(kuznecova).isNotNull();

        assertThat(ivanov.getPosition().getName()).isEqualTo("Директор");
        assertThat(petrova.getPosition().getName()).isEqualTo("Бухгалтер");
        assertThat(sidorov.getPosition().getName()).isEqualTo("Директор");
        assertThat(kuznecova.getPosition().getName()).isEqualTo("Бухгалтер");

        Employee maria = employeeRepository.findByEmail("maria@example.com").orElse(null);

		assertThat(maria).isNotNull();
		assertThat(maria.getFirstName()).isEqualTo("Мария");
		assertThat(maria.getLastName()).isEqualTo("Петрова");
		assertThat(maria.getEmail()).isEqualTo("maria@example.com");
        assertThat(maria.getPosition().getName()).isEqualTo("Бухгалтер");
    }

    @Test
    public void shouldNotReinitializeDataWhenPositionsExist() {
        positionRepository.save(new Position(99L, "Test Position"));

        long countAfterInit = positionRepository.count();

        // Then
        assertThat(countAfterInit).isEqualTo(5);
    }

    @Test
    public void saveEmployeeWithNewPosition() {
        Position pos = new Position();
		pos.setId(positionRepository.getNextId());
        pos.setName("Developer");
        positionRepository.save(pos);

        employeeRepository.save(new Employee("Test", "User", "test@example.com", pos));

        long employeeCount = employeeRepository.count();

        assertThat(employeeCount).isEqualTo(55L);
    }
}