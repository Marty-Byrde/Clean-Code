package org.crawler.translate;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class Translater {

    /**
     * Translates the given text to the target language. However, each translation consumes one API call.
     * @param text Text to be translated
     * @param targetLanguage Language to translate the text to
     * @return Translated text
     * @apiNote Since the free-tier is limited to 1000 calls per month, use this method wisely.
     */
    public static String translate (String text, String targetLanguage) throws IOException, InterruptedException {
        if (!System.getenv().containsKey("API_KEY")) {
            return text + " (Not translated because of missing API-Key)";
        }

        String escaped = URLEncoder.encode(text, StandardCharsets.US_ASCII);
        return callAPI("&text=" + escaped + "&target_language=" + targetLanguage);
    }

    private static String callAPI (String body) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://text-translator2.p.rapidapi.com/translate"))
                .header("content-type", "application/x-www-form-urlencoded")
                .header("X-RapidAPI-Key", System.getenv("API_KEY"))
                .header("X-RapidAPI-Host", "text-translator2.p.rapidapi.com")
                .method("POST", HttpRequest.BodyPublishers.ofString("source_language=auto" + body))
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject json = new JSONObject(response.body());
        String status = json.getString("status");

        if (!status.equals("success")) {
            return handleError(body);
        }
        
        return json.getJSONObject("data").getString("translatedText");
    }


    private static String handleError (String request_body) {
        String[] arguments = request_body.split("&");
        String text = "";

        for (String argument : arguments) {
            if (!argument.contains("text=")) continue;
            text = argument.split("=")[1];
        }

        return text + " (Not translated because of translation error)";
    }
}
