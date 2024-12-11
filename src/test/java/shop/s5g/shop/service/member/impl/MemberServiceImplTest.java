package shop.s5g.shop.service.member.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import shop.s5g.shop.dto.address.AddressResponseDto;
import shop.s5g.shop.dto.coupon.MemberRegisteredEvent;
import shop.s5g.shop.dto.customer.CustomerResponseDto;
import shop.s5g.shop.dto.member.IdCheckResponseDto;
import shop.s5g.shop.dto.member.LoginResponseDto;
import shop.s5g.shop.dto.member.MemberDetailResponseDto;
import shop.s5g.shop.dto.member.MemberLoginIdResponseDto;
import shop.s5g.shop.dto.member.MemberRegistrationRequestDto;
import shop.s5g.shop.dto.member.MemberResponseDto;
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
import shop.s5g.shop.service.member.MemberStatusService;
import shop.s5g.shop.service.point.PointHistoryService;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @InjectMocks
    private MemberServiceImpl memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberStatusService memberStatusService;

    @Mock
    private MemberGradeService memberGradeService;

    @Mock
    private CustomerService customerService;

    @Mock
    private PointHistoryService pointHistoryService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AddressService addressService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Test
    void getMember_shouldReturnMember_whenExists() {
        String loginId = "testUser";
        MemberStatus activeStatus = new MemberStatus(1L, "ACTIVE");
        Member member = new Member(1L, null, activeStatus, null, loginId, "password123", null,
            LocalDateTime.now(), null, 0L, null);

        when(memberRepository.existsByLoginIdAndStatus_TypeName(loginId,
            MemberRepository.ACTIVE_STATUS))
            .thenReturn(true);
        when(memberRepository.findByLoginIdAndStatus_TypeName(loginId,
            MemberRepository.ACTIVE_STATUS))
            .thenReturn(member);

        Member result = memberService.getMember(loginId);

        assertThat(result.getLoginId()).isEqualTo(loginId);
        verify(memberRepository).existsByLoginIdAndStatus_TypeName(loginId,
            MemberRepository.ACTIVE_STATUS);
        verify(memberRepository).findByLoginIdAndStatus_TypeName(loginId,
            MemberRepository.ACTIVE_STATUS);
    }

    @Test
    void getMember_shouldThrowException_whenNotFound() {
        String loginId = "testUser";

        when(memberRepository.existsByLoginIdAndStatus_TypeName(loginId,
            MemberRepository.ACTIVE_STATUS))
            .thenReturn(false);

        assertThatThrownBy(() -> memberService.getMember(loginId))
            .isInstanceOf(MemberNotFoundException.class);

        verify(memberRepository).existsByLoginIdAndStatus_TypeName(loginId,
            MemberRepository.ACTIVE_STATUS);
    }

    @Test
    void saveMember_shouldSaveMember_whenValidRequest() {
        // Given
        MemberRegistrationRequestDto request = new MemberRegistrationRequestDto(
            "Test User", "test@example.com", "testUser", "password123", "01012345678", "19900101"
        );

        MemberStatus activeStatus = new MemberStatus(1L, "ACTIVE");
        MemberGrade defaultGrade = new MemberGrade(1L, "일반", 0, 0, true);
        Customer customer = new Customer(null, request.name(), request.phoneNumber(),
            request.email());

        Member savedMember = new Member(
            1L, customer, activeStatus, defaultGrade, request.loginId(), "encodedPassword",
            request.birthDate(), LocalDateTime.now(), null, 0L, null
        );

        when(memberRepository.existsByLoginId(request.loginId())).thenReturn(false);
        when(memberStatusService.getMemberStatusByTypeName(
            MemberRepository.ACTIVE_STATUS)).thenReturn(activeStatus);
        when(memberGradeService.getGradeByName("일반")).thenReturn(defaultGrade);
        when(customerService.addCustomerByMember(any(Customer.class))).thenReturn(customer);
        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");
        when(memberRepository.save(any(Member.class))).thenReturn(savedMember);

        // When
        memberService.saveMember(request);

        // Then
        verify(memberRepository).existsByLoginId(request.loginId());
        verify(memberRepository).save(any(Member.class));
        verify(pointHistoryService).createPointHistory(eq(1L),
            eq(PointHistoryCreateRequestDto.REGISTER_POINT));
        verify(eventPublisher).publishEvent(any(MemberRegisteredEvent.class));
    }


    @Test
    void saveMember_shouldThrowException_whenMemberAlreadyExists() {
        MemberRegistrationRequestDto request = new MemberRegistrationRequestDto(
            "Test User", "test@example.com", "testUser", "password123", "01012345678", "19900101"
        );

        when(memberRepository.existsByLoginId(request.loginId())).thenReturn(true);

        assertThatThrownBy(() -> memberService.saveMember(request))
            .isInstanceOf(MemberAlreadyExistsException.class)
            .hasMessage("이미 존재하는 회원입니다");

        verify(memberRepository).existsByLoginId(request.loginId());
    }

    @Test
    void getLoginDto_shouldReturnLoginResponseDto_whenValidLoginId() {
        String loginId = "testUser";
        MemberStatus withdrawalStatus = new MemberStatus(1L, "WITHDRAWAL");
        Member member = new Member(1L, null, null, null, loginId, "password123", null,
            LocalDateTime.now(), null, 0L, null);

        when(memberStatusService.getMemberStatusByTypeName("WITHDRAWAL")).thenReturn(
            withdrawalStatus);
        when(memberRepository.findByLoginIdAndStatusNot(loginId, withdrawalStatus)).thenReturn(
            Optional.of(member));

        LoginResponseDto result = memberService.getLoginDto(loginId);

        assertThat(result.loginId()).isEqualTo(loginId);
        assertThat(result.password()).isEqualTo("password123");

        verify(memberRepository).findByLoginIdAndStatusNot(loginId, withdrawalStatus);
    }

    @Test
    void getLoginDto_shouldThrowException_whenMemberNotFound() {
        String loginId = "testUser";
        MemberStatus withdrawalStatus = new MemberStatus(1L, "WITHDRAWAL");

        when(memberStatusService.getMemberStatusByTypeName("WITHDRAWAL")).thenReturn(
            withdrawalStatus);
        when(memberRepository.findByLoginIdAndStatusNot(loginId, withdrawalStatus)).thenReturn(
            Optional.empty());

        assertThatThrownBy(() -> memberService.getLoginDto(loginId))
            .isInstanceOf(MemberNotFoundException.class);

        verify(memberRepository).findByLoginIdAndStatusNot(loginId, withdrawalStatus);
    }

    @Test
    void findAllMembers_shouldReturnListOfMemberResponseDto_whenMembersExist() {
        Pageable pageable = Pageable.ofSize(10);
        MemberStatus activeStatus = new MemberStatus(1L, "ACTIVE");
        MemberGrade defaultGrade = new MemberGrade(1L, "일반", 0, 0, true);

        Member member = new Member(
            1L, null, activeStatus, defaultGrade, "testUser", "password123",
            "19900101", LocalDateTime.of(2023, 1, 1, 10, 0), null, 100L, null
        );

        when(memberRepository.findByStatus_TypeName(MemberRepository.ACTIVE_STATUS)).thenReturn(
            List.of(member));

        List<MemberResponseDto> result = memberService.findAllMembers(pageable);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().loginId()).isEqualTo("testUser");

        verify(memberRepository).findByStatus_TypeName(MemberRepository.ACTIVE_STATUS);
    }

    @Test
    void deleteById_shouldChangeMemberStatusToWithdrawal_whenMemberExists() {
        // Given
        Long memberId = 1L;

        MemberStatus withdrawalStatus = new MemberStatus(2L, "WITHDRAWAL");
        Member existingMember = new Member(
            memberId, null, new MemberStatus(1L, "ACTIVE"), null,
            "testUser", "password123", "19900101",
            LocalDateTime.of(2023, 1, 1, 10, 0), null, 0L, null
        );

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(existingMember));
        when(memberStatusService.getMemberStatusByTypeName(MemberRepository.WITHDRAWAL_STATUS))
            .thenReturn(withdrawalStatus);

        // When
        memberService.deleteById(memberId);

        // Then
        assertThat(existingMember.getStatus().getTypeName()).isEqualTo("WITHDRAWAL");

        verify(memberRepository).findById(memberId);
        verify(memberStatusService).getMemberStatusByTypeName(MemberRepository.WITHDRAWAL_STATUS);
    }

    @Test
    void deleteById_shouldThrowException_whenMemberDoesNotExist() {
        // Given
        Long memberId = 1L;

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> memberService.deleteById(memberId))
            .isInstanceOf(MemberNotFoundException.class);

        verify(memberRepository).findById(memberId);
        verifyNoInteractions(memberStatusService); // MemberStatusService should not be called
    }

    @Test
    void updateLatestLoginAt_shouldUpdateLatestLoginAt_whenMemberExists() {
        // Given
        String loginId = "testUser";

        when(memberRepository.existsByLoginIdAndStatus_TypeName(loginId,
            MemberRepository.ACTIVE_STATUS))
            .thenReturn(true);

        // When
        memberService.updateLatestLoginAt(loginId);

        // Then
        verify(memberRepository).existsByLoginIdAndStatus_TypeName(loginId,
            MemberRepository.ACTIVE_STATUS);
        verify(memberRepository).updateLatestLoginAt(eq(loginId), any(LocalDateTime.class));
    }

    @Test
    void updateLatestLoginAt_shouldThrowException_whenMemberDoesNotExist() {
        // Given
        String loginId = "testUser";

        when(memberRepository.existsByLoginIdAndStatus_TypeName(loginId,
            MemberRepository.ACTIVE_STATUS))
            .thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> memberService.updateLatestLoginAt(loginId))
            .isInstanceOf(MemberNotFoundException.class);

        verify(memberRepository).existsByLoginIdAndStatus_TypeName(loginId,
            MemberRepository.ACTIVE_STATUS);
        verifyNoMoreInteractions(memberRepository); // updateLatestLoginAt should not be called
    }

    @Test
    void getMemberDto_shouldReturnMemberDetailResponseDto_whenMemberExists() {
        // Given
        String loginId = "testUser";

        // Mock 데이터
        MemberStatus activeStatus = new MemberStatus(1L, "ACTIVE");
        MemberGrade defaultGrade = new MemberGrade(1L, "일반", 0, 0, true);

        Customer customer = new Customer(
            1L, "password123", "Test User", "01012345678", "test@example.com", true, null
        );

        AddressResponseDto addressResponseDto = new AddressResponseDto(1L, "123 Main St", "Apt 4B",
            "Home", true);

        Member member = new Member(
            1L, customer, activeStatus, defaultGrade, loginId, "password123", "19900101",
            LocalDateTime.of(2023, 1, 1, 10, 0), null, 100L, null
        );

        // Mock 설정
        when(memberRepository.existsByLoginIdAndStatus_TypeName(loginId,
            MemberRepository.ACTIVE_STATUS))
            .thenReturn(true);
        when(memberRepository.findByLoginIdAndStatus_TypeName(loginId,
            MemberRepository.ACTIVE_STATUS))
            .thenReturn(member);
        when(customerService.getCustomer(1L)).thenReturn(CustomerResponseDto.of(customer));
        when(addressService.getAddresses(1L)).thenReturn(List.of(addressResponseDto));

        // When
        MemberDetailResponseDto result = memberService.getMemberDto(loginId);

        // Then
        assertThat(result.customerId()).isEqualTo(1L);
        assertThat(result.status().typeName()).isEqualTo("ACTIVE");
        assertThat(result.grade().gradeName()).isEqualTo("일반");
        assertThat(result.loginId()).isEqualTo("testUser");
        assertThat(result.name()).isEqualTo("Test User");
        assertThat(result.email()).isEqualTo("test@example.com");
        assertThat(result.phoneNumber()).isEqualTo("01012345678");
        assertThat(result.point()).isEqualTo(100L);
        assertThat(result.addresses()).hasSize(1);
        assertThat(result.addresses().getFirst().primaryAddress()).isEqualTo("123 Main St");

        verify(memberRepository).existsByLoginIdAndStatus_TypeName(loginId,
            MemberRepository.ACTIVE_STATUS);
        verify(memberRepository).findByLoginIdAndStatus_TypeName(loginId,
            MemberRepository.ACTIVE_STATUS);
        verify(customerService).getCustomer(1L);
        verify(addressService).getAddresses(1L);
    }

    @Test
    void getMemberDto_shouldThrowException_whenMemberDoesNotExist() {
        // Given
        String loginId = "testUser";

        when(memberRepository.existsByLoginIdAndStatus_TypeName(loginId,
            MemberRepository.ACTIVE_STATUS))
            .thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> memberService.getMemberDto(loginId))
            .isInstanceOf(MemberNotFoundException.class);

        verify(memberRepository).existsByLoginIdAndStatus_TypeName(loginId,
            MemberRepository.ACTIVE_STATUS);
        verifyNoMoreInteractions(memberRepository, customerService, addressService);
    }

    @Test
    void isExistsByLoginId_shouldReturnTrue_whenLoginIdExists() {
        // Given
        String loginId = "testUser";

        when(memberRepository.existsByLoginId(loginId)).thenReturn(true);

        // When
        IdCheckResponseDto result = memberService.isExistsByLoginId(loginId);

        // Then
        assertThat(result.isExists()).isTrue();
        verify(memberRepository).existsByLoginId(loginId);
    }

    @Test
    void isExistsByLoginId_shouldReturnFalse_whenLoginIdDoesNotExist() {
        // Given
        String loginId = "nonExistentUser";

        when(memberRepository.existsByLoginId(loginId)).thenReturn(false);

        // When
        IdCheckResponseDto result = memberService.isExistsByLoginId(loginId);

        // Then
        assertThat(result.isExists()).isFalse();
        verify(memberRepository).existsByLoginId(loginId);
    }

    @Test
    void getMemberLoginIdDtoByPaycoId_shouldReturnLoginId_whenPaycoIdIsLinked() {
        // Given
        String paycoId = "payco123";
        Member member = new Member(
            1L, null, null, null, "testUser", "password123", null,
            LocalDateTime.now(), null, 0L, paycoId
        );

        when(memberRepository.findByPaycoIdNo(paycoId)).thenReturn(Optional.of(member));

        // When
        MemberLoginIdResponseDto result = memberService.getMemberLoginIdDtoByPaycoId(paycoId);

        // Then
        assertThat(result.loginId()).isEqualTo("testUser");
        verify(memberRepository).findByPaycoIdNo(paycoId);
    }

    @Test
    void getMemberLoginIdDtoByPaycoId_shouldThrowException_whenPaycoIdIsNotLinked() {
        // Given
        String paycoId = "unlinkedPayco";

        when(memberRepository.findByPaycoIdNo(paycoId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> memberService.getMemberLoginIdDtoByPaycoId(paycoId))
            .isInstanceOf(PaycoNotLinkedException.class)
            .hasMessage("account not linked");

        verify(memberRepository).findByPaycoIdNo(paycoId);
    }

    @Test
    void changePassword_shouldUpdatePassword_whenOldPasswordMatches() {
        // Given
        Long customerId = 1L;
        String oldPassword = "oldPassword123";
        String newPassword = "newPassword123";
        Member member = new Member(
            customerId, null, null, null, "testUser", "encodedOldPassword", null,
            LocalDateTime.now(), null, 0L, null
        );

        when(memberRepository.findById(customerId)).thenReturn(Optional.of(member));
        when(passwordEncoder.matches(oldPassword, "encodedOldPassword")).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");

        // When
        memberService.changePassword(customerId, oldPassword, newPassword);

        // Then
        assertThat(member.getPassword()).isEqualTo("encodedNewPassword");
        verify(memberRepository).findById(customerId);
        verify(passwordEncoder).matches(oldPassword, "encodedOldPassword");
        verify(passwordEncoder).encode(newPassword);
    }

    @Test
    void changePassword_shouldThrowException_whenOldPasswordDoesNotMatch() {
        // Given
        Long customerId = 1L;
        String oldPassword = "wrongOldPassword";
        String newPassword = "newPassword123";
        Member member = new Member(
            customerId, null, null, null, "testUser", "encodedOldPassword", null,
            LocalDateTime.now(), null, 0L, null
        );

        when(memberRepository.findById(customerId)).thenReturn(Optional.of(member));
        when(passwordEncoder.matches(oldPassword, "encodedOldPassword")).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> memberService.changePassword(customerId, oldPassword, newPassword))
            .isInstanceOf(PasswordIncorrectException.class)
            .hasMessage("현재 비밀번호가 일치하지 않습니다.");

        verify(memberRepository).findById(customerId);
        verify(passwordEncoder).matches(oldPassword, "encodedOldPassword");
        verifyNoMoreInteractions(passwordEncoder); // encode should not be called
    }

    @Test
    void linkAccountByPaycoId_shouldLinkPaycoAccount_whenValidRequest() {
        // Given
        Long customerId = 1L;
        String paycoId = "payco123";
        Member member = new Member(
            customerId, null, null, null, "testUser", "password123", null,
            LocalDateTime.now(), null, 0L, null
        );

        when(memberRepository.existsByPaycoIdNo(paycoId)).thenReturn(false);
        when(memberRepository.findById(customerId)).thenReturn(Optional.of(member));

        // When
        memberService.linkAccountByPaycoId(customerId, paycoId);

        // Then
        assertThat(member.getPaycoIdNo()).isEqualTo(paycoId);

        verify(memberRepository).existsByPaycoIdNo(paycoId);
        verify(memberRepository).findById(customerId);
    }

    @Test
    void linkAccountByPaycoId_shouldThrowException_whenPaycoIdIsAlreadyLinked() {
        // Given
        Long customerId = 1L;
        String paycoId = "payco123";

        when(memberRepository.existsByPaycoIdNo(paycoId)).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> memberService.linkAccountByPaycoId(customerId, paycoId))
            .isInstanceOf(AlreadyLinkAccountException.class)
            .hasMessage("already linked account");

        verify(memberRepository).existsByPaycoIdNo(paycoId);
        verifyNoMoreInteractions(memberRepository);
    }

    @Test
    void linkAccountByPaycoId_shouldThrowException_whenMemberHasAlreadyLinkedPaycoId() {
        // Given
        Long customerId = 1L;
        String paycoId = "newPayco123";
        Member member = new Member(
            customerId, null, null, null, "testUser", "password123", null,
            LocalDateTime.now(), null, 0L, "existingPayco123"
        );

        when(memberRepository.existsByPaycoIdNo(paycoId)).thenReturn(false);
        when(memberRepository.findById(customerId)).thenReturn(Optional.of(member));

        // When & Then
        assertThatThrownBy(() -> memberService.linkAccountByPaycoId(customerId, paycoId))
            .isInstanceOf(AlreadyLinkAccountException.class)
            .hasMessage("already linked account");

        verify(memberRepository).existsByPaycoIdNo(paycoId);
        verify(memberRepository).findById(customerId);
    }

    @Test
    void getMemberStatusDtoByloginId_shouldReturnStatus_whenValidLoginId() {
        // Given
        String loginId = "testUser";
        MemberStatus activeStatus = new MemberStatus(1L, "ACTIVE");
        MemberStatus withdrawalStatus = new MemberStatus(2L, "WITHDRAWAL");
        Member member = new Member(
            1L, null, activeStatus, null, loginId, "password123", null,
            LocalDateTime.now(), null, 0L, null
        );

        when(memberStatusService.getMemberStatusByTypeName("WITHDRAWAL")).thenReturn(
            withdrawalStatus);
        when(memberRepository.findByLoginIdAndStatusNot(loginId, withdrawalStatus))
            .thenReturn(Optional.of(member));

        // When
        MemberStatusResponseDto result = memberService.getMemberStatusDtoByloginId(loginId);

        // Then
        assertThat(result.typeName()).isEqualTo("ACTIVE");

        verify(memberStatusService).getMemberStatusByTypeName("WITHDRAWAL");
        verify(memberRepository).findByLoginIdAndStatusNot(loginId, withdrawalStatus);
    }

    @Test
    void getMemberStatusDtoByloginId_shouldThrowException_whenMemberNotFound() {
        // Given
        String loginId = "nonExistentUser";
        MemberStatus withdrawalStatus = new MemberStatus(2L, "WITHDRAWAL");

        when(memberStatusService.getMemberStatusByTypeName("WITHDRAWAL")).thenReturn(
            withdrawalStatus);
        when(memberRepository.findByLoginIdAndStatusNot(loginId, withdrawalStatus))
            .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> memberService.getMemberStatusDtoByloginId(loginId))
            .isInstanceOf(MemberNotFoundException.class);

        verify(memberStatusService).getMemberStatusByTypeName("WITHDRAWAL");
        verify(memberRepository).findByLoginIdAndStatusNot(loginId, withdrawalStatus);
    }

    @Test
    void changeStatusToActive_shouldUpdateStatus_whenValidLoginId() {
        // Given
        String loginId = "testUser";
        MemberStatus activeStatus = new MemberStatus(1L, "ACTIVE");
        Member member = new Member(
            1L, null, new MemberStatus(2L, "INACTIVE"), null, loginId, "password123", null,
            LocalDateTime.now(), null, 0L, null
        );

        when(memberRepository.findByLoginId(loginId)).thenReturn(Optional.of(member));
        when(memberStatusService.getMemberStatusByTypeName("ACTIVE")).thenReturn(activeStatus);

        // When
        memberService.changeStatusToActive(loginId);

        // Then
        assertThat(member.getStatus().getTypeName()).isEqualTo("ACTIVE");

        verify(memberRepository).findByLoginId(loginId);
        verify(memberStatusService).getMemberStatusByTypeName("ACTIVE");
    }

    @Test
    void changeStatusToActive_shouldThrowException_whenMemberNotFound() {
        // Given
        String loginId = "nonExistentUser";

        when(memberRepository.findByLoginId(loginId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> memberService.changeStatusToActive(loginId))
            .isInstanceOf(MemberNotFoundException.class)
            .hasMessage("존재하지 않는 회원입니다.");

        verify(memberRepository).findByLoginId(loginId);
        verifyNoInteractions(memberStatusService);
    }
}
