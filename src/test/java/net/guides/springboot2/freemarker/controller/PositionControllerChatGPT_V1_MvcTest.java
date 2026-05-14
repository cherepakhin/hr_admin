package net.guides.springboot2.freemarker.controller;

import net.guides.springboot2.freemarker.model.Position;
import net.guides.springboot2.freemarker.repository.EmployeeRepository;
import net.guides.springboot2.freemarker.repository.PositionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Сгенерировано https://chatgpt.com/c/6a0177f8-e77c-83ea-b43d-1f8c17a10537
 * через прокси!
 * Версия V1 от версии V2 мало чем отличаются.
 */
@WebMvcTest(PositionController.class)
public class PositionControllerChatGPT_V1_MvcTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PositionRepository positionRepository;

	@MockBean
	private EmployeeRepository employeeRepository;

	@Test
	@DisplayName("GET /positions/ -> список должностей")
	void shouldReturnPositionsList() throws Exception {
		Position p1 = new Position();
		p1.setId(1L);
		p1.setName("Developer");

		Position p2 = new Position();
		p2.setId(2L);
		p2.setName("Manager");

		when(positionRepository.findAll()).thenReturn(List.of(p1, p2));

		mockMvc.perform(get("/positions/"))
				.andExpect(status().isOk())
				.andExpect(view().name("show_positions"))
				.andExpect(model().attributeExists("positions"));

		verify(positionRepository).findAllAndSort(any(Sort.class));
	}

	@Test
	@DisplayName("GET /positions/new -> форма создания")
	void shouldShowCreateForm() throws Exception {
		mockMvc.perform(get("/positions/new"))
				.andExpect(status().isOk())
				.andExpect(view().name(NamesView.CREATE_POSITION))
				.andExpect(model().attributeExists("position"));
	}

	@Test
	@DisplayName("POST /positions/ -> успешное создание")
	void shouldCreatePosition() throws Exception {
		when(positionRepository.existsByName("Developer"))
				.thenReturn(false);

		when(positionRepository.getNextId())
				.thenReturn(10L);

		mockMvc.perform(post("/positions/")
						.param("name", "Developer"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/positions/"));

		verify(positionRepository).save(any(Position.class));
	}

	@Test
	@DisplayName("POST /positions/ -> ошибка валидации")
	void shouldReturnValidationErrorWhenCreate() throws Exception {
		mockMvc.perform(post("/positions/")
						.param("name", ""))
				.andExpect(status().isOk())
				.andExpect(view().name(NamesView.CREATE_POSITION))
				.andExpect(model().attributeExists("error"));
	}

	@Test
	@DisplayName("POST /positions/ -> дубликат должности")
	void shouldRejectDuplicatePosition() throws Exception {
		when(positionRepository.existsByName("Developer"))
				.thenReturn(true);

		mockMvc.perform(post("/positions/")
						.param("name", "Developer"))
				.andExpect(status().isOk())
				.andExpect(view().name(NamesView.CREATE_POSITION))
				.andExpect(model().attributeExists("error"));

		verify(positionRepository, never()).save(any());
	}

	@Test
	@DisplayName("GET /positions/edit/{id} -> форма редактирования")
	void shouldShowEditForm() throws Exception {
		Position position = new Position();
		position.setId(1L);
		position.setName("Developer");

		when(positionRepository.findById(1L))
				.thenReturn(Optional.of(position));

		mockMvc.perform(get("/positions/edit/1"))
				.andExpect(status().isOk())
				.andExpect(view().name(NamesView.EDIT_POSITION))
				.andExpect(model().attributeExists("position"));
	}

	@Test
	@DisplayName("POST /positions/update/{id} -> успешное обновление")
	void shouldUpdatePosition() throws Exception {
		when(positionRepository.existsByName("Developer"))
				.thenReturn(true);

		mockMvc.perform(post("/positions/update/1")
						.param("name", "Developer"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/show_positions/"));

		verify(positionRepository).save(any(Position.class));
	}

	@Test
	@DisplayName("POST /positions/update/{id} -> ошибка, если позиции нет")
	void shouldFailUpdateWhenPositionNotFound() throws Exception {
		when(positionRepository.existsByName("Unknown"))
				.thenReturn(false);

		mockMvc.perform(post("/positions/update/1")
						.param("name", "Unknown"))
				.andExpect(status().isOk())
				.andExpect(view().name(NamesView.EDIT_POSITION));

		verify(positionRepository, never()).save(any());
	}

	@Test
	@DisplayName("GET /positions/delete/{id} -> удаление")
	void shouldDeletePosition() throws Exception {
		mockMvc.perform(get("/positions/delete/1"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/positions/"));

		verify(positionRepository).sqlDeleteById(1L);
	}
}
