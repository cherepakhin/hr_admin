package ru.perm.v.hradmin.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import ru.perm.v.hradmin.model.Employee;
import ru.perm.v.hradmin.model.Position;
import ru.perm.v.hradmin.repository.EmployeeRepository;
import ru.perm.v.hradmin.repository.PositionRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Создано OpenCode
 *
 * @author OpenCode
 */
@ExtendWith(MockitoExtension.class)
class EmployeeControllerUnitTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PositionRepository positionRepository;

    @Mock
    private Model model;

    private EmployeeController controller;

    @BeforeEach
    void setUp() {
        controller = new EmployeeController();
        controller.setEmployeeRepository(employeeRepository);
        controller.setPositionRepository(positionRepository);
    }

    private Position position() {
        return new Position(1L, "Developer");
    }

    private Employee employee() {
        return new Employee("John", "Doe", "john@example.com", position());
    }

    // ==================== listEmployees ====================

    @Test
    void listEmployees_returnsIndexView() {
        when(employeeRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(
                List.of(employee()), PageRequest.of(0, 10), 1));

        String view = controller.listEmployees(model, 0, 10, "id", "asc");

        assertEquals("index", view);
        verify(model).addAttribute(eq("employees"), any());
        verify(model).addAttribute("currentPage", 0);
    }

    @Test
    void listEmployees_defaultParams_returnsIndexView() {
        when(employeeRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(
                List.of(employee()), PageRequest.of(0, 10), 1));

        String view = controller.listEmployees(model, 0, 10, "id", "desc");

        assertEquals("index", view);
    }

    // ==================== createEmployee ====================

    @Test
    void createEmployee_validEmployee_savesAndRedirects() {
        Employee employee = employee();
        when(positionRepository.findAll()).thenReturn(List.of(position()));

        String view = controller.createEmployee(employee, model);

        assertEquals("redirect:/", view);
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void createEmployee_invalidEmployee_returnsCreateForm() {
        Employee invalid = new Employee("Jo", "Doe", "john@example.com", position());
        when(positionRepository.findAll()).thenReturn(List.of(position()));

        String view = controller.createEmployee(invalid, model);

        assertEquals(NamesView.CREATE_EMPLOYEE, view);
        verify(model).addAttribute(eq("error_for_firstName"), any());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void createEmployee_invalidLastName_returnsCreateForm() {
        Employee invalid = new Employee("John", "Do", "john@example.com", position());
        when(positionRepository.findAll()).thenReturn(List.of(position()));

        String view = controller.createEmployee(invalid, model);

        assertEquals(NamesView.CREATE_EMPLOYEE, view);
        verify(model).addAttribute(eq("error_for_lastName"), any());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    // ==================== showCreateForm ====================

    @Test
    void showCreateForm_addsAttributesAndReturnsView() {
        when(positionRepository.findAll()).thenReturn(List.of(position()));

        String view = controller.showCreateForm(model);

        assertEquals(NamesView.CREATE_EMPLOYEE, view);
        verify(model).addAttribute(eq("employee"), any(Employee.class));
        verify(model).addAttribute(eq("positions"), any());
    }

    // ==================== showEditForm ====================

    @Test
    void showEditForm_existingEmployee_returnsEditView() {
        Employee employee = employee();
        employee.setId(1L);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(positionRepository.findAll()).thenReturn(List.of(position()));

        String view = controller.showEditForm(1L, model);

        assertEquals(NamesView.EDIT_EMPLOYEE, view);
        verify(model).addAttribute("employee", employee);
        verify(model).addAttribute("positions", List.of(position()));
    }

    @Test
    void showEditForm_missingEmployee_throws() {
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> controller.showEditForm(999L, model));

        assertTrue(e.getMessage().contains("999"));
    }

    // ==================== updateEmployee ====================

    @Test
    void updateEmployee_setsIdAndSavesAndRedirects() {
        Employee employee = employee();
        employee.setId(1L);

        String view = controller.updateEmployee(1L, employee, model);

        assertEquals(1L, employee.getId());
        assertEquals("redirect:/", view);
        verify(employeeRepository, times(1)).save(employee);
    }

    // ==================== deleteEmployee ====================

    @Test
    void deleteEmployee_existingEmployee_deletesAndRedirectsToIndex() {
        when(employeeRepository.existsById(1L)).thenReturn(true);

        ModelAndView mv = controller.deleteEmployee(1L, model);

        assertEquals("redirect:/index/", mv.getViewName());
        verify(employeeRepository).deleteById(1L);
    }

    @Test
    void deleteEmployee_fromShowEmployees_redirectsToShowEmployees() {
        when(employeeRepository.existsById(1L)).thenReturn(true);
        when(positionRepository.findAll()).thenReturn(List.of(position()));
        when(employeeRepository.findByFiltersAndSort(eq(""), eq(""), anyList(), eq(""), any()))
                .thenReturn(new org.springframework.data.domain.PageImpl<>(List.of(employee())));

        controller.showAllEmployees(model, 0, 10, "", "", "", -1L, "lastName", "asc");
        ModelAndView mv = controller.deleteEmployee(1L, model);

        assertEquals("redirect:/show_employees/", mv.getViewName());
    }

    @Test
    void deleteEmployee_missingEmployee_throws() {
        when(employeeRepository.existsById(999L)).thenReturn(false);

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> controller.deleteEmployee(999L, model));

        assertTrue(e.getMessage().contains("999"));
        verify(employeeRepository, never()).deleteById(any());
    }

    // ==================== refreshEmployees ====================

    @Test
    void refreshEmployees_setsModelAttributes() {
        Page<Employee> page = new PageImpl<>(List.of(employee()), PageRequest.of(0, 10), 1);
        when(employeeRepository.findAll(any(Pageable.class))).thenReturn(page);

        controller.refreshEmployees(model, 0, 10, "firstName", "asc");

        verify(model).addAttribute("employees", page.getContent());
        verify(model).addAttribute("sortField", "firstName");
        verify(model).addAttribute("direction", Sort.Direction.ASC);
        verify(model).addAttribute("currentPage", 0);
        verify(model).addAttribute("totalPages", 1);
        verify(model).addAttribute("totalElements", 1L);
    }

    @Test
    void refreshEmployees_emptySortField_usesId() {
        Page<Employee> page = new PageImpl<>(List.of(employee()), PageRequest.of(0, 10), 1);
        when(employeeRepository.findAll(any(Pageable.class))).thenReturn(page);

        controller.refreshEmployees(model, 0, 10, "", "desc");

        verify(model).addAttribute("sortField", "id");
    }

    @Test
    void refreshEmployees_unknownDirection_usesAsc() {
        Page<Employee> page = new PageImpl<>(List.of(employee()), PageRequest.of(0, 10), 1);
        when(employeeRepository.findAll(any(Pageable.class))).thenReturn(page);

        controller.refreshEmployees(model, 0, 10, "lastName", "unknown");

        verify(model).addAttribute("direction", Sort.Direction.ASC);
    }

    // ==================== showFilterPage ====================

    @Test
    void showFilterPage_addsPositionsAndReturnsView() {
        when(positionRepository.findAll()).thenReturn(List.of(position()));

        String view = controller.showFilterPage(model);

        assertEquals(NamesView.FILTER_EMPLOYEES, view);
        verify(model).addAttribute("positions", List.of(position()));
    }

    // ==================== showAllEmployees ====================

    @Test
    void showAllEmployees_withoutPositionId_searchesAllPositions() {
        Page<Employee> page = new PageImpl<>(List.of(employee()), PageRequest.of(0, 10), 1);
        when(positionRepository.findAll()).thenReturn(List.of(position()));
        when(employeeRepository.findByFiltersAndSort(any(), any(), anyList(), any(), any())).thenReturn(page);

        String view = controller.showAllEmployees(model, 0, 10, "", "", "", -1L, "lastName", "asc");

        assertEquals(NamesView.SHOW_EMPLOYEES, view);
        verify(model).addAttribute("firstName", "");
        verify(model).addAttribute("sortField", "lastName");
        verify(employeeRepository, times(1))
                .findByFiltersAndSort(eq(""), eq(""), eq(List.of(1L)), eq(""), any());
    }

    @Test
    void showAllEmployees_withPositionId_searchesOnlyThatPosition() {
        Page<Employee> page = new PageImpl<>(List.of(employee()), PageRequest.of(0, 10), 1);
        Position other = new Position(2L, "Manager");
        when(positionRepository.findAll()).thenReturn(List.of(position(), other));
        when(employeeRepository.findByFiltersAndSort(any(), any(), anyList(), any(), any())).thenReturn(page);

        String view = controller.showAllEmployees(model, 0, 10, "", "", "", 2L, "lastName", "asc");
        assertEquals(NamesView.SHOW_EMPLOYEES, view);

        verify(employeeRepository, times(1))
                .findByFiltersAndSort(eq(""), eq(""), eq(List.of(2L)), eq(""), any());
    }

    @Test
    void showAllEmployees_sortByPosition_usesPositionName() {
        Page<Employee> page = new PageImpl<>(List.of(employee()), PageRequest.of(0, 10), 1);
        when(positionRepository.findAll()).thenReturn(List.of(position()));
        when(employeeRepository.findByFiltersAndSort(any(), any(), anyList(), any(), any())).thenReturn(page);

        String view = controller.showAllEmployees(model, 0, 10, "", "", "", -1L, "position", "asc");

        assertEquals(NamesView.SHOW_EMPLOYEES, view);
        verify(model).addAttribute("sortField", "position.name");
    }

    // ==================== getters / setters ====================

    @Test
    void getPersonRepository_returnsInjectedRepository() {
        assertEquals(employeeRepository, controller.getEmployeeRepository());
        assertEquals(positionRepository, controller.getPositionRepository());
    }
}