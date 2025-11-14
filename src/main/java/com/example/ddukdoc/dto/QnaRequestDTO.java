package com.example.ddukdoc.dto;

import lombok.Data;

@Data
public class QnaRequestDTO {
    private Integer doctorId;
    private String title;
    private String content;
}
