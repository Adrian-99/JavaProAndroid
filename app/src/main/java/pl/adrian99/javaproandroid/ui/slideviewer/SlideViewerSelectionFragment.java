package pl.adrian99.javaproandroid.ui.slideviewer;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.adrian99.javaproandroid.R;
import pl.adrian99.javaproandroid.data.AsyncHttpClient;
import pl.adrian99.javaproandroid.data.dtos.SlideCategory;
import pl.adrian99.javaproandroid.databinding.FragmentSlideViewerSelectionBinding;

public class SlideViewerSelectionFragment extends Fragment implements AdapterView.OnItemClickListener {

    private List<SlideCategory> slideCategories;
    private final ArrayList<String> slideCategoriesNames = new ArrayList<>();
    private ArrayAdapter<String> slideCategoriesNamesAdapter;
    private FragmentSlideViewerSelectionBinding binding;
    private Activity activity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSlideViewerSelectionBinding.inflate(inflater, container, false);
        activity = getActivity();

        slideCategoriesNamesAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_list_item_1,
                slideCategoriesNames
        );
        binding.categoriesList.setAdapter(slideCategoriesNamesAdapter);
        binding.categoriesList.setOnItemClickListener(this);

        AsyncHttpClient.get(
                "slides/categories",
                SlideCategory[].class,
                response -> activity.runOnUiThread(() -> {
                    if (response != null) {
                        slideCategories = Arrays.asList(response);
                        slideCategoriesNames.clear();
                        slideCategories.forEach(category -> slideCategoriesNames.add(category.getName()));
                        slideCategoriesNamesAdapter.notifyDataSetChanged();
                    }
                }),
                exception -> activity.runOnUiThread(() ->
                        Toast.makeText(activity, getString(R.string.server_connection_error), Toast.LENGTH_LONG).show()
                )
        );

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        var arguments = new Bundle();
        arguments.putLong("categoryId", slideCategories.get(position).getId());
        Navigation.findNavController(view).navigate(R.id.action_nav_slide_viewer_selection_to_nav_slide_viewer2, arguments);
    }
}