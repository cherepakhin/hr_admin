package net.guides.springboot2.freemarker.repository;

import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import net.guides.springboot2.freemarker.model.Position;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Table(name = "position")
@Transactional
public interface PositionRepository extends JpaRepository<Position, Long> {
    boolean existsByName(String name);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END FROM Position p WHERE LOWER(p.name) = LOWER(:name) AND p.id <> :id")
    boolean existsByNameAndIdNot(@Param("name") String name, @Param("id") Long id);

	//  COALESCE -  Замена NULL на значение по умолчанию.
	@Query("select COALESCE(max(id)+1, 1) from Position")
	Long getNextId();

	@Modifying
	@Query("delete from Position where id= :id")
	void sqlDeleteById(@Param("id") long positionId);

	@Query("select p from Position p")
	List<Position> findAllAndSort(Sort sort);
}