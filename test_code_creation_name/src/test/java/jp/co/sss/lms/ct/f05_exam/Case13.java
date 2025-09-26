package jp.co.sss.lms.ct.f05_exam;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.Assert.*;

import java.util.Date;
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
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import jp.co.sss.lms.ct.util.WebDriverUtils;

/**
 * 結合テスト 試験実施機能
 * ケース13
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース13 受講生 試験の実施 結果0点")
public class Case13 {

	/** テスト07およびテスト08 試験実施日時 */
	static Date date;

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
	@DisplayName("テスト03 「試験有」の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {
		// ”アルゴリズム、フローチャート”の行にあるすべてのセルを取得
		WebElement rowElement = WebDriverUtils.webDriver.findElement(By.xpath("//td[text()='アルゴリズム、フローチャート']/.."));
		List<WebElement> cellsInRow = rowElement.findElements(By.tagName("td"));
		WebElement thirdCell = cellsInRow.get(3);

		String submitReport = thirdCell.getText();
		if (!(submitReport.equals("試験有"))) {
			System.out.println("この研修に試験はありません。");
		} else {
			WebElement fourthCell = cellsInRow.get(4);
			fourthCell.click();
			//遷移先のURLを確認
			String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
			assertTrue(currentUrl.startsWith("http://localhost:8080/lms/section/detail"));
			// ページのキャプチャを取得する
			getEvidence(new Object() {
			});
		}
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「本日の試験」エリアの「詳細」ボタンを押下し試験開始画面に遷移")
	void test04() {
		// 詳細ボタンをクリック
		WebElement rowElement = WebDriverUtils.webDriver.findElement(By.xpath("//td[text()='ITリテラシー①']/.."));
		List<WebElement> cellsInRow = rowElement.findElements(By.tagName("td"));
		WebElement secondCell = cellsInRow.get(1).findElement(By.tagName("input"));
		secondCell.click();
		//遷移先のURLを確認
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		assertTrue(currentUrl.startsWith("http://localhost:8080/lms/exam/start"));
		// ページのキャプチャを取得する
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 「試験を開始する」ボタンを押下し試験問題画面に遷移")
	void test05() {
		//試験を開始するボタンを押下する
		WebElement startElement = WebDriverUtils.webDriver.findElement(By.xpath("//form//input[@value='試験を開始する']"));
		startElement.click();
		//遷移先のURLを確認
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		assertTrue(currentUrl.startsWith("http://localhost:8080/lms/exam/question"));
		// ページのキャプチャを取得する
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 未回答の状態で「確認画面へ進む」ボタンを押下し試験回答確認画面に遷移")
	void test06() throws InterruptedException {
		//最下部までスクロール
		JavascriptExecutor js = (JavascriptExecutor) WebDriverUtils.webDriver;
		js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

		Thread.sleep(5000);
		//確認画面へ進むボタンを押下する
		WebElement confirmationBtn = WebDriverUtils.webDriver.findElement(By.xpath("//input[@value='確認画面へ進む']"));
		confirmationBtn.click();
		//遷移先のURLを確認
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		assertTrue(currentUrl.startsWith("http://localhost:8080/lms/exam/answerCheck"));
		// ページのキャプチャを取得する
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 「回答を送信する」ボタンを押下し試験結果画面に遷移")
	void test07() throws InterruptedException {
		//最下部までスクロール
		JavascriptExecutor js = (JavascriptExecutor) WebDriverUtils.webDriver;
		js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

		Thread.sleep(5000);
		//回答を送信するボタンを押下する
		WebElement answerBtn = WebDriverUtils.webDriver.findElement(By.xpath("//button[text()='回答を送信する']/.."));
		answerBtn.click();
		//アラートダイアログ操作
		Alert alert = WebDriverUtils.webDriver.switchTo().alert();
		//ダイアログ「OK」ボタン押下
		alert.accept();
		pageLoadTimeout(10);
		//遷移先のURLを確認
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		assertTrue(currentUrl.startsWith("http://localhost:8080/lms/exam/result"));
		// ページのキャプチャを取得する
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 「戻る」ボタンを押下し試験開始画面に遷移後当該試験の結果が反映される")
	void test08() throws InterruptedException {
		//最下部までスクロール
		JavascriptExecutor js = (JavascriptExecutor) WebDriverUtils.webDriver;
		js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
		Thread.sleep(3000);
		//戻るボタンを押下する
		WebElement answerBtn = WebDriverUtils.webDriver.findElement(By.xpath("//input[@value='戻る']"));
		visibilityTimeout(By.xpath("//input[@value='戻る']"), 10);
		answerBtn.click();
		// 結果が反映されているか確認
		WebElement rowElement = WebDriverUtils.webDriver.findElement(By.xpath("//td[text()='5回目']/.."));
		List<WebElement> cellsInRow = rowElement.findElements(By.tagName("td"));
		WebElement secondCell = cellsInRow.get(1);
		String resultScoreString = secondCell.getText();
		assertEquals("0.0点", resultScoreString);
		//遷移先のURLを確認
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		assertTrue(currentUrl.startsWith("http://localhost:8080/lms/exam/start"));
		// ページのキャプチャを取得する
		getEvidence(new Object() {
		});
	}

}