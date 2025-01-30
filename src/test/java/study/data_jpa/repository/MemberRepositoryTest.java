package study.data_jpa.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.data_jpa.dto.MemberDto;
import study.data_jpa.entity.Member;
import study.data_jpa.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember() throws Exception {
        //given
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        //when
        Member findMember = memberRepository.findById(savedMember.getId()).get();

        //then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void findByUsernameAndAgeGreaterThen() throws Exception {
        //given
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("AAA", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);

        //when

        //then

    }

    @Test
    public void findHelloBy() throws Exception {
        //given
        List<Member> helloBy = memberRepository.findHelloBy();
        List<Member> helloByLimit = memberRepository.findTop3HelloBy();
        //when

        //then

    }

    @Test
    public void testQuery() throws Exception {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 10);
        assertThat(result.get(0)).isEqualTo(m1);
        //when

        //then

    }

    @Test
    public void findUsernameList() throws Exception {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> userNameList = memberRepository.findUserNameList();
        for (String s : userNameList) {
            System.out.println("s = " + s);
        }
        //when

        //then

    }

    @Test
    public void findMemberDto() throws Exception {
        //given
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA", 10);
        m1.setTeam(team);
        memberRepository.save(m1);

        List<MemberDto> userNameList = memberRepository.findMemberDto();
        for (MemberDto memberDto : userNameList) {
            System.out.println("memberDto = " + memberDto);
        }
        //when

        //then

    }

    @Test
    public void findByNames() throws Exception {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member member : result) {
            System.out.println("member = " + member);
        }
        //when

        //then

    }

    @Test
    public void returnType() throws Exception {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

//        List<Member> aaa = memberRepository.findListByUsername("AAA"); // 가능
//        Member findMember = memberRepository.findMemberByUsername("AAA"); // 가능
//        Optional<Member> aaa = memberRepository.findOptionalByUsername("AAA"); //가능
        Optional<Member> findMember = memberRepository.findOptionalByUsername("asd"); //결과가 있을거 같기도하고 없을거 같으면 Optional 사용하기
        System.out.println("findMember: " + findMember);

        //when

        //then

    }


    @Test
    public void paging() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));
        memberRepository.save(new Member("member6", 10));
        memberRepository.save(new Member("member7", 10));
        memberRepository.save(new Member("member8", 10));
        memberRepository.save(new Member("member9", 10));
        memberRepository.save(new Member("member10", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        //when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

        //then
        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();

        assertThat(content.size()).isEqualTo(3); //페이징 출력 개수
        assertThat(page.getTotalElements()).isEqualTo(10); //조회 총 개수
        assertThat(page.getNumber()).isEqualTo(0); //현재 페이지 번호 출력 (0부터임)
        assertThat(page.getTotalPages()).isEqualTo(4); //전체 페이지 개수
        assertThat(page.isFirst()).isTrue(); //첫번째 페이지인가?
        assertThat(page.hasNext()).isTrue(); //다음 페이지가 있는가?
    }

/*
    @Test
    public void slicing() throws Exception{
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));
        memberRepository.save(new Member("member6", 10));
        memberRepository.save(new Member("member7", 10));
        memberRepository.save(new Member("member8", 10));
        memberRepository.save(new Member("member9", 10));
        memberRepository.save(new Member("member10", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        //when
        Slice<Member> page = memberRepository.findByAge(age, pageRequest);

        //then
        List<Member> content = page.getContent();
//        long totalElements = page.getTotalElements();

        assertThat(content.size()).isEqualTo(3); //페이징 출력 개수
//        assertThat(page.getTotalElements()).isEqualTo(10); //조회 총 개수 -> Slice 에 없는 기능
        assertThat(page.getNumber()).isEqualTo(0); //현재 페이지 번호 출력 (0부터임)
//        assertThat(page.getTotalPages()).isEqualTo(4); //전체 페이지 개수 -> Slice 에 없는 기능
        assertThat(page.isFirst()).isTrue(); //첫번째 페이지인가?
        assertThat(page.hasNext()).isTrue(); //다음 페이지가 있는가?
    }
*/

    @Test
    public void bulkUpdate() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        //when
        int resultCount = memberRepository.bulkAgePlus(20);//20살 이상은 모두 +1

        //bulk 연산 이후에 영속성 컨텍스트를 비워준다.
//        em.clear();

        List<Member> result = memberRepository.findListByUsername("member5");
        Member member5 = result.get(0);
        System.out.println("member5 = " + member5);

        //then
        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberLazy() throws Exception {
        //given
        //member1 -> teamA
        //member2 => teamB
        Team teamA = new Team("TeamA");
        Team teamB = new Team("TeamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        //when N + 1 ISSUE 발생
        // select Member == 1
        List<Member> members = memberRepository.findMemberFetchJoin();

        for (Member member : members) {
            System.out.println("member = " + member.getUsername());
            // select Team
            System.out.println("member.team = " + member.getTeam().getName()); // Team == N

        }

        //then

    }

    @Test
    public void queryHint() throws Exception {
        //given
        Member member1 = memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();

        //when
//        Member findMember = memberRepository.findById(member1.getId()).get();
//        findMember.setUsername("member2");

        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2");

        em.flush();
        //then

    }

    @Test
    public void lock() throws Exception {
        //given
        Member member1 = memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();
        //when
        List<Member> result = memberRepository.findLockByUsername("member1");

        //then

    }

    @Test
    public void callCustom() throws Exception{
        //given
        List<Member> result = memberRepository.findMemberCustom();
        //when

        //then

    }
}