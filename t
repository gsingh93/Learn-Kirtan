[1mdiff --git a/src/gsingh/learnkirtan/settings/SettingsManager.java b/src/gsingh/learnkirtan/settings/SettingsManager.java[m
[1mindex ef47f89..1b46e5e 100644[m
[1m--- a/src/gsingh/learnkirtan/settings/SettingsManager.java[m
[1m+++ b/src/gsingh/learnkirtan/settings/SettingsManager.java[m
[36m@@ -78,7 +78,7 @@[m [mpublic class SettingsManager {[m
 	/** The file manager for this class */[m
 	private FileManager fileManager;[m
 [m
[31m-	private SettingsManager() throws XPathExpressionException {[m
[32m+[m	[32mprivate SettingsManager() {[m
 		// Retrieve DOM from XML file[m
 		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();[m
 		DocumentBuilder db;[m
[36m@@ -96,18 +96,17 @@[m [mpublic class SettingsManager {[m
 			e.printStackTrace();[m
 		}[m
 [m
[31m-		initSettings();[m
[32m+[m		[32mtry {[m
[32m+[m			[32minitSettings();[m
[32m+[m		[32m} catch (XPathExpressionException e) {[m
[32m+[m			[32m// TODO Auto-generated catch block[m
[32m+[m			[32me.printStackTrace();[m
[32m+[m		[32m}[m
 	}[m
 [m
 	public static SettingsManager getInstance() {[m
 		if (instance == null)[m
[31m-			try {[m
[31m-				instance = new SettingsManager();[m
[31m-			} catch (XPathExpressionException e) {[m
[31m-				// TODO: Catch exception/make custom exception[m
[31m-				throw new RuntimeException([m
[31m-						"An error occured while reading the config file");[m
[31m-			}[m
[32m+[m			[32minstance = new SettingsManager();[m
 		return instance;[m
 	}[m
 [m
[36m@@ -115,7 +114,6 @@[m [mpublic class SettingsManager {[m
 	/**[m
 	 * Initializes the settings variables by reading the config file[m
 	 * [m
[31m-	 * @throws XPathExpressionException[m
 	 */[m
 	private void initSettings() throws XPathExpressionException {[m
 [m
[36m@@ -147,10 +145,8 @@[m [mpublic class SettingsManager {[m
 	 * [m
 	 * @param bool[m
 	 *            whether to enable or disable this setting[m
[31m-	 * @throws XPathExpressionException[m
 	 */[m
[31m-	public void setShowSargamLabels(boolean bool)[m
[31m-			throws XPathExpressionException {[m
[32m+[m	[32mpublic void setShowSargamLabels(boolean bool) {[m
 		showSargamLabels = bool;[m
 		changeSetting(SHOW_SARGAM_LABELS_XPATH, Boolean.toString(bool));[m
 	}[m
[36m@@ -160,10 +156,8 @@[m [mpublic class SettingsManager {[m
 	 * [m
 	 * @param bool[m
 	 *            whether to enable or disable this setting[m
[31m-	 * @throws XPathExpressionException[m
 	 */[m
[31m-	public void setShowKeyboardLabels(boolean bool)[m
[31m-			throws XPathExpressionException {[m
[32m+[m	[32mpublic void setShowKeyboardLabels(boolean bool) {[m
 		showKeyboardLabels = bool;[m
 		changeSetting(SHOW_KEYBOARD_LABELS_XPATH, Boolean.toString(bool));[m
 	}[m
[36m@@ -185,10 +179,8 @@[m [mpublic class SettingsManager {[m
 	 *            whether to turn update checks[m
 	 * @param duration[m
 	 *            used to specify length of time not to remind.[m
[31m-	 * @throws XPathExpressionException[m
 	 */[m
[31m-	public void setCheckForUpdate(boolean bool, Duration duration)[m
[31m-			throws XPathExpressionException {[m
[32m+[m	[32mpublic void setCheckForUpdate(boolean bool, Duration duration) {[m
 		if (!bool) {[m
 			String d = calculateDate(duration);[m
 			checkForUpdate = false;[m
[36m@@ -232,10 +224,8 @@[m [mpublic class SettingsManager {[m
 	 * Checks whether the duration for not checking reminding about updates is[m
 	 * completed[m
 	 * [m
[31m-	 * @throws XPathExpressionException[m
 	 */[m
[31m-	public void checkReminderOffDurationReached()[m
[31m-			throws XPathExpressionException {[m
[32m+[m	[32mpublic void checkReminderOffDurationReached() {[m
 		long time = System.currentTimeMillis();[m
 [m
 		if (checkForUpdateInterval < time) {[m
[36m@@ -249,9 +239,8 @@[m [mpublic class SettingsManager {[m
 	 * [m
 	 * @param key[m
 	 *            the key number input by the user from 1 to 36[m
[31m-	 * @throws XPathExpressionException[m
 	 */[m
[31m-	public void setSaKey(int key) throws XPathExpressionException {[m
[32m+[m	[32mpublic void setSaKey(int key) {[m
 		saKey = key;[m
 		changeSetting(SA_KEY_XPATH, String.valueOf(saKey));[m
 	}[m
[36m@@ -270,18 +259,21 @@[m [mpublic class SettingsManager {[m
 	 *            full name of the setting, with '.'s to delimit nodes[m
 	 * @param value[m
 	 *            value to change the setting to[m
[31m-	 * @throws XPathExpressionException[m
 	 */[m
[31m-	private void changeSetting(String xPathQuery, String value)[m
[31m-			throws XPathExpressionException {[m
[31m-		Node node = (Node) xPath.evaluate(xPathQuery, dom, XPathConstants.NODE);[m
[31m-		node.setTextContent(value);[m
[32m+[m	[32mprivate void changeSetting(String xPathQuery, String value) {[m
 		try {[m
[32m+[m			[32mNode node = (Node) xPath.evaluate(xPathQuery, dom,[m
[32m+[m					[32mXPathConstants.NODE);[m
[32m+[m			[32mnode.setTextContent(value);[m
[32m+[m			[32m// TODO Handle error[m
 			fileManager.saveSettings(dom);[m
 		} catch (TransformerFactoryConfigurationError e) {[m
 			e.printStackTrace();[m
 		} catch (TransformerException e) {[m
 			e.printStackTrace();[m
[32m+[m		[32m} catch (XPathExpressionException e) {[m
[32m+[m			[32m// TODO Auto-generated catch block[m
[32m+[m			[32me.printStackTrace();[m
 		}[m
 	}[m
 }[m
