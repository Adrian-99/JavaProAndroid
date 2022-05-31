package pl.adrian99.javaproandroid.ui.webtools;

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
import pl.adrian99.javaproandroid.data.dtos.WebTool;
import pl.adrian99.javaproandroid.databinding.FragmentWebToolsSelectionBinding;

public class WebToolsSelectionFragment extends Fragment implements AdapterView.OnItemClickListener {

    private List<WebTool> webTools = new ArrayList<>();
    private final ArrayList<String> webToolsUrls = new ArrayList<>();
    private ArrayAdapter<String> webToolsUrlsAdapter;
    private FragmentWebToolsSelectionBinding binding;
    private Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWebToolsSelectionBinding.inflate(inflater, container, false);
        activity = getActivity();

        webToolsUrlsAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_list_item_1,
                webToolsUrls
        );
        binding.linksList.setAdapter(webToolsUrlsAdapter);
        binding.linksList.setOnItemClickListener(this);

        AsyncHttpClient.get("webtools",
                WebTool[].class,
                response -> activity.runOnUiThread(() -> {
                    if (response != null) {
                        webTools = Arrays.asList(response);
                        webToolsUrls.clear();
                        webTools.forEach(webTool -> webToolsUrls.add(webTool.getName()));
                        webToolsUrlsAdapter.notifyDataSetChanged();
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
        arguments.putString("url", webTools.get(position).getUrl());
        Navigation.findNavController(view).navigate(R.id.action_nav_web_tools_selection_to_nav_web_tools, arguments);
    }
}