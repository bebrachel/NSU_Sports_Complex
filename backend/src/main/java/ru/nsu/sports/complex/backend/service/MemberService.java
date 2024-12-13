package ru.nsu.sports.complex.backend.service;

import ru.nsu.sports.complex.backend.model.Member;
import java.util.List;

public interface MemberService {
    Member find(Integer id);
    Member findByName(String name);
    List<Member> findAll();
    Member create(Member object);
    Member update(Integer id, Member object);
    boolean delete(Integer id);
    boolean deleteAll();
}
