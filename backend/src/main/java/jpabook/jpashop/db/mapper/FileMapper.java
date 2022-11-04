package jpabook.jpashop.db.mapper;

import org.springframework.stereotype.Component;

import jpabook.jpashop.db.dto.FileDTO;
import jpabook.jpashop.db.entity.File;

@Component
public class FileMapper {
    public FileDTO toDto(File file) {
        return FileDTO.builder()
                .id(file.getId())
                .name(file.getName())
                .createdDate(file.getCreatedDate())
                .filePath(file.getFilePath())
                .build();
    }

    public File toEntity(FileDTO fileDto) {
        return File.builder()
                .id(fileDto.getId())
                .name(fileDto.getName())
                .createdDate(fileDto.getCreatedDate())
                .filePath(fileDto.getFilePath())
                .build();
    }
}
