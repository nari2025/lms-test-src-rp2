package jp.co.sss.lms.ct.f06_login2;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.Assert.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import jp.co.sss.lms.ct.util.WebDriverUtils;

/**
 * 結合テスト ログイン機能②
 * ケース17
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース17 受講生 初回ログイン 正常系")
public class Case17 {

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
	@DisplayName("テスト02 DBに初期登録された未ログインの受講生ユーザーでログイン")
	void test02() {
		final WebElement loginId = WebDriverUtils.webDriver.findElement(By.id("loginId"));
		final WebElement password = WebDriverUtils.webDriver.findElement(By.id("password"));
		//初回ログイン済みのログインIDを入力し、TABキーを押下
		loginId.clear();
		loginId.sendKeys("StudentAA03");
		loginId.sendKeys(Keys.TAB);
		//初回ログイン済みのパスワードを入力し、Enterキーを押下
		password.clear();
		password.sendKeys("StudentAA03");
		password.sendKeys(Keys.ENTER);
		// 遷移先のURLを取得し、正しいか確認
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		assertTrue(currentUrl.startsWith("http://localhost:8080/lms/user/agreeSecurity"));
		pageLoadTimeout(10);
		//ユーザー名が表示されているか確認
		WebElement smallElement = WebDriverUtils.webDriver.findElement(By.tagName("small"));
		String userName = smallElement.getText();
		assertEquals("ようこそ受講生ＡＡ３さん", userName);

		// ページのキャプチャを取得する
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 「同意します」チェックボックスにチェックを入れ「次へ」ボタン押下")
	void test03() {
		// 同意するにチェックをいれ、次へを押す
		final WebElement checkBox = WebDriverUtils.webDriver.findElement(By.xpath("//input[@value='1']"));
		checkBox.click();
		final WebElement nextBtn = WebDriverUtils.webDriver.findElement(By.xpath("//button[text()='次へ']"));
		nextBtn.click();
		//URLの確認
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		assertTrue(currentUrl.startsWith("http://localhost:8080/lms/password/changePassword"));
		// ページのキャプチャを取得する
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 変更パスワードを入力し「変更」ボタン押下")
	void test04() {
		final WebElement currentPassword = WebDriverUtils.webDriver.findElement(By.id("currentPassword"));
		final WebElement password = WebDriverUtils.webDriver.findElement(By.id("password"));
		final WebElement passwordConfirm = WebDriverUtils.webDriver.findElement(By.id("passwordConfirm"));
		//パスワードをいれて変更ボタンを押下
		currentPassword.clear();
		currentPassword.sendKeys("StudentAA03");
		currentPassword.sendKeys(Keys.TAB);
		password.clear();
		password.sendKeys("StudentAA03test");
		password.sendKeys(Keys.ENTER);
		passwordConfirm.clear();
		passwordConfirm.sendKeys("StudentAA03test");
		passwordConfirm.sendKeys(Keys.ENTER);
		WebElement changeButton = WebDriverUtils.webDriver.findElement(By.xpath("//button[text()='変更']"));
		JavascriptExecutor js = (JavascriptExecutor) WebDriverUtils.webDriver;
		js.executeScript("arguments[0].click();", changeButton);
		// モーダル要素が出るのをまつ
		visibilityTimeout(By.xpath("//div[@class='modal-dialog modal-dialog-center']"), 5);
		// モーダル要素の変更ボタンを押す
		WebElement modalElement = WebDriverUtils.webDriver
				.findElement(By.xpath("//div[@class='modal-dialog modal-dialog-center']"));
		WebElement upDateButton = modalElement.findElement(By.xpath(".//button[text()='変更']"));
		upDateButton.click();

		//ログアウトボタンを押下
		final WebElement attendance = WebDriverUtils.webDriver
				.findElement(By.xpath("//button[@class='btn btn-default navbar-btn']"));
		attendance.click();
		pageLoadTimeout(10);
		//初回ログイン済みのログインIDを入力し、TABキーを押下
		final WebElement loginId = WebDriverUtils.webDriver.findElement(By.id("loginId"));
		final WebElement newPassword = WebDriverUtils.webDriver.findElement(By.id("password"));
		//初回ログイン済みのログインIDを入力し、TABキーを押下
		loginId.clear();
		loginId.sendKeys("StudentAA03");
		loginId.sendKeys(Keys.TAB);
		//初回ログイン済みのパスワードを入力し、Enterキーを押下
		newPassword.clear();
		newPassword.sendKeys("StudentAA03test");
		newPassword.sendKeys(Keys.ENTER);
		//URLの確認
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		assertTrue(currentUrl.startsWith("http://localhost:8080/lms/course/detail"));
		pageLoadTimeout(10);
		getEvidence(new Object() {
		});
	}

}
