package fukuchi.junpou.Fragment;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import fukuchi.junpou.DataBase.DataDetailDbWriteHelper;
import fukuchi.junpou.DataBase.InputValueDbWriteHelper;
import fukuchi.junpou.View.InputViewAdapter;
import fukuchi.junpou.Model.DateDetailsContainer;
import fukuchi.junpou.R;
import fukuchi.junpou.View.RecyclerViewDecoration;
import fukuchi.junpou.Model.DateData;
import fukuchi.junpou.Util.JunpouPattern;

public class InputViewFragment extends Fragment {

    public RecyclerView mInputRecyclerView;
    private InputViewAdapter mInputViewAdapter;
    private InputValueDbWriteHelper mWriterHelper;
    private DataDetailDbWriteHelper mDetailWriteHelper;
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

    private final View.OnTouchListener mTouchListener =
            new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // Saved data when spinner touched.
                    UpsertDataTask task = new UpsertDataTask();
                    task.execute();
                    return false;
                }
            };

    private JunpouPattern.OnDataQueryListener mDataQueryListener =
            new JunpouPattern.OnDataQueryListener() {
                @Override
                public void onDataQueryFinished(List<DateData> data) {
                    mInputViewAdapter = new InputViewAdapter(mContext, data);
                    mInputRecyclerView.setAdapter(mInputViewAdapter);
                }
            };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext().getApplicationContext();
        mWriterHelper = new InputValueDbWriteHelper(mContext);
        mDetailWriteHelper = new DataDetailDbWriteHelper(mContext);
        mPattern = new JunpouPattern(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView != null) {
            return mView;
        }

        mView = inflater.inflate(R.layout.input_fragment_layout, container, false);

        mInputRecyclerView = (RecyclerView) mView.findViewById(R.id.input_recycler_view);
        mInputRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mInputRecyclerView.addItemDecoration(new RecyclerViewDecoration(mContext));

        Spinner mSeasonSpinner = (Spinner) mView.findViewById(R.id.season_spinner);

        TreeSet<String> seasonArray = mWriterHelper.seasonQuery();
        SeasonSpinnerAdapter seasonSpinnerAdapter =
                new SeasonSpinnerAdapter(getContext().getApplicationContext(), seasonArray);
        mSeasonSpinner.setAdapter(seasonSpinnerAdapter.getSpinnerAdapter());
        mSeasonSpinner.setSelection(seasonArray.size() - 1);
        mSeasonSpinner.setOnTouchListener(mTouchListener);

        mSeasonSpinner.setOnItemSelectedListener(mSelectedListener);

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        createInputRecyclerView(-1, -1, new ArrayList<Integer>());
    }

    @Override
    public void onPause() {
        super.onPause();

        new UpsertDataTask().execute();
    }

    private void createInputRecyclerView(int year, int month, List<Integer> targetDayList) {
        if (year == -1 || month == -1 || targetDayList.isEmpty()) {
            mPattern.getDateData(mContext, mDataQueryListener);
        } else {
            mPattern.getDateData(mContext, year, month, targetDayList, mDataQueryListener);
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

        for (int i = 0; i < mInputViewAdapter.getItemCount(); i++) {
            res.add(mInputViewAdapter.getDateData(i));
        }
        return res;
    }

    //2016年 12月 3旬
    private void selectedSpinnerHandler(String spinnerItem) {
        int year = Integer.parseInt(spinnerItem.substring(0, spinnerItem.indexOf("年")));
        int month = Integer.parseInt(spinnerItem.substring(spinnerItem.indexOf("年") + 2,
                spinnerItem.indexOf("月")));
        int season = Integer.parseInt(spinnerItem.substring(spinnerItem.indexOf("月") + 2,
                spinnerItem.indexOf("旬")));

        createInputRecyclerView(year, month,
                mPattern.getDayListFromSeason(mPattern.convertSeasonInt(season)));
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

    private class SeasonSpinnerAdapter {

        private final Context mContext;
        private final TreeSet<String> mSpinnerItemSet;

        public SeasonSpinnerAdapter(@NonNull Context context, TreeSet<String> spinnerItemSet) {
            mContext = context;
            mSpinnerItemSet = spinnerItemSet;
        }

        ArrayAdapter getSpinnerAdapter() {
            ArrayAdapter adapter = new ArrayAdapter(mContext, R.layout.spinner_layout);
            for (String spinnerItem : mSpinnerItemSet) {
                adapter.add(spinnerItem);
            }
            adapter.setDropDownViewResource(R.layout.spinner_list_layout);
            return adapter;
        }
    }
}
