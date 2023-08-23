package com.example.OAuth_;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public enum OAuthAttributes {

    GOOGLE("google", (attribute) -> {
        UserDto userDto = new UserDto();
        userDto.setUserName((String)attribute.get("name"));
        userDto.setEmail((String)attribute.get("email"));

        return userDto;
    }),

    NAVER("naver", (attribute) -> {
        UserDto userDto = new UserDto();

        Map<String, String> responseValue = (Map)attribute.get("response");

        userDto.setUserName(responseValue.get("name"));
        userDto.setEmail(responseValue.get("email"));

        return userDto;
    }),

    KAKAO("kakao", (attribute) -> {

        Map<String, Object> account = (Map)attribute.get("kakao_account");
        Map<String, String> profile = (Map)account.get("profile");

        UserDto userDto = new UserDto();
        userDto.setUserName(profile.get("nickname"));
        userDto.setEmail((String)account.get("email"));

        return userDto;
    });

    private final String registrationId; // 로그인한 서비스(ex) google, naver..)
    private final Function<Map<String, Object>, UserDto> of; // 로그인한 사용자의 정보를 통하여 UserProfile을 가져옴

    OAuthAttributes(String registrationId, Function<Map<String, Object>, UserDto> of) {
        this.registrationId = registrationId;
        this.of = of;
    }

    public static UserDto extract(String registrationId, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(value -> registrationId.equals(value.registrationId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .of.apply(attributes);
    }
}
