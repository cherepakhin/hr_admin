package net.guides.springboot2.freemarker.initializer;

import net.guides.springboot2.freemarker.model.Employee;
import net.guides.springboot2.freemarker.model.Position;
import net.guides.springboot2.freemarker.repository.EmployeeRepository;
import net.guides.springboot2.freemarker.repository.PositionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
public class DataInitializer implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private PositionRepository positionRepository;

	@Override
	public void run(String... args) throws Exception {
		Position nullPosition = null;
		Position director = null;
		Position accounter = null;
		Position worker = null;
		if (positionRepository.count() == 0) {
			log.info("Initialize positions");
			nullPosition = positionRepository.save(new Position(-1L, "-"));
			log.info("Saved: {}", nullPosition );
			director = positionRepository.save(new Position(1L, "Директор"));
			log.info("Saved: {}", director );
			accounter = positionRepository.save(new Position(2L, "Бухгалтер"));
			log.info("Saved: {}", accounter );
			worker = positionRepository.save(new Position(3L, "Рабочий"));
			log.info("Saved: {}", worker );
		}

		if (employeeRepository.count() == 0) {
			log.info("Initialize employees");
			employeeRepository.save(new Employee("Иван", "Иванов", "ivan@example.com", director));
			employeeRepository.save(new Employee("Мария", "Петрова", "maria@example.com", accounter));
			employeeRepository.save(new Employee("Алексей", "Сидоров", "alex@example.com", director));
			employeeRepository.save(new Employee("Елена", "Кузнецова", "elena@example.com", accounter));

			for (int i = 0; i < 10; i++) {
				employeeRepository.save(new Employee("Name " + i, "Lastname " + i, format("emp%s@example.com", i), worker));
			}
			log.info("Test data added.");
		} else {
			log.info("The database already contains data. Initialization has been skipped.");
		}

	}
}