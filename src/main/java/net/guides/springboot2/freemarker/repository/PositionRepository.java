package net.guides.springboot2.freemarker.repository;

import net.guides.springboot2.freemarker.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
    boolean existsByName(String name);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END FROM Position p WHERE LOWER(p.name) = LOWER(:name) AND p.id <> :id")
    boolean existsByNameAndIdNot(@Param("name") String name, @Param("id") Long id);
}