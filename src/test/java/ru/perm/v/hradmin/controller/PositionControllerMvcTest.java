package ru.perm.v.hradmin.controller;

import jakarta.validation.ConstraintViolationException;
import ru.perm.v.hradmin.model.Position;
import ru.perm.v.hradmin.repository.EmployeeRepository;
import ru.perm.v.hradmin.repository.PositionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PositionController.class)
public class PositionControllerMvcTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PositionRepository positionRepository;

	@MockBean
	private EmployeeRepository employeeRepository;

	@Test
	public void shouldListPositions() throws Exception {
		// Given
		Position dev = new Position(1L, "Developer");
		Position mgr = new Position(2L, "Manager");
		List<Position> positions = List.of(dev, mgr);
		when(positionRepository.findAllAndSort(any(Sort.class))).thenReturn(positions);

		// When & Then
		mockMvc.perform(get("/positions/"))
				.andExpect(status().isOk())
				.andExpect(view().name("show_positions"))
				.andExpect(model().attributeExists("positions"))
				.andExpect(model().attribute("positions", hasSize(2)))
				.andExpect(content().string(containsString("Developer")))
				.andExpect(content().string(containsString("Manager")));
	}

	@Test
	public void shouldListPositionsSortedByNameAsc() throws Exception {
		// Given
		Position dev = new Position(1L, "Developer");
		when(positionRepository.findAllAndSort(Sort.by("name").ascending())).thenReturn(List.of(dev));

		// When & Then
		mockMvc.perform(get("/positions/")
						.param("sortField", "name")
						.param("direction", "asc"))
				.andExpect(status().isOk())
				.andExpect(view().name("show_positions"))
				.andExpect(model().attribute("sortField", "name"))
				.andExpect(model().attribute("direction", "asc"));
	}

	@Test
	public void shouldListPositionsSortedByNameDesc() throws Exception {
		// Given
		Position mgr = new Position(2L, "Manager");
		when(positionRepository.findAllAndSort(Sort.by("name").descending())).thenReturn(List.of(mgr));

		// When & Then
		mockMvc.perform(get("/positions/")
						.param("sortField", "name")
						.param("direction", "desc"))
				.andExpect(status().isOk())
				.andExpect(view().name("show_positions"))
				.andExpect(model().attribute("direction", "desc"));
	}

	@Test
	public void shouldListPositionsSortedById() throws Exception {
		// Given
		Position dev = new Position(1L, "Developer");
		when(positionRepository.findAllAndSort(Sort.by("id").ascending())).thenReturn(List.of(dev));

		// When & Then
		mockMvc.perform(get("/positions/")
						.param("sortField", "id")
						.param("direction", "asc"))
				.andExpect(status().isOk())
				.andExpect(view().name("show_positions"))
				.andExpect(model().attribute("sortField", "id"));
	}

	@Test
	public void shouldShowCreateForm() throws Exception {
		// When & Then
		mockMvc.perform(get("/positions/new"))
				.andExpect(status().isOk())
				.andExpect(view().name("create_position"))
				.andExpect(model().attributeExists("position"))
				.andExpect(model().attribute("position", is(notNullValue())));
	}

	@Test
	public void shouldCreatePositionAndRedirect() throws Exception {
		// Given
		String NAME = "Tester";
		when(positionRepository.existsByName(NAME)).thenReturn(false);
		when(positionRepository.getNextId()).thenReturn(5L);

		// When & Then
		mockMvc.perform(post("/positions/")
						.param("name", NAME))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/positions/"));

		verify(positionRepository).save(argThat(p -> NAME.equals(p.getName()) && Long.valueOf(5L).equals(p.getId())));
	}

	@Test
	public void shouldNotCreateDuplicatePosition() throws Exception {
		// Given
		String NAME = "Developer";
		when(positionRepository.existsByName(NAME)).thenReturn(true);

		// When & Then
		mockMvc.perform(post("/positions/")
						.param("name", NAME))
				.andExpect(status().isOk())
				.andExpect(view().name("create_position"))
				.andExpect(model().attribute("name", NAME))
				.andExpect(model().attribute("error_for_name", "Должность с таким названием уже существует."));

		verify(positionRepository, never()).save(any(Position.class));
	}

	@Test
	public void shouldNotCreatePositionWithShortName() throws Exception {
		// Given
		String SHORT_NAME = "AB";
		when(positionRepository.existsByName(SHORT_NAME)).thenReturn(false);

		// When & Then
		mockMvc.perform(post("/positions/")
						.param("name", SHORT_NAME))
				.andExpect(status().isOk())
				.andExpect(view().name("create_position"))
				.andExpect(model().attribute("name", SHORT_NAME))
				.andExpect(model().attribute("error_for_name", containsString("от 3 to 15 символов")));

		verify(positionRepository, never()).save(any(Position.class));
	}

	@Test
	public void shouldShowEditFormForExistingPosition() throws Exception {
		// Given
		Position position = new Position(1L, "Developer");
		when(positionRepository.findById(1L)).thenReturn(Optional.of(position));

		// When & Then
		mockMvc.perform(get("/positions/edit/1"))
				.andExpect(status().isOk())
				.andExpect(view().name("edit_position"))
				.andExpect(model().attributeExists("position"))
				.andExpect(model().attribute("position", position));
	}

	@Test
	public void shouldReturnErrorWhenEditPositionNotFound() throws Exception {
		// Given
		when(positionRepository.findById(999L)).thenReturn(Optional.empty());

		// When & Then
		assertThatThrownBy(() -> mockMvc.perform(get("/positions/edit/999")))
				.cause()
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Invalid position ID: 999");
	}

	@Test
	public void shouldUpdatePositionAndRedirect() throws Exception {
		// Given
		Long ID = 1L;
		String NEW_NAME = "SeniorDeveloper";
		Position existing = new Position(ID, "Developer");
		when(positionRepository.findById(ID)).thenReturn(Optional.of(existing));
		when(positionRepository.existsByName(NEW_NAME)).thenReturn(false);

		// When & Then
		mockMvc.perform(post("/positions/update/" + ID)
						.param("name", NEW_NAME))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/positions/"));

		verify(positionRepository).save(argThat(p -> ID.equals(p.getId()) && NEW_NAME.equals(p.getName())));
	}

	@Test
	public void shouldNotUpdatePositionWithDuplicateName() throws Exception {
		// Given
		Long ID = 1L;
		String DUPLICATE_NAME = "Developer";
		Position existing = new Position(ID, "Tester");
		when(positionRepository.findById(ID)).thenReturn(Optional.of(existing));
		when(positionRepository.existsByName(DUPLICATE_NAME)).thenReturn(true);

		// When & Then
		mockMvc.perform(post("/positions/update/" + ID)
						.param("name", DUPLICATE_NAME))
				.andExpect(status().isOk())
				.andExpect(view().name("edit_position"))
				.andExpect(model().attribute("name", DUPLICATE_NAME))
				.andExpect(model().attribute("error_for_name", "Должность с таким названием УЖЕ существует."));

		verify(positionRepository, never()).save(any(Position.class));
	}

	@Test
	public void shouldNotUpdatePositionWithShortName() throws Exception {
		// Given
		Long ID = 1L;
		String SHORT_NAME = "A";
		Position existing = new Position(ID, "Developer");
		when(positionRepository.findById(ID)).thenReturn(Optional.of(existing));
		when(positionRepository.existsByName(SHORT_NAME)).thenReturn(false);

		// When & Then
		mockMvc.perform(post("/positions/update/" + ID)
						.param("name", SHORT_NAME))
				.andExpect(status().isOk())
				.andExpect(view().name("edit_position"))
				.andExpect(model().attribute("name", SHORT_NAME))
				.andExpect(model().attribute("error_for_name", containsString("от 3 to 15 символов")));

		verify(positionRepository, never()).save(any(Position.class));
	}

	@Test
	public void shouldDeletePositionAndRedirect() throws Exception {
		// When & Then
		mockMvc.perform(get("/positions/delete/1"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/positions/"));

		verify(positionRepository).sqlDeleteById(1L);
	}

	@Test
	public void shouldReturnBadRequestWhenDirectionIsInvalid() {
		// When & Then
		assertThatThrownBy(() -> mockMvc.perform(get("/positions/")
						.param("direction", "invalid")))
				.cause()
				.isInstanceOf(ConstraintViolationException.class)
				.hasMessageContaining("Направление должно быть 'asc' или 'desc'");
	}

	@Test
	public void shouldReturnBadRequestWhenSortFieldIsInvalid() {
		// When & Then
		assertThatThrownBy(() -> mockMvc.perform(get("/positions/")
						.param("sortField", "invalid")
						.param("direction", "asc")))
				.cause()
				.isInstanceOf(ConstraintViolationException.class)
				.hasMessageContaining("Направление должно быть 'id' или 'name'");
	}

	@Test
	public void shouldReturnEmptyListWhenNoPositions() throws Exception {
		// Given
		when(positionRepository.findAllAndSort(any(Sort.class))).thenReturn(Collections.emptyList());

		// When & Then
		mockMvc.perform(get("/positions/"))
				.andExpect(status().isOk())
				.andExpect(view().name("show_positions"))
				.andExpect(model().attribute("positions", hasSize(0)));
	}
}
