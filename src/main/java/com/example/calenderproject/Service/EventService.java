package com.example.calenderproject.Service;

import com.example.calenderproject.Dto.EventDTO;
import com.example.calenderproject.Entity.EventEntity;
import com.example.calenderproject.Entity.MemberEntity;
import com.example.calenderproject.Repository.EventRepository;
import com.example.calenderproject.Repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class EventService {
    private final EventRepository eventRepository;
    private final MemberRepository memberRepository;

    public EventEntity saveEvent(EventDTO eventDTO, Long memberId) throws Exception {
        try {
            MemberEntity memberEntity = memberRepository.findById(memberId)
                    .orElseThrow(() -> new Exception("해당 유저가 없습니다"));


            LocalDateTime startTime = eventDTO.getStartTime();
            LocalDateTime endTime = eventDTO.getEndTime();

            // 9시간을 더한 시간을 얻기 (로컬 라이징)
            LocalDateTime adjustedStartTime = startTime.plusHours(9);
            LocalDateTime adjustedEndTime = endTime.plusHours(9);

            EventEntity eventEntity = eventDTO.toEntity();
            eventEntity.setMemberEntity(memberEntity);
            eventEntity.setStartTime(adjustedStartTime);
            eventEntity.setEndTime(adjustedEndTime);

            eventRepository.save(eventEntity);
            return eventEntity;

        } catch (Exception error) {
            log.info("save error : " + error.getMessage());
            throw new Exception("save error : " + error.getMessage());
        }
    }

    public List<EventEntity> getEvent(Long memberId) throws Exception {
        try{
            List<EventEntity> events = eventRepository.fetchByMemberId(memberId);
            return events;
        }catch (Exception error) {
            log.info("save error : " + error.getMessage());
            throw new Exception("save error : " + error.getMessage());
        }
    }



}
