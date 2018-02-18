package com.Merchant.Recharge;

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
import com.Merchant.Reports.TransactionHistory;
import com.relevantcodes.extentreports.LogStatus;

import Utility.Constants;
import Utility.DataUtils;
import Utility.Excel_Reader;
import Utility.ExtentManager;
import Utility.Utility;

public class PostpaidRecharge extends AppBase{

	static int count=-1;
	static boolean skip=false;
	static boolean pass = false;
	static boolean fail=true;
	static boolean isTestPass=false;
	public String msg = "";

	private PostpaidRecharge(){
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
			test.log(LogStatus.SKIP, "Skipping the test as runmode is N");
			DataUtils.reportDataSetResult(xls, "TestCase",DataUtils.getRowNum(xls,this.getClass().getSimpleName()), "Skip");
			skip = true;
			throw new SkipException("Skipping test case" + this.getClass().getSimpleName() + " as runmode set to NO in excel");
		}
	}

	@Test(dataProvider="getData")
	public void mobileRecharge(Hashtable<String,String> data){
		count++;
		try {
			this.driver = new RetailerLogin().getLogin(data);
			Thread.sleep(3000);
			Util = new Utility(this.test, this.driver);
			Util.takeScreenShot("Successfully Logged in");
			test.log(LogStatus.PASS, "Successfully logged in the System");
			driver.findElement(By.xpath(prop.getProperty("mobileIcon"))).click();test.log(LogStatus.INFO, "Clicking on MobileIcon");
			driver.findElement(By.xpath(prop.getProperty("postpaid"))).click();test.log(LogStatus.INFO, "Clicking on Bill Payment");
			driver.findElement(By.xpath(prop.getProperty("postproduct"))).click();test.log(LogStatus.INFO, "Selecting the Product");//Idea change any service provider needed
			Thread.sleep(1000);
			driver.findElement(By.id(prop.getProperty("mobileNumber"))).sendKeys(data.get("Mobile"));test.log(LogStatus.INFO, "Entering the Mobile Number");
			driver.findElement(By.id(prop.getProperty("amountValue"))).sendKeys(data.get("Amount"));test.log(LogStatus.INFO, "Entering the Amount");
			//driver.hideKeyboard();
			Thread.sleep(1000);
			driver.findElement(By.id(prop.getProperty("rechargeButton"))).click();test.log(LogStatus.INFO, "Clicking on Recharge button");
			System.out.println(driver.findElement(By.id(prop.getProperty("confirmationBox"))).getText());// Pop confirmation
			driver.findElement(By.id(prop.getProperty("confirmOkButton"))).click();new Utility(test, driver).takeScreenShot("Capturing the Confirmation box");//Enter button1 for confirming the amount
			try{
				if(driver.findElement(By.className("android.widget.RelativeLayout")).isDisplayed())
					Util.takeScreenShot("");driver.findElement(By.id(prop.getProperty("selectoperator"))).click();
			}catch (Exception e){
				test.log(LogStatus.PASS, "Successfully Done PostPaidMobile Recharge");
				msg="Successfully Done PostPaidMobile Recharge"; fail= false;
				String message = driver.findElement(By.id("com.mindsarray.pay1:id/textViewMsg")).getText();
				test.log(LogStatus.PASS, message);
				String transid = message.replaceAll("[^0-9]", "");
				System.out.println(transid);
				driver.findElement(By.id("android:id/button1")).click();
				driver.findElement(By.className("android.widget.ImageButton")).click();
				new TransactionHistory(test, driver).checkTransaction(transid,"mobile bill");}
		} catch (Exception e){
			System.out.println("Error while doing PostPaid Mobile Recharge");
			test.log(LogStatus.ERROR, "Error while doing PostPaid Mobile Recharge");
			msg = "Error while doing PostPaid Mobile Recharge";fail= true;
			e.printStackTrace();
		}rep.endTest(test);
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
		fail=true; }

	@AfterTest
	public void reportTestResult(){
		if(skip)
			DataUtils.reportDataSetResult(xls, "TestCase",DataUtils.getRowNum(xls,this.getClass().getSimpleName()), "Skip");
		if(isTestPass)
			DataUtils.reportDataSetResult(xls, "TestCase",DataUtils.getRowNum(xls,this.getClass().getSimpleName()), "Pass");
		else
			DataUtils.reportDataSetResult(xls, "TestCase", DataUtils.getRowNum(xls,this.getClass().getSimpleName()), "Fail");
		rep.flush();
		isTestPass = false;}

	@DataProvider
	public Object[][] getData(){
		return DataUtils.getData(xls, this.getClass().getSimpleName(),this.getClass().getSimpleName());}
}
