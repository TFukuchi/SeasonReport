package fukuchi.junpou.View;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fukuchi.junpou.Controller.TextWatcherImpl;
import fukuchi.junpou.Model.DateData;
import fukuchi.junpou.Model.DateDetailsContainer;
import fukuchi.junpou.R;

public class InputViewAdapter extends RecyclerView.Adapter<InputViewHolder> {

    private static final int VIEW_INPUT = 0;

    private final LayoutInflater mInflater;

    /**
     * Base list
     * <p/>
     * get from Database
     */
    private final List<DateData> mJunpouDateList;

    //Map<Day, pos>
    private Map<String, Integer> mTagMap = new HashMap<String, Integer>();

    private Context mContext;

    public InputViewAdapter(Context context, List<DateData> data) {
        super();
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mJunpouDateList = data;
        for (int i = 0; i < mJunpouDateList.size(); i++) {
            DateData dateData = mJunpouDateList.get(i);
            mTagMap.put(dateData.getDay(), i);
        }
        setHasStableIds(true);
    }

    @Override
    public InputViewHolder onCreateViewHolder(ViewGroup container, int viewType) {
        View view = mInflater.inflate(R.layout.input_view, container, false);
        InputViewHolder holder = new InputViewHolder(view);

        setViewListener(holder);

        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_INPUT;
    }

    @Override
    public void onBindViewHolder(final InputViewHolder holder, final int position) {
        if (mJunpouDateList != null) {
            int target = holder.getAdapterPosition();
            DateData data = mJunpouDateList.get(target);

            holder.mDayTextView.setText(data.getDay());
            setDayOfWeek(holder, data);
            setNote(holder, data);
            setVisibility(holder, data);
            setDataForLayout(holder, data);
            setPaidTimeSpinner(holder, data);
        }
    }

    @Override
    public int getItemCount() {
        if (mJunpouDateList != null) {
            return mJunpouDateList.size();
        } else {
            return 0;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    public DateData getDateData(int position) {
        return mJunpouDateList.get(position);
    }

    public int getViewPosition(String day) {
        return mTagMap.get(day);
    }

    private void setDayOfWeek(final InputViewHolder viewHolder, DateData data) {

        String dayOfWeek = data.getDayOfWeek();
        viewHolder.mDayOfWeekTextView.setText("(" + dayOfWeek + ")");
        viewHolder.mDayOfWeekTextView.setTextColor(data.getColor());
    }

    private void setNote(final InputViewHolder viewHolder, DateData data) {
        String note = data.getHolidayText();
        viewHolder.mNoteTextView.setText(note);
    }

    private void setVisibility(final InputViewHolder viewHolder, DateData data) {
        int visibility = data.getVisibility() == View.GONE ? View.GONE : View.VISIBLE;
        viewHolder.mPlanArea.setVisibility(visibility);
        viewHolder.mPlanText.setVisibility(visibility);
        viewHolder.mRealArea.setVisibility(visibility);
        viewHolder.mRealText.setVisibility(visibility);
        viewHolder.mWorkContent.setVisibility(visibility);
        viewHolder.mWorkText.setVisibility(visibility);
        viewHolder.mPaidHolidayCheckBox.setVisibility(visibility);
        viewHolder.mPaidTimeLinearLayout.setVisibility(visibility);
        viewHolder.mFlexCheckBox.setVisibility(visibility);
        if (visibility == View.VISIBLE) {
            viewHolder.mTransferWorkCheckBox.setText("振替休日");
        } else {
            viewHolder.mHolidayWorkCheckBox.setVisibility(View.VISIBLE);
        }
    }

    // 休日出勤を選択された場合に、Viewの表示を切り替える
    private void changeVisibility(final InputViewHolder viewHolder, int visibility) {
        viewHolder.mPlanArea.setVisibility(visibility);
        viewHolder.mPlanText.setVisibility(visibility);
        viewHolder.mRealArea.setVisibility(visibility);
        viewHolder.mRealText.setVisibility(visibility);
        viewHolder.mDeepNightBreakTime.setVisibility(visibility);
        viewHolder.mWorkContent.setVisibility(visibility);
        viewHolder.mWorkText.setVisibility(visibility);
    }

    private void setDataForLayout(final InputViewHolder viewHolder, DateData data) {
        viewHolder.mPlanAttendance.setTime(data.getPlanAttend());
        viewHolder.mPlanLeaveTime.setTime(data.getPlanLeave());
        viewHolder.mPlanBreakTime.setTime(data.getPlanBreakTime());
        viewHolder.mRealAttendance.setTime(data.getRealAttend());
        viewHolder.mRealLeaveTime.setTime(data.getRealLeave());
        viewHolder.mRealBreakTime.setTime(data.getRealBreakTime());
        viewHolder.mDeepNightBreakTime.setTime(data.getDeepNightBreakTime());
        viewHolder.mWorkContent.setText(data.getWorkContent());
        DateDetailsContainer container = data.getDetailContainer();
        viewHolder.mPaidHolidayCheckBox.setChecked(container.getPaidHolidayFrag());
        viewHolder.mTransferWorkCheckBox.setChecked(container.getTransferWorkFlag());
        viewHolder.mFlexCheckBox.setChecked(container.getFlex());
        viewHolder.mHolidayWorkCheckBox.setChecked(container.getHolidayWorkFlag());
    }

    private void setPaidTimeSpinner(final InputViewHolder viewHolder, DateData data) {
        PaidTimeSpinnerAdapter spinnerAdapter = new PaidTimeSpinnerAdapter(mContext);
        viewHolder.mPaidTimeSpinner.setAdapter(spinnerAdapter.getSpinnerAdapter());
        viewHolder.mPaidTimeSpinner.setSelection(Integer.valueOf(
                data.getDetailContainer().getPaidTime()));
    }

    private void setViewListener(final InputViewHolder viewHolder) {
        viewHolder.mPlanAttendance.setListener(new TimerPickerLinearLayout.OnTimeChangeListener() {
            @Override
            public void OnTimeChanged(String time) {
                if (!time.isEmpty()) {
                    DateData data = mJunpouDateList.get(viewHolder.getAdapterPosition());
                    data.setPlanAttend(time);
                }
            }
        });

        viewHolder.mPlanLeaveTime.setListener(new TimerPickerLinearLayout.OnTimeChangeListener() {
            @Override
            public void OnTimeChanged(String time) {
                if (!time.isEmpty()) {
                    DateData data = mJunpouDateList.get(viewHolder.getAdapterPosition());
                    data.setPlanLeave(time);
                }
            }
        });

        viewHolder.mPlanBreakTime.setListener(new TimerPickerLinearLayout.OnTimeChangeListener() {
            @Override
            public void OnTimeChanged(String time) {
                if (!time.isEmpty()) {
                    DateData data = mJunpouDateList.get(viewHolder.getAdapterPosition());
                    data.setPlanBreakTime(time);
                }
            }
        });

        viewHolder.mRealAttendance.setListener(new TimerPickerLinearLayout.OnTimeChangeListener() {
            @Override
            public void OnTimeChanged(String time) {
                if (!time.isEmpty()) {
                    DateData data = mJunpouDateList.get(viewHolder.getAdapterPosition());
                    data.setRealAttend(time);
                }
            }
        });

        viewHolder.mRealLeaveTime.setListener(new TimerPickerLinearLayout.OnTimeChangeListener() {
            @Override
            public void OnTimeChanged(String time) {
                if (!time.isEmpty()) {
                    DateData data = mJunpouDateList.get(viewHolder.getAdapterPosition());
                    data.setRealLeave(time);
                }
            }
        });

        viewHolder.mRealBreakTime.setListener(new TimerPickerLinearLayout.OnTimeChangeListener() {
            @Override
            public void OnTimeChanged(String time) {
                if (!time.isEmpty()) {
                    DateData data = mJunpouDateList.get(viewHolder.getAdapterPosition());
                    data.setRealBreakTime(time);
                }
            }
        });

        viewHolder.mDeepNightBreakTime.setListener(new TimerPickerLinearLayout.OnTimeChangeListener() {
            @Override
            public void OnTimeChanged(String time) {
                if (!time.isEmpty()) {
                    DateData data = mJunpouDateList.get(viewHolder.getAdapterPosition());
                    data.setDeepNightBreakTime(time);
                }
            }
        });

        viewHolder.mWorkContent.addTextChangedListener(new TextWatcherImpl() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s != null) {
                    String text = s.toString();
                    if (!text.isEmpty()) {
                        DateData data = mJunpouDateList.get(viewHolder.getAdapterPosition());
                        data.setWorkContent(s.toString());
                    }
                }
            }
        });

        viewHolder.mPaidHolidayCheckBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        DateData data = mJunpouDateList.get(viewHolder.getAdapterPosition());
                        data.getDetailContainer().setPaidHolidayFlag(isChecked);
                        if (isChecked) {
                            changeVisibility(viewHolder, View.GONE);
                        } else {
                            changeVisibility(viewHolder, View.VISIBLE);
                        }
                    }
                });

        viewHolder.mTransferWorkCheckBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        DateData data = mJunpouDateList.get(viewHolder.getAdapterPosition());
                        data.getDetailContainer().setTransferWorkFrag(isChecked);
                        int visibility = data.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
                        if (isChecked) {
                            changeVisibility(viewHolder, visibility);
                        } else {
                            changeVisibility(viewHolder, data.getVisibility());
                        }
                    }
                });

        viewHolder.mFlexCheckBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        DateData data = mJunpouDateList.get(viewHolder.getAdapterPosition());
                        data.getDetailContainer().setFlex(isChecked);
                    }
                });

        viewHolder.mHolidayWorkCheckBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        DateData data = mJunpouDateList.get(viewHolder.getAdapterPosition());
                        data.getDetailContainer().setHolidayWorkFlag(isChecked);
                        int visibility = data.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
                        if (isChecked) {
                            changeVisibility(viewHolder, visibility);
                        } else {
                            changeVisibility(viewHolder, data.getVisibility());
                        }
                    }
                });

        viewHolder.mPaidTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DateData data = mJunpouDateList.get(viewHolder.getAdapterPosition());
                data.getDetailContainer().setPaidTime((String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private class PaidTimeSpinnerAdapter {

        private final Context mContext;

        public PaidTimeSpinnerAdapter(@NonNull Context context) {
            mContext = context;
        }

        ArrayAdapter getSpinnerAdapter() {
            ArrayAdapter adapter = new ArrayAdapter(mContext, R.layout.paid_time_spinner_layout);

            String[] stringArray = mContext.getResources().getStringArray(R.array.paid_time_array);
            List<String> spinnerList = Arrays.asList(stringArray);
            for (String item : spinnerList) {
                adapter.add(item);
            }

            adapter.setDropDownViewResource(R.layout.paid_time_spinner_layout);

            return adapter;
        }
    }

}
