package cn.cjlmonster.timepickerlibrary;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.BasePickerView;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.contrarywind.view.WheelView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by CJL on 2020/7/16 9:25.
 */
public class WheelSelector {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);

    public static String getDateString() {
        return sdf.format(Calendar.getInstance().getTime());
    }

    public interface OnOptionsSelected {
        void onSelected(int position, String item);
    }

    public interface OnTimeSelected {
        void onSelected(Date date, String dateString);
    }

    public static void createOptionsPicker(Context context, String title, final List<String> items, final OnOptionsSelected listener) {
        OptionsPickerView<String> pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3 , View v) {
                listener.onSelected(options1, items.get(options1));
            }
        })
                .setTitleText(title)
                .setSubmitText(context.getString(R.string.confirm))
                .setCancelText(context.getString(R.string.cancel))
                .isDialog(true)
                .build();
        setDialogGravity(pvOptions);
        pvOptions.setNPicker(items, null, null);
        pvOptions.show();
    }

    private static Calendar getCurrentCalendar(String dateString) {
        try {
            Log.i("时间参数", "getCurrentCalendar: " + dateString);
            Calendar calendar = Calendar.getInstance(Locale.CHINA);
            calendar.setTime(sdf.parse(dateString));
            return calendar;
        } catch (Exception e) {
            Log.e("测试日志", "getCalendar: ", e);
        }
        return Calendar.getInstance(Locale.CHINA);
    }

    public static void createTimePicker(Context context, String title, String dateString, final OnTimeSelected listener) {
        String[] labels = context.getResources().getStringArray(R.array.calendar_label);
        TimePickerView pvTime = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                listener.onSelected(date, sdf.format(date));
            }
        })
                .setTitleText(title) // 标题文字
                .setSubmitText(context.getString(R.string.confirm))
                .setCancelText(context.getString(R.string.cancel))
                .setType(new boolean[]{true, true, true, true, true, false}) // 默认全部显示
                .isCyclic(false) // 是否循环滚动
                .isCenterLabel(true) // 是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(true) // 是否显示为对话框样式
                .setLineSpacingMultiplier(3)
                .setDividerType(WheelView.DividerType.WRAP)
                .setLabel(labels[0], labels[1], labels[2], labels[3], labels[4], labels[5])
                .setContentTextSize(16)
                .setDate(getCurrentCalendar(dateString))
                .build();
        setDialogGravity(pvTime);
        pvTime.show();
    }

    public static boolean compareDate(String date1, String date2) {
        try {
            return sdf.parse(date1).compareTo(sdf.parse(date2)) >= 0;
        } catch (Exception e) {
            Log.e("测试日志", "compareDate: ", e);
        }
        return false;
    }

    private static final SimpleDateFormat sdf_HM = new SimpleDateFormat("HHmm", Locale.CHINA);

    public static boolean compareDate1(String date1, String date2) {
        try {
            long time1 = sdf.parse(date1).getTime();
            long time2 = sdf.parse(date2).getTime();
            long c = time2 - time1 - ONE_DAY;
            if (c <= 0) return true;
            int hm1 = Integer.parseInt(sdf_HM.format(time1));
            int hm2 = Integer.parseInt(sdf_HM.format(time2));
            return hm1 >= hm2;
        } catch (Exception e) {
            Log.e("测试日志", "compareDate: ", e);
        }
        return false;
    }

    public static long getTimestamp(String date) {
        try {
            return sdf.parse(date).getTime();
        } catch (Exception e) {
            Log.e("测试日志", "getTimestamp: ", e);
        }
        return -1;
    }

    private static final long ONE_DAY = 24 * 60 * 60 * 1000;

    public static List<Integer> checkWeekday(String validTime, String invalidTime, List<Integer> weekdays) {
        try {
            List<Integer> mList = new ArrayList<>();

            Date date1 = sdf.parse(validTime);
            Date date2 = sdf.parse(invalidTime);

            int d1 = (int) (date1.getTime() / ONE_DAY);
            int d2 = (int) (date2.getTime() / ONE_DAY);
            int c = d2 - d1 + 1;
            if (c >= 7) return mList;

            int day1 = date1.getDay();
            int day2 = date2.getDay();

            if (day1 == day2) {
                for (int day : weekdays) {
                    if (day != day1) mList.add(day);
                }
                return mList;
            } else if(day1 < day2) {
                for (int day : weekdays) {
                    if (day < day1 || day > day2) mList.add(day);
                }
                return mList;
            } else {
                for (int day : weekdays) {
                    if(day > day2 && day < day1) mList.add(day);
                }
                return mList;
            }
        } catch (Exception e) {
            Log.e("测试日志", "checkWeekday: ", e);
        }
        return null;
    }

    private static void setDialogGravity(BasePickerView pickerView) {
        Dialog mDialog = pickerView.getDialog();
        if (mDialog == null) return;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.leftMargin = 0;
        params.rightMargin = 0;
        pickerView.getDialogContainerLayout().setLayoutParams(params);

        Window dialogWindow = mDialog.getWindow();
        if (dialogWindow == null) return;
        dialogWindow.getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.getAttributes().height = WindowManager.LayoutParams.WRAP_CONTENT;
        // 修改动画样式
        dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);
        // 改成Bottom，底部显示
        dialogWindow.setGravity(Gravity.BOTTOM);
    }
}
