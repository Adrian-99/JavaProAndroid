package pl.adrian99.javaproandroid.ui.codeviewer;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
        var codeHeader = inflater.inflate(R.layout.code_header_layout, null);
        binding.codesList.addHeaderView(codeHeader);

        AsyncHttpClient.get("codes/" + codeExampleId,
                CodeExample.class,
                response -> activity.runOnUiThread(() -> {
                    ((TextView)codeHeader.findViewById(R.id.name)).setText(response.getName());
                    ((TextView)codeHeader.findViewById(R.id.context)).setText(response.getContext());
                    codeAdapter.clear();
                    codeAdapter.addAll(response.getCodes());
                }),
                exception -> activity.runOnUiThread(() ->
                        Toast.makeText(activity, getString(R.string.server_connection_error), Toast.LENGTH_LONG).show()
                )
        );

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
        activity = null;
    }
}