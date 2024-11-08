package shop.S5G.shop.service.member.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.S5G.shop.dto.memberStatus.MemberStatusRequestDto;
import shop.S5G.shop.dto.memberStatus.MemberStatusResponseDto;
import shop.S5G.shop.entity.member.MemberStatus;
import shop.S5G.shop.exception.member.MemberStatusAlreadyExistsException;
import shop.S5G.shop.exception.member.MemberStatusNotFoundException;
import shop.S5G.shop.repository.member.MemberStatusRepository;
import shop.S5G.shop.service.member.MemberStatusService;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberStatusServiceImpl implements MemberStatusService {

    private final MemberStatusRepository memberStatusRepository;

    @Override
    public void saveMemberStatus(MemberStatusRequestDto memberStatusRequestDto) {
        if (memberStatusRepository.existsByTypeName(memberStatusRequestDto.typeName())) {
            throw new MemberStatusAlreadyExistsException(
                memberStatusRequestDto.typeName() + " 등록하려는 상태가 이미 존재합니다");
        }
        memberStatusRepository.save(new MemberStatus(memberStatusRequestDto.typeName()));
    }

    @Override
    public void updateMemberStatus(long memberStatusId,
        MemberStatusRequestDto memberStatusRequestDto) {
        if (!memberStatusRepository.existsById(memberStatusId)) {
            throw new MemberStatusNotFoundException("변경하려는 상태가 존재하지 않습니다");
        }
        memberStatusRepository.updateMemberStatus(memberStatusId, memberStatusRequestDto);
    }

    @Transactional(readOnly = true)
    @Override
    public MemberStatusResponseDto getMemberStatus(long memberStatusId) {
        MemberStatus memberStatus = memberStatusRepository.findById(memberStatusId)
            .orElseThrow(() -> new MemberStatusNotFoundException("조회하려는 상태가 존재하지 않습니다"));

        return MemberStatusResponseDto.toDto(memberStatus);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MemberStatusResponseDto> getAllMemberStatus() {
        return memberStatusRepository.findAll()
            .stream()
            .map(MemberStatusResponseDto::toDto
            )
            .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public MemberStatus getMemberStatusByTypeName(String typeName) {
        if (!memberStatusRepository.existsByTypeName(typeName)) {
            throw new MemberStatusAlreadyExistsException(typeName + " 존재하지 않습니다");
        }
        return memberStatusRepository.findByTypeName(typeName);
    }

    @Override
    public void deleteMemberStatus(long memberStatusId) {
        if (!memberStatusRepository.existsById(memberStatusId)) {
            throw new MemberStatusNotFoundException("삭제하려는 상태가 존재하지 않습니다");
        }
        memberStatusRepository.deleteById(memberStatusId);
    }
}
