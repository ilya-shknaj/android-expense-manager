package by.gravity.expensemanager.util;

import java.math.BigDecimal;
import java.text.ParseException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources.NotFoundException;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.widget.EditText;
import android.widget.Toast;
import by.gravity.expensemanager.R;

public class DialogHelper {

	public interface OnPositiveButtonClickListener {

		public void onPositiveButtonClicked();
	}

	public interface OnSelectedListener {

		public void onSelectedListener(String value);
	}

	public interface onEditCompleteListener {

		public void onEditCompelted(double value);
	}

	public static void showConfirmDialog(Activity activity, int title, int message, final OnPositiveButtonClickListener onPositiveButtonClickListener) {

		AlertDialog.Builder adb = new AlertDialog.Builder(activity);

		adb.setTitle(title);

		adb.setMessage(message);

		adb.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

				if (onPositiveButtonClickListener != null) {
					onPositiveButtonClickListener.onPositiveButtonClicked();
				}
			}
		});

		adb.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
			}
		});
		adb.show();

	}

	public static void showNumberEditDialog(final Activity activity, int title, int message, String value,
			final onEditCompleteListener onEditCompleteListener) {

		showNumberEditDialog(activity, activity.getString(title), message, value, onEditCompleteListener);
	}

	public static void showNumberEditDialog(final Activity activity, String title, int message, String value,
			final onEditCompleteListener onEditCompleteListener) {

		final EditText editText = new EditText(activity);
		editText.setText(value);
		editText.setRawInputType(InputType.TYPE_CLASS_NUMBER);

		int position = editText.length();
		Editable editable = editText.getText();
		Selection.setSelection(editable, position);

		AlertDialog.Builder alertDiaogBuilder = new AlertDialog.Builder(activity);

		alertDiaogBuilder.setTitle(title);
		if (message != 0) {
			alertDiaogBuilder.setMessage(message);
		}

		alertDiaogBuilder.setView(editText);

		alertDiaogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

				String value = editText.getText().toString();
				Double doubleValue = null;
				try {
					doubleValue = GlobalUtils.getExchangeNumberFormat().parse(value).doubleValue();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if (doubleValue != null && doubleValue <= 0.0) {
					Toast.makeText(activity, R.string.valueMustBeMoreThenZero, Toast.LENGTH_SHORT).show();
					return;
				}

				if (doubleValue != null && onEditCompleteListener != null) {
					onEditCompleteListener.onEditCompelted(doubleValue);
				}
			}
		});

		alertDiaogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
			}
		});
		alertDiaogBuilder.show();
	}

	public static void showRadioButtonDialog(final Context context, final int title, final String[] items, String activeItem,
			final OnSelectedListener onSelectedListener) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);

		int checkedIndex = -1;
		for (int i = 0; i < items.length; i++) {
			if (items[i].equals(activeItem)) {
				checkedIndex = i;
				break;
			}
		}

		builder.setSingleChoiceItems(items, checkedIndex, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				onSelectedListener.onSelectedListener(items[which]);
				dialog.dismiss();
			}
		});
		builder.show();

	}

}
