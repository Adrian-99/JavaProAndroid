package pl.adrian99.javaproandroid.ui.webtools;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.HashMap;

import pl.adrian99.javaproandroid.R;
import pl.adrian99.javaproandroid.databinding.FragmentTestSelectionBinding;
import pl.adrian99.javaproandroid.databinding.FragmentWebToolsSelectionBinding;

public class WebToolsSelectionFragment extends Fragment implements AdapterView.OnItemClickListener {

    private final HashMap<String, String> links = new HashMap<>();
    private ArrayAdapter<String> linksAdapter;
    private FragmentWebToolsSelectionBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        links.put("https://docs.oracle.com/en/java/javase/18/docs/api/index.html", "JDK 18 API");
        links.put("https://developer.android.com/guide", "Android Guide");
        links.put("https://openjfx.io/javadoc/18/", "JavaFX 18 API");
        links.put("https://www.oracle.com/pl/java/technologies/java-ee-glance.html", "Java EE API");
//        linksAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWebToolsSelectionBinding.inflate(inflater, container, false);

        linksAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_list_item_1,
                links.values().toArray(new String[0])
        );
        binding.linksList.setAdapter(linksAdapter);
        binding.linksList.setOnItemClickListener(this);

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
        arguments.putString("link", links.keySet().toArray(new String[0])[position]);
        Navigation.findNavController(view).navigate(R.id.action_nav_web_tools_selection_to_nav_web_tools, arguments);
    }
}