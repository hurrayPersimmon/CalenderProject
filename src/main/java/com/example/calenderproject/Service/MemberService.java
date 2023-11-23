package com.example.calenderproject.Service;

import com.example.calenderproject.Dto.MemberDTO;
import com.example.calenderproject.Entity.MemberEntity;
import com.example.calenderproject.Repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
@AllArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;


    //로그인 구현
    public MemberEntity loginMember(MemberDTO memberDto) throws Exception {
        if (memberRepository.existsByUsername(memberDto.getUsername())) {
            try {
                checkPassword(memberDto);
                return memberRepository.findByUsername(memberDto.getUsername());
            } catch (Exception error) {
                throw new Exception("아이디 혹은 비밀번호를 확인하세요");
            }
        } else {
            //신규 유저, 가천대 로그인 API 호출
            return gachonLogin(memberDto);
        }
    }

    //회원 탈퇴 구현
    public MemberEntity deleteMember(MemberDTO memberDto) throws Exception {
        //비밀번호 확인
        if(checkPassword(memberDto)) {
            memberRepository.deleteById(memberDto.getId());
            log.info("회원 탈퇴 성공");
            return memberDto.toEntity();
        }
        else {
            log.info("회원 탈퇴 실패");
            throw new Exception("회원 탈퇴 실패");
        }
    }

    //DB내 비밀번호 맞는지 조회
    private Boolean checkPassword(MemberDTO memberDto) throws Exception {
        String username = memberDto.getUsername();
        String password = memberDto.getPassword();

        MemberEntity member = memberRepository.findByUsername(username);
        if(member.getPassword().equals(encodePassword(password))) {
            log.info("기존 유저 로그인 성공");
            return true;
        }
        else {
            log.info("password 오류 : " + encodePassword(password) + " " + password);
            throw new Exception("아이디 혹은 비밀번호를 확인하세요");
        }
    }


    //가천대학교 로그인 API
    private MemberEntity gachonLogin(MemberDTO memberDto) throws Exception{
        String username = memberDto.getUsername();
        String password = memberDto.getPassword();

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://smart.gachon.ac.kr:8080/WebJSON";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        org.json.JSONObject jsonObject= new org.json.JSONObject();

        jsonObject.put("fsp_cmd","login");
        jsonObject.put("DVIC_ID","dwFraM1pVhl6mMn4npgL2dtZw7pZxw2lo2uqpm1yuMs=");
        jsonObject.put("fsp_action","UserAction");
        jsonObject.put("USER_ID",username); //user ID 사용자에게 받은 값 입력
        log.info(username);
        jsonObject.put("PWD",password); //user PW 사용자에게 받은 값 입력
        jsonObject.put("APPS_ID","com.sz.Atwee.gachon");

        HttpEntity<String> requestMessage = new HttpEntity<>(jsonObject.toString(), httpHeaders); //Request&Header Setting
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestMessage, String.class); //호출 하여 Response 받기

        JSONObject data = new JSONObject(response.getBody());

        if(data.getString("ErrorCode").equals("0")) {
            //신규 유저, DB에 저장
            //password 암호화 함수 호출 후 저장
            memberDto.setPassword(encodePassword(memberDto.getPassword()));
            memberRepository.save(memberDto.toEntity());
            log.info("신규 유저 저장 완료");
            return memberRepository.findByUsername(username);
        }
        else {
            log.info("password 오류 : " + data.getString("ErrorCode") + " " + encodePassword(password));
            throw new Exception("아이디 혹은 비밀번호를 확인하세요");
        }

    }

    //password 암호화
    private static String encodePassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes());

        StringBuilder hexString = new StringBuilder();
        for(byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

}

