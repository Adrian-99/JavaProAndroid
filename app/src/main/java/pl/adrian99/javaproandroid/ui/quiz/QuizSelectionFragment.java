package pl.adrian99.javaproandroid.ui.quiz;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.adrian99.javaproandroid.R;
import pl.adrian99.javaproandroid.data.AsyncHttpClient;
import pl.adrian99.javaproandroid.data.dtos.QuizCategory;
import pl.adrian99.javaproandroid.databinding.FragmentQuizSelectionBinding;

public class QuizSelectionFragment extends Fragment implements AdapterView.OnItemClickListener {

    private List<QuizCategory> tests = new ArrayList<>();
    private final ArrayList<String> testNames = new ArrayList<>();
    private FragmentQuizSelectionBinding binding;
    private Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentQuizSelectionBinding.inflate(inflater, container, false);
        activity = getActivity();

        ArrayAdapter<String> testsAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_list_item_1,
                testNames
        );
        binding.testsList.setAdapter(testsAdapter);
        binding.testsList.setOnItemClickListener(this);

        AsyncHttpClient.get("quiz/categories",
                QuizCategory[].class,
                response -> activity.runOnUiThread(() -> {
                    if (response != null) {
                        tests = Arrays.asList(response);
                        testNames.clear();
                        tests.forEach(test -> testNames.add(test.getName()));
                        testsAdapter.notifyDataSetChanged();
                    }
                }));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle arguments = new Bundle();
        arguments.putLong("quizCategoryId", tests.get(position).getId());
        Navigation.findNavController(view).navigate(R.id.action_nav_quiz_selection_to_quizFragment, arguments);
    }
}