package jp.co.sss.lms.ct.f03_report;

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
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import jp.co.sss.lms.ct.util.WebDriverUtils;

/**
 * 結合テスト レポート機能
 * ケース08
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース08 受講生 レポート修正(週報) 正常系")
public class Case08 {

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
	@DisplayName("テスト03 提出済の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {
		// ”アルゴリズム、フローチャート”の行にあるすべてのセルを取得
		WebElement rowElement = WebDriverUtils.webDriver.findElement(By.xpath("//td[text()='アルゴリズム、フローチャート']/.."));
		List<WebElement> cellsInRow = rowElement.findElements(By.tagName("td"));
		WebElement thirdCell = cellsInRow.get(2);

		String submitReport = thirdCell.getText();
		if (!(submitReport.equals("提出済み"))) {
			System.out.println("この研修の報告は提出済みではありません。");
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
	@DisplayName("テスト04 「確認する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		// 提出するボタンをクリック
		WebElement submitButton = WebDriverUtils.webDriver
				.findElement(By.xpath("//*[@value='提出済み週報【デモ】を確認する']"));
		submitButton.click();
		//遷移先のURLを確認
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		assertTrue(currentUrl.startsWith("http://localhost:8080/lms/report/regist"));
		// ページのキャプチャを取得する
		getEvidence(new Object() {
		});

	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しセクション詳細画面に遷移")
	void test05() {
		final WebElement fieldName = WebDriverUtils.webDriver.findElement(By.id("intFieldName_0"));
		final WebElement fieldValue = WebDriverUtils.webDriver.findElement(By.id("intFieldValue_0"));
		final WebElement achievementLevel = WebDriverUtils.webDriver.findElement(By.id("content_0"));
		final WebElement impressions = WebDriverUtils.webDriver.findElement(By.id("content_1"));
		final WebElement reviewOfTheWeek = WebDriverUtils.webDriver.findElement(By.id("content_2"));
		//学習項目の入力
		fieldName.clear();
		fieldName.sendKeys("テスト");
		fieldName.sendKeys(Keys.TAB);
		//理解度の入力
		fieldValue.sendKeys("1");
		fieldValue.sendKeys(Keys.TAB);
		//目標の達成度の入力
		achievementLevel.clear();
		achievementLevel.sendKeys("1");
		achievementLevel.sendKeys(Keys.TAB);
		//所感の入力
		impressions.clear();
		impressions.sendKeys("テスト");
		impressions.sendKeys(Keys.TAB);
		//1週間の振り返りの入力
		reviewOfTheWeek.clear();
		reviewOfTheWeek.sendKeys("テスト");
		reviewOfTheWeek.sendKeys(Keys.ENTER);
		// 提出するボタンをクリック
		WebElement submitBtn = WebDriverUtils.webDriver.findElement(By.className("btn-primary"));
		submitBtn.click();
		//遷移先のURLを確認
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		assertTrue(currentUrl.startsWith("http://localhost:8080/lms/section/detail"));
		// ページのキャプチャを取得する
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test06() {
		//ようこそ○○さんリンクをクリック
		final WebElement dropdownToggle = WebDriverUtils.webDriver.findElement(By.linkText("ようこそ受講生ＡＡ１さん"));
		dropdownToggle.click();
		pageLoadTimeout(10);
		//遷移先のURLを確認
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		assertTrue(currentUrl.startsWith("http://localhost:8080/lms/user/detail"));
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 該当レポートの「詳細」ボタンを押下しレポート詳細画面で修正内容が反映される")
	void test07() {
		// ”週報【デモ】”の行にある詳細をクリック
		final WebElement detailElement = WebDriverUtils.webDriver
				.findElement(By.xpath("//td[text()='週報【デモ】']/following-sibling::td//input[@value='詳細']"));
		detailElement.click();
		pageLoadTimeout(10);
		//遷移先のURLを確認
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		assertTrue(currentUrl.startsWith("http://localhost:8080/lms/report/detail"));

		//修正内容が反映されているか確認
		//学習項目の値
		WebElement firstElement = WebDriverUtils.webDriver.findElement(By.xpath("//tr[2]/td[1]/p[1]"));
		String fieldName = firstElement.getText();
		assertEquals("テスト", fieldName);
		//理解度の値
		final WebElement secondElement = WebDriverUtils.webDriver.findElement(By.xpath("//tr[2]/td[2]/p[1]"));
		String fieldValue = secondElement.getText();
		assertEquals("1", fieldValue);
		//目標の達成度の値
		List<WebElement> thirdElementList = WebDriverUtils.webDriver.findElements(By.xpath("//tr[1]/td[1]"));//同様のものあり
		WebElement thirdElement = thirdElementList.get(1);
		String achievementLevel = thirdElement.getText();
		assertEquals("1", achievementLevel);
		//所感の値
		List<WebElement> fourthElementList = WebDriverUtils.webDriver.findElements(By.xpath("//tr[2]/td[1]"));//同様のものあり
		WebElement fourthElement = fourthElementList.get(1);
		String impressions = fourthElement.getText();
		assertEquals("テスト", impressions);
		//1週間の振り返りの値
		WebElement fifthElement = WebDriverUtils.webDriver.findElement(By.xpath("//tr[3]/td[1]"));
		String reviewOfTheWeek = fifthElement.getText();
		assertEquals("テスト", reviewOfTheWeek);

		// ページのキャプチャを取得する
		getEvidence(new Object() {
		});

	}

}
