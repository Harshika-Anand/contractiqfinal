# 🚀 MAVEN + TESTNG SETUP GUIDE FOR ECLIPSE

Your ContractIQ test project has been converted to use **Maven** and **TestNG** framework - this is the industry standard for test automation!

## ✅ What Has Been Done

1. ✓ Created **pom.xml** - Maven configuration with all dependencies
2. ✓ Created **testng.xml** - Test suite configuration  
3. ✓ Updated **BaseTest.java** - Added TestNG annotations (@BeforeMethod, @AfterMethod)
4. ✓ Converted **LoginPageTest.java** - Sample test with @Test annotations
5. ✓ Deprecated TestRunner.java - No longer needed

## 📦 STEP 1: Install Maven in Eclipse

### If Maven is NOT installed:

1. **Download Maven**
   - Go to: https://maven.apache.org/download.cgi
   - Download "Binary zip archive" (latest version)
   - Extract to a location (e.g., C:\apache-maven-3.x.x)

2. **Configure Maven in Eclipse**
   - Open Eclipse
   - Go to: **Eclipse → Preferences** (or **File → Settings** on some versions)
   - Search for "Maven"
   - Click **Maven → Installations**
   - Click **Add...**
   - Browse to your Maven installation folder
   - Click **Apply and Close**

### If Maven IS installed:
Skip to Step 2!

---

## 🔄 STEP 2: Convert Project to Maven Project

1. Right-click **ContractIQTests** project in Eclipse
2. Select: **Configure → Convert to Maven Project**
3. Eclipse will prompt you with dialog
4. Leave default settings and click **Finish**
5. Wait for the project to rebuild

---

## 🎯 STEP 3: Verify pom.xml is Present

Check that these files exist in ContractIQTests root directory:
- ✓ **pom.xml** - Contains dependencies
- ✓ **testng.xml** - Contains test suite configuration
- ✓ **src/test/java/** - Standard Maven test directory structure

If pom.xml is missing, the conversion didn't work. Contact your teacher or recreate it.

---

## 🔧 STEP 4: Update Remaining Test Classes (IMPORTANT!)

You still need to convert the other test classes to use TestNG:

### Classes to Convert:
- [ ] HomePageTest.java
- [ ] RegisterPageTest.java
- [ ] DashboardPageTest.java
- [ ] DocumentsPageTest.java
- [ ] DocumentComparisonPageTest.java

### How to Convert Each Test Class:

For each test class, follow this pattern (already done for LoginPageTest):

1. **Add imports:**
```java
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
```

2. **Update beforeTest() method:**
```java
@BeforeMethod
public void beforeTest() {
    setUp();
    navigateTo[PageName]();
    [pageName] = new [PageName](driver);
}
```

3. **Update afterTest() method:**
```java
@AfterMethod
public void afterTest() {
    tearDown();
}
```

4. **Add @Test annotation to ALL test methods:**
```java
@Test  // ADD THIS LINE
public void testSomething() {
    // Remove the beforeTest() and afterTest() calls
    // Remove try-finally blocks
    // Keep only the actual test logic
    
    boolean result = somePage.doSomething();
    printTestResult("TC001", result);
    assert result;
}
```

5. **Remove the main() method** at the end of the class

6. **Remove beforeTest()/afterTest() calls** from inside test methods

### Example Before:
```java
public void testLoginPageDisplay() {
    beforeTest();
    try {
        boolean result = loginPage.isPageDisplayed();
        printTestResult("TC001_LoginPageDisplay", result);
        assert result : "Login page should be displayed";
    } finally {
        afterTest();
    }
}
```

### Example After:
```java
@Test
public void testLoginPageDisplay() {
    boolean result = loginPage.isPageDisplayed();
    printTestResult("TC001_LoginPageDisplay", result);
    assert result : "Login page should be displayed";
}
```

---

## ▶️ STEP 5: Run Tests from Eclipse

### Option A: Using Maven Run Configuration (RECOMMENDED)

1. Right-click **ContractIQTests** project
2. Click **Run As**
3. Select **Maven test**
4. Tests will run using testng.xml configuration
5. Results appear in Console

### Option B: Using Right-Click Menu

1. Right-click **ContractIQTests** project
2. **Run As → Maven test**

### Option C: Using Command Line (Terminal)

Navigate to ContractIQTests folder and run:
```bash
mvn clean test
```

---

## 📊 Test Execution Flow

```
testng.xml
    ↓
Defines which test classes to run:
    • HomePageTest
    • LoginPageTest  
    • RegisterPageTest
    • DashboardPageTest
    • DocumentsPageTest
    • DocumentComparisonPageTest
    ↓
For each test class:
    @BeforeMethod → Run setUp()
    @Test → Run test method
    @AfterMethod → Run tearDown()
    ↓
    Repeat for each test
```

---

## 🛠️ Troubleshooting

### Problem: "Maven is not installed"
**Solution:** Complete Step 1 above to install Maven

### Problem: "testng.xml not found"
**Solution:** File should be in project root directory. Verify its location.

### Problem: Tests don't run, no output
**Solution:** 
1. Right-click project → Maven → Update Project
2. Wait for rebuild
3. Try running tests again

### Problem: Compilation errors in test classes
**Solution:** First convert that test class following the pattern in Step 4

### Problem: "No tests were run"
**Solution:** 
1. Make sure test methods have **@Test** annotation
2. Make sure test class name ends with "Test" (e.g., LoginPageTest)
3. Make sure test class is listed in testng.xml

### Problem: Port already in use (5000 or 5173)
**Solution:** Make sure backend and frontend are running on correct ports
- Backend: http://127.0.0.1:5000
- Frontend: http://localhost:5173

---

## 📋 Checklist Before Running Tests

- [ ] Maven installed and configured in Eclipse
- [ ] Project converted to Maven Project
- [ ] pom.xml exists in project root
- [ ] testng.xml exists in project root
- [ ] All test classes have @Test annotations on test methods
- [ ] All test classes have @BeforeMethod and @AfterMethod
- [ ] Backend running on port 5000
- [ ] Frontend running on port 5173
- [ ] ChromeDriver in ContractIQTests/drivers/ folder
- [ ] All JAR files from Selenium in lib/ folder

---

## 📚 Additional Resources

- TestNG Documentation: https://testng.org/doc/
- Maven Documentation: https://maven.apache.org/
- Selenium + Maven: https://www.selenium.dev/documentation/webdriver/getting_started/

---

**Your teacher expects you to:**
1. ✅ Use Maven for build management
2. ✅ Use TestNG for test framework
3. ✅ Use @Test, @BeforeMethod, @AfterMethod annotations
4. ✅ Have testng.xml configuration file
5. ✅ Run tests from Eclipse using Maven

Good luck! 🎉
