package by.gravity.expensemanager.util;

import java.text.ParseException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import by.gravity.expensemanager.R;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.ColorPicker.OnColorSelectedListener;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;

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

	public static void showColorPickerDialog(final Context context, int color, final OnColorSelectedListener colorSelectedListener) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		View view = LayoutInflater.from(context).inflate(R.layout.d_color_picker, null);

		final ColorPicker picker = (ColorPicker) view.findViewById(R.id.picker);
		SVBar svBar = (SVBar) view.findViewById(R.id.svbar);
		OpacityBar opacityBar = (OpacityBar) view.findViewById(R.id.opacitybar);

		picker.addSVBar(svBar);
		picker.addOpacityBar(opacityBar);
		picker.setColor(color);
		picker.setOldCenterColor(color);
		picker.setNewCenterColor(color);

		builder.setView(view);

		builder.setNegativeButton(R.string.cancel, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
			}
		});

		builder.setPositiveButton(R.string.ok, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				colorSelectedListener.onColorSelected(picker.getColor());
			}
		});
		builder.show();
	}

}
