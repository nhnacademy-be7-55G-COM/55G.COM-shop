package shop.s5g.shop.service.admin;

import shop.s5g.shop.dto.admin.AdminRegistrationRequestDto;
import shop.s5g.shop.dto.member.LoginResponseDto;

public interface AdminService {

    LoginResponseDto getLoginDto(String loginId);

    void registerAdmin(AdminRegistrationRequestDto adminRegistrationRequestDto);
}
