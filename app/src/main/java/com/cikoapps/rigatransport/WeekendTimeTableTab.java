package com.cikoapps.rigatransport;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by arvis.taurenis on 1/30/2015.
 */
public class WeekendTimeTableTab extends Fragment {
    int tableRowCount = 0;
    int stopId = -1;
    int direction = -1;
    ArrayList<Time> times = new ArrayList<Time>();


    int nextA = 10000;
    int nextB = 10000;
    int nextC = 10000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle stopBundle = getActivity().getIntent().getExtras();
        if (stopBundle != null) {
            stopId = stopBundle.getInt("stop_id");
            direction = stopBundle.getInt("dir");
        }
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity().getApplicationContext());
        Cursor cursor = dataBaseHelper.getWeekEndTimesByStopId(stopId);
        if (cursor.moveToFirst()) {
            do {
                String time = cursor.getString(cursor.getColumnIndex("time"));
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                int stop_id = cursor.getInt(cursor.getColumnIndex("stopid"));
                Time timeObj = new Time(id, time, stop_id);

                Calendar calendar = Calendar.getInstance();
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), timeObj.getHourOfDay(), timeObj.getMinutesOfHour());
                Calendar calendarNow = Calendar.getInstance();
                timeObj.setDifference(getDiff(calendar, calendarNow));
                times.add(timeObj);


                if (getDiff(calendar, calendarNow) > 0) {
                    if (getDiff(calendar, calendarNow) < nextA) {
                        int temp = nextA;
                        int temp2 = nextB;
                        nextA = getDiff(calendar, calendarNow);
                        nextB = temp;
                        nextC = temp2;
                    } else if (getDiff(calendar, calendarNow) < nextB) {
                        int temp = nextB;
                        nextB = getDiff(calendar, calendarNow);
                        nextC = temp;
                    } else if (getDiff(calendar, calendarNow) < nextC) {
                        nextC = getDiff(calendar, calendarNow);
                    }
                }


            } while (cursor.moveToNext());

            Log.w("NEXT TRHEE DIFFS", nextA + " -> " + nextB + " -> " + nextC);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.weekend_time_table_layout, container, false);
        TableLayout table = (TableLayout) view.findViewById(R.id.WeekendTable);

        for (int i = 0; i < times.size() + 1; i++) {
            if (i > 0) i -= 1;
            TableRow row = new TableRow(getActivity().getApplicationContext());


            int baseTime = times.get(i).getHourOfDay();
            TextView baseTimeTextView = new TextView(getActivity().getApplicationContext());
            baseTimeTextView.setText(baseTime + "");
            baseTimeTextView.setPadding(10, 10, 10, 10);
            baseTimeTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
            row.addView(baseTimeTextView);
            while (i < (times.size())) {
                if (times.get(i).getHourOfDay() == baseTime) {


                    TextView t = new TextView(getActivity().getApplicationContext());

                    t.setPadding(10, 0, 10, 0);

                    t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    t.setText(times.get(i).getMinutesOfHour() + "");

                    int currDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                    if (currDay == Calendar.SATURDAY || currDay == Calendar.SUNDAY) {
                        if (times.get(i).getDifference() == nextA) {
                            t.setBackgroundColor(getResources().getColor(R.color.yellow_700));
                        } else if (times.get(i).getDifference() == nextB) {
                            t.setBackgroundColor(getResources().getColor(R.color.yellow_300));
                        } else if (times.get(i).getDifference() == nextC) {
                            t.setBackgroundColor(getResources().getColor(R.color.yellow_50));
                        }
                    }

                    row.addView(t);
                    i++;
                } else break;
            }
            table.addView(row, new TableLayout.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        }

        return view;

    }

    private int getDiff(Calendar calendar, Calendar calendarNow) {
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int hourOfDayNow = calendarNow.get(Calendar.HOUR_OF_DAY);
        int minuteOfHour = calendar.get(Calendar.MINUTE);
        int minuteOfHourNow = calendarNow.get(Calendar.MINUTE);
        int diffHour = hourOfDay - hourOfDayNow;
        int diffMinutes = minuteOfHour - minuteOfHourNow;
        return (diffHour * 60 + diffMinutes);

    }


}