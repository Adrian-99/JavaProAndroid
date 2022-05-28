package pl.adrian99.javaproandroid.ui.webtools;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import pl.adrian99.javaproandroid.R;
import pl.adrian99.javaproandroid.databinding.FragmentWebToolsBinding;

public class WebToolsFragment extends Fragment {

    private FragmentWebToolsBinding binding;
    private String link = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            link = getArguments().getString("link");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWebToolsBinding.inflate(inflater, container, false);

        binding.webView.loadUrl(link);
        binding.webView.setWebViewClient(new MyWebViewClient());

        return binding.getRoot();
    }

    private static class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
    }
}