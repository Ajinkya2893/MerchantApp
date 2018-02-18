package com.Merchant.BillPayment;

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

public class BsnlRecharge extends AppBase{
	static int count=-1;
	static boolean skip=false;
	static boolean pass = false;
	static boolean fail=true;
	static boolean isTestPass=false;
	public String msg = "";

	private BsnlRecharge(){
		try {
			prop = new Properties();
			FileInputStream fs = new FileInputStream(Constants.Properties_file_path); //Object repository path
			prop.load(fs);
			xls = new Excel_Reader(Constants.RetailerLogin); // Loading the Excel Sheet 
			rep = ExtentManager.getInstance(); // Getting the Extent Report Instance 
			test  = rep.startTest(this.getClass().getSimpleName()); //Starting the Extent Report test
			Util = new Utility(test, driver);
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
			throw new SkipException("Skipping test case" + this.getClass().getSimpleName() + " as runmode set to NO in excel");
		}
	}

	@Test(dataProvider="getData")
	public void LandlineRecharge(Hashtable<String,String> data){
		count++;
		try {
			new RetailerLogin().getLogin(data);
			Thread.sleep(3000);
			driver.findElement(By.xpath(prop.getProperty("billIcon"))).click(); test.log(LogStatus.INFO, "Clicking on Water icon");
			driver.findElement(By.xpath(prop.getProperty("billproduct"))).click();test.log(LogStatus.INFO, "Selecting the Water Product");//Mahanagar gas change any service provider needed
			Thread.sleep(1000);
			driver.findElement(By.id(prop.getProperty("mobilenumber"))).sendKeys("Mobile Number");test.log(LogStatus.INFO, "Entering the Mobile Number");
			driver.findElement(By.id(prop.getProperty("billaccno"))).sendKeys(data.get("KA Number"));test.log(LogStatus.INFO, "Entering the Account Number");
			driver.findElement(By.id(prop.getProperty("billtelno"))).sendKeys(data.get("KA Number"));test.log(LogStatus.INFO, "Entering the Telephone Number");
			Thread.sleep(1000);
			driver.findElement(By.id(prop.getProperty("fetchBill"))).click();test.log(LogStatus.INFO, "Clicking on fetchBill Button");
			Thread.sleep(5000);
			driver.findElement(By.xpath(prop.getProperty("elecpaybill")));
			Thread.sleep(2000);
			Util.takeScreenShot("Bill fetch Successfully by BBPS");
			driver.findElement(By.xpath("//android.widget.TextView[@text='NO']")).click();
			driver.findElement(By.className("android.widget.ImageButton")).click();
			driver.findElement(By.className("android.widget.ImageButton")).click();fail =false;msg ="Successfully fetched the bill";
		} catch (InterruptedException e){
			// TODO Auto-generated catch block
			test.log(LogStatus.ERROR, "Issue while doing a LandLine Recharge");
			msg="Issue while doing a LandLine Recharge";
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
	}

	@DataProvider
	public Object[][] getData(){
		return DataUtils.getData(xls, this.getClass().getSimpleName(),this.getClass().getSimpleName());
	}
}

