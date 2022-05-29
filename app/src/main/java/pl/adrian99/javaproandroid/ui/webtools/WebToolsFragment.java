package pl.adrian99.javaproandroid.ui.webtools;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import pl.adrian99.javaproandroid.databinding.FragmentWebToolsBinding;

public class WebToolsFragment extends Fragment {

    private String url = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString("url");
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentWebToolsBinding binding = FragmentWebToolsBinding.inflate(inflater, container, false);

        binding.webView.loadUrl(url);
        binding.webView.setWebViewClient(new MyWebViewClient());
        binding.webView.getSettings().setJavaScriptEnabled(true);

        return binding.getRoot();
    }

    private static class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
    }
}