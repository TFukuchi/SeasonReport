package fukuchi.junpou.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TreeSet;

import fukuchi.junpou.DataBase.InputValueDbWriteHelper;
import fukuchi.junpou.View.InputViewAdapter;
import fukuchi.junpou.R;
import fukuchi.junpou.View.RecyclerViewDecoration;
import fukuchi.junpou.Model.DateData;
import fukuchi.junpou.Util.JunpouPattern;

public class DataListViewFragment extends Fragment {

    public RecyclerView mDataListRecyclerView;
    private InputValueDbWriteHelper mWriterHelper;
    private View mView;
    private JunpouPattern mPattern;
    private Context mContext;

    private final AdapterView.OnItemSelectedListener mSelectedListener =
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedSpinnerHandler((String) parent.getItemAtPosition(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            };

    private JunpouPattern.OnDataQueryListener mDataQueryListener =
            new JunpouPattern.OnDataQueryListener() {
                @Override
                public void onDataQueryFinished(List<DateData> data) {
                    mDataListRecyclerView.setAdapter(new InputViewAdapter(mContext, data));
                }
            };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext().getApplicationContext();
        mWriterHelper = new InputValueDbWriteHelper(mContext);
        mPattern = new JunpouPattern(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView != null) {
            return mView;
        }

        mView = inflater.inflate(R.layout.input_fragment_layout, container, false);

        mDataListRecyclerView = (RecyclerView) mView.findViewById(R.id.input_recycler_view);
        mDataListRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mDataListRecyclerView.addItemDecoration(new RecyclerViewDecoration(mContext));

        Spinner mSeasonSpinner = (Spinner) mView.findViewById(R.id.season_spinner);

        TreeSet<String> seasonArray = mWriterHelper.seasonQuery();

        String current = getCurrentSeason();
        if (!seasonArray.contains(current)) {
            seasonArray.add(current);
        }

        SeasonSpinnerAdapter seasonSpinnerAdapter =
                new SeasonSpinnerAdapter(getContext().getApplicationContext(), seasonArray);
        mSeasonSpinner.setAdapter(seasonSpinnerAdapter.getSpinnerAdapter());
        mSeasonSpinner.setSelection(seasonArray.size() - 1);

        mSeasonSpinner.setOnItemSelectedListener(mSelectedListener);

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        createInputRecyclerView(-1, -1, new ArrayList<Integer>());
    }

    private String getCurrentSeason() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);

        int season = mPattern.getSeasonInteger(day);

        StringBuilder builder = new StringBuilder();
        builder.append(year);
        builder.append("年 ");
        builder.append(month);
        builder.append("月 ");
        builder.append(season);
        builder.append("旬");

        return builder.toString();
    }

    private void createInputRecyclerView(int year, int month, List<Integer> targetDayList) {
        if (year == -1 || month == -1 || targetDayList.isEmpty()) {
            mPattern.getDateData(mDataQueryListener);
        } else {
            mPattern.getDateData(year, month, targetDayList, mDataQueryListener);
        }
    }

    private void selectedSpinnerHandler(String spinnerItem) {
        int year = Integer.parseInt(spinnerItem.substring(0, spinnerItem.indexOf("年")));
        int month = Integer.parseInt(spinnerItem.substring(spinnerItem.indexOf("年") + 2,
                spinnerItem.indexOf("月")));
        int season = Integer.parseInt(spinnerItem.substring(spinnerItem.indexOf("月") + 2,
                spinnerItem.indexOf("旬")));

        createInputRecyclerView(year, month,
                mPattern.getDayListFromSeason(mPattern.convertSeasonInt(season)));
    }

    private class SeasonSpinnerAdapter {

        private final Context mContext;
        private final TreeSet<String> mSpinnerItemSet;

        SeasonSpinnerAdapter(@NonNull Context context, TreeSet<String> spinnerItemSet) {
            mContext = context;
            mSpinnerItemSet = spinnerItemSet;
        }

        ArrayAdapter getSpinnerAdapter() {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, R.layout.spinner_layout);
            for (String spinnerItem : mSpinnerItemSet) {
                adapter.add(spinnerItem);
            }
            adapter.setDropDownViewResource(R.layout.spinner_list_layout);
            return adapter;
        }
    }
}
