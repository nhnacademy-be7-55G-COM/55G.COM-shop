package shop.s5g.shop.service.member.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.address.AddressResponseDto;
import shop.s5g.shop.dto.coupon.MemberRegisteredEvent;
import shop.s5g.shop.dto.customer.CustomerResponseDto;
import shop.s5g.shop.dto.member.IdCheckResponseDto;
import shop.s5g.shop.dto.member.LoginResponseDto;
import shop.s5g.shop.dto.member.MemberDetailResponseDto;
import shop.s5g.shop.dto.member.MemberLoginIdResponseDto;
import shop.s5g.shop.dto.member.MemberRegistrationRequestDto;
import shop.s5g.shop.dto.member.MemberResponseDto;
import shop.s5g.shop.dto.member_grade.MemberGradeResponseDto;
import shop.s5g.shop.dto.member_status.MemberStatusResponseDto;
import shop.s5g.shop.dto.point.PointHistoryCreateRequestDto;
import shop.s5g.shop.entity.member.Customer;
import shop.s5g.shop.entity.member.Member;
import shop.s5g.shop.entity.member.MemberGrade;
import shop.s5g.shop.entity.member.MemberStatus;
import shop.s5g.shop.exception.member.MemberAlreadyExistsException;
import shop.s5g.shop.exception.member.MemberNotFoundException;
import shop.s5g.shop.exception.member.PasswordIncorrectException;
import shop.s5g.shop.exception.payco.AlreadyLinkAccountException;
import shop.s5g.shop.exception.payco.PaycoNotLinkedException;
import shop.s5g.shop.repository.member.MemberRepository;
import shop.s5g.shop.service.member.AddressService;
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
    private final PasswordEncoder passwordEncoder;
    private final AddressService addressService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional(readOnly = true)
    public Member getMember(String loginId) {
        if (!memberRepository.existsByLoginIdAndStatus_TypeName(loginId,
            MemberRepository.ACTIVE_STATUS)) {
            throw new MemberNotFoundException();
        }

        return memberRepository.findByLoginIdAndStatus_TypeName(loginId,
            MemberRepository.ACTIVE_STATUS);
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponseDto getLoginDto(String loginId) {

        MemberStatus withdrawal = memberStatusService.getMemberStatusByTypeName("WITHDRAWAL");

        Optional<Member> memberOpt = memberRepository.findByLoginIdAndStatusNot(loginId,
            withdrawal);
        Member member = memberOpt.orElseThrow(MemberNotFoundException::new);

        return new LoginResponseDto(member.getLoginId(), member.getPassword());
    }

    @Override
    public void saveMember(MemberRegistrationRequestDto memberRegistrationRequestDto) {
        if (memberRepository.existsByLoginId(memberRegistrationRequestDto.loginId())) {
            throw new MemberAlreadyExistsException("이미 존재하는 회원입니다");
        }
        MemberStatus memberStatus = memberStatusService.getMemberStatusByTypeName(
            MemberRepository.ACTIVE_STATUS);
        MemberGrade memberGrade = memberGradeService.getGradeByName("일반");

        Customer customer = customerService.addCustomerByMember(
            new Customer(null, memberRegistrationRequestDto.name(),
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

        eventPublisher.publishEvent(new MemberRegisteredEvent(saved));

    }

    @Override
    @Transactional(readOnly = true)
    public MemberDetailResponseDto getMemberDto(String loginId) {
        if (!memberRepository.existsByLoginIdAndStatus_TypeName(loginId,
            MemberRepository.ACTIVE_STATUS)) {
            throw new MemberNotFoundException();
        }
        Member member = memberRepository.findByLoginIdAndStatus_TypeName(loginId,
            MemberRepository.ACTIVE_STATUS);
        CustomerResponseDto customer = customerService.getCustomer(member.getId());
        List<AddressResponseDto> addresses = addressService.getAddresses(member.getId());

        return new MemberDetailResponseDto(
            member.getId(),
            MemberStatusResponseDto.toDto(member.getStatus()),
            MemberGradeResponseDto.toDto(member.getGrade()),
            member.getLoginId(),
            member.getPassword(), member.getBirth(), customer.name(), customer.email(),
            customer.phoneNumber(), member.getCreatedAt(),
            member.getLatestLoginAt(), member.getPoint(),
            addresses
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberResponseDto> findAllMembers(Pageable pageable) {
        return memberRepository.findByStatus_TypeName(MemberRepository.ACTIVE_STATUS)
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
        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);
        MemberStatus memberStatus = memberStatusService.getMemberStatusByTypeName(
            MemberRepository.WITHDRAWAL_STATUS);
        member.setStatus(memberStatus);
    }

    @Override
    public void updateLatestLoginAt(String loginId) {
        if (!memberRepository.existsByLoginIdAndStatus_TypeName(loginId,
            MemberRepository.ACTIVE_STATUS)) {
            throw new MemberNotFoundException();
        }
        memberRepository.updateLatestLoginAt(loginId, LocalDateTime.now());
    }

    @Override
    public IdCheckResponseDto isExistsByLoginId(String loginId) {
        return new IdCheckResponseDto(memberRepository.existsByLoginId(loginId));
    }

    @Override
    @Transactional(readOnly = true)
    public MemberLoginIdResponseDto getMemberLoginIdDtoByPaycoId(String paycoId) {
        Member member = memberRepository.findByPaycoIdNo(paycoId)
            .orElseThrow(() -> new PaycoNotLinkedException("account not linked"));
        return new MemberLoginIdResponseDto(member.getLoginId());
    }

    @Override
    public void changePassword(Long customerId, String oldPassword, String newPassword) {
        Member member = memberRepository.findById(customerId)
            .orElseThrow(MemberNotFoundException::new);

        if (!passwordEncoder.matches(oldPassword, member.getPassword())) {
            throw new PasswordIncorrectException("현재 비밀번호가 일치하지 않습니다.");
        }

        member.setPassword(passwordEncoder.encode(newPassword));
    }

    @Override
    public void linkAccountByPaycoId(Long customerId, String paycoId) {
        if (memberRepository.existsByPaycoIdNo(paycoId)) {
            throw new AlreadyLinkAccountException("already linked account");
        }

        Member member = memberRepository.findById(customerId)
            .orElseThrow(MemberNotFoundException::new);

        if (member.getPaycoIdNo() != null) {
            throw new AlreadyLinkAccountException("already linked account");
        }
        member.setPaycoIdNo(paycoId);
    }

    @Override
    public MemberStatusResponseDto getMemberStatusDtoByloginId(String loginId) {
        MemberStatus withdrawal = memberStatusService.getMemberStatusByTypeName("WITHDRAWAL");

        Optional<Member> memberOpt = memberRepository.findByLoginIdAndStatusNot(loginId,
            withdrawal);
        Member member = memberOpt.orElseThrow(MemberNotFoundException::new);

        return MemberStatusResponseDto.toDto(member.getStatus());
    }

    @Override
    public void changeStatusToActive(String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
            .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));
        MemberStatus memberStatus = memberStatusService.getMemberStatusByTypeName("ACTIVE");
        member.setStatus(memberStatus);
    }
}
