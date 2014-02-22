package by.gravity.expensemanager.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import by.gravity.common.utils.GlobalUtil;
import by.gravity.common.utils.StringUtil;
import by.gravity.expensemanager.R;
import by.gravity.expensemanager.util.Constants;
import by.gravity.expensemanager.util.math.Parser;
import by.gravity.expensemanager.util.math.SyntaxException;

public class NumberDialog extends DialogFragment {
	private static final String TAG = NumberDialog.class.getSimpleName();

	private static NumberDialog instance;

	private static final String ARG_COSTS = "ARG_COSTS";

	private static OnInputCompleteListener onInputCompleteListener;

	public interface OnInputCompleteListener {
		public void onInputCompleted(String value);
	}

	public static NumberDialog newInstance(String costs, OnInputCompleteListener onInputCompleteListener) {
		NumberDialog.onInputCompleteListener = onInputCompleteListener;

		instance = new NumberDialog();
		Bundle bundle = new Bundle();
		bundle.putString(ARG_COSTS, costs);
		instance.setArguments(bundle);
		instance.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.d_input_number, null);
	}

	private String getCostsArg() {
		return getArguments().getString(ARG_COSTS);
	}

	@Override
	public void onActivityCreated(Bundle bundle) {
		super.onActivityCreated(bundle);

		final TextView numberTextView = (TextView) getView().findViewById(R.id.text);
		if (!StringUtil.isEmpty(getCostsArg())) {
			numberTextView.setText(getCostsArg());
		}
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				int viewId = v.getId();
				StringBuilder currentText = new StringBuilder(numberTextView.getText());

				if (viewId == R.id.one) {
					currentText.append(Constants.ONE_STRING);
				} else if (viewId == R.id.two) {
					currentText.append(Constants.TWO_STRING);
				} else if (viewId == R.id.three) {
					currentText.append(Constants.THREE_STRING);
				} else if (viewId == R.id.four) {
					currentText.append(Constants.FOUR_STRING);
				} else if (viewId == R.id.five) {
					currentText.append(Constants.FIVE_STRING);
				} else if (viewId == R.id.six) {
					currentText.append(Constants.SIX_STRING);
				} else if (viewId == R.id.seven) {
					currentText.append(Constants.SEVEN_STRING);
				} else if (viewId == R.id.eight) {
					currentText.append(Constants.EIGHT_STRING);
				} else if (viewId == R.id.nine) {
					currentText.append(Constants.NINE_STRING);
				} else if (viewId == R.id.doubleZero) {
					currentText.append(Constants.DOUBLE_ZERO_STRIG);
				} else if (viewId == R.id.zero) {
					currentText.append(Constants.ZERO_STRING);
				} else if (viewId == R.id.dot) {
					if (currentText.lastIndexOf(Constants.DOT_STRING) == -1) {
						currentText.append(Constants.DOT_STRING);
					}
				} else if (viewId == R.id.backspace) {
					if (currentText.length() > 0) {
						currentText.deleteCharAt(currentText.length() - 1);
					}
				} else if (viewId == R.id.plus) {
					int index = currentText.length() - 1;
					char lastChar = currentText.charAt(index);
					if (lastChar == Constants.PLUS_CHAR) {
						return;
					} else if (lastChar == Constants.MINUS_CHAR || lastChar == Constants.MULTIPLE_CHAR || lastChar == Constants.DEVIDE_CHAR) {
						currentText.deleteCharAt(index);
					}
					currentText.append(Constants.PLUS_CHAR);
				} else if (viewId == R.id.minus) {
					int index = currentText.length() - 1;
					char lastChar = currentText.charAt(index);
					if (lastChar == Constants.MINUS_CHAR) {
						return;
					} else if (lastChar == Constants.PLUS_CHAR || lastChar == Constants.MULTIPLE_CHAR || lastChar == Constants.DEVIDE_CHAR) {
						currentText.deleteCharAt(index);
					}
					currentText.append(Constants.MINUS_CHAR);
				} else if (viewId == R.id.multiple) {
					int index = currentText.length() - 1;
					char lastChar = currentText.charAt(index);
					if (lastChar == Constants.MULTIPLE_CHAR) {
						return;
					} else if (lastChar == Constants.MINUS_CHAR || lastChar == Constants.PLUS_CHAR || lastChar == Constants.DEVIDE_CHAR) {
						currentText.deleteCharAt(index);
					}
					currentText.append(Constants.MULTIPLE_CHAR);
				} else if (viewId == R.id.devide) {
					int index = currentText.length() - 1;
					char lastChar = currentText.charAt(index);
					if (lastChar == Constants.DEVIDE_CHAR) {
						return;
					} else if (lastChar == Constants.MINUS_CHAR || lastChar == Constants.PLUS_CHAR || lastChar == Constants.MULTIPLE_CHAR) {
						currentText.deleteCharAt(index);
					}
					currentText.append(Constants.DEVIDE_CHAR);
				} else if (viewId == R.id.cancelButton) {
					GlobalUtil.hideSoftKeyboard(getActivity());
					dismiss();
				}
				numberTextView.setText(StringUtil.convertNumberToHumanFriednly(currentText.toString().replaceAll(Constants.SPACE_PATTERN,
						Constants.EMPTY_STRING)));
				if (viewId == R.id.okButton) {
					if (!StringUtil.isEmpty(currentText.toString())) {
						try {

							String parsedValue = StringUtil.convertNumberToHumanFriednly(Parser.parse(
									currentText.toString().replaceAll(Constants.SPACE_PATTERN, Constants.EMPTY_STRING)).value());
							if (onInputCompleteListener != null) {
								onInputCompleteListener.onInputCompleted(parsedValue);
							}
						} catch (SyntaxException e) {
							Toast.makeText(getActivity(), R.string.parseNumberError, Toast.LENGTH_SHORT).show();
							Log.e(TAG, "error ", e);
						}
					}
					dismiss();
				}
			}
		};
		View one = getView().findViewById(R.id.one);
		View two = getView().findViewById(R.id.two);
		View three = getView().findViewById(R.id.three);
		View four = getView().findViewById(R.id.four);
		View five = getView().findViewById(R.id.five);
		View six = getView().findViewById(R.id.six);
		View seven = getView().findViewById(R.id.seven);
		View eight = getView().findViewById(R.id.eight);
		View nine = getView().findViewById(R.id.nine);
		View doubleZero = getView().findViewById(R.id.doubleZero);
		View zero = getView().findViewById(R.id.zero);
		View dot = getView().findViewById(R.id.dot);
		View okButton = getView().findViewById(R.id.okButton);
		View cancelButton = getView().findViewById(R.id.cancelButton);
		View backspace = getView().findViewById(R.id.backspace);
		View plus = getView().findViewById(R.id.plus);
		View minus = getView().findViewById(R.id.minus);
		View multiple = getView().findViewById(R.id.multiple);
		View devide = getView().findViewById(R.id.devide);
		one.setOnClickListener(onClickListener);
		two.setOnClickListener(onClickListener);
		three.setOnClickListener(onClickListener);
		four.setOnClickListener(onClickListener);
		five.setOnClickListener(onClickListener);
		six.setOnClickListener(onClickListener);
		seven.setOnClickListener(onClickListener);
		eight.setOnClickListener(onClickListener);
		nine.setOnClickListener(onClickListener);
		doubleZero.setOnClickListener(onClickListener);
		zero.setOnClickListener(onClickListener);
		dot.setOnClickListener(onClickListener);
		okButton.setOnClickListener(onClickListener);
		cancelButton.setOnClickListener(onClickListener);
		backspace.setOnClickListener(onClickListener);
		backspace.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				numberTextView.setText("");
				return false;
			}
		});
		plus.setOnClickListener(onClickListener);
		minus.setOnClickListener(onClickListener);
		multiple.setOnClickListener(onClickListener);
		devide.setOnClickListener(onClickListener);

		setCancelable(true);

	}

}
