package net.guides.springboot2.freemarker.initializer;

import net.guides.springboot2.freemarker.model.Employee;
import net.guides.springboot2.freemarker.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public void run(String... args) throws Exception {
        // Проверяем, пуста ли таблица
        if (employeeRepository.count() == 0) {
            employeeRepository.save(new Employee("Иван", "Иванов", "ivan@example.com"));
            employeeRepository.save(new Employee("Мария", "Петрова", "maria@example.com"));
            employeeRepository.save(new Employee("Алексей", "Сидоров", "alex@example.com"));
            employeeRepository.save(new Employee("Елена", "Кузнецова", "elena@example.com"));

			for (int i = 0; i < 50; i++) {
				employeeRepository.save(new Employee("Name " + i, "Lastname " + i, format("emp%s@example.com", i)));
			}

            System.out.println("✅ Добавлены тестовые данные в базу.");
        } else {
            System.out.println("📊 База уже содержит данные. Инициализация пропущена.");
        }
    }
}