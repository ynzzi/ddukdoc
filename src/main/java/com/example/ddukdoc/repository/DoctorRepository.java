package com.example.ddukdoc.repository;

import com.example.ddukdoc.entity.Doctor;
import com.example.ddukdoc.entity.QnA;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
}
