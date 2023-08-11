package com.ssafy.jarviser.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseAudioMessage {
    private String name;
    private String content;
    private int length;
}
