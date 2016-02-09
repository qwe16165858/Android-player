package com.example.happle.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import objects.Price;
import objects.songs;


public class priceAdapter<S>
        extends BaseAdapter {
 private LayoutInflater mInflater;
 private ListView lv2;
 public ArrayList<Price> set;
 public priceAdapter(Context context, int activity_griditem, ArrayList<Price> set) {
this.mInflater = LayoutInflater.from(context);
this.set = set;


 }

 @Override
 public int getCount() {
  // TODO Auto-generated method stub
  return set.size();
 }

 @Override
 public Object getItem(int arg0) {
  // TODO Auto-generated method stub
  return null;
 }

 @Override
 public long getItemId(int arg0) {
  // TODO Auto-generated method stub
  return 0;
 }

 @Override
 public View getView(int position, View view, ViewGroup parent) {

  ViewHolder holder = null;
  if (view == null) {
   holder = new ViewHolder();
   view = mInflater.inflate(R.layout.activity_pricesitem, null);
   holder.name = (TextView) view.findViewById(R.id.textView);
   holder.price = (TextView) view.findViewById(R.id.textView4);
   holder.id = (TextView) view.findViewById(R.id.textView5);
   holder.state = (TextView)view.findViewById(R.id.textView3);
   view.setTag(holder);

  } else {
   holder = (ViewHolder) view.getTag();
  }
     String p = String.valueOf(set.get(position).price);
     String i =set.get(position).conditionNames;

  holder.name.setText(i);
   holder.price.setText(p);




  return view;
 }

 public final class ViewHolder {
  public TextView id;
  public TextView name;
  public TextView price;
  public ListView lv2;
  public TextView state;
 }
}