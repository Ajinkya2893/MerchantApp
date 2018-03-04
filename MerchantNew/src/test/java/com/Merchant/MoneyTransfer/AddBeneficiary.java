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

public class AddBeneficiary extends AppBase {

	static int count=-1;
	static boolean skip=false;
	static boolean pass = false;
	static boolean fail=true;
	static boolean isTestPass=false;
	public String msg = "";
	public String testName;

	private AddBeneficiary(){
		try {
			testName = this.getClass().getSimpleName();
			prop = new Properties();
			FileInputStream fs = new FileInputStream(Constants.ReProperties_file_path); //Object repository path
			prop.load(fs);
			xls = new Excel_Reader(Constants.Remit); // Loading the Excel Sheet 
			rep = ExtentManager.getInstance(); // Getting the Extent Report Instance 
			test  = rep.startTest(testName); //Starting the Extent Report test
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Unable to run this class and create any instance");
		}
	}

	@BeforeTest()
	public void Testskip(){
		if (DataUtils.isSkip(xls, testName)){
			test.log(LogStatus.SKIP, "Skipping the test as runmode is N");
			DataUtils.reportDataSetResult(xls, "TestCase",DataUtils.getRowNum(xls,testName), "Skip");
			skip = true;
			msg= "Skipping the test as runmode is N";
			rep.endTest(test);
			rep.flush();
			throw new SkipException("Skipping test case " + testName + " as runmode set to NO in excel");
		}
	}

	@Test(dataProvider="getData")
	public void Addbeneficary(Hashtable<String,String> data){
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
			driver.findElement(By.xpath(prop.getProperty("AddBenBtn"))).click();test.log(LogStatus.INFO, "Clicking on Add Beneficiary button");
			driver.findElement(By.xpath(prop.getProperty("AccountNumb"))).sendKeys(data.get("Account No"));test.log(LogStatus.INFO, "Enter Account Number");
			driver.findElement(By.xpath(prop.getProperty("BankName"))).sendKeys(data.get("Bank Name"));test.log(LogStatus.INFO, "Enter Bank Name");
			driver.findElement(By.id("com.mindsarray.pay1:id/input_layout_mobile")).click();
			driver.findElement(By.xpath(prop.getProperty("BenMobile"))).sendKeys(data.get("BenNumber"));test.log(LogStatus.INFO, "Enter Mobile No.");
			driver.findElement(By.xpath(prop.getProperty("ProceedBtn"))).click();test.log(LogStatus.INFO, "Clicking on Add Beneficiary button");
			Util.takeScreenShot("Entered details");	
			fail = false; msg ="Successfully added the remitter";
		} catch (InterruptedException e){
			// TODO Auto-generated catch block
			test.log(LogStatus.ERROR, "Issue while Adding beneficiary");
			fail = true;
			msg="Issue while Adding beneficiary";
			e.printStackTrace();
		}
	}

	@AfterMethod
	public void reporter(){
		if(fail){
			DataUtils.reportDataSetResult(xls, testName, count+2, "Fail");
			DataUtils.reportDataSetActualResult(xls, testName,count+2, msg);
		}else{
			isTestPass=true;
			DataUtils.reportDataSetResult(xls, testName, count+2, "Pass");
			DataUtils.reportDataSetActualResult(xls, testName,count+2, msg);
		}
		if(skip)
			DataUtils.reportDataSetResult(xls, testName, count+2, "Skip");
		skip=false;
		fail=true;
	}

	@AfterTest
	public void reportTestResult() throws InterruptedException{
		if(skip)
			DataUtils.reportDataSetResult(xls, "TestCase",DataUtils.getRowNum(xls,testName), "Skip");
		if(isTestPass)
			DataUtils.reportDataSetResult(xls, "TestCase",DataUtils.getRowNum(xls,testName), "Pass");
		else
			DataUtils.reportDataSetResult(xls, "TestCase", DataUtils.getRowNum(xls,testName), "Fail");
		isTestPass = false;
		rep.endTest(test);
		closure();
	}

	@DataProvider
	public Object[][] getData(){
		return DataUtils.getData(xls, testName,testName);
	}
}