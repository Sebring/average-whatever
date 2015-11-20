package se.sebring.avgwhat;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Sequence extends BaseObservable implements Serializable, Comparable<Sequence> {
    public static final String KEY = "sequence";
    public static final double MILLIS_DAY = 86400000D;

    private int count;
    private Date startDate;
    private Date endDate;
    private String dateFormat;
    private String name;
    private int period;
    private int goal;
    private int id;

    public Sequence() {
        this("");
    }

    public Sequence(String name) {
        this.name = name;
        this.startDate = new Date();
        this.id = startDate.hashCode();
        count = 0;
        dateFormat = "yy.MM.dd";
        period = Calendar.DAY_OF_YEAR;
        goal = 0;
    }

    @Bindable
    public String getCount() {
        Log.v(KEY, "getCount:" + count);
        return String.valueOf(count);
    }

    public int incrementCount(int increment) {
        count += increment;
        notifyPropertyChanged(se.sebring.avgwhat.BR.count);
        notifyPropertyChanged(se.sebring.avgwhat.BR.average);
        notifyPropertyChanged(se.sebring.avgwhat.BR.reach);
        return count;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Sequence))
            return false;
        if (this.id == ((Sequence) other).id)
            return true;
        return false;
    }

    @Override
    public int compareTo(Sequence another) {
        return Integer.compare(this.id, another.getId());
    }

    @Bindable
    public String getAverage() {

        int d = 0;
        switch (period) {
            case Calendar.DAY_OF_YEAR:
                d = getDeltaDays();
                break;
        }
        if (d < 2 || count == 0) {
            return "" + count;
        }
        int a = (int) Math.floor(count/getDeltaDays());
        return "" + a;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
        notifyPropertyChanged(se.sebring.avgwhat.BR.average);
        notifyPropertyChanged(se.sebring.avgwhat.BR.reach);
        notifyPropertyChanged(se.sebring.avgwhat.BR.deltaDays);
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Bindable
    public int getDeltaDays() {
        return getDaysInterval();
    }

    public int getDaysInterval() {
        final DateTime start = new DateTime(startDate);
        final DateTime end = new DateTime();

        start.minusHours(start.getHourOfDay());
        start.minusMinutes(start.getMinuteOfDay());
        return 1 + Days.daysBetween(start, end).getDays();
    }

    public String getFormattedStartDay() {
        return new SimpleDateFormat(dateFormat, Locale.ENGLISH).format(startDate);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(se.sebring.avgwhat.BR.name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Bindable
    public String getGoal() {
        return String.valueOf(goal);
    }

    public void setGoal(int goal) {
        this.goal = goal;
        notifyPropertyChanged(se.sebring.avgwhat.BR.goal);
        notifyPropertyChanged(se.sebring.avgwhat.BR.reach);
    }

    @Bindable
    public String getReach() {
        int c = count;
        int d = getDeltaDays();
        int g = goal;

        return String.valueOf(g*d-c);
    }

    public int getId() {
        return this.id;
    }
}
