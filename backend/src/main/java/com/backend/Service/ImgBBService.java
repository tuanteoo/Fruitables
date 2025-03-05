package com.backend.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class ImgBBService {

    private static final String IMGBB_API_URL = "https://api.imgbb.com/1/upload";
    private static final String IMGBB_API_KEY = "your_api_key_here"; // Thay bằng API key của bạn

    public String uploadImage(MultipartFile file) throws IOException, InterruptedException {
        // Chuyển đổi file thành base64
        String base64Image = Base64.getEncoder().encodeToString(file.getBytes());

        // Tạo body cho request
        Map<String, String> body = new HashMap<>();
        body.put("key", IMGBB_API_KEY);
        body.put("image", base64Image);

        // Chuyển đổi body thành JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(body);

        // Gửi request đến ImgBB API
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(IMGBB_API_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // In ra response để debug
        System.out.println("Response: " + response.body());

        // Xử lý response
        if (response.statusCode() == 200) {
            Map<String, Object> responseMap = objectMapper.readValue(response.body(), Map.class);
            Map<String, Object> data = (Map<String, Object>) responseMap.get("data");
            return data.get("url").toString(); // Trả về URL của ảnh
        } else {
            throw new IOException("Failed to upload image: " + response.body());
        }
    }
}
