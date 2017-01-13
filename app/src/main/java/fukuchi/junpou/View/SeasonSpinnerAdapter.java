package fukuchi.junpou.View;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.TreeSet;

import fukuchi.junpou.R;

public class SeasonSpinnerAdapter {

    public static ArrayAdapter getSpinnerAdapter(@NonNull Context context, TreeSet<String> spinnerItemSet) {
        ArrayAdapter adapter = new ArrayAdapter(context, R.layout.spinner_layout);
        for (String spinnerItem : spinnerItemSet) {
            adapter.add(spinnerItem);
        }

        adapter.setDropDownViewResource(R.layout.spinner_list_layout);

        return adapter;
    }
}
