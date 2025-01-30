package study.data_jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.LocalDateTime;

@MappedSuperclass //부모 클래스에 선언하고 속성만 상속 받아서 사용하고 싶을 때 사용 (상속X)
@Getter
public class JpaBaseEntity {

    @Column(updatable = false)
    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    @PrePersist //저장하기 전에 아레 메소드 실행
    public void prePersist(){
        LocalDateTime now = LocalDateTime.now();
        createDate = now;
        updateDate = now;
    }

    @PreUpdate //수정하기 전에 아래 메소드 실행
    public void preUpdate() {
        updateDate = LocalDateTime.now();
    }


}
