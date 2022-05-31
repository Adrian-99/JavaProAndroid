package pl.adrian99.javaproandroid.ui.codeviewer;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import pl.adrian99.javaproandroid.data.dtos.CodeExampleBrief;
import pl.adrian99.javaproandroid.databinding.FragmentCodeViewerCategorySelectionBinding;
import pl.adrian99.javaproandroid.databinding.FragmentCodeViewerExampleSelectionBinding;

public class CodeViewerExampleSelectionFragment extends Fragment implements AdapterView.OnItemClickListener {

    private List<CodeExampleBrief> codeExamples;
    private final ArrayList<String> codeExamplesNames = new ArrayList<>();
    private ArrayAdapter<String> codeExamplesNamesAdapter;
    private Long categoryId;
    private FragmentCodeViewerExampleSelectionBinding binding;
    private Activity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getLong("categoryId");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCodeViewerExampleSelectionBinding.inflate(inflater, container, false);
        activity = getActivity();

        codeExamplesNamesAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_list_item_1,
                codeExamplesNames
        );
        binding.examplesList.setAdapter(codeExamplesNamesAdapter);
        binding.examplesList.setOnItemClickListener(this);

        AsyncHttpClient.get(
                "code/examples/" + categoryId,
                CodeExampleBrief[].class,
                response -> activity.runOnUiThread(() -> {
                    if (response != null) {
                        codeExamples = Arrays.asList(response);
                        codeExamplesNames.clear();
                        codeExamples.forEach(example -> codeExamplesNames.add(example.getName()));
                        codeExamplesNamesAdapter.notifyDataSetChanged();
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
        arguments.putLong("exampleId", codeExamples.get(position).getId());
        Navigation.findNavController(view).navigate(
                R.id.action_nav_code_viewer_example_selection_to_nav_code_viewer,
                arguments
        );
    }
}