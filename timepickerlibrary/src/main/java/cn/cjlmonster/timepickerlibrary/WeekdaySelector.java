package cn.cjlmonster.timepickerlibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CJL on 2020/7/17 13:08.
 */
public class WeekdaySelector extends LinearLayout {

    public WeekdaySelector(Context context) {
        this(context, null);
    }

    public WeekdaySelector(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private String[] weekdays;
    private CheckBox[] checkBoxes;

    private void init(Context context) {
        setOrientation(HORIZONTAL);

        weekdays = getResources().getStringArray(R.array.weekdays);
        String[] weekdays_simplify = getResources().getStringArray(R.array.weekdays_simplify);

        LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
        FrameLayout.LayoutParams paramsFrame = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        paramsFrame.gravity = Gravity.CENTER;

        checkBoxes = new CheckBox[weekdays_simplify.length];

        for (int i = 0;i < checkBoxes.length;i++) {
            checkBoxes[i] = new CheckBox(context);
            checkBoxes[i].setButtonDrawable(null);
            checkBoxes[i].setGravity(Gravity.CENTER);
            checkBoxes[i].setBackgroundResource(R.drawable.selector_checkbox_style);
            checkBoxes[i].setTextColor(ContextCompat.getColorStateList(context, R.color.select_weekday_text));
            checkBoxes[i].setText(weekdays_simplify[i]);

            FrameLayout frameLayout = new FrameLayout(context);
            frameLayout.addView(checkBoxes[i], paramsFrame);
            addView(frameLayout, params);
        }
    }

    public void reset() {
        for (CheckBox checkBox : checkBoxes) checkBox.setChecked(false);
    }

    public boolean hasSelected() {
        for (CheckBox checkBox : checkBoxes) {
            if (checkBox.isChecked()) return true;
        }
        return false;
    }

    public List<Integer> getSelectedList() {
        List<Integer> mList = new ArrayList<>();
        for (int i = 0; i < checkBoxes.length; i++) {
            if (checkBoxes[i].isChecked()) mList.add(i);
        }
        return mList;
    }

    public List<String> getSelectedListParams() {
        List<String> mList = new ArrayList<>();
        for (int i = 0; i < checkBoxes.length; i++) {
            if (checkBoxes[i].isChecked()) mList.add(String.format("0%s", i));
        }
        return mList;
    }

    public String formatWeekdays(List<Integer> mList) {
        StringBuilder sb = new StringBuilder(" [ ");
        for (int i = 0; i  < mList.size(); i++) {
            sb.append(weekdays[mList.get(i)]).append(" ");
        }
        sb.append("] ");
        return sb.toString();
    }
}
