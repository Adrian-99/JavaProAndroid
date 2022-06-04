package pl.adrian99.javaproandroid.data;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class AsyncHttpClient {

    private static final String API_CONTEXT = "http://192.168.1.4:8080/public/";
//    private static final String API_CONTEXT = "http://192.168.100.5:8080/public/";
//    private static final String API_CONTEXT = "http://192.168.135.180:8080/public/";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static final Executor executor = Executors.newSingleThreadExecutor();
    private static final OkHttpClient client;
    private static final ObjectMapper mapper = new ObjectMapper();

    public interface Callback<T> {
        void call(T arg);
    }

    static {
        client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .build();
    }

    public static <T> void get(String endpoint, Class<T> responseType, Callback<T> onResponse, Callback<Exception> onError) {
        executor.execute(() -> {
            try {
                var request = new Request.Builder()
                        .url(API_CONTEXT + endpoint)
                        .build();
                try (var response = client.newCall(request).execute()) {
                    if (response.body() != null) {
                        onResponse.call(mapper.readValue(response.body().string(), responseType));
                    } else {
                        onResponse.call(null);
                    }
                }
            } catch (IOException exception) {
                System.err.println(exception.getMessage());
                onError.call(exception);
            }
        });
    }

    public static void getBytes(String endpoint, Callback<byte[]> onResponse, Callback<Exception> onError) {
        executor.execute(() -> {
            try {
                var request = new Request.Builder()
                        .url(API_CONTEXT + endpoint)
                        .build();
                try (var response = client.newCall(request).execute()) {
                    if (response.body() != null) {
                        onResponse.call(response.body().bytes());
                    } else {
                        onResponse.call(null);
                    }
                }
            } catch (IOException exception) {
                System.err.println(exception.getMessage());
                onError.call(exception);
            }
        });
    }

    public static <T> void post(String endpoint,
                                Object requestBody,
                                Class<T> responseType,
                                Callback<T> onResponse,
                                Callback<Exception> onError) {
        executor.execute(() -> {
            try {
                var body = RequestBody.create(JSON, mapper.writeValueAsString(requestBody));
                var request = new Request.Builder()
                        .url(API_CONTEXT + endpoint)
                        .post(body)
                        .build();
                try (var response = client.newCall(request).execute()) {
                    if (response.body() != null) {
                        onResponse.call(mapper.readValue(response.body().string(), responseType));
                    } else {
                        onResponse.call(null);
                    }
                }
            } catch (IOException exception) {
                System.err.println(exception.getMessage());
                onError.call(exception);
            }
        });
    }
}
