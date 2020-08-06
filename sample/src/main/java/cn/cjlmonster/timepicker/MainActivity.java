package cn.cjlmonster.timepicker;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.Date;

import cn.cjlmonster.timepickerlibrary.WeekdaySelector;
import cn.cjlmonster.timepickerlibrary.WheelSelector;

public class MainActivity extends AppCompatActivity {

    private View layout_container;
    private WeekdaySelector weekdaySelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        layout_container = findViewById(R.id.layout_container);
        weekdaySelector = findViewById(R.id.weekdaySelector);
    }

    private void showMsg(String msg) {
        Snackbar.make(layout_container, msg, Snackbar.LENGTH_SHORT).show();
    }

    public void showWeekdays(View v) {
        String txt = weekdaySelector.formatWeekdays(weekdaySelector.getSelectedList());
        showMsg(txt);
    }

    public void showOptionsPicker(View v) {
        String[] items = {"item1", "item2", "item3"};
        WheelSelector.createOptionsPicker(this, "列表选择器", Arrays.asList(items), new WheelSelector.OnOptionsSelected() {
            @Override
            public void onSelected(int position, String item) {
                showMsg(position + " : " + item);
            }
        });
    }

    public void showTimePicker(View v) {
        WheelSelector.createTimePicker(this, "时间选择器", WheelSelector.getDateString(), new WheelSelector.OnTimeSelected() {
            @Override
            public void onSelected(Date date, String dateString) {
                showMsg(dateString);
            }
        });
    }

}
