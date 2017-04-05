package com.kirilv.android.splitme;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.kirilv.android.splitme.model.Result;
import com.kirilv.android.splitme.model.Section;
import com.kirilv.android.splitme.model.User;

public class SummaryActivity extends BaseActivity {

	private ExpandableListView expListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_summary);
		setTitle(getResources().getString(R.string.lbl_title_summary).toString());
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		ApplicationHolder.getInstance().setCompletedSteps(3);
		
		SparseArray<Result> groupList = new SparseArray<Result>();
		SparseArray<SparseArray<Result>> summaryCollection = new SparseArray<SparseArray<Result>>();		
		SparseArray<Section> sectionMap = ApplicationHolder.getInstance().getSectionsMap();			
		
		final SparseArray<Result> totalMap = new SparseArray<Result>();
		float toatlAmount = 0.0f;
		final int sectionsSize = sectionMap.size();
		for (int i = 0; i < sectionsSize; i++) {
			Section section = sectionMap.get(i);
			final float sectionAmount = section.getAmount();	
			toatlAmount += sectionAmount;
			List<User> list = section.getUserList();
			for(User u : list) {
				final float amount = CalculationEngine.getPrice(u.getPercentage(), sectionAmount);
				Result res = totalMap.get(u.getIndex());
				if(res != null) {
					res.setAmount(res.getAmount() + amount);
				} else {
					res = new Result();
					res.setIndex(u.getIndex());
					res.setAmount(amount);
					res.setName(u.getName());
				}
				totalMap.append(u.getIndex(), res);
			}
		}
		
		// Add the Total section first
		groupList.put(0, new Result(getResources().getString(R.string.lbl_total), toatlAmount, 0));
		summaryCollection.put(0, totalMap);
		// Add the rest of the sections
		for (int i = 0; i < sectionMap.size(); i++) {
			Section section = sectionMap.get(i);
			int position = i + 1;
			groupList.put(position, new Result(section.getName(), section.getAmount(), position));
			
			List<User> list = section.getUserList();
			SparseArray<Result> sa = new SparseArray<Result>();
			for (int j = 0; j < list.size(); j++) {
				User u = list.get(j);
				sa.put(j, new Result(u.getName(), Float.valueOf(CalculationEngine.getPrice(u.getPercentage(), section.getAmount())), j));
			}
			summaryCollection.put(position, sa);
		}
		
		expListView = (ExpandableListView) findViewById(R.id.activity_summary_listview);
        final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(this, groupList, summaryCollection);
        expListView.setAdapter(expListAdapter);
        expListView.expandGroup(0);		
        expListView.setOnTouchListener(swipeDetector);
    }

	@Override
	public void getNextActivity() {
		Intent intent = new Intent(SummaryActivity.this, ReportActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}	
    
	private class ExpandableListAdapter extends BaseExpandableListAdapter {

		private Context context;
		private SparseArray<Result> group;
	    private SparseArray<SparseArray<Result>> summaryCollection;	    

		public ExpandableListAdapter(Context context, SparseArray<Result> group, SparseArray<SparseArray<Result>> summaryCollection) {
			this.context = context;
			this.summaryCollection = summaryCollection;
			this.group = group;
		}
		
		public Object getChild(int groupPosition, int childPosition) {
	        return summaryCollection.get(groupPosition).get(childPosition);
	    }
		
		public long getChildId(int groupPosition, int childPosition) {
	        return childPosition;
	    }
		
		public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
	        Result childResult = (Result) getChild(groupPosition, childPosition);
	        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 
	        if (convertView == null) {
	            convertView = inflater.inflate(R.layout.row_summary_child, null);
	        }
	 
	        TextView name = (TextView) convertView.findViewById(R.id.row_summary_child_name);	 
	        name.setText(childResult.getName());
	        
	        TextView amount = (TextView) convertView.findViewById(R.id.row_summary_child_amount);	 
	        amount.setText(CalculationEngine.getFormatterObject(childResult.getAmount()));
	        
	        return convertView;
	    }
	 
	    public int getChildrenCount(int groupPosition) {
	        return summaryCollection.get(groupPosition).size();
	    }
	 
	    public Object getGroup(int groupPosition) {
	        return group.get(groupPosition);
	    }
	 
	    public int getGroupCount() {
	        return group.size();
	    }
	 
	    public long getGroupId(int groupPosition) { 
	        return groupPosition;
	    }
	 
	    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup group) {
	        Result groupResult = (Result) getGroup(groupPosition);
	        if (convertView == null) {
	            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView = infalInflater.inflate(R.layout.row_summary_group, null);
	        }
	        
	        TextView name = (TextView) convertView.findViewById(R.id.row_summary_group_name);
			name.setText(groupResult.getName());
			
			TextView amount = (TextView) convertView.findViewById(R.id.row_summary_group_amount);
			amount.setText(CalculationEngine.getFormatterObject(groupResult.getAmount()));
	        
	        return convertView;
	    }
	 
	    public boolean hasStableIds() {
	        return true;
	    }
	 
	    public boolean isChildSelectable(int groupPosition, int childPosition) {
	        return true;
	    }
	}
}
