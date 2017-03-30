package fukuchi.junpou.View;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fukuchi.junpou.Model.DateData;
import fukuchi.junpou.Model.DateDetailsContainer;
import fukuchi.junpou.R;

public class InputViewListHolder extends InputViewHolderBase {
    private final TextView mDay;
    private final TextView mWeek;
    private final TextView mNote;

    private final TextView mPlanAttend;
    private final TextView mPlanLeave;
    private final TextView mPlanBreakTime;

    private final TextView mRealAttend;
    private final TextView mRealLeave;
    private final TextView mRealBreakTime;
    private final TextView mRealDeepBreakTime;
    private final ImageView mHolidayImage;

    private final TextView mWorkContents;

    public InputViewListHolder(View itemView) {
        super(itemView);

        mDay = (TextView) itemView.findViewById(R.id.list_day);
        mWeek = (TextView) itemView.findViewById(R.id.list_day_of_week);
        mNote = (TextView) itemView.findViewById(R.id.list_note);
        mPlanAttend = (TextView) itemView.findViewById(R.id.list_plan_attend);
        mPlanLeave = (TextView) itemView.findViewById(R.id.list_plan_leave);
        mPlanBreakTime = (TextView) itemView.findViewById(R.id.list_plan_break_time);
        mRealAttend = (TextView) itemView.findViewById(R.id.list_real_attend);
        mRealLeave = (TextView) itemView.findViewById(R.id.list_real_leave);
        mRealBreakTime = (TextView) itemView.findViewById(R.id.list_real_break_time);
        mRealDeepBreakTime = (TextView) itemView.findViewById(R.id.list_real_night_break);
        mHolidayImage = (ImageView) itemView.findViewById(R.id.holiday_image);
        mWorkContents = (TextView) itemView.findViewById(R.id.work_content);
    }

    public void setData(DateData data) {
        mDay.setText(data.getDay());
        String dayOfWeek = data.getDayOfWeek();
        mWeek.setText("(" + dayOfWeek + ")");
        mWeek.setTextColor(data.getColor());
        mNote.setText(data.getHolidayText());

        DateDetailsContainer container = data.getDetailContainer();
        if (container == null) return;

        boolean holiday = data.getVisibility() == View.GONE ||
                container.getPaidHolidayFrag();

        mPlanAttend.setText(data.getPlanAttend());
        mPlanLeave.setText(data.getPlanLeave());
        mPlanBreakTime.setText(data.getPlanBreakTime());

        mRealAttend.setText(data.getRealAttend());
        mRealLeave.setText(data.getRealLeave());
        mRealBreakTime.setText(data.getRealBreakTime());
        mRealDeepBreakTime.setText(data.getDeepNightBreakTime());
        mWorkContents.setText(data.getWorkContent());

        int visibility = holiday ? View.VISIBLE : View.GONE;
        mHolidayImage.setVisibility(visibility);
    }
}
