package com.example.dcproject.service;

import com.google.gson.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class FcOnlineService {
    private final String API_KEY = System.getenv("FCURL");
    private final String rank = "https://open.api.nexon.com/static/fconline/meta/division.json";

    public String ouid(String word) {
        try {
            String characterName1 = URLEncoder.encode(word, StandardCharsets.UTF_8);
            String urlString1 = "https://open.api.nexon.com/fconline/v1/id?nickname="+characterName1;
            URL url1 = new URL(urlString1);

            HttpURLConnection connection1 = (HttpURLConnection)url1.openConnection();
            connection1.setRequestMethod("GET");
            connection1.setRequestProperty("x-nxopen-api-key", API_KEY);

            int responseCode1 = connection1.getResponseCode();


            BufferedReader in1;
            if(responseCode1 == 200) {
                in1 = new BufferedReader(new InputStreamReader(connection1.getInputStream()));
            } else {
                in1 = new BufferedReader(new InputStreamReader(connection1.getErrorStream()));
            }

            String inputLine1;
            StringBuffer response1 = new StringBuffer();
            while ((inputLine1 = in1.readLine()) != null) {
                response1.append(inputLine1);
            }

            in1.close();
            System.out.println(response1.toString());
            String ouid = response1.toString();
            return ouid;
        } catch(Exception e) {
            System.out.println(e);
            String error = e.toString();
            return error;
        }
    }

    public String baseUser(Model model, String word) {
        try {
            String dataFromFirstApi = parseFirstApiResponse(ouid(word));
            model.addAttribute("ouid", dataFromFirstApi);

            String characterName2 = URLEncoder.encode(dataFromFirstApi, StandardCharsets.UTF_8);

            String urlString2 = "https://open.api.nexon.com/fconline/v1/user/basic?ouid=" + characterName2;
            URL url2 = new URL(urlString2);

            HttpURLConnection connection2 = (HttpURLConnection)url2.openConnection();
            connection2.setRequestMethod("GET");
            connection2.setRequestProperty("x-nxopen-api-key", API_KEY);

            int responseCode2 = connection2.getResponseCode();


            BufferedReader in2;
            if(responseCode2 == 200) {
                in2 = new BufferedReader(new InputStreamReader(connection2.getInputStream()));
            } else {
                in2 = new BufferedReader(new InputStreamReader(connection2.getErrorStream()));
            }

            String inputLine2;
            StringBuffer response2 = new StringBuffer();
            while ((inputLine2 = in2.readLine()) != null) {
                response2.append(inputLine2);
            }
            in2.close();

            Gson gson1 = new Gson();
            JsonObject jsonObject1 = gson1.fromJson(response2.toString(), JsonObject.class);
            int characterLevel = jsonObject1.get("level").getAsInt();
            String serverName = jsonObject1.get("nickname").getAsString();

            model.addAttribute("characterLevel", characterLevel);
            model.addAttribute("serverName", serverName);
            System.out.println(response2.toString());

            String nick = response2.toString();
            return nick;
        } catch(Exception e) {
            System.out.println(e);
            String error = e.toString();
            return error;
        }
    }

    public void user(Model model, String word) {
        try {
            // 첫 번째 API 응답을 파싱하여 필요한 데이터 추출
            String dataFromFirstApi = parseFirstApiResponse(baseUser(model, word));
            model.addAttribute("ouid", dataFromFirstApi);

            String characterName3 = URLEncoder.encode(dataFromFirstApi, StandardCharsets.UTF_8);

            String urlString3 = "https://open.api.nexon.com/fconline/v1/user/maxdivision?ouid=" + characterName3;
            URL url3 = new URL(urlString3);

            HttpURLConnection connection3 = (HttpURLConnection)url3.openConnection();
            connection3.setRequestMethod("GET");
            connection3.setRequestProperty("x-nxopen-api-key", API_KEY);

            int responseCode3 = connection3.getResponseCode();

            BufferedReader in3;
            if(responseCode3 == 200) {
                in3 = new BufferedReader(new InputStreamReader(connection3.getInputStream()));
            } else {
                in3 = new BufferedReader(new InputStreamReader(connection3.getErrorStream()));
            }

            String inputLine3;
            StringBuffer response3 = new StringBuffer();
            while ((inputLine3 = in3.readLine()) != null) {
                response3.append(inputLine3);
            }
            in3.close();

            JsonArray jsonArray2 = JsonParser.parseString(response3.toString()).getAsJsonArray();

            for (JsonElement jsonElement : jsonArray2) {
                JsonObject jsonObject2 = jsonElement.getAsJsonObject();
                int matchType = jsonObject2.get("matchType").getAsInt();
                int maxDivision = jsonObject2.get("division").getAsInt();
                String date = jsonObject2.get("achievementDate").getAsString();

                if (matchType == 50) {
                    model.addAttribute("matchType", matchType);
                    model.addAttribute("maxDivision", maxDivision);
                    model.addAttribute("date", date);
                }
            }

            System.out.println(response3.toString());

            URL url4 = new URL(rank);

            HttpURLConnection connection4 = (HttpURLConnection) url4.openConnection();
            connection4.setRequestMethod("GET");
            connection4.setRequestProperty("x-nxopen-api-key", API_KEY);

            int responseCode4 = connection4.getResponseCode();

            BufferedReader in4;
            if (responseCode4 == 200) {
                in4 = new BufferedReader(new InputStreamReader(connection4.getInputStream()));
            } else {
                in4 = new BufferedReader(new InputStreamReader(connection4.getErrorStream()));
            }

            String inputLine4;
            StringBuilder response4 = new StringBuilder();
            while ((inputLine4 = in4.readLine()) != null) {
                response4.append(inputLine4);
            }
            in4.close();

            // 두 번째 API 응답의 JSON 배열 파싱 및 maxDivisionId 추출
            int maxDivisionId = -1;
            JsonArray jsonArray3 = JsonParser.parseString(response3.toString()).getAsJsonArray();
            for (JsonElement jsonElement : jsonArray3) {
                JsonObject jsonObject3 = jsonElement.getAsJsonObject();
                int matchType = jsonObject3.get("matchType").getAsInt();
                int divisionId = jsonObject3.get("division").getAsInt();
                if (matchType == 50) {
                    maxDivisionId = divisionId;
                    break; // matchType이 50인 첫 번째 divisionId만 가져오기 때문에 반복문 종료
                }
            }

            // divisionId와 divisionName을 매핑하는 Map 생성
            Map<Integer, String> divisionMap = new HashMap<>();
            JsonArray jsonArray = JsonParser.parseString(response4.toString()).getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                int divisionId = jsonObject.get("divisionId").getAsInt();
                String divisionName = jsonObject.get("divisionName").getAsString();
                divisionMap.put(divisionId, divisionName);
            }

            // maxDivisionId에 대응하는 divisionName 가져오기
            String maxDivisionName = divisionMap.get(maxDivisionId);

            // model에 데이터 추가
            model.addAttribute("maxDivision", maxDivisionName);
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public void match(Model model, String word, HttpServletRequest request) {
        try {
            String dataFromFirstApi = parseFirstApiResponse(baseUser(model, word));


            String characterName = URLEncoder.encode(dataFromFirstApi, StandardCharsets.UTF_8);
            String urlString = "https://open.api.nexon.com/fconline/v1/user/match?ouid="+ characterName +"&matchtype=50&offset=0&limit=100";
            URL url = new URL(urlString);

            // HTTP connection 설정
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("x-nxopen-api-key", API_KEY);

            int responseCode = connection.getResponseCode();

            BufferedReader in;
            if (responseCode == 200) {
                // responseCode 200 정상응답
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                // responseCode 200 이외의 코드가 반환되었을 경우
                in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            Gson gson = new Gson();
            // JSON 문자열로 변환
            String jsonResponse = response.toString();
            JsonArray jsonArray = gson.fromJson(jsonResponse, JsonArray.class);

            // JSON 배열 데이터를 문자열로 변환하여 모델에 추가
            model.addAttribute("matches", jsonArray);
            model.addAttribute("ouid", dataFromFirstApi);
            for (JsonElement element : jsonArray) {
                System.out.println(element);
            }

            HttpSession session = request.getSession();
            session.setAttribute("word", word);
            session.setAttribute("previousPage", "/fconline/match");
            // 경기 정보에서 경기 ID 추출
            // 주어진 문자열이 JSON 배열인지 확인하고 처리
            try {
                JsonArray matchArray = JsonParser.parseString(response.toString()).getAsJsonArray();
                if (matchArray.size() > 0) {
                    JsonObject firstMatch = matchArray.get(0).getAsJsonObject();
                    String matchId = firstMatch.get("matchId").getAsString();

                    // 추출한 경기 ID와 검색어를 이용하여 matchDetail() 메소드 호출
                    matchDetail(matchId, word, model, request);
                }
            } catch (JsonSyntaxException e) {
                // 주어진 문자열이 유효한 JSON 배열이 아닌 경우
                e.printStackTrace();
                System.out.println(e);
                // 예외 처리를 진행하거나 로그를 출력하거나 필요한 작업을 수행합니다.
            }

        } catch(Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public void matchDetail(@RequestParam("jsonData") String jsonData,
                            @RequestParam("word")String word, Model model,HttpServletRequest request) {
        try {
            String urlString = "https://open.api.nexon.com/fconline/v1/match-detail?matchid=" + jsonData;
            URL url = new URL(urlString);

            // HTTP connection 설정
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("x-nxopen-api-key", API_KEY);

            int responseCode = connection.getResponseCode();

            BufferedReader in;
            if (responseCode == 200) {
                // responseCode 200 정상응답
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                // responseCode 200 이외의 코드가 반환되었을 경우
                in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // 첫 번째 API 응답을 파싱하여 필요한 데이터 추출
            String dataFromFirstApi = parseFirstApiResponse(ouid(word));
            model.addAttribute("ouid", dataFromFirstApi);
            model.addAttribute("matches", response.toString());
            model.addAttribute("tests", response.toString());

            System.out.println(response.toString());
        } catch(Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    private static String parseFirstApiResponse(String response) {
        // JSON 형식의 응답을 파싱하여 필요한 데이터를 추출
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        String data = jsonObject.get("ouid").getAsString();
        return data;
    }
}
