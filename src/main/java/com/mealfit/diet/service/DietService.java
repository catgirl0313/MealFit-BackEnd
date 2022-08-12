package com.mealfit.diet.service;

import com.mealfit.diet.domain.Diet;
import com.mealfit.diet.dto.DietCUDResponseDto;
import com.mealfit.diet.dto.DietRequestDto;
import com.mealfit.diet.dto.DietResponseDto;
import com.mealfit.diet.dto.PostRequestDto;
import com.mealfit.diet.repository.DietRepository;
import com.mealfit.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class DietService {

    private final DietRepository dietRepository;
    private final UserRepository userRepository;

    //식단 조회
    public DietResponseDto getDiet(LocalDate date) {
        Diet diet = dietRepository.findByDate(date)
                .orElseThrow(() -> new IllegalArgumentException("기록한 식단이 없습니다."));

        User user = userRepository.findById(diet.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("없는 회원정보입니다."));

        return DietResponseDto.builder()
                .foodList(diet.get)

    }


    // 식단 입력
    @Transactional
    public DietCUDResponseDto createDiet(DietRequestDto postRequestDto, User user) {

        //post 저장
        Diet dietEntity = postRequestDto.toEntity();
        dietEntity.settingUserInfo(user.getId());

        dietRepository.save(dietEntity);

        //return 값 생성
        return new DietCUDResponseDto(dietEntity, user);
    }

    // 식단 수정
    @Transactional
    public void updateDiet(PostRequestDto postDto, User user) {
        //item
        Long dietId = postDto.getDietId();
        Diet diet = dietRepository.findById(dietId)
                .orElseThrow(() -> new IllegalStateException("해당 게시글이 없습니다."));

        validateUser(user, diet);

        diet.update(postDto);
    }

    // 어차피 로그인 한 사람만의 기록이니까 이게 필요할까요...?
    private static void validateUser(User user, Diet diet) {
        Long postUserId = diet.getUserId();
        if (!user.getId().equals(postUserId)) {
            throw new IllegalArgumentException("작성자가 아니므로, 해당 게시글을 수정할 수 없습니다.");
        }
    }

    // 식단 삭제
    @Transactional
    public Long deleteDiet(Long dietId, User user) {
        //유효성 검사
        Diet diet = dietRepository.findById(dietId)
                .orElseThrow(() -> new IllegalArgumentException("기록한 식단이 없습니다."));

        //작성자 검사
        validateUser(user, diet);

        dietRepository.deleteById(dietId);

        return dietId;
    }
}
