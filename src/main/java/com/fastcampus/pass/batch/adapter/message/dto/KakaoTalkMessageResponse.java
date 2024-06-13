package com.fastcampus.pass.batch.adapter.message.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class KakaoTalkMessageResponse {

    @JsonProperty("successful_receiver_uuids")
    private List<String> successfulReceiverUuids;

}
