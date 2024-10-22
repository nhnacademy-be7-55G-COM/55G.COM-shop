package shop.S5G.shop.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import shop.S5G.shop.entity.member.Member;
import shop.S5G.shop.entity.member.MemberGrade;
import shop.S5G.shop.entity.member.MemberStatus;

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

    public static Member toEntity(MemberRegistrationDto dto) {
        return new Member(

        );
    }
}
