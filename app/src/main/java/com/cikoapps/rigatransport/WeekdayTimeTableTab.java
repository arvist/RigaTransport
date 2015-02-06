package com.cikoapps.rigatransport;

import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
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
 * Creation date 1/30/2015
 * -------------------------
 * Modified 2/1/2015 by Arvis code formatting
 */
public class WeekdayTimeTableTab extends Fragment {
    private int stopId = -1;
    private ArrayList<Time> times = new ArrayList<>();

    private int nextA = 10000;
    private int nextB = 10000;
    private int nextC = 10000;


    private Typeface regularFont = null;
    private Typeface italicFont = null;
    private View selectedTimeView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        regularFont = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(), "NotoSerif-Regular.ttf");
        italicFont = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(), "NotoSerif-Italic.ttf");
        Bundle stopBundle = getActivity().getIntent().getExtras();
        if (stopBundle != null) {
            stopId = stopBundle.getInt("stop_id");
        }
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity().getApplicationContext());
        Cursor cursor = dataBaseHelper.getWeekDayTimesByStopId(stopId);
        if (cursor.moveToFirst()) {
            do {
                String time = cursor.getString(cursor.getColumnIndex("time"));
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                int stop_id = cursor.getInt(cursor.getColumnIndex("stop_id"));
                boolean isStandart = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("standartTime")));

                Time timeObj = new Time(id, time, stop_id, isStandart);

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
        dataBaseHelper.close();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.weekday_time_table_layout, container, false);
        TableLayout table = (TableLayout) view.findViewById(R.id.WeekdayTable);

          /*
                Creates table of all times and adds it to view
         */
        if (times.size() > 0) {
            for (int i = 0; i < times.size() + 1; i++) {
                if (i > 0) i -= 1;
                TableRow row = new TableRow(getActivity().getApplicationContext());
                int baseTime = times.get(i).getHourOfDay();
                TextView baseTimeTextView = new TextView(getActivity().getApplicationContext());
                baseTimeTextView.setText(baseTime + "");
                baseTimeTextView.setPadding(15, 10, 15, 10);
                baseTimeTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
                row.addView(baseTimeTextView);

                while (i < (times.size())) {

                    final Time currTime = times.get(i);
                    if (currTime.getHourOfDay() == baseTime) {

                        TextView minutesTextView = new TextView(getActivity().getApplicationContext());
                        minutesTextView.setPadding(15, 10, 15, 10);
                        minutesTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

                        String minutes = (currTime.getMinutesOfHour() + "").trim();

                        if (minutes.length() < 2) minutes = 0 + minutes;
                        minutesTextView.setText(minutes);

                        minutesTextView.setTag(currTime.getId() + "-" + currTime.getStopId() + "-" + currTime.getIsStandart());
                        if (!currTime.getIsStandart()) {
                            minutesTextView.setTypeface(italicFont);
                            minutesTextView.setTextColor(getResources().getColor(R.color.red_400));
                        } else {
                            minutesTextView.setTypeface(regularFont);
                        }

                        minutesTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (selectedTimeView != null) {
                                    selectedTimeView.setBackground(null);
                                }

                                selectedTimeView = v;
                                selectedTimeView.setBackgroundColor(getResources().getColor(R.color.yellow_300));
                                TimeTableActivity.showLoadingWidget();
                                (new RouteTimes(currTime)).execute();
                            }


                        });
                    /*
                        Colors three next route times
                        TODO Make better design
                     */
                        int currDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                        if (currDay != Calendar.SATURDAY && currDay != Calendar.SUNDAY) {
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
        } else {

            TableRow row = new TableRow(getActivity().getApplicationContext());
            TextView emptyTableTextView = new TextView(getActivity().getApplicationContext());
            emptyTableTextView.setText("Nothing to show");
            emptyTableTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            emptyTableTextView.setHeight(800);
            emptyTableTextView.setWidth(600);
            emptyTableTextView.setPadding(100, 10, 10, 10);
            emptyTableTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            emptyTableTextView.setGravity(Gravity.CENTER_VERTICAL);
            row.addView(emptyTableTextView);
            table.addView(row, new TableLayout.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        }

        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        selectedTimeView.setBackground(null);
        super.onHiddenChanged(hidden);
    }

    /*
                    Returns difference in minutes between current time and given time
             */
    private int getDiff(Calendar calendar, Calendar calendarNow) {
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int hourOfDayNow = calendarNow.get(Calendar.HOUR_OF_DAY);
        int minuteOfHour = calendar.get(Calendar.MINUTE);
        int minuteOfHourNow = calendarNow.get(Calendar.MINUTE);
        int diffHour = hourOfDay - hourOfDayNow;
        int diffMinutes = minuteOfHour - minuteOfHourNow;
        return (diffHour * 60 + diffMinutes);
    }

    private class RouteTimes extends AsyncTask<String, Void, String> {
        String[] drawerTimes;
        String[] drawerTimesAll;
        boolean standart;
        int minutesPressed;

        public RouteTimes(Time time) {
            this.minutesPressed = time.getMinutesOfHour() + 60 * time.getHourOfDay();
        }

        @Override
        protected String doInBackground(String... params) {
            String tag = selectedTimeView.getTag() + "";
            int timeId = Integer.parseInt(tag.split("-")[0]);
            int stopId = Integer.parseInt(tag.split("-")[1]);
            standart = Boolean.parseBoolean(tag.split("-")[2]);

            DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity().getApplicationContext());
            int routeId = dataBaseHelper.getRouteIdByStopId(stopId);
            dataBaseHelper.close();

            if (standart) {
                int num = dataBaseHelper.getStandartTimeNumWeekDay(timeId, stopId);
                dataBaseHelper.close();
                ArrayList<String> stopArrayList = new ArrayList<>();
                int count = 0;
                int i = 0;
                Cursor cursor = dataBaseHelper.getWeekDayStandartTimeByStopAndPosition(routeId);
                if (cursor.moveToFirst()) {
                    int id = cursor.getInt(cursor.getColumnIndex("stop_id"));
                    do {
                        int id2 = cursor.getInt(cursor.getColumnIndex("stop_id"));
                        if (id == id2) count++;
                        else break;
                    } while (cursor.moveToNext());
                }
                cursor.moveToFirst();
                if (cursor.moveToPosition(num)) {
                    do {
                        String currStopName = cursor.getString(cursor.getColumnIndex("stop_name"));
                        String time = cursor.getString(cursor.getColumnIndex("time"));
                        stopArrayList.add(currStopName + " " + time);
                        i++;
                    } while (cursor.moveToPosition(i * count + num));
                }
                dataBaseHelper.close();
                drawerTimes = new String[stopArrayList.size()];
                drawerTimes = stopArrayList.toArray(drawerTimes);

            } else {
                int num = dataBaseHelper.getNonStandartTimeNumWeekDay(timeId, stopId);
                dataBaseHelper.close();
                ArrayList<String> stopArrayList = new ArrayList<>();
                int count = 0;
                int i = 0;
                Cursor cursor = dataBaseHelper.getWeekDayNonStandartTimeByStopAndPosition(routeId);
                if (cursor.moveToFirst()) {
                    int id = cursor.getInt(cursor.getColumnIndex("stop_id"));
                    do {
                        int id2 = cursor.getInt(cursor.getColumnIndex("stop_id"));
                        if (id == id2) count++;
                        else break;
                    } while (cursor.moveToNext());
                }
                if (cursor.moveToPosition(num)) {
                    do {
                        String currStopName = cursor.getString(cursor.getColumnIndex("stop_name"));
                        String time = cursor.getString(cursor.getColumnIndex("time"));
                        if (time.length() < 2 || time.length() > 6) {
                            stopArrayList.add("Empty");
                        } else {
                            stopArrayList.add(currStopName + " " + time);
                        }
                        i++;
                    } while (cursor.moveToPosition(i * count + num));
                }
                dataBaseHelper.close();
                ArrayList<String> drawerTimesArrayList = new ArrayList<>();
                for (int k = 0; k < stopArrayList.size(); k++) {

                    if (stopArrayList.get(k).equalsIgnoreCase("Empty")) {
                    } else {
                        int hour = Integer.parseInt((stopArrayList.get(k).substring(stopArrayList.get(k).length() - 5, stopArrayList.get(k).length() - 3)).trim());
                        int minutes = Integer.parseInt(stopArrayList.get(k).substring(stopArrayList.get(k).length() - 2, stopArrayList.get(k).length()));
                        int stopTime = minutes + 60 * hour;
                        if ((stopTime - minutesPressed) < 120) {
                            if (stopTime - minutesPressed > -120)
                                drawerTimesArrayList.add(stopArrayList.get(k));
                        }
                    }
                }
                drawerTimes = new String[drawerTimesArrayList.size()];
                drawerTimes = drawerTimesArrayList.toArray(drawerTimes);
            }

            return "Async Task Complete";
        }

        @Override
        protected void onPostExecute(String result) {
            TimeTableActivity.reloadDrawer(drawerTimes);
            TimeTableActivity.closeLoadingWidget();
        }
    }
}

