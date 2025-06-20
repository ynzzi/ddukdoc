package com.example.ddukdoc.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "d_qna")
public class QnA extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String content;

    private String answer;

    private String is_answerd; // 답변여부

    private LocalDateTime answeredAt;

    // 작성자 (Member 테이블 외래키)
    @ManyToOne
    @JoinColumn(name = "writer_id")
    private Member writer;

    // 답변자 (Doctor 테이블 외래키)
    @OneToOne
    @JoinColumn(name = "doctor_id")
    private Member doctor;
}
