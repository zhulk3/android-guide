package com.longkai.photogallery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//在写一个Fragment时通常要考虑是不是可以先抽象出基类
public class PhotoGalleryFragment extends VisibleFragment {
  private static final String TAG = "PhotoGalleryFragment";
  private RecyclerView mRecyclerView;
  private List<GalleryItem> mGalleryItems = new ArrayList<>();
  private ThumbnailDownload mThumbnailDownload;
  private static final String HANDLE_NAME = "download_image";

  public static Fragment newInstance() {
    return new PhotoGalleryFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
    setHasOptionsMenu(true);
    Intent intent = PollService.newIntent(getActivity());
//    getActivity().startService(intent);
    PollService.setServiceAlarm(getContext(), true);
    updateItems();
    Handler responseHandler = new Handler();
    mThumbnailDownload = new ThumbnailDownload<PhotoHolder>(HANDLE_NAME, responseHandler);
    mThumbnailDownload.setThumbnailDownloadListener(
        new ThumbnailDownload.ThumbnailDownloadListener<PhotoHolder>() {

          @Override
          public void onThumbnailDownloaded(PhotoHolder target, Bitmap thumbnail) {
            Drawable drawable = new BitmapDrawable(getResources(), thumbnail);
            target.bindDrawable(drawable);
          }
        });
    mThumbnailDownload.start();
    mThumbnailDownload.getLooper();
    Log.i(TAG, "background thread started");
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.fragment_photo_gallery, menu);
    MenuItem searchItem = menu.findItem(R.id.menu_item_search);
    final SearchView searchView = (SearchView) searchItem.getActionView();
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        QueryPreferences.setStoredQuery(getActivity(), query);
        updateItems();
        return true;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        return false;
      }
    });

    MenuItem toggleItem = menu.findItem(R.id.menu_item_toggle_poll);
    if (PollService.isServiceAlarmOn(getActivity())) {
      toggleItem.setTitle(R.string.stop_polling);
    } else {
      toggleItem.setTitle(R.string.start_polling);
    }
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_item_clear:
        QueryPreferences.setStoredQuery(getActivity(), null);
        updateItems();
        return true;
      case R.id.menu_item_toggle_poll:
        boolean shouldStartAlarm = !PollService.isServiceAlarmOn(getActivity());
        PollService.setServiceAlarm(getActivity(), shouldStartAlarm);
        getActivity().invalidateOptionsMenu();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void updateItems() {
    String text = QueryPreferences.getStoredQuery(getActivity());
    new FetchItemsTask(text).execute();
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
    mRecyclerView = view.findViewById(R.id.photo_recycler_view);
    mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
    setUpAdapter();
    return view;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mThumbnailDownload.quit();
    mThumbnailDownload.clearQueue();
    Log.i(TAG, "background thread stopped");
  }

  private class FetchItemsTask extends AsyncTask<Void, Void, List<GalleryItem>> {
    private String mQuery;

    public FetchItemsTask(String text) {
      mQuery = text;
    }

    @Override
    protected List<GalleryItem> doInBackground(Void... voids) {
      if (mQuery == null) {
        return new FlickrFetcher().fetchRecentPhotos();
      } else {
        return new FlickrFetcher().searchPhotos(mQuery);
      }
    }

    @Override
    protected void onPostExecute(List<GalleryItem> items) {
      mGalleryItems = items;
      setUpAdapter();
    }
  }

  private class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private ImageView mImageView;
    private GalleryItem mGalleryItem;

    public PhotoHolder(@NonNull View itemView) {
      super(itemView);
      mImageView = (ImageView) itemView;
      itemView.setOnClickListener(this);
    }

    public void bindDrawable(Drawable drawable) {
      mImageView.setImageDrawable(drawable);
    }

    public void bindGalleryItem(GalleryItem item) {
      mGalleryItem = item;
    }

    @Override
    public void onClick(View v) {
      startActivity(PhotoPageActivity.newIntent(getActivity(), mGalleryItem.getPhotoPageUri()));
    }
  }

  private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
    private List<GalleryItem> mGalleryItems;

    public PhotoAdapter(List<GalleryItem> galleryItems) {
      mGalleryItems = galleryItems;
    }

    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
      return new PhotoHolder(layoutInflater.inflate(R.layout.list_item_gallery, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
      GalleryItem item = mGalleryItems.get(position);
      holder.bindGalleryItem(item);
      Drawable placeholder = getResources().getDrawable(R.drawable.ic_launcher_foreground);
      holder.bindDrawable(placeholder);
      mThumbnailDownload.queueThumbnail(holder, item.getUrl());
    }

    @Override
    public int getItemCount() {
      return mGalleryItems.size();
    }
  }

  private void setUpAdapter() {
    if (isAdded()) {
      mRecyclerView.setAdapter(new PhotoAdapter(mGalleryItems));
    }
  }
}
