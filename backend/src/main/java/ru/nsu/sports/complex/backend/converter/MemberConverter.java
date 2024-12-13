package ru.nsu.sports.complex.backend.converter;

import org.springframework.stereotype.Component;
import ru.nsu.sports.complex.backend.dto.MemberDTO;
import ru.nsu.sports.complex.backend.model.Member;

@Component
public final class MemberConverter {

    public Member DTOtoMember(MemberDTO memberDTO) {
        Member member = new Member();
        member.setId(memberDTO.getId());
        member.setName(memberDTO.getName());
        member.setEmail(memberDTO.getEmail());

        if (memberDTO.getSections() != null) {
            member.setSections(memberDTO.getSections().stream()
                    .map(sectionDTO -> {
                        Section section = new Section();
                        section.setId(sectionDTO.getId());
                        return section;
                    })
                    .collect(Collectors.toSet()));
        }

        return member;
    }

    public MemberDTO memberToDTO(Member member) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(member.getId());
        memberDTO.setName(member.getName());
        memberDTO.setEmail(member.getEmail());

        if (member.getSections() != null) {
            memberDTO.setSections(member.getSections().stream()
                    .map(section -> {
                        Section sectionDTO = new Section();
                        sectionDTO.setId(section.getId());
                        sectionDTO.setName(section.getName());
                        return sectionDTO;
                    })
                    .collect(Collectors.toSet()));
        }

        return memberDTO;
    }
}
