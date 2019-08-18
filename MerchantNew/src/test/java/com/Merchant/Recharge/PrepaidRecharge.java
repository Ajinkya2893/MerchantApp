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

public class PrepaidRecharge extends AppBase
{

	static int count=-1;
	static boolean skip=false;
	static boolean pass = false;
	static boolean fail=true;
	static boolean isTestPass=false;
	public String msg = "";

	private PrepaidRecharge(){
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
			
			//Clicking the <mobile> and selecting the product <IDEA>
			Util.getElement("mobileIcon_xpath").click();	test.log(LogStatus.INFO, "Clicking on MobileIcon");
			Util.getElement("selectProduct_xpath").click();		test.log(LogStatus.INFO, "Selecting the Product"); //Idea change any service provider needed
			Util.waitfor("1000");
			
			//Entering the <Mobile number> and <AMount>
			Util.getElement("mobileNumber_id").sendKeys(data.get("Mobile"));	test.log(LogStatus.INFO, "Entering the MobileNumber");
			Util.getElement("amountValue_id").sendKeys(data.get("Amount")); 	test.log(LogStatus.INFO, "Entering the Amount");
			//driver.hideKeyboard();
			Util.waitfor("1000");
			Util.getElement("rechargeButton_id").click();			test.log(LogStatus.INFO, "Clicking on Recharge button");
			
			System.out.println(Util.getElement("confirmationBox_id").getText());// Pop confirmation
			
			Util.getElement("confirmOkButton_id").click();
			Util.takeScreenShot("Capturing the Confirmation box");//Enter button1 for confirming the amount
			
			
			if(driver.findElement(By.className("android.widget.RelativeLayout")).isDisplayed()){
				Util.takeScreenShot("");
				driver.findElement(By.id(prop.getProperty("selectoperator"))).click();
			}
			driver.findElement(By.id("android:id/button1")).click();
			test.log(LogStatus.PASS, "Successfully Done PrepaidMobile Recharge");
			msg="Successfully Done PrepaidMobile Recharge"; fail= false;
			String message = driver.findElement(By.id("com.mindsarray.pay1:id/textViewMsg")).getText();
			test.log(LogStatus.PASS, message);
			String transid = message.replaceAll("[^0-9]", "");
			System.out.println(transid);
			driver.findElement(By.className("android.widget.ImageButton")).click();
			new TransactionHistory(test, driver).checkTransaction(transid,"mobile");
		} catch (Exception e){
			System.out.println("Error while doing PrepaidMobile Recharge");
			test.log(LogStatus.ERROR, "Error while doing PrepaidMobile Recharge");
			msg = "Error while doing DTH Recharge";fail= true;
			e.printStackTrace();
		}
		rep.endTest(test);
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
		rep.flush();
		isTestPass = false;
	}

	@DataProvider
	public Object[][] getData(){
		return DataUtils.getData(xls, this.getClass().getSimpleName(),this.getClass().getSimpleName());
	}

}
