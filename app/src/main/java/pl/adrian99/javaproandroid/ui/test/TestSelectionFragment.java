package pl.adrian99.javaproandroid.ui.test;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import pl.adrian99.javaproandroid.R;
import pl.adrian99.javaproandroid.databinding.FragmentTestBinding;
import pl.adrian99.javaproandroid.databinding.FragmentTestSelectionBinding;

public class TestSelectionFragment extends Fragment implements AdapterView.OnItemClickListener {

    private final HashMap<Integer, String> tests = new HashMap<>();
    private ArrayAdapter<String> testsAdapter;
    private FragmentTestSelectionBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tests.put(0, "Test1");
        tests.put(1, "Test2");
        tests.put(2, "Test3");
        tests.put(3, "Test4");
//        testsAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTestSelectionBinding.inflate(inflater, container, false);

        testsAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_list_item_1,
                tests.values().toArray(new String[0])
        );
        binding.testsList.setAdapter(testsAdapter);
        binding.testsList.setOnItemClickListener(this);

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
        arguments.putInt("testId", tests.keySet().toArray(new Integer[0])[position]);
        Navigation.findNavController(view).navigate(R.id.action_nav_test_selection_to_testFragment, arguments);
    }
}