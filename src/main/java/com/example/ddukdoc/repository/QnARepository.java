package com.example.ddukdoc.repository;

import com.example.ddukdoc.entity.Member;
import com.example.ddukdoc.entity.QnA;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QnARepository extends JpaRepository<QnA, Integer> {
    List<QnA> findAllByWriter(Member member);
}
