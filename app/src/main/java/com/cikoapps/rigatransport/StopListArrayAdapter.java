package com.cikoapps.rigatransport;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Creation date 1/28/2015
 * -------------------------
 * Modified 2/1/2015 by Arvis code formatting
 */
class StopListArrayAdapter extends ArrayAdapter<Stop> {
    private Context myContext;
    private String callingActivity = "";
    Typeface font;

    public StopListArrayAdapter(Context context, ArrayList<Stop> values, String callingActivity) {
        super(context, R.layout.stop_row_layout, values);
        myContext = context;
        this.callingActivity = callingActivity;
        this.font = Typeface.createFromAsset(myContext.getAssets(), "NotoSerif-Regular.ttf");
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        final LayoutInflater theInflater = LayoutInflater.from(getContext());

        final Stop stop = getItem(position);

        View theView = theInflater.inflate(R.layout.stop_row_layout, parent, false);

        ImageView stopImageView = (ImageView) theView.findViewById(R.id.stop_image);
        TextView stopNameView = (TextView) theView.findViewById(R.id.stop_name);
        ImageView stopMapImageView = (ImageView) theView.findViewById(R.id.stop_map_image);


        stopNameView.setTypeface(font);
        stopNameView.setText(stop.getName());
        stopMapImageView.setImageResource(R.drawable.map);
        stopMapImageView.setTag(position);
        int count = getCount();


        if (callingActivity.equalsIgnoreCase("routeList")) {
            if (position == 0) {
                stopImageView.setImageResource(R.drawable.start);
            } else if (position == count - 1) {
                stopImageView.setImageResource(R.drawable.finish);
            } else {
                stopImageView.setImageResource(R.drawable.middle);
            }
        } else if (callingActivity.equalsIgnoreCase("favorites")) {
            Bitmap bm = BitmapFactory.decodeResource(myContext.getResources(), R.drawable.untitled);
            Bitmap.Config config = bm.getConfig();
            Bitmap newImage = Bitmap.createBitmap(100, 100, config);
            Canvas c = new Canvas(newImage);
            c.drawBitmap(bm, 100, 100, null);

            DataBaseHelper dataBaseHelper = new DataBaseHelper(myContext);
            int[] routeInfo = dataBaseHelper.getRouteIntAndTypeByRouteId(stop.getRouteId());
            dataBaseHelper.close();

            stop.getRouteId();
            Paint paint = new Paint();
            paint.setColor(myContext.getResources().getColor(R.color.grey_700));
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(70);
            c.drawText(routeInfo[1] + "", 15, 80, paint);
            stopImageView.setImageBitmap(newImage);
        }
        theView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TimeTableActivity.class);
                intent.putExtra("stop_id", stop.getId());
                intent.putExtra("route_id", stop.getRouteId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });

        return theView;
    }

}
