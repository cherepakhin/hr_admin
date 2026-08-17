package net.guides.springboot2.freemarker.mapper;

import net.guides.springboot2.freemarker.dto.PositionDto;
import net.guides.springboot2.freemarker.model.Position;

public class PositionMapper implements IMapper<PositionDto, Position> {
    @Override
    public PositionDto toDTO(Position position) {
        if (position == null) {
            return null;
        }
        return new PositionDto(position.getId(), position.getName());
    }

    @Override
    public Position toEntity(PositionDto positionDto) {
        if (positionDto == null) {
            return null;
        }
        return new Position(positionDto.getId(), positionDto.getName());
    }
}
