package pl.adrian99.javaproandroid.data;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class AsyncHttpClient {

    private static final String BASE_URL = "http://192.168.1.3:8080/";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static final Executor executor = Executors.newSingleThreadExecutor();
    private static final OkHttpClient client = new OkHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    public interface Callback<T> {
        void call(T arg);
    }

    public static <T> void get(String endpoint, Class<T> responseType, Callback<T> callback) {
        executor.execute(() -> {
            try {
                var request = new Request.Builder()
                        .url(BASE_URL + endpoint)
                        .build();
                try (var response = client.newCall(request).execute()) {
                    if (response.body() != null) {
                        callback.call(mapper.readValue(response.body().string(), responseType));
                    }
                }
            } catch (IOException exception) {
                System.err.println(exception.getMessage());
            }
        });
    }

    public static <T> void post(String endpoint, Object requestBody, Class<T> responseType, Callback<T> callback) {
        executor.execute(() -> {
            try {
                var body = RequestBody.create(JSON, mapper.writeValueAsString(requestBody));
                var request = new Request.Builder()
                        .url(BASE_URL + endpoint)
                        .post(body)
                        .build();
                try (var response = client.newCall(request).execute()) {
                    if (response.body() != null) {
                        callback.call(mapper.readValue(response.body().string(), responseType));
                    }
                }
            } catch (IOException exception) {
                System.err.println(exception.getMessage());
            }
        });
    }
}
