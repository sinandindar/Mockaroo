package mockaroo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import io.github.bonigarcia.wdm.WebDriverManager;

public class MockarooDataValidation {
	private static int N = 1001;
	private static MockarooDataValidation[] cityList = new MockarooDataValidation[N];		//This array holds all city objects
	public String city;
	public String country;

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	public MockarooDataValidation(String city, String country){
		this.city = city;
		this.country = country;
	}

	public static void main(String[] args) throws InterruptedException {
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		Actions action = new Actions(driver);

		//2
		driver.get("https://mockaroo.com/");

		//3
		String titleShould = "Mockaroo - Random Data Generator and API Mocking Tool | JSON / CSV / SQL / Excel";
		Assert.assertEquals(driver.getTitle(), titleShould);

		//4
		String mackaroo = "mockaroo";
		String realistic = "realistic data generator";
		Assert.assertEquals(driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/a/div[1]")).getText(), mackaroo);
		Assert.assertEquals(driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/a/div[2]")).getText(), realistic);

		//5
		for(int i = 1; i < 7; i++){
			driver.findElement(By.xpath("//*[@id=\"fields\"]/div[" + i + "]/div[5]/a")).click();
		}
		
		//6
		Assert.assertTrue(driver.findElement(By.xpath("//*[@id=\"schema_form\"]/div[2]/div[3]/div[1]/div[1]")).isDisplayed());
		Assert.assertTrue(driver.findElement(By.xpath("//*[@id=\"schema_form\"]/div[2]/div[3]/div[1]/div[2]")).isDisplayed());
		Assert.assertTrue(driver.findElement(By.xpath("//*[@id=\"schema_form\"]/div[2]/div[3]/div[1]/div[3]")).isDisplayed());
		
		//7
		Assert.assertTrue(driver.findElement(By.xpath("//*[@id=\"schema_form\"]/div[2]/div[3]/div[2]/a")).isEnabled());

		//8
		String thousand = "1000";
		String csv = "CSV";
		String lineEnding = "Unix (LF)";
		Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"num_rows\"]")).getAttribute("value"), thousand);

		//9
		Assert.assertEquals(driver.findElement(By.xpath("//select[@id='schema_file_format']/option[1]")).getText(), csv);

		//10
		Assert.assertEquals(driver.findElement(By.xpath("//select[@id='schema_line_ending']/option[1]")).getText(), lineEnding);

		//11
		Assert.assertTrue(driver.findElement(By.xpath("//*[@id=\"schema_include_header\"]")).isSelected());
		Assert.assertTrue(!driver.findElement(By.xpath("//*[@id=\"schema_bom\"]")).isSelected());

		//12
		driver.findElement(By.xpath("//*[@id=\"schema_form\"]/div[2]/div[3]/div[2]/a")).click();
		action.sendKeys("City").build().perform();

		//13
		driver.findElement(By.xpath("//*[@id=\"fields\"]/div[7]/div[3]/input[3]")).click();
		Thread.sleep(500);
		Assert.assertTrue(driver.findElement(By.xpath("//*[@id=\"type_dialog\"]/div/div")).isDisplayed());

		//14
		action.sendKeys("city").build().perform();
		driver.findElement(By.xpath("//*[@id=\"type_list\"]/div[1]")).click();

		//15
		Thread.sleep(500);
		driver.findElement(By.xpath("//*[@id=\"schema_form\"]/div[2]/div[3]/div[2]/a")).click();
		action.sendKeys("Country").build().perform();
		driver.findElement(By.xpath("//*[@id=\"fields\"]/div[8]/div[3]/input[3]")).click();
		Thread.sleep(500);
		Assert.assertTrue(driver.findElement(By.xpath("//*[@id=\"type_dialog\"]/div/div")).isDisplayed());
		action.sendKeys("country").build().perform();
		driver.findElement(By.xpath("//*[@id=\"type_list\"]/div[1]")).click();

		//16
		Thread.sleep(500);
		driver.findElement(By.xpath("//*[@id=\"download\"]")).click();

		//17
		Thread.sleep(1000);
		loadCities("C:/Users/sinan/Downloads/MOCK_DATA.csv");

		//18
		Assert.assertEquals(cityList[0].getCity(), "City");
		Assert.assertEquals(cityList[0].getCountry(), "Country");

		//19
		Assert.assertTrue(cityList.length == 1001);

		//20 & 21
		ArrayList<String> Cities = new ArrayList<String>();
		ArrayList<String> Countries = new ArrayList<String>();
		for(int i = 1; i < cityList.length; i++) {
			Cities.add(i - 1, cityList[i].getCity());
			Countries.add(i - 1, cityList[i].getCountry());
		}

		//22
		Collections.sort(Cities);
		
		String longest = Cities.get(0);
		for(String str : Cities) {
			if (str.length() > longest.length()) {
				longest = str;
			}
		}
		System.out.println("The longest name: " + longest);

		String shortest = Cities.get(0);
		for(String str : Cities) {
			if (str.length() < shortest.length()) {
				shortest = str;
			}
		}
		System.out.println("The shortest name: " + shortest);

		//23
		Collections.sort(Countries);
		ArrayList<String> Counted = new ArrayList<String>();
		for(int i = 0; i < Countries.size(); i++) {
			if(Counted.contains(Countries.get(i))) continue;
			int c = 0;
			Counted.add(Countries.get(i));
			for(int j = 0; j < Countries.size(); j++) {
				if(Countries.get(j).equals(Countries.get(i))) {
					c++;
				}
			}
			System.out.println(Countries.get(i) + " - " + c);
		}

		//24
		Set<String> CitiesHashSet = new HashSet<>(Cities);

		//25
		Assert.assertEquals(CitiesHashSet.size(), countThis(Cities));

		//26
		Set<String> CountriesHashSet = new HashSet<String>(Countries);

		//27
		Assert.assertEquals(CountriesHashSet.size(), countThis(Countries));
	}
	
	private static void loadCities(String fileName){
		String line = null;
		int count = 0;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader =  new BufferedReader(fileReader);
			while((line = bufferedReader.readLine()) != null) {
				String[] c = line.split(",");
				String city = c[0];
				String country = c[1];
				MockarooDataValidation ct = new MockarooDataValidation(city, country);
				cityList[count++] = ct;
			}   
			bufferedReader.close(); 
		}
		catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");                
		}
		catch(IOException ex) {
			System.out.println("Error reading file '" + fileName + "'"); 
		}
	}

	public static int countThis(ArrayList<String> arrayList) {
		int total = 0;
		ArrayList<String> Counted = new ArrayList<String>();
		for(int i = 0; i < arrayList.size(); i++) {
			if(Counted.contains(arrayList.get(i))) continue;
			Counted.add(arrayList.get(i));
			total++;
		}
		return total;
	}
}