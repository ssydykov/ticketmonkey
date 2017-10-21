package kz.strixit.ticketmonkey;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by saken2316 on 10/21/17.
 */

public class EventsAdapter extends BaseAdapter {

    private List<EventsModule> events;
    private Context context;
    private LayoutInflater inflater;

    public EventsAdapter(List<EventsModule> events, Context context) {
        this.events = events;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;

        if (view == null) {

            view = inflater.inflate(R.layout.row_events, null);
            viewHolder = new ViewHolder();
            viewHolder.titleTextView = (TextView) view.findViewById(R.id.titleTextView);
            viewHolder.dateTextView = (TextView) view.findViewById(R.id.dateTextView);
            viewHolder.priceTextView = (TextView) view.findViewById(R.id.priceTextView);
            viewHolder.coverImageView = (ImageView) view.findViewById(R.id.coverImageView);
            view.setTag(viewHolder);
        }
        else {

            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.titleTextView.setText(events.get(i).getTitle());
        viewHolder.priceTextView.setText(events.get(i).getMin_price());
        viewHolder.dateTextView.setText(events.get(i).getEvent_start());
        Glide
                .with(context)
                .load("http://tktmonkey.kz" + events.get(i).getPortrait_img())
                .into(viewHolder.coverImageView);

        return view;
    }

    private class ViewHolder {

        TextView titleTextView, priceTextView, dateTextView;
        ImageView coverImageView;
    }
}
