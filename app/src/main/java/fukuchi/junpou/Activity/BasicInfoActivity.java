package fukuchi.junpou.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;

import fukuchi.junpou.Model.BasicInfoData;
import fukuchi.junpou.R;
import fukuchi.junpou.Util.PaidHolidayTimeCalculator;
import fukuchi.junpou.Util.PublicVariable;
import fukuchi.junpou.View.TimerPickerLinearLayout;

import static fukuchi.junpou.Util.PublicVariable.KEY_BASE_ATTENDING;
import static fukuchi.junpou.Util.PublicVariable.KEY_BASE_BREAK_TIME;
import static fukuchi.junpou.Util.PublicVariable.KEY_BASE_LEAVING_OFFICE;
import static fukuchi.junpou.Util.PublicVariable.KEY_PAID_HOLIDAY_COUNT;
import static fukuchi.junpou.Util.PublicVariable.KEY_PAID_TIME_COUNT;
import static fukuchi.junpou.Util.PublicVariable.KEY_USER_FIRST_NAME;
import static fukuchi.junpou.Util.PublicVariable.KEY_USER_ID;
import static fukuchi.junpou.Util.PublicVariable.KEY_USER_LAST_NAME;
import static fukuchi.junpou.Util.PublicVariable.KEY_WORK_CODE;
import static fukuchi.junpou.Util.PublicVariable.KEY_WORK_LOCATION;

public class BasicInfoActivity extends AppCompatActivity {
    private EditText mFirstNameEditText;
    private EditText mLastNameEditText;
    private EditText mUserIdEditText;
    private EditText mWorkCodeEditText;
    private EditText mWorkLocationEditText;
    private TimerPickerLinearLayout mBaseAttendingPicker;
    private TimerPickerLinearLayout mBaseLeavingOfficePicker;
    private TimerPickerLinearLayout mBaseBreakTimePicker;
    private EditText mPaidHolidayDateCountEditText;
    private EditText mPaidTimeCountEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_info_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mLastNameEditText = (EditText) findViewById(R.id.last_name);
        mFirstNameEditText = (EditText) findViewById(R.id.first_name);

        mUserIdEditText = (EditText) findViewById(R.id.user_id);
        mWorkCodeEditText = (EditText) findViewById(R.id.work_code);
        mWorkLocationEditText = (EditText) findViewById(R.id.work_location);

        mBaseAttendingPicker = (TimerPickerLinearLayout) findViewById(R.id.base_attending);
        mBaseLeavingOfficePicker = (TimerPickerLinearLayout) findViewById(R.id.base_leaving_office);
        mBaseBreakTimePicker = (TimerPickerLinearLayout) findViewById(R.id.base_break_time);

        mPaidHolidayDateCountEditText = (EditText) findViewById(R.id.paid_holiday_date_count);
        mPaidTimeCountEditText = (EditText) findViewById(R.id.paid_time_count);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BasicInfoData infoData = new BasicInfoData(getApplicationContext());
        mLastNameEditText.setText(infoData.getLastName());
        mFirstNameEditText.setText(infoData.getFirstName());
        setDataForView(infoData.getUserId(), mUserIdEditText);
        setDataForView(infoData.getWorkCode(), mWorkCodeEditText);
        setDataForView(infoData.getWorkLocation(), mWorkLocationEditText);
        mBaseAttendingPicker.setTime(infoData.getBaseAttendingTime());
        mBaseLeavingOfficePicker.setTime(infoData.getBaseLeavingOfficeTime());
        mBaseBreakTimePicker.setTime(infoData.getBaseBreakTime());
        mPaidHolidayDateCountEditText.setText(infoData.getPaidHolidayCount());
        mPaidTimeCountEditText.setText(infoData.getPaidTimeCount());
    }

    @Override
    protected void onPause() {
        saveSharedPrefData();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        boolean result = true;

        switch (id) {
            case android.R.id.home:
                if (isContentsCompleted()) {
                    finish();
                } else {
                    new ErrorDialog().show(getSupportFragmentManager(), "errordialog");
                }
                break;
            default:
                result = super.onOptionsItemSelected(item);
        }
        return result;
    }

    @Override
    public void onBackPressed() {
        if (isContentsCompleted()) {
            super.onBackPressed();
            finish();
        } else {
            new ErrorDialog().show(getSupportFragmentManager(), "errordialog");
        }
    }

    private boolean isContentsCompleted() {
        return !TextUtils.isEmpty(mLastNameEditText.getText()) &&
                !TextUtils.isDigitsOnly(mFirstNameEditText.getText()) &&
                !TextUtils.isEmpty(mUserIdEditText.getText()) &&
                !TextUtils.isEmpty(mWorkCodeEditText.getText()) &&
                !TextUtils.isEmpty(mWorkLocationEditText.getText()) &&
                !TextUtils.isEmpty(mPaidHolidayDateCountEditText.getText()) &&
                !TextUtils.isEmpty(mPaidTimeCountEditText.getText()) &&
                !mBaseAttendingPicker.getTime().equals("0:00") &&
                !mBaseLeavingOfficePicker.getTime().equals("0:00") &&
                !mBaseBreakTimePicker.getTime().equals("0:00");
    }

    private void saveSharedPrefData() {
        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(
                PublicVariable.USER_DATA_PREFERENCE, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_USER_LAST_NAME, mLastNameEditText.getText().toString());
        editor.putString(KEY_USER_FIRST_NAME, mFirstNameEditText.getText().toString());
        editor.putString(KEY_USER_ID, mUserIdEditText.getText().toString());
        editor.putString(KEY_WORK_CODE, mWorkCodeEditText.getText().toString());
        editor.putString(KEY_WORK_LOCATION, mWorkLocationEditText.getText().toString());
        editor.putString(KEY_BASE_ATTENDING, mBaseAttendingPicker.getTime());
        editor.putString(KEY_BASE_LEAVING_OFFICE, mBaseLeavingOfficePicker.getTime());
        editor.putString(KEY_BASE_BREAK_TIME, mBaseBreakTimePicker.getTime());
        editor.putString(KEY_PAID_HOLIDAY_COUNT, mPaidHolidayDateCountEditText.getText().toString());
        editor.putString(KEY_PAID_TIME_COUNT, mPaidTimeCountEditText.getText().toString());
        editor.apply();
        PaidHolidayTimeCalculator.calculate(getApplicationContext());
    }

    private void setDataForView(String baseValue, EditText target) {
        if (baseValue != null && !baseValue.isEmpty()) {
            target.setText(baseValue);
        } else {
            target.setHint(PublicVariable.EMPTY_EDITOR_HINT);
        }
    }

    public static class ErrorDialog extends DialogFragment {

        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("全項目入力してください。");
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            this.setCancelable(false);
            return builder.create();
        }
    }
}
