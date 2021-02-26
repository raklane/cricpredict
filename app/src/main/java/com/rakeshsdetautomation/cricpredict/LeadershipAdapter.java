package com.rakeshsdetautomation.cricpredict;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rakeshsdetautomation.cricpredict.users.Participant;

import java.util.ArrayList;

public class LeadershipAdapter extends ArrayAdapter<Participant> {

    Context context;

    public LeadershipAdapter(Activity context, ArrayList<Participant> participants){
        super(context, 0, participants);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.leadership_list_item, parent, false);
        }

        TextView rankView = (TextView) listItemView.findViewById(R.id.rank_text_view);
        TextView nameView = (TextView) listItemView.findViewById(R.id.name_text_view);
        TextView pointsView = (TextView) listItemView.findViewById(R.id.points_text_view);
        TextView toppedView = (TextView) listItemView.findViewById(R.id.topped_text_view);
        TextView playedView = (TextView) listItemView.findViewById(R.id.played_text_view);

        final Participant currentParticipant = getItem(position);

        if(currentParticipant.getParticipantRank() > 0){
            rankView.setText(String.valueOf(currentParticipant.getParticipantRank()));
        }else {
            rankView.setText("--");
        }

        nameView.setText(currentParticipant.getParticipantName());
        pointsView.setText(String.valueOf(currentParticipant.getParticipantScore()));
        toppedView.setText(String.valueOf(currentParticipant.getTopScoredInMatches()));
        playedView.setText(String.valueOf(currentParticipant.getMatchesPlayed()));



        return listItemView;
    }
}
