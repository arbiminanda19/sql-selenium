package Test;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class sqlUItest {

	public static void main(String args[]) {
		
		//database variable
		String databaseURL = "";
		String user = "";
		String password = "";
		String databaseName = "";
		
		//dashboard admin webpage url
		String dashboardAdminURL = "";
		
		//array to store data from database 
		String[] id = null;
		String[] userName = null;
		String[] srcBank = null;
		String[] destBank = null;
		String[] amount = null;
		
		WebDriver driver = null;
		
		//connect database and fetch all data in transaction database to array
		int count_row = 0;
		try {
			Class.forName(databaseName);
			Connection connection = DriverManager.getConnection(databaseURL, user, password);
			System.out.print("Connected to Database");
			String query = "select * from transaction";
			ResultSet value = connection.createStatement().executeQuery(query);
			while (value.next()) {
				id[count_row] = value.getString(1);
				userName[count_row] = value.getString(2);
				srcBank[count_row] = value.getString(3);
				destBank[count_row] = value.getString(4);
				amount[count_row] = value.getString(5);
				count_row = count_row + 1;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		
		//open dashboard admin webpage
		System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get(dashboardAdminURL);
		
		//verify the suitability between data per row on table in webpage and data from database by utilizing preceding and following xpath 
		int i = 0;
		while (i <= count_row) {
			WebElement id_element = driver.findElement(By.xpath("//table[contains(@id,'transaction')]/tbody/tr/td[contains(text(),'" + userName[i] + "')]/preceding::td[contains(text(),'" + id[i] + "')]"));
			WebElement userName_element = driver.findElement(By.xpath("//table[@id='transaction']/tbody/tr/td[contains(text(),'" + id[i] + "')]/following::td[contains(text(),'" + userName[i] + "')]"));
			WebElement srcBank_element = driver.findElement(By.xpath("//table[@id='transaction']/tbody/tr/td[contains(text(),'" + destBank[i] + "')]/preceding::td[contains(text(),'" + srcBank[i] + "')]"));
			WebElement destBank_element = driver.findElement(By.xpath("//table[@id='transaction']/tbody/tr/td[contains(text(),'" + srcBank[i] + "')]/following::td[contains(text(),'" + destBank[i] + "')]"));
			WebElement amount_element = driver.findElement(By.xpath("//table[@id='transaction']/tbody/tr/td[contains(text(),'" + userName[i] + "')]/following::td[contains(text(),'" + amount[i] + "')]"));
			assertEquals(true, id_element.isDisplayed());
			assertEquals(true, userName_element.isDisplayed());
			assertEquals(true, srcBank_element.isDisplayed());
			assertEquals(true, destBank_element.isDisplayed());
			assertEquals(true, amount_element.isDisplayed());
		}
	}
}