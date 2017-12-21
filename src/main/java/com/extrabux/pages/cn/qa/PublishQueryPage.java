package com.extrabux.pages.cn.qa;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.extrabux.pages.BasePage;

public class PublishQueryPage extends BasePage{

	static final String URI = "/publish";
	
	@FindBy(id = "question_contents")
	WebElement queryTitle;
	
	@FindBy(name = "question_detail")
	WebElement queryContent;
	
	@FindBy(id = "aw-topic-tags-select")
	WebElement queryCategory;
	
	@FindBy(linkText = "默认分类")
	WebElement defaultCategory;
	
	@FindBy(id = "aw_edit_topic_title")
	WebElement queryTag;
	
	@FindBy(id = "publish_submit")
	WebElement submitButton;
	
	public PublishQueryPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public String getUrl(String serverName) {
		return "http://" + serverName + URI;
	}
	
	public QuestionDetailPage addQuestion(String title,String content,String tag){
		queryTitle.sendKeys(title);
		queryContent.sendKeys(content);
	
		//choose default category
		queryCategory.click();
		defaultCategory.click();
		
		queryTag.sendKeys(tag);
		submitButton.click();
		
		return new QuestionDetailPage(driver);
	}
}
