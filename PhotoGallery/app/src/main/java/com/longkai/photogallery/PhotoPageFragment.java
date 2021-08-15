package com.longkai.photogallery;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PhotoPageFragment extends VisibleFragment {
  private static final String ARG_URI = "photo_page_url";
  private Uri mUri;
  private WebView mWebView;
  private ProgressBar mProgressBar;

  public static PhotoPageFragment newInstance(Uri uri) {
    Bundle bundle = new Bundle();
    bundle.putParcelable(ARG_URI, uri);
    PhotoPageFragment photoPageFragment = new PhotoPageFragment();
    photoPageFragment.setArguments(bundle);
    return photoPageFragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Bundle bundle = getArguments();
    mUri = bundle.getParcelable(ARG_URI);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_photo_page, container, false);
    mWebView = view.findViewById(R.id.web_view);
    mProgressBar = view.findViewById(R.id.progress_bar);
    mProgressBar.setMax(100);
    mWebView.getSettings().setJavaScriptEnabled(true);
    mWebView.setWebViewClient(new WebViewClient());
    mWebView.loadUrl(String.valueOf(mUri));

    mWebView.setWebChromeClient(new WebChromeClient() {
      @Override
      public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if (newProgress == 100) {
          mProgressBar.setVisibility(View.GONE);
        } else {
          mProgressBar.setVisibility(View.VISIBLE);
          mProgressBar.setProgress(newProgress);
        }
      }

      @Override
      public void onReceivedTitle(WebView view, String title) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(title);
      }
    });
    return view;
  }
}
