package com.mealfit.diet.service;

import com.mealfit.diet.domain.Diet;
import com.mealfit.diet.dto.DietCUDResponseDto;
import com.mealfit.diet.dto.DietListResponseDto;
import com.mealfit.diet.dto.DietRequestDto;
import com.mealfit.diet.dto.DietResponseDto;
import com.mealfit.diet.dto.PostRequestDto;
import com.mealfit.diet.repository.DietRepository;
import com.mealfit.food.domain.Food;
import com.mealfit.food.repository.FoodRepository;
import com.mealfit.user.domain.User;
import com.mealfit.user.application.dto.response.UserNutritionGoalResponseDto;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class DietService {

    private final DietRepository dietRepository;
    private final FoodRepository foodRepository;

    //식단 조회
    @Transactional
    public DietListResponseDto getDiet(LocalDate date, User user) {
        List<Diet> dietList = dietRepository.findByDietDateAndUserId(date, user.getId()); // 얘도 여러개니까 리스트로
        List<DietResponseDto> dietResponseDtoList = new ArrayList<>();

        for (Diet diet : dietList) {
            Food food = foodRepository.findById(diet.getFoodId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 음식입니다."));
            DietResponseDto dietResponseDto = new DietResponseDto(diet.getStatus(),food,diet.getFoodWeight());
            dietResponseDtoList.add(dietResponseDto);
        }

        UserNutritionGoalResponseDto userGoalDto = new UserNutritionGoalResponseDto(user);

        return new DietListResponseDto(dietResponseDtoList, userGoalDto);
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

    private static void validateUser(User user, Diet diet) {
        Long postUserId = diet.getUserId();
        if (!user.getId().equals(postUserId)) {
            throw new IllegalArgumentException("작성자가 아니므로, 해당 게시글을 수정할 수 없습니다.");
        }
    }

    // 식단 삭제
    @Transactional
    public void deleteDiet(Long dietId, User user) {
        //유효성 검사
        Diet diet = dietRepository.findById(dietId)
                .orElseThrow(() -> new IllegalArgumentException("기록한 식단이 없습니다."));

        //작성자 검사
        validateUser(user, diet);

        dietRepository.delete(diet);
    }
}
