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

import com.rakeshsdetautomation.cricpredict.users.MilestoneListItemClass;
import com.rakeshsdetautomation.cricpredict.users.Participant;

import java.util.ArrayList;

public class MilestonesAdapter extends ArrayAdapter<MilestoneListItemClass> {

    Context context;

    public MilestonesAdapter(Activity context, ArrayList<MilestoneListItemClass> milestoneListItems){
        super(context, 0, milestoneListItems);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.milestones_list_item, parent, false);
        }

        TextView categoryView = (TextView) listItemView.findViewById(R.id.category_milestones_text_view);
        TextView nameView = (TextView) listItemView.findViewById(R.id.participant_milestones_text_view);
        TextView pointsView = (TextView) listItemView.findViewById(R.id.points_milestones_text_view);

        final MilestoneListItemClass currentMilestoneListItem = getItem(position);

        if(currentMilestoneListItem.getName() != null){
            categoryView.setText(currentMilestoneListItem.getCategory());
            nameView.setText(currentMilestoneListItem.getName());
            pointsView.setText(String.valueOf(currentMilestoneListItem.getPoints()));
        }

        return listItemView;
    }

}
