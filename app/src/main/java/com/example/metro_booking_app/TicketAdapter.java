package com.example.metro_booking_app;

import android.content.Context;
import android.view.*;
import android.widget.*;
import java.util.List;

public class TicketAdapter extends ArrayAdapter<Ticket> {
    public TicketAdapter(Context context, List<Ticket> tickets) {
        super(context, 0, tickets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Ticket ticket = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ticket_item, parent, false);
        }

        TextView txtType = convertView.findViewById(R.id.txtType);
        TextView txtDate = convertView.findViewById(R.id.txtDate);
        TextView txtPrice = convertView.findViewById(R.id.txtPrice);

        txtType.setText(ticket.getTicketType());
        txtDate.setText("Từ: " + ticket.getStart() + " → " + ticket.getEnd());
        txtPrice.setText(String.format("Giá: %.0f đ", ticket.getPrice()));

        return convertView;
    }
}
