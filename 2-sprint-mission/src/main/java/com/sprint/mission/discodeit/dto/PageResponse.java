package com.sprint.mission.discodeit.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record PageResponse<T>(
    List<T> content,      // 실제 데이터 목록
    int number,           // 현재 페이지 번호
    int size,             // 페이지 크기
    boolean hasNext,      // 다음 페이지 존재 여부
    Long totalElements    // Page 총 갯수
) {

}