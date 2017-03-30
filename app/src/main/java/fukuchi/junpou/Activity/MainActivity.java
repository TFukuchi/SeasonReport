package fukuchi.junpou.Activity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import fukuchi.junpou.Fragment.CompleteDialogFragment;
import fukuchi.junpou.Fragment.DataListViewFragment;
import fukuchi.junpou.Excel.ExcelOutput;
import fukuchi.junpou.Helper.SharedPreferencesHelper;
import fukuchi.junpou.Model.DateData;
import fukuchi.junpou.Util.JunpouPattern;
import fukuchi.junpou.R;

public class MainActivity extends AppCompatActivity {

    private JunpouPattern mPattern = null;
    private SharedPreferencesHelper mPrefHelper;

    private CompleteDialogFragment.DialogListener mDialogListener =
            new CompleteDialogFragment.DialogListener() {
                @Override
                public void onPreview() {
                    mPattern.getDateData(mDataQueryListener);
                }

                @Override
                public void onSendMail() {
                    // TODO
                }
            };

    private JunpouPattern.OnDataQueryListener mDataQueryListener =
            new JunpouPattern.OnDataQueryListener() {
                @Override
                public void onDataQueryFinished(List<DateData> data) {
                    new ExcelOutputAsyncTask(data).execute();
                }
            };

    private View.OnClickListener mFABClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CompleteDialogFragment newFragment = new CompleteDialogFragment();
            newFragment.setListener(mDialogListener);
            newFragment.show(getSupportFragmentManager(), CompleteDialogFragment.DIALOG_TAG);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPattern = new JunpouPattern(getApplicationContext());
        mPrefHelper = new SharedPreferencesHelper(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && toolbar != null) {
            toolbar.setElevation(0);
        }
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            DataListViewFragment fragment = new DataListViewFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, fragment).commit();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(mFABClickListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPrefHelper.getFirstLaunch()) {
            FirstLaunchDialog dialog = new FirstLaunchDialog();
            dialog.setListener(new FirstLaunchDialog.FirstLaunchListener() {
                @Override
                public void onclicked() {
                    startActivity(new Intent(getApplicationContext(), BasicInfoActivity.class));
                    mPrefHelper.setFirstLaunch(false);
                }
            });
            dialog.show(getSupportFragmentManager(), "FirstLaunchDialog");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.basic_info:
                startActivity(new Intent(getApplicationContext(), BasicInfoActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class ExcelOutputAsyncTask extends AsyncTask<Void, Void, String> {

        private final List<DateData> mData;

        ExcelOutputAsyncTask(List<DateData> data) {
            mData = data;
        }

        @Override
        protected String doInBackground(Void... params) {
            ExcelOutput output = new ExcelOutput(getApplicationContext(), mData);
            return output.outputExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(result)), "application/vnd.ms-excel");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(MainActivity.this, "Excel Appが見つかりません", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static class FirstLaunchDialog extends DialogFragment {

        public interface FirstLaunchListener {
            void onclicked();
        }

        private FirstLaunchListener mListener;

        public void setListener(@NonNull FirstLaunchListener listener) {
            mListener = listener;
        }

        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("基本情報の設定を行ってください。" +
                    "全項目の入力が必須です。");
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mListener.onclicked();
                }
            });
            this.setCancelable(false);
            return builder.create();
        }
    }
}
