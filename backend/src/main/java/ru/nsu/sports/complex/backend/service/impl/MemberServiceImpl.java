package ru.nsu.sports.complex.backend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.nsu.sports.complex.backend.model.Member;
import ru.nsu.sports.complex.backend.repository.MemberRepository;
import ru.nsu.sports.complex.backend.service.MemberService;
import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {
    private final Logger LOGGER = LoggerFactory.getLogger(MemberServiceImpl.class);
    private final MemberRepository memberRepository;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Member create(Member member) {
        try {
            return memberRepository.save(member);
        } catch (DataAccessException e) {
            LOGGER.error("Error creating user: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Member find(Integer id) {
        return memberRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("User not found with id: " + id));
    }

    @Override
    public Member findByName(String username) {
        return memberRepository.findByName(username);
    }

    @Override
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @Override
    public Member update(Integer id, Member member) {
        if (!memberRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
        member.setId(id);
        return memberRepository.save(member);
    }

    @Override
    public boolean delete(Integer id) {
        try {
            memberRepository.deleteById(id);
            return true;
        } catch (DataAccessException e) {
            LOGGER.info(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteAll() {
        try {
            memberRepository.deleteAll();
            return true;
        } catch (DataAccessException e) {
            LOGGER.info(e.getMessage());
            return false;
        }
    }
}