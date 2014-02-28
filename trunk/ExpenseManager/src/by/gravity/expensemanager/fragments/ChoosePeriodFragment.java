package by.gravity.expensemanager.fragments;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import by.gravity.common.utils.CalendarUtil;
import by.gravity.common.utils.StringUtil;
import by.gravity.common.utils.ViewUtils;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.activity.MainActivity;
import by.gravity.expensemanager.data.SettingsManager;
import by.gravity.expensemanager.util.Constants;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;

public class ChoosePeriodFragment extends CommonSherlockFragment {

	public static ChoosePeriodFragment newInstanse() {
		ChoosePeriodFragment fragment = new ChoosePeriodFragment();

		return fragment;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		final RadioGroup periodGroup = (RadioGroup) getView().findViewById(R.id.periodGroup);
		periodGroup.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup radiogroup, int i) {
				if (i == R.id.periodUser) {
					enableUserPeriod();
				} else {
					disableUserPeriod();
				}
			}
		});
		int periodViewId = getPeriodViewId();
		if (periodViewId != -1) {
			periodGroup.check(periodViewId);
		}

		View startDateLayout = getView().findViewById(R.id.startDateLayout);
		final TextView startDate = (TextView) getView().findViewById(R.id.startDate);
		startDate.setText(SettingsManager.getUserPeriodStartDate(getCurrentDate()));
		startDateLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Calendar date = getStartDate();
				((MainActivity) getActivity()).showSelectDateDialog(date.get(Calendar.YEAR), date.get(Calendar.MONTH),
						date.get(Calendar.DAY_OF_MONTH), new OnDateSetListener() {

							@Override
							public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
								initDateView(startDate, year, month, day);
							}
						});
			}
		});

		View finishDateLayout = getView().findViewById(R.id.finishDateLayout);
		final TextView finishDate = (TextView) getView().findViewById(R.id.finishDate);
		finishDate.setText(SettingsManager.getUserPeriodEndDate(getCurrentDate()));
		finishDateLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Calendar date = getFinishDate();
				((MainActivity) getActivity()).showSelectDateDialog(date.get(Calendar.YEAR), date.get(Calendar.MONTH),
						date.get(Calendar.DAY_OF_MONTH), new OnDateSetListener() {

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
				String selectedPeriod = getPeriodString(periodGroup.getCheckedRadioButtonId());
				if (!StringUtil.isEmpty(selectedPeriod)) {
					SettingsManager.putCurrentPeriod(selectedPeriod);
					if (selectedPeriod.equals(getString(R.string.period_user))) {
						SettingsManager.putUserPeriodStartDate(startDate.getText().toString());
						SettingsManager.putUserPeriodEndDate(finishDate.getText().toString());
					}
				}
				if (getActivity().getCallingActivity() != null) {
					getActivity().setResult(Activity.RESULT_OK);
					getActivity().finish();
				} else {
					getActivity().getSupportFragmentManager().popBackStack();
				}
			}
		});

		View cancelButton = getView().findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (getActivity().getCallingActivity() != null) {
					getActivity().setResult(Activity.RESULT_CANCELED);
					getActivity().finish();
				} else {
					getActivity().getSupportFragmentManager().popBackStack();
				}

			}
		});

	}

	private int getPeriodViewId() {
		String currentPeriod = SettingsManager.getCurrentPeriod();
		int periodId = -1;
		if (currentPeriod.equals(getString(R.string.period_current_month))) {
			periodId = R.id.periodCurrentMonth;
		} else if (currentPeriod.equals(getString(R.string.period_prev_month))) {
			periodId = R.id.periodPrevMonth;
		} else if (currentPeriod.equals(getString(R.string.period_week))) {
			periodId = R.id.periodWeek;
		} else if (currentPeriod.equals(getString(R.string.period_day))) {
			periodId = R.id.periodDay;
		} else if (currentPeriod.equals(getString(R.string.periodAllTime))) {
			periodId = R.id.periodAllTime;
		} else if (currentPeriod.equals(getString(R.string.period_user))) {
			periodId = R.id.periodUser;
		}

		return periodId;
	}

	private String getPeriodString(int viewId) {
		int resId = -1;
		if (viewId == R.id.periodCurrentMonth) {
			resId = R.string.period_current_month;
		} else if (viewId == R.id.periodPrevMonth) {
			resId = R.string.period_prev_month;
		} else if (viewId == R.id.periodWeek) {
			resId = R.string.period_week;
		} else if (viewId == R.id.periodDay) {
			resId = R.string.period_day;
		} else if (viewId == R.id.periodAllTime) {
			resId = R.string.periodAllTime;
		} else if (viewId == R.id.periodUser) {
			resId = R.string.period_user;
		}

		if (resId != -1) {
			return getString(resId);
		}
		return null;

	}

	private Calendar getStartDate() {
		Calendar calendar = Calendar.getInstance();

		return calendar;
	}

	private Calendar getFinishDate() {
		Calendar calendar = Calendar.getInstance();

		return calendar;
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

	private void initDateView(TextView textView, int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);

		textView.setText(String.format(Constants.DATE_FORMAT_FULL, CalendarUtil.getDay(day), CalendarUtil.getMonth(month), year));

	}

	private String getCurrentDate() {
		Calendar calendar = Calendar.getInstance();

		return String.format(Constants.DATE_FORMAT_FULL, calendar.get(Calendar.DAY_OF_MONTH), CalendarUtil.getMonth(calendar.get(Calendar.MONTH)),
				calendar.get(Calendar.YEAR));
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
