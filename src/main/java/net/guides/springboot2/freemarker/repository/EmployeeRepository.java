package net.guides.springboot2.freemarker.repository;

import net.guides.springboot2.freemarker.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	@Query("SELECT e FROM Employee e " +
			"WHERE (:firstName IS NULL OR LOWER(e.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) " +
			"AND (:lastName IS NULL OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) " +
			"AND (:email IS NULL OR LOWER(e.email) LIKE LOWER(CONCAT('%', :email, '%'))) " +
			"AND (:positionId IS NULL OR e.position.id = :positionId)")
	Page<Employee> findByFiltersAndSort(
			@Param("firstName") String firstName,
			@Param("lastName") String lastName,
			@Param("email") String email,
			@Param("positionId") Long positionId,
			Pageable pageable
	);

	Optional<Employee> findByFirstNameAndLastName(String firstName, String lastName);

	Optional<Employee> findByEmail(String mail);
}