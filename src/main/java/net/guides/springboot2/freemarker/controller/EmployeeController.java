package net.guides.springboot2.freemarker.controller;

import net.guides.springboot2.freemarker.model.Employee;
import net.guides.springboot2.freemarker.repository.EmployeeRepository;
import net.guides.springboot2.freemarker.repository.PositionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class EmployeeController {
	private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);
	private String currentIndexPage = "/";
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private PositionRepository positionRepository;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String listEmployees(Model model,
								@RequestParam(defaultValue = "0") int page,
								@RequestParam(defaultValue = "10") int size) {
		refreshEmployees(model, page, size);

		log.info("/ ->{}", currentIndexPage);
		currentIndexPage = "/";
		log.info("/ -> {}", currentIndexPage);

		return "index";
	}

	@RequestMapping(value = "/employees/new", method = RequestMethod.GET)
	public String showCreateForm(Model model) {
		model.addAttribute("employee", new Employee());
		model.addAttribute("positions", positionRepository.findAll());
		log.info("/employees/new: from page={}", currentIndexPage);
		return "create_employee";
	}

	@RequestMapping(value = "/employees", method = RequestMethod.POST)
	public String createEmployee(@ModelAttribute Employee employee, Model model) {
		employeeRepository.save(employee);

		log.info("post /employees/: from page={}", currentIndexPage);

		return "redirect:" + currentIndexPage;
	}

	@RequestMapping(value = "/employees/edit/{id}", method = RequestMethod.GET)
	public String showEditForm(@PathVariable("id") Long id, Model model) {
		Optional<Employee> optional = employeeRepository.findById(id);
		log.info("/employees/edit/{id}: from page={}", currentIndexPage);
		if (optional.isPresent()) {
			log.info("Edit employee = {}", optional.get());
			model.addAttribute("employee", optional.get());
			model.addAttribute("positions", positionRepository.findAll()); // ←
			model.addAttribute("prevPage", currentIndexPage);
			return "edit_employee";
		}
		throw new IllegalArgumentException("Employee not exist with id=" + id);
	}

	@RequestMapping(value = "/employees/update/{id}", method = RequestMethod.POST)
	public String updateEmployee(@PathVariable("id") Long id,
								 @ModelAttribute Employee employee,
								 Model model) {
		employee.setId(id);
		employeeRepository.save(employee);
		log.info("/employees/update/{}:", currentIndexPage);
		log.info(currentIndexPage);
		log.info("Update employee = {}", employee);
		if (!currentIndexPage.startsWith("/")) {
			currentIndexPage = "/" + currentIndexPage;
		}
		return "redirect:" + currentIndexPage;
	}

	@RequestMapping(value = "/employees/delete/{id}", method = RequestMethod.GET)
	public String deleteEmployee(@PathVariable("id") Long id, Model model) {
		employeeRepository.deleteById(id);
		log.info("/employees/delete/{}", id);
		log.info("currentIndexPage: {}", currentIndexPage);
		return "redirect:" + currentIndexPage;
	}

	/*
	@RequestMapping(value = "showEmployees", method = RequestMethod.GET)
	public String showAllEmployees(Model model,
								   @RequestParam(defaultValue = "0") int page,
								   @RequestParam(defaultValue = "10") int size) {

		refreshEmployees(model, page, size);
		log.info(currentIndexPage);
		currentIndexPage = "showEmployees";
		log.info("from /showEmployees:" + currentIndexPage);
		return "showEmployees";
	}
*/

	/*
	@RequestMapping(value = "/hideToolbar", method = RequestMethod.GET)
	public String hideToolbar() {
		log.info("hideToolbar");
		log.info(currentIndexPage);
		return currentIndexPage;
	}
   */

	private void refreshEmployees(Model model, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Employee> employeePage = employeeRepository.findAll(pageable);
		model.addAttribute("employees", employeePage.getContent());
		model.addAttribute("currentPage", employeePage.getNumber());
		model.addAttribute("totalPages", employeePage.getTotalPages());
		model.addAttribute("totalElements", employeePage.getTotalElements());
	}

	// Отображение формы фильтра
	@GetMapping("/filter")
	public String showFilterPage(Model model) {
		model.addAttribute("positions", positionRepository.findAll() );
		return "filter";
	}

	// Показать всех сотрудников с фильтрацией
	@GetMapping("/show_employees")
	public String showAllEmployees(
			Model model,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) String firstName,
			@RequestParam(required = false) String lastName,
			@RequestParam(required = false) String email,
			@RequestParam(required = false) Long positionId,
			@RequestParam(required = false) String sortField,
			@RequestParam(required = false) String direction
	) {
		log.info("Page show_employees");
		log.info("firstName: {}", firstName);
		log.info("lastName: {}", lastName);
		log.info("email: {}", email);
		log.info("positionId: {}", positionId);
		log.info("sortField: {}", sortField);
		log.info("direction: {}", direction);

		currentIndexPage = "/show_employees";

		// Сортировка
		if (sortField == null || sortField.isEmpty()) {
			sortField = "id";
		}
		if (direction == null || direction.isEmpty()) {
			direction = "asc";
		}

		Sort sort = Sort.by(direction.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortField);
		Pageable pageable = PageRequest.of(page, size, sort);
		log.info(pageable.toString());
		// Передаём positionId в репозиторий
		Page<Employee> employeePage = employeeRepository.findByFiltersAndSort(
				firstName, lastName, email, positionId, pageable);

		model.addAttribute("employees", employeePage.getContent());
		model.addAttribute("currentPage", employeePage.getNumber());
		model.addAttribute("totalPages", employeePage.getTotalPages());
		model.addAttribute("totalElements", employeePage.getTotalElements());

		// Сохраняем значения фильтров
		model.addAttribute("firstName", firstName);
		model.addAttribute("lastName", lastName);
		model.addAttribute("email", email);
		model.addAttribute("positionId", positionId); // ← важно

		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDirection", direction);

		return "show_employees";
	}
}