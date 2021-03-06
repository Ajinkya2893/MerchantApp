package com.Merchant.MoneyTransfer;

import java.io.FileInputStream;
import java.util.Hashtable;
import java.util.Properties;

import org.openqa.selenium.By;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.BaseClass.AppBase;
import com.Merchant.Login.RetailerLogin;
import com.relevantcodes.extentreports.LogStatus;

import Utility.Constants;
import Utility.DataUtils;
import Utility.Excel_Reader;
import Utility.ExtentManager;

public class TransferMoney extends AppBase{

	static int count=-1;
	static boolean skip=false;
	static boolean pass = false;
	static boolean fail=true;
	static boolean isTestPass=false;
	public String msg = "";

	private TransferMoney(){
		try {
			prop = new Properties();
			FileInputStream fs = new FileInputStream(Constants.ReProperties_file_path); //Object repository path
			prop.load(fs);
			xls = new Excel_Reader(Constants.Remit); // Loading the Excel Sheet 
			rep = ExtentManager.getInstance(); // Getting the Extent Report Instance 
			test  = rep.startTest(this.getClass().getSimpleName()); //Starting the Extent Report test
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Unable to run this class and create any instance");
		}
	}

	@BeforeTest()
	public void Testskip(){
		if (DataUtils.isSkip(xls, this.getClass().getSimpleName())){
			test.log(LogStatus.SKIP, "Skipping the test as runmode is N");
			DataUtils.reportDataSetResult(xls, "TestCase",DataUtils.getRowNum(xls,this.getClass().getSimpleName()), "Skip");
			skip = true;
			msg= "Skipping the test as runmode is N";
			rep.endTest(test);
			rep.flush();
			throw new SkipException("Skipping test case" + this.getClass().getSimpleName() + " as runmode set to NO in excel");
		}
	}

	@Test(dataProvider="getData")
	public void GasRecharge(Hashtable<String,String> data){
		count++;
		try {
			if (data.get("Runmode").equalsIgnoreCase("N")){
				test.log(LogStatus.SKIP, "Skipping the test as this set of data is set to N");
				skip = true;
				rep.endTest(test);
				rep.flush();
				throw new SkipException("Skipping the test as this set of data is set to N");
			}
			this.driver = new RetailerLogin().getLogin(data);
			Thread.sleep(3000);
			driver.findElement(By.xpath(prop.getProperty("DMTIcon"))).click(); test.log(LogStatus.INFO, "Clicking on DMT icon");
			driver.findElement(By.xpath(prop.getProperty("Search/AddBtn"))).sendKeys(data.get("Search"));test.log(LogStatus.INFO, "Enter Search Number");
			Thread.sleep(5000);
			driver.findElement(By.id(prop.getProperty("SelectBen"))).click(); test.log(LogStatus.INFO, "Clicking on Ben icon");Thread.sleep(3000);
			driver.findElement(By.xpath(prop.getProperty("benmindsArray"))).click(); test.log(LogStatus.INFO, "Clicking on Ben icon");
			driver.findElement(By.xpath(prop.getProperty("AmountBtn"))).sendKeys(data.get("Amount"));test.log(LogStatus.INFO, "Entering Amount");
			driver.hideKeyboard();
			Util.takeScreenShot("Selected Beneficary");
			driver.findElement(By.xpath(prop.getProperty("ProceedBtn"))).click();test.log(LogStatus.INFO, "Clicking on Transfer Now button");
			driver.findElement(By.xpath(prop.getProperty("transnowbtn"))).click();test.log(LogStatus.INFO, "Clicking on Transfer Now button");
			Util.takeScreenShot("Successfully transfered Amount"); 
			driver.findElement(By.xpath(prop.getProperty("Gotohomebtn"))).click();test.log(LogStatus.INFO, "Clicking on Go to Home button");
			fail = false;
			msg = "Successfully transfered Amount";
			test.log(LogStatus.PASS, "Successfully transfered Amount");
			
		} catch (InterruptedException e){
			// TODO Auto-generated catch block
			test.log(LogStatus.ERROR, "Issue while doing a Gas Recharge");
			msg="Issue while transfering amount";
			e.printStackTrace();
		}
	}

	@AfterMethod
	public void reporter(){
		if(fail){
			DataUtils.reportDataSetResult(xls, this.getClass().getSimpleName(), count+2, "Fail");
			DataUtils.reportDataSetActualResult(xls, this.getClass().getSimpleName(),count+2, msg);
		}else{
			isTestPass=true;
			DataUtils.reportDataSetResult(xls, this.getClass().getSimpleName(), count+2, "Pass");
			DataUtils.reportDataSetActualResult(xls, this.getClass().getSimpleName(),count+2, msg);
		}
		if(skip)
			DataUtils.reportDataSetResult(xls, this.getClass().getSimpleName(), count+2, "Skip");
		skip=false;
		fail=true;
	}

	@AfterTest
	public void reportTestResult(){
		if(skip)
			DataUtils.reportDataSetResult(xls, "TestCase",DataUtils.getRowNum(xls,this.getClass().getSimpleName()), "Skip");
		if(isTestPass)
			DataUtils.reportDataSetResult(xls, "TestCase",DataUtils.getRowNum(xls,this.getClass().getSimpleName()), "Pass");
		else
			DataUtils.reportDataSetResult(xls, "TestCase", DataUtils.getRowNum(xls,this.getClass().getSimpleName()), "Fail");
		isTestPass = false;
		rep.endTest(test);
		rep.flush();
	}

	@DataProvider
	public Object[][] getData(){
		return DataUtils.getData(xls, this.getClass().getSimpleName(),this.getClass().getSimpleName());
	}
}