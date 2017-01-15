package fukuchi.junpou.Excel;

import android.content.Context;
import android.support.annotation.NonNull;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import fukuchi.junpou.Model.BasicInfoData;
import fukuchi.junpou.Model.DateData;
import fukuchi.junpou.Model.DateDetailsContainer;
import fukuchi.junpou.Util.JunpouValues;
import fukuchi.junpou.Util.JunpouUtil;

import static fukuchi.junpou.Util.JunpouUtil.calDeepWorkingTime;
import static fukuchi.junpou.Util.JunpouUtil.calDiffTime;
import static fukuchi.junpou.Util.JunpouUtil.calSumTime;
import static fukuchi.junpou.Util.JunpouUtil.getTotalWorkingTime;

public class ExcelOutput {
    private static final int DATE_TARGET_START_ROW = 8;

    private CellStyle mBorderStyle;
    private final Context mContext;
    private final List<DateData> mDateDataList;

    private final JunpouValues mValues;
    private final BasicInfoData mBasingInfo;

    private final static String SELECTED_COMMENT = "*";
    private final static String EXCEL_EXTENSION = ".xls";

    public ExcelOutput(@NonNull Context context, @NonNull List<DateData> dateDataList) {
        mContext = context;
        mDateDataList = dateDataList;
        mValues = mDateDataList.get(0).getJunpouValues();
        mBasingInfo = new BasicInfoData(context);
    }

    public void outputTest() {
        Workbook workbook = new HSSFWorkbook();
        try {
            StringBuilder fileName = new StringBuilder();
            fileName.append("旬報");
            fileName.append(mValues.getYear());
            fileName.append(mValues.getMonth());
            fileName.append("_");
            fileName.append(mValues.getSeason());
            fileName.append("旬");
            fileName.append("(");
            fileName.append(mBasingInfo.getUserId());
            fileName.append(")");
            fileName.append(EXCEL_EXTENSION);

            FileOutputStream fileOutputStream = new FileOutputStream(
                    new File(mContext.getExternalFilesDir(null), fileName.toString()));

            // Create new sheet.
            StringBuilder sheetName = new StringBuilder();
            sheetName.append(mValues.getYear());
            sheetName.append("年 ");
            sheetName.append(mValues.getMonth());
            sheetName.append("月 ");
            sheetName.append(mValues.getSeason());
            sheetName.append("旬 ");
            sheetName.append(mBasingInfo.getName());
            Sheet sheet = workbook.createSheet(sheetName.toString());
            mBorderStyle = getBorder(workbook);

            setDefaultFormat(sheet);

            setDateDataFormat(sheet);

            workbook.write(fileOutputStream);
            fileOutputStream.close();
        } catch (IOException e) {
            android.util.Log.v("Fukuchi", "IOException : " + e.getMessage());
        }
    }

    private void setDefaultFormat(Sheet sheet) {
        sheet.setDisplayGridlines(false);
        cellBindingBorder(1, 2, 3, 8, sheet, "作 業 報 告 書");
        cellBindingBorder(4, 7, 1, 1, sheet, "日付");
        cellBindingBorder(4, 7, 2, 2, sheet, "予定出社");
        cellBindingBorder(4, 7, 3, 3, sheet, "予定退社");
        cellBindingBorder(4, 7, 4, 4, sheet, "出社");
        cellBindingBorder(4, 7, 5, 5, sheet, "退社");
        cellBindingBorder(4, 7, 6, 6, sheet, "F適用外");
        cellBindingBorder(4, 7, 7, 7, sheet, "休憩");
        cellBindingBorder(4, 7, 8, 8, sheet, "深夜休憩");
        cellBindingBorder(4, 7, 9, 9, sheet, "勤務時間");
        cellBindingBorder(4, 7, 10, 10, sheet, "累計");
        cellBindingBorder(4, 4, 11, 14, sheet, "所定外勤務");
        cellBindingBorder(5, 7, 11, 11, sheet, "深夜");
        cellBindingBorder(5, 7, 12, 12, sheet, "法定休日");
        cellBindingBorder(5, 7, 13, 13, sheet, "所定休日");
        cellBindingBorder(5, 7, 14, 14, sheet, "F適用外");
        cellBindingBorder(3, 3, 11, 13, sheet, "標準勤務時間");
        //その月の勤務日数を計算し、日数*8h
        cellBindingBorder(3, 3, 14, 16, sheet, "160時間(T)");// TODO
        cellBindingBorder(4, 4, 15, 15, sheet, "(40H)");
        cellBindingBorder(5, 5, 15, 15, sheet, "有休");
        cellBindingBorder(6, 6, 15, 15, sheet, "6.0(T)");// TODO
        cellBindingBorder(7, 7, 15, 15, sheet, "40h(T)");// TODO
        cellBindingBorder(4, 7, 16, 16, sheet, "遅刻・早退");
        cellBindingBorder(4, 7, 17, 22, sheet, "作業内容");

        StringBuilder currentSeason = new StringBuilder();
        currentSeason.append(mValues.getMonth());
        currentSeason.append("月 ");
        currentSeason.append(mValues.getSeason());
        currentSeason.append("旬");

        cellBindingBorder(3, 3, 20, 22, sheet, currentSeason.toString());
    }

    private void setDateDataFormat(Sheet sheet) {

        String total = mValues.getTotalWorkingTime();

        for (int i = 0; i < mDateDataList.size(); i++) {
            int targetRow = DATE_TARGET_START_ROW + i;

            boolean isHoliday = false;
            boolean isDoNichiWork = false;
            boolean isSyukujituWork = false;
            DateData dateData = mDateDataList.get(i);
            DateDetailsContainer container = dateData.getDetailContainer();
            if (dateData.isHoliday() && container != null && !container.getTransferWorkFlag() &&
                    !container.getHolidayWorkFlag()) {
                isHoliday = true;
            } else if (!dateData.isHoliday() && container != null && container.getTransferWorkFlag()) {
                isHoliday = true;
            } else if (dateData.isHoliday() && (dateData.getHolidayText() != null &&
                    !dateData.getHolidayText().isEmpty())) {
                isSyukujituWork = true;
            } else if (dateData.isHoliday() && (dateData.getHolidayText() == null ||
                    dateData.getHolidayText().isEmpty())) {
                isDoNichiWork = true;
            }

            boolean isHolidayWork = container != null && container.getHolidayWorkFlag();

            // 日付
            cellBindingBorder(targetRow, targetRow, 1, 1, sheet, dateData.getDay());
            // 予定出社
            String planAttend = isHoliday ? "" : dateData.getPlanAttend();
            cellBindingBorder(targetRow, targetRow, 2, 2, sheet, planAttend);
            // 予定退社
            String planLeave = isHoliday ? "" : dateData.getPlanLeave();
            cellBindingBorder(targetRow, targetRow, 3, 3, sheet, planLeave);
            // 出社
            String realAttend = dateData.getRealAttend();
            cellBindingBorder(targetRow, targetRow, 4, 4, sheet, realAttend);
            // 退社
            String realLeave = dateData.getRealLeave();
            cellBindingBorder(targetRow, targetRow, 5, 5, sheet, realLeave);
            // F適用外
            cellBindingBorder(targetRow, targetRow, 6, 6, sheet,
                    (container != null && container.getFlex()) ? SELECTED_COMMENT : "");
            // 休憩
            cellBindingBorder(targetRow, targetRow, 7, 7, sheet, dateData.getRealBreakTime());
            // 深夜休憩
            // 項目を作ってないので実装必要
            cellBindingBorder(targetRow, targetRow, 8, 8, sheet, dateData.getDeepNightBreakTime());
            // 勤務時間

            // 深夜時間帯の勤務時間の計算　ここから
            String deepWorkerTime = calDeepWorkingTime(dateData.getRealLeave());
            String overWorkingTime = calDiffTime(dateData.getDeepNightBreakTime(), deepWorkerTime);
            // ここまで

            String workingTime = getTotalWorkingTime(realAttend, realLeave,
                    dateData.getRealBreakTime(),
                    dateData.getDetailContainer().getPaidTime());
            String calTime = calSumTime(workingTime, overWorkingTime);

            String targetTime = calTime.isEmpty() ? workingTime : calTime;
            if (!isHolidayWork) {
                cellBindingBorder(targetRow, targetRow, 9, 9, sheet, targetTime);
                // 累計
                if (!workingTime.isEmpty()) {
                    total = JunpouUtil.calSumTime(total, targetTime);
                }
                cellBindingBorder(targetRow, targetRow, 10, 10, sheet, total);
            } else {
                cellBindingBorder(targetRow, targetRow, 9, 9, sheet, "");
                // 累計
                cellBindingBorder(targetRow, targetRow, 10, 10, sheet, "");
            }
            // 深夜
            cellBindingBorder(targetRow, targetRow, 11, 11, sheet, overWorkingTime);
            //法定休日
            if (isHolidayWork && isDoNichiWork) {
                cellBindingBorder(targetRow, targetRow, 12, 12, sheet, targetTime);
            } else {
                cellBindingBorder(targetRow, targetRow, 12, 12, sheet, "");
            }
            // 所定休日
            if (isHolidayWork && isSyukujituWork) {
                cellBindingBorder(targetRow, targetRow, 13, 13, sheet, targetTime);
            } else {
                cellBindingBorder(targetRow, targetRow, 13, 13, sheet, "");
            }
            // F適用外
            cellBindingBorder(targetRow, targetRow, 14, 14, sheet, container.getFlex() ? SELECTED_COMMENT : "");
            // 有給
            cellBindingBorder(targetRow, targetRow, 15, 15, sheet, container.getPaidHolidayFrag() ? SELECTED_COMMENT : "");
            // 遅刻・早退
            String lateOrEarlyTimes = "";
            if (container.getFlex()) {
                lateOrEarlyTimes =
                        JunpouUtil.calLateOrEarlyTime(planAttend, planLeave, realAttend, realLeave);
            }
            cellBindingBorder(targetRow, targetRow, 16, 16, sheet, lateOrEarlyTimes);
            // 作業内容
            cellBindingBorder(targetRow, targetRow, 17, 22, sheet, dateData.getWorkContent());
        }
    }

    // 枠線
    private CellStyle getBorder(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(CellStyle.BORDER_MEDIUM);
        style.setBorderLeft(CellStyle.BORDER_MEDIUM);
        style.setBorderRight(CellStyle.BORDER_MEDIUM);
        style.setBorderTop(CellStyle.BORDER_MEDIUM);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setWrapText(true);
        return style;
    }

    // ターゲットのセルに枠線を付ける
    private void cellBindingBorder(int firstRow, int lastRow, int firstColumn, int lastColumn,
                                   Sheet sheet, String text) {
        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstColumn, lastColumn));
        for (int targetRow = firstRow; targetRow <= lastRow; targetRow++) {
            Row row = sheet.getRow(targetRow);
            if (row == null) {
                row = sheet.createRow(targetRow);
            }
            for (int targetColumn = firstColumn; targetColumn <= lastColumn; targetColumn++) {
                Cell cell = row.getCell(targetColumn);
                if (cell == null) {
                    cell = row.createCell(targetColumn);
                    sheet.setColumnWidth(targetColumn, 1800);
                }
                cell.setCellStyle(mBorderStyle);
                if (targetRow == firstRow && targetColumn == firstColumn) {
                    cell.setCellValue(text);
                }
            }
        }
    }
}
