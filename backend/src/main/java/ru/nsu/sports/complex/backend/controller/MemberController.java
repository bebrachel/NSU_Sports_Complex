package ru.nsu.sports.complex.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.sports.complex.backend.converter.MemberConverter;
import ru.nsu.sports.complex.backend.dto.MemberDTO;
import ru.nsu.sports.complex.backend.model.Member;
import ru.nsu.sports.complex.backend.service.MemberService;
import ru.nsu.sports.complex.backend.service.SectionService;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final Logger LOGGER = LoggerFactory.getLogger(MemberController.class);
    private final MemberService memberService;
    private final SectionService sectionService;
    private final MemberConverter memberConverter;

    @Autowired
    public MemberController(MemberService memberService, MemberConverter memberConverter, SectionService sectionService) {
        this.memberService = memberService;
        this.sectionService = sectionService;
        this.memberConverter = memberConverter;
    }

    @Operation(summary = "Получить список созданных пользователей.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список пользователей.", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = List.class))
            })})
    @GetMapping
    public ResponseEntity<List<Member>> loadAll() {
        LOGGER.info("start loadAll users");
        try {
            List<Member> members = memberService.findAll();
            LOGGER.info("Found {} members", members.size());
            return new ResponseEntity<>(members, HttpStatus.OK);
        } catch (DataAccessException e) {
            LOGGER.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Получить информацию о пользователе по Id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь.", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = MemberDTO.class))
            })})
    @GetMapping("/{id}")
    public ResponseEntity<MemberDTO> loadOne(@PathVariable int id) {
        LOGGER.info("start loadOne member by id: {}", id);
        try {
            Member member = memberService.find(id);
            LOGGER.info("Found: {}", member);
            return new ResponseEntity<>(memberConverter.memberToDTO(member), HttpStatus.OK);
        } catch (DataAccessException e) {
            LOGGER.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Создать нового пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Созданный пользователь.", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = MemberDTO.class))
            })})
    @PostMapping
    public ResponseEntity<MemberDTO> create(@RequestBody MemberDTO memberDTO) {
        LOGGER.info("start creating member: {}", memberDTO);
        try {
            Member member = memberService.create(memberConverter.DTOtoMember(memberDTO));
            return new ResponseEntity<>(memberConverter.memberToDTO(member), HttpStatus.CREATED);
        } catch (DataAccessException e) {
            LOGGER.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @Operation(summary = "Обновить данные существующего пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Обновленный пользователь.", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = MemberDTO.class))
            })})
    @PutMapping("/{id}")
    public ResponseEntity<MemberDTO> update(@PathVariable int id, @RequestBody MemberDTO memberDTO) {
        LOGGER.info("start update member: {}", memberDTO);
        try {
            Member member = memberService.update(id, memberConverter.DTOtoMember(memberDTO));
            return new ResponseEntity<>(memberConverter.memberToDTO(member), HttpStatus.OK);
        } catch (DataAccessException e) {
            LOGGER.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @Operation(summary = "Удалить мембера.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema())
            })})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (memberService.delete(id))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Записать мембера в секцию.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Мембер успешно записан в секцию", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            }),
            @ApiResponse(responseCode = "400", description = "Секция не найдена или заполнена", content = @Content),
            @ApiResponse(responseCode = "404", description = "Мембер не найден", content = @Content)
    })
    @PostMapping("/{memberId}/assign-to-section/{sectionId}")
    public ResponseEntity<String> enrollMemberToSection(
            @PathVariable Integer memberId,
            @PathVariable Integer sectionId) {
        memberService.enrollMemberToSection(memberId, sectionId);
        return ResponseEntity.ok("Member successfully assigned to section");
    }

    @Operation(summary = "Удалить мембера из секции.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Мембер успешно удален из секции", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            }),
            @ApiResponse(responseCode = "400", description = "Мембер не найден в секции", content = @Content),
            @ApiResponse(responseCode = "404", description = "Секция или мембер не найдены", content = @Content)
    })
    @DeleteMapping("/{memberId}/remove-from-section/{sectionId}")
    public ResponseEntity<String> removeMemberFromSection(
            @PathVariable Integer memberId,
            @PathVariable Integer sectionId) {
        sectionService.removeMemberFromSection(memberId, sectionId);
        return ResponseEntity.ok("Member successfully removed from section");
    }
}
