package src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class excel_test {

	public static void main(String[] args) throws Exception {

		//import_excel();
		export_excle();
	}

	//excel import
	public static void import_excel() throws Exception
	{
		//file read
		File file = new File("c:\\excel_test\\import.xlsx");
		FileInputStream fis = new FileInputStream(file);
		
		//fileInputStream to excel Workbook object
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		
		//get excel sheet from workbook object
		XSSFSheet s = wb.getSheetAt(0);
		
		//get iterated row
		Iterator<Row> it_r = s.iterator();
		
		
		while(it_r.hasNext())
		{
			Row r = it_r.next();
			
			//get cell value by looping
			for(int i = 0; i < r.getPhysicalNumberOfCells(); i++)
			{
				System.out.print(r.getCell(i) + "|");
			}
			
			System.out.println("");
			
		}
		
	}
	
	
	//excel export
	public static void export_excle() throws IOException
	{
		XSSFWorkbook wb = new XSSFWorkbook();
		
		XSSFSheet s = wb.createSheet("시트생성테스트1");
		
		for(int i = 0 ; i < 10; i++)
		{
			XSSFRow r = s.createRow(i);
			
			for(int j = 0 ; j < 10; j++)
			{
				XSSFCell c = r.createCell(j);
				
				c.setCellValue("테스트_" + i + "_" + j);
			}
			
		}
		
		File file = new File("c:\\excel_test\\export.xlsx");
		FileOutputStream fout = new FileOutputStream(file);
		
		wb.write(fout);
		
		fout.close();
		
	}
		

}
