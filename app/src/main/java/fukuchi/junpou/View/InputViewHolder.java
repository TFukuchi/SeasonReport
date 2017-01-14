package fukuchi.junpou.View;

import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import fukuchi.junpou.R;

public class InputViewHolder extends RecyclerView.ViewHolder {

    final TextView mDayTextView;
    final TextView mDayOfWeekTextView;
    final TextView mNoteTextView;

    final EditText mPlanAttendance;
    final EditText mPlanLeaveTime;
    final EditText mPlanBreakTime;

    final EditText mRealAttendance;
    final EditText mRealLeaveTime;
    final EditText mRealBreakTime;

    final EditText mDeepNightBreakTime;

    final EditText mWorkContent;

    final LinearLayout mPlanArea;
    final TextView mPlanText;

    final LinearLayout mRealArea;
    final TextView mRealText;

    final TextView mWorkText;

    final CheckBox mPaidHolidayCheckBox;
    final CheckBox mTransferWorkCheckBox;
    final CheckBox mFlexCheckBox;
    final CheckBox mHolidayWorkCheckBox;

    final LinearLayout mPaidTimeLinearLayout;
    final Spinner mPaidTimeSpinner;

    InputViewHolder(View itemView) {
        super(itemView);

        // day text
        mDayTextView = (TextView) itemView.findViewById(R.id.day);
        mDayOfWeekTextView = (TextView) itemView.findViewById(R.id.day_of_week);

        // initialize plan area
        mPlanArea = (LinearLayout) itemView.findViewById(R.id.plans);
        mPlanText = (TextView) itemView.findViewById(R.id.plan_text);
        mPlanAttendance = (EditText) itemView.findViewById(R.id.plan_attendance);
        mPlanAttendance.setInputType(InputType.TYPE_CLASS_DATETIME);
        mPlanLeaveTime = (EditText) itemView.findViewById(R.id.plan_leaving_office);
        mPlanLeaveTime.setInputType(InputType.TYPE_CLASS_DATETIME);
        mPlanBreakTime = (EditText) itemView.findViewById(R.id.plan_break_time);
        mPlanBreakTime.setInputType(InputType.TYPE_CLASS_DATETIME);

        // initialize real area
        mRealArea = (LinearLayout) itemView.findViewById(R.id.production);
        mRealText = (TextView) itemView.findViewById(R.id.production_text);
        mRealAttendance = (EditText) itemView.findViewById(R.id.production_attendance);
        mRealAttendance.setInputType(InputType.TYPE_CLASS_DATETIME);
        mRealLeaveTime = (EditText) itemView.findViewById(R.id.production_leaving_office);
        mRealLeaveTime.setInputType(InputType.TYPE_CLASS_DATETIME);
        mRealBreakTime = (EditText) itemView.findViewById(R.id.production_break_time);
        mRealBreakTime.setInputType(InputType.TYPE_CLASS_DATETIME);

        mDeepNightBreakTime = (EditText) itemView.findViewById(R.id.deep_night_break_time_edit);
        mDeepNightBreakTime.setInputType(InputType.TYPE_CLASS_DATETIME);

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
}
