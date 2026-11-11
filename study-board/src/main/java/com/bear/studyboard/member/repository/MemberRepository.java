package com.bear.studyboard.member.repository;

import com.bear.studyboard.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    @Modifying
    @Query("""
            update Member member
            set member.drawnAt = now()
            where member.id = :id
            """)
    Member withdrawMember(Long id);
}
