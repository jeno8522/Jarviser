package com.ssafy.jarviser.dto;

import lombok.*;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class KeywordStatisticsDTO {
    String keyword;
    double percent;
}
