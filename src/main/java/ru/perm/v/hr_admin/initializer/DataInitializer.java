package ru.perm.v.hr_admin.initializer;

import ru.perm.v.hr_admin.model.Employee;
import ru.perm.v.hr_admin.model.Position;
import ru.perm.v.hr_admin.repository.EmployeeRepository;
import ru.perm.v.hr_admin.repository.PositionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@SuppressWarnings("unused")
@Component
public class DataInitializer implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private PositionRepository positionRepository;

	@Override
	public void run(String... args) {
		Position nullPosition = null;
		Position director = null;
		Position accounter = null;
		Position worker = null;
		Position forTest = null;
		if (this.positionRepository.count() == 0) {
			log.info("Initialize positions");
			nullPosition = this.positionRepository.save(new Position(-1L, "------"));
			log.info("Saved nullPosition: {}", nullPosition );
			director = this.positionRepository.save(new Position(1L, "Директор"));
			log.info("Saved director: {}", director );
			accounter = this.positionRepository.save(new Position(2L, "Бухгалтер"));
			log.info("Saved accounter: {}", accounter );
			worker = this.positionRepository.save(new Position(3L, "Рабочий"));
			log.info("Saved worker: {}", worker );
			forTest = this.positionRepository.save(new Position(4L, "Для тестов"));
			log.info("Saved forTest: {}", forTest );
		}

		if (this.employeeRepository.count() == 0) {
			log.info("Initialize employees");
			this.employeeRepository.save(new Employee("Иван", "Иванов", "ivan@example.com", director));
			this.employeeRepository.save(new Employee("Мария", "Петрова", "maria@example.com", accounter));
			this.employeeRepository.save(new Employee("Алексей", "Сидоров", "alex@example.com", director));
			this.employeeRepository.save(new Employee("Елена", "Кузнецова", "elena@example.com", accounter));

			for (int i = 0; i < 10; i++) {
				this.employeeRepository.save(new Employee("Name " + i, "Lastname " + i, format("emp%s@example.com", i), worker));
			}
			log.info("Test data added.");
		} else {
			log.info("The database already contains data. Initialization has been skipped.");
		}

	}
}