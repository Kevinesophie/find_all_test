import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys


displayName= "Bertrand BENE"
userName= "Bertrand-BENE"
src= "https://github.com/Bertrand-BENE"
logo_src= "https://avatars.githubusercontent.com/u/161041830?v=4"

WebUI.openBrowser('https://sparkling-starship-a8100b.netlify.app')
WebUI.click(findTestObject('Object Repository/Page_Home/a_Profile'))
WebUI.setText(findTestObject('Object Repository/Page_Profile/input_user'), userName)
WebUI.click(findTestObject('Object Repository/Page_Profile/button_Enter'))

WebUI.delay(10)
 
WebUI.verifyMatch(WebUI.getAttribute(findTestObject('Object Repository/Page_Profile/img_logo'), 'src'), logo_src, false)
WebUI.verifyMatch(WebUI.getText(findTestObject('Object Repository/Page_Profile/h3_displayName')), displayName, false)
WebUI.verifyMatch(WebUI.getText(findTestObject('Object Repository/Page_Profile/p_login')), "Login name: "+userName, false)

WebUI. verifyElementAttributeValue(findTestObject('Object Repository/Page_Profile/a_View on Github'), 'href', 'https://github.com/Bertrand-BENE', 10)

WebUI.closeBrowser()

