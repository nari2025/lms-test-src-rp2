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
import org.openqa.selenium.support.ui.Select;

import jp.co.sss.lms.ct.util.WebDriverUtils;

/**
 * 結合テスト 勤怠管理機能
 * ケース12
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース12 受講生 勤怠直接編集 入力チェック")
public class Case12 {

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
	@DisplayName("テスト05 不適切な内容で修正してエラー表示：出退勤の（時）と（分）のいずれかが空白")
	void test05() {

		//定時ボタンをクリック
		final WebElement firstFixedTimeBtn = WebDriverUtils.webDriver
				.findElement(By.xpath("//td[text()='ハードウェア、ソフトウェア、WWW']/following-sibling::td//button[text()='定時']"));
		firstFixedTimeBtn.click();
		final WebElement secondFixedTimeBtn = WebDriverUtils.webDriver
				.findElement(By.xpath("//td[text()='アルゴリズム、フローチャート']/following-sibling::td//button[text()='定時']"));
		secondFixedTimeBtn.click();
		//出勤時の（時）を空白にする
		final WebElement startHour = WebDriverUtils.webDriver.findElement(By.id("startHour0"));
		Select dropdown = new Select(WebDriverUtils.webDriver.findElement(By.id("startHour0")));
		dropdown.selectByIndex(0);
		startHour.sendKeys(Keys.TAB);

		final WebElement attendance = WebDriverUtils.webDriver.findElement(By.xpath("//input[@value='更新']"));
		attendance.click();
		//アラートダイアログ操作
		Alert alert = WebDriverUtils.webDriver.switchTo().alert();
		//ダイアログ「OK」ボタン押下
		alert.accept();
		//エラーにより自画面遷移したことを確認
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		assertTrue(currentUrl.startsWith("http://localhost:8080/lms/attendance/update"));
		//属性値が変化しているか確認
		String classText = WebDriverUtils.webDriver.findElement(By.id("startHour0")).getAttribute("class");
		assertEquals("form-control errorInput", classText);
		//エラーメッセージが正しく表示されているか確認
		final WebElement errorMsgElement = WebDriverUtils.webDriver
				.findElement(By.xpath("//span[@class='help-inline error']"));
		String errorMsg = errorMsgElement.getText();
		assertEquals("* 出勤時間が正しく入力されていません。", errorMsg);
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 不適切な内容で修正してエラー表示：出勤が空白で退勤に入力あり")
	void test06() {
		//定時ボタンをクリック
		final WebElement firstFixedTimeBtn = WebDriverUtils.webDriver
				.findElement(By.xpath("//td[text()='ハードウェア、ソフトウェア、WWW']/following-sibling::td//button[text()='定時']"));
		firstFixedTimeBtn.click();
		final WebElement secondFixedTimeBtn = WebDriverUtils.webDriver
				.findElement(By.xpath("//td[text()='アルゴリズム、フローチャート']/following-sibling::td//button[text()='定時']"));
		secondFixedTimeBtn.click();
		//出勤時を空白にする
		final WebElement startHour = WebDriverUtils.webDriver.findElement(By.id("startHour0"));
		Select hourDropdown = new Select(WebDriverUtils.webDriver.findElement(By.id("startHour0")));
		hourDropdown.selectByIndex(0);
		startHour.sendKeys(Keys.TAB);
		final WebElement startMinute = WebDriverUtils.webDriver.findElement(By.id("startMinute0"));
		Select mintuteDropdown = new Select(WebDriverUtils.webDriver.findElement(By.id("startMinute0")));
		mintuteDropdown.selectByIndex(0);
		startMinute.sendKeys(Keys.TAB);

		final WebElement attendance = WebDriverUtils.webDriver.findElement(By.xpath("//input[@value='更新']"));
		attendance.click();
		//アラートダイアログ操作
		Alert alert = WebDriverUtils.webDriver.switchTo().alert();
		//ダイアログ「OK」ボタン押下
		alert.accept();
		//エラーにより自画面遷移したことを確認
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		assertTrue(currentUrl.startsWith("http://localhost:8080/lms/attendance/update"));
		//属性値が変化しているか確認
		String startHourClassText = WebDriverUtils.webDriver.findElement(By.id("startHour0")).getAttribute("class");
		assertEquals("form-control errorInput", startHourClassText);
		String startMinuteClassText = WebDriverUtils.webDriver.findElement(By.id("startMinute0")).getAttribute("class");
		assertEquals("form-control errorInput", startMinuteClassText);
		//エラーメッセージが正しく表示されているか確認
		final WebElement errorMsgElement = WebDriverUtils.webDriver
				.findElement(By.xpath("//span[@class='help-inline error']"));
		String errorMsg = errorMsgElement.getText();
		assertEquals("* 出勤情報がないため退勤情報を入力出来ません。", errorMsg);
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 不適切な内容で修正してエラー表示：出勤が退勤よりも遅い時間")
	void test07() {
		//定時ボタンをクリック
		final WebElement firstFixedTimeBtn = WebDriverUtils.webDriver
				.findElement(By.xpath("//td[text()='ハードウェア、ソフトウェア、WWW']/following-sibling::td//button[text()='定時']"));
		firstFixedTimeBtn.click();
		final WebElement secondFixedTimeBtn = WebDriverUtils.webDriver
				.findElement(By.xpath("//td[text()='アルゴリズム、フローチャート']/following-sibling::td//button[text()='定時']"));
		secondFixedTimeBtn.click();
		//出勤(時)を19に変更する
		final WebElement startHour = WebDriverUtils.webDriver.findElement(By.id("startHour0"));
		Select hourDropdown = new Select(WebDriverUtils.webDriver.findElement(By.id("startHour0")));
		hourDropdown.selectByIndex(20);
		startHour.sendKeys(Keys.TAB);

		final WebElement attendance = WebDriverUtils.webDriver.findElement(By.xpath("//input[@value='更新']"));
		attendance.click();
		//アラートダイアログ操作
		Alert alert = WebDriverUtils.webDriver.switchTo().alert();
		//ダイアログ「OK」ボタン押下
		alert.accept();
		//エラーにより自画面遷移したことを確認
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		assertTrue(currentUrl.startsWith("http://localhost:8080/lms/attendance/update"));
		getEvidence(new Object() {
		});
		//属性値が変化しているか確認
		String endHourClassText = WebDriverUtils.webDriver.findElement(By.id("endHour0")).getAttribute("class");
		assertEquals("form-control errorInput", endHourClassText);
		String endMinuteClassText = WebDriverUtils.webDriver.findElement(By.id("endMinute0")).getAttribute("class");
		assertEquals("form-control errorInput", endMinuteClassText);
		//エラーメッセージの確認
		final WebElement errorMsgElement = WebDriverUtils.webDriver
				.findElement(By.xpath("//span[@class='help-inline error']"));
		String errorMsg = errorMsgElement.getText();
		assertEquals("* 退勤時刻[0]は出勤時刻[0]より後でなければいけません。", errorMsg);
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 不適切な内容で修正してエラー表示：出退勤時間を超える中抜け時間")
	void test08() {
		//定時ボタンをクリック
		final WebElement firstFixedTimeBtn = WebDriverUtils.webDriver
				.findElement(By.xpath("//td[text()='ハードウェア、ソフトウェア、WWW']/following-sibling::td//button[text()='定時']"));
		firstFixedTimeBtn.click();
		final WebElement secondFixedTimeBtn = WebDriverUtils.webDriver
				.findElement(By.xpath("//td[text()='アルゴリズム、フローチャート']/following-sibling::td//button[text()='定時']"));
		secondFixedTimeBtn.click();
		//出勤(時)を17に変更する
		final WebElement startHour = WebDriverUtils.webDriver.findElement(By.id("startHour0"));
		Select hourDropdown = new Select(WebDriverUtils.webDriver.findElement(By.id("startHour0")));
		hourDropdown.selectByIndex(17);
		startHour.sendKeys(Keys.TAB);
		//中抜け時間を2時間に設定する
		WebElement rowElement = WebDriverUtils.webDriver.findElement(By.xpath("//td[text()='ハードウェア、ソフトウェア、WWW']/.."));
		List<WebElement> cellsInRow = rowElement.findElements(By.tagName("td"));
		WebElement blankTimeCell = cellsInRow.get(9).findElement(By.tagName("select"));
		Select blankTimeDropdown = new Select(
				cellsInRow.get(9).findElement(By.tagName("select")));
		blankTimeDropdown.selectByValue("120");
		blankTimeCell.sendKeys(Keys.TAB);
		final WebElement attendance = WebDriverUtils.webDriver.findElement(By.xpath("//input[@value='更新']"));
		attendance.click();
		//アラートダイアログ操作
		Alert alert = WebDriverUtils.webDriver.switchTo().alert();
		//ダイアログ「OK」ボタン押下
		alert.accept();
		getEvidence(new Object() {
		});
		//エラーにより自画面遷移したことを確認
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		assertTrue(currentUrl.startsWith("http://localhost:8080/lms/attendance/update"));
		//属性値が変化しているか確認
		WebElement newRowElement = WebDriverUtils.webDriver
				.findElement(By.xpath("//td[text()='ハードウェア、ソフトウェア、WWW']/.."));
		List<WebElement> newCellsInRow = newRowElement.findElements(By.tagName("td"));
		WebElement newBlankTimeCell = newCellsInRow.get(9).findElement(By.tagName("select"));

		String classText = newBlankTimeCell.getAttribute("class");
		assertEquals("form-control errorInput", classText);
		//エラーメッセージの確認
		final WebElement errorMsgElement = WebDriverUtils.webDriver
				.findElement(By.xpath("//span[@class='help-inline error']"));
		String errorMsg = errorMsgElement.getText();
		assertEquals("* 中抜け時間が勤務時間を超えています。", errorMsg);
	}

	@Test
	@Order(9)
	@DisplayName("テスト09 不適切な内容で修正してエラー表示：備考が100文字超")
	void test09() {
		//定時ボタンをクリック
		final WebElement firstFixedTimeBtn = WebDriverUtils.webDriver
				.findElement(By.xpath("//td[text()='ハードウェア、ソフトウェア、WWW']/following-sibling::td//button[text()='定時']"));
		firstFixedTimeBtn.click();
		final WebElement secondFixedTimeBtn = WebDriverUtils.webDriver
				.findElement(By.xpath("//td[text()='アルゴリズム、フローチャート']/following-sibling::td//button[text()='定時']"));
		secondFixedTimeBtn.click();

		//備考に文字を入力
		WebElement rowElement = WebDriverUtils.webDriver.findElement(By.xpath("//td[text()='ハードウェア、ソフトウェア、WWW']/.."));
		List<WebElement> cellsInRow = rowElement.findElements(By.tagName("td"));
		WebElement remarksCell = cellsInRow.get(11).findElement(By.tagName("input"));
		remarksCell.clear();
		remarksCell.sendKeys("0123456789abcdefghijklmnopqrstuvwxyz"
				+ "ABCDEFGHIJKLMNOPQRSTUVWXYZ１２３４５６７８９ａｂｃ"
				+ "ｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚＡＢ"
				+ "ＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺあ"
				+ "いうえおかきくけこさしすせそたちつてとなにぬねのは"
				+ "ひふへほまみむめもやゆよらりるれろわをんアイウエオ"
				+ "カキクケコサシスセソタチツテトナニヌネノハヒフヘホ"
				+ "マミムメモヤユヨラリルレロワヲン一二三四五六七八九"
				+ "十①②③④⑤⑥⑦⑧⑨⑩!-/:-@¥[-`{-~]*$");
		remarksCell.sendKeys(Keys.TAB);
		final WebElement attendance = WebDriverUtils.webDriver.findElement(By.xpath("//input[@value='更新']"));
		attendance.click();
		//アラートダイアログ操作
		Alert alert = WebDriverUtils.webDriver.switchTo().alert();
		//ダイアログ「OK」ボタン押下
		alert.accept();
		getEvidence(new Object() {
		});
		//エラーにより自画面遷移したことを確認
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		assertTrue(currentUrl.startsWith("http://localhost:8080/lms/attendance/update"));
		//属性値が変化しているか確認
		WebElement newRowElement = WebDriverUtils.webDriver
				.findElement(By.xpath("//td[text()='ハードウェア、ソフトウェア、WWW']/.."));
		List<WebElement> newCellsInRow = newRowElement.findElements(By.tagName("td"));
		WebElement newBlankTimeCell = newCellsInRow.get(11).findElement(By.tagName("input"));

		String classText = newBlankTimeCell.getAttribute("class");
		assertEquals("form-control errorInput", classText);
		//エラーメッセージの確認
		final WebElement errorMsgElement = WebDriverUtils.webDriver
				.findElement(By.xpath("//span[@class='help-inline error']"));
		String errorMsg = errorMsgElement.getText();
		assertEquals("* 備考の長さが最大値(100)を超えています。", errorMsg);
	}

}
