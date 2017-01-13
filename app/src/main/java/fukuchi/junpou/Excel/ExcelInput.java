package fukuchi.junpou.Excel;

import java.net.URI;

/**
 * Look !!
 * https://msdn.microsoft.com/ja-jp/library/office/dn906146.aspx
 */

public class ExcelInput {

    private static final String EXCEL_SCHEME = "ms-excel:";
    private static final String READ_ONLY_CMD = "ofv|u|";

    private void openReadOnlyExcel(URI documentUri){
        String excelScheme = EXCEL_SCHEME + READ_ONLY_CMD + documentUri;
    }
}
