package ru.nsu.sports.complex.backend.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.nsu.sports.complex.backend.converter.MemberConverter;
import ru.nsu.sports.complex.backend.dto.MemberDTO;
import ru.nsu.sports.complex.backend.model.Member;
import ru.nsu.sports.complex.backend.service.impl.MemberServiceImpl;

import java.util.List;

class MemberControllerTest {

    @Mock
    private MemberServiceImpl userService;

    @Mock
    private MemberConverter memberConverter;

    @InjectMocks
    private MemberController memberController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadAllUsers() {
        List<Member> members = List.of(
                new Member(1, "Alexandr Astashenok", "alexandr.astashenok@gmail.com"),
                new Member(2, "Alexandr Berdnikov", "alexandr.berdnikov@gmail.com"),
                new Member(3, "Alexey Kabanov", "alexey.kabanov@gmail.com"),
                new Member(4, "Pavel Artemyev", "pavel.artemyev@gmail.com")
        );
        when(userService.findAll()).thenReturn(members);

        ResponseEntity<List<Member>> response = memberController.loadAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(4, response.getBody().size());
        assertEquals("Alexandr Astashenok", response.getBody().get(0).getName());
        assertEquals("Alexandr Berdnikov", response.getBody().get(1).getName());
        assertEquals("Alexey Kabanov", response.getBody().get(2).getName());
        assertEquals("Pavel Artemyev", response.getBody().get(3).getName());
    }

    @Test
    void testLoadOneUser() {
        Member member = new Member(1, "Alexandr Astashenok", "alberto@gmail.com");
        MemberDTO memberDTO = new MemberDTO(1, "Alexandr Astashenok", "alberto@gmail.com");

        when(userService.find(1)).thenReturn(member);
        when(memberConverter.memberToDTO(member)).thenReturn(memberDTO);

        ResponseEntity<MemberDTO> response = memberController.loadOne(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(memberDTO.getName(), response.getBody().getName());
    }

    @Test
    void testCreateUser() {
        Member member = new Member(null, "Alberto", "alberto@gmail.com");
        MemberDTO memberDTO = new MemberDTO(null, "Alberto", "alberto@gmail.com");

        when(memberConverter.DTOtoMember(memberDTO)).thenReturn(member);
        when(userService.create(member)).thenReturn(new Member(1, "Alberto", "alberto@gmail.com"));
        when(memberConverter.memberToDTO(any())).thenReturn(new MemberDTO(1, "Alberto", "alberto@gmail.com"));

        ResponseEntity<MemberDTO> response = memberController.create(memberDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getId());
    }

    @Test
    void testUpdateUser() {
        Integer userId = 1;
        MemberDTO memberDTO = new MemberDTO(userId, "Updated Alberto", "alberto.updated@gmail.com");
        Member updatedMember = new Member(userId, "Updated Alberto", "alberto.updated@gmail.com");

        when(memberConverter.DTOtoMember(memberDTO)).thenReturn(updatedMember);
        when(userService.update(userId, updatedMember)).thenReturn(updatedMember);
        when(memberConverter.memberToDTO(updatedMember)).thenReturn(memberDTO);

        ResponseEntity<MemberDTO> response = memberController.update(userId, memberDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Alberto", response.getBody().getName());
        assertEquals("alberto.updated@gmail.com", response.getBody().getEmail());
        verify(userService, times(1)).update(userId, updatedMember);
    }

    @Test
    void testDeleteUser() {
        when(userService.delete(1)).thenReturn(true);

        ResponseEntity<Void> response = memberController.delete(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
