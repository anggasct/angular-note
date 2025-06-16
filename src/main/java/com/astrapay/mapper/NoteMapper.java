package com.astrapay.mapper;

import com.astrapay.dto.NoteDto;
import com.astrapay.entity.Note;
import org.springframework.stereotype.Component;

@Component
public class NoteMapper {
    
    public Note toEntity(NoteDto noteDto) {
        return Note.builder()
                .id(noteDto.getId())
                .title(noteDto.getTitle())
                .content(noteDto.getContent())
                .build();
    }
    
    public NoteDto toDto(Note note) {
        return NoteDto.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
                .build();
    }
}
