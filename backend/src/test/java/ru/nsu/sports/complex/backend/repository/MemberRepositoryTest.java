package ru.nsu.sports.complex.backend.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.nsu.sports.complex.backend.model.Member;
import ru.nsu.sports.complex.backend.service.impl.MemberServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class MemberRepositoryTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveAndFindUser() {
        Member member = new Member(
                1,
                "Andrei Grigoriev-Apollonov",
                "andrei.grigoriev-apollonov@rambler.ru");

        when(memberRepository.save(member)).thenReturn(new Member(
                2, "Andrei Grigoriev-Apollonov", "andrei.grigoriev-apollonov@rambler.ru"));

        Member savedMember = userService.create(member);

        assertNotNull(savedMember);
        assertEquals("Andrei Grigoriev-Apollonov", savedMember.getName());
        assertEquals("andrei.grigoriev-apollonov@rambler.ru", savedMember.getEmail());
    }

    @Test
    void testFindByName() {
        Integer memberId = 1;
        Member member = new Member(
                memberId,
                "Andrei Grigoriev-Apollonov",
                "andrei.grigoriev-apollonov@rambler.ru");

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        Member foundMember = userService.find(memberId);

        assertNotNull(foundMember);
        assertEquals("Andrei Grigoriev-Apollonov", foundMember.getName());
        assertEquals("andrei.grigoriev-apollonov@rambler.ru", foundMember.getEmail());
    }
}

