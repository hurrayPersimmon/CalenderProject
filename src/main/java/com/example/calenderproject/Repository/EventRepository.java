package com.example.calenderproject.Repository;

import com.example.calenderproject.Entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {
    @Query("select e from EventEntity e where e.memberEntity.id = :memberId")
    List<EventEntity> fetchByMemberId(@Param("memberId")Long memberId);
//    EventEntity deleteByMemberId(Long memberId);
}
