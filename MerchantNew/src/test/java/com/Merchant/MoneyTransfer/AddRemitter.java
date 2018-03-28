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
import Utility.Utility;

public class AddRemitter extends AppBase{

	static int count=-1;
	static boolean skip=false;
	static boolean pass = false;
	static boolean fail=true;
	static boolean isTestPass=false;
	public String msg = "";

	private AddRemitter(){
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
	public void AddRemiter(Hashtable<String,String> data){
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
			Util = new Utility(test, driver);
			Thread.sleep(3000);
			driver.findElement(By.xpath(prop.getProperty("DMTIcon"))).click(); test.log(LogStatus.INFO, "Clicking on DMT icon");
			driver.findElement(By.xpath(prop.getProperty("Search/AddBtn"))).sendKeys(data.get("Search"));test.log(LogStatus.INFO, "Enter Search Number");
			Thread.sleep(10000);
			driver.findElement(By.xpath(prop.getProperty("AddremBtn"))).click(); test.log(LogStatus.INFO, "Clttonicking on Add bu");
			driver.findElement(By.id(prop.getProperty("RemiterName"))).sendKeys(data.get("Name")); test.log(LogStatus.INFO, "Entering the remitter name");
			driver.hideKeyboard();
			driver.findElement(By.xpath(prop.getProperty("remitaddbtn"))).click(); test.log(LogStatus.INFO, "Clicking on Proceed button");
			driver.findElement(By.xpath(prop.getProperty("otpteet"))).sendKeys(data.get("Otp")); test.log(LogStatus.INFO, "Entring the OTP");
			driver.findElement(By.xpath(prop.getProperty("addstm"))).click(); test.log(LogStatus.INFO, "Submitting the OTP");
			Util.takeScreenShot("Successfully entered the OTP");
		} catch (InterruptedException e){
			// TODO Auto-generated catch block
			test.log(LogStatus.ERROR, "Error while adding A Remitter");
			msg="Error while adding A Remitter"; 
			Util.takeScreenShot(msg);
			fail = true;
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
	public void reportTestResult() throws InterruptedException{
		if(skip)
			DataUtils.reportDataSetResult(xls, "TestCase",DataUtils.getRowNum(xls,this.getClass().getSimpleName()), "Skip");
		if(isTestPass)
			DataUtils.reportDataSetResult(xls, "TestCase",DataUtils.getRowNum(xls,this.getClass().getSimpleName()), "Pass");
		else
			DataUtils.reportDataSetResult(xls, "TestCase", DataUtils.getRowNum(xls,this.getClass().getSimpleName()), "Fail");
		isTestPass = false;
		rep.endTest(test);
		closure();
	}

	@DataProvider
	public Object[][] getData(){
		return DataUtils.getData(xls, this.getClass().getSimpleName(),this.getClass().getSimpleName());
	}
}