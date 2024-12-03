package shop.s5g.shop.service.point.impl;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.point.PointPolicyFormResponseDto;
import shop.s5g.shop.dto.point.PointSourceCreateRequestDto;
import shop.s5g.shop.entity.point.PointSource;
import shop.s5g.shop.exception.AlreadyExistsException;
import shop.s5g.shop.repository.point.PointSourceRepository;
import shop.s5g.shop.service.point.PointSourceService;

@Service
@RequiredArgsConstructor
@Transactional
public class PointSourceServiceImpl implements PointSourceService {

    private final PointSourceRepository pointSourceRepository;

    public void createPointSource(PointSourceCreateRequestDto pointSourceCreateRequestDto) {

        pointSourceRepository.findBySourceName(pointSourceCreateRequestDto.pointSourceName()).ifPresent(pointSource -> {
            throw new AlreadyExistsException("이미 존재하는 포인트 정책 분류입니다.");
        });


        pointSourceRepository.save(new PointSource(pointSourceCreateRequestDto.pointSourceName()));
    }

    public List<PointPolicyFormResponseDto> getPointPolicyFormData() {
        List<PointSource> allPonitSourceList = pointSourceRepository.findAll();

        List<PointPolicyFormResponseDto> responseList = new ArrayList<>();

        allPonitSourceList.stream().forEach(pointSource -> responseList.add(
            new PointPolicyFormResponseDto(pointSource.getId(), pointSource.getSourceName())));

        return responseList;
    }
}
