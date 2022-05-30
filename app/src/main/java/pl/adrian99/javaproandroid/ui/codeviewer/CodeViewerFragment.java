package pl.adrian99.javaproandroid.ui.codeviewer;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import pl.adrian99.javaproandroid.R;
import pl.adrian99.javaproandroid.data.AsyncHttpClient;
import pl.adrian99.javaproandroid.data.dtos.Code;
import pl.adrian99.javaproandroid.data.dtos.CodeExample;
import pl.adrian99.javaproandroid.databinding.FragmentCodeViewerBinding;
import pl.adrian99.javaproandroid.util.CodeAdapter;

public class CodeViewerFragment extends Fragment {

    private Long codeExampleId;
    private final ArrayList<Code> codes = new ArrayList<>();
    private CodeAdapter codeAdapter;
    private FragmentCodeViewerBinding binding;
    private Activity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            codeExampleId = getArguments().getLong("exampleId");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCodeViewerBinding.inflate(inflater, container, false);
        activity = getActivity();

        codeAdapter = new CodeAdapter(getContext(), codes);
        binding.codesList.setAdapter(codeAdapter);

        AsyncHttpClient.get("code/" + codeExampleId,
                CodeExample.class,
                response -> activity.runOnUiThread(() -> {
                    binding.name.setText(response.getName());
                    binding.context.setText(response.getContext());
                    codeAdapter.clear();
                    response.getCodes().forEach(codeAdapter::add);
//                    codeAdapter.addAll(response.getCodes());
                }));

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
        activity = null;
    }
}