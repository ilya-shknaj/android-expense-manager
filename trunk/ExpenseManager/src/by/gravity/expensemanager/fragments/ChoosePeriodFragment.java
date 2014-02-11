package by.gravity.expensemanager.fragments;

import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import by.gravity.common.utils.CalendarUtil;
import by.gravity.common.utils.ViewUtils;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.activity.MainActivity;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;

public class ChoosePeriodFragment extends CommonSherlockFragment {

	private static final String DATE_FORMAT = "%s %s %s";

	public static ChoosePeriodFragment newInstanse() {
		ChoosePeriodFragment fragment = new ChoosePeriodFragment();

		return fragment;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		RadioGroup periodGroup = (RadioGroup) getView().findViewById(R.id.period_group);
		periodGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup radiogroup, int i) {
				if (i == R.id.periodUser) {
					enableUserPeriod();
				} else if (i == R.id.periodCurrentMonth) {
					disableUserPeriod();
				} else if (i == R.id.periodAllTime) {
					disableUserPeriod();
				}
			}
		});

		View startDateLayout = getView().findViewById(R.id.startDateLayout);
		final TextView startDate = (TextView) getView().findViewById(R.id.startDate);
		startDateLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Date date = getStartDate();
				((MainActivity) getActivity()).showSelectDateDialog(date.getYear(), date.getMonth(), date.getDay(), new OnDateSetListener() {

					@Override
					public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
						initDateView(startDate, year, month, day);
					}
				});
			}
		});

		View finishDateLayout = getView().findViewById(R.id.finishDateLayout);
		final TextView finishDate = (TextView) getView().findViewById(R.id.finishDate);
		finishDateLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Date date = getFinishDate();
				((MainActivity) getActivity()).showSelectDateDialog(date.getYear(), date.getMonth(), date.getDay(), new OnDateSetListener() {

					@Override
					public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
						initDateView(finishDate, year, month, day);
					}
				});
			}
		});

		View saveButton = getView().findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});

		View cancelButton = getView().findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				getActivity().getSupportFragmentManager().popBackStack();

			}
		});

	}

	private Date getStartDate() {
		Calendar calendar = Calendar.getInstance();

		return calendar.getTime();
	}

	private Date getFinishDate() {
		Calendar calendar = Calendar.getInstance();

		return calendar.getTime();
	}

	private void enableUserPeriod() {
		LinearLayout chooseDateLayout = (LinearLayout) getView().findViewById(R.id.chooseDateLayout);
		if (!chooseDateLayout.isEnabled()) {
			ViewUtils.enableAllView(chooseDateLayout, true);
		}
	}

	private void disableUserPeriod() {
		LinearLayout chooseDateLayout = (LinearLayout) getView().findViewById(R.id.chooseDateLayout);
		if (chooseDateLayout.isEnabled()) {
			ViewUtils.enableAllView(chooseDateLayout, false);
		}
	}

	// private void showSelectDateDialog(OnDateSetListener dateSetListener) {
	// Calendar calendar = Calendar.getInstance();
	// final DatePickerDialog datePickerDialog =
	// DatePickerDialog.newInstance(dateSetListener,
	// calendar.get(Calendar.YEAR),
	// calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
	// false);
	// datePickerDialog.show(getActivity().getSupportFragmentManager(),
	// DatePickerDialog.class.getSimpleName());
	// }

	private void initDateView(TextView textView, int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);

		textView.setText(String.format(DATE_FORMAT, day, CalendarUtil.getMonth(month), year));

	}

	@Override
	public int getViewId() {
		return R.layout.f_choose_period;
	}

	@Override
	public int getTitleResource() {
		return R.string.choose_period;
	}

}
