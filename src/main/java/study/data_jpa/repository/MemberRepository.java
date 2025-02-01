package study.data_jpa.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.data_jpa.dto.MemberDto;
import study.data_jpa.entity.Member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom { //<Entity, PK type>

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findHelloBy(); //By 뒤에 조건 안넣으면 전체 조회

    List<Member> findTop3HelloBy(); //By 뒤에 조건 안넣으면 전체 조회

    //@Query 를 이용해서 JPQL 메서드 바로 주입 (*이름이 없는 네임드 쿼리)
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    //List를 반환하고 싶은 경우
    @Query("select m.username from Member m")
    List<String> findUserNameList();

    //조회 후 DTO 를 활용해서 반환하고 싶은 경우
    @Query("select new study.data_jpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    //JPQL 에 여러개의 이름을 조건으로 넣고 싶을 때,
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    //* 다양한 반환 타입 제공 *//
    //case1. 반환 타입: 컬렉션
    List<Member> findListByUsername(String username);
    //case2. 반환 타입: 단건
    Member findMemberByUsername(String username); //단건
    //case3. 반환 타입: 단건 Optional
    Optional<Member> findOptionalByUsername(String username); //단건 Optional

    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);

//    Slice<Member> findByAge(int age, Pageable pageable);

    @Modifying(clearAutomatically = true) //excuteUpate가 날라가게 된다.
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"}) //team 도 같이 조회하고 싶을 때 사용 (fetch join JPQL 작성할 필요없음)
    List<Member> findAll();

    @EntityGraph(attributePaths = "{team}")
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    @QueryHints(value=@QueryHint(name="org.hibernate.readOnly", value="true")) //변경 감지 체크를 안한다. 오직 읽기만!
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);

    @Query(value = "select * from member where username =?", nativeQuery = true)
    Member findByNativeQuery(String username);

    @Query(value="select m.member_id as id, m.username, t.name as teamName from member m" +
            " left join team t",
            countQuery = "select count(*) from member",
            nativeQuery = true)
    Page<MemberProjection> findByNativeProjection(Pageable pageable);
}
