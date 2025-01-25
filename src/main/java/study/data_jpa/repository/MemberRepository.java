package study.data_jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.data_jpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> { //<Entity, PK type>

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findHelloBy(); //By 뒤에 조건 안넣으면 전체 조회
    List<Member> findTop3HelloBy(); //By 뒤에 조건 안넣으면 전체 조회
}
