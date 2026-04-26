package net.guides.springboot2.freemarker.controller;

import jakarta.validation.Valid;
import net.guides.springboot2.freemarker.model.Position;
import net.guides.springboot2.freemarker.repository.PositionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

//TODO: добавить валидацию
@Controller
@RequestMapping("/positions")
public class PositionController {

	private static final Logger log = LoggerFactory.getLogger(PositionController.class);

	private final PositionRepository positionRepository;

	// Используем внедрение через конструктор (рекомендуется)
	@Autowired
	public PositionController(PositionRepository positionRepository) {
		this.positionRepository = positionRepository;
	}

	// GET /positions/ - отображение списка
	@GetMapping("/")
	public ModelAndView listPositions(Model model) {
		log.info("get all positions");
		List<Position> positions = positionRepository.findAll();
		positions.forEach(p-> log.info(p.toString()));
		ModelAndView mv = new ModelAndView();
		mv.setViewName(NamesView.POSITIONS);
		mv.addObject("positions", positions);
		return mv;
	}

	// GET /positions/new - форма создания
	@GetMapping("/new")
	public String showCreateForm(Model model) {
		Position position = new Position();
		position.setName("?"); // значение по умолчанию
		model.addAttribute("position", position);
		return NamesView.CREATE_POSITION;
	}

	// POST /positions/ - обработка создания
	@PostMapping("/")
	public String createPosition(@Valid @ModelAttribute Position position,
								 BindingResult bindingResult,  Model model) {
		log.info("Create position: {}", position);
		if (bindingResult.hasErrors()) {
			log.info("Binding result: {}", bindingResult);
			StringBuilder errors = new StringBuilder();
			for(ObjectError error :  bindingResult.getAllErrors()) {
				errors.append(error.getDefaultMessage()).append("\n");
				log.error(error.getDefaultMessage());
			}
			model.addAttribute("name", position.getName());
			model.addAttribute("error", errors.toString());
			return NamesView.CREATE_POSITION;
		}
		if (positionRepository.existsByName(position.getName())) {
			log.error(String.format("Должность с названием '%s' уже существует.", position.getName()));
			model.addAttribute("position", position);
			model.addAttribute("error", "Должность с таким названием уже существует.");
			return NamesView.CREATE_POSITION;
		}
		if (bindingResult.hasErrors()) {
			return NamesView.CREATE_POSITION;
		}
		position.setId(positionRepository.getNextId());
		positionRepository.save(position);
		return "redirect:/" + NamesView.POSITIONS + "/";
	}

	// GET /positions/edit/{id} - форма редактирования
	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable Long id, Model model) {
		Position position = positionRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid position ID: " + id));
		model.addAttribute("position", position);
		return NamesView.EDIT_POSITION;
	}

	// POST /positions/update/{id} - обработка обновления
	@PostMapping("/update/{id}")
	public String updatePosition(@PathVariable Long id,
								 @Valid @ModelAttribute Position position,
								 BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			log.info("Binding result: {}", bindingResult);
			String errors = "";
			for (ObjectError error : bindingResult.getAllErrors()) {
				errors += error.getDefaultMessage() + "\n";
				log.error(error.getDefaultMessage());
			}
			model.addAttribute("name", position.getName());
			model.addAttribute("error", errors);
		}

		if (positionRepository.existsByNameAndIdNot(position.getName(), id)) {
			bindingResult.rejectValue("name", "error.position", "Должность с таким названием уже существует.");
		}
		if (bindingResult.hasErrors()) {
			position.setId(id); // Восстанавливаем id для формы
			return NamesView.EDIT_POSITION;
		}
		position.setId(id);
		positionRepository.save(position);
		return "redirect:/" + NamesView.POSITIONS + "/";
	}

	// GET /positions/delete/{id} - удаление
	@GetMapping("/delete/{id}")
	public ModelAndView deletePosition(@PathVariable Long id, Model model) {
		log.info("Delete position id: {}", id);
		ModelAndView mv = new ModelAndView(NamesView.POSITIONS);
		mv.clear();

		// TODO: Проверить на существование
		// TODO: Проверить, используется ли позиция где-то ещё (например, у сотрудников)

		positionRepository.deleteById(id);

		// Вместо простого редиректа, добавляем уникальный параметр
		// UUID.randomUUID().toString() создаст случайную строку типа "a1b2-c3d4..."
		//String redirectUrl = "/" + NamesView.POSITIONS + "/?v==" + UUID.randomUUID().toString();
		//return redirectUrl;
		//mv.addreturn listPositions(model);
		mv.setViewName("redirect:/" +NamesView.POSITIONS +"/");
		return mv;

//		return "redirect:" + redirectUrl;
	}
}