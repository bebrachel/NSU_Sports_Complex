package ru.nsu.sports.complex.backend.converter;

import org.springframework.stereotype.Component;
import ru.nsu.sports.complex.backend.dto.MemberDTO;
import ru.nsu.sports.complex.backend.model.Member;

@Component
public final class MemberConverter {
    public Member DTOtoMember(MemberDTO memberDTO) {
        Member member = new Member();
        member.setName(memberDTO.getName());
        member.setEmail(memberDTO.getEmail());
        return member;
    }

    public MemberDTO memberToDTO(Member member) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(member.getId());
        memberDTO.setName(member.getName());
        memberDTO.setEmail(member.getEmail());
        return memberDTO;
    }
}
