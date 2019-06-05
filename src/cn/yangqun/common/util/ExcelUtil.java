package cn.yangqun.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import com.jfinal.upload.UploadFile;
/**
 * Excel工具类
 * @author 小木
 *
 */
public class ExcelUtil {
	/**
	 * 检测上传文件名和文件格式是否是Excel
	 * @param file
	 * @param upfile
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static boolean checkExcel(UploadFile file, File upfile) throws FileNotFoundException, IOException {
		String fileName=file.getFileName();
		boolean isExcel=fileName.endsWith(".xls")||fileName.endsWith(".xlsx");
		if(isExcel==false){
			return false;
		}
		InputStream inp=new FileInputStream(upfile);
		if(!inp.markSupported()){
			inp=new PushbackInputStream(inp, 8);
		}
		//判断后缀是xls 2003
		if(POIFSFileSystem.hasPOIFSHeader(inp)==false&&POIXMLDocument.hasOOXMLHeader(inp)==false){
			inp.close();
			return false;
		}
		inp.close();

		return true;
	}
	 /**
	  * 得到单元格数据 返回String类型
	  * @param row
	  * @param cellIndex
	  * @return
	  */
	public static String getCellStringValue(Row row,int cellIndex){
		Cell cell=row.getCell(cellIndex);
		if(cell != null) {
			cell.setCellType(CellType.STRING);
			return cell.getStringCellValue();
		}else {
			return "";
		}
		
		
	}
	/**
	 * 得到单元格数据 返回double类型
	 * @param row
	 * @param cellIndex
	 * @return
	 */
	public static double getCellNumericValue(Row row,int cellIndex){
		Cell cell=row.getCell(cellIndex);
		cell.setCellType(CellType.NUMERIC);
		return cell.getNumericCellValue();
	}
	/**
	 * 得到单元格数据 返回int类型
	 * @param row
	 * @param cellIndex
	 * @return
	 */
	public static int getCellIntValue(Row row,int cellIndex){
		Cell cell=row.getCell(cellIndex);
		cell.setCellType(CellType.NUMERIC);
		return (int) getCellNumericValue(row,cellIndex);
	}
}
