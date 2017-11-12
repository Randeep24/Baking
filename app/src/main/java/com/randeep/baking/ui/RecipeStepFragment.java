package com.randeep.baking.ui;


import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.randeep.baking.R;
import com.randeep.baking.util.Utility;
import com.randeep.baking.bean.RecipeSteps;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.randeep.baking.util.Constants.IS_TABLET;
import static com.randeep.baking.util.Constants.RECIPE_STEP;
import static com.randeep.baking.util.Constants.STEP_NUMBER;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeStepFragment extends Fragment implements Player.EventListener {


    RecipeSteps recipeSteps;

    SimpleExoPlayer mPlayer;


    @BindView(R.id.step)
    TextView step;

    @BindView(R.id.playerView)
    SimpleExoPlayerView mPlayerView;

    @BindView(R.id.videoThumbnail)
    ImageView videoThumbnail;

    long playerPosition = 0;

    public RecipeStepFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            playerPosition = savedInstanceState.getLong("PLAYER_POSITION");
        }

        Bundle bundle = getArguments();
        recipeSteps = bundle.getParcelable(RECIPE_STEP);

        View rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);
        ButterKnife.bind(this, rootView);

        step.setText(recipeSteps.getDescription());


        if (recipeSteps.getVideoUrl() == null || recipeSteps.getVideoUrl().isEmpty()) {

            mPlayerView.setVisibility(View.INVISIBLE);
            if (!recipeSteps.getThumbnailUrl().isEmpty()) {
                Picasso.with(getActivity()).load(recipeSteps.getThumbnailUrl())
                        .fit()
                        .into(videoThumbnail);
            } else {
                mPlayerView.setVisibility(View.GONE);
            }
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!Utility.isSW600dp((AppCompatActivity) getActivity()) &&
                getActivity().getResources().getConfiguration().orientation ==
                        Configuration.ORIENTATION_LANDSCAPE) {

            mPlayerView.getLayoutParams().height =
                    Utility.getWidthOfDevice((AppCompatActivity) getActivity());

            View decorView = getActivity().getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }

    }

    private void initializePlayer(Uri mediaUri) {
        if (mPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            DefaultRenderersFactory defaultRenderersFactory = new DefaultRenderersFactory(getActivity());

            mPlayer = ExoPlayerFactory.newSimpleInstance(defaultRenderersFactory, trackSelector, loadControl);
            mPlayerView.setPlayer(mPlayer);

            mPlayer.addListener(this);

            String userAgent = Util.getUserAgent(getActivity(), "RecipeStep");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            mPlayer.prepare(mediaSource);
            mPlayer.setPlayWhenReady(true);
            mPlayer.seekTo(playerPosition);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();

    }

    @Override
    public void onResume() {
        super.onResume();

        if (recipeSteps.getVideoUrl() != null && !recipeSteps.getVideoUrl().isEmpty()) {
            initializePlayer(Uri.parse(recipeSteps.getVideoUrl()));
        }

    }

    private void releasePlayer() {
        if (mPlayer != null) {
            playerPosition = mPlayer.getCurrentPosition();
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong("PLAYER_POSITION", playerPosition);

    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_ENDED) {
            mPlayerView.findViewById(R.id.exo_play).setVisibility(View.VISIBLE);
            mPlayerView.findViewById(R.id.exo_pause).setVisibility(View.GONE);
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

        if (Utility.isInternetConnected(getActivity())) {
            mPlayerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

}
