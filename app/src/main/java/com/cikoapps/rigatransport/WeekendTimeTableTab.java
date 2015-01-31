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

    View selectedTimeView = null;

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
            baseTimeTextView.setPadding(15, 10, 15, 10);
            baseTimeTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            row.addView(baseTimeTextView);
            while (i < (times.size())) {
                if (times.get(i).getHourOfDay() == baseTime) {

                    TextView minutesTextView = new TextView(getActivity().getApplicationContext());
                    minutesTextView.setPadding(15, 10, 15, 10);
                    minutesTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    String minutes = (times.get(i).getMinutesOfHour() + "").trim();
                    if (minutes.length() < 2) minutes = 0 + minutes;
                    minutesTextView.setText(minutes);

                    minutesTextView.setTag(times.get(i).getId() + "-" + times.get(i).getStopId());
                    minutesTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (selectedTimeView != null) {
                                selectedTimeView.setBackground(null);
                            }
                            selectedTimeView = v;
                            selectedTimeView.setBackgroundColor(getResources().getColor(R.color.yellow_300));

                            String tag = v.getTag() + "";
                            int timeId = Integer.parseInt(tag.split("-")[0]);
                            int stopId = Integer.parseInt(tag.split("-")[1]);

                            DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity().getApplicationContext());
                            int routeId = dataBaseHelper.getRouteIdByStopId(stopId);
                            dataBaseHelper.close();
                            int position = dataBaseHelper.getDiffBetweenFirstAndPivotTimeWeekend(stopId, timeId);
                            dataBaseHelper.close();
                            Cursor cursor = dataBaseHelper.getRouteStops(routeId);
                            String[] drawerTimes = new String[cursor.getCount()];
                            int i = 0;
                            if (cursor.moveToFirst()) {
                                do {
                                    int currStopId = cursor.getInt(cursor.getColumnIndex("_id"));
                                    String currStopName = cursor.getString(cursor.getColumnIndex("name"));
                                    String time = dataBaseHelper.getWeekEndTimeByStopIdAndPosition(currStopId, position);
                                    Log.w(currStopName, time);
                                    drawerTimes[i] = currStopName + " " + time;
                                    i++;
                                } while (cursor.moveToNext());
                            }
                            TimeTableActivity.reloadDrawer(drawerTimes);
                        }
                    });


                    int currDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                    if (currDay == Calendar.SATURDAY || currDay == Calendar.SUNDAY) {
                        if (times.get(i).getDifference() == nextA) {
                            minutesTextView.setBackgroundColor(getResources().getColor(R.color.yellow_700));
                        } else if (times.get(i).getDifference() == nextB) {
                            minutesTextView.setBackgroundColor(getResources().getColor(R.color.yellow_300));
                        } else if (times.get(i).getDifference() == nextC) {
                            minutesTextView.setBackgroundColor(getResources().getColor(R.color.yellow_50));
                        }
                    }

                    row.addView(minutesTextView);
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