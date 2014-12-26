package com.ant.vkgeoclasstest;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

/**
 * Created by apple on 11.12.14.
 */
public class ImageTextExpandableListAdapter extends BaseExpandableListAdapter {

        private Context mContext;
        private ArrayList<Map<String,String>>  mListDataHeader;
        private int mLayoutDataHeader;
        private Map<String, ArrayList<Map<String,String>>> mListDataChild;
        private int mLayoutDataChild;

        private ChildViewHolder childViewHolder;
            private GroupViewHolder groupViewHolder;

        public ImageTextExpandableListAdapter (Context context,
                                       ArrayList<Map<String,String>>  listDataHeader,
                                       int layoutDataHeader,
                                       Map<String, ArrayList<Map<String,String>>> listDataChild,
                                       int layoutDataChild) {

            mContext = context;
            mListDataHeader = listDataHeader;
            mLayoutDataHeader = layoutDataHeader;
            mListDataChild = listDataChild;

            mLayoutDataChild = layoutDataChild;
        }

        @Override
        public int getGroupCount() {
            return mListDataHeader.size();
        }

        @Override
        public Map<String,String> getGroup(int groupPosition) {
            return mListDataHeader.get(groupPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {

            String groupName="";
            for (String key : getGroup(groupPosition).keySet()){ groupName = key;  }
            String groupImage = getGroup(groupPosition).get(groupName);

            if (convertView == null) {

                LayoutInflater inflater = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(mLayoutDataHeader, null);

                groupViewHolder = new GroupViewHolder();


                groupViewHolder.mGroupName = (TextView) convertView
                        .findViewById(R.id.tvGroup);

                groupViewHolder.mGroupImage = (ImageView) convertView
                        .findViewById(R.id.ivGroup);

                convertView.setTag(groupViewHolder);
            } else {

                groupViewHolder = (GroupViewHolder) convertView.getTag();
            }

            if (groupImage != null) {
                //groupViewHolder.mGroupImage.setImageBitmap(groupImage);

                //groupViewHolder.mGroupImage
                  //      .setImageResource(R.drawable.ic_launcher);

            } else {
                //groupViewHolder.mGroupImage
                  //      .setImageResource(R.drawable.ic_launcher);
            }

            groupViewHolder.mGroupName.setText(groupName);

            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            int size=0;
            for (String key : mListDataHeader.get(groupPosition).keySet()) {
                size = mListDataChild.get(key).size();
            }
            return size;
        }

        @Override
        public Map<String, String> getChild(int groupPosition, int childPosition) {
            Map<String, String> map = new HashMap<String,String>();
            for (String key : mListDataHeader.get(groupPosition).keySet()) {
                map = mListDataChild.get(key)
                        .get(childPosition);
            }
            return map;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            String childName = "";
            for (String key : getChild(groupPosition, childPosition).keySet()) {
                childName = key;
            }
            String childImage = getChild(groupPosition, childPosition).get(childName);

            final int mGroupPosition = groupPosition;

            if (convertView == null) {

                LayoutInflater inflater = (LayoutInflater) this.mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(mLayoutDataChild, null);

                childViewHolder = new ChildViewHolder();

                childViewHolder.mChildName = (TextView) convertView
                        .findViewById(R.id.tvItem);

                childViewHolder.mChildImage = (ImageView) convertView
                        .findViewById(R.id.ivItem);

                convertView.setTag(childViewHolder);

            } else {

                childViewHolder = (ChildViewHolder) convertView.getTag();
            }

            childViewHolder.mChildName.setText(childName);

            if (childImage != "") {
                Picasso.with(mContext).load(childImage)
                        //.resize(30,30)
                        .transform(new CircleTransform())
                        .into(childViewHolder.mChildImage);
            } else {
                String url = "http://vkontakte.ru/images/camera_b.gif";
                Picasso.with(mContext).load(url)
                        //.resize(30,30)
                        .transform(new CircleTransform())
                        .into(childViewHolder.mChildImage);
            }

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }


        public class GroupItem {
            String groupName;
            String groupImage;
        }

        public final class GroupViewHolder {

            TextView mGroupName;
            ImageView mGroupImage;
        }

        public final class ChildViewHolder {

            TextView mChildName;
            ImageView mChildImage;
        }


}
