package fukuchi.junpou.View;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class TimerPickerLinearLayout extends LinearLayout {

    public interface OnTimeChangeListener {
        void OnTimeChanged(String time);
    }

    private final static String[] mHours = new String[24];
    private final static String[] mMinutes = new String[]{"0", "15", "30", "45"};

    private Spinner mHourSpinner;
    private Spinner mMinuteSpinner;

    private OnTimeChangeListener mListener = new OnTimeChangeListener() {
        @Override
        public void OnTimeChanged(String time) {
            // nop
        }
    };

    static {
        int hour = 24;
        for (int i = 0; i < hour; i++) {
            mHours[i] = String.valueOf(i);
        }
    }

    public TimerPickerLinearLayout(Context context) {
        this(context, null);
    }

    public TimerPickerLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimerPickerLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TimerPickerLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        setOrientation(LinearLayout.HORIZONTAL);

        mHourSpinner = new Spinner(context);
        mMinuteSpinner = new Spinner(context);

        ArrayAdapter<String> hourAdapter =
                new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, mHours);
        mHourSpinner.setAdapter(hourAdapter);

        this.addView(mHourSpinner);

        TextView text = new TextView(context);
        text.setText(":");
        this.addView(text);

        ArrayAdapter<String> minuteAdapter =
                new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, mMinutes);
        mMinuteSpinner.setAdapter(minuteAdapter);

        this.addView(mMinuteSpinner);
    }

    public void setTime(@Nullable String time) {
        if (time == null || time.isEmpty()) {
            return;
        }
        int hour = Integer.parseInt(time.substring(0, time.indexOf(":")));
        int minute = Integer.parseInt(time.substring(time.indexOf(":") + 1, time.length()));

        mHourSpinner.setSelection(hour);

        int val = minute / 15;

        mMinuteSpinner.setSelection(val);
    }

    public String getTime() {
        return mHourSpinner.getSelectedItem() + ":" + mMinuteSpinner.getSelectedItem();
    }

    public void setListener(@NonNull final OnTimeChangeListener listener) {
        mListener = listener;

        mHourSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mListener.OnTimeChanged(getTime());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // nop
            }
        });

        mMinuteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mListener.OnTimeChanged(getTime());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // nop
            }
        });
    }
}
