package Choi.clean_lottery.web.kakaoapi;

import Choi.clean_lottery.web.WebConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Choi.clean_lottery.web.kakaoapi.KakaoApiHelper.HttpMethodType.GET;

@Component
@Slf4j
public class KakaoApiHelper {
    public enum HttpMethodType { POST, GET, DELETE }

    private static final String API_SERVER_HOST  = "https://kapi.kakao.com";
    private static final String API_AUTH_HOST = "https://kauth.kakao.com";

    private static final String OAUTH_TOKEN_PATH = "/oauth/token";

    private static final String USER_SIGNUP_PATH = "/v2/user/signup";
    private static final String USER_UNLINK_PATH = "/v2/user/unlink";
    private static final String USER_LOGOUT_PATH = "/v2/user/logout";
    private static final String USER_ME_PATH = "/v2/user/me";
    private static final String USER_UPDATE_PROFILE_PATH = "/v2/user/update_profile";
    private static final String USER_IDS_PATH = "/v1/user/ids";
    private static final String USER_TOKEN_INFO_PATH = "/v1/user/access_token_info";

    private static final String STORY_PROFILE_PATH = "/v2/api/story/profile";
    private static final String STORY_ISSTORYUSER_PATH = "/v2/api/story/isstoryuser";
    private static final String STORY_MYSTORIES_PATH = "/v2/api/story/mystories";
    private static final String STORY_MYSTORY_PATH = "/v2/api/story/mystory";
    private static final String STORY_DELETE_MYSTORY_PATH = "/v2/api/story/delete/mystory";
    private static final String STORY_POST_NOTE_PATH = "/v2/api/story/post/note";
    private static final String STORY_UPLOAD_MULTI_PATH = "/v2/api/story/upload/multi";
    private static final String STORY_POST_PHOTO_PATH = "/v2/api/story/post/photo";
    private static final String STORY_LINKINFO_PATH = "/v2/api/story/linkinfo";
    private static final String STORY_POST_LINK_PATH = "/v2/api/story/post/link";

    private static final String TALK_PROFILE_PATH = "/v2/api/talk/profile";

    private static final String PUSH_REGISTER_PATH = "/v2/push/register";
    private static final String PUSH_TOKENS_PATH = "/v2/push/tokens";
    private static final String PUSH_DEREGISTER_PATH = "/v2/push/deregister";
    private static final String PUSH_SEND_PATH = "/v2/push/send";

    private static final ObjectMapper JACKSON_OBJECT_MAPPER = new ObjectMapper();
    //private static final Gson GSON = new Gson();
    private static final String PROPERTIES_PARAM_NAME = "properties";

    private static final List<String> adminApiPaths = new ArrayList<String>();

    static {
        adminApiPaths.add(USER_IDS_PATH);
        adminApiPaths.add(PUSH_REGISTER_PATH);
        adminApiPaths.add(PUSH_TOKENS_PATH);
        adminApiPaths.add(PUSH_DEREGISTER_PATH);
        adminApiPaths.add(PUSH_SEND_PATH);
        adminApiPaths.add(USER_ME_PATH);
    }

    private String accessToken;
    private final String adminKey = KakaoAppConst.ADMIN_KEY;

    public KakaoTokenInfo getTokenByCode(String code, String redirectURI) {
        Map<String, String> params = new HashMap<>();
        params.put("grant_type", "authorization_code");
        params.put("client_id", KakaoAppConst.REST_API_KEY);
        params.put("redirect_uri", redirectURI);
        params.put("code", code);
        log.info("카카오로 보내는 메시지 -> {}", params);
        String tokenInfoJson = request(HttpMethodType.POST, OAUTH_TOKEN_PATH, mapToParams(params));
        KakaoTokenInfo kakaoTokenInfo = KakaoTokenInfo.fromJson(tokenInfoJson);
        if (kakaoTokenInfo.hasError()) {
            params.put("redirect_uri", redirectURI.replace("http", "https"));
            tokenInfoJson = request(HttpMethodType.POST, OAUTH_TOKEN_PATH, mapToParams(params));
            kakaoTokenInfo = KakaoTokenInfo.fromJson(tokenInfoJson);
        }
        return kakaoTokenInfo;
    }

    // return : {access_token, refresh_token}
    public KakaoTokenInfo getTokenByCode(String code) {
        Map<String, String> params = new HashMap<>();
        params.put("grant_type", "authorization_code");
        params.put("client_id", KakaoAppConst.REST_API_KEY);
        params.put("redirect_uri", WebConfig.APP_DOMAIN + "/member/join-pass");
        params.put("code", code);
        String tokenInfoJson = request(HttpMethodType.POST, OAUTH_TOKEN_PATH, mapToParams(params));
        return KakaoTokenInfo.fromJson(tokenInfoJson);
    }

    public boolean isValidToken(String token) throws JsonProcessingException {
        this.accessToken = token;
        String tokenInfoJson = request(USER_TOKEN_INFO_PATH);
        KakaoUserTokenInfo userTokenInfo = JACKSON_OBJECT_MAPPER.readValue(tokenInfoJson, KakaoUserTokenInfo.class);
        return userTokenInfo.isValid();
    }

    /**
     * @return (Long) userId => null or userId
     */
    public Long getUserIdByToken(String token) throws JsonProcessingException {
        this.accessToken = token;
        String tokenInfoJson = request(USER_TOKEN_INFO_PATH);
        KakaoUserTokenInfo userTokenInfo = JACKSON_OBJECT_MAPPER.readValue(tokenInfoJson, KakaoUserTokenInfo.class);
        return userTokenInfo.getId();
    }

    public KakaoUserInfo getUserInfoByUserId(Long userId) throws JsonProcessingException {
        Map<String, String> params = new HashMap<>();
        params.put("target_id_type", "user_id");
        params.put("target_id", userId.toString());
        String userInfoJson = request(HttpMethodType.GET, USER_ME_PATH, "?" + mapToParams(params));
        return KakaoUserInfo.fromJson(userInfoJson);
    }

    public String request(final String apiPath) {
        return request(GET, apiPath, null);
    }

    // request 메서드
    public String request(HttpMethodType httpMethod, final String apiPath, final String params) {

        String requestUrl;
        if (apiPath == OAUTH_TOKEN_PATH) {
            requestUrl = API_AUTH_HOST + apiPath;
        } else {
            requestUrl = API_SERVER_HOST + apiPath;
        }

        if (httpMethod == null) {
            httpMethod = GET;
        }
        if (params != null && params.length() > 0
                && (httpMethod == GET || httpMethod == HttpMethodType.DELETE)) {
            requestUrl += params;
        }

        HttpsURLConnection conn;
        OutputStreamWriter writer = null;
        BufferedReader reader = null;
        InputStreamReader isr = null;

        try {
            final URL url = new URL(requestUrl);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod(httpMethod.toString());

            if (adminApiPaths.contains(apiPath)) {
                conn.setRequestProperty("Authorization", "KakaoAK " + this.adminKey);
            } else {
                conn.setRequestProperty("Authorization", "Bearer " + this.accessToken);
            }

            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");

            if (params != null && params.length() > 0 && httpMethod == HttpMethodType.POST) {
                conn.setDoOutput(true);
                writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(params);
                writer.flush();
            }

            final int responseCode = conn.getResponseCode();
            log.debug("Sending {} request to URL : {}", httpMethod.toString(), requestUrl);
            log.debug("Reponse Code : {}", responseCode);
            if (responseCode == 200)
                isr = new InputStreamReader(conn.getInputStream());
            else
                isr = new InputStreamReader(conn.getErrorStream());

            reader = new BufferedReader(isr);
            final StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            log.debug(buffer.toString());
            return buffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) try { writer.close(); } catch (Exception ignore) { }
            if (reader != null) try { reader.close(); } catch (Exception ignore) { }
            if (isr != null) try { isr.close(); } catch (Exception ignore) { }
        }

        return null;
    }

    public String urlEncodeUTF8(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public String mapToParams(Map<String, String > map) {
        StringBuilder paramBuilder = new StringBuilder();
        for (String key : map.keySet()) {
            paramBuilder.append(paramBuilder.length() > 0 ? "&" : "");
            paramBuilder.append(String.format("%s=%s", urlEncodeUTF8(key),
                    urlEncodeUTF8(map.get(key).toString())));
        }
        return paramBuilder.toString();
    }

    public String mapToJsonStr(Map<String, String > map) throws JsonProcessingException {
        return JACKSON_OBJECT_MAPPER.writeValueAsString(map);
        // return GSON.toJson(map);
    }
}
