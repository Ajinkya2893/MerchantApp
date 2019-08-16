package com.Merchant.Reports;

import java.io.FileInputStream;
import java.util.Properties;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.BaseClass.AppBase;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import Utility.Constants;
import Utility.Excel_Reader;
import Utility.ExtentManager;
import Utility.Utility;
import io.appium.java_client.AppiumDriver;

public class TransactionHistory extends AppBase{
	
	public TransactionHistory(ExtentTest test, AppiumDriver<?> driver){
		try {
			prop = new Properties();
			FileInputStream fs = new FileInputStream(Constants.Properties_file_path); //Object repository path
			prop.load(fs);
			xls = new Excel_Reader(Constants.RetailerLogin); // Loading the Excel Sheet 
			rep = ExtentManager.getInstance(); // Getting the Extent Report Instance 
			this.test = test = rep.startTest(this.getClass().getSimpleName()); //Starting the Extent Report test
			this.driver=driver;
			Util = new Utility(test, driver);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Unable to run this class and create any instance");
			
		}
	}

	public void navigateToRepoorts(String service) throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.xpath("//android.widget.TextView[@text='Reports']")).click(); //Report section click
		driver.findElement(By.xpath("//android.widget.TextView[@text='Transaction History']")).click();
		if(service.equalsIgnoreCase("mobile"))driver.findElement(By.xpath("//android.widget.TextView[@text='Mobile']")).click();
		else if(service.equalsIgnoreCase("Dth"))driver.findElement(By.xpath("//android.widget.TextView[@text='Dth']")).click();
		else if(service.equalsIgnoreCase("mobile bill"))driver.findElement(By.xpath("//android.widget.TextView[@text='Mobile Bill']")).click();
		else if(service.equalsIgnoreCase("Utility Bill"))driver.findElement(By.xpath("//android.widget.TextView[@text='Utility Bill']")).click();
		else if(service.equalsIgnoreCase("Online Payment"))driver.findElement(By.xpath("//android.widget.TextView[@text='Online Payment']")).click();
	}

	@Test
	public void checkTransaction(String trnsid, String service) throws InterruptedException {
		navigateToRepoorts(service);
		if(driver.findElement(By.xpath("//android.widget.RelativeLayout[@index='0']")).isDisplayed()) {//Check the transaction 
			String id = driver.findElement(By.id("com.mindsarray.pay1:id/transactionID")).getText();
			String transid = id.replaceAll("[^0-9]", "");
			Assert.assertEquals(trnsid, transid); //Asserting the Number
			Util.takeScreenShot("Transaction ID matched");
			test.log(LogStatus.PASS, "Tramsaction Confirmed");
		}else {
			System.out.println("No Transaction Done");
			Util.takeScreenShot("");
		}
	}

}
