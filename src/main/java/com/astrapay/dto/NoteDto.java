package com.astrapay.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@Builder
public class NoteDto {
    private Long id;
    @NotEmpty
    private String title;
    @NotEmpty
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
