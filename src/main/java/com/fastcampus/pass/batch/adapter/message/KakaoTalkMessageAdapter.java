package com.fastcampus.pass.batch.adapter.message;

import com.fastcampus.pass.batch.adapter.message.dto.KakaoTalkMessageRequest;
import com.fastcampus.pass.batch.adapter.message.dto.KakaoTalkMessageResponse;
import com.fastcampus.pass.batch.config.KakaoTalkMessageConfig;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class KakaoTalkMessageAdapter {

    private final WebClient webClient;

    public KakaoTalkMessageAdapter(KakaoTalkMessageConfig config) {
        webClient = WebClient.builder()
                .baseUrl(config.getHost())
                .defaultHeaders(header -> {
                    header.setBearerAuth(config.getToken());
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                })
                .build();
    }

    public boolean sendKakaoTalkMessage(final String uuid, String text) {
        KakaoTalkMessageResponse response = webClient.post().uri("/v1/api/talk/frieds/message/default/send")
                .body(BodyInserters.fromValue(new KakaoTalkMessageRequest(uuid, text)))
                .retrieve()
                .bodyToMono(KakaoTalkMessageResponse.class)
                .block();

        if (response == null || response.getSuccessfulReceiverUuids() == null) {
            return false;
        }

        return response.getSuccessfulReceiverUuids().size() > 0;

    }

}
