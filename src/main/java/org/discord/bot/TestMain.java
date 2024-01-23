package org.discord.bot;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class TestMain {
    public static void main(String[] args) throws IOException {
        //String apiKey = "Xt0cUVgX7VOk%2FpDmBxslTm7SLwbGzgTElJ21zTVv3rGvij61O40z9mfztRNf%2BP8ICeJpXeMr%2Fv%2BD2tzNXsHtxQ%3D%3D";
        //String endPoint = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst";
        //
        //String param = "ServiceKey="+apiKey+"&pageNo=1&numOfRows=10000&dataType=json&base_date=20240122&base_time=0600&nx=55&ny=127";
        //String jsonData = RequestAPI.get(endPoint, null, param);
        //
        //System.out.println(jsonData);

        try {
            FileInputStream file = new FileInputStream("/Users/haeseok/Desktop/dev/java/discord/chatjpt/src/main/resources/location_info.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            int rowNo = 0;
            int cellIndex = 0;

            // 0 번째 시트 가져오기
            XSSFSheet sheet = workbook.getSheetAt(0);

            // row 수 가져오기
            int rows = sheet.getPhysicalNumberOfRows();
            for(rowNo = 0; rowNo<rows; rowNo++) {
                XSSFRow row = sheet.getRow(rowNo);

                if( row!=null ) {
                    // 해당 row 에 존재하는 cell 갯수를 가져온다
                    int cells = row.getPhysicalNumberOfCells();

                    for(cellIndex = 0; cellIndex<cells; cellIndex++) {
                        XSSFCell cell = row.getCell(cellIndex);
                    }
                }
            }

            System.out.println(rows);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
