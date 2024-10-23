package shop.S5G.shop.dto.member;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberRegistrationDto {

    String name;
    String email;
    String loginId;
    String password;
    String phoneNumber;
    String birthDate;

}
