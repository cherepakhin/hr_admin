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
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/positions")
public class PositionController {

	private static final Logger log = LoggerFactory.getLogger(PositionController.class);

    @Autowired
    private PositionRepository positionRepository;

	public PositionController() {
		super();
	}

	public PositionController(PositionRepository positionRepository) {
		this();
		this.positionRepository = positionRepository;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
    public String listPositions(Model model) {
        model.addAttribute("positions", positionRepository.findAll());
        return NamesView.POSITIONS;
    }

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String listPositionsRoot(Model model) {
		return listPositions(model);
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
    public String showCreateForm(Model model) {
		log.info("Show dialog new position");
        model.addAttribute("position", new Position());
        return NamesView.CREATE_POSITION;
    }

//	            <form action="/positions" method="post"> // post on /position
//                <div>
//                    <label>Название</label>
//                    <input type="text" name="name" required> <-- field "name" (position.name=name)
//                </div>
//                <button type="submit">Сохранить</button>   <-- type=SUBMIT !!!
//              </form>

	@RequestMapping(value = "/", method = RequestMethod.POST)
    public String createPosition(@Valid @ModelAttribute Position position,
                                 BindingResult result,
                                 Model model) {
		log.info("Create position: {}", position);
        if (positionRepository.existsByName(position.getName())) {
            result.rejectValue("name", "error.position", "Должность с таким названием уже существует.");
        }
        if (result.hasErrors()) {
            return NamesView.CREATE_POSITION;
        }
		Long id = positionRepository.getNextId();
		position.setId(id);
        position = positionRepository.save(position);
		log.info("Saved position: {}", position);
        return "redirect:/" + NamesView.POSITIONS + "/";
    }

	@RequestMapping(value = "", method = RequestMethod.POST)
	public String createPositionForEmpty(@Valid @ModelAttribute Position position,
										 BindingResult result,
										 Model model) {
		return createPosition(position, result, model);
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid position ID: " + id));
        model.addAttribute("position", position);
        return NamesView.EDIT_POSITION;
    }

	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public String updatePosition(@PathVariable("id") Long id,
                                 @Valid @ModelAttribute Position position,
                                 BindingResult result,
                                 Model model) {
        if (positionRepository.existsByNameAndIdNot(position.getName(), id)) {
            result.rejectValue("name", "error.position", "Должность с таким названием уже существует.");
        }
        if (result.hasErrors()) {
            position.setId(id);
            return NamesView.EDIT_POSITION;
        }
        position.setId(id);
        positionRepository.save(position);
        return "redirect:/" + NamesView.POSITIONS + "/";
    }

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deletePosition(@PathVariable("id") Long id) {
        positionRepository.deleteById(id);
        return "redirect:/" + NamesView.POSITIONS +"/";
    }
}