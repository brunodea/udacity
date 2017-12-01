package udabake.brunodea.com.udabake.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import udabake.brunodea.com.udabake.model.RecipeStepModel;

public class RecipeStepDetailsFragment extends Fragment {
    private static final String RECIPE_STEP_MODEL_ARG = "recipe_step_model_arg";
    private static final String RECIPE_STEP_POS_ARG = "recipe_step_pos_arg";

    @BindView(R.id.tv_step_description) TextView mTVStepDescription;
    @BindView(R.id.exo_player_view) SimpleExoPlayerView mExoPlayerView;
    @BindView(R.id.bt_next_step) Button mBTNext;
    @BindView(R.id.bt_previous_step) Button mBTPrev;

    private SimpleExoPlayer mExoPlayer;

    private RecipeStepModel mRecipeStepModel;
    private OnActionListener mOnActionListener;

    public enum StepPosition {
        First,
        Last,
        Other,
    }

    private StepPosition mStepPosition;

    public RecipeStepDetailsFragment() {
    }

    // Steps position start from 0.
    public static RecipeStepDetailsFragment newInstance(RecipeStepModel model, StepPosition position) {
        RecipeStepDetailsFragment frag = new RecipeStepDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(RECIPE_STEP_MODEL_ARG, model);
        args.putString(RECIPE_STEP_POS_ARG, position.toString());
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_recipe_details_item, container, false);
        ButterKnife.bind(this, view);

        mTVStepDescription.setText(mRecipeStepModel.getDescription());
        String video_url = mRecipeStepModel.getVideoURL();
        if (video_url != null && !video_url.isEmpty()) {
            initializePlayer(Uri.parse(video_url));
        }

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
