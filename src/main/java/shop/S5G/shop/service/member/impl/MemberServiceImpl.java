package shop.S5G.shop.service.member.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import shop.S5G.shop.dto.member.MemberRegistrationDto;
import shop.S5G.shop.dto.member.MemberResponseDto;
import shop.S5G.shop.dto.member.MemberUpdateDto;
import shop.S5G.shop.repository.member.MemberRepository;
import shop.S5G.shop.service.member.MemberService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void saveMember(MemberRegistrationDto memberRegistrationDto) {

    }

    @Override
    @Transactional
    public void updateMember(MemberUpdateDto memberUpdateDto) {

    }

    @Override
    public MemberResponseDto findMember(String loginId) {
        return null;
    }

    @Override
    public List<MemberResponseDto> findAllMembers(Pageable pageable) {
        return List.of();
    }

    @Override
    public boolean existsMember(String loginId) {
        return memberRepository.existsByLoginId(loginId);
    }

    @Override
    public void deleteById(Long memberId) {
        //TODO delete 추후 생각해보기
        throw new UnsupportedOperationException();
    }
}
