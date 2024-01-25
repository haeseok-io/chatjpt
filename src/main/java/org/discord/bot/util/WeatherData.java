package org.discord.bot.util;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.discord.bot.dto.WeatherDTO;

import java.io.FileInputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WeatherData {
    private List<WeatherDTO> excelList = null;

    public WeatherData() {
        try {
            FileInputStream file = new FileInputStream("/Users/haeseok/Desktop/dev/java/discord/chatjpt/src/main/resources/location_info.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            // 시트정보 가져오기
            XSSFSheet sheet = workbook.getSheetAt(0);
            Integer rowLength = sheet.getPhysicalNumberOfRows();

            excelList = IntStream.range(1, rowLength)
                    .filter(i -> sheet.getRow(i)!=null)
                    .mapToObj(i -> {
                        XSSFRow row = sheet.getRow(i);
                        Integer cellLength = row.getPhysicalNumberOfCells();
                        WeatherDTO dto = new WeatherDTO();

                        for(int c=0; c<cellLength; c++) {
                            XSSFCell cell = row.getCell(c);
                            String value = cellValue(cell);

                            if( value!=null ) {
                                switch(c) {
                                    case 0 : dto.setSi(value); break;
                                    case 1 : dto.setGu(value); break;
                                    case 2 : dto.setDong(value); break;
                                    case 3 : dto.setX(value); break;
                                    case 4 : dto.setY(value); break;
                                }
                            }
                        }

                        return dto;
                    }).collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getWeatherSi(){
        List<String> result = excelList.stream()
                                        .map(obj -> obj.getSi())
                                        .distinct()
                                        .collect(Collectors.toList());
        return result;
    }

    private String cellValue(XSSFCell cell) {
        String result = null;
        CellType type = cell.getCellType();

        if( type.equals(CellType.BLANK) || type.equals(CellType.ERROR) ) {
            return result;
        }

        switch (cell.getCellType()) {
            case FORMULA :
                result = cell.getCellFormula();
                break;
            case NUMERIC :
                result = cell.getNumericCellValue()+"";
                break;
            case STRING :
                result = cell.getStringCellValue();
                break;
        }

        if( result.equals("") )     result = null;

        return result;
    }

}
