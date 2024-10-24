package shop.S5G.shop.service.member.impl;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import shop.S5G.shop.dto.customer.CustomerRegistrationRequestDto;
import shop.S5G.shop.dto.member.MemberRegistrationRequestDto;
import shop.S5G.shop.dto.member.MemberResponseDto;
import shop.S5G.shop.dto.member.MemberUpdateDto;
import shop.S5G.shop.entity.member.MemberGrade;
import shop.S5G.shop.entity.member.MemberStatus;
import shop.S5G.shop.repository.member.MemberRepository;
import shop.S5G.shop.service.member.CustomerService;
import shop.S5G.shop.service.member.MemberGradeService;
import shop.S5G.shop.service.member.MemberService;
import shop.S5G.shop.service.member.MemberStatusService;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberStatusService memberStatusService;
    private final MemberGradeService memberGradeService;
    private final CustomerService customerService;

    @Override
    public void saveMember(MemberRegistrationRequestDto memberRegistrationRequestDto) {
        MemberStatus memberStatus = memberStatusService.getMemberStatusByTypeName("ACTIVE");
        MemberGrade memberGrade = memberGradeService.getGradeByName("일반");

        customerService.addCustomer(
            new CustomerRegistrationRequestDto(null, memberRegistrationRequestDto.name(),
                memberRegistrationRequestDto.phoneNumber(), memberRegistrationRequestDto.email()));

    }

    @Override
    public void updateMember(MemberUpdateDto memberUpdateDto) {

    }

    @Override
    public MemberResponseDto findMember(String loginId) {
        return null;
    }

    @Override
    public List<MemberResponseDto> findAllMembers(Pageable pageable) {
        return List.of();
    }

    @Override
    public boolean existsMember(String loginId) {
        return memberRepository.existsByLoginId(loginId);
    }

    @Override
    public void deleteById(Long memberId) {
        throw new UnsupportedOperationException();
    }
}
