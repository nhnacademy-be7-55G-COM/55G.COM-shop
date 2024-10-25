package shop.S5G.shop.service.member.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.S5G.shop.dto.customer.CustomerRegistrationRequestDto;
import shop.S5G.shop.dto.customer.CustomerResponseDto;
import shop.S5G.shop.dto.customer.CustomerUpdateRequestDto;
import shop.S5G.shop.entity.member.Customer;
import shop.S5G.shop.exception.member.CustomerNotFoundException;
import shop.S5G.shop.repository.member.CustomerRepository;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        testCustomer = Customer.builder()
            .customerId(1L)
            .email("test@test.com")
            .name("Test User")
            .phoneNumber("010-1234-5678")
            .password("securePassword")
            .active(true)
            .build();
    }

    @Test
    void addCustomerTest() {
        // Given
        CustomerRegistrationRequestDto requestDto = new CustomerRegistrationRequestDto(
            "securePassword", "Test User", "010-1234-5678", "test@test.com"
        );
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        // When
        Customer savedCustomer = customerService.addCustomer(requestDto);

        // Then
        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getCustomerId()).isEqualTo(testCustomer.getCustomerId());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void updateCustomerTest() {
        // Given
        Long customerId = testCustomer.getCustomerId();
        CustomerUpdateRequestDto updateRequestDto = new CustomerUpdateRequestDto("updated@test.com",
            "Updated User", "010-9876-5432");
        when(customerRepository.existsById(customerId)).thenReturn(true);

        // When
        customerService.updateCustomer(customerId, updateRequestDto);

        // Then
        verify(customerRepository, times(1)).updateCustomer(customerId, updateRequestDto);
    }

    @Test
    void updateCustomer_NotFoundTest() {
        // Given
        Long nonExistentId = 999L;
        CustomerUpdateRequestDto updateRequestDto = new CustomerUpdateRequestDto(
            "notfound@test.com", "Not Found User", "010-0000-0000");
        when(customerRepository.existsById(nonExistentId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> customerService.updateCustomer(nonExistentId, updateRequestDto))
            .isInstanceOf(CustomerNotFoundException.class)
            .hasMessage("변경할 고객이 존재하지 않습니다.");
        verify(customerRepository, never()).updateCustomer(anyLong(), any());
    }

    @Test
    void getCustomerTest() {
        // Given
        Long customerId = testCustomer.getCustomerId();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(testCustomer));

        // When
        CustomerResponseDto responseDto = customerService.getCustomer(customerId);

        // Then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.customerId()).isEqualTo(testCustomer.getCustomerId());
        assertThat(responseDto.email()).isEqualTo(testCustomer.getEmail());
        verify(customerRepository, times(1)).findById(customerId);
    }

    @Test
    void getCustomer_NotFoundTest() {
        // Given
        Long nonExistentId = 999L;
        when(customerRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> customerService.getCustomer(nonExistentId))
            .isInstanceOf(CustomerNotFoundException.class)
            .hasMessage("고객이 존재하지 않습니다.");
        verify(customerRepository, times(1)).findById(nonExistentId);
    }

    @Test
    void deleteCustomerTest() {
        // Given
        Long customerId = testCustomer.getCustomerId();
        when(customerRepository.existsById(customerId)).thenReturn(true);

        // When
        customerService.deleteCustomer(customerId);

        // Then
        verify(customerRepository, times(1)).inactiveCustomer(customerId);
    }

    @Test
    void deleteCustomer_NotFoundTest() {
        // Given
        Long nonExistentId = 999L;
        when(customerRepository.existsById(nonExistentId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> customerService.deleteCustomer(nonExistentId))
            .isInstanceOf(CustomerNotFoundException.class)
            .hasMessage("고객이 존재하지 않습니다.");
        verify(customerRepository, never()).inactiveCustomer(anyLong());
    }
}
