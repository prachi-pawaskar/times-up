package com.tiinnamune.timesup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set current date on date label
        final TextView currentDate = (TextView) findViewById(R.id.date_value);
        String date;
        //Set current time
        date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        currentDate.setText(date);

        //Change In time label
        changeInTimeLabel();

        try {
            //Creating table
            db = openOrCreateDatabase("TimeDB", Context.MODE_PRIVATE, null);
            //Day,month,year,in time,desired ou time,actual out time,total time
            db.execSQL("CREATE TABLE IF NOT EXISTS time(day VARCHAR,month VARCHAR,year VARCHAR," +
                    "in_time VARCHAR, desired_out_time VARCHAR, actual_out_time VARCHAR, total_time VARCHAR);");
        }catch (Exception e){
            System.out.println("ERROR: " + e.toString());
        }
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

        //Save time
        final Button saveButton = (Button) findViewById(R.id.save_button);

        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String day = new SimpleDateFormat("dd").format(new Date());
                String month = new SimpleDateFormat("MM").format(new Date());
                String year = new SimpleDateFormat("yyyy").format(new Date());

                System.out.println("day: " + day);
                System.out.println("month: " + month);
                System.out.println("year: " + year);
                System.out.println("inTimeText: " + inTimeText.getText());
                System.out.println("desiredTimeText: " + desiredTimeText.getText());
                System.out.println("outTimeText: " + outTimeText.getText());
                System.out.println("actualHours: " + actualHours.getText());

                try {
                    //Check if update already exist
                    Cursor c1 = db.rawQuery("SELECT count("+day+") FROM time WHERE day='"+day+"'",null);
                    if(c1.moveToFirst()) {
                        String dateEtryFound = c1.getString(0);
                        System.out.println("dateEtryFound SAVE button: " + dateEtryFound);
                        if (Integer.parseInt (dateEtryFound) > 0) {
                            showMessage("ERROR", "Record already exist");
                        } else {
                            //Insert the entry
                            db.execSQL("INSERT INTO time VALUES('" + day + "','" + month + "','" + year + "','" + inTimeText.getText() + "','" +
                                    desiredTimeText.getText() + "','" + outTimeText.getText() + "','" + actualHours.getText() + "');");

                            Cursor c2 = db.rawQuery("SELECT count(" + day + ") FROM time WHERE day='" + day + "'", null);
                            if (c2.moveToFirst()) {
                                String entryFound = c2.getString(0);
                                if (entryFound.isEmpty()) {
                                    showMessage("ERROR", "Record not added");
                                } else {
                                    showMessage("SUCCESS", "Record added");
                                }

                            } else {
                                showMessage("Error", "Invalid data");
                            }
                        }
                    }
                    else {
                        showMessage("Error", "Invalid data");
                    }
                }catch (Exception e){
                    System.out.println("ERROR: " + e.toString());
                }


            }
        });

        //Update time
        final Button updateButton = (Button) findViewById(R.id.update_button);

        updateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String day = new SimpleDateFormat("dd").format(new Date());
                String month = new SimpleDateFormat("MM").format(new Date());
                String year = new SimpleDateFormat("yyyy").format(new Date());

                System.out.println("day: " + day);
                System.out.println("month: " + month);
                System.out.println("year: " + year);
                System.out.println("inTimeText: " + inTimeText.getText());
                System.out.println("desiredTimeText: " + desiredTimeText.getText());
                System.out.println("outTimeText: " + outTimeText.getText());
                System.out.println("actualHours: " + actualHours.getText());

                try {
                    //Check if update already exist
                    Cursor c1 = db.rawQuery("SELECT count("+day+") FROM time WHERE day='"+day+"'",null);
                    if(c1.moveToFirst()) {
                        String dateEtryFound = c1.getString(0);
                        System.out.println("dateEtryFound UPDATE button: " + dateEtryFound);


//                        if (dateEtryFound != day) {
//                            showMessage("ERROR", "Record do not exist");
//                        } else {
                        if(Integer.parseInt(dateEtryFound) == 1 ){
                            //update the entry
                            db.execSQL("UPDATE time SET day='" + day + "',month='" + month + "',year='" + year + "',in_time='" + inTimeText.getText() + "',desired_out_time='" +
                                    desiredTimeText.getText() + "',actual_out_time='" + outTimeText.getText() + "',total_time='" + actualHours.getText() + "' WHERE day='" + day+"'");
                            showMessage("Success", "Record updated");
                        }
                        else {
                            showMessage("ERROR", "Record do not exist");
                        }
                    }
                    else {
                        showMessage("Error", "Invalid data");
                    }
                }catch (Exception e){
                    System.out.println("ERROR: " + e.toString());
                }


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
        //Currently do nothing
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

    public void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
