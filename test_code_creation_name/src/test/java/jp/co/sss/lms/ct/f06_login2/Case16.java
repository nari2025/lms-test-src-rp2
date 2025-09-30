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
 * ケース16
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース16 受講生 初回ログイン 変更パスワード未入力")
public class Case16 {

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
	void test02() throws InterruptedException {
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
		Thread.sleep(3000);
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
	@DisplayName("テスト04 パスワードを未入力で「変更」ボタン押下")
	void test04() {
		final WebElement currentPassword = WebDriverUtils.webDriver.findElement(By.id("currentPassword"));
		final WebElement password = WebDriverUtils.webDriver.findElement(By.id("password"));
		final WebElement passwordConfirm = WebDriverUtils.webDriver.findElement(By.id("passwordConfirm"));
		//全て未入力で変更ボタンを押下
		currentPassword.clear();
		currentPassword.sendKeys(Keys.TAB);
		password.clear();
		password.sendKeys(Keys.TAB);
		passwordConfirm.clear();
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

		//URLの確認
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		assertTrue(currentUrl.startsWith("http://localhost:8080/lms/password/changePassword/change"));
		//属性値が変化しているか確認
		String currentPasswordError = WebDriverUtils.webDriver.findElement(By.id("currentPassword"))
				.getAttribute("class");
		assertEquals("form-control errorInput", currentPasswordError);
		String passwordError = WebDriverUtils.webDriver.findElement(By.id("password")).getAttribute("class");
		assertEquals("form-control errorInput", passwordError);
		String passwordConfirmError = WebDriverUtils.webDriver.findElement(By.id("passwordConfirm"))
				.getAttribute("class");
		assertEquals("form-control errorInput", passwordConfirmError);
		//エラーメッセージの確認
		final WebElement currentPasswordErrorMsgElement = WebDriverUtils.webDriver
				.findElement(By.xpath("//input[@id='currentPassword']/following-sibling::ul[1]//span"));
		String currentPasswordErrorMsg = currentPasswordErrorMsgElement.getText();
		assertEquals("現在のパスワードは必須です。", currentPasswordErrorMsg);

		final WebElement passwordErrorElement = WebDriverUtils.webDriver
				.findElement(By.xpath("//input[@id='password']/following-sibling::ul[1]//span"));
		String passwordErrorMsg = passwordErrorElement.getText();
		assertTrue(passwordErrorMsg
				.contains("「パスワード」には半角英数字のみ使用可能です。また、半角英大文字、半角英小文字、数字を含めた8～20文字を入力してください。"));
		assertTrue(passwordErrorMsg
				.contains("パスワードは必須です。"));
		final WebElement passwordConfirmErrorMsgElement = WebDriverUtils.webDriver
				.findElement(By.xpath("//input[@id='passwordConfirm']/following-sibling::ul[1]//span"));
		String passwordConfirmErrorMsg = passwordConfirmErrorMsgElement.getText();
		assertEquals("確認パスワードは必須です。", passwordConfirmErrorMsg);

		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 20文字以上の変更パスワードを入力し「変更」ボタン押下")
	void test05() {
		final WebElement currentPassword = WebDriverUtils.webDriver.findElement(By.id("currentPassword"));
		final WebElement password = WebDriverUtils.webDriver.findElement(By.id("password"));
		final WebElement passwordConfirm = WebDriverUtils.webDriver.findElement(By.id("passwordConfirm"));
		//20文字以上の変更パスワードで変更ボタンを押下
		currentPassword.clear();
		currentPassword.sendKeys(Keys.TAB);
		password.clear();
		password.sendKeys("0123abcdABCD!#$%0123");
		password.sendKeys(Keys.TAB);
		passwordConfirm.clear();
		passwordConfirm.sendKeys("0123abcdABCD!#$%0123");
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

		//URLの確認
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		assertTrue(currentUrl.startsWith("http://localhost:8080/lms/password/changePassword/change"));
		//属性値が変化しているか確認
		String classText = WebDriverUtils.webDriver.findElement(By.id("password")).getAttribute("class");
		assertEquals("form-control errorInput", classText);
		//エラーメッセージの確認
		final WebElement errorMsgElement = WebDriverUtils.webDriver
				.findElement(By.xpath("//input[@id='password']/following-sibling::ul[1]//span"));
		String errorMsg = errorMsgElement.getText();
		assertEquals("「パスワード」には半角英数字のみ使用可能です。また、半角英大文字、半角英小文字、数字を含めた8～20文字を入力してください。", errorMsg);
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 ポリシーに合わない変更パスワードを入力し「変更」ボタン押下")
	void test06() {
		final WebElement currentPassword = WebDriverUtils.webDriver.findElement(By.id("currentPassword"));
		final WebElement password = WebDriverUtils.webDriver.findElement(By.id("password"));
		final WebElement passwordConfirm = WebDriverUtils.webDriver.findElement(By.id("passwordConfirm"));
		//ポリシーに合わない変更パスワードを入力
		currentPassword.clear();
		currentPassword.sendKeys(Keys.TAB);
		password.clear();
		password.sendKeys("testtest");
		password.sendKeys(Keys.TAB);
		passwordConfirm.clear();
		passwordConfirm.sendKeys("testtest");
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

		//URLの確認
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		assertTrue(currentUrl.startsWith("http://localhost:8080/lms/password/changePassword/change"));
		//属性値が変化しているか確認
		String classText = WebDriverUtils.webDriver.findElement(By.id("password")).getAttribute("class");
		assertEquals("form-control errorInput", classText);
		//エラーメッセージの確認
		final WebElement errorMsgElement = WebDriverUtils.webDriver
				.findElement(By.xpath("//input[@id='password']/following-sibling::ul[1]//span"));
		String errorMsg = errorMsgElement.getText();
		assertEquals("「パスワード」には半角英数字のみ使用可能です。また、半角英大文字、半角英小文字、数字を含めた8～20文字を入力してください。", errorMsg);
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 一致しない確認パスワードを入力し「変更」ボタン押下")
	void test07() {
		final WebElement currentPassword = WebDriverUtils.webDriver.findElement(By.id("currentPassword"));
		final WebElement password = WebDriverUtils.webDriver.findElement(By.id("password"));
		final WebElement passwordConfirm = WebDriverUtils.webDriver.findElement(By.id("passwordConfirm"));
		//一致しない確認パスワードを入力
		currentPassword.clear();
		currentPassword.sendKeys("StudentAA03");
		currentPassword.sendKeys(Keys.TAB);
		password.clear();
		password.sendKeys("testTest1234");
		password.sendKeys(Keys.TAB);
		passwordConfirm.clear();
		passwordConfirm.sendKeys("testTest5678");
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

		//URLの確認
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		assertTrue(currentUrl.startsWith("http://localhost:8080/lms/password/changePassword/change"));
		//属性値が変化しているか確認
		String classText = WebDriverUtils.webDriver.findElement(By.id("password")).getAttribute("class");
		assertEquals("form-control errorInput", classText);
		//エラーメッセージの確認
		final WebElement errorMsgElement = WebDriverUtils.webDriver
				.findElement(By.xpath("//input[@id='password']/following-sibling::ul[1]//span"));
		String errorMsg = errorMsgElement.getText();
		assertEquals("パスワードと確認パスワードが一致しません。", errorMsg);
		getEvidence(new Object() {
		});
	}

}
