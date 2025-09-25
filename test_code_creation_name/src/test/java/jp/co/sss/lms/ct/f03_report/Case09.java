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
 * ケース09
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース09 受講生 レポート登録 入力チェック")
public class Case09 {

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
	@DisplayName("テスト03 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test03() {
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
	@Order(4)
	@DisplayName("テスト04 該当レポートの「修正する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		// ”週報【デモ】”の行にある詳細をクリック
		final WebElement detailElement = WebDriverUtils.webDriver
				.findElement(By.xpath("//td[text()='週報【デモ】']/following-sibling::td//input[@value='修正する']"));
		detailElement.click();
		pageLoadTimeout(10);
		//遷移先のURLを確認
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		assertTrue(currentUrl.startsWith("http://localhost:8080/lms/report/regist"));
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しエラー表示：学習項目が未入力")
	void test05() {
		final WebElement fieldName = WebDriverUtils.webDriver.findElement(By.id("intFieldName_0"));
		final WebElement fieldValue = WebDriverUtils.webDriver.findElement(By.id("intFieldValue_0"));
		final WebElement achievementLevel = WebDriverUtils.webDriver.findElement(By.id("content_0"));
		final WebElement impressions = WebDriverUtils.webDriver.findElement(By.id("content_1"));
		final WebElement reviewOfTheWeek = WebDriverUtils.webDriver.findElement(By.id("content_2"));
		//学習項目は未入力で次の項目へ
		fieldName.clear();
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
		// 提出するボタンをクリック
		WebElement submitBtn = WebDriverUtils.webDriver.findElement(By.className("btn-primary"));
		submitBtn.click();
		//自画面遷移のためURLが変わっていないことを確認
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		assertTrue(currentUrl.startsWith("http://localhost:8080/lms/report/regist"));
		//エラーメッセージが正しいか確認
		final WebElement errorMsgElement = WebDriverUtils.webDriver
				.findElement(By.cssSelector(".help-inline.error"));
		String errorMsg = errorMsgElement.getText();
		assertEquals("* 理解度を入力した場合は、学習項目は必須です。", errorMsg);
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：理解度が未入力")
	void test06() {
		final WebElement fieldName = WebDriverUtils.webDriver.findElement(By.id("intFieldName_0"));
		final WebElement fieldValue = WebDriverUtils.webDriver.findElement(By.id("intFieldValue_0"));
		final WebElement achievementLevel = WebDriverUtils.webDriver.findElement(By.id("content_0"));
		final WebElement impressions = WebDriverUtils.webDriver.findElement(By.id("content_1"));
		final WebElement reviewOfTheWeek = WebDriverUtils.webDriver.findElement(By.id("content_2"));
		//学習項目の入力
		fieldName.clear();
		fieldName.sendKeys(Keys.TAB);
		//理解度は未入力で次の項目へ
		fieldValue.sendKeys("");
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
		// 提出するボタンをクリック
		WebElement submitBtn = WebDriverUtils.webDriver.findElement(By.className("btn-primary"));
		submitBtn.click();
		//自画面遷移のためURLが変わっていないことを確認
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		assertTrue(currentUrl.startsWith("http://localhost:8080/lms/report/regist"));
		//エラーメッセージが正しいか確認
		final WebElement errorMsgElement = WebDriverUtils.webDriver
				.findElement(By.cssSelector(".help-inline.error"));
		String errorMsg = errorMsgElement.getText();
		assertEquals("* 学習項目を入力した場合は、理解度は必須です。", errorMsg);
		getEvidence(new Object() {
		});

	}

	@Test
	@Order(7)
	@DisplayName("テスト07 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が数値以外")
	void test07() {
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
		//目標の達成度が数値以外で次の項目へ
		achievementLevel.clear();
		achievementLevel.sendKeys("ABC");
		achievementLevel.sendKeys(Keys.TAB);
		//所感の入力
		impressions.clear();
		impressions.sendKeys("テスト");
		impressions.sendKeys(Keys.TAB);
		//1週間の振り返りの入力
		reviewOfTheWeek.clear();
		reviewOfTheWeek.sendKeys("テスト");
		// 提出するボタンをクリック
		WebElement submitBtn = WebDriverUtils.webDriver.findElement(By.className("btn-primary"));
		submitBtn.click();
		//自画面遷移のためURLが変わっていないことを確認
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		assertTrue(currentUrl.startsWith("http://localhost:8080/lms/report/regist"));
		//エラーメッセージが正しいか確認
		final WebElement errorMsgElement = WebDriverUtils.webDriver
				.findElement(By.cssSelector(".help-inline.error"));
		String errorMsg = errorMsgElement.getText();
		assertEquals("* 目標の達成度は半角数字で入力してください。", errorMsg);
		getEvidence(new Object() {
		});

	}

	@Test
	@Order(8)
	@DisplayName("テスト08 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が範囲外")
	void test08() {
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
		//目標の達成度が範囲外
		achievementLevel.clear();
		achievementLevel.sendKeys("11");
		achievementLevel.sendKeys(Keys.TAB);
		//所感の入力
		impressions.clear();
		impressions.sendKeys("テスト");
		impressions.sendKeys(Keys.TAB);
		//1週間の振り返りの入力
		reviewOfTheWeek.clear();
		reviewOfTheWeek.sendKeys("テスト");
		// 提出するボタンをクリック
		WebElement submitBtn = WebDriverUtils.webDriver.findElement(By.className("btn-primary"));
		submitBtn.click();
		//自画面遷移のためURLが変わっていないことを確認
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		assertTrue(currentUrl.startsWith("http://localhost:8080/lms/report/regist"));
		//エラーメッセージが正しいか確認
		final WebElement errorMsgElement = WebDriverUtils.webDriver
				.findElement(By.cssSelector(".help-inline.error"));
		String errorMsg = errorMsgElement.getText();
		assertEquals("* 目標の達成度は半角数字で入力してください。", errorMsg);
		getEvidence(new Object() {
		});

	}

	@Test
	@Order(9)
	@DisplayName("テスト09 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度・所感が未入力")
	void test09() {
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
		//目標の達成度が未入力で次の項目へ
		achievementLevel.clear();
		achievementLevel.sendKeys(Keys.TAB);
		//所感が未入力で次の項目へ
		impressions.clear();
		impressions.sendKeys(Keys.TAB);
		//1週間の振り返りの入力
		reviewOfTheWeek.clear();
		reviewOfTheWeek.sendKeys("テスト");
		// 提出するボタンをクリック
		WebElement submitBtn = WebDriverUtils.webDriver.findElement(By.className("btn-primary"));
		submitBtn.click();
		//自画面遷移のためURLが変わっていないことを確認
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		assertTrue(currentUrl.startsWith("http://localhost:8080/lms/report/regist"));
		//エラーメッセージが正しいか確認
		WebElement errorMsg = WebDriverUtils.webDriver.findElement(By.cssSelector(".help-inline.error"));
		List<WebElement> errorMsgList = errorMsg.findElements(By.tagName("sapn"));
		WebElement firstErrorMsg = errorMsgList.get(0);
		WebElement secondErrorMsg = errorMsgList.get(1);
		assertEquals("* 目標の達成度は必須です。", firstErrorMsg.getText());

		assertEquals("* 所感は必須です。", secondErrorMsg.getText());
		getEvidence(new Object() {
		});

	}

	@Test
	@Order(10)
	@DisplayName("テスト10 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：所感・一週間の振り返りが2000文字超")
	void test10() {
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
		//所感の入力が2000文字以上
		impressions.clear();
		impressions.sendKeys("テスト");
		impressions.sendKeys(Keys.TAB);
		//1週間の振り返りの入力が2000文字以上
		reviewOfTheWeek.clear();
		reviewOfTheWeek.sendKeys("テスト");
		// 提出するボタンをクリック
		WebElement submitBtn = WebDriverUtils.webDriver.findElement(By.className("btn-primary"));
		submitBtn.click();
		//自画面遷移のためURLが変わっていないことを確認
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		assertTrue(currentUrl.startsWith("http://localhost:8080/lms/report/regist"));
		//エラーメッセージが正しいか確認
		WebElement errorMsg = WebDriverUtils.webDriver.findElement(By.cssSelector(".help-inline.error"));
		List<WebElement> errorMsgList = errorMsg.findElements(By.tagName("sapn"));
		WebElement firstErrorMsg = errorMsgList.get(0);
		WebElement secondErrorMsg = errorMsgList.get(1);
		assertEquals("* 所感の長さが最大値200を超えています。", firstErrorMsg.getText());

		assertEquals("* 1週間の振り返りの長さが最大値200を超えています。", secondErrorMsg.getText());

		getEvidence(new Object() {
		});

	}

}
