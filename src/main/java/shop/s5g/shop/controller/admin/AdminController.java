package shop.s5g.shop.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.shop.dto.admin.AdminRegistrationRequestDto;
import shop.s5g.shop.dto.member.LoginResponseDto;
import shop.s5g.shop.dto.tag.MessageDto;
import shop.s5g.shop.service.admin.AdminService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/admin/login/{loginId}")
    public ResponseEntity<LoginResponseDto> getMemberLoginInfo(@PathVariable String loginId) {

        LoginResponseDto loginResponseDto = adminService.getLoginDto(loginId);

        return ResponseEntity.ok().body(loginResponseDto);
    }

    @PostMapping("/admin/register")
    public ResponseEntity<MessageDto> registerAdmin(
        @RequestBody AdminRegistrationRequestDto adminRegistrationRequestDto) {
        adminService.registerAdmin(adminRegistrationRequestDto);
        return ResponseEntity.ok().body(new MessageDto("Admin registered successfully"));
    }
}
