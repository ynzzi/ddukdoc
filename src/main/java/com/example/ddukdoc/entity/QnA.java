package com.example.ddukdoc.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "d_qna")
@Data
public class QnA extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String content;

    private String answer;

    @Column(name = "is_answered")
    private String isAnswered; // 답변여부(Y/N)

    @Column(name = "answertime")
    private LocalDateTime answeredAt;

    // 작성자 (Member 테이블 외래키)
    @ManyToOne
    @JoinColumn(name = "writer_id")
    private Member writer;

    // 답변자 (Doctor 테이블 외래키)
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Member doctor;
}
