package com.example.calenderproject.Repository;

import com.example.calenderproject.Entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    MemberEntity findByUsername(String username);
}
