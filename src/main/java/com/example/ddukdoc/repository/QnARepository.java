package com.example.ddukdoc.repository;

import com.example.ddukdoc.entity.Member;
import com.example.ddukdoc.entity.QnA;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QnARepository extends JpaRepository<QnA, Integer> {

    List<QnA> findByDoctorIdIn(List<Integer> doctorIds);

    List<QnA> findByDoctorId(Integer doctorId);

    List<QnA> findAllByWriter(Member member);

    List<QnA> findByIsAnswered(String isAnswered);

    // 의사 username 기준
    List<QnA> findByDoctorUsername(String username);

    List<QnA> findByIsAnsweredAndDoctor(String isAnswered, Member doctor);


}
