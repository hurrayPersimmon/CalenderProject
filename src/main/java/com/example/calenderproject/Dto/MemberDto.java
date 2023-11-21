package com.example.calenderproject.Dto;

import com.example.calenderproject.Entity.MemberEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberDto {
    private Long id;
    private String username;
    private String password;

    public MemberEntity toEntity() {
        return new MemberEntity(id, username, password);
    }
}
