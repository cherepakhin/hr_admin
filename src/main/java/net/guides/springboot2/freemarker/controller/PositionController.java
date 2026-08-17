package net.guides.springboot2.freemarker.controller;

import jakarta.validation.*;
import jakarta.validation.constraints.Pattern;
import net.guides.springboot2.freemarker.model.Position;
import net.guides.springboot2.freemarker.repository.EmployeeRepository;
import net.guides.springboot2.freemarker.repository.PositionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Validated // добавляем валидацию
@Controller
@RequestMapping("/positions")
public class PositionController {

	private static final Logger log = LoggerFactory.getLogger(PositionController.class);

	private final PositionRepository positionRepository;
	private final EmployeeRepository employeeRepository;

	private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	// Используем внедрение через конструктор (рекомендуется)
	@Autowired
	public PositionController(PositionRepository positionRepository, EmployeeRepository employeeRepository) {
		this.positionRepository = positionRepository;
		this.employeeRepository=employeeRepository;
	}

	// GET /positions/ - отображение списка
	@RequestMapping("/")
	public String listPositions(Model model,
								@RequestParam(defaultValue = "name")
								@Pattern(regexp = "^(id|name)$", message = "Направление должно быть 'id' или 'name'")
								String sortField,

								@RequestParam(defaultValue = "asc")
								@Pattern(regexp = "^(asc|desc)$", message = "Направление должно быть 'asc' или 'desc'")
								String direction) {
		log.info("get all positions");
		log.info("sortField {}", sortField);
		log.info("direction {}", direction);
//		List<Position> positions = positionRepository.findAll();
		Sort sort;
		if(direction.equals("desc")) {
			sort = Sort.by(sortField).descending();
		} else {
			sort = Sort.by(sortField).ascending();
		}

		List<Position> positions = positionRepository.findAllAndSort(sort);
		if(positions == null || positions.isEmpty()) {
			positions = new ArrayList<>();
		}
		positions.forEach(p-> log.info(p.toString()));

		//ModelAndView mv = new ModelAndView();
		//mv.setViewName(NamesView.POSITIONS);
		model.addAttribute("positions", positions);
		model.addAttribute("sortField", sortField); // для указания поля по которому сортируем
		model.addAttribute("direction", direction); // для указания направления сортировки
		return "show_positions";
		// return mv;
	}

	// GET /positions/new - форма создания
	@RequestMapping("/new")
	public String showCreateForm(Model model) {
		Position position = new Position();
		position.setName(""); // значение по умолчанию
		model.addAttribute("position", position);
		return NamesView.CREATE_POSITION;
	}

	// POST /positions/ - обработка создания
	@PostMapping("/")
	public String createPosition(@ModelAttribute("position") Position position,
								 BindingResult bindingResult,  Model model) {
		log.info("Create position: {}", position);
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<Position>> violations = validator.validate(position);
		if(!violations.isEmpty()) {
			String errors = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", "));
			log.info("Binding result: {}", bindingResult);
			// find in create_position.ftlh by name="name" (<input ... name="name")
			model.addAttribute("name", position.getName());
			model.addAttribute("error_for_name", errors); // <p class="text-red-600 text-xs mt-1">${error}</p>
			return NamesView.CREATE_POSITION;
		}
		if (positionRepository.existsByName(position.getName())) {
			log.error(String.format("Должность с названием '%s' уже существует.", position.getName()));
			model.addAttribute("name", position.getName());
			model.addAttribute("error_for_name", "Должность с таким названием уже существует.");
			return NamesView.CREATE_POSITION;
		}
		position.setId(positionRepository.getNextId());
		positionRepository.save(position);
		return "redirect:/positions/";
	}

	// GET /positions/edit/{id} - вызов формы для редактирования
	@RequestMapping("/edit/{id}")
	public String showEditForm(@PathVariable Long id, Model model) {
		Position position = positionRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid position ID: " + id));
		model.addAttribute("position", position);
		return NamesView.EDIT_POSITION;
	}

	// POST /positions/update/{id} - обработка обновления
	@PostMapping("/update/{id}")
	public String updatePosition(@PathVariable Long id,
								 @ModelAttribute Position position,
								 Model model) {
		log.info("Update position: {}", position);
		// Первый УРОВЕНЬ проверки - обработка ошибок @Size и т.п.
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<Position>> violations = validator.validate(position);
		if(!violations.isEmpty()) {
			String errors = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", "));
			model.addAttribute("name", position.getName());
			model.addAttribute("error_for_name", errors);
			position.setId(id); // Восстановление id для формы
			return NamesView.EDIT_POSITION;
		}
		// Второй УРОВЕНЬ проверки. Backend проверка.
		// (existsByName для этого метода не очень актуален, более актуален для ввода новой должности, оставлен для демонстрации)
		if (positionRepository.existsByName(position.getName())) {
			// Обработка backend ошибки
			log.error("Position with name {} already exists", position.getName());
			model.addAttribute("name", position.getName());
			model.addAttribute("error_for_name", "Должность с таким названием УЖЕ существует.");
			// Другой способ сообщения об ошибке:
			// Метод rejectValue() — мощный инструмент для реализации сложной бизнес-логики валидации.
			// bindingResult.rejectValue("name", "error_for_name", "Должность с таким названием УЖЕ существует.");
			position.setId(id); // Восстановление id для формы
			return NamesView.EDIT_POSITION;
		}
		position.setId(id);
		positionRepository.save(position);
		return "redirect:/" + NamesView.POSITIONS + "/";
	}

	// GET /positions/delete/{id} - удаление

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String deletePosition(@PathVariable Long id) {
		// Проверка на возможность удаления уже сделана в форме и в PositionRestController и здесь не делается
		// через const response = await fetch("${springMacroRequestContext.contextPath}/rest/positions/can_delete/" + id);
		// PositionRestController это REST контроллер, выделен в отдельный класс, т.к. он не связан со Spring MVC
		positionRepository.sqlDeleteById(id);

		// Вместо простого редиректа, добавляем уникальный параметр для отключения кеша браузера
		// Здесь не используется, т.к. сделано через "redirect", но оставил как напоминание
		// UUID.randomUUID().toString() создаст случайную строку типа "a1b2-c3d4..."
		//String redirectUrl = "/" + NamesView.POSITIONS + "/?v==" + UUID.randomUUID().toString();
		//return redirectUrl;
		//mv.addreturn listPositions(model);
		//mv.setViewName("redirect:/" +NamesView.POSITIONS +"/");
		log.info("get all positions");
//		List<Position> positions = positionRepository.findAll();
//		positions.forEach(p-> log.info(p.toString()));
		// ModelAndView mv = new ModelAndView();
//		model.addAttribute("positions", positions);
		//mv.setViewName(NamesView.POSITIONS);
		//mv.addObject("positions", positions);
		//return NamesView.POSITIONS;
		return "redirect:/positions/";
//		return NamesView.POSITIONS;
		//return mv;

//		return "redirect:" + redirectUrl;
	}
}