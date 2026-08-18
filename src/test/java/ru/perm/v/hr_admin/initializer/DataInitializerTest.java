package ru.perm.v.hr_admin.initializer;

import ru.perm.v.hr_admin.model.Employee;
import ru.perm.v.hr_admin.model.Position;
import ru.perm.v.hr_admin.repository.EmployeeRepository;
import ru.perm.v.hr_admin.repository.PositionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
        long positionCount = this.positionRepository.count();

		assertThat(positionCount).isEqualTo(4);

		assertThat(this.positionRepository.findById(1L)).isPresent();
		assertThat(this.positionRepository.findById(2L)).isPresent();
		assertThat(this.positionRepository.findById(3L)).isPresent();
		assertThat(this.positionRepository.findById(4L)).isPresent();

		assertThat(this.positionRepository.findById(1L).get().getName()).isEqualTo("Директор");
		assertThat(this.positionRepository.findById(2L).get().getName()).isEqualTo("Бухгалтер");
		assertThat(this.positionRepository.findById(3L).get().getName()).isEqualTo("Рабочий");
        assertThat(this.positionRepository.findById(3L).get().getName()).isEqualTo("Рабочий");
    }

    @Test
    public void shouldInitializeEmployeesWhenEmpty() {
        long employeeCount = this.employeeRepository.count();
        Iterable<Employee> allEmployees = this.employeeRepository.findAll();

        assertThat(employeeCount).isEqualTo(14);
        assertThat(allEmployees).hasSize(14);

        Employee ivanov = this.employeeRepository.findByFirstNameAndLastName("Иван", "Иванов").orElse(null);
        Employee petrova = this.employeeRepository.findByFirstNameAndLastName("Мария", "Петрова").orElse(null);
        Employee sidorov = this.employeeRepository.findByFirstNameAndLastName("Алексей", "Сидоров").orElse(null);
        Employee kuznecova = this.employeeRepository.findByFirstNameAndLastName("Елена", "Кузнецова").orElse(null);

        assertThat(ivanov).isNotNull();
        assertThat(petrova).isNotNull();
        assertThat(sidorov).isNotNull();
        assertThat(kuznecova).isNotNull();

        assertThat(ivanov.getPosition().getName()).isEqualTo("Директор");
        assertThat(petrova.getPosition().getName()).isEqualTo("Бухгалтер");
        assertThat(sidorov.getPosition().getName()).isEqualTo("Директор");
        assertThat(kuznecova.getPosition().getName()).isEqualTo("Бухгалтер");

        Employee maria = this.employeeRepository.findByEmail("maria@example.com").orElse(null);

		assertThat(maria).isNotNull();
		assertThat(maria.getFirstName()).isEqualTo("Мария");
		assertThat(maria.getLastName()).isEqualTo("Петрова");
		assertThat(maria.getEmail()).isEqualTo("maria@example.com");
        assertThat(maria.getPosition().getName()).isEqualTo("Бухгалтер");
    }

    @Test
    public void shouldNotReinitializeDataWhenPositionsExist() {
        this.positionRepository.save(new Position(99L, "Test Position"));

        long countAfterInit = this.positionRepository.count();

        assertThat(countAfterInit).isEqualTo(5);
    }

    @Test
    public void saveEmployeeWithNewPosition() {
        Position pos = new Position();
		pos.setId(this.positionRepository.getNextId());
        pos.setName("Developer");
        pos = this.positionRepository.save(pos);

        this.employeeRepository.save(new Employee("Test", "User", "test@example.com", pos));

        long employeeCount = this.employeeRepository.count();

        assertThat(employeeCount).isEqualTo(15L);
    }
}