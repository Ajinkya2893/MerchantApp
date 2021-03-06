package com.Merchant.Login;

import java.io.FileInputStream;
import java.util.Hashtable;
import java.util.Properties;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.BaseClass.AppBase;
import com.relevantcodes.extentreports.LogStatus;

import Utility.Constants;
import Utility.DataUtils;
import Utility.Excel_Reader;
import Utility.ExtentManager;
import Utility.Utility;
import io.appium.java_client.AppiumDriver;

public class RetailerLogin extends AppBase{
	static int count=-1;
	static boolean skip=false;
	static boolean pass = false;
	static boolean fail=true;
	static boolean isTestPass=false;
	public String msg = "";


	public RetailerLogin(){
		try {
			prop = new Properties();
			FileInputStream fs = new FileInputStream(Constants.Properties_file_path); //Object repository path
			prop.load(fs);
			xls = new Excel_Reader(Constants.RetailerLogin); // Loading the Excel Sheet 
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
			DataUtils.reportDataSetResult(xls, "TestCase",DataUtils.getRowNum(xls,this.getClass().getSimpleName()), "Skip");
			test.log(LogStatus.SKIP, "Skipping the test as runmode is NO in the Excel Sheet");
			skip = true;
			throw new SkipException("Skipping test case" + this.getClass().getSimpleName() + " as runmode set to NO in excel");
		} 
	}

	@Test(dataProvider="getData")
	public void log(Hashtable<String,String> data) {
		try {
			this.driver = getLogin(data);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	public AppiumDriver getLogin(Hashtable<String,String> data) throws InterruptedException {
		count++;
		try{
			if (data.get("Runmode").equalsIgnoreCase("N")){
				test.log(LogStatus.SKIP, "Skipping the test as runmode is N");
				skip = true;
				rep.endTest(test);
				rep.flush();
				throw new SkipException("Skipping the test as runmode is N");
			}
			LaunchApp();
			//SwipeScreens();
			SelectLaunguage();
			//navigateToLogin();
			newProfileLogin();
			Util =  new Utility(test, driver);
			msg ="Passed";
			driver.findElement(By.id(prop.getProperty("Username"))).click();
			driver.findElement(By.id(prop.getProperty("Username"))).sendKeys(data.get("Username")); test.log(LogStatus.INFO, "Entering the UserName");
			driver.findElement(By.id(prop.getProperty("Password"))).sendKeys(data.get("Password")); test.log(LogStatus.INFO, "Entering the Password");
			driver.hideKeyboard();
			Thread.sleep(1000);
			driver.findElement(By.id(prop.getProperty("LoginSubmit"))).click(); test.log(LogStatus.PASS, "Clicking On Submit Button");


			// Pre check to avoid response time
			if(Util.isElementPresent("wrongpasswdmsz_id") || Util.isElementPresent("otpfield_id")) { 
				if(Util.isElementPresent("wrongpasswdmsz_id")) {
					msg = "Verify the entered username and password is wrong";
					Util.takeScreenShot(msg);
					Util.getElement("wrongpasswdbtn_id").click();
					fail= true; 
					test.log(LogStatus.FAIL, msg);
					Assert.fail(msg);
				}
				else if(Util.isElementPresent("otpfield_id")) {
					otpFlag = true;
					getOtp("123456");
				}
			}
			if (Util.isElementPresent("OK_xpath") || Util.isElementPresent("closeAdd_id") || Util.isElementPresent("addBal_id")){
				if(Util.isElementPresent("OK_xpath")) {
					driver.findElement(By.xpath(prop.getProperty("OK_xpath"))).click();
					Util.takeScreenShot("After login try");
				}
				else if(Util.isElementPresent("closeAdd_id"))
					Util.getElement("closeAdd_id").click();
				if(Util.isElementPresent("addBal_id")) { 
					fail =false ;
					System.out.println("Pass");
					test.log(LogStatus.PASS, "Sucessfully Logged into Application"); msg="Sucessfully Logged into Application";
				}
			}
			else {
				fail= true; test.log(LogStatus.FAIL, "User does not got Login"); msg ="User does not got Login";
			}

		}
		catch (Exception e) {
			msg ="Unable to Access Login Page and Get Login";
			e.printStackTrace();
			Util.takeScreenShot(msg);
			test.log(LogStatus.ERROR, msg);
			fail = true;
		}
		return driver;
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
		return DataUtils.getData(xls, this.getClass().getSimpleName(), this.getClass().getSimpleName());
	}
}