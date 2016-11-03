package com.brotherslynn.nianclock;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RemoteViews;

/**
 * ClockChoice screen allows users to choose between widget designs
 *
 * Sue Smith August 2012
 *
 * Mobiletuts+ Building a Customizable Android Analog Clock Widget
 *
 */

public class ClockChoice extends Activity implements OnClickListener {

    //count of designs
    private int numDesigns;
    //image buttons for each design
    private ImageButton[] designBtns;
    //identifiers for each clock element
    private int[] designs;
    //shared preferences
    private SharedPreferences clockPrefs;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clock_choice);

        //find out how many designs we have
        numDesigns = this.getResources().getInteger(R.integer.num_clocks);
        //prepare to hold design buttons
        designBtns = new ImageButton[numDesigns];
        //prepare to hold AnalogClock IDs
        designs = new int[numDesigns];

        //iterate through designs
        for(int d=0; d<numDesigns; d++){
            //get AnalogClock element ID from widget layout
            designs[d] = this.getResources().getIdentifier
                    ("AnalogClock"+d, "id", getPackageName());
            //get buttons in clock_choice layout for user choice
            designBtns[d]=(ImageButton)findViewById(this.getResources().getIdentifier
                    ("design_"+d, "id", getPackageName()));
            //set click listeners for each button
            designBtns[d].setOnClickListener(this);
        }

        //get the application preferences
        clockPrefs = getSharedPreferences("CustomClockPrefs", 0);

    }

    /**
     * onClick handles users selecting designs
     */
    public void onClick(View v) {
        //store which design was picked
        int picked = -1;
        //iterate through designs
        for(int c=0; c<numDesigns; c++){
            //is this the chosen design - check ID against Image Button
            if(v.getId()==designBtns[c].getId()){
                picked=c;
                break;
            }
        }
        //set the chosen analog clock ID
        int pickedClock = designs[picked];

        //get Remote Views for widget
        RemoteViews remoteViews = new RemoteViews
                (this.getApplicationContext().getPackageName(),
                        R.layout.clock_widget_layout);

        //iterate through and set all invisible unless chosen
        for(int d=0; d<designs.length; d++){
            if(d!=pickedClock)
                remoteViews.setViewVisibility(designs[d], View.INVISIBLE);
        }
        //set chosen design visible
        remoteViews.setViewVisibility(pickedClock, View.VISIBLE);

        //get component name for widget class
        ComponentName comp = new ComponentName(this, ClockWidget.class);
        //get AppWidgetManager
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());
        //update
        appWidgetManager.updateAppWidget(comp, remoteViews);

        //update shared preferences with user choice
        SharedPreferences.Editor custClockEdit = clockPrefs.edit();
        custClockEdit.putInt("clockdesign", picked);
        custClockEdit.commit();

        //this Activity is done
        finish();
    }
}