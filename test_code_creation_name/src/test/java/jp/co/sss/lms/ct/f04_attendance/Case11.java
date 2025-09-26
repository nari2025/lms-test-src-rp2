package jp.co.sss.lms.ct.f04_attendance;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import jp.co.sss.lms.ct.util.WebDriverUtils;

/**
 * 結合テスト 勤怠管理機能
 * ケース11
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース11 受講生 勤怠直接編集 正常系")
public class Case11 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		// 指定のURLの画面を開く
		goTo("http://localhost:8080/lms/");
		//遷移したURLが正しいか確認
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		assertEquals("http://localhost:8080/lms/", currentUrl);
		//タイトルが正しいか確認
		String pageTitleString = WebDriverUtils.webDriver.getTitle();
		assertEquals("ログイン | LMS", pageTitleString);

		// ページのキャプチャを取得する
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {
		final WebElement loginId = WebDriverUtils.webDriver.findElement(By.id("loginId"));
		final WebElement password = WebDriverUtils.webDriver.findElement(By.id("password"));
		//初回ログイン済みのログインIDを入力し、TABキーを押下
		loginId.clear();
		loginId.sendKeys("StudentAA01");
		loginId.sendKeys(Keys.TAB);
		//初回ログイン済みのパスワードを入力し、Enterキーを押下
		password.clear();
		password.sendKeys("testTest1234");
		password.sendKeys(Keys.ENTER);

		// 遷移先のURLを取得し、正しいか確認
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		assertTrue(currentUrl.startsWith("http://localhost:8080/lms/course/detail"));
		pageLoadTimeout(10);
		//ユーザー名が表示されているか確認
		WebElement smallElement = WebDriverUtils.webDriver.findElement(By.tagName("small"));
		String userName = smallElement.getText();
		assertEquals("ようこそ受講生ＡＡ１さん", userName);
		// ページのキャプチャを取得する
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「勤怠」リンクから勤怠管理画面に遷移")
	void test03() {
		//勤怠リンクをクリック
		final WebElement attendance = WebDriverUtils.webDriver.findElement(By.linkText("勤怠"));
		attendance.click();
		pageLoadTimeout(10);
		//アラートダイアログ操作
		Alert alert = WebDriverUtils.webDriver.switchTo().alert();
		//ダイアログ「OK」ボタン押下
		alert.accept();
		pageLoadTimeout(10);
		//遷移先のURLを確認
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		assertTrue(currentUrl.startsWith("http://localhost:8080/lms/attendance/detail"));
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「勤怠情報を直接編集する」リンクから勤怠情報直接変更画面に遷移")
	void test04() {
		//勤怠リンクをクリック
		final WebElement attendance = WebDriverUtils.webDriver.findElement(By.linkText("勤怠情報を直接編集する"));
		attendance.click();
		//遷移先のURLを確認
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		assertTrue(currentUrl.startsWith("http://localhost:8080/lms/attendance/update"));
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 すべての研修日程の勤怠情報を正しく更新し勤怠管理画面に遷移")
	void test05() {

		//定時ボタンを押下する
		final WebElement firstFixedTimeBtn = WebDriverUtils.webDriver
				.findElement(By.xpath("//td[text()='ハードウェア、ソフトウェア、WWW']/following-sibling::td//button[text()='定時']"));
		firstFixedTimeBtn.click();
		final WebElement secondFixedTimeBtn = WebDriverUtils.webDriver
				.findElement(By.xpath("//td[text()='アルゴリズム、フローチャート']/following-sibling::td//button[text()='定時']"));
		secondFixedTimeBtn.click();
		//更新ボタンを押下する
		final WebElement attendance = WebDriverUtils.webDriver.findElement(By.xpath("//input[@value='更新']"));
		attendance.click();
		//アラートダイアログ操作
		Alert alert = WebDriverUtils.webDriver.switchTo().alert();
		//ダイアログ「OK」ボタン押下
		alert.accept();
		//入力された時刻が反映されているか確認 
		WebElement firstRowElement = WebDriverUtils.webDriver
				.findElement(By.xpath("//td[text()='ハードウェア、ソフトウェア、WWW']/.."));
		List<WebElement> firstCellsInRow = firstRowElement.findElements(By.tagName("td"));
		WebElement firstPunchInCell = firstCellsInRow.get(2);
		String firstPunchInTime = firstPunchInCell.getText();
		assertEquals("09:00", firstPunchInTime);
		//入力された時刻が反映されているか確認 
		WebElement firstPunchOutCell = firstCellsInRow.get(3);
		String firstPunchOutTime = firstPunchOutCell.getText();
		assertEquals("18:00", firstPunchOutTime);

		WebElement secondRowElement = WebDriverUtils.webDriver
				.findElement(By.xpath("//td[text()='アルゴリズム、フローチャート']/.."));
		List<WebElement> secondCellsInRow = secondRowElement.findElements(By.tagName("td"));
		WebElement secondPunchInCell = secondCellsInRow.get(2);
		String secondPunchInTime = secondPunchInCell.getText();
		assertEquals("09:00", secondPunchInTime);
		WebElement secondPunchOutCell = secondCellsInRow.get(3);
		String secondPunchOutTime = secondPunchOutCell.getText();
		assertEquals("18:00", secondPunchOutTime);

		//遷移先のURLを確認
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		assertTrue(currentUrl.startsWith("http://localhost:8080/lms/attendance/update"));
		getEvidence(new Object() {
		});

	}

}
