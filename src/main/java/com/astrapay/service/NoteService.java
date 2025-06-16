package com.astrapay.service;

import com.astrapay.dto.NoteDto;
import com.astrapay.entity.Note;
import com.astrapay.mapper.NoteMapper;
import com.astrapay.repository.NoteRepository;
import com.astrapay.util.NoteSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class NoteService {
    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;

    public NoteDto createNote(NoteDto noteDto) {
        Note note = noteMapper.toEntity(noteDto);
        Note savedNote = noteRepository.save(note);
        return noteMapper.toDto(savedNote);
    }

    public NoteDto updateNote(Long id, NoteDto noteDto) {
        if (!noteRepository.existsById(id)) {
            throw new NoSuchElementException("Note with id " + id + " not found");
        }
        noteDto.setId(id);
        Note note = noteMapper.toEntity(noteDto);
        Note updatedNote = noteRepository.save(note);
        return noteMapper.toDto(updatedNote);
    }

    public NoteDto getNoteById(Long id) {
        return noteRepository.findById(id)
                .map(noteMapper::toDto)
                .orElseThrow(() -> new NoSuchElementException("Note with id " + id + " not found"));
    }

    public void deleteNote(Long id) {
        if (!noteRepository.existsById(id)) {
            throw new NoSuchElementException("Note with id " + id + " not found");
        }
        noteRepository.deleteById(id);
    }

    public Page<NoteDto> getAllNotes(Pageable pageable) {
        return noteRepository.findAll(pageable)
                .map(noteMapper::toDto);
    }


}
