package com.musical.memories.musicalmemories.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musical.memories.musicalmemories.api.auth.Authentication;
import com.musical.memories.musicalmemories.datatransferobjects.GeniusResponse;
import com.musical.memories.musicalmemories.datatransferobjects.Hit;
import com.musical.memories.musicalmemories.datatransferobjects.Response;
import com.musical.memories.musicalmemories.datatransferobjects.Token;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Spotify {
    private RestTemplate restTemplate = new RestTemplate();
    private ObjectMapper objectMapper = new ObjectMapper();
    private Logger logger = LoggerFactory.getLogger(Spotify.class);
    private String clientId;
    private String clientSecret;
    private static final String redirectURI = "http://localhost:8080/";

    public Spotify(){
       this.clientId = new Authentication().getAPIKey("client-id");
       this.clientSecret = new Authentication().getAPIKey("clientSecret");
    }


    public HttpEntity createHttpEntity(String queryString)
    {
        String auth = clientId + ":" + clientSecret;
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(Charset.forName("US-ASCII")) );
        String authHeader = "Basic " + new String( encodedAuth );
        HttpHeaders httpHeaders = new  HttpHeaders();
        httpHeaders.set("Authorization", authHeader);
        httpHeaders.set("Content-type","application/x-www-form-urlencoded");
        httpHeaders.set("Accept", "");
        httpHeaders.set("grant_type", "authorization_code");
        Map<String, String> queryMap =  getQueryMap(queryString);
        String code = queryMap.get("code");
        httpHeaders.set("code", code);
        HttpEntity<String> httpEntity = new HttpEntity<>("body",httpHeaders);
        return httpEntity;
    }

    public String authorize()
    {
        String response = null;
        try {
            URI uri = new URI(String.format("https://accounts.spotify.com/authorize?client_id=%s&response_type=code" +
                            "&redirect_uri=%s&scope=user-read-private%20user-read-email&state=34fFs29kd09",
                    URLEncoder.encode(clientId, "UTF-8"),URLEncoder.encode(clientId, "UTF-8"),redirectURI));
            ResponseEntity<String> responseVal = restTemplate.getForEntity(uri, String.class);
            response = responseVal.toString();
        }
        catch(Exception ex)
        {
            logger.error(ex.toString());
        }
    }

    //request refresh and access tokens;

    public Token requestToken(){
        Token token = null;
        try {

            URI uri = new URI("https://accounts.spotify.com/api/token");
            String queryString = authorize();
            HttpEntity httpEntity = createHttpEntity(queryString);
            ResponseEntity<Token> responseVal = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, Token.class);
            token = responseVal.getBody();

        }
        catch(URISyntaxException ex)
        {
            logger.error(ex.toString());
        }
        return token;

    }


    public static Map<String, String> getQueryMap(String query)
    {
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params)
        {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }
}
