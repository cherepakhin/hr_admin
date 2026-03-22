package net.guides.springboot2.freemarker.controller;

import net.guides.springboot2.freemarker.model.Employee;
import net.guides.springboot2.freemarker.repository.EmployeeRepository;
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

import static java.lang.String.format;

@Controller
public class EmployeeController {
	private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);
	private String currentIndexPage = "/";
	@Autowired
	private EmployeeRepository employeeRepository;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String listEmployees(Model model,
								@RequestParam(defaultValue = "0") int page,
								@RequestParam(defaultValue = "10") int size) {
		refreshEmployees(model, page, size);

		log.info("/ ->" + currentIndexPage);
		currentIndexPage = "/";
		log.info("/ ->" + currentIndexPage);

		return "index";
	}

	@RequestMapping(value = "/employees/new", method = RequestMethod.GET)
	public String showCreateForm(Model model) {
		model.addAttribute("employee", new Employee());
		log.info("/employees/new: " + currentIndexPage);
		return "create_employee";
	}

	@RequestMapping(value = "/employees", method = RequestMethod.POST)
	public String createEmployee(@ModelAttribute Employee employee, Model model) {
		employeeRepository.save(employee);

		log.info("post /employees/:" + currentIndexPage);

		return "redirect:" + currentIndexPage;
	}

	@RequestMapping(value = "/employees/edit/{id}", method = RequestMethod.GET)
	public String showEditForm(@PathVariable("id") Long id, Model model) {
		Optional<Employee> optional = employeeRepository.findById(id);
		log.info("/employees/edit/{id}:" + currentIndexPage);
		if (optional.isPresent()) {
			model.addAttribute("employee", optional.get());
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
		log.info("/employees/update/{id}:" + currentIndexPage);
		log.info(currentIndexPage);
		if (!currentIndexPage.startsWith("/")) {
			currentIndexPage = "/" + currentIndexPage;
		}
		return "redirect:" + currentIndexPage;
	}

	@RequestMapping(value = "/employees/delete/{id}", method = RequestMethod.GET)
	public String deleteEmployee(@PathVariable("id") Long id, Model model) {
		employeeRepository.deleteById(id);
		log.info(format("/employees/delete/%s:", id) + currentIndexPage);
		log.info("currentIndexPage:" + currentIndexPage);
		if (currentIndexPage.equals("/")) {
			currentIndexPage = "";
		}
		return "redirect:/" + currentIndexPage;
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
	@RequestMapping(value = "/hideToolbar", method = RequestMethod.GET)
	public String hideToolbar() {
		log.info("hideToolbar");
		log.info(currentIndexPage);
		return currentIndexPage;
	}

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
		return "filter";
	}

	// Показать всех сотрудников с фильтрацией
	@GetMapping("/showEmployees")
	public String showAllEmployees(
			Model model,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) String firstName,
			@RequestParam(required = false) String lastName,
			@RequestParam(required = false) String email,
			@RequestParam(required = false) String sortField,
			@RequestParam(required = false) String direction
	) {
		log.info("firstName:" + firstName);
		log.info("lastName:" + lastName);
		log.info("email:" + email);
		log.info("sortField:" + sortField);
		log.info("direction:" + direction);

		// sort
		if (sortField == null || sortField.equals("")) {
			sortField = "id";
		}
		if (sortField == null || sortField.equals("n")) {
			sortField = "id";
		}
		if (sortField != null && sortField.equals("firstName")) {
			sortField = "firstName";
		}
		if (sortField != null && sortField.equals("lastName")) {
			sortField = "lastName";
		}
		if (sortField != null && sortField.equals("email")) {
			sortField = "email";
		}
		if (direction == null || direction.equals("")) {
			direction = "asc";
		}
		log.info("sortField:" + sortField);
		log.info("direction:" + direction);

		Sort sort = Sort.by(direction.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortField);
		Pageable pageable = PageRequest.of(page, size, sort);

		Page<Employee> employeePage = employeeRepository.findByFiltersAndSort(firstName, lastName, email, pageable);

		model.addAttribute("employees", employeePage.getContent());
		model.addAttribute("currentPage", employeePage.getNumber());
		model.addAttribute("totalPages", employeePage.getTotalPages());
		model.addAttribute("totalElements", employeePage.getTotalElements());

		// Передаём значения фильтров обратно в шаблон (для сохранения в форме при пагинации)
		model.addAttribute("firstName", firstName);
		model.addAttribute("lastName", lastName);
		model.addAttribute("email", email);

		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDirection", direction);

		return "showEmployees";
	}
}