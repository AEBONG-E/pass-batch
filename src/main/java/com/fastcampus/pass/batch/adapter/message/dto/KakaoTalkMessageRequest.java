package com.fastcampus.pass.batch.adapter.message.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class KakaoTalkMessageRequest {

    @JsonProperty("template_object")
    private TemplateObject templateObject;

    @JsonProperty("receiver_uuids")
    private List<String> receiverUuids;

    @Builder
    @Data
    public static class TemplateObject {

        @JsonProperty("object_type")
        private String objectType;
        private String text;
        private Link link;

        @Data
        public static class Link {

            @JsonProperty("web_url")
            private String webUrl;

        }

    }

    public KakaoTalkMessageRequest(String uuid, String text) {
        List<String> receiverUuids = Collections.singletonList(uuid);

        TemplateObject.Link link = new TemplateObject.Link();

        this.templateObject = TemplateObject.builder()
                .objectType("text")
                .text(text)
                .link(link)
                .build();
        this.receiverUuids = receiverUuids;
    }
}
