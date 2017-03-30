package fukuchi.junpou.View;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private Context mContext;

    public InputViewAdapter(Context context, List<DateData> data) {
        super();
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mJunpouDateList = data;
        setHasStableIds(true);
    }

    @Override
    public InputViewHolderBase onCreateViewHolder(ViewGroup container, int viewType) {
        final InputViewHolderBase holder;
        switch (viewType) {
            case VIEW_LIST:
                View listView = mInflater.inflate(R.layout.input_view_list, container, false);
                holder = new InputViewListHolder(listView);
                listView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent startIntent = new Intent(mContext, DataInputActivity.class);
                        int targetPosition = holder.getAdapterPosition();
                        DateData data = mJunpouDateList.get(targetPosition);
                        if (data != null) {
                            startIntent.putExtra("TEST_YEAR", data.getJunpouValues().getYear());
                            startIntent.putExtra("TEST_DATE", data.getDay());
                            startIntent.putExtra("TEST_WEEK", data.getDayOfWeek());
                            mContext.startActivity(startIntent);
                        }
                    }
                });
                break;
            default:
                holder = null;
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
        if (mJunpouDateList != null && holder instanceof InputViewListHolder) {
            int target = holder.getAdapterPosition();
            DateData data = mJunpouDateList.get(target);
            ((InputViewListHolder) holder).setData(data);
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
}
