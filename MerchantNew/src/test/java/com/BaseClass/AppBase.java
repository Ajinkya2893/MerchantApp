package com.BaseClass;

import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.SkipException;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import Utility.Constants;
import Utility.Excel_Reader;
import Utility.ExtentManager;
import Utility.Utility;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;

public class AppBase {

	public static boolean isinitialized = false;
	public static Excel_Reader xls;
	public ExtentReports rep;
	public ExtentTest test;
	public Properties prop;
	@SuppressWarnings("rawtypes")
	public AndroidDriver driver;
	public static Utility Util;
	public boolean testend = true;

	public void initialize(Excel_Reader xls, String testName) {
		if(!isinitialized) {
			xls = new Excel_Reader(Constants.Remit);
			rep = ExtentManager.getInstance();
			System.out.println("Initialized the ExcelReader and Logger");
			isinitialized = true;
		}
	}	

	/******************************Opening a Mobile App******************************/
	@SuppressWarnings("rawtypes")
	public void LaunchApp() {
		try {
			// Created object of DesiredCapabilities class.
			DesiredCapabilities capabilities = new DesiredCapabilities();

			// Set android deviceName desired capability. Set your device name.
			capabilities.setCapability("deviceName", "TOQOPBH6AYBQCIP7");

			// Set BROWSER_NAME desired capability. It's Android in our case here.
			capabilities.setCapability(CapabilityType.BROWSER_NAME, "Android");

			// Set android VERSION desired capability. Set your mobile device's OS version.
			capabilities.setCapability(CapabilityType.VERSION, "6.0");

			// Set android platformName desired capability. It's Android in our case here.
			capabilities.setCapability("platformName", "Android");

			// Set android appPackage desired capability. It is
			// com.android.calculator2 for calculator application.
			// Set your application's appPackage if you are using any other app.
			capabilities.setCapability("appPackage", "com.mindsarray.pay1");

			// Set android appActivity desired capability. It is
			// com.android.calculator2.Calculator for calculator application.
			// Set your application's appPackage if you are using any other app.
			capabilities.setCapability("appActivity", "com.mindsarray.pay1.ui.intro.SplashActivity");

			// Created object of RemoteWebDriver will all set capabilities.
			// Set appium server address and port number in URL string.
			// It will launch calculator app in android device.
			driver = new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub"), capabilities);
			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			test.log(LogStatus.PASS, "Successfully Launched the Application");

		}catch (Exception e){
			System.out.println("Unable to open the Application");
			test.log(LogStatus.FAIL, "Unable to open the Application");
			e.printStackTrace();
		}
	}

	public void SwipeScreens() throws InterruptedException {
		try {
			Thread.sleep(2000);
			for (int i=0 ; i<3; i++) new TouchAction(driver).longPress(1000, 450).moveTo(120, 450).release().perform();
			test.log(LogStatus.INFO, "Going through the intro Screen");Thread.sleep(2000);
			new TouchAction(driver).longPress(1000, 450).moveTo(700, 450).perform().release();}
		catch (Exception e) {
			System.out.println("Unable to Swipe through the Screens");
			test.log(LogStatus.ERROR, "Unable to Swipe through the Screens");
			e.printStackTrace();
		}
	}

	public void navigateToLogin() throws InterruptedException {
		try {
			test.log(LogStatus.INFO, "Navigated to DashBoard");
			driver.findElement(By.className(prop.getProperty("menuTab"))).click(); test.log(LogStatus.INFO, "Clicking on Menu Tab");
			driver.findElement(By.xpath(prop.getProperty("LoginButton"))).click(); test.log(LogStatus.INFO, "Clicking on Login Button");
			Thread.sleep(1000);
			driver.findElement(By.id("com.android.packageinstaller:id/permission_allow_button")).click(); 
			Thread.sleep(1000);
			driver.findElement(By.id("com.android.packageinstaller:id/permission_deny_button")).click();
			Thread.sleep(1000);
			driver.findElement(By.id("com.android.packageinstaller:id/permission_deny_button")).click();
			test.log(LogStatus.PASS, "Successfully Navigated to Login Screen");}
		catch (Exception e) {
			System.out.println("Unable to Navigate to Dash Board"); test.log(LogStatus.ERROR, "Unable to Navigate to Dash Board");
			e.printStackTrace();
		}
	}

	//@AfterSuite
	public void closure() throws InterruptedException{
		if(testend) {
			Thread.sleep(4000);
			driver.quit();
			rep.flush();
			//rep.close();
			testend =false;
		}else {
			testend= true;
			System.out.println("Unable to end the test under exigustion");
			throw new SkipException("Unable to end the test under exigustion");
		}
	}

}
