package com.Merchant.Login;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;

public class test {


	@SuppressWarnings("rawtypes")
	AndroidDriver driver;

	@SuppressWarnings("rawtypes")
	@BeforeTest
	public void setUp() throws MalformedURLException {
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

		// Set android appPackage desired capability.
		// Set your application's appPackage if you are using any other app.
		capabilities.setCapability("appPackage", "com.mindsarray.pay1");

		// Set android appActivity desired capability. It is
		// Set your application's appPackage if you are using any other app.
		capabilities.setCapability("appActivity", "com.mindsarray.pay1.ui.intro.SplashActivity");

		// Created object of RemoteWebDriver will all set capabilities.
		// Set appium server address and port number in URL string.
		// It will launch calculator app in android device.
		driver = new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub"), capabilities);
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
	}

	@Test(priority=1)
	public void SwipeScreens() throws InterruptedException {
		Thread.sleep(4000);
		/*//Left
		new TouchAction(driver).longPress(250, 1200).moveTo(900, 1200).release().perform();*/

		for (int i=0 ; i<3;) {
			//Right
			new TouchAction(driver).longPress(1000, 450).moveTo(100, 450).release().perform();
			i++;
		}
		new TouchAction(driver).press(1000, 450).release().perform();
	}

	@Test(priority=2)
	public void navigateToLogin() throws InterruptedException {
		Thread.sleep(4000);
		driver.findElement(By.className("android.widget.ImageButton")).click(); //Clicking on menu tab
		Thread.sleep(2000);
		driver.findElement(By.xpath("//android.widget.CheckedTextView[@text='Login']")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("com.android.packageinstaller:id/permission_allow_button")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("com.android.packageinstaller:id/permission_deny_button")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("com.android.packageinstaller:id/permission_deny_button")).click();
	}

	@Test(priority=3)
	public void getLogin() throws InterruptedException {
		Thread.sleep(1000);
		driver.findElement(By.id("com.mindsarray.pay1:id/userNameEditText")).click();
		driver.findElement(By.id("com.mindsarray.pay1:id/userNameEditText")).sendKeys("9819042543");
		driver.findElement(By.id("com.mindsarray.pay1:id/passwordEditText")).sendKeys("12345");
		driver.hideKeyboard();
		Thread.sleep(2000);
		driver.findElement(By.id("com.mindsarray.pay1:id/loginTextView")).click();
	}

	//@Test(priority=4)
	public void mobileRecharge() throws InterruptedException {
		Thread.sleep(3000);
		driver.findElement(By.xpath("//android.widget.TextView[@text='Mobile']")).click();
		//driver.findElement(By.xpath("android.widget.TextView[@text='Airtel']")).click();//Airtel
		driver.findElement(By.xpath("//android.widget.TextView[@text='Idea']")).click();//Idea change any service provider needed
		Thread.sleep(1000);
		driver.findElement(By.id("com.mindsarray.pay1:id/input_mobile")).sendKeys("9967903705");
		driver.findElement(By.id("com.mindsarray.pay1:id/input_amount")).sendKeys("10");
		//driver.hideKeyboard();
		Thread.sleep(1000);
		driver.findElement(By.id("com.mindsarray.pay1:id/rechargeButton")).click();
		System.out.println(driver.findElement(By.id("com.mindsarray.pay1:id/textViewMsg")).getText());// Pop confirmation
		driver.findElement(By.id("android:id/button2")).click();//Enter button1 for confirming the amount
	
	
	}

	//@Test(priority=5)
	public void dthRecharge() throws InterruptedException {
		Thread.sleep(3000);
		driver.findElement(By.xpath("//android.widget.TextView[@text='Mobile']")).click();
		driver.findElement(By.xpath("//android.widget.TextView[@text='MOBILE POSTPAID']")).click();
		driver.findElement(By.xpath("//android.widget.TextView[@text='Tata Docomo']")).click();
	}
	
	//@Test(priority=6)
	public void getWater()throws InterruptedException {
		Thread.sleep(3000);
		driver.findElement(By.xpath("//android.widget.TextView[@text='Water']")).click();
		driver.findElement(By.xpath("//android.widget.TextView[@text='Delhi Jal Board']")).click();
		driver.findElement(By.xpath("//android.widget.LinearLayout[@index='0']")).sendKeys("");
		driver.findElement(By.xpath("//android.widget.LinearLayout[@index='1']")).sendKeys("");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//android.widget.Button[@text='FETCH BILL']")).click();
		
	}
	
	@Test
	public void getElectrcity() throws InterruptedException {
		driver.findElement(By.xpath("//android.widget.TextView[@text='Electricity']")).click();
		driver.findElement(By.xpath("//android.widget.TextView[@text='Reliance Energy (Mumbai)']")).click();
		driver.findElement(By.xpath("//android.widget.LinearLayout[@index='0']")).sendKeys("");
		driver.findElement(By.xpath("//android.widget.LinearLayout[@index='1']")).sendKeys("");
		driver.findElement(By.xpath("//android.widget.LinearLayout[@index='2']")).sendKeys("");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//android.widget.Button[@text='FETCH BILL']")).click();
	}
	
	@Test
	public void getGas() throws InterruptedException {
		driver.findElement(By.xpath("//android.widget.TextView[@text='Gas']")).click();
		driver.findElement(By.xpath("//android.widget.TextView[@text='Mahanagar Gas Limited']")).click();
		driver.findElement(By.xpath("//android.widget.LinearLayout[@index='0']")).sendKeys("");
		driver.findElement(By.xpath("//android.widget.LinearLayout[@index='1']")).sendKeys("");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//android.widget.Button[@text='FETCH BILL']")).click();
	}
	
	@Test
	public void LandLine() throws InterruptedException {
		driver.findElement(By.xpath("//android.widget.TextView[@text='Landline']")).click();
		driver.findElement(By.xpath("//android.widget.TextView[@text='Airtel']")).click();
		driver.findElement(By.xpath("//android.widget.LinearLayout[@index='0']")).sendKeys("");
		driver.findElement(By.xpath("//android.widget.LinearLayout[@index='1']")).sendKeys("");
		driver.findElement(By.xpath("//android.widget.LinearLayout[@index='2']")).sendKeys("");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//android.widget.Button[@text='FETCH BILL']")).click();
	}
	
	@Test
	public void Dmt() {
		driver.findElement(By.id("/home/pay1lap-51/android-sdks")).sendKeys("9819042543");
		driver.findElement(By.id("com.mindsarray.pay1:id/iconSelectBene")).click(); //Select bene
		driver.findElement(By.xpath("//android.widget.RelativeLayout[index='1']")).click() ; //select beneficiaries number starts from 0
		driver.findElement(By.id("com.mindsarray.pay1:id/amountTextView")).sendKeys("100"); //Amount
		
		driver.findElement(By.xpath("//android.widget.RadioButton[@text='IMPS']")).click(); //Radio button
		driver.findElement(By.id("com.mindsarray.pay1:id/proceedButton")).click(); // Proceed
	
		///////////////////////////add ben
		driver.findElement(By.xpath("//android.widget.TextView[text='Add beneficiary']")).click(); //clicking on add ben
		driver.findElement(By.id("com.mindsarray.pay1:id/input_account_number")).sendKeys("account number");
		driver.findElement(By.id("com.mindsarray.pay1:id/input_bank_name")).sendKeys("bankNam");
		driver.findElement(By.id("com.mindsarray.pay1:id/input_mobile_number")).sendKeys("mobile number");
		
		driver.findElement(By.id("com.mindsarray.pay1:id/btn_bene_proceed")).click(); //Procee
		//driver.findElement(By.id("com.mindsarray.pay1:id/balanceTextView")).getText();  check balance 
		
		
		driver.findElement(By.xpath("//android.widget.LinearLayout[@index='1']")).click(); //Manage
		
		driver.findElement(By.xpath("//android.widget.LinearLayout[@index='3']")).click(); //Report
	}
	
	@Test
	public void Settings() {
		
	}
}
