package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
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
 * 結合テスト よくある質問機能
 * ケース06
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース06 カテゴリ検索 正常系")
public class Case06 {

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
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() {
		//機能のリンクをクリック
		final WebElement dropdownToggle = WebDriverUtils.webDriver.findElement(By.linkText("機能"));
		dropdownToggle.click();
		pageLoadTimeout(10);
		//ヘルプのリンクをクリック
		final WebElement helpLink = WebDriverUtils.webDriver.findElement(By.linkText("ヘルプ"));
		helpLink.click();
		//遷移先のURLを確認
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		assertTrue(currentUrl.startsWith("http://localhost:8080/lms/help"));
		//タイトルが正しいか確認
		String pageTitleString = WebDriverUtils.webDriver.getTitle();
		assertEquals("ヘルプ | LMS", pageTitleString);
		// ページのキャプチャを取得する
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() {
		//よくある質問をクリックして別タブへ遷移
		final WebElement faqElement = WebDriverUtils.webDriver.findElement(By.linkText("よくある質問"));
		faqElement.click();
		pageLoadTimeout(10);
		//遷移したタブのURLを取得するため、タブでListを作成
		ArrayList<String> tabs = new ArrayList<String>(WebDriverUtils.webDriver.getWindowHandles());
		//2つ目のタブを選択
		WebDriverUtils.webDriver.switchTo().window(tabs.get(1));
		//遷移先のURLを確認
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		assertTrue(currentUrl.startsWith("http://localhost:8080/lms/faq"));
		//表示されているよくある質問の全件の件数を取得
		final List<WebElement> fqaElements = WebDriverUtils.webDriver.findElements(By.className("sorting_1"));
		int countFqa = fqaElements.size();
		//DBに登録されている件数で比較
		final int DB_COUNT_FQA = 5;
		assertEquals(DB_COUNT_FQA, countFqa);
		//件数が正しく表示されているか確認
		final WebElement countFqaElements = WebDriverUtils.webDriver.findElement(By.className("dataTables_info"));
		String countFqaText = countFqaElements.getText();
		assertEquals("5 件中 1 件から 5 件までを表示", countFqaText);
		// ページのキャプチャを取得する
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 カテゴリ検索で該当カテゴリの検索結果だけ表示")
	void test05() {
		//カテゴリ検索で該当のカテゴリをクリック
		final WebElement categoryElement = WebDriverUtils.webDriver.findElement(By.linkText("【研修関係】"));
		categoryElement.click();
		pageLoadTimeout(10);
		//表示されているよくある質問の件数を取得
		final List<WebElement> fqaElements = WebDriverUtils.webDriver.findElements(By.className("sorting_1"));
		WebElement firstFqa = fqaElements.get(0);
		WebElement secondFqa = fqaElements.get(1);
		String firstFqlTitle = firstFqa.getText();
		String secondFqlTitle = secondFqa.getText();
		assertEquals("Q.キャンセル料・途中退校について", firstFqlTitle);
		assertEquals("Q.研修の申し込みはどのようにすれば良いですか？", secondFqlTitle);
		//件数が正しく表示されているか確認
		final WebElement countFqaElements = WebDriverUtils.webDriver.findElement(By.className("dataTables_info"));
		String countFqaText = countFqaElements.getText();
		assertEquals("2 件中 1 件から 2 件までを表示", countFqaText);
		// ページのキャプチャを取得する
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 検索結果の質問をクリックしその回答を表示")
	void test06() {
		final WebElement faqElement = WebDriverUtils.webDriver.findElement(By.className("sorting_1"));
		faqElement.click();
		pageLoadTimeout(10);
		//表示されているよくある質問の件数を取得
		final WebElement categoryFaqTitle = WebDriverUtils.webDriver.findElement(By.className("fs18"));
		String firstFqlTitle = categoryFaqTitle.getText();
		assertEquals("A. 受講者の退職や解雇等、やむを得ない事情による途中終了に関してなど、事情をお伺いした上で、協議という形を取らせて頂きます。 弊社営業担当までご相談下さい。", firstFqlTitle);
		// ページのキャプチャを取得する
		getEvidence(new Object() {
		});
	}

}
