package fukuchi.junpou.Activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fukuchi.junpou.DataBase.DataDetailDbWriteHelper;
import fukuchi.junpou.DataBase.InputValueDbWriteHelper;
import fukuchi.junpou.Model.DateData;
import fukuchi.junpou.Model.DateDetailsContainer;
import fukuchi.junpou.R;
import fukuchi.junpou.Util.JunpouPattern;
import fukuchi.junpou.View.TimerPickerLinearLayout;

public class DataInputActivity extends AppCompatActivity {

    private TimerPickerLinearLayout mPlanAttendance;
    private TimerPickerLinearLayout mPlanLeaveTime;
    private TimerPickerLinearLayout mPlanBreakTime;

    private TimerPickerLinearLayout mRealAttendance;
    private TimerPickerLinearLayout mRealLeaveTime;
    private TimerPickerLinearLayout mRealBreakTime;

    private TimerPickerLinearLayout mDeepNightBreakTime;

    private EditText mWorkContent;

    private CheckBox mPaidHolidayCheckBox;
    private CheckBox mTransferWorkCheckBox;
    private CheckBox mFlexCheckBox;
    private CheckBox mHolidayWorkCheckBox;

    private InputValueDbWriteHelper mWriterHelper;
    private DataDetailDbWriteHelper mDetailWriteHelper;

    private Spinner mPaidTimeSpinner;

    private DateData mData;

    private JunpouPattern.OnDataQueryListener mOnDataQueryListener =
            new JunpouPattern.OnDataQueryListener() {

                @Override
                public void onDataQueryFinished(List<DateData> dataList) {
                    for (DateData data : dataList) {
                        setDataToLayout(data);
                    }
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String date = intent.getStringExtra("TEST_DATE");
        int month = Integer.parseInt(date.substring(0, date.indexOf("/")));
        int day = Integer.parseInt(date.substring(date.indexOf("/") + 1));
        //年月日を取得して、JunpouPatternから日付のデータを取得する

        JunpouPattern pattern = new JunpouPattern(getApplicationContext());

        List<Integer> dayList = new ArrayList<>();
        dayList.add(day);
        pattern.getDateData(intent.getIntExtra("TEST_YEAR", 0), month, dayList, mOnDataQueryListener);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(month + "月 " + day + "日 " + "(" + intent.getStringExtra("TEST_WEEK") + ")");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initLayout();
        mWriterHelper = new InputValueDbWriteHelper(getApplicationContext());
        mDetailWriteHelper = new DataDetailDbWriteHelper(getApplicationContext());
    }

    @Override
    protected void onPause() {
        super.onPause();
        new UpsertDataTask().execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initLayout() {
        mPlanAttendance = (TimerPickerLinearLayout) findViewById(R.id.plan_attendance);
        mPlanLeaveTime = (TimerPickerLinearLayout) findViewById(R.id.plan_leaving_office);
        mPlanBreakTime = (TimerPickerLinearLayout) findViewById(R.id.plan_break_time);

        mRealAttendance = (TimerPickerLinearLayout) findViewById(R.id.production_attendance);
        mRealLeaveTime = (TimerPickerLinearLayout) findViewById(R.id.production_leaving_office);
        mRealBreakTime = (TimerPickerLinearLayout) findViewById(R.id.production_break_time);
        mDeepNightBreakTime = (TimerPickerLinearLayout) findViewById(R.id.deep_night_break_time_edit);

        mWorkContent = (EditText) findViewById(R.id.work_content);

        mPaidHolidayCheckBox = (CheckBox) findViewById(R.id.paid_holiday);
        mTransferWorkCheckBox = (CheckBox) findViewById(R.id.transfer_work);
        mFlexCheckBox = (CheckBox) findViewById(R.id.flex);
        mHolidayWorkCheckBox = (CheckBox) findViewById(R.id.holiday_work);
        mPaidTimeSpinner = (Spinner) findViewById(R.id.paid_time_spinner);
    }

    private void setDataToLayout(@NonNull DateData data) {
        mData = data;
        mPlanAttendance.setTime(data.getPlanAttend());
        mPlanLeaveTime.setTime(data.getPlanLeave());
        mPlanBreakTime.setTime(data.getPlanBreakTime());

        mRealAttendance.setTime(data.getRealAttend());
        mRealLeaveTime.setTime(data.getRealLeave());
        mRealBreakTime.setTime(data.getRealBreakTime());
        mDeepNightBreakTime.setTime(data.getDeepNightBreakTime());

        mWorkContent.setText(data.getWorkContent());

        DateDetailsContainer container = data.getDetailContainer();

        if (container == null) return;

        mPaidHolidayCheckBox.setChecked(container.getPaidHolidayFrag());
        mTransferWorkCheckBox.setChecked(container.getTransferWorkFlag());
        mFlexCheckBox.setChecked(container.getFlex());
        mHolidayWorkCheckBox.setChecked(container.getHolidayWorkFlag());

        PaidTimeSpinnerAdapter spinnerAdapter = new PaidTimeSpinnerAdapter(getApplicationContext());
        mPaidTimeSpinner.setAdapter(spinnerAdapter.getSpinnerAdapter());
        mPaidTimeSpinner.setSelection(Integer.valueOf(container.getPaidTime()));
    }

    private class UpsertDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            setDB();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //if implements this
            // must be dead check UIThread.
        }
    }

    private void setDB() {
        List<ContentValues> dateValuesList = new ArrayList<>();
        List<ContentValues> dateDetailValuesList = new ArrayList<>();
        List<DateData> dataList = createDataList();
        for (DateData dateData : dataList) {
            dateValuesList.add(mWriterHelper.createContentValue(dateData));
            DateDetailsContainer container = dateData.getDetailContainer();
            dateDetailValuesList.add(mDetailWriteHelper.createContentValue(dateData.getId(),
                    dateData.getDay(), container.getPaidHolidayFrag(), container.getPaidTime(),
                    container.getTransferWorkFlag(), container.getFlex(),
                    container.getHolidayWorkFlag()));
        }

        mWriterHelper.upsert(dateValuesList);
        mDetailWriteHelper.upsert(dateDetailValuesList);
    }

    private List<DateData> createDataList() {
        List<DateData> res = new ArrayList<>();

        mData.setPlanAttend(mPlanAttendance.getTime());
        mData.setPlanLeave(mPlanLeaveTime.getTime());
        mData.setPlanBreakTime(mPlanBreakTime.getTime());
        mData.setRealAttend(mRealAttendance.getTime());
        mData.setRealLeave(mRealLeaveTime.getTime());
        mData.setRealBreakTime(mRealBreakTime.getTime());
        mData.setDeepNightBreakTime(mDeepNightBreakTime.getTime());
        mData.setWorkContent(mWorkContent.getText().toString());

        DateDetailsContainer container = new DateDetailsContainer();
        container.setPaidHolidayFlag(mPaidHolidayCheckBox.isChecked());
        container.setTransferWorkFrag(mTransferWorkCheckBox.isChecked());
        container.setFlex(mFlexCheckBox.isChecked());
        container.setHolidayWorkFlag(mHolidayWorkCheckBox.isChecked());
        container.setPaidTime((String)mPaidTimeSpinner.getSelectedItem());

        mData.setDateDetailContainer(container);
        res.add(mData);
        return res;
    }

    private class PaidTimeSpinnerAdapter {

        private final Context mContext;

        PaidTimeSpinnerAdapter(@NonNull Context context) {
            mContext = context;
        }

        ArrayAdapter<String> getSpinnerAdapter() {
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(mContext, R.layout.paid_time_spinner_layout);

            String[] stringArray = mContext.getResources().getStringArray(R.array.paid_time_array);
            List<String> spinnerList = Arrays.asList(stringArray);
            for (String item : spinnerList) {
                adapter.add(item);
            }

            adapter.setDropDownViewResource(R.layout.paid_time_spinner_list_layout);

            return adapter;
        }
    }
}
