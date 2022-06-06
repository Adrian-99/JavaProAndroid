package pl.adrian99.javaproandroid.ui.slideviewer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.HashMap;

import pl.adrian99.javaproandroid.R;
import pl.adrian99.javaproandroid.data.AsyncHttpClient;
import pl.adrian99.javaproandroid.data.dtos.SlideIds;
import pl.adrian99.javaproandroid.databinding.FragmentSlideviewerBinding;
import pl.adrian99.javaproandroid.util.OnSwipeTouchListener;

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

    @SuppressLint("ClickableViewAccessibility")
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
                },
                exception -> activity.runOnUiThread(() ->
                        Toast.makeText(activity, getString(R.string.server_connection_error), Toast.LENGTH_LONG).show()
                )
        );

        binding.buttonPrev.setOnClickListener(view -> showPreviousSlide());
        binding.buttonNext.setOnClickListener(view -> showNextSlide());
        binding.slideDisplay.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            public void onSwipeRight() {
                showPreviousSlide();
            }
            public void onSwipeLeft() {
                showNextSlide();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showNextSlide() {
        if (currentSlide < slideIds.getSlideIds().size() - 1) {
            currentSlide++;
            showSlide();
        }
    }

    private void showPreviousSlide() {
        if (currentSlide > 0) {
            currentSlide--;
            showSlide();
        }
    }

    private void showSlide() {
        var slideId = slideIds.getSlideIds().get(currentSlide);
        if (slides.containsKey(slideId)) {
            binding.slideDisplay.setImageBitmap(slides.get(slideId));
            binding.slideNumber.setText(getString(
                    R.string.slide_viewer_number,
                    currentSlide + 1,
                    slideIds.getSlideIds().size()
            ));
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
                            binding.slideNumber.setText(getString(
                                    R.string.slide_viewer_number,
                                    currentSlide + 1,
                                    slideIds.getSlideIds().size()
                            ));
                        }
                    }),
                    exception -> activity.runOnUiThread(() ->
                            Toast.makeText(activity, getString(R.string.server_connection_error), Toast.LENGTH_LONG).show()
                    )
            );
        }
    }
}