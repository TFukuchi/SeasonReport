package fukuchi.junpou.Activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import fukuchi.junpou.BasicInfo.BasicInfoActivity;
import fukuchi.junpou.DataBase.InputValueDbOpenHelper;
import fukuchi.junpou.Fragment.CompleteDialogFragment;
import fukuchi.junpou.Fragment.InputViewFragment;
import fukuchi.junpou.Excel.ExcelOutput;
import fukuchi.junpou.Model.JunpouPattern;
import fukuchi.junpou.R;

public class MainActivity extends AppCompatActivity {

    private JunpouPattern mPattern = null;

    private CompleteDialogFragment.DialogListener mDialogListener =
            new CompleteDialogFragment.DialogListener() {
        @Override
        public void onPreview() {
            ExcelOutput output = new ExcelOutput(getApplicationContext(),
                    mPattern.getDateData(getApplicationContext()));
            output.outputTest();
        }

        @Override
        public void onSendMail() {
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (toolbar != null) {
                toolbar.setElevation(0);
            }
        }

        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            InputViewFragment fragment = new InputViewFragment();
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

        InputValueDbOpenHelper dbOpenHelper = new InputValueDbOpenHelper(this);
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        db.close();
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
}
