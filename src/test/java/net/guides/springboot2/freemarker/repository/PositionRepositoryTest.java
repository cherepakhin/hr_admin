package net.guides.springboot2.freemarker.repository;

import net.guides.springboot2.freemarker.model.Position;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class PositionRepositoryTest {

    @Autowired
    PositionRepository positionRepository;

    @Test
    void findAll() {
        List<Position> positions = positionRepository.findAll();

        assertTrue(positions.size() > 0);
        assertEquals(new Position(2L, "Бухгалтер"), positions.get(0));
    }

    @Test
    void findPositionByNameContainsIgnoreCase() {
        List<Position> positions = positionRepository.findPositionByNameContainsIgnoreCase("директор");

        assertEquals(1, positions.size());
        assertEquals("Директор", positions.get(0).getName());
    }
}