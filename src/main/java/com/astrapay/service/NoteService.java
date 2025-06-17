package com.astrapay.service;

import com.astrapay.dto.NoteDto;
import com.astrapay.entity.Note;
import com.astrapay.mapper.NoteMapper;
import com.astrapay.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

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
        return noteRepository.findById(id)
                .map(existingNote -> {
                    existingNote.setTitle(noteDto.getTitle());
                    existingNote.setContent(noteDto.getContent());
                    Note updatedNote = noteRepository.save(existingNote);
                    return noteMapper.toDto(updatedNote);
                })
                .orElseThrow(() -> new NoSuchElementException("Note with id " + id + " not found"));
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
        return noteRepository.findAll(pageable).map(noteMapper::toDto);
    }

}
