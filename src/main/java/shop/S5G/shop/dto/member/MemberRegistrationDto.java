package shop.S5G.shop.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class MemberRegistrationDto {

    String name;
    String email;
    String loginId;
    String password;
    String phoneNumber;
    String birthDate;

}
