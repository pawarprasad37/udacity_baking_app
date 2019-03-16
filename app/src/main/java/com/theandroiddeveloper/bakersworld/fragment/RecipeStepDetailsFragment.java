package com.theandroiddeveloper.bakersworld.fragment;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.theandroiddeveloper.bakersworld.CommonUtil;
import com.theandroiddeveloper.bakersworld.R;
import com.theandroiddeveloper.bakersworld.activity.BaseActivity;
import com.theandroiddeveloper.bakersworld.model.RecipeStep;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeStepDetailsFragment extends Fragment implements View.OnClickListener {
    private RecipeStep selectedRecipeStep;
    private View rootView;
    private boolean isMasterDetailFlow;
    private PlayerView playerView;
    private ExoPlayer exoPlayer;
    private int activeStepIndex;
    private int totalStepCount;
    private NavButtonClickListener navButtonClickListener;

    public interface NavButtonClickListener {
        void onPreviousStepClicked();

        void onNextStepClicked();
    }

    public RecipeStepDetailsFragment() {
        // Required empty public constructor
    }

    public RecipeStepDetailsFragment setNavButtonClickListener(NavButtonClickListener listener) {
        this.navButtonClickListener = listener;
        return this;
    }

    public RecipeStepDetailsFragment setIsMasterDetailFlow(boolean isMasterDetailFlow) {
        this.isMasterDetailFlow = isMasterDetailFlow;
        return this;
    }

    //selectedRecipeStep is null
    public RecipeStepDetailsFragment setSelectedRecipeStep(RecipeStep selectedRecipeStep,
                                                           int activeStepIndex,
                                                           int totalStepCount) {
        this.selectedRecipeStep = selectedRecipeStep;
        this.activeStepIndex = activeStepIndex;
        this.totalStepCount = totalStepCount;
        reloadUI();
        return this;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_recipe_step_details, container,
                false);
        View btPrevious = rootView.findViewById(R.id.btPrevious);
        View btNext = rootView.findViewById(R.id.btNext);
        if (isMasterDetailFlow) {
            btPrevious.setVisibility(View.GONE);
            btNext.setVisibility(View.GONE);
        } else {
            btPrevious.setOnClickListener(this);
            btNext.setOnClickListener(this);
            if (activeStepIndex == 0) {
                btPrevious.setVisibility(View.INVISIBLE);
            } else if (activeStepIndex == totalStepCount - 1) {
                btNext.setVisibility(View.INVISIBLE);
            }
        }
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        reloadUI();
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseExoPlayer();
    }

    private void setupExoPlayer() {
        //create exo player
        exoPlayer = ExoPlayerFactory
                .newSimpleInstance(getContext(), new DefaultRenderersFactory(getContext()),
                        new DefaultTrackSelector(), new DefaultLoadControl());

        ExtractorMediaSource extractorMediaSource =
                new ExtractorMediaSource.Factory(new DefaultDataSourceFactory(getContext(),
                        Util.getUserAgent(getContext(), getString(R.string.app_name))))
                        .setExtractorsFactory(new DefaultExtractorsFactory())
                        .createMediaSource(Uri.parse(selectedRecipeStep.getVideoURL()));

        exoPlayer.prepare(extractorMediaSource);
        exoPlayer.setPlayWhenReady(true);

        playerView.setPlayer(exoPlayer);

        playerView.setBackgroundColor(Color.BLACK);
    }

    private void setPlayerOrImageViewDimens(View targetView) {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        int playerHeight = isMasterDetailFlow ? CommonUtil.getDeviceHeight(activity) :
                CommonUtil.getDeviceWidth(activity) / 2;
        RelativeLayout.LayoutParams params = new RelativeLayout
                .LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, playerHeight);
        targetView.setLayoutParams(params);
    }

    private void reloadUI() {
        if (!isAdded() || rootView == null || selectedRecipeStep == null) {
            return;
        }
        releaseExoPlayer();
        playerView = rootView.findViewById(R.id.playerView);
        ImageView imageView = rootView.findViewById(R.id.imageView);

        if (selectedRecipeStep.getVideoURL() != null &&
                !selectedRecipeStep.getVideoURL().isEmpty()) {
            //display video
            imageView.setVisibility(View.GONE);
            playerView.setVisibility(View.VISIBLE);
            setPlayerOrImageViewDimens(playerView);
            setupExoPlayer();
            setFullScreenModeIfNeeded();
        } else {
            //display image
            imageView.setVisibility(View.VISIBLE);
            playerView.setVisibility(View.GONE);
            setPlayerOrImageViewDimens(imageView);
            Glide
                    .with(getContext())
                    .load(selectedRecipeStep.getThumbnailURL())
                    .fitCenter()
                    .placeholder(R.mipmap.ic_image_placeholder)
                    .into(imageView);
        }

        ((TextView) rootView.findViewById(R.id.tvDescription))
                .setText(selectedRecipeStep.getDescription());
    }

    private void setFullScreenModeIfNeeded() {
        if (rootView.findViewById(R.id.landscapeIdentifier) != null) {
            //landscape mode; go immersive.
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            ((BaseActivity) getActivity()).getSupportActionBar()
                    .hide();
        } else {
            ((BaseActivity) getActivity()).getSupportActionBar()
                    .show();
        }
    }

    private void releaseExoPlayer() {
        if (exoPlayer == null) {
            return;
        }
        exoPlayer.stop();
        exoPlayer.release();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btPrevious) {
            if (navButtonClickListener != null) {
                navButtonClickListener.onPreviousStepClicked();
            }
        } else if (v.getId() == R.id.btNext) {
            if (navButtonClickListener != null) {
                navButtonClickListener.onNextStepClicked();
            }
        }
    }

}
