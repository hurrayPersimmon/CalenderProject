package com.example.calenderproject.Controller;

import com.example.calenderproject.Dto.MemberDto;
import com.example.calenderproject.Entity.MemberEntity;
import com.example.calenderproject.Service.MemberService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
@AllArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class MemberApiController {

    private final MemberService memberService;

    //로그인 구현
    @PostMapping("/login")
    public ResponseEntity<?> loginMember(@RequestBody MemberDto memberDto) {
        try{
            MemberEntity memberEntity= memberService.loginMember(memberDto);
            return new ResponseEntity<>(memberEntity,HttpStatus.OK);
        } catch (Exception error) {
            log.info("login error : " + error.getMessage());
            return new ResponseEntity<>(error.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //회원 탈퇴 구현
    @DeleteMapping("/delete")
    @Transactional
    public ResponseEntity<?> deleteMember(@RequestBody MemberDto memberDto) {
        log.info("deleteMember : " + memberDto.getUsername());
        try{
            memberService.deleteMember(memberDto);
            return new ResponseEntity<>(memberDto,HttpStatus.OK);
        } catch (Exception error) {
            log.info("delete error : " + error.getMessage());
            return new ResponseEntity<>(error.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
