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
 * ケース07
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース07 受講生 レポート新規登録(日報) 正常系")
public class Case07 {

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
	@DisplayName("テスト03 未提出の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {
		// ”Java概要”の行にあるすべてのセルを取得
		WebElement rowElement = WebDriverUtils.webDriver.findElement(By.xpath("//td[text()='Java概要']/.."));
		List<WebElement> cellsInRow = rowElement.findElements(By.tagName("td"));
		WebElement thirdCell = cellsInRow.get(2);

		String submitReport = thirdCell.getText();
		if (!(submitReport.equals("未提出"))) {
			System.out.println("この研修の報告は未提出ではありません。");
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
	@DisplayName("テスト04 「提出する」ボタンを押下しレポート登録画面に遷移")
	void test04() {

		//提出するボタンが更新されていないか確認
		WebElement submitButton = WebDriverUtils.webDriver
				.findElement(By.xpath("//*[@class='btn btn-default']"));
		String submitBtnValue = submitButton.getAttribute("value");
		assertEquals("日報【デモ】を提出する", submitBtnValue);
		// 提出するボタンをクリック
		WebElement submitBtn = WebDriverUtils.webDriver.findElement(By.tagName("form"));
		submitBtn.click();
		//遷移先のURLを確認
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		assertTrue(currentUrl.startsWith("http://localhost:8080/lms/report/regist"));
		// ページのキャプチャを取得する
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を入力して「提出する」ボタンを押下し確認ボタン名が更新される")
	void test05() {
		final WebElement submitReport = WebDriverUtils.webDriver.findElement(By.className("form-control"));
		//報告内容を入力し、Enterキーを押下
		submitReport.clear();
		submitReport.sendKeys("0123456789abcdefghijklmnopqrstuvwxyz\r\n"
				+ "ABCDEFGHIJKLMNOPQRSTUVWXYZ１２３４５６７８９ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖ"
				+ "ｗｘｙｚＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺあいうえおかきくけこさしすせ"
				+ "そたちつてとなにぬねのはひふへほまみむめもやゆよらりるれろわをんアイウエオカキクケコサシ"
				+ "スセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワヲン一二三四五六七八九十"
				+ "①②③④⑤⑥⑦⑧⑨⑩!-/:-@¥[-`{-~]*$");
		final WebElement submitReportBtn = WebDriverUtils.webDriver.findElement(By.className("btn-primary"));
		submitReportBtn.click();
		pageLoadTimeout(10);
		//提出するボタンが更新されていないか確認
		WebElement submitButton = WebDriverUtils.webDriver
				.findElement(By.xpath("//*[@class='btn btn-default']"));
		String submitBtnValue = submitButton.getAttribute("value");
		assertEquals("提出済み日報【デモ】を確認する", submitBtnValue);
		// ページのキャプチャを取得する
		getEvidence(new Object() {
		});
	}

}
