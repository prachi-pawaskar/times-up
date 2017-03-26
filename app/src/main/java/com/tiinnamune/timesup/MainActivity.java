package com.tiinnamune.timesup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Change In time label
        changeInTimeLabel();
    }

    public void changeInTimeLabel(){

        //In time logic
        final TextView inTimeText = (TextView) findViewById(R.id.in_time_value);
        final TextView desiredTimeText = (TextView) findViewById(R.id.desired_out_time_value);
        Button inTimeButton = (Button) findViewById(R.id.in_button);

        inTimeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String inTimeValue;
                //Set current time
                inTimeValue = new SimpleDateFormat("hh:mm:ss a").format(new Date());
                //Display the in time
                inTimeText.setText(inTimeValue);
                //Display Desired out time
                desiredTimeText.setText(addWorkingHours(inTimeValue));
            }
        });

        //Actual out time logic
        final TextView outTimeText = (TextView) findViewById(R.id.actual_out_time_value);
        final TextView actualHours = (TextView) findViewById(R.id.total_out_time_value);
        final Button outTimeButton = (Button) findViewById(R.id.out_button);

        outTimeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String outTimeValue;
                //Set current time
                outTimeValue = new SimpleDateFormat("hh:mm:ss a").format(new Date());
                //Display the in time
                outTimeText.setText(outTimeValue);
                //Display actual hours completed
//                actualHours.setText(totalHours(outTimeText.toString()));
                actualHours.setText(totalHours());
            }
        });
    }


    //To show the menu three dot
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //To show the activity in the menu, three dot
        getMenuInflater().inflate(R.menu.activity_main,menu);
        return true;
    }

    //To display monthly activity when monthly menu is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //When user selects Monthly View
        if(item.getItemId() == R.id.monthly_menu){
            setContentView(R.layout.activity_monthly_view);
        }
        //When user selects Settings View
        else if(item.getItemId() == R.id.setting_menu){
            setContentView(R.layout.activity_settings_view);
        }
        return super.onOptionsItemSelected(item);
    }

    //To enable go back to main activity
    @Override
    public void onBackPressed() {
        setContentView(R.layout.activity_main);
    }


    //Method to add hours to out time
    public String addWorkingHours(String inTimeValue){
        String desiredOutTimeValue = null;
        try {
            //Adding 8.5 hours code
            //Set the Formatter
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
            //parse inTimeValue to Date type and the formatter
            Date inDate = null;
            inDate = formatter.parse(inTimeValue);
            //Initiate Calendar to be able to add hours, minutes
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(inDate);
            calendar.add(Calendar.HOUR, 8);
            calendar.add(Calendar.MINUTE, 30);
            //Get the time after timing
            Date outDate = calendar.getTime();
            //convert into the formatter
            desiredOutTimeValue = formatter.format(outDate);
        }catch (Exception e){
            System.out.println("Exception in addWorkingHours: " + e.toString());
        }
        //return string
        return desiredOutTimeValue;
    }

    //Method to calculate actual hours
    public String totalHours(){
        String totalHoursCompleted = null;
        final TextView inTimeSet = (TextView) findViewById(R.id.in_time_value);
        final TextView outTimeSet = (TextView) findViewById(R.id.actual_out_time_value);

        try {
            //Set the Formatter
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
            //parse inTimeValue to Date type and the formatter
            Date inDateSet = null;
            inDateSet = formatter.parse(inTimeSet.getText().toString());
            Date outDateSet = null;
            outDateSet = formatter.parse(outTimeSet.getText().toString());
            long difference = outDateSet.getTime() - inDateSet.getTime();
            long diffSeconds = difference / 1000 % 60;
            long diffMinutes = difference / (60 * 1000) % 60;
            long diffHours = difference / (60 * 60 * 1000) % 24;
            //Display the total hours
            totalHoursCompleted = Long.toString(diffHours)+":"+Long.toString(diffMinutes)+":"+Long.toString(diffSeconds);

        }catch (Exception e){
            System.out.println("Exception in totalHours: " + e.toString());
        }
        //return string
        return totalHoursCompleted;

    }
}
