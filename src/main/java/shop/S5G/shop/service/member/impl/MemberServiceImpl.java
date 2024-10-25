package shop.S5G.shop.service.member.impl;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import shop.S5G.shop.dto.customer.CustomerRegistrationRequestDto;
import shop.S5G.shop.dto.member.MemberRegistrationRequestDto;
import shop.S5G.shop.dto.member.MemberResponseDto;
import shop.S5G.shop.dto.member.MemberUpdateRequestDto;
import shop.S5G.shop.entity.member.Customer;
import shop.S5G.shop.entity.member.Member;
import shop.S5G.shop.entity.member.MemberGrade;
import shop.S5G.shop.entity.member.MemberStatus;
import shop.S5G.shop.exception.member.MemberAlreadyExistsException;
import shop.S5G.shop.exception.member.MemberNotFoundException;
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
        if (memberRepository.existsByLoginId(memberRegistrationRequestDto.loginId())) {
            throw new MemberAlreadyExistsException("이미 존재하는 회원입니다");
        }
        MemberStatus memberStatus = memberStatusService.getMemberStatusByTypeName("ACTIVE");
        MemberGrade memberGrade = memberGradeService.getGradeByName("일반");

        Customer customer = customerService.addCustomer(
            new CustomerRegistrationRequestDto(null, memberRegistrationRequestDto.name(),
                memberRegistrationRequestDto.phoneNumber(), memberRegistrationRequestDto.email()));

        Member member = Member.builder()
            .customer(customer)
            .status(memberStatus)
            .grade(memberGrade)
            .password(memberRegistrationRequestDto.password())
            .loginId(memberRegistrationRequestDto.loginId())
            .birth(memberRegistrationRequestDto.birthDate())
            .createdAt(LocalDateTime.now())
            .latestLoginAt(null)
            .build();

        memberRepository.save(member);
    }

    @Override
    public void updateMember(MemberUpdateRequestDto memberUpdateRequestDto) {
        //TODO View 마이 페이지 구현 후 진행 예정
    }

    @Override
    public MemberResponseDto findMember(String loginId) {
        if (!memberRepository.existsByLoginIdAndStatus_TypeName(loginId, "ACTIVE")) {
            throw new MemberNotFoundException("회원이 존재하지 않습니다");
        }
        Member member = memberRepository.findByLoginIdAndStatus_TypeName(loginId, "ACTIVE");

        return new MemberResponseDto(member.getStatus(), member.getGrade(), member.getLoginId(),
            member.getPassword(), member.getBirth(), member.getCreatedAt(),
            member.getLatestLoginAt(), member.getPoint());
    }

    @Override
    public List<MemberResponseDto> findAllMembers(Pageable pageable) {
        return memberRepository.findByStatus_TypeName("ACTIVE")
            .stream().map(member -> new MemberResponseDto(member.getStatus(), member.getGrade(),
                member.getLoginId(),
                member.getPassword(), member.getBirth(), member.getCreatedAt(),
                member.getLatestLoginAt(), member.getPoint()))
            .toList();
    }

    @Override
    public void deleteById(Long memberId) {
        //TODO 추후 진행 예정
        throw new UnsupportedOperationException();
    }
}
