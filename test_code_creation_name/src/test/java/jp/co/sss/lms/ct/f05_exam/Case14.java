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
 * ケース14
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース14 受講生 試験の実施 結果50点")
public class Case14 {

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
	@DisplayName("テスト06 正答と誤答が半々で「確認画面へ進む」ボタンを押下し試験回答確認画面に遷移")
	void test06() throws InterruptedException {
		//第1問入力
		WebElement firstQuestion = WebDriverUtils.webDriver.findElement(By.xpath("//label[@for='answer-0-2']"));
		firstQuestion.click();
		//第2問入力
		WebElement secondQuestion = WebDriverUtils.webDriver.findElement(By.xpath("//label[@for='answer-1-2']"));
		secondQuestion.click();
		//スクロール
		WebElement labelElement1 = WebDriverUtils.webDriver.findElement(By.xpath("//label[@for='answer-2-0']"));
		JavascriptExecutor js1 = (JavascriptExecutor) WebDriverUtils.webDriver;
		js1.executeScript("arguments[0].scrollIntoView({block: \"center\"});", labelElement1);
		//第3問入力
		WebElement thirdQuestion = WebDriverUtils.webDriver.findElement(By.xpath("//label[@for='answer-2-0']"));
		thirdQuestion.click();
		//第4問入力
		WebElement fourthQuestion = WebDriverUtils.webDriver.findElement(By.xpath("//label[@for='answer-3-0']"));
		fourthQuestion.click();
		//スクロール
		WebElement labelElement2 = WebDriverUtils.webDriver.findElement(By.xpath("//label[@for='answer-4-1']"));
		JavascriptExecutor js2 = (JavascriptExecutor) WebDriverUtils.webDriver;
		js2.executeScript("arguments[0].scrollIntoView({block: \"center\"});", labelElement2);
		//第5問入力
		WebElement fifthQuestion = WebDriverUtils.webDriver.findElement(By.xpath("//label[@for='answer-4-1']"));
		fifthQuestion.click();
		//第6問入力
		WebElement sixthQuestion = WebDriverUtils.webDriver.findElement(By.xpath("//label[@for='answer-5-1']"));
		sixthQuestion.click();
		//スクロール
		WebElement labelElement3 = WebDriverUtils.webDriver.findElement(By.xpath("//label[@for='answer-6-0']"));
		JavascriptExecutor js3 = (JavascriptExecutor) WebDriverUtils.webDriver;
		js3.executeScript("arguments[0].scrollIntoView({block: \"center\"});", labelElement3);
		//第7問入力
		WebElement seventhQuestion = WebDriverUtils.webDriver.findElement(By.xpath("//label[@for='answer-6-0']"));
		seventhQuestion.click();
		//第8問入力
		WebElement eighthQuestion = WebDriverUtils.webDriver.findElement(By.xpath("//label[@for='answer-7-0']"));
		eighthQuestion.click();
		//スクロール
		WebElement labelElement4 = WebDriverUtils.webDriver.findElement(By.xpath("//label[@for='answer-8-0']"));
		JavascriptExecutor js4 = (JavascriptExecutor) WebDriverUtils.webDriver;
		js4.executeScript("arguments[0].scrollIntoView({block: \"center\"});", labelElement4);
		//第9問入力
		WebElement ninthQuestion = WebDriverUtils.webDriver.findElement(By.xpath("//label[@for='answer-8-0']"));
		ninthQuestion.click();
		//第10問入力
		WebElement tenthQuestion = WebDriverUtils.webDriver.findElement(By.xpath("//label[@for='answer-9-0']"));
		tenthQuestion.click();
		//スクロール
		WebElement labelElement5 = WebDriverUtils.webDriver.findElement(By.xpath("//label[@for='answer-10-3']"));
		JavascriptExecutor js5 = (JavascriptExecutor) WebDriverUtils.webDriver;
		js5.executeScript("arguments[0].scrollIntoView({block: \"center\"});", labelElement5);
		//第11問入力
		WebElement eleventhQuestion = WebDriverUtils.webDriver.findElement(By.xpath("//label[@for='answer-10-3']"));
		eleventhQuestion.click();
		//第12問入力
		WebElement twelfthQuestion = WebDriverUtils.webDriver.findElement(By.xpath("//label[@for='answer-11-0']"));
		twelfthQuestion.click();
		//スクロール
		WebElement labelElement6 = WebDriverUtils.webDriver.findElement(By.xpath("//label[@for='answer-11-0']"));
		JavascriptExecutor js6 = (JavascriptExecutor) WebDriverUtils.webDriver;
		js6.executeScript("arguments[0].scrollIntoView({block: \"center\"});", labelElement6);
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
		WebElement rowElement = WebDriverUtils.webDriver.findElement(By.xpath("//td[text()='6回目']/.."));
		List<WebElement> cellsInRow = rowElement.findElements(By.tagName("td"));
		WebElement secondCell = cellsInRow.get(1);
		String resultScoreString = secondCell.getText();
		assertEquals("50.0点", resultScoreString);
		//遷移先のURLを確認
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		assertTrue(currentUrl.startsWith("http://localhost:8080/lms/exam/start"));
		// ページのキャプチャを取得する
		getEvidence(new Object() {
		});

	}

}
