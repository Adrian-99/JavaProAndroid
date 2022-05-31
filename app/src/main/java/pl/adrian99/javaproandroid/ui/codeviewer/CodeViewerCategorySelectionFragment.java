package pl.adrian99.javaproandroid.ui.codeviewer;

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
import pl.adrian99.javaproandroid.data.dtos.CodeCategory;
import pl.adrian99.javaproandroid.data.dtos.SlideCategory;
import pl.adrian99.javaproandroid.databinding.FragmentCodeViewerCategorySelectionBinding;
import pl.adrian99.javaproandroid.databinding.FragmentSlideViewerSelectionBinding;

public class CodeViewerCategorySelectionFragment extends Fragment implements AdapterView.OnItemClickListener {

    private List<CodeCategory> codeCategories;
    private final ArrayList<String> codeCategoriesNames = new ArrayList<>();
    private ArrayAdapter<String> codeCategoriesNamesAdapter;
    private FragmentCodeViewerCategorySelectionBinding binding;
    private Activity activity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCodeViewerCategorySelectionBinding.inflate(inflater, container, false);
        activity = getActivity();

        codeCategoriesNamesAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_list_item_1,
                codeCategoriesNames
        );
        binding.categoriesList.setAdapter(codeCategoriesNamesAdapter);
        binding.categoriesList.setOnItemClickListener(this);

        AsyncHttpClient.get(
                "code/categories",
                CodeCategory[].class,
                response -> activity.runOnUiThread(() -> {
                    if (response != null) {
                        codeCategories = Arrays.asList(response);
                        codeCategoriesNames.clear();
                        codeCategories.forEach(category -> codeCategoriesNames.add(category.getName()));
                        codeCategoriesNamesAdapter.notifyDataSetChanged();
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
        activity = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        var arguments = new Bundle();
        arguments.putLong("categoryId", codeCategories.get(position).getId());
        Navigation.findNavController(view).navigate(
                R.id.action_nav_code_viewer_category_selection_to_nav_code_viewer_example_selection,
                arguments
        );
    }
}