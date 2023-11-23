package com.example.calenderproject.Controller;

import com.example.calenderproject.Dto.EventDTO;
import com.example.calenderproject.Entity.EventEntity;
import com.example.calenderproject.Service.EventService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/event")
@AllArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class EventApiController {
    private final EventService eventService;

    //이벤트 DB에 저장하기
    @PostMapping("/save/{memberId}")
    public ResponseEntity<?> saveEvent(@RequestBody EventDTO eventDto, @PathVariable("memberId") Long memberId){
        try {
            EventEntity eventEntity = eventService.saveEvent(eventDto,memberId);
            return eventEntity != null
                    ? ResponseEntity.ok().body(eventEntity)
                    : ResponseEntity.notFound().build();
        } catch (Exception error) {
            log.info("save error : " + error.getMessage());
            return null;
        }
    }

    //이벤트 DB에서 가져오기
    @GetMapping("/get/{memberId}")
    public ResponseEntity<?> getEvent(@PathVariable("memberId") Long memberId) {
        try{
            List<EventEntity> eventEntities = (List<EventEntity>) eventService.getEvent(memberId);
            return eventEntities != null
                    ? ResponseEntity.ok().body(eventEntities)
                    : ResponseEntity.notFound().build();
        } catch (Exception error) {
            log.info("get error : " + error.getMessage());
            throw new RuntimeException("get error : " + error.getMessage());
        }
    }

    //이벤트 DB에서 수정하기
    @PatchMapping("/update/{eventId}")
    public ResponseEntity<?> updateEvent(@PathVariable("eventId") Long eventId, @RequestBody EventDTO eventDto) {
        try{
            EventEntity eventEntity = eventService.updateEvent(eventId,eventDto);
            return eventEntity != null
                    ? ResponseEntity.ok().body(eventEntity)
                    : ResponseEntity.notFound().build();
        } catch (Exception error) {
            log.info("update error : " + error.getMessage());
            throw new RuntimeException("update error : " + error.getMessage());
        }
    }


    //이벤트 DB에서 삭제하기
    @DeleteMapping("/delete/{eventId}")
    public ResponseEntity<?> deleteEvent() {
        return null;
    }
}
