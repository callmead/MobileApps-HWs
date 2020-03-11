package edu.utep.cs.cs4330.mypricewatcher2;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ItemListAdapter extends ArrayAdapter<Item> {
    private Context myContext;
    private int myResource;
    private int lastPosition = -1;
    private static DecimalFormat df2 = new DecimalFormat(".##");

    private static class ViewHolder {
        TextView lblItem1;
        TextView lblURL1;
        //TextView lblPrice1;
        TextView lblCP1;
        TextView lblPC1;
    }

    public ItemListAdapter(Context context, int resource, ArrayList<Item> objects){
        super(context, resource, objects);
        myContext = context;
        myResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String itemX = getItem(position).getItemName();
        String urlX = getItem(position).getItemURL();
        //String priceX = getItem(position).getItemPrice();
        String currentPriceX = df2.format(Double.parseDouble(getItem(position).getItemNewPrice()));
        String priceChangeX = getItem(position).getItemChange();

        //create an item object
        //Item item00 = new Item(itemX, urlX, priceX, currentPriceX, priceChangeX);
        Item item00 = new Item(itemX, urlX, currentPriceX, priceChangeX);

        //create the view result for showing the animation
        final View result;

        //ViewHolder object
        ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(myContext);
            convertView = inflater.inflate(myResource, parent, false);
            holder= new ViewHolder();

            holder.lblItem1 = (TextView) convertView.findViewById(R.id.lblItem1);
            holder.lblURL1 = (TextView) convertView.findViewById(R.id.lblURL1);
            //holder.lblPrice1 = (TextView) convertView.findViewById(R.id.lblPrice1);
            holder.lblCP1 = (TextView) convertView.findViewById(R.id.lblCP1);
            holder.lblPC1 = (TextView) convertView.findViewById(R.id.lblPC1);

            result = convertView;

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        lastPosition = position;

        holder.lblItem1.setText(itemX);
        holder.lblURL1.setText(urlX);
        //holder.lblPrice1.setText(priceX);
        holder.lblCP1.setText(currentPriceX);
        holder.lblPC1.setText(priceChangeX);

        return convertView;
    }
}