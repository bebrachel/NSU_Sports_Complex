package ru.nsu.sports.complex.backend.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.nsu.sports.complex.backend.model.Member;
import ru.nsu.sports.complex.backend.repository.MemberRepository;
import ru.nsu.sports.complex.backend.service.impl.MemberServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateMember() {
        Member member = new Member(null, "Alberto", "mandarino@gmail.com");
        when(memberRepository.save(member)).thenReturn(new Member(1, "Alberto", "mandarino@gmail.com"));

        Member createdMember = memberService.create(member);

        assertNotNull(createdMember.getId());
        assertEquals("Alberto", createdMember.getName());
        assertEquals("mandarino@gmail.com", createdMember.getEmail());
        verify(memberRepository, times(1)).save(member);
    }

    @Test
    void testFindById() {
        Integer userId = 1;
        String userName = "Alberto";
        String userEmail = "mandarino@gmail.com";
        Member member = new Member(userId, userName, userEmail);

        when(memberRepository.findById(userId)).thenReturn(Optional.of(member));

        Member result = memberService.find(userId);

        assertNotNull(result);
        assertEquals(member.getId(), result.getId());
        assertEquals(member.getName(), result.getName());
        assertEquals(member.getEmail(), result.getEmail());
        verify(memberRepository, times(1)).findById(userId);
    }

    @Test
    void testFindByName() {
        String userName = "Alberto";
        Member member = new Member(1, userName, "alberto@example.com");

        when(memberRepository.findByName(userName)).thenReturn(member);

        Member result = memberService.findByName(userName);

        assertNotNull(result);
        assertEquals(userName, result.getName());
        assertEquals("alberto@example.com", result.getEmail());
        verify(memberRepository, times(1)).findByName(userName);
    }

    @Test
    void testFindAllMembers() {
        List<Member> members = Arrays.asList(
                new Member(1, "Murilo Benício", "murilo.benício@gmail.com"),
                new Member(2, "Giovanna Antonelli", "giovanna.antonelli@gmail.com"),
                new Member(3, "Vera Fischer", "vera.fischer@gmail.com")
        );

        when(memberRepository.findAll()).thenReturn(members);

        List<Member> result = memberService.findAll();

        assertEquals(3, result.size());
        verify(memberRepository, times(1)).findAll();
    }

    @Test
    void testUpdateMember() {
        Integer memberId = 1;
        Member updatedMember = new Member(memberId, "Alberto Updated", "updated@gmail.com");

        when(memberRepository.existsById(memberId)).thenReturn(true);
        when(memberRepository.save(updatedMember)).thenReturn(updatedMember);

        Member result = memberService.update(memberId, updatedMember);

        assertEquals("Alberto Updated", result.getName());
        assertEquals("updated@gmail.com", result.getEmail());
        verify(memberRepository, times(1)).existsById(memberId);
        verify(memberRepository, times(1)).save(updatedMember);
    }

    @Test
    void testDeleteMember() {
        Integer memberId = 1;

        doNothing().when(memberRepository).deleteById(memberId);

        boolean isDeleted = memberService.delete(memberId);

        assertTrue(isDeleted);
        verify(memberRepository, times(1)).deleteById(memberId);
    }

    @Test
    void testDeleteAll() {
        doNothing().when(memberRepository).deleteAll();

        boolean isDeleted = memberService.deleteAll();

        assertTrue(isDeleted);
        verify(memberRepository, times(1)).deleteAll();
    }
}
