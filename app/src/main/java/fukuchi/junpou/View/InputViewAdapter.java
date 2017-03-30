package fukuchi.junpou.View;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fukuchi.junpou.Activity.DataInputActivity;
import fukuchi.junpou.Model.DateData;
import fukuchi.junpou.R;

public class InputViewAdapter extends RecyclerView.Adapter<InputViewHolderBase> {

    private static final int VIEW_LIST = 1;
    private final LayoutInflater mInflater;
    private final List<DateData> mJunpouDateList;

    private Map<String, Integer> mTagMap = new HashMap<>();
    private Context mContext;
    private List<Integer> mInitFinPosition = new ArrayList<>();

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
    public InputViewHolderBase onCreateViewHolder(ViewGroup container, int viewType) {
        InputViewHolderBase holder = null;
        switch (viewType) {
            case VIEW_LIST:
                View listView = mInflater.inflate(R.layout.input_view_list, container, false);
                holder = new InputViewListHolder(listView);
                listView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContext.startActivity(new Intent(mContext, DataInputActivity.class));
                    }
                });
                break;
            default:
                break;
        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_LIST;
    }

    @Override
    public void onBindViewHolder(final InputViewHolderBase holder, final int position) {
        if (mJunpouDateList != null) {
            if (holder instanceof InputViewHolder) {

                int target = holder.getAdapterPosition();
                DateData data = mJunpouDateList.get(target);
                ((InputViewHolder) holder).setVisibility(data);
                ((InputViewHolder) holder).setDataForLayout(data);
                if (!mInitFinPosition.contains(target)) {
                    mInitFinPosition.add(target);
                    setPaidTimeSpinner((InputViewHolder) holder, data);
                }
            } else if (holder instanceof InputViewListHolder) {
                int target = holder.getAdapterPosition();
                DateData data = mJunpouDateList.get(target);
                ((InputViewListHolder) holder).setData(data);
            }
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

    private void setPaidTimeSpinner(final InputViewHolder viewHolder, DateData data) {
        PaidTimeSpinnerAdapter spinnerAdapter = new PaidTimeSpinnerAdapter(mContext);
        viewHolder.mPaidTimeSpinner.setAdapter(spinnerAdapter.getSpinnerAdapter());
        viewHolder.mPaidTimeSpinner.setSelection(Integer.valueOf(
                data.getDetailContainer().getPaidTime()));
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

            adapter.setDropDownViewResource(R.layout.paid_time_spinner_list_layout);

            return adapter;
        }
    }

}
