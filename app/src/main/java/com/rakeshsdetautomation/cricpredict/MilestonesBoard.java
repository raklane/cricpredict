package com.rakeshsdetautomation.cricpredict;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.rakeshsdetautomation.cricpredict.constants.BaseClass;
import com.rakeshsdetautomation.cricpredict.loginandregistration.HomeScreenActivity;
import com.rakeshsdetautomation.cricpredict.users.MilestoneListItemClass;
import com.rakeshsdetautomation.cricpredict.users.Participant;
import com.rakeshsdetautomation.prediction_history.PredictionHistoryActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MilestonesBoard extends BaseActivity  implements View.OnClickListener {

    final String TAG = "MilestonesBoardActivity";
    ArrayList<MilestoneListItemClass> milestoneListItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milestones_board);

        MilestonesBoardAsyncTaskRunner milestonesBoardAsyncTaskRunner = new MilestonesBoardAsyncTaskRunner();
        milestonesBoardAsyncTaskRunner.execute();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.banner:
                startActivity(new Intent(this, HomeScreenActivity.class));
                break;
        }
    }

    private class MilestonesBoardAsyncTaskRunner extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MilestonesBoard.this, "Loading", "Loading information");
        }

        @Override
        protected String doInBackground(String... strings) {
            createMilestonesBoard();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            fillUpMilestonesListView();
            progressDialog.dismiss();
        }
    }

    private void createMilestonesBoard() {

        //final ArrayList<MilestoneListItemClass> milestoneListItems = new ArrayList<MilestoneListItemClass>();
        milestoneListItems = new ArrayList<MilestoneListItemClass>();

        try {
            JSONArray jsonArray = callMilestoneBoardApi();
            for(int i=0; i<jsonArray.length(); i++){
                MilestoneListItemClass milestonesItem = new MilestoneListItemClass();
                milestonesItem.setCategory(((JSONObject) jsonArray.get(i)).getString("category"));
                milestonesItem.setName(((JSONObject) jsonArray.get(i)).getString("name"));
                milestonesItem.setUserId(((JSONObject) jsonArray.get(i)).getString("userId"));
                milestonesItem.setPoints(((JSONObject) jsonArray.get(i)).getInt("points"));
                milestoneListItems.add(milestonesItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Server Error", Toast.LENGTH_SHORT);
        }

    }

    public void fillUpMilestonesListView(){
        MilestonesAdapter milestonesAdapter = new MilestonesAdapter(this, milestoneListItems);
        ListView listView = (ListView) findViewById(R.id.milestones_list_view);
        listView.setAdapter(milestonesAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MilestoneListItemClass milestoneListItem = milestoneListItems.get(position);
                String userId = milestoneListItem.getUserId();

                if(userId != "null"){
                    Intent predictionHistoryActivityIntent = new Intent(MilestonesBoard.this, PredictionHistoryActivity.class);
                    predictionHistoryActivityIntent.putExtra("userId", userId);
                    startActivity(predictionHistoryActivityIntent);
                }

            }
        });
    }


    private JSONArray callMilestoneBoardApi(){

        try {
            String milestonesBoardString = BaseClass.getCall(BaseClass.serviceUrl + BaseClass.resourceMilestones);
            JSONObject milestonesBoardJsonObj = new JSONObject(milestonesBoardString);
            JSONArray jsonArray = milestonesBoardJsonObj.getJSONArray("milestonesInBoard");
            return jsonArray;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout_link:
                logout();
                break;
            case R.id.milestones:
                milestones();
                break;
            case R.id.settings:
                //write code for settings
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cricpredict_menu_items, menu);
        return super.onCreateOptionsMenu(menu);
    }

}