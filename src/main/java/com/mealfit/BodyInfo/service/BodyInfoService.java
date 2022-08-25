package com.mealfit.BodyInfo.service;

import com.mealfit.BodyInfo.domain.BodyInfo;
import com.mealfit.BodyInfo.dto.request.BodyInfoChangeRequestDto;
import com.mealfit.BodyInfo.dto.request.BodyInfoSaveRequestDto;
import com.mealfit.BodyInfo.dto.response.BodyInfoResponseDto;
import com.mealfit.BodyInfo.repository.BodyInfoRepository;
import com.mealfit.user.domain.User;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BodyInfoService {

    private final BodyInfoRepository bodyInfoRepository;

    public BodyInfoService(BodyInfoRepository bodyInfoRepository) {
        this.bodyInfoRepository = bodyInfoRepository;
    }

    @Transactional
    public void saveBodyInfo(User user, BodyInfoSaveRequestDto dto) {
        BodyInfo bodyInfo = BodyInfo.createBodyInfo(user.getId(), dto.getWeight(),
              dto.getBodyFat(), dto.getSavedDate());
        bodyInfoRepository.save(bodyInfo);
    }

    @Transactional
    public void changeBodyInfo(User user, BodyInfoChangeRequestDto dto) {
        BodyInfo bodyInfo = bodyInfoRepository.findByIdAndUserId(dto.getId(), user.getId())
              .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 체중기록입니다."));

        bodyInfo.changeWeight(dto.getWeight());
    }

    public List<BodyInfoResponseDto> showBodyInfos(User user) {
        return bodyInfoRepository.findByUserIdOrderBySavedDateDesc(user.getId())
              .stream()
              .map(BodyInfoResponseDto::new)
              .collect(Collectors.toList());
    }

    public BodyInfoResponseDto showBodyInfo(User user, Long bodyInfoId) {
        BodyInfo bodyInfo = bodyInfoRepository.findByIdAndUserId(bodyInfoId, user.getId())
              .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 체중기록입니다."));

        return new BodyInfoResponseDto(bodyInfo);
    }
}
