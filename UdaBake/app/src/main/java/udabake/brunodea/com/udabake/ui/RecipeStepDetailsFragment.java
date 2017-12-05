package udabake.brunodea.com.udabake.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import udabake.brunodea.com.udabake.R;
import udabake.brunodea.com.udabake.UdabakeUtil;
import udabake.brunodea.com.udabake.model.RecipeStepModel;

public class RecipeStepDetailsFragment extends Fragment {
    private static final String RECIPE_STEP_MODEL_ARG = "recipe_step_model_arg";
    private static final String RECIPE_STEP_POS_ARG = "recipe_step_pos_arg";
    private static final String VIDEO_POSITION = "start_video_position";
    private static final String VIDEO_HEIGHT_ARG = "video_height";

    @BindView(R.id.tv_step_description) TextView mTVStepDescription;
    @BindView(R.id.bt_next_step) Button mBTNext;
    @BindView(R.id.bt_previous_step) Button mBTPrev;
    @BindView(R.id.tv_no_video) TextView mTVNoVideo;

    @BindView(R.id.exo_player_view) SimpleExoPlayerView mExoPlayerView;
    private SimpleExoPlayer mExoPlayer;

    private RecipeStepModel mRecipeStepModel;
    private OnActionListener mOnActionListener;
    private long mStartPosition;
    private int mVideoHeight;

    public enum StepPosition {
        First,
        Last,
        Other,
    }

    private StepPosition mStepPosition;

    public RecipeStepDetailsFragment() {
    }

    // Steps position start from 0.
    public static RecipeStepDetailsFragment newInstance(RecipeStepModel model, StepPosition position,
                                                        long start_position, int video_height) {
        RecipeStepDetailsFragment frag = new RecipeStepDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(RECIPE_STEP_MODEL_ARG, model);
        args.putString(RECIPE_STEP_POS_ARG, position.toString());
        args.putLong(VIDEO_POSITION, start_position);
        args.putInt(VIDEO_HEIGHT_ARG, video_height);

        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mRecipeStepModel = args.getParcelable(RECIPE_STEP_MODEL_ARG);
            mStepPosition = StepPosition.valueOf(args.getString(RECIPE_STEP_POS_ARG));
            mStartPosition = args.getLong(VIDEO_POSITION);
            mVideoHeight = args.getInt(VIDEO_HEIGHT_ARG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_recipe_details_item, container, false);
        ButterKnife.bind(this, view);

        if (!UdabakeUtil.isLandscape(getContext())) {
            mTVStepDescription.setText(mRecipeStepModel.getDescription());

            mBTPrev.setEnabled(mStepPosition != StepPosition.First);
            mBTNext.setEnabled(mStepPosition != StepPosition.Last);

            mBTPrev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnActionListener.onButtonPrevStepClicked();
                }
            });
            mBTNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnActionListener.onButtonNextStepClicked();
                }
            });
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mExoPlayerView.getLayoutParams();
            params.height = mVideoHeight;
            mExoPlayerView.setLayoutParams(params);
        } else {
            // Only the video player should be visible in landscape mode!
            mTVStepDescription.setVisibility(View.GONE);
            mBTNext.setVisibility(View.GONE);
            mBTPrev.setVisibility(View.GONE);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mExoPlayerView.getLayoutParams();
            params.width = FrameLayout.LayoutParams.MATCH_PARENT;
            params.height = FrameLayout.LayoutParams.MATCH_PARENT;
            mExoPlayerView.setLayoutParams(params);
            mExoPlayerView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |// hide nav bar
                View.SYSTEM_UI_FLAG_FULLSCREEN |// hide status bar
                View.SYSTEM_UI_FLAG_IMMERSIVE
            );
        }

        // ExoPlayer exists in all orientations.
        String video_url = mRecipeStepModel.getVideoURL();
        if (video_url != null && !video_url.isEmpty()) {
            initializePlayer(Uri.parse(video_url));
        } else {
            mTVNoVideo.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
                TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveVideoTrackSelection.Factory(null);
            TrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);
            LoadControl loadControl = new DefaultLoadControl();

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);

            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                    Util.getUserAgent(getContext(), "UdaBake"), null);
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource videoSource = new ExtractorMediaSource(mediaUri, dataSourceFactory,
                    extractorsFactory, null, null);
            mExoPlayer.prepare(videoSource);
            mExoPlayer.seekTo(mStartPosition);
            mExoPlayerView.setPlayer(mExoPlayer);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    public long videoPosition() {
        if (!isAdded()) return 0;
        return mExoPlayer.getCurrentPosition();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnActionListener) {
            mOnActionListener = (OnActionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        releasePlayer();
        super.onDetach();
    }

    public interface OnActionListener {
        void onButtonPrevStepClicked();
        void onButtonNextStepClicked();
    }
}
