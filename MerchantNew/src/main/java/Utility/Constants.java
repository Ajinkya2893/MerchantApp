package Utility;

public class Constants {

	//Driver paths 
	public static final String ChromeDriver_path = "/home/pay1lap-51/Downloads/chromedriver.exe";
	public static final String GeckoDriver_path = System.getProperty("user.dir")+"/Drivers/GeckoDriver/geckodriver";
	public static final String InternetExplorer_Driver_path ="";
	//Properties file path
	public static final String Properties_file_path = System.getProperty("user.dir")+"/src/test/resources/Merchant.properties";
	public static final String ReProperties_file_path = System.getProperty("user.dir")+"/src/test/resources/Remit.properties";
	//ExcelSheets paths 
	public static final String Remit = System.getProperty("user.dir")+"//ExcelReader//Remit.xlsx";
	public static final String RetailerLogin = System.getProperty("user.dir")+"//ExcelReader//MerchantApp.xlsx";
	
	
	//All the Excel Related Values
	public static final String KEYWORDS_SHEET = "Keywords";
	
	public static final String TCID_COL = "TCID";
	public static final String KEYWORD_COL = "Keywords";
	public static final String OBJECT_COL = "Object";
	public static final String DATA_COL = "Data";
	public static final String RESULTS_COL = "Result";
	
	public static final String TESTCASES_SHEET = "TestCase";
	public static final String RUNMODE_COL = "Runmode";
	public static final String DESCRIPTION_COL = "Description";
	public static final String SCREENSHOT_PATH = System.getProperty("user.dir")+"//Screenshots//";
	public static final String REPORT_PATH = System.getProperty("user.dir")+"//Reports//";
	public static final String PASS = "PASS";
	public static final String FAIL = "FAIL";
	
}
