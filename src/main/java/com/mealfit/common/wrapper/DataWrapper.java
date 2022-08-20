package com.mealfit.common.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 리스트로 반환되는 경우 리엑트 측에서 사용하기 쉽게 + 후에 가공하기 쉽게 Wrapping 해주는 클래스
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataWrapper<T> {

    T data;
}
