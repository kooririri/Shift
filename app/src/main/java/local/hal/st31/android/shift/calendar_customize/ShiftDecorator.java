package local.hal.st31.android.shift.calendar_customize;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Calendar;
import java.util.Date;

import local.hal.st31.android.shift.utils.DateUtils;

public class ShiftDecorator implements DayViewDecorator {

    private final Calendar calendar = Calendar.getInstance();

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        Date date = new Date();
        String dateStr = DateUtils.date2String(date, "yyyy-MM-dd");
        Date parse = DateUtils.string2Date(dateStr, "yyyy-MM-dd");
        if (day.getDate().equals(parse)) {
            return true;
        }
        return true;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new CircleBackGroundSpan());
    }
}
