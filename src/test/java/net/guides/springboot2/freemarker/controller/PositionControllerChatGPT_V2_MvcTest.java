package net.guides.springboot2.freemarker.controller;

import net.guides.springboot2.freemarker.model.Position;
import net.guides.springboot2.freemarker.repository.EmployeeRepository;
import net.guides.springboot2.freemarker.repository.PositionRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Сгенерировано https://chatgpt.com/c/6a0177f8-e77c-83ea-b43d-1f8c17a10537
 * через прокси!
 */
@WebMvcTest(PositionController.class)
@Disabled // this test created SourceCraft for demo purpose
public class PositionControllerChatGPT_V2_MvcTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PositionRepository positionRepository;

	@MockBean
	private EmployeeRepository employeeRepository;

	@Test
	@DisplayName("Получение списка позиций")
	void listPositions() throws Exception {
		Position position = new Position();
		position.setId(1L);
		position.setName("Developer");

		when(positionRepository.findAll(any(Sort.class))).thenReturn(List.of(position));

		mockMvc.perform(get("/positions/"))
				.andExpect(status().isOk())
				.andExpect(view().name("show_positions"))
				.andExpect(model().attributeExists("positions"));

		verify(positionRepository).findAllAndSort(any(Sort.class));

		Sort sort = Sort.by("name").ascending();
		verify(positionRepository).findAllAndSort(eq(sort));
	}

	@Test
	@DisplayName("Форма создания позиции")
	void showCreateForm() throws Exception {
		mockMvc.perform(get("/positions/new"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("position"));
	}

	@Test
	@DisplayName("Успешное создание позиции")
	void createPositionSuccess() throws Exception {
		when(positionRepository.existsByName("Developer")).thenReturn(false);
		when(positionRepository.getNextId()).thenReturn(1L);

		mockMvc.perform(post("/positions/")
						.param("name", "Developer"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/positions/"));

		verify(positionRepository).save(any(Position.class));
	}

	@Test
	@DisplayName("Ошибка создания - позиция уже существует")
	void createPositionDuplicate() throws Exception {
		when(positionRepository.existsByName("Developer")).thenReturn(true);

		mockMvc.perform(post("/positions/")
						.param("name", "Developer"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("error"));

		verify(positionRepository, never()).save(any());
	}

	@Test
	@DisplayName("Форма редактирования")
	void showEditForm() throws Exception {
		Position position = new Position();
		position.setId(1L);
		position.setName("Developer");

		when(positionRepository.findById(1L))
				.thenReturn(Optional.of(position));

		mockMvc.perform(get("/positions/edit/1"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("position"));
	}

	@Test
	@DisplayName("Успешное обновление")
	void updatePositionSuccess() throws Exception {
		when(positionRepository.existsByName("Developer"))
				.thenReturn(true);

		mockMvc.perform(post("/positions/update/1")
						.param("name", "Developer"))
				.andExpect(status().isOk())
				.andExpect(view().name("edit_position"));

		verify(positionRepository, never()).save(any(Position.class));
	}

	@Test
	@DisplayName("Удаление позиции")
	void deletePosition() throws Exception {
		mockMvc.perform(get("/positions/delete/1"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/positions/"));

		verify(positionRepository).sqlDeleteById(1L);
	}
}
