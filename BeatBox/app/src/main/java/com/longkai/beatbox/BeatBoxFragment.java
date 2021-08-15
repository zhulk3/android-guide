package com.longkai.beatbox;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.longkai.beatbox.databinding.FragmentBeatBoxBinding;
import com.longkai.beatbox.databinding.ListItemSoundBinding;

public class BeatBoxFragment extends Fragment {
  private BeatBox mBeatBox;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBeatBox = new BeatBox(getActivity());

  }

  public static BeatBoxFragment newInstance() {
    return new BeatBoxFragment();
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    FragmentBeatBoxBinding fragmentBeatBoxBinding =
        DataBindingUtil.inflate(inflater, R.layout.fragment_beat_box, container, false);
    fragmentBeatBoxBinding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
    fragmentBeatBoxBinding.recyclerView.setAdapter(new SoundAdapter(mBeatBox.getSounds()));
    return fragmentBeatBoxBinding.getRoot();
  }

  private class SoundHolder extends RecyclerView.ViewHolder {
    private ListItemSoundBinding mListItemSoundBinding;

    public SoundHolder(ListItemSoundBinding itemSoundBinding) {
      super(itemSoundBinding.getRoot());
      mListItemSoundBinding = itemSoundBinding;
      mListItemSoundBinding.setViewModel(new SoundViewModel(mBeatBox));
    }

    public void bind(Sound sound) {
      mListItemSoundBinding.getViewModel().setSound(sound);
      mListItemSoundBinding.executePendingBindings();
    }
  }

  public class SoundAdapter extends RecyclerView.Adapter<SoundHolder> {
    private List<Sound> mSounds;

    public SoundAdapter(List<Sound> sounds) {
      mSounds = sounds;
    }

    @NonNull
    @Override
    public SoundHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
      ListItemSoundBinding binding =
          DataBindingUtil.inflate(layoutInflater, R.layout.list_item_sound, parent, false);
      return new SoundHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SoundHolder holder, int position) {
      Sound sound = mSounds.get(position);
      holder.bind(sound);
    }

    @Override
    public int getItemCount() {
      return mSounds.size();
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mBeatBox.release();
  }
}
