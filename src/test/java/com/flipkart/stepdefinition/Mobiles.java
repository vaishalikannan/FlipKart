package com.flipkart.stepdefinition;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Mobiles {

	public static WebDriver driver;
	public static int price;
	public String  Lowestpricemodel;
	List<String> ModelName=new LinkedList<String>();
	List<String> PriceName=new LinkedList<String>();

	@Given("user need to launch the browser")
	public void user_need_to_launch_the_browser() {

		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.get("https://www.flipkart.com/");
		driver.manage().window().maximize();
	}

	@Given("user need to handle the login option")
	public void user_need_to_handle_the_login_option() {
		try
		{
			WebElement loginoption = driver.findElement(By.xpath("(//button)[2]"));
			if(loginoption.isDisplayed())
			{
				loginoption.click();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	@Given("user need to click the search option")
	public void user_need_to_click_the_search_option() throws InterruptedException {

		WebElement search = driver.findElement(By.xpath("//input[@type='text']"));
		search.click();
		search.sendKeys("RedmiMobiles",Keys.ENTER);
		
		Thread.sleep(3000);

	}

	@When("user need to get the name of mobiles with price,write the result in excel")
	public void user_need_to_get_the_name_of_mobiles_with_price_write_the_result_in_excel() throws IOException {

		XSSFWorkbook w = new XSSFWorkbook();
		XSSFSheet s = w.createSheet("MobilesData");
		List<WebElement> modelname = driver.findElements(By.xpath("//div[@class='_4rR01T']"));
		List<WebElement> modelprice = driver.findElements(By.xpath("//div[@class='_30jeq3 _1_WHN1']"));
		for (WebElement redmi : modelprice) {
			String price1= redmi.getText();
			String redmiprice = price1.replaceAll("[^0-9]", "");
			PriceName.add(redmiprice);
		}
		for (WebElement redmi1 : modelname) {
			String name1 = redmi1.getText();
			ModelName.add(name1);
		}
		for (int i = 0; i < modelname.size(); i++) 
		{


			XSSFRow Row = s.createRow(i);
			XSSFCell Cell = Row.createCell(0);
			Cell.setCellValue(ModelName.get(i));

			XSSFCell Cell1 = Row.createCell(1);
			Cell1.setCellValue(PriceName.get(i));
			System.out.println(ModelName.get(i)+" "+PriceName.get(i));


		}
		FileOutputStream op = new FileOutputStream("./target/FlipkartMobiles2.xlsx");
		w.write(op);
		op.close();


	}




	@When("user need to print the lowest value and search the mobile")
	public void user_need_to_print_the_lowest_value_and_search_the_mobile() throws IOException {

		FileInputStream ff = new FileInputStream("C:\\Users\\Vinoth\\Desktop\\Vinoth\\task\\target\\FlipkartMobiles2.xlsx");
		XSSFWorkbook ww = new XSSFWorkbook(ff);
		XSSFSheet ss = ww.getSheet("MobilesData");
		Set<Integer> Leastvalue = new TreeSet<Integer>();
		int k=0;
		for (int i = 0; i < ModelName.size(); i++) {

			XSSFRow row1 = ss.getRow(i);
			XSSFCell cell1 = row1.getCell(1);
			String str = cell1.getStringCellValue();
			int least = Integer.parseInt(str);
			Leastvalue.add(least);
		}

		for (Integer leastprice : Leastvalue) {
			System.out.println(leastprice);
			k=leastprice;
			break;

		}

		String Leastprice = String.valueOf(k);
		for (int i = 0; i < ModelName.size(); i++) {

			XSSFRow row1 = ss.getRow(i);
			XSSFCell cell1 = row1.getCell(1);
			String Pstr = cell1.getStringCellValue();

			XSSFCell cell2 = row1.getCell(0);
			String Mstr = cell2.getStringCellValue();
			if(Pstr.equals(Leastprice))
			{
				System.out.println(Mstr);
				Lowestpricemodel=Mstr;
				break;
			}
		}
	}	

	@When("user click on the lowest mobile.")
	public void user_click_on_the_lowest_mobile() throws InterruptedException {
		Thread.sleep(3000);
		
		String leastmodel="//div[text()='"+Lowestpricemodel+"']";
	
		WebElement Searchleast = driver.findElement(By.xpath(leastmodel));
		Searchleast.click();
		String Parentwindow=driver.getWindowHandle();
		Set<String> Parent = driver.getWindowHandles();
		for (String window : Parent) {
			if(!Parentwindow.equals(window))
			{
				driver.switchTo().window(window);
				break;
			}

		}

	}


	@Then("user validate the output.")
	public void user_validate_the_output() throws IOException {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File screenshot = ts.getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screenshot, new File("./target/RedmiLeastFLipkart/Screenshot.png"));
		driver.close();
		driver.quit();
	}
}