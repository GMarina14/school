package ru.hogwarts.school.Mapper;

import org.springframework.stereotype.Component;
import ru.hogwarts.school.Model.Avatar;
import ru.hogwarts.school.dto.AvatarDTO;

@Component
public class AvatarMapper {
    public AvatarDTO mapToDTO(Avatar avatar) {
        return new AvatarDTO(
                avatar.getId(),
                avatar.getFilePath(),
                avatar.getFileSize(),
                avatar.getMediaType(),
                avatar.getStudent().getId()
        );
    }
}
