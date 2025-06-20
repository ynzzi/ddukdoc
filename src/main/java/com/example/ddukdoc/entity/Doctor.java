package com.example.ddukdoc.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "doctor_info")
public class Doctor {

    @Id
    private Integer id;  // Member의 id를 그대로 사용 (자동 증가 X)

    @OneToOne
    @MapsId
    @JoinColumn(name = "id") // doctor_info.id = member.id
    private Member member;

    private String hospital;

    @Column(name = "treat_type")
    private String treatType; // 진료과목

    @Column(name = "h_time")
    private String hTime; // 진료시간
}

