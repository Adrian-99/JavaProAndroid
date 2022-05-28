package pl.adrian99.javaproandroid.ui.slideviewer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import pl.adrian99.javaproandroid.databinding.FragmentSlideviewerBinding;

public class SlideViewerFragment extends Fragment {

    private FragmentSlideviewerBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideViewerViewModel slideViewerViewModel =
                new ViewModelProvider(this).get(SlideViewerViewModel.class);

        binding = FragmentSlideviewerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSlideshow;
        slideViewerViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}