package fukuchi.junpou.View;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import fukuchi.junpou.Model.DateData;
import fukuchi.junpou.Model.DateDetailsContainer;
import fukuchi.junpou.R;

public class InputViewHolder extends InputViewHolderBase {

    private final TextView mDayTextView;
    private final TextView mDayOfWeekTextView;
    private final TextView mNoteTextView;

    final TimerPickerLinearLayout mPlanAttendance;
    final TimerPickerLinearLayout mPlanLeaveTime;
    final TimerPickerLinearLayout mPlanBreakTime;

    final TimerPickerLinearLayout mRealAttendance;
    final TimerPickerLinearLayout mRealLeaveTime;
    final TimerPickerLinearLayout mRealBreakTime;

    final TimerPickerLinearLayout mDeepNightBreakTime;

    final EditText mWorkContent;

    private final LinearLayout mPlanArea;
    private final TextView mPlanText;

    private final LinearLayout mRealArea;
    private final TextView mRealText;

    private final TextView mWorkText;

    final CheckBox mPaidHolidayCheckBox;
    final CheckBox mTransferWorkCheckBox;
    final CheckBox mFlexCheckBox;
    final CheckBox mHolidayWorkCheckBox;

    private final LinearLayout mPaidTimeLinearLayout;
    final Spinner mPaidTimeSpinner;

    InputViewHolder(View itemView) {
        super(itemView);

        // day text
        mDayTextView = (TextView) itemView.findViewById(R.id.day);
        mDayOfWeekTextView = (TextView) itemView.findViewById(R.id.day_of_week);

        // initialize plan area
        mPlanArea = (LinearLayout) itemView.findViewById(R.id.plans);
        mPlanText = (TextView) itemView.findViewById(R.id.plan_text);
        mPlanAttendance = (TimerPickerLinearLayout) itemView.findViewById(R.id.plan_attendance);
        mPlanLeaveTime = (TimerPickerLinearLayout) itemView.findViewById(R.id.plan_leaving_office);
        mPlanBreakTime = (TimerPickerLinearLayout) itemView.findViewById(R.id.plan_break_time);

        // initialize real area
        mRealArea = (LinearLayout) itemView.findViewById(R.id.production);
        mRealText = (TextView) itemView.findViewById(R.id.production_text);
        mRealAttendance = (TimerPickerLinearLayout) itemView.findViewById(R.id.production_attendance);
        mRealLeaveTime = (TimerPickerLinearLayout) itemView.findViewById(R.id.production_leaving_office);
        mRealBreakTime = (TimerPickerLinearLayout) itemView.findViewById(R.id.production_break_time);
        mDeepNightBreakTime = (TimerPickerLinearLayout) itemView.findViewById(R.id.deep_night_break_time_edit);

        // work text area
        mWorkText = (TextView) itemView.findViewById(R.id.work_text);
        mWorkContent = (EditText) itemView.findViewById(R.id.work_content);
        mNoteTextView = (TextView) itemView.findViewById(R.id.note);

        mPaidHolidayCheckBox = (CheckBox) itemView.findViewById(R.id.paid_holiday);
        mTransferWorkCheckBox = (CheckBox) itemView.findViewById(R.id.transfer_work);
        mFlexCheckBox = (CheckBox) itemView.findViewById(R.id.flex);
        mHolidayWorkCheckBox = (CheckBox) itemView.findViewById(R.id.holiday_work);

        mPaidTimeLinearLayout = (LinearLayout) itemView.findViewById(R.id.paid_time);
        mPaidTimeSpinner = (Spinner) itemView.findViewById(R.id.paid_time_spinner);
    }

    public void setVisibility(DateData data) {
        int visibility = data.getVisibility() == View.GONE ? View.GONE : View.VISIBLE;
        mPlanArea.setVisibility(visibility);
        mPlanText.setVisibility(visibility);
        mRealArea.setVisibility(visibility);
        mRealText.setVisibility(visibility);
        mWorkContent.setVisibility(visibility);
        mWorkText.setVisibility(visibility);
        mPaidHolidayCheckBox.setVisibility(visibility);
        mPaidTimeLinearLayout.setVisibility(visibility);
        mFlexCheckBox.setVisibility(visibility);
        if (visibility == View.VISIBLE) {
            mHolidayWorkCheckBox.setVisibility(View.GONE);
        } else {
            mHolidayWorkCheckBox.setVisibility(View.VISIBLE);
        }
    }

    public void setDataForLayout(DateData data) {
        mDayTextView.setText(data.getDay());
        String dayOfWeek = data.getDayOfWeek();
        mDayOfWeekTextView.setText("(" + dayOfWeek + ")");
        mDayOfWeekTextView.setTextColor(data.getColor());
        mNoteTextView.setText(data.getHolidayText());
        mPlanAttendance.setTime(data.getPlanAttend());
        mPlanLeaveTime.setTime(data.getPlanLeave());
        mPlanBreakTime.setTime(data.getPlanBreakTime());
        mRealAttendance.setTime(data.getRealAttend());
        mRealLeaveTime.setTime(data.getRealLeave());
        mRealBreakTime.setTime(data.getRealBreakTime());
        mDeepNightBreakTime.setTime(data.getDeepNightBreakTime());
        mWorkContent.setText(data.getWorkContent());
        DateDetailsContainer container = data.getDetailContainer();
        mPaidHolidayCheckBox.setChecked(container.getPaidHolidayFrag());
        mTransferWorkCheckBox.setChecked(container.getTransferWorkFlag());
        mFlexCheckBox.setChecked(container.getFlex());
        mHolidayWorkCheckBox.setChecked(container.getHolidayWorkFlag());
    }

    public void changeVisibility(int visibility) {
        mPlanArea.setVisibility(visibility);
        mPlanText.setVisibility(visibility);
        mRealArea.setVisibility(visibility);
        mRealText.setVisibility(visibility);
        mDeepNightBreakTime.setVisibility(visibility);
    }

}
