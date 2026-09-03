package ru.perm.v.hradmin.mapper;

import ru.perm.v.hradmin.dto.PositionDto;
import ru.perm.v.hradmin.model.Position;

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
