package pl.adrian99.javaproandroid.ui.slideviewer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.HashMap;

import pl.adrian99.javaproandroid.data.AsyncHttpClient;
import pl.adrian99.javaproandroid.data.dtos.SlideIds;
import pl.adrian99.javaproandroid.databinding.FragmentSlideviewerBinding;

public class SlideViewerFragment extends Fragment {

    private Long categoryId;
    private SlideIds slideIds;
    private int currentSlide = 0;
    private HashMap<Long, Bitmap> slides;
    private FragmentSlideviewerBinding binding;
    private Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getLong("categoryId");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSlideviewerBinding.inflate(inflater, container, false);
        activity = getActivity();

        AsyncHttpClient.get(
                "slides/ids/" + categoryId,
                SlideIds.class,
                response -> {
                    slideIds = response;
                    slides = new HashMap<>();
                    if (slideIds.getSlideIds().size() > 0) {
                        activity.runOnUiThread(this::showSlide);
                    }
                }
        );

        binding.buttonPrev.setOnClickListener(view -> {
            if (currentSlide > 0) {
                currentSlide--;
                showSlide();
            }
        });
        binding.buttonNext.setOnClickListener(view -> {
            if (currentSlide < slideIds.getSlideIds().size() - 1) {
                currentSlide++;
                showSlide();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showSlide() {
        var slideId = slideIds.getSlideIds().get(currentSlide);
        if (slides.containsKey(slideId)) {
            binding.slideDisplay.setImageBitmap(slides.get(slideId));
        } else {
            AsyncHttpClient.getBytes(
                    "slides/" + slideId,
                    response -> activity.runOnUiThread(() -> {
                        slides.put(
                                slideId,
                                BitmapFactory.decodeByteArray(response, 0, response.length)
                        );
                        if (slideIds.getSlideIds().get(currentSlide).equals(slideId)) {
                            binding.slideDisplay.setImageBitmap(slides.get(slideId));
                        }
                    })
            );
        }
    }
}