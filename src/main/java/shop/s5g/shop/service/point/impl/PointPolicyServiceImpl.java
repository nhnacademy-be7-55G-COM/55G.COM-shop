package shop.s5g.shop.service.point.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.point.PointPolicyCreateRequestDto;
import shop.s5g.shop.dto.point.PointPolicyRemoveRequestDto;
import shop.s5g.shop.dto.point.PointPolicyResponseDto;
import shop.s5g.shop.dto.point.PointPolicyUpdateRequestDto;
import shop.s5g.shop.dto.point.PointPolicyView;
import shop.s5g.shop.entity.point.PointPolicy;
import shop.s5g.shop.entity.point.PointSource;
import shop.s5g.shop.exception.BadRequestException;
import shop.s5g.shop.exception.EssentialDataNotFoundException;
import shop.s5g.shop.repository.point.PointPolicyRepository;
import shop.s5g.shop.repository.point.PointSourceRepository;
import shop.s5g.shop.service.point.PointPolicyService;

@Service
@RequiredArgsConstructor
@Transactional
public class PointPolicyServiceImpl implements PointPolicyService {
    private final PointPolicyRepository pointPolicyRepository;
    private final PointSourceRepository pointSourceRepository;
    @Override
    @Transactional(readOnly = true)
    public List<PointPolicyResponseDto> getAllPolicies() {
        return pointPolicyRepository.findAllPolicies();
    }

    @Override
    @Transactional(readOnly = true)
    public PointPolicyView getPolicy(String name) {
        return pointPolicyRepository.findByName(name).orElseThrow(
            () -> new EssentialDataNotFoundException("Point policy does not exist: "+name)
        );
    }

    @Override
    public void updatePolicyValue(PointPolicyUpdateRequestDto pointPolicyUpdateRequestDto) {

        PointPolicy pointPolicy = pointPolicyRepository.findById(pointPolicyUpdateRequestDto.id())
            .orElseThrow(BadRequestException::new);

        pointPolicy.setValue(pointPolicyUpdateRequestDto.value());

        pointPolicyRepository.save(pointPolicy);
    }

    @Override
    public void createPointPolicy(PointPolicyCreateRequestDto pointPolicyCreateRequestDto) {

        pointSourceRepository.findBySourceName(pointPolicyCreateRequestDto.policyName()).ifPresent(pointSource -> {
            throw new BadRequestException();
        });

        PointSource savedPolicySource = pointSourceRepository.save(
            new PointSource(pointPolicyCreateRequestDto.policyName()));
        pointPolicyRepository.save(
            new PointPolicy(savedPolicySource, pointPolicyCreateRequestDto.policyName(),
                pointPolicyCreateRequestDto.discountValue()));

    }

    @Override
    public void removePointPolicy(PointPolicyRemoveRequestDto pointPolicyRemoveRequestDto) {

        pointPolicyRepository.deleteById(pointPolicyRemoveRequestDto.id());

        pointSourceRepository.deleteBySourceName(pointPolicyRemoveRequestDto.sourceName());

    }
}
