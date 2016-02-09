package com.example.happle.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import objects.songs;

public class Adapter<S>
        extends BaseAdapter {
 private LayoutInflater mInflater;
 private ListView lv2;
 private LinearLayout rrr;
 public ArrayList<songs> cart;
 private ArrayList<String> mCheckedStates = null;
 public Adapter(Context context, int activity_griditem, ArrayList<songs> cart) {
this.mInflater = LayoutInflater.from(context);
this.cart = cart;


 }

 @Override
 public int getCount() {
  // TODO Auto-generated method stub
  return cart.size();
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
 public View getView(int position, View convertView , ViewGroup parent) {
  convertView = null;
  ViewHolder holder = null;
  if (convertView  == null) {
   holder = new ViewHolder();

   convertView  = mInflater.inflate(R.layout.activity_griditem, null);
   holder.name = (TextView) convertView .findViewById(R.id.textView);
   holder.rrr = (LinearLayout) convertView. findViewById(R.id.rrr);
   holder.price = (TextView) convertView .findViewById(R.id.textView4);
   holder.id = (TextView) convertView .findViewById(R.id.textView5);
   holder.state = (TextView)convertView .findViewById(R.id.textView3);
   convertView .setTag(holder);

  } else {
   holder = (ViewHolder) convertView .getTag();
  }
//  holder.state.setText(mCheckedStates.get(position));

//  holder.state.setText(mCheckedStates.get(position));
   mCheckedStates = new ArrayList<String>();
  String item = String.valueOf(cart.get(position).id);
  if(mCheckedStates.contains(item)){
   holder.rrr.setBackgroundColor(Color.parseColor("#FF6625"));
  }else {
   holder.rrr.setBackgroundColor(Color.TRANSPARENT);
  }




     String p = "$"+cart.get(position).priceList.get(0).price+"";
     String i =cart.get(position).album;
  if(cart.get(position).state==true){
   holder.state.setText("Purchased");
  }
  holder.name.setText(cart.get(position).name);
   holder.price.setText(p);
  holder.id.setText(i);
  //给每一个列表后面的按钮添加响应事件


  return convertView ;
 }

 public final class ViewHolder {
  public TextView id;
  public TextView name;
  public TextView price;
  public ListView lv2;
  public TextView state;
  public  LinearLayout rrr;
 }
}