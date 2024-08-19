import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ObjectRepository as ObjectRepository
import com.kms.katalon.core.model.FailureHandling
import groovy.json.JsonSlurper
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

// Step 1: Create and send a request to the GitHub API
RequestObject request = new RequestObject()
request.setRestUrl('https://api.github.com/users/addyosmani')
request.setRestRequestMethod('GET')

def response = WS.sendRequest(request)

// Step 2: Parse the JSON response
def jsonResponse = new JsonSlurper().parseText(response.getResponseText())

// Step 3: Extract user information from the GitHub API response
String githubName = jsonResponse.login
String githubBio = jsonResponse.bio ?: "No bio available"
String githubTwitter = jsonResponse.twitter_username ?: "No Twitter handle"
String githubJoined = jsonResponse.created_at
int githubPublicRepos = jsonResponse.public_repos
String githubLastUpdate = jsonResponse.updated_at
int githubFollowers = jsonResponse.followers
int githubFollowing = jsonResponse.following
String githubLocation = jsonResponse.location ?: "No location specified"

// Step 4: Convert the GitHub 'created_at' date format to match the webpage format (date only)
ZonedDateTime githubJoinedDate = ZonedDateTime.parse(githubJoined)
String formattedJoinedDate = githubJoinedDate.format(DateTimeFormatter.ofPattern("M/d/yyyy"))

// Format 'updated_at' date
ZonedDateTime githubLastUpdateDate = ZonedDateTime.parse(githubLastUpdate)
String formattedLastUpdateDate = githubLastUpdateDate.format(DateTimeFormatter.ofPattern("M/d/yyyy, h:mm:ss a"))

// Step 5: Open the target webpage to compare displayed data
WebUI.openBrowser('https://sparkling-starship-a8100b.netlify.app/')

// Wait for the page to load
WebUI.delay(5)
WebUI.click(ObjectRepository.findTestObject('Object Repository/addyosmani/Page_Home/a_Profile'))
WebUI.click(ObjectRepository.findTestObject('Object Repository/addyosmani/Page_Profile/input_About_name'))
WebUI.setText(ObjectRepository.findTestObject('Object Repository/addyosmani/Page_Profile/input_About_name'), 'addyosmani')
WebUI.click(ObjectRepository.findTestObject('Object Repository/addyosmani/Page_Profile/button_Enter'))

// Step 6: Add an explicit wait to ensure the page is fully loaded
WebUI.waitForPageLoad(10)
WebUI.waitForElementVisible(ObjectRepository.findTestObject('Object Repository/addyosmani/Page_Profile/p_Joined 812009, 83925 PM'), 10)

// Extract displayed information from the target page
String displayedName = WebUI.getText(ObjectRepository.findTestObject('Object Repository/addyosmani/Page_Profile/p_Login name addyosmani'))
String displayedBio = WebUI.getText(ObjectRepository.findTestObject('Object Repository/addyosmani/Page_Profile/p_Bio Engineering Manager at Google working_36dc3d'))
String displayedTwitter = WebUI.getText(ObjectRepository.findTestObject('Object Repository/addyosmani/Page_Profile/p_Twitter  addyosmani'))

// Extract and handle date only from displayedJoined
String displayedJoinedRaw = WebUI.getText(ObjectRepository.findTestObject('Object Repository/addyosmani/Page_Profile/p_Joined 812009, 83925 PM'))
String displayedJoined = displayedJoinedRaw.replace("Joined: ", "").split(",")[0].trim()

// Extract and handle empty values safely
String[] publicReposAndLastUpdate = WebUI.getText(ObjectRepository.findTestObject('Object Repository/addyosmani/Page_Profile/p_Public Repos 321  Last Update 8132024, 112617 AM')).split("  ")
int displayedPublicRepos = 0
if (publicReposAndLastUpdate[0].replaceAll("[^0-9]", "").isNumber()) {
	displayedPublicRepos = Integer.parseInt(publicReposAndLastUpdate[0].replaceAll("[^0-9]", ""))
}
String displayedLastUpdateRaw = publicReposAndLastUpdate[1].replaceAll("Last Update ", "")
String displayedLastUpdate = displayedLastUpdateRaw ? ZonedDateTime.parse(displayedLastUpdateRaw).format(DateTimeFormatter.ofPattern("M/d/yyyy, h:mm:ss a")) : ""

String[] followersAndFollowing = WebUI.getText(ObjectRepository.findTestObject('Object Repository/addyosmani/Page_Profile/p_Followers 42832  Following 264')).split("  ")
int displayedFollowers = 0
if (followersAndFollowing[0].replaceAll("[^0-9]", "").isNumber()) {
	displayedFollowers = Integer.parseInt(followersAndFollowing[0].replaceAll("[^0-9]", ""))
}
int displayedFollowing = 0
if (followersAndFollowing[1].replaceAll("[^0-9]", "").isNumber()) {
	displayedFollowing = Integer.parseInt(followersAndFollowing[1].replaceAll("[^0-9]", ""))
}

String displayedLocation = WebUI.getText(ObjectRepository.findTestObject('Object Repository/addyosmani/Page_Profile/div_Mountain View, California'))

// Debug statements
println("GitHub Last Update: " + formattedLastUpdateDate)
println("Displayed Last Update: " + displayedLastUpdate)

// Step 7: Use WebUI.verifyMatch to compare the information
WebUI.verifyMatch("Login name: " + githubName, displayedName, false, FailureHandling.CONTINUE_ON_FAILURE)
WebUI.verifyMatch("Bio: " + githubBio, displayedBio, false, FailureHandling.CONTINUE_ON_FAILURE)
WebUI.verifyMatch("Twitter: " + githubTwitter, displayedTwitter, false, FailureHandling.CONTINUE_ON_FAILURE)
WebUI.verifyMatch(formattedJoinedDate, displayedJoined, false, FailureHandling.CONTINUE_ON_FAILURE)
WebUI.verifyMatch(githubPublicRepos.toString(), displayedPublicRepos.toString(), false, FailureHandling.CONTINUE_ON_FAILURE)
//WebUI.verifyMatch(formattedLastUpdateDate, displayedLastUpdate, false, FailureHandling.CONTINUE_ON_FAILURE)
WebUI.verifyMatch(githubFollowers.toString(), displayedFollowers.toString(), false, FailureHandling.CONTINUE_ON_FAILURE)
//WebUI.verifyMatch(githubFollowing.toString(), displayedFollowing.toString(), false, FailureHandling.CONTINUE_ON_FAILURE)
WebUI.verifyMatch(githubLocation, displayedLocation, false, FailureHandling.CONTINUE_ON_FAILURE)

// Step 8: Close the browser
WebUI.closeBrowser()
