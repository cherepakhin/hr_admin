package net.guides.springboot2.freemarker.initializer;

import net.guides.springboot2.freemarker.model.Employee;
import net.guides.springboot2.freemarker.model.Position;
import net.guides.springboot2.freemarker.repository.EmployeeRepository;
import net.guides.springboot2.freemarker.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private EmployeeRepository employeeRepository;

	@Autowired
	private PositionRepository positionRepository;

    @Override
    public void run(String... args) throws Exception {
		Position director = null;
		Position accounter = null;
		Position worker = null;
		if(positionRepository.count() == 0) {
			positionRepository.save(new Position(-1L, "-"));
			director = positionRepository.save(new Position(1L, "Директор"));
			accounter = positionRepository.save(new Position(2L, "Бухгалтер"));
			worker = positionRepository.save(new Position(3L, "Рабочий"));
		}
        // Проверяем, пуста ли таблица
        if (employeeRepository.count() == 0) {
            employeeRepository.save(new Employee("Иван", "Иванов", "ivan@example.com", director));
            employeeRepository.save(new Employee("Мария", "Петрова", "maria@example.com", accounter));
            employeeRepository.save(new Employee("Алексей", "Сидоров", "alex@example.com", director));
            employeeRepository.save(new Employee("Елена", "Кузнецова", "elena@example.com", accounter));

			for (int i = 0; i < 50; i++) {
				employeeRepository.save(new Employee("Name " + i, "Lastname " + i, format("emp%s@example.com", i), worker));
			}

            System.out.println("✅ Добавлены тестовые данные в базу.");
        } else {
            System.out.println("📊 База уже содержит данные. Инициализация пропущена.");
        }
    }
}