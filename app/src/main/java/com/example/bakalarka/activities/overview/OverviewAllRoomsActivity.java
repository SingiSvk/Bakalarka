package com.example.bakalarka.activities.overview;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.example.bakalarka.activities.overview.basic.OverviewActivity;
import com.example.bakalarka.activities.overview.basic.OverviewFragment;

public class OverviewAllRoomsActivity extends OverviewActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if(savedInstanceState == null){
            savedInstanceState = new Bundle();
        }
        int roomId = getIntent().getIntExtra("page", 0);
        savedInstanceState.putInt("page", roomId);
        savedInstanceState.putBoolean(OverviewRiskyRoomsActivity.RISK_KEY, false);
        super.onCreate(savedInstanceState);
        updateOverview();
    }

    private void updateOverview(){
        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                handler.postDelayed(this, 2*60*1000);
                if (OverviewActivity.ACTIVE){
                    for (int i = 0; i < allRoomsFragmentPager.mFragments.size()-1; i++){
                        OverviewFragment overviewFragment = (OverviewFragment)allRoomsFragmentPager.getItem(i);
                        overviewFragment.updateFragment();
                    }
                }
            }
        };
        handler.postDelayed(r, 2*60*1000);
    }
}
