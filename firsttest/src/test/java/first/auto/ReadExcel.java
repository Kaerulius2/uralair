package first.auto;


import jxl.Cell;
import jxl.write.*;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;


import java.io.File;
import java.io.IOException;

public class ReadExcel {

    private String inputFile;

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public void read() throws IOException {
        File inputWorkbook = new File(inputFile);
        Workbook w;
        try {
            w = Workbook.getWorkbook(inputWorkbook);
            // Get the first sheet
            Sheet sheet = w.getSheet(0);
            // Loop over first 10 column and lines

            for (int j = 0; j < sheet.getColumns(); j++) {
                for (int i = 0; i < sheet.getRows(); i++) {
                    Cell cell = sheet.getCell(j, i);
                    CellType type = cell.getType();
                    if (type == CellType.LABEL) {
                        System.out.println("I got a label "
                                + cell.getContents());
                    }

                    if (type == CellType.NUMBER) {
                        System.out.println("I got a number "
                                + cell.getContents());
                    }

                }
            }
        } catch (BiffException e) {
            e.printStackTrace();
        }
    }


    public int getStrokaCount() throws IOException {
        File inputWorkbook = new File(inputFile);
        Workbook w;
        try {
            w = Workbook.getWorkbook(inputWorkbook);
            // Get the first sheet
            Sheet sheet = w.getSheet(0);
            // Loop over first 10 column and lines

            return sheet.getRows();

        } catch (BiffException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getStolbecCount() throws IOException {
        File inputWorkbook = new File(inputFile);
        Workbook w;
        try {
            w = Workbook.getWorkbook(inputWorkbook);
            // Get the first sheet
            Sheet sheet = w.getSheet(0);
            // Loop over first 10 column and lines

            return sheet.getColumns();

        } catch (BiffException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String readCell(int stroka, int stolbec) throws IOException {
        File inputWorkbook = new File(inputFile);
        Workbook w;
        try {
            w = Workbook.getWorkbook(inputWorkbook);
            // Get the first sheet
            Sheet sheet = w.getSheet(0);
            // Loop over first 10 column and lines

            //for (int j = 0; j < sheet.getColumns(); j++) {
            Cell cell = sheet.getCell(stolbec, stroka);

            return cell.getContents();
        } catch (BiffException e) {
            e.printStackTrace();
        }
       return null;
    }

    public boolean writeCell(int stroka, int stolbec, String data) throws IOException {
        File inputWorkbook = new File(inputFile);

        try {
            Workbook w = Workbook.getWorkbook(inputWorkbook);
            WritableWorkbook copy = Workbook.createWorkbook(new File("c:/aData/temp.xls"),w);
            // Get the first sheet
            WritableSheet wsheet = copy.getSheet(0);

            WritableCell wcell = wsheet.getWritableCell(stolbec, stroka);

            Label lab = (Label) wcell;
            lab.setString(data);
            copy.write();
            copy.close();
            w.close();

            return true;
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return false;
    }

}