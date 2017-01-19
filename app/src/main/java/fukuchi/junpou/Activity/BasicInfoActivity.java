package fukuchi.junpou.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import fukuchi.junpou.Model.BasicInfoData;
import fukuchi.junpou.R;
import fukuchi.junpou.Util.PublicVariable;
import fukuchi.junpou.View.TimerPickerLinearLayout;

public class BasicInfoActivity extends AppCompatActivity {
    private EditText mNameEditText;
    private EditText mUserIdEditText;
    private EditText mWorkCodeEditText;
    private EditText mWorkLocationEditText;
    private TimerPickerLinearLayout mBaseAttendingPicker;
    private TimerPickerLinearLayout mBaseLeavingOfficePicker;
    private TimerPickerLinearLayout mBaseBreakTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_info_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mNameEditText = (EditText) findViewById(R.id.name);

        mUserIdEditText = (EditText) findViewById(R.id.user_id);

        mWorkCodeEditText = (EditText) findViewById(R.id.work_code);

        mWorkLocationEditText = (EditText) findViewById(R.id.work_location);

        mBaseAttendingPicker = (TimerPickerLinearLayout) findViewById(R.id.base_attending);
        mBaseLeavingOfficePicker = (TimerPickerLinearLayout) findViewById(R.id.base_leaving_office);
        mBaseBreakTimePicker = (TimerPickerLinearLayout) findViewById(R.id.base_break_time);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BasicInfoData infoData = new BasicInfoData(getApplicationContext());
        setDataForView(infoData.getName(), mNameEditText);
        setDataForView(infoData.getUserId(), mUserIdEditText);
        setDataForView(infoData.getWorkCode(), mWorkCodeEditText);
        setDataForView(infoData.getWorkLocation(), mWorkLocationEditText);
        mBaseAttendingPicker.setTime(infoData.getBaseAttendingTime());
        mBaseLeavingOfficePicker.setTime(infoData.getBaseLeavingOfficeTime());
        mBaseBreakTimePicker.setTime(infoData.getBaseBreakTime());
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
                finish();
                break;
            default:
                result = super.onOptionsItemSelected(item);
        }

        return result;
    }

    private void saveSharedPrefData() {
        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(
                PublicVariable.USER_DATA_PREFERENCE, Context.MODE_PRIVATE).edit();
        editor.putString(PublicVariable.KEY_USER_NAME, mNameEditText.getText().toString());
        editor.putString(PublicVariable.KEY_USER_ID, mUserIdEditText.getText().toString());
        editor.putString(PublicVariable.KEY_WORK_CODE, mWorkCodeEditText.getText().toString());
        editor.putString(PublicVariable.KEY_WORK_LOCATION, mWorkLocationEditText.getText().toString());
        editor.putString(PublicVariable.KEY_BASE_ATTENDING, mBaseAttendingPicker.getTime());
        editor.putString(PublicVariable.KEY_BASE_LEAVING_OFFICE, mBaseLeavingOfficePicker.getTime());
        editor.putString(PublicVariable.KEY_BASE_BREAK_TIME, mBaseBreakTimePicker.getTime());
        editor.apply();
    }

    private void setDataForView(String baseValue, EditText target) {
        if (baseValue != null && !baseValue.isEmpty()) {
            target.setText(baseValue);
        } else {
            target.setHint(PublicVariable.EMPTY_EDITOR_HINT);
        }
    }
}
