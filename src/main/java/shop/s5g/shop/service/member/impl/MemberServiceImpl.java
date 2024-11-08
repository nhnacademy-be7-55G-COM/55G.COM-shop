package shop.s5g.shop.service.member.impl;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.customer.CustomerRegistrationRequestDto;
import shop.s5g.shop.dto.customer.CustomerResponseDto;
import shop.s5g.shop.dto.customer.CustomerUpdateRequestDto;
import shop.s5g.shop.dto.member.LoginResponseDto;
import shop.s5g.shop.dto.member.MemberDetailResponseDto;
import shop.s5g.shop.dto.member.MemberRegistrationRequestDto;
import shop.s5g.shop.dto.member.MemberResponseDto;
import shop.s5g.shop.dto.memberGrade.MemberGradeResponseDto;
import shop.s5g.shop.dto.memberStatus.MemberStatusResponseDto;
import shop.s5g.shop.entity.member.Customer;
import shop.s5g.shop.entity.member.Member;
import shop.s5g.shop.entity.member.MemberGrade;
import shop.s5g.shop.entity.member.MemberStatus;
import shop.s5g.shop.exception.member.MemberAlreadyExistsException;
import shop.s5g.shop.exception.member.MemberNotFoundException;
import shop.s5g.shop.repository.member.MemberRepository;
import shop.s5g.shop.service.member.CustomerService;
import shop.s5g.shop.service.member.MemberGradeService;
import shop.s5g.shop.service.member.MemberService;
import shop.s5g.shop.service.member.MemberStatusService;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberStatusService memberStatusService;
    private final MemberGradeService memberGradeService;
    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public Member getMember(String loginId) {
        if (!memberRepository.existsByLoginIdAndStatus_TypeName(loginId, "ACTIVE")) {
            throw new MemberNotFoundException("회원이 존재하지 않습니다");
        }

        return memberRepository.findByLoginIdAndStatus_TypeName(loginId, "ACTIVE");
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponseDto getLoginDto(String loginId) {
        if (!memberRepository.existsByLoginIdAndStatus_TypeName(loginId, "ACTIVE")) {
            throw new MemberNotFoundException("회원이 존재하지 않습니다");
        }
        Member member = memberRepository.findByLoginIdAndStatus_TypeName(loginId, "ACTIVE");

        return new LoginResponseDto(member.getLoginId(), member.getPassword());
    }

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
            .password(passwordEncoder.encode(memberRegistrationRequestDto.password()))
            .loginId(memberRegistrationRequestDto.loginId())
            .birth(memberRegistrationRequestDto.birthDate())
            .createdAt(LocalDateTime.now())
            .latestLoginAt(null)
            .point(0L)
            .build();

        memberRepository.save(member);
    }

    @Override
    public void updateMember(CustomerUpdateRequestDto updateRequestDto) {

    }

    @Override
    @Transactional(readOnly = true)
    public MemberDetailResponseDto getMemberDto(String loginId) {
        if (!memberRepository.existsByLoginIdAndStatus_TypeName(loginId, "ACTIVE")) {
            throw new MemberNotFoundException("회원이 존재하지 않습니다");
        }
        Member member = memberRepository.findByLoginIdAndStatus_TypeName(loginId, "ACTIVE");
        CustomerResponseDto customer = customerService.getCustomer(member.getId());

        return new MemberDetailResponseDto(member.getId(),
            MemberStatusResponseDto.toDto(member.getStatus()),
            MemberGradeResponseDto.toDto(member.getGrade()),
            member.getLoginId(),
            member.getPassword(), member.getBirth(), customer.name(), customer.email(),
            customer.phoneNumber(), member.getCreatedAt(),
            member.getLatestLoginAt(), member.getPoint());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberResponseDto> findAllMembers(Pageable pageable) {
        return memberRepository.findByStatus_TypeName("ACTIVE")
            .stream().map(member -> new MemberResponseDto(member.getId(),
                MemberStatusResponseDto.toDto(member.getStatus()),
                MemberGradeResponseDto.toDto(member.getGrade()),
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

    @Override
    public void updateLatestLoginAt(String loginId) {
        if (!memberRepository.existsByLoginIdAndStatus_TypeName(loginId, "ACTIVE")) {
            throw new MemberNotFoundException("회원이 존재하지 않습니다.");
        }
        memberRepository.updateLatestLoginAt(loginId, LocalDateTime.now());
    }

}
