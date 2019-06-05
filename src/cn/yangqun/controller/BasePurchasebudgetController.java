package cn.yangqun.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.upload.UploadFile;

import cn.yangqun.common.model.Member;
import cn.yangqun.common.model.Purchasebudget;
import cn.yangqun.common.util.DateUtil;
import cn.yangqun.common.util.ExcelUtil;

public class BasePurchasebudgetController extends Controller {
	/**
	 * 首页
	 */
	public void index() {
		String unitCode = getPara(0);
		/** 获取当前用户信息 */
		Member user = Member.dao.findFirst("SELECT * FROM MEMBER WHERE CODE = ?", unitCode);
		/**获取当前年度信息*/
		int year = DateUtil.YEAR();
		Integer count = Db.queryInt("SELECT COUNT(1) FROM PURCHASEBUDGET WHERE UNITCODE = ? AND YEAR = ?", unitCode,year);
		set("user",user).set("year", year).set("count", count).render("excel.html");
		
	}
	/**
	 * 导入文件
	 */
	public void imp() throws FileNotFoundException, IOException{
		UploadFile file=getFile();
		File upfile=file.getFile();
		String unitCode = getPara("unitCode");
		String unitName = getPara("unitName");
		String deptCode = getPara("deptCode");
		String deptName = getPara("deptName");
		Integer year = getInt("year");
		
		if(ExcelUtil.checkExcel(file,upfile)==false){
			renderText("上传文件格式不正确，请上传Excel文件(*.xls或*.xlsx)");
			return;
		}
		
	try {
		//获得工作簿
		Workbook workBook = WorkbookFactory.create(upfile);
		//解析工作表
		Sheet sheet = null;
		Row row = null;
		Cell cell = null;
		List<Purchasebudget> purchasebudgets = new ArrayList<Purchasebudget>();
		// 获取第一个Sheet
		sheet=workBook.getSheetAt(0);
		//拿到一个具体工作表
		//得到有效行数
		int rowNumber = sheet.getPhysicalNumberOfRows();
		for(int rowIndex = 1;rowIndex < rowNumber;rowIndex++){
					row = sheet.getRow(rowIndex);
					Purchasebudget p = new Purchasebudget();
					Integer id = Db.queryInt("SELECT SEQ_PURCHASEBUDGET.NEXTVAL FROM DUAL");
					p.setId(id);
					p.setYear(year);
					p.setUnitcode(unitCode);
					p.setUnitname(unitName);
					p.setDeptcode(deptCode);
					p.setDeptname(deptName);
					p.setSubjectcode(ExcelUtil.getCellStringValue(row, 0));
					p.setSubjectname(ExcelUtil.getCellStringValue(row, 1));
					p.setItemscode(ExcelUtil.getCellStringValue(row, 2));
					p.setItemsname(ExcelUtil.getCellStringValue(row, 3));
					p.setMethodcode(ExcelUtil.getCellStringValue(row, 4));
					p.setMethodname(ExcelUtil.getCellStringValue(row, 5));
					p.setDetaildesc(ExcelUtil.getCellStringValue(row,6));
					p.setPrice(ExcelUtil.getCellNumericValue(row, 7));
					p.setQuantity(ExcelUtil.getCellNumericValue(row, 8));
					p.setAmount(ExcelUtil.getCellNumericValue(row, 9));
					p.setBudgetuse(ExcelUtil.getCellStringValue(row, 10));
					p.setRemark(ExcelUtil.getCellStringValue(row, 11));
					p.setInputuser(unitCode);
					p.setInputtime(new Date());
					p.setUpdateuser(unitCode);
					p.setUpdatetime(new Date());
					purchasebudgets.add(p);
				}
				//批量存入数据库
				Db.batchSave(purchasebudgets, purchasebudgets.size());
				renderText("成功导入");
				//关掉工作簿
				workBook.close();
				//删除掉临时文件
				upfile.delete();
				renderText("成功导入"+ purchasebudgets.size() + "条数据！");
			}catch (Exception e) {
				e.printStackTrace();
			renderText("Excel文件解析失败");
			return;
		}
	}
	
	public void add() {
		String unitCode = getPara(0);
		System.err.println(unitCode);
		renderText("进入add");
		
	}
	public void update() {
		renderText("进入update");
	}
}
