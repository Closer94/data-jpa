package study.data_jpa.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id") //db 테이블에서 member_id 로 생성
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY) //연관관계 맵핑 되어있는 부분에 반드시 LAZY로 설정하기
    @JoinColumn(name = "team_id") //다대일 중 다에 Foreign key 걸기
    private Team team;

//    protected Member(){ //@Entity 에는 기본생성자가 있어야함. (private 는 안됨)
//
//    }

    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }
    }

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this); //연관관계 맵핑되어 있는 team도 같이 바꿔준다.
    }
}
