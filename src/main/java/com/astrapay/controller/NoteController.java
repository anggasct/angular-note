package com.astrapay.controller;

import com.astrapay.dto.NoteDto;
import com.astrapay.exception.ExampleException;
import com.astrapay.service.NoteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notes")
@Api(value = "NoteController")
@Slf4j
public class NoteController {
    private final NoteService noteService;

    @PostMapping
    @ApiOperation(value = "Create a new note")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 201, message = "Created", response = NoteDto.class),
                    @ApiResponse(code = 400, message = "Bad Request")
            }
    )
    public ResponseEntity<NoteDto> createNote(@Valid @RequestBody NoteDto noteDto) {
        try {
            NoteDto createdNote = noteService.createNote(noteDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdNote);
        } catch (ExampleException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping
    @ApiOperation(value = "Get all notes with pagination")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "OK")
            }
    )
    public ResponseEntity<Page<NoteDto>> getAllNotes(Pageable pageable) {
        return ResponseEntity.ok(noteService.getAllNotes(pageable));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get a note by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "OK", response = NoteDto.class),
                    @ApiResponse(code = 404, message = "Not Found")
            }
    )
    public ResponseEntity<NoteDto> getNoteById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(noteService.getNoteById(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update a note")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "OK", response = NoteDto.class),
                    @ApiResponse(code = 404, message = "Not Found"),
                    @ApiResponse(code = 400, message = "Bad Request")
            }
    )
    public ResponseEntity<NoteDto> updateNote(@PathVariable Long id, @Valid @RequestBody NoteDto noteDto) {
        try {
            return ResponseEntity.ok(noteService.updateNote(id, noteDto));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a note")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 204, message = "No Content"),
                    @ApiResponse(code = 404, message = "Not Found")
            }
    )
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        try {
            noteService.deleteNote(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
