package fukuchi.junpou.View;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.Arrays;
import java.util.List;

import fukuchi.junpou.R;

public class PaidTimeSpinnerAdapter {

    public static ArrayAdapter getSpinnerAdapter(@NonNull Context context) {
        ArrayAdapter adapter = new ArrayAdapter(context, R.layout.paid_time_spinner_layout);

        String[] stringArray = context.getResources().getStringArray(R.array.paid_time_array);
        List<String> spinnerList = Arrays.asList(stringArray);
        for (String item : spinnerList) {
            adapter.add(item);
        }

        adapter.setDropDownViewResource(R.layout.paid_time_spinner_layout);

        return adapter;
    }
}
