package shop.s5g.shop.controller.customer;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.shop.dto.customer.CustomerRegisterRequestDto;
import shop.s5g.shop.dto.customer.CustomerResponseDto;
import shop.s5g.shop.exception.BadRequestException;
import shop.s5g.shop.service.member.CustomerService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop/customer")
public class CustomerController {
    private final CustomerService customerService;

    /**
     * 비회원 주문 조회용 Customer Info GET 요청. 이메일, 휴대폰번호 둘 중 하나는 존재해야함.
     * @param type type=email, type=phoneNumber 비회원 검색 기준
     * @param email 이메일(필수아님)
     * @param phoneNumber 휴대폰번호(필수아님)
     * @param name 이름
     * @param password 패스워드(평문)
     * @return CustomerResponseDto with 200, 400 Bad
     * @throws shop.s5g.shop.exception.member.CustomerNotFoundException and status code 404
     */
    @GetMapping
    public CustomerResponseDto getCustomerInfo(
        @RequestParam String type,
        @RequestParam(required = false) String email,
        @RequestParam(required = false) String phoneNumber,
        @RequestParam String name,
        @RequestParam String password
    ) {
        return switch (type) {
            case "email" -> throw new UnsupportedOperationException("email is not implemented yet");
            case "phoneNumber" ->
                customerService.queryCustomer(phoneNumber, name, password);
            default -> throw new BadRequestException("타입이 잘못되었습니다.");
        };
    }

    /**
     * 주문서에서 사용할 비회원 생성 또는 서치 메소드
     * @param type 비회원 타입. email, phoneNumber 이 있다. 그러나 email은 미구현.
     * @param register 기입된 비회원 정보
     * @return 존재했거나 저장한 회원 정보 리턴(customerId 포함), 400 Bad
     * @throws BadRequestException 타입이나 형식이 맞지 않을 경우 400 Bad
     */
    @PostMapping
    public CustomerResponseDto createOrReturnCustomerForPurchase(
        @RequestParam String type,
        @RequestBody @Valid CustomerRegisterRequestDto register,
        BindingResult errors
    ) {
        if (errors.hasErrors()) {
            throw new BadRequestException("형식이 맞지 않습니다: "+errors.toString());
        }

        return switch (type) {
            case "phoneNumber" -> customerService.getOrRegisterCustomerPhoneNumber(
                register.phoneNumber(), register.name(), register.password()
            );
            // 미구현
            case "email" -> throw new UnsupportedOperationException("Email is not implemented yet");
            default -> throw new BadRequestException("타입이 잘못되었습니다.");
        };
    }
}
