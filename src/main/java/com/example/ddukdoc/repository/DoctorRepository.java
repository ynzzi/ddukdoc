package com.example.ddukdoc.repository;

import com.example.ddukdoc.entity.Doctor;
import com.example.ddukdoc.entity.QnA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    //진료과 이름 조회(목록)
    @Query("SELECT DISTINCT d.treatType FROM Doctor d")
    List<String> findDistinctTreatTypes();

    //선택한 진료과 의사들 조회
    List<Doctor> findByTreatType(String treatType);
}
