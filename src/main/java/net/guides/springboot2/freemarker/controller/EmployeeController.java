package net.guides.springboot2.freemarker.controller;

import net.guides.springboot2.freemarker.model.Employee;
import net.guides.springboot2.freemarker.repository.EmployeeRepository;
import net.guides.springboot2.freemarker.repository.PositionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(value = "/employees")
public class EmployeeController {
	private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);
	private String currentIndexPage = "/";

	private String sortField = Fields.ID;
	private String direction = Direction.ASC;

	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private PositionRepository positionRepository;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String listEmployees(Model model,
								@RequestParam(defaultValue = "0") int page,
								@RequestParam(defaultValue = "10") int size,
								@RequestParam(defaultValue = "id") String sortField,
								@RequestParam(defaultValue = "asc") String direction

								) {
		log.info("listEmployees");

		if(!sortField.isEmpty()) {
			this.sortField = sortField;
		} else {
			this.sortField = Fields.ID;
		}
		if(!direction.isEmpty()) {
			this.direction = direction;
		} else {
			this.direction = Direction.ASC;
		}

		log.info("page: {}, sortField: {}, direction: {}", page, sortField, direction);
		this.
		refreshEmployees(model, page, size, sortField, direction);

		log.info("/ ->{}", currentIndexPage);
		currentIndexPage = "/";
		log.info("/ -> {}", currentIndexPage);

		return NamesView.INDEX;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String showCreateForm(Model model) {
		log.info("showCreateForm");
		model.addAttribute("employee", new Employee());
		model.addAttribute("positions", positionRepository.findAll());
		log.info("/employees/new: from page={}", currentIndexPage);
		return NamesView.CREATE_EMPLOYEE;
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String createEmployee(@ModelAttribute Employee employee) {
		log.info("createEmployee");
		employeeRepository.save(employee);

		log.info("post /employees/: from page={}", currentIndexPage);

		return "redirect:" + currentIndexPage;
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String showEditForm(@PathVariable("id") Long id, Model model) {
		log.info("showEditForm");
		Optional<Employee> optional = employeeRepository.findById(id);
		log.info("/employees/edit/{id}: from page={}", currentIndexPage);
		if (optional.isPresent()) {
			log.info("Edit employee = {}", optional.get());
			model.addAttribute("employee", optional.get());
			model.addAttribute("positions", positionRepository.findAll()); // ←
			model.addAttribute("prevPage", currentIndexPage);
			return NamesView.EDIT_EMPLOYEE;
		}
		throw new IllegalArgumentException("Employee not exist with id=" + id);
	}

	// POST from edit_employee.ftl:
    //    <form action="/employees/update/${employee.id}" method="post">
    //        <input type="hidden" name="id" value="${employee.id}" />
    //        <div>
    //            <label >Имя</label>
    //            <input name="firstName" value="${employee.firstName}" /> <-- field NAME link to  = "firstName" (employee.firstName)
    //        </div>

	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public String updateEmployee(@PathVariable("id") Long id,
								 @ModelAttribute Employee employee,
								 Model model) {
		log.info("updateEmployee");
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

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String deleteEmployee(@PathVariable("id") Long id) {
		log.info("deleteEmployee");
		employeeRepository.deleteById(id);
		log.info("/employees/delete/{}", id);
		log.info("currentIndexPage: {}", currentIndexPage);
		return "redirect:" + currentIndexPage;
	}

	private void refreshEmployees(Model model, int page, int size, String sortField, String direction) {
		if(!sortField.isEmpty()) {
			this.sortField = sortField;
		} else {
			this.sortField = Fields.ID;
		}

		if(!direction.isEmpty()) {
			this.direction = direction;
		} else {
			this.direction = Direction.ASC;
		}

		Sort.Direction directionSort;
		if (this.direction.equals(Direction.DESC)) {
			directionSort = Sort.Direction.DESC;
		} else {
			directionSort = Sort.Direction.ASC;
		}

		log.info("refreshEmployees");
		Sort sort = Sort.by(directionSort, sortField);
		Pageable pageable = PageRequest.of(page, size, sort);
		Page<Employee> employeePage = employeeRepository.findAll(pageable);
		model.addAttribute("employees", employeePage.getContent());
		model.addAttribute("sortField", sortField);
		model.addAttribute("direction", direction);
		model.addAttribute("currentPage", employeePage.getNumber());
		model.addAttribute("totalPages", employeePage.getTotalPages());
		model.addAttribute("totalElements", employeePage.getTotalElements());
	}

	// Отображение формы фильтра
	@RequestMapping(value = "/filter", method = RequestMethod.GET)
	public String showFilterPage(Model model) {
		log.info("showFilterPage");
		model.addAttribute("positions", positionRepository.findAll() );
		return NamesView.FILTER_EMPLOYEE;
	}

	// Показать всех сотрудников с фильтрацией
	@RequestMapping(value = "/show_employees", method = RequestMethod.GET)
	public String showAllEmployees(
			Model model,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "") String firstName,
			@RequestParam(required = false, defaultValue = "") String lastName,
			@RequestParam(required = false, defaultValue = "") String email,
			@RequestParam(required = false, defaultValue = "") String sortField,
			@RequestParam(required = false, defaultValue = "") String direction
	) {
		log.info("showAllEmployees");
		log.info("firstName: {}", firstName);
		log.info("lastName: {}", lastName);
		log.info("email: {}", email);
		log.info("sortField: {}", sortField);
		log.info("direction: {}", direction);

		currentIndexPage = "/" + NamesView.SHOW_EMPLOYEES;

		// Сортировка
		if (sortField == null || sortField.isEmpty()) {
			sortField = Fields.ID;
			log.info("SET sortField: {}", sortField);
		}
		if (direction == null || direction.isEmpty()) {
			direction = Direction.ASC;
			log.info("SET direction: {}", direction);
		}

		// define sort direction by field
		Sort sort = Sort.by(direction.equals(Direction.ASC) ? Sort.Direction.ASC : Sort.Direction.DESC, sortField);
		Pageable pageable = PageRequest.of(page, size, sort);
		log.info(pageable.toString());
		Page<Employee> employeePage = employeeRepository.findByFiltersAndSort(
				firstName, lastName, email, pageable);

		model.addAttribute("employees", employeePage.getContent());
		model.addAttribute("currentPage", employeePage.getNumber());
		model.addAttribute("totalPages", employeePage.getTotalPages());
		model.addAttribute("totalElements", employeePage.getTotalElements());

		model.addAttribute("firstName", firstName);
		model.addAttribute("lastName", lastName);
		model.addAttribute("email", email);

		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDirection", direction);

		return NamesView.SHOW_EMPLOYEES;
	}
}
