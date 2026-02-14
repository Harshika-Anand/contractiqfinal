# ContractIQ Selenium Test Automation Project

## 📋 Project Overview

This is a **pure Java Selenium WebDriver** test automation project for the **ContractIQ** smart contract analysis application. It uses the **Page Object Model (POM)** design pattern and is configured for **Eclipse IDE**.

### Test Coverage

| Test Suite | Test Cases | Description |
|------------|------------|-------------|
| HomePageTest | 11 tests | Landing page functionality |
| LoginPageTest | 16 tests | Login functionality |
| RegisterPageTest | 19 tests | Registration functionality |
| DashboardPageTest | 15 tests | Dashboard after login |
| DocumentsPageTest | 15 tests | Document upload & text extraction |
| **Total** | **76 tests** | Complete application coverage |

---

## 🛠️ Prerequisites

Before running the tests, ensure you have:

1. **Java JDK 8 or higher** installed
   - Download from: https://www.oracle.com/java/technologies/javase-downloads.html
   - Set JAVA_HOME environment variable

2. **Eclipse IDE** installed
   - Download from: https://www.eclipse.org/downloads/

3. **Chrome Browser** (latest version)
   - Download from: https://www.google.com/chrome/

4. **ChromeDriver** matching your Chrome version
   - Download from: https://chromedriver.chromium.org/downloads
   - Place in the `drivers` folder

---

## 📁 Project Structure

```
ContractIQTests/
├── src/test/java/
│   ├── pages/                    # Page Object Model classes
│   │   ├── BasePage.java         # Base class for all pages
│   │   ├── HomePage.java         # Home/Landing page
│   │   ├── LoginPage.java        # Login page
│   │   ├── RegisterPage.java     # Registration page
│   │   ├── DashboardPage.java    # Dashboard page
│   │   ├── DocumentsPage.java    # Documents page
│   │   └── NavbarComponent.java  # Navigation bar component
│   │
│   ├── tests/                    # Test classes
│   │   ├── BaseTest.java         # Base test with setup/teardown
│   │   ├── HomePageTest.java     # Home page tests
│   │   ├── LoginPageTest.java    # Login tests
│   │   ├── RegisterPageTest.java # Registration tests
│   │   ├── DashboardPageTest.java# Dashboard tests
│   │   ├── DocumentsPageTest.java# Documents tests
│   │   └── TestRunner.java       # Main test runner
│   │
│   └── utils/                    # Utility classes
│       ├── ConfigReader.java     # Configuration reader
│       ├── WaitHelper.java       # Explicit wait utilities
│       ├── TestDataGenerator.java# Test data generation
│       └── ScreenshotHelper.java # Screenshot capture
│
├── lib/                          # Selenium JAR files (add manually)
├── drivers/                      # WebDriver executables
├── testdata/                     # Test data files
├── reports/                      # Test reports & screenshots
├── config.properties             # Test configuration
├── .project                      # Eclipse project file
├── .classpath                    # Eclipse classpath
└── README.md                     # This file
```

---

## 🚀 Setup Instructions

### Step 1: Download Selenium JARs

Download the following JAR files and place them in the `lib` folder:

1. **Selenium Java** (version 4.x recommended)
   - Download from: https://www.selenium.dev/downloads/
   - Extract and copy all JARs to `lib` folder

2. Required JARs:
   - selenium-java-4.x.x.jar
   - selenium-api-4.x.x.jar
   - selenium-chrome-driver-4.x.x.jar
   - selenium-support-4.x.x.jar
   - All dependency JARs from the `libs` folder in the Selenium download

### Step 2: Download ChromeDriver

1. Check your Chrome version: Chrome menu → Help → About Google Chrome
2. Download matching ChromeDriver from: https://chromedriver.chromium.org/downloads
3. Extract and place `chromedriver.exe` in the `drivers` folder

### Step 3: Import Project in Eclipse

1. Open Eclipse IDE
2. Go to **File → Import**
3. Select **General → Existing Projects into Workspace**
4. Browse to `ContractIQTests` folder
5. Click **Finish**

### Step 4: Add JARs to Build Path

1. Right-click on project → **Build Path → Configure Build Path**
2. Go to **Libraries** tab
3. Click **Add JARs** or **Add External JARs**
4. Select all JAR files from the `lib` folder
5. Click **Apply and Close**

---

## ▶️ Running Tests

### Method 1: Run All Tests (Recommended)

1. In Eclipse, navigate to `src/test/java/tests/TestRunner.java`
2. Right-click → **Run As → Java Application**

### Method 2: Run Individual Test Suites

1. Navigate to any test class (e.g., `LoginPageTest.java`)
2. Right-click → **Run As → Java Application**

### Method 3: Run from Command Line

```bash
cd ContractIQTests
javac -cp "lib/*;src/test/java" src/test/java/tests/*.java src/test/java/pages/*.java src/test/java/utils/*.java
java -cp "lib/*;src/test/java" tests.TestRunner
```

---

## ⚙️ Configuration

Edit `config.properties` to customize:

```properties
# Application URL
base.url=http://localhost:5173

# Browser (chrome, firefox, edge)
browser=chrome

# Timeouts (seconds)
implicit.wait=10
explicit.wait=15

# Headless mode
headless=false
```

---

## 📝 Test Cases Summary

### Home Page Tests (11)
- TC001: Verify home page display
- TC002: Verify main title text
- TC003: Verify subtitle text
- TC004: Verify Get Started button
- TC005: Verify Sign In button
- TC006: Verify feature cards
- TC007: Get Started navigation
- TC008: Sign In navigation
- TC009-011: Individual feature cards

### Login Page Tests (16)
- TC001-007: Page element verification
- TC008-009: Field placeholders/hints
- TC010-011: Required field validation
- TC012: Register link navigation
- TC013-016: Login validation scenarios

### Register Page Tests (19)
- TC001-010: Page element verification
- TC011: Role dropdown options
- TC012: Login link navigation
- TC013-017: Registration scenarios
- TC018-019: Field placeholders

### Dashboard Page Tests (15)
- TC001-002: Page display & welcome message
- TC003-006: Statistics cards
- TC007-009: Recent documents section
- TC010-015: Navigation & counts

### Documents Page Tests (15)
- TC001-005: Page & tab verification
- TC006-010: Form elements
- TC011-015: Functionality tests

---

## 🐛 Troubleshooting

### Common Issues:

1. **ChromeDriver version mismatch**
   - Ensure ChromeDriver version matches your Chrome browser version

2. **Element not found errors**
   - Increase wait times in `config.properties`
   - Check if the application is running

3. **Build path errors**
   - Ensure all Selenium JARs are added to build path
   - Refresh the project (F5)

4. **Application not running**
   - Start the backend: `cd contractiqB && python app.py`
   - Start the frontend: `cd contractiqF && npm run dev`

---

## 👨‍🎓 For Students

This project demonstrates:
- **Page Object Model (POM)** design pattern
- **Selenium WebDriver** for browser automation
- **Test organization** with clear structure
- **Reusable utilities** for waits, screenshots, test data
- **Configuration management** with properties files

---

## 📞 Support

For issues or questions about this test automation project, please contact your test automation instructor.

---

**Happy Testing! 🚀**
