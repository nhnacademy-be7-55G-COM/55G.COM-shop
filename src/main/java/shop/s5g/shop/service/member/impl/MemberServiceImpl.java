package shop.s5g.shop.service.member.impl;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.coupon.user.UserCouponRequestDto;
import shop.s5g.shop.dto.customer.CustomerRegistrationRequestDto;
import shop.s5g.shop.dto.customer.CustomerResponseDto;
import shop.s5g.shop.dto.customer.CustomerUpdateRequestDto;
import shop.s5g.shop.dto.member.IdCheckResponseDto;
import shop.s5g.shop.dto.member.LoginResponseDto;
import shop.s5g.shop.dto.member.MemberDetailResponseDto;
import shop.s5g.shop.dto.member.MemberRegistrationRequestDto;
import shop.s5g.shop.dto.member.MemberResponseDto;
import shop.s5g.shop.dto.memberGrade.MemberGradeResponseDto;
import shop.s5g.shop.dto.memberStatus.MemberStatusResponseDto;
import shop.s5g.shop.dto.point.PointHistoryCreateRequestDto;
import shop.s5g.shop.entity.member.Customer;
import shop.s5g.shop.entity.member.Member;
import shop.s5g.shop.entity.member.MemberGrade;
import shop.s5g.shop.entity.member.MemberStatus;
import shop.s5g.shop.exception.member.MemberAlreadyExistsException;
import shop.s5g.shop.exception.member.MemberNotFoundException;
import shop.s5g.shop.exception.member.PasswordIncorrectException;
import shop.s5g.shop.repository.member.MemberRepository;
import shop.s5g.shop.service.coupon.user.UserCouponService;
import shop.s5g.shop.service.member.CustomerService;
import shop.s5g.shop.service.member.MemberGradeService;
import shop.s5g.shop.service.member.MemberService;
import shop.s5g.shop.service.member.MemberStatusService;
import shop.s5g.shop.service.point.PointHistoryService;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberStatusService memberStatusService;
    private final MemberGradeService memberGradeService;
    private final CustomerService customerService;
    private final PointHistoryService pointHistoryService;
    private final UserCouponService userCouponService;
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

        Member saved = memberRepository.save(member);

        pointHistoryService.createPointHistory(saved.getId(),
            PointHistoryCreateRequestDto.REGISTER_POINT);

        userCouponService.createWelcomeCoupon(new UserCouponRequestDto(saved.getId()));

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
        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException("회원이 존재하지 않습니다.");
        }
        MemberStatus memberStatus = memberStatusService.getMemberStatusByTypeName(
            MemberRepository.WITHDRAWAL_STATUS);
        Member member = memberRepository.findById(memberId).get();
        member.setStatus(memberStatus);
    }

    @Override
    public void updateLatestLoginAt(String loginId) {
        if (!memberRepository.existsByLoginIdAndStatus_TypeName(loginId, "ACTIVE")) {
            throw new MemberNotFoundException("회원이 존재하지 않습니다.");
        }
        memberRepository.updateLatestLoginAt(loginId, LocalDateTime.now());
    }

    @Override
    public IdCheckResponseDto isExistsByLoginId(String loginId) {
        return new IdCheckResponseDto(memberRepository.existsByLoginId(loginId));
    }

    @Override
    public void changePassword(Long customerId, String oldPassword, String newPassword) {
        Member member = memberRepository.findById(customerId)
            .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));

        if (!passwordEncoder.matches(oldPassword, member.getPassword())) {
            throw new PasswordIncorrectException("현재 비밀번호가 일치하지 않습니다.");
        }

        member.setPassword(passwordEncoder.encode(newPassword));
    }

}
