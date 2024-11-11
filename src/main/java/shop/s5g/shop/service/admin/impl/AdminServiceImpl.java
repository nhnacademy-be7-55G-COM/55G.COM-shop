package shop.s5g.shop.service.admin.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.admin.AdminRegistrationRequestDto;
import shop.s5g.shop.dto.member.LoginResponseDto;
import shop.s5g.shop.entity.admin.Admin;
import shop.s5g.shop.exception.admin.AdminAlreadyExistsException;
import shop.s5g.shop.exception.admin.AdminNotFoundException;
import shop.s5g.shop.repository.admin.AdminRepository;
import shop.s5g.shop.service.admin.AdminService;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    @Override
    public LoginResponseDto getLoginDto(String loginId) {
        if (!adminRepository.existsById(loginId)) {
            throw new AdminNotFoundException(loginId + " not found");
        }
        Admin admin = adminRepository.findById(loginId);
        return new LoginResponseDto(admin.getId(), admin.getPassword());
    }

    @Override
    public void registerAdmin(AdminRegistrationRequestDto adminRegistrationRequestDto) {
        if (adminRepository.existsById(adminRegistrationRequestDto.id())) {
            throw new AdminAlreadyExistsException(
                adminRegistrationRequestDto.id() + " already exists");
        }

        adminRepository.save(new Admin(adminRegistrationRequestDto.id(),
            passwordEncoder.encode(adminRegistrationRequestDto.password())));
    }
}
