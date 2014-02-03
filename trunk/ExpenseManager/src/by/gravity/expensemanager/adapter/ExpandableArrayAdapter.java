/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package by.gravity.expensemanager.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.model.ExpandableModel;
import by.gravity.expensemanager.view.expandingcells.ExpandingLayout;

/**
 * This is a custom array adapter used to populate the listview whose items will
 * expand to display extra content in addition to the default display.
 */
public abstract class ExpandableArrayAdapter<T extends ExpandableModel> extends ArrayAdapter<T> {

	private List<T> mData;
	private int mLayoutViewResourceId;

	public ExpandableArrayAdapter(Context context, int layoutViewResourceId, List<T> data) {
		super(context, layoutViewResourceId, data);
		mData = data;
		mLayoutViewResourceId = layoutViewResourceId;
	}

	/**
	 * Populates the item in the listview cell with the appropriate data. This
	 * method sets the thumbnail image, the title and the extra text. This
	 * method also updates the layout parameters of the item's view so that the
	 * image and title are centered in the bounds of the collapsed view, and
	 * such that the extra text is not displayed in the collapsed state of the
	 * cell.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final T object = mData.get(position);

		if (convertView == null) {
			LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
			convertView = inflater.inflate(mLayoutViewResourceId, parent, false);
		}

		LinearLayout linearLayout = (LinearLayout) (convertView.findViewById(R.id.collapsed_layout));
		LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 100);
		linearLayout.setLayoutParams(linearLayoutParams);

		convertView = initView(convertView, object);

		ExpandingLayout expandingLayout = (ExpandingLayout) convertView.findViewById(R.id.expanding_layout);
		expandingLayout.setExpandedHeight(object.getExpandedHeight());
		expandingLayout.setSizeChangedListener(object);

		if (!object.isExpanded()) {
			expandingLayout.setVisibility(View.GONE);
		} else {
			expandingLayout.setVisibility(View.VISIBLE);
		}

		return convertView;
	}

	public abstract View initView(View view, T model);

}