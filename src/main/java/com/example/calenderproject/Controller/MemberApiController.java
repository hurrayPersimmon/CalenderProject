package com.example.calenderproject.Controller;

import com.example.calenderproject.Dto.MemberDto;
import com.example.calenderproject.Service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
@AllArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<String> registerMember(@RequestBody MemberDto memberDto) {
        memberService.registerMember(memberDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
