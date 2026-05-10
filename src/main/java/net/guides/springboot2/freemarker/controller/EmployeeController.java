package net.guides.springboot2.freemarker.controller;

import jakarta.validation.Valid;
import net.guides.springboot2.freemarker.model.Employee;
import net.guides.springboot2.freemarker.model.Position;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//TODO: добавить валидацию
// Методы могут отдавать, либо ModelAndView, либо просто строку
@Controller
@RequestMapping(value = "/employees")
public class EmployeeController {
	private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);
	private String currentIndexPage = "/";

	private static final  String defaultSortField = Fields.ID;

	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private PositionRepository positionRepository;

	@GetMapping("/")
	public String listEmployees(Model model,
								@RequestParam(defaultValue = "0") int page,
								@RequestParam(defaultValue = "10") int size,
								@RequestParam(defaultValue = "id") String sortField,
								@RequestParam(defaultValue = "asc") String direction

	) {
		log.info("listEmployees. page: {}, sortField: {}, direction: {}", page, sortField, direction);
		this.refreshEmployees(model, page, size, sortField, direction);

		/*
		if (currentIndexPage.equals("/show_employees")) {
			currentIndexPage = "/employees" + currentIndexPage;
			log.info("New currentIndexPage:{}", currentIndexPage);
		} else {
			currentIndexPage = "/employees/index";
		}
		*/
		log.info("/employees");
		log.info("/ -> {}", currentIndexPage);
		currentIndexPage = "/";
		return "index";
	}

	@GetMapping("/new")
	public String showCreateForm(Model model) {
		log.info("showCreateForm");
		model.addAttribute("employee", new Employee());
		model.addAttribute("positions", positionRepository.findAll());
		log.info("/employees/new: from page={}", currentIndexPage);
		return NamesView.CREATE_EMPLOYEE;
	}

/* Пример валидации из https://sky.pro/wiki/java/validatsiya-form-v-spring-mvc-bez-hibernate-luchshiy-metod/
	@PostMapping("/submit")
	public String submitForm(@Valid @ModelAttribute("formData") FormData formData, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "formPage";
		}
		// Ваша бизнес-логика находится здесь.
		return "successPage";
	}
*/
	@PostMapping("/")
	public String createEmployee(@Valid @ModelAttribute Employee employee,
								 BindingResult bindingResult,  Model model) {
		if (bindingResult.hasErrors()) {
			log.info("Binding result: {}", bindingResult);
			StringBuilder errors = new StringBuilder();
			for(ObjectError error :  bindingResult.getAllErrors()) {
				errors.append(error.getDefaultMessage()).append("\n");
				log.error(error.getDefaultMessage());
			}
			// find in create_position.ftlh by name="name" (<input ... name="name")
			model.addAttribute("firstName", employee.getFirstName());
			model.addAttribute("error", errors.toString()); // <p class="text-red-600 text-xs mt-1">${error}</p>
			return NamesView.CREATE_EMPLOYEE;
		}
		log.info("createEmployee {}:", employee);
		employeeRepository.save(employee);

		log.info("post /employees/: from page={}", currentIndexPage);

		return "redirect:" + currentIndexPage;
	}

	@PostMapping("")
	public String createEmployeeForEmpty(@ModelAttribute Employee employee) {
		return "redirect:" + "/employees/";
	}

	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable("id") Long id, Model model) {
		log.info("showEditForm");
		Optional<Employee> optional = employeeRepository.findById(id);
		log.info("/employees/edit/{id}: from page={}", currentIndexPage);
		if (optional.isPresent()) {
			log.info("Edit employee = {}", optional.get());
			model.addAttribute("employee", optional.get());
			model.addAttribute("positions", positionRepository.findAll());
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

	@PostMapping("/update/{id}")
	public String updateEmployee(@PathVariable("id") Long id,
								 @ModelAttribute Employee employee,
								 Model model) {
		log.info("updateEmployee");
		employee.setId(id);
		employeeRepository.save(employee);
		log.info("/employees/update/ from currentIndexPage:{}", currentIndexPage);
		log.info("Update employee = {}", employee);
		if (currentIndexPage.equals("/show_employees")) {
			currentIndexPage = "/employees" + currentIndexPage;
			log.info("New currentIndexPage:{}", currentIndexPage);
		}

		log.info("redirect:{}", currentIndexPage);
		return "redirect:" + currentIndexPage;
	}

	@GetMapping("/delete/{id}")
	public ModelAndView deletePosition(@PathVariable Long id, Model model) {
		// TODO: проверка на существование
		// if(id == null) {
		//	throw new Exception("id is null for /delete/{id}");
		// }
		log.info("deleteEmployee");
		employeeRepository.deleteById(id);
		log.info("/employees/delete/{}", id);
		log.info("currentIndexPage: {}", currentIndexPage);
		if (currentIndexPage.contains("/show_employees")) {
			ModelAndView mv = new ModelAndView(NamesView.SHOW_EMPLOYEES);
			mv.clear();
			mv.setViewName("redirect:/" + NamesView.SHOW_EMPLOYEES + "/");
			return mv;
		} else {
			ModelAndView mv = new ModelAndView(NamesView.SHOW_EMPLOYEES);
			mv.clear();
			mv.setViewName("redirect:/" + NamesView.INDEX + "/");
			return mv;
		}
	}

	protected void refreshEmployees(Model model, int page, int size, String sortField, String direction) {
		if (sortField.isEmpty()) {
			sortField = defaultSortField;
		}

		Sort.Direction directionSort;
		if (direction.equals(Direction.DESC)) {
			directionSort = Sort.Direction.DESC;
		} else {
			directionSort = Sort.Direction.ASC;
		}

		log.info("refreshEmployees.sortField: {}", sortField);
		log.info("refreshEmployees.directionSort: {}", directionSort);
		Sort sort = Sort.by(directionSort, sortField);
		Pageable pageable = PageRequest.of(page, size, sort);
		Page<Employee> employeePage = employeeRepository.findAll(pageable);
		model.addAttribute("employees", employeePage.getContent());
		model.addAttribute("sortField", sortField);
		model.addAttribute("direction", directionSort);
		model.addAttribute("currentPage", employeePage.getNumber());
		model.addAttribute("totalPages", employeePage.getTotalPages());
		model.addAttribute("totalElements", employeePage.getTotalElements());
	}

	// Отображение формы фильтра
	@GetMapping("/filter")
	public String showFilterPage(Model model) {
		log.info("showFilterPage");
		model.addAttribute("positions", positionRepository.findAll());
		return NamesView.FILTER_EMPLOYEE;
	}

	// Показать всех сотрудников с фильтрацией
	@GetMapping("/show_employees")
	public String showAllEmployees(
			Model model,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "") String firstName,
			@RequestParam(required = false, defaultValue = "") String lastName,
			@RequestParam(required = false, defaultValue = "") String email,
			@RequestParam(required = false, defaultValue = "-1") Long positionId,
			@RequestParam(required = false, defaultValue = "lastName") String sortField,
			@RequestParam(required = false, defaultValue = "asc") String direction
	) {
		log.info("showAllEmployees");
		log.info("firstName: {}", firstName);
		log.info("lastName: {}", lastName);
		log.info("email: {}", email);
		log.info("positionId: {}", positionId);
		log.info("sortField: {}", sortField);
		log.info("direction: {}", direction);

		currentIndexPage = "/" + NamesView.SHOW_EMPLOYEES;

		// Сортировка. Уже не нужно, т.е. определено в параметрах и вообще вся логика уже есть в форме
		//if (sortField == null || sortField.isEmpty()) {
		//	sortField = Fields.ID;
		//	log.info("SET sortField: {}", sortField);
		//}

		// default defined in param
		//if (direction == null || direction.isEmpty()) {
		//	direction = Direction.ASC;
		//	log.info("SET DEFAULT direction: {}", direction);
		//}
		// TODO: В запрос включены все должности, если не задан поиск по конкретной, хочется динамический sql
		// при этом sql запрос будет position_id in (все id должностей)
		// динамический sql в планах, см. как сделать в проекте для МТС
		List<Long> positions = positionRepository.findAll().stream().map(Position::getId).collect(Collectors.toList());
		log.debug("positions: {}", positions);
		// если задана позиция, то в списке должностей оставить только ее
		// иначе выбирать по всем позициям
		if (!positionId.equals(-1L)) {
			positions = positions.stream().filter(p -> p.equals(positionId)).toList();
		}
		// for position sort by 'name'
		if(sortField.equals("position")) {
			sortField ="position.name";
		}
		// define sort direction by field
		log.info("sortField: {}", sortField);
		log.info("direction: {}", direction);
		// direction.equals("desc") - значения "desc" или "asc" - задано в форме
		Sort sort = Sort.by(direction.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortField);
		Pageable pageable = PageRequest.of(page, size, sort);
		log.info("pageable:");
		log.info(pageable.toString());
		Page<Employee> employeePage = employeeRepository.findByFiltersAndSort(
				firstName, lastName, positions, email, pageable);

		model.addAttribute("employees", employeePage.getContent());
		model.addAttribute("currentPage", employeePage.getNumber());
		model.addAttribute("totalPages", employeePage.getTotalPages());
		model.addAttribute("totalElements", employeePage.getTotalElements());

		model.addAttribute("firstName", firstName);
		model.addAttribute("lastName", lastName);
		model.addAttribute("email", email);

		model.addAttribute("sortField", sortField);
		model.addAttribute("direction", direction);

		return NamesView.SHOW_EMPLOYEES;
	}

	public EmployeeRepository getEmployeeRepository() {
		return employeeRepository;
	}

	public void setEmployeeRepository(@Autowired EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	public PositionRepository getPositionRepository() {
		return positionRepository;
	}

	public void setPositionRepository(@Autowired PositionRepository positionRepository) {
		this.positionRepository = positionRepository;
	}
}
