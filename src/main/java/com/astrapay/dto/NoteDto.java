package com.astrapay.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NoteDto {
    private Long id;
    private String title;
    private String content;
}
