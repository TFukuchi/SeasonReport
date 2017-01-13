package fukuchi.junpou.Util;


import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import fukuchi.junpou.Model.JunpouPattern;

public class JunpouUtil {

    private final static int ONE_HOUR_MINUTE = 60;

    public static String getTotalWorkingTime(String attend, String leave, String breakTime, String paidTime) {
        if (attend == null || attend.isEmpty()) {
            return "";
        }
        if (leave == null || leave.isEmpty()) {
            return "";
        }
        if (breakTime == null || breakTime.isEmpty()) {
            return "";
        }
        int attendHour = Integer.parseInt(attend.substring(0, attend.indexOf(":")));
        int leaveHour = Integer.parseInt(leave.substring(0, leave.indexOf(":")));
        if (leaveHour > 22) {
            leaveHour = 22;
        }

        int baseWorkingHour = leaveHour - attendHour;

        int attendMinute = Integer.parseInt(attend.substring(attend.indexOf(":") + 1, attend.length()));
        int leaveMinute = Integer.parseInt(leave.substring(leave.indexOf(":") + 1, leave.length()));

        if (leaveHour >= 22 && leaveMinute != 0) {
            leaveMinute = 0;
        }

        int baseWorkingMinute = leaveMinute - attendMinute;

        if (baseWorkingMinute < 0) {
            baseWorkingHour--;
            baseWorkingMinute = ONE_HOUR_MINUTE + baseWorkingMinute;
        }

        int breakTimeHour = Integer.parseInt(breakTime.substring(0, breakTime.indexOf(":")));
        int breakTimeMinute = Integer.parseInt(breakTime.substring(breakTime.indexOf(":") + 1, breakTime.length()));

        baseWorkingHour -= breakTimeHour;
        baseWorkingMinute -= breakTimeMinute;

        if (baseWorkingMinute < 0) {
            baseWorkingHour--;
            baseWorkingMinute = ONE_HOUR_MINUTE + baseWorkingMinute;
        }

        if (!paidTime.isEmpty()) {
            baseWorkingHour += Integer.valueOf(paidTime);
        }

        StringBuilder builder = new StringBuilder();
        builder.append(baseWorkingHour);
        builder.append(":");
        builder.append(baseWorkingMinute);
        if (baseWorkingMinute == 0) {
            builder.append(0);
        }

        return builder.toString();
    }

    public static String calSumTime(String base, String current) {
        if (base == null || base.isEmpty()) {
            return "";
        }
        if (current == null || current.isEmpty()) {
            return "";
        }
        int baseHour = Integer.parseInt(base.substring(0, base.indexOf(":")));
        int currentHour = Integer.parseInt(current.substring(0, current.indexOf(":")));

        int workingHour = baseHour + currentHour;

        int baseMinute = Integer.parseInt(base.substring(base.indexOf(":") + 1, base.length()));
        int currentMinute = Integer.parseInt(current.substring(current.indexOf(":") + 1, current.length()));

        int workingMinute = baseMinute + currentMinute;

        while (workingMinute >= 60) {
            workingHour++;
            workingMinute = workingMinute - ONE_HOUR_MINUTE;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(workingHour);
        builder.append(":");
        builder.append(workingMinute);
        if (workingMinute == 0) {
            builder.append(0);
        }

        return builder.toString();
    }

    //引き算
    public static String calDiffTime(String breakTime, String workTime) {
        if (breakTime == null || breakTime.isEmpty()) {
            return "";
        }
        if (workTime == null || workTime.isEmpty()) {
            return "";
        }
        int breakHour = Integer.parseInt(breakTime.substring(0, breakTime.indexOf(":")));
        int breakMinute = Integer.parseInt(breakTime.substring(breakTime.indexOf(":") + 1, breakTime.length()));

        int workHour = Integer.parseInt(workTime.substring(0, workTime.indexOf(":")));
        int workMinute = Integer.parseInt(workTime.substring(workTime.indexOf(":") + 1, workTime.length()));

        int targetHour = workHour - breakHour;

        int targetMinute = 0;
        if(workMinute > breakMinute){
            targetMinute = workMinute - breakMinute;
        }else if(workMinute < breakMinute){
            targetHour--;
            int hoge = breakMinute - workMinute;
            targetMinute = 60 - hoge;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(targetHour);
        builder.append(":");
        builder.append(targetMinute);
        if (targetMinute == 0) {
            builder.append(0);
        }

        return builder.toString();
    }

    public static String calLateOrEarlyTime(String planAttend, String planLeaves,
                                            String realAttend, String realLeaves) {
        if (planAttend == null || planAttend.isEmpty()) {
            return "";
        }
        if (planLeaves == null || planLeaves.isEmpty()) {
            return "";
        }
        if (realAttend == null || realAttend.isEmpty()) {
            return "";
        }
        if (realLeaves == null || realLeaves.isEmpty()) {
            return "";
        }

        if (planAttend.equals(realAttend) && planLeaves.equals(realLeaves)) {
            return "";
        }

        int targetMinute = 0;

        int planAttendHour = Integer.parseInt(planAttend.substring(0, planAttend.indexOf(":")));
        int realAttendHour = Integer.parseInt(realAttend.substring(0, realAttend.indexOf(":")));

        int planLeaveHour = Integer.parseInt(planLeaves.substring(0, planLeaves.indexOf(":")));
        int realLeaveHour = Integer.parseInt(realLeaves.substring(0, realLeaves.indexOf(":")));

        int targetHour = (realAttendHour - planAttendHour) + (planLeaveHour - realLeaveHour);

        int planAttendMinute = Integer.parseInt(planAttend.substring(planAttend.indexOf(":") + 1, planAttend.length()));
        int realAttendMinute = Integer.parseInt(realAttend.substring(realAttend.indexOf(":") + 1, realAttend.length()));
        if (planAttendMinute != realAttendMinute) {
            int hoge = 60 - planAttendMinute;
            int piyo = 60 - realAttendMinute;
            if (hoge > piyo) {
                targetMinute = hoge - piyo;
            } else {
                targetMinute = piyo - hoge;
            }
        }

        int planLeaveMinute = Integer.parseInt(planLeaves.substring(planLeaves.indexOf(":") + 1, planLeaves.length()));
        int realLeaveMinute = Integer.parseInt(realLeaves.substring(realLeaves.indexOf(":") + 1, realLeaves.length()));

        if (planLeaveMinute != realLeaveMinute) {
            int hoge = 60 - planLeaveMinute;
            int piyo = 60 - realLeaveMinute;
            if (hoge > piyo) {
                targetMinute += hoge - piyo;
            } else {
                targetMinute += piyo - hoge;
            }
        }

        while (targetMinute >= 60) {
            targetHour++;
            targetMinute -= 60;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(targetHour);
        builder.append(":");
        builder.append(targetMinute);
        if (targetMinute == 0) {
            builder.append(0);
        }

        return builder.toString();
    }

    public static String calDeepWorkingTime(String realLeave) {
        if (realLeave == null || realLeave.isEmpty()) {
            return "";
        }
        int realHour = Integer.parseInt(realLeave.substring(0, realLeave.indexOf(":")));
        int realMinute = Integer.parseInt(realLeave.substring(realLeave.indexOf(":") + 1, realLeave.length()));

        if (realHour < 22) {
            return "";
        } else if (realHour == 22 && realMinute == 0) {
            return "";
        }

        int hour = realHour - 22;
        StringBuilder builder = new StringBuilder();
        builder.append(hour);
        builder.append(":");
        builder.append(realMinute);
        if (realMinute == 0) {
            builder.append(0);
        }

        return builder.toString();
    }

    public static String createId(int year, int month, int day) {
        StringBuilder builder = new StringBuilder();
        builder.append(year);
        builder.append(month);
        builder.append(day);

        return builder.toString();
    }

    public static String createId(String year, String month, String day) {
        StringBuilder builder = new StringBuilder();
        builder.append(year);
        builder.append(month);
        builder.append(day);

        return builder.toString();
    }

    public static String formatSeasonText(String id, String date, JunpouPattern pattern) {
        String year = id.substring(0, 4);
        String month = date.substring(0, date.indexOf("/"));
        String day = date.substring(date.indexOf("/") + 1);

        int season = pattern.getSeasonInteger(Integer.valueOf(day));

        return year + "年 " + month + "月 " + season + "旬";
    }

    public static String booleanToStringConverter(boolean value) {
        return value ? "true" : "false";
    }

    public static boolean stringToBooleanConverter(@NonNull String value) {
        return value.equals("true");
    }
}
