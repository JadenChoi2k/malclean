package Choi.clean_lottery;

import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JSONParserTest {

    @Test
    public void json_parsing_test() throws Exception {
        // given
        // kakao api에 요청하면 주는 정보
        String jsonStr = "{\n" +
                "  \"id\": 123456789,\n" +
                "  \"connected_at\": \"2022-01-23T11:18:43Z\",\n" +
                "  \"properties\": {\n" +
                "    \"nickname\": \"choi\"\n" +
                "  },\n" +
                "  \"kakao_account\": {\n" +
                "    \"profile_needs_agreement\": false,\n" +
                "    \"profile\": {\n" +
                "      \"nickname\": \"choi\",\n" +
                "      \"thumbnail_image_url\": \"http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_110x110.jpg\",\n" +
                "      \"profile_image_url\": \"http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg\",\n" +
                "      \"is_default_image\": true\n" +
                "    },\n" +
                "    \"has_email\": true,\n" +
                "    \"email_needs_agreement\": false,\n" +
                "    \"is_email_valid\": true,\n" +
                "    \"is_email_verified\": true,\n" +
                "    \"email\": \"mail123@gmail.com\",\n" +
                "    \"has_age_range\": true,\n" +
                "    \"age_range_needs_agreement\": false,\n" +
                "    \"age_range\": \"20~29\",\n" +
                "    \"has_birthday\": true,\n" +
                "    \"birthday_needs_agreement\": false,\n" +
                "    \"birthday\": \"0410\",\n" +
                "    \"birthday_type\": \"SOLAR\",\n" +
                "    \"has_gender\": true,\n" +
                "    \"gender_needs_agreement\": false,\n" +
                "    \"gender\": \"male\"\n" +
                "  }\n" +
                "}";
        JSONObject jsonObject = new JSONObject(jsonStr);

        // when
        long id = jsonObject.getLong("id");
        JSONObject kakao_account = jsonObject.getJSONObject("kakao_account");
        // profile을 얻는 방법
        JSONObject profile = kakao_account.getJSONObject("profile");
        String nickname = profile.getString("nickname");
        Boolean is_default_image = profile.getBoolean("is_default_image");
        String thumbnail_image_url = profile.getString("thumbnail_image_url");
        String profile_image_url = profile.getString("profile_image_url");

        // then
        System.out.println("id = " + id);
        System.out.println("nickname = " + nickname);
        System.out.println("is_default_image = " + is_default_image);
        System.out.println("thumbnail_image_url = " + thumbnail_image_url);
        System.out.println("profile_image_url = " + profile_image_url);

        Assertions.assertThrows(JSONException.class, ()->profile.getString("has error"));
    }
}
