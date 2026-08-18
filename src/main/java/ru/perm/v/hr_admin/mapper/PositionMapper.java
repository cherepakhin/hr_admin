package ru.perm.v.hr_admin.mapper;

import ru.perm.v.hr_admin.dto.PositionDto;
import ru.perm.v.hr_admin.model.Position;

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
