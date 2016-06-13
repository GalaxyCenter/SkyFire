package apollo.tianya.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by kuibo on 2016/6/13.
 */
public class DateTime {
    /**
     *
     */
    private static final long serialVersionUID = -6022894128699751840L;

    private Calendar calender;

    public static final DateTime MinValue = new DateTime(0);

    public DateTime() {
        this(2000, 0, 1, 0, 0, 0);
    }

    public DateTime(Date time) {
        this.calender = Calendar.getInstance();
        this.calender.setTime(time);
    }

    public DateTime(int year, int month, int date) {
        this(year, month, date, 0, 0, 0);
    }

    public DateTime(long t) {
        this.calender = Calendar.getInstance();
        this.calender.setTimeInMillis(t);
    }

    public DateTime(int year, int month, int date, int hourOfDay, int minute,
                    int second) {
        this.calender = Calendar.getInstance();
        this.calender.set(year, month, date, hourOfDay, minute, second);
    }

    public static DateTime now() {
        return new DateTime(new Date());
    }

    public static long diff(DateTime t1, DateTime t2) {
        return diff(t1.getDate(), t2.getDate());
    }

    public static long diff(Date t1, Date t2) {
        return t1.getTime() - t2.getTime();
    }

    public static DateTime parse(String time) {
        return parse(time, "yyyy-MM-dd HH:mm:ss");
    }

    public static DateTime parse(String time, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date date;
        try {
            date = format.parse(time);
        } catch (ParseException ex) {
            throw new IllegalArgumentException("DateTime parse error"
                    + ex.getMessage());
        }
        return new DateTime(date);
    }

    public DateTime addYears(int amount) {
        return add(Calendar.YEAR, amount);
    }

    public DateTime addMonths(int amount) {
        return add(Calendar.MONTH, amount);
    }

    public DateTime addWeeks(int amount) {
        return add(Calendar.WEEK_OF_YEAR, amount);
    }

    public DateTime addDays(int amount) {
        return add(Calendar.DAY_OF_MONTH, amount);
    }

    public DateTime addHours(int amount) {
        return add(Calendar.HOUR_OF_DAY, amount);
    }

    public DateTime addMinutes(int amount) {
        return add(Calendar.MINUTE, amount);
    }

    public DateTime addSeconds(int amount) {
        return add(Calendar.SECOND, amount);
    }

    public DateTime addMilliseconds(int amount) {
        return add(Calendar.MILLISECOND, amount);
    }

    private DateTime add(int calendarField, int amount) {
        Calendar c = Calendar.getInstance();
        c.setTime(this.calender.getTime());
        c.add(calendarField, amount);
        return new DateTime(c.getTime());
    }

    public Date getDate() {
        return calender.getTime();
    }

    public long getTime() {
        return getDate().getTime();
    }

    public int getYear() {
        return calender.get(Calendar.YEAR);
    }

    public int getMonth() {
        return calender.get(Calendar.MONTH);
    }

    public int getDay() {
        return calender.get(Calendar.DAY_OF_MONTH);
    }

    public int getWeekOfMonth() {
        return calender.get(Calendar.WEEK_OF_MONTH);
    }

    public int getWeekOfYear() {
        return calender.get(Calendar.WEEK_OF_YEAR);
    }

    public int getDayOfYear() {
        return calender.get(Calendar.DAY_OF_YEAR);
    }

    public int getDayOfWeek() {
        return calender.get(Calendar.DAY_OF_WEEK);
    }

    public int getHour() {
        return calender.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinute() {
        return calender.get(Calendar.MINUTE);
    }

    public int getSecond() {
        return calender.get(Calendar.SECOND);
    }

    public int getMillisecond() {
        return calender.get(Calendar.MILLISECOND);
    }

    public boolean after(DateTime other) {
        return getDate().after(other.getDate());
    }

    public boolean between(DateTime start, DateTime end) {
        return this.getDate().after(start.getDate()) && end.after(this);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof DateTime)) {
            return false;
        }
        DateTime other = (DateTime) obj;
        return this.getDate().equals(other.getDate());
    }

    public String toString() {
        return toString("yyyy-MM-dd HH:mm:ss");
    }

    public String toString(String pattern) {
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern(pattern);

        return format.format(calender.getTime());
    }
}