# 🚀 Quick Setup Guide for Eclipse

## Step-by-Step Instructions to Run Tests

### STEP 1: Download Selenium JARs

1. Go to: https://www.selenium.dev/downloads/
2. Click "Download" under "Selenium Server (Grid)" section - NO!
3. Instead, go to the Java section and download "selenium-java-4.x.x.zip"
4. Extract the ZIP file
5. Copy ALL JAR files from:
   - The main folder (selenium-java-4.x.x.jar)
   - The "libs" subfolder (all JARs)
6. Paste them into: `ContractIQTests/lib/`

**Alternative Direct Download Links:**
- Latest Selenium: https://github.com/SeleniumHQ/selenium/releases

### STEP 2: Download ChromeDriver

1. Open Chrome browser
2. Go to: chrome://version/
3. Note your Chrome version (e.g., 120.0.6099.130)
4. Go to: https://chromedriver.chromium.org/downloads
5. Download ChromeDriver for your version
6. Extract `chromedriver.exe`
7. Place it in: `ContractIQTests/drivers/`

### STEP 3: Import Project in Eclipse

1. Open Eclipse IDE
2. Click: File → Import
3. Select: General → Existing Projects into Workspace
4. Click "Next"
5. Click "Browse" and select `ContractIQTests` folder
6. Check "ContractIQTests" in the projects list
7. Click "Finish"

### STEP 4: Add JARs to Build Path

1. In Eclipse Package Explorer, find the `lib` folder
2. Select ALL JAR files in it (Ctrl+A)
3. Right-click → Build Path → Add to Build Path
4. You should see a small jar icon appear on each file

**OR Alternative Method:**
1. Right-click on project → Build Path → Configure Build Path
2. Go to "Libraries" tab
3. Click "Add JARs..."
4. Expand lib folder and select all JARs
5. Click OK
6. Click "Apply and Close"

### STEP 5: Start the Application

Before running tests, start the ContractIQ application:

**Terminal 1 - Start Backend:**
```
cd contractiqB
pip install -r requirements.txt
python app.py
```

**Terminal 2 - Start Frontend:**
```
cd contractiqF
npm install
npm run dev
```

Wait until you see:
- Backend: "Running on http://127.0.0.1:5000"
- Frontend: "Local: http://localhost:5173/"

### STEP 6: Run Tests

1. In Eclipse, expand: src/test/java → tests
2. Right-click on `TestRunner.java`
3. Click: Run As → Java Application
4. Watch the tests execute in your browser!

---

## 🔧 Common Problems & Solutions

### Problem: "ChromeDriver not found"
**Solution:** Make sure chromedriver.exe is in the `drivers` folder

### Problem: "Class not found" errors
**Solution:** 
1. Right-click project → Build Path → Configure Build Path
2. Remove all JARs
3. Add all JARs from lib folder again

### Problem: "Connection refused"
**Solution:** Make sure both backend (port 5000) and frontend (port 5173) are running

### Problem: "Chrome version mismatch"
**Solution:** Download ChromeDriver that matches your Chrome browser version exactly

### Problem: "Element not found"
**Solution:** 
1. Increase wait times in config.properties
2. Make sure the application is fully loaded

---

## ✅ Checklist Before Running

- [ ] Java JDK installed (check: `java -version`)
- [ ] Eclipse IDE installed
- [ ] Selenium JARs in `lib` folder
- [ ] ChromeDriver in `drivers` folder
- [ ] JARs added to Build Path
- [ ] Backend running on port 5000
- [ ] Frontend running on port 5173

---

## 📞 Need Help?

Contact your test automation instructor if you face any issues!
