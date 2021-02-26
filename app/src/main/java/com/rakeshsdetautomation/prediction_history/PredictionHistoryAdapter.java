package com.rakeshsdetautomation.prediction_history;

import android.content.Context;
import android.graphics.Color;
import android.opengl.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.rakeshsdetautomation.cricpredict.R;

import java.util.ArrayList;

public class PredictionHistoryAdapter extends ArrayAdapter<PredictionHistory> {

    Context context;

    public PredictionHistoryAdapter(@NonNull Context context, ArrayList<PredictionHistory> predictionHistoryArrayList) {
        super(context, 0, predictionHistoryArrayList);
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.predictions_scores_history_list_item, parent, false);
        }

        final PredictionHistory currentPredictionHistory = getItem(position);

        TextView match_name_view = (TextView) listItemView.findViewById(R.id.scores_history_list_item_match_name);
        TextView match_date_view = (TextView) listItemView.findViewById(R.id.scores_history_list_item_match_date);
        TextView batsman1_view = (TextView) listItemView.findViewById(R.id.scores_history_list_item_batsman1);
        TextView batsman1_points_view = (TextView) listItemView.findViewById(R.id.scores_history_list_item_batsman1_points);
        TextView batsman2_view = (TextView) listItemView.findViewById(R.id.scores_history_list_item_batsman2);
        TextView batsman2_points_view = (TextView) listItemView.findViewById(R.id.scores_history_list_item_batsman2_points);
        TextView bowler1_view = (TextView) listItemView.findViewById(R.id.scores_history_list_item_bowler1);
        TextView bowler1_points_view = (TextView) listItemView.findViewById(R.id.scores_history_list_item_bowler1_points);
        TextView bowler2_view = (TextView) listItemView.findViewById(R.id.scores_history_list_item_bowler2);
        TextView bowler2_points_view = (TextView) listItemView.findViewById(R.id.scores_history_list_item_bowler2_points);
        TextView mom1_view = (TextView) listItemView.findViewById(R.id.scores_history_list_item_mom1);
        TextView mom2_view = (TextView) listItemView.findViewById(R.id.scores_history_list_item_mom2);
        TextView mom1_points_view = (TextView) listItemView.findViewById(R.id.scores_history_list_item_mom1_points);
        TextView mom2_points_view = (TextView) listItemView.findViewById(R.id.scores_history_list_item_mom2_points);
        TextView match_winner_view = (TextView) listItemView.findViewById(R.id.scores_history_list_item_match_winner);
        TextView match_winner_points_view = (TextView) listItemView.findViewById(R.id.scores_history_list_item_match_winner_points);
        TextView toss_winner_view = (TextView) listItemView.findViewById(R.id.scores_history_list_item_toss_winner);
        TextView toss_winner_points_view = (TextView) listItemView.findViewById(R.id.scores_history_list_item_toss_winner_points);
        TextView total_points_view = (TextView) listItemView.findViewById(R.id.scores_history_list_item_total_points);
        TextView topped_For_match_view = (TextView) listItemView.findViewById(R.id.scores_history_list_item_top_scored);

        LinearLayout scores_linear_layout = (LinearLayout) listItemView.findViewById(R.id.scores_history_list_item_linear_layout);

        match_name_view.setText(currentPredictionHistory.getMatch_name());
        match_date_view.setText(currentPredictionHistory.getMatch_date());
        batsman1_view.setText(currentPredictionHistory.getBatsman1());
        batsman2_view.setText(currentPredictionHistory.getBatsman2());
        batsman1_points_view.setText(Integer.toString(currentPredictionHistory.getBatsman1_points()));
        batsman2_points_view.setText(Integer.toString(currentPredictionHistory.getBatsman2_points()));
        bowler1_view.setText(currentPredictionHistory.getBowler1());
        bowler2_view.setText(currentPredictionHistory.getBowler2());
        bowler1_points_view.setText(Integer.toString(currentPredictionHistory.getBowler1_points()));
        bowler2_points_view.setText(Integer.toString(currentPredictionHistory.getBowler2_points()));
        mom1_view.setText(currentPredictionHistory.getMom1());
        mom2_view.setText(currentPredictionHistory.getMom2());
        mom1_points_view.setText(Integer.toString(currentPredictionHistory.getMom1_points()));
        mom2_points_view.setText(Integer.toString(currentPredictionHistory.getMom2_points()));
        match_winner_view.setText(currentPredictionHistory.getMatchWinner());
        match_winner_points_view.setText(Integer.toString(currentPredictionHistory.getMatch_winner_points()));
        toss_winner_view.setText(currentPredictionHistory.getTossWinner());
        toss_winner_points_view.setText(Integer.toString(currentPredictionHistory.getToss_wineer_points()));
        total_points_view.setText(Integer.toString(currentPredictionHistory.getTotal_Points()));

        if(currentPredictionHistory.isMatch_topper()){
            topped_For_match_view.setText("Top scorer for this match!");
            scores_linear_layout.setBackgroundColor(Color.RED);
        }else {
            topped_For_match_view.setText("");
            scores_linear_layout.setBackgroundColor(ContextCompat.getColor(context, R.color.secondaryAppColor));
        }

        return listItemView;
    }
}
