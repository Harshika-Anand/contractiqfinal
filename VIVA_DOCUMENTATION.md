# ContractIQ - Complete Project Documentation for Viva

> **Smart Contract Analysis Application** - A full-stack web application with automated testing

---

## Table of Contents

1. [Project Overview](#1-project-overview)
2. [System Architecture](#2-system-architecture)
3. [Technology Stack](#3-technology-stack)
4. [Backend Documentation](#4-backend-documentation)
5. [Frontend Documentation](#5-frontend-documentation)
6. [Testing Documentation](#6-testing-documentation)
7. [Database Design](#7-database-design)
8. [Security Features](#8-security-features)
9. [API Endpoints](#9-api-endpoints)
10. [How to Run the Project](#10-how-to-run-the-project)
11. [Viva Questions & Answers](#11-viva-questions--answers)

---

## 1. Project Overview

### What is ContractIQ?

ContractIQ is a **Smart Contract Analysis Application** that helps legal professionals, businesses, and individuals analyze contract documents automatically. 

### Key Features

| Feature | Description |
|---------|-------------|
| **PDF Upload** | Upload contract documents in PDF format |
| **Text Extraction** | Automatically extract text content from PDFs using PyPDF2 |
| **Clause Identification** | Use keyword matching to identify and categorize important contract clauses |
| **Text Paste** | Paste contract text directly for analysis |
| **Dashboard Analytics** | View statistics about uploaded documents and extracted clauses |
| **User Authentication** | Secure login/registration with role-based access |

### Clause Categories Detected

1. **⚖️ Termination** - How and when the contract can be ended
2. **💰 Payment** - Financial terms and obligations
3. **🔒 Confidentiality** - Information protection requirements
4. **⚠️ Liability** - Responsibility and indemnification terms
5. **💡 Intellectual Property** - IP ownership and rights

### Target Users

- **Lawyers** - Quickly review contracts and identify key terms
- **Businesses** - Analyze vendor contracts and agreements
- **Freelancers** - Understand client contracts before signing
- **Students** - Learn about contract structure and key clauses

---

## 2. System Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                         FRONTEND (React + Vite)                      │
│                         Port: 5173                                   │
│  ┌───────────┐ ┌───────────┐ ┌───────────┐ ┌───────────────────────┐│
│  │   Home    │ │   Login   │ │ Register  │ │      Dashboard        ││
│  └───────────┘ └───────────┘ └───────────┘ └───────────────────────┘│
│  ┌───────────────────────────────────────────────────────────────┐  │
│  │                      Documents Page                            │  │
│  │  ┌─────────────────┐    ┌─────────────────────────────────┐   │  │
│  │  │   PDF Upload    │    │       Text Paste                │   │  │
│  │  └─────────────────┘    └─────────────────────────────────┘   │  │
│  └───────────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────────┘
                                   │
                                   │ HTTP/REST API (Axios)
                                   │ JWT Token Authentication
                                   ▼
┌─────────────────────────────────────────────────────────────────────┐
│                         BACKEND (Flask API)                          │
│                         Port: 5000                                   │
│  ┌───────────────────────────────────────────────────────────────┐  │
│  │                      app.py (Main Application)                 │  │
│  │  ┌─────────────┐ ┌─────────────┐ ┌─────────────────────────┐  │  │
│  │  │ Auth Routes │ │ Doc Routes  │ │   Dashboard Routes      │  │  │
│  │  └─────────────┘ └─────────────┘ └─────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────────┘  │
│                                                                      │
│  ┌─────────────────┐ ┌─────────────────┐ ┌─────────────────────┐    │
│  │  database.py    │ │ pdf_processor   │ │  clause_extractor   │    │
│  │  (SQLite ORM)   │ │    (PyPDF2)     │ │(Keyword Matching)   │    │
│  └─────────────────┘ └─────────────────┘ └─────────────────────┘    │
└─────────────────────────────────────────────────────────────────────┘
                                   │
                                   ▼
┌─────────────────────────────────────────────────────────────────────┐
│                         DATABASE (SQLite)                            │
│  ┌───────────────────────┐    ┌───────────────────────────────────┐ │
│  │       users           │    │           documents               │ │
│  │  - id                 │    │  - id                             │ │
│  │  - username           │◄───│  - user_id (FK)                   │ │
│  │  - email              │    │  - filename                       │ │
│  │  - password_hash      │    │  - original_filename              │ │
│  │  - role               │    │  - extracted_text                 │ │
│  │  - created_at         │    │  - clauses (JSON)                 │ │
│  └───────────────────────┘    │  - upload_date                    │ │
│                               └───────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────┘
                                   │
                                   ▼
┌─────────────────────────────────────────────────────────────────────┐
│                     TESTING (Selenium WebDriver)                     │
│  ┌───────────────────────────────────────────────────────────────┐  │
│  │   Page Object Model (POM) - 76 Test Cases                      │  │
│  │  ┌──────────────┐ ┌──────────────┐ ┌──────────────────────┐   │  │
│  │  │  LoginPage   │ │ RegisterPage │ │   DocumentsPage      │   │  │
│  │  │  (16 tests)  │ │  (19 tests)  │ │    (15 tests)        │   │  │
│  │  └──────────────┘ └──────────────┘ └──────────────────────┘   │  │
│  └───────────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 3. Technology Stack

### Backend Technologies

| Technology | Purpose | Version |
|------------|---------|---------|
| **Python** | Programming Language | 3.8+ |
| **Flask** | Web Framework (REST API) | 3.0.0 |
| **Flask-CORS** | Cross-Origin Resource Sharing | 4.0.0 |
| **SQLite** | Relational Database | Built-in |
| **PyPDF2** | PDF Text Extraction | 3.0.1 |
| **Werkzeug** | Password Hashing (PBKDF2) | 3.0.1 |
| **PyJWT** | JWT Token Authentication | 2.8.0 |

### Frontend Technologies

| Technology | Purpose | Version |
|------------|---------|---------|
| **React** | UI Library | 18.x |
| **Vite** | Build Tool & Dev Server | 5.x |
| **Tailwind CSS** | Utility-First CSS Framework | 3.x |
| **React Router** | Client-Side Routing | 6.x |
| **Axios** | HTTP Client | 1.x |
| **React Hot Toast** | Toast Notifications | 2.x |

### Testing Technologies

| Technology | Purpose | Version |
|------------|---------|---------|
| **Selenium WebDriver** | Browser Automation | 4.x |
| **Java** | Programming Language | 8+ |
| **ChromeDriver** | Chrome Browser Driver | Matching Chrome |
| **Page Object Model** | Design Pattern | - |

### Why These Technologies?

1. **Flask** - Lightweight, easy to learn, perfect for REST APIs
2. **SQLite** - No separate database server needed, portable, zero-configuration
3. **PyPDF2** - Reliable PDF processing in pure Python
4. **React** - Component-based UI, large ecosystem, fast development
5. **Vite** - Extremely fast HMR (Hot Module Replacement)
6. **Tailwind CSS** - Rapid UI development without writing custom CSS
7. **Selenium** - Industry standard for web automation testing

---

## 4. Backend Documentation

### 4.1 Project Structure

```
contractiqB/
├── app.py              # Main Flask application (routes, config)
├── database.py         # SQLite database operations
├── pdf_processor.py    # PDF text extraction using PyPDF2
├── clause_extractor.py # Contract clause identification
├── requirements.txt    # Python dependencies
├── instance/           # Database file storage
│   └── contractiq.db   # SQLite database
└── uploads/            # Uploaded PDF files storage
```

### 4.2 Main Application (app.py)

**Key Components:**

```python
# Flask Application Setup
app = Flask(__name__)
app.config['SECRET_KEY'] = os.environ.get('SECRET_KEY', 'dev-secret-key')
app.config['UPLOAD_FOLDER'] = 'uploads'
app.config['MAX_CONTENT_LENGTH'] = 10 * 1024 * 1024  # 10 MB

# CORS Configuration
CORS(app, supports_credentials=True, origins=[
    "http://localhost:5173",  # Vite dev server
    "http://127.0.0.1:5173"
])
```

**Password Validation Function:**

```python
def validate_password(password):
    """
    Requirements:
    - Minimum 8 characters
    - At least one uppercase letter
    - At least one lowercase letter
    - At least one digit
    - At least one special character
    """
    if len(password) < 8:
        return False, "Password must be at least 8 characters long"
    if not any(c.isupper() for c in password):
        return False, "Password must contain at least one uppercase letter"
    if not any(c.islower() for c in password):
        return False, "Password must contain at least one lowercase letter"
    if not any(c.isdigit() for c in password):
        return False, "Password must contain at least one number"
    special_chars = "!@#$%^&*()-_=+[]{}|;:',.<>?/"
    if not any(c in special_chars for c in password):
        return False, "Password must contain at least one special character"
    return True, None
```

### 4.3 Database Operations (database.py)

**Database Schema:**

```sql
-- Users Table
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT UNIQUE NOT NULL,
    email TEXT UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    role TEXT DEFAULT 'client' CHECK(role IN ('admin', 'lawyer', 'client')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Documents Table
CREATE TABLE documents (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    filename TEXT NOT NULL,
    original_filename TEXT NOT NULL,
    file_path TEXT NOT NULL,
    extracted_text TEXT,
    clauses TEXT,  -- JSON string
    upload_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

**Key Functions:**

| Function | Purpose |
|----------|---------|
| `init_db()` | Initialize database and create tables |
| `create_user()` | Create new user account |
| `get_user_by_email()` | Find user by email for login |
| `save_document()` | Save uploaded document metadata |
| `get_user_documents()` | Get all documents for a user |
| `get_dashboard_stats()` | Get statistics for dashboard |
| `delete_document()` | Delete document by ID |

### 4.4 PDF Processor (pdf_processor.py)

**Text Extraction Process:**

```python
def extract_text_from_pdf(pdf_path):
    """
    Process:
    1. Validate that the file exists
    2. Open and read the PDF using PyPDF2
    3. Check if PDF is encrypted (password-protected)
    4. Iterate through all pages and extract text
    5. Clean the extracted text (remove extra whitespace)
    6. Return the combined text from all pages
    """
    reader = PdfReader(pdf_path)
    
    if reader.is_encrypted:
        return None  # Cannot read password-protected PDFs
    
    all_text = []
    for page in reader.pages:
        page_text = page.extract_text()
        if page_text:
            all_text.append(page_text)
    
    return clean_extracted_text('\n'.join(all_text))
```

### 4.5 Clause Extractor (clause_extractor.py)

**Keyword-Based Clause Detection:**

```python
CLAUSE_KEYWORDS = {
    "Termination": [
        "termination", "terminate", "cancel", "cancellation",
        "end agreement", "notice period", "right to terminate"
    ],
    "Liability": [
        "liability", "indemnify", "indemnification", "damages",
        "liable", "limitation of liability", "hold harmless"
    ],
    "Payment": [
        "payment", "fee", "invoice", "compensation",
        "price", "billing", "pay", "cost", "charges"
    ],
    "Confidentiality": [
        "confidential", "confidentiality", "nda", "non-disclosure",
        "secret", "proprietary information", "trade secret"
    ],
    "Intellectual Property": [
        "intellectual property", "copyright", "trademark",
        "patent", "ip rights", "ownership of work", "license"
    ]
}

def extract_clauses(text):
    """
    Extracts clauses from contract text using keyword matching.
    Returns dict with categories as keys and list of matching sentences.
    """
    sentences = split_into_sentences(text)
    clauses = {category: [] for category in CLAUSE_KEYWORDS}
    
    for sentence in sentences:
        for category, keywords in CLAUSE_KEYWORDS.items():
            for keyword in keywords:
                if keyword.lower() in sentence.lower():
                    clauses[category].append(sentence)
                    break
    
    return clauses
```

---

## 5. Frontend Documentation

### 5.1 Project Structure

```
contractiqF/
├── src/
│   ├── main.jsx           # Application entry point
│   ├── App.jsx            # Main app with routing
│   ├── index.css          # Global styles (Tailwind)
│   ├── context/
│   │   └── AuthContext.jsx # Authentication state management
│   ├── services/
│   │   └── api.js         # Axios API client
│   ├── hooks/
│   │   └── useDocuments.js # Document operations hook
│   ├── components/
│   │   └── layout/
│   │       ├── Layout.jsx  # Main layout wrapper
│   │       └── Navbar.jsx  # Navigation bar
│   └── pages/
│       ├── Home.jsx        # Landing page
│       ├── Login.jsx       # Login page
│       ├── Register.jsx    # Registration page
│       ├── Dashboard.jsx   # User dashboard
│       ├── Documents.jsx   # Document upload/list
│       └── DocumentDetails.jsx # Single document view
├── index.html             # HTML template
├── package.json           # Dependencies
├── vite.config.js         # Vite configuration
├── tailwind.config.js     # Tailwind CSS config
└── postcss.config.js      # PostCSS config
```

### 5.2 Routing (App.jsx)

```jsx
// Protected Route - Requires authentication
const ProtectedRoute = ({ children }) => {
  const { isAuthenticated, loading } = useAuth();
  if (!isAuthenticated) return <Navigate to="/login" replace />;
  return children;
};

// Public Route - Redirects to dashboard if logged in
const PublicRoute = ({ children }) => {
  const { isAuthenticated } = useAuth();
  if (isAuthenticated) return <Navigate to="/dashboard" replace />;
  return children;
};

// Route Configuration
<Routes>
  <Route path="/" element={<Home />} />
  <Route path="/login" element={<PublicRoute><Login /></PublicRoute>} />
  <Route path="/register" element={<PublicRoute><Register /></PublicRoute>} />
  <Route path="/dashboard" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
  <Route path="/documents" element={<ProtectedRoute><Documents /></ProtectedRoute>} />
  <Route path="/documents/:id" element={<ProtectedRoute><DocumentDetails /></ProtectedRoute>} />
</Routes>
```

### 5.3 Authentication Context (AuthContext.jsx)

```jsx
const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  // Initialize from localStorage
  useEffect(() => {
    const storedUser = localStorage.getItem('user');
    const token = localStorage.getItem('accessToken');
    if (storedUser && token) {
      setUser(JSON.parse(storedUser));
      setIsAuthenticated(true);
    }
  }, []);

  const login = async (email, password) => { /* ... */ };
  const register = async (userData) => { /* ... */ };
  const logout = async () => { /* ... */ };

  return (
    <AuthContext.Provider value={{ user, isAuthenticated, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
```

### 5.4 API Service (api.js)

```javascript
const API_BASE_URL = 'http://localhost:5000';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: { 'Content-Type': 'application/json' },
  withCredentials: true,
});

// Request interceptor - Add JWT token
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('accessToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Response interceptor - Handle 401 errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('accessToken');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// API Modules
export const authAPI = {
  register: (userData) => api.post('/api/register', userData),
  login: (credentials) => api.post('/api/login', credentials),
  logout: () => api.post('/api/logout'),
  getProfile: () => api.get('/api/profile'),
};

export const documentAPI = {
  upload: (file) => api.post('/api/upload', formData, {...}),
  getAll: () => api.get('/api/documents'),
  getById: (id) => api.get(`/api/document/${id}`),
  delete: (id) => api.delete(`/api/document/${id}`),
};

export const dashboardAPI = {
  getStats: () => api.get('/api/dashboard'),
};
```

### 5.5 Key Pages

#### Home Page
- Landing page with hero section
- Feature highlights (PDF Analysis, Clause Detection, Dashboard Analytics)
- Call-to-action buttons (Get Started, Sign In)

#### Login Page
- Email and password fields
- Password requirements hint
- Link to registration page
- Toast notifications for success/error

#### Register Page
- Username, email, role selection, password fields
- Real-time password validation with visual feedback
- Password strength indicator
- Confirm password matching

#### Dashboard Page
- Welcome message with username
- Statistics cards (Total Documents, Recent Uploads, Clauses Extracted)
- Recent documents list with links to view details

#### Documents Page
- Two tabs: "Upload PDF" and "Paste Text"
- File upload with progress bar
- Text input area for direct text analysis
- Document list with delete functionality

---

## 6. Testing Documentation

### 6.1 Project Structure

```
ContractIQTests/
├── src/test/java/
│   ├── pages/               # Page Object Model classes
│   │   ├── BasePage.java    # Base class with common methods
│   │   ├── HomePage.java    # Home/Landing page
│   │   ├── LoginPage.java   # Login page
│   │   ├── RegisterPage.java # Registration page
│   │   ├── DashboardPage.java # Dashboard page
│   │   ├── DocumentsPage.java # Documents page
│   │   └── NavbarComponent.java # Navigation component
│   │
│   ├── tests/               # Test classes
│   │   ├── BaseTest.java    # Setup/teardown, browser config
│   │   ├── HomePageTest.java # Home page tests (11 tests)
│   │   ├── LoginPageTest.java # Login tests (16 tests)
│   │   ├── RegisterPageTest.java # Registration tests (19 tests)
│   │   ├── DashboardPageTest.java # Dashboard tests (15 tests)
│   │   ├── DocumentsPageTest.java # Documents tests (15 tests)
│   │   └── TestRunner.java  # Main test runner
│   │
│   └── utils/               # Utility classes
│       ├── ConfigReader.java # Read config.properties
│       ├── WaitHelper.java  # Explicit wait utilities
│       ├── TestDataGenerator.java # Generate test data
│       └── ScreenshotHelper.java # Screenshot capture
│
├── config.properties        # Test configuration
├── drivers/                 # WebDriver executables
├── lib/                     # Selenium JAR files
└── reports/                 # Test reports & screenshots
```

### 6.2 Test Coverage Summary

| Test Suite | Test Cases | Description |
|------------|------------|-------------|
| HomePageTest | 11 tests | Landing page functionality |
| LoginPageTest | 16 tests | Login functionality |
| RegisterPageTest | 19 tests | Registration functionality |
| DashboardPageTest | 15 tests | Dashboard after login |
| DocumentsPageTest | 15 tests | Document upload & text extraction |
| **Total** | **76 tests** | Complete application coverage |

### 6.3 Page Object Model (POM)

**BasePage.java - Common Methods:**

```java
public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }
    
    // Wait for element to be visible
    protected WebElement waitForElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    // Wait for element to be clickable
    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    
    // Click on element
    protected void click(By locator) {
        waitForClickable(locator).click();
    }
    
    // Enter text in input field
    protected void enterText(By locator, String text) {
        WebElement element = waitForElement(locator);
        element.clear();
        element.sendKeys(text);
    }
    
    // Check if element is displayed
    protected boolean isElementDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
```

**LoginPage.java - Page Object Example:**

```java
public class LoginPage extends BasePage {
    // Locators
    private By emailInput = By.id("email");
    private By passwordInput = By.id("password");
    private By signInButton = By.xpath("//button[contains(text(), 'Sign In')]");
    private By registerLink = By.linkText("Register here");
    
    // Actions
    public DashboardPage login(String email, String password) {
        enterText(emailInput, email);
        enterText(passwordInput, password);
        click(signInButton);
        pause(3000);
        return new DashboardPage(driver);
    }
    
    public boolean isPageDisplayed() {
        return isElementDisplayed(emailInput) && 
               isElementDisplayed(passwordInput) && 
               isElementDisplayed(signInButton);
    }
}
```

### 6.4 Test Example

```java
public class LoginPageTest extends BaseTest {
    private LoginPage loginPage;
    
    public void beforeTest() {
        setUp();                    // Initialize WebDriver
        navigateToLogin();          // Go to login page
        loginPage = new LoginPage(driver);
    }
    
    public void afterTest() {
        tearDown();                 // Close browser
    }
    
    /**
     * TC001: Verify login page is displayed correctly
     */
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
    
    /**
     * TC012: Verify successful login with valid credentials
     */
    public void testValidLogin() {
        beforeTest();
        try {
            DashboardPage dashboard = loginPage.login(VALID_EMAIL, VALID_PASSWORD);
            boolean result = dashboard.isPageDisplayed();
            printTestResult("TC012_ValidLogin", result);
            assert result : "Should navigate to dashboard after login";
        } finally {
            afterTest();
        }
    }
}
```

### 6.5 Test Data Generator

```java
public class TestDataGenerator {
    private static Random random = new Random();
    
    // Generate unique username
    public static String generateUsername() {
        return "testuser_" + System.currentTimeMillis();
    }
    
    // Generate unique email
    public static String generateEmail() {
        return "testuser_" + System.currentTimeMillis() + "@test.com";
    }
    
    // Generate valid password meeting all requirements
    public static String getValidTestPassword() {
        return "Test@1234";
    }
    
    // Sample contract text for testing
    public static String getSampleContractText() {
        return "This Agreement... TERMINATION: Either party may terminate...";
    }
}
```

### 6.6 Configuration (config.properties)

```properties
# Application URLs
base.url=http://localhost:5173
api.url=http://localhost:5000

# Browser Settings
browser=chrome
headless=false

# Timeout Settings
implicit.wait=10
explicit.wait=15

# Test User Credentials
test.user.email=testuser@test.com
test.user.password=Test@1234

# Screenshot Settings
screenshot.on.failure=true
screenshot.directory=reports/screenshots
```

---

## 7. Database Design

### Entity Relationship Diagram

```
┌───────────────────────┐         ┌───────────────────────────────────┐
│        USERS          │         │           DOCUMENTS               │
├───────────────────────┤         ├───────────────────────────────────┤
│ PK  id (INTEGER)      │◄────────│ FK  user_id (INTEGER)             │
│     username (TEXT)   │    1:N  │ PK  id (INTEGER)                  │
│     email (TEXT)      │         │     filename (TEXT)               │
│     password_hash     │         │     original_filename (TEXT)      │
│     role (TEXT)       │         │     file_path (TEXT)              │
│     created_at (TS)   │         │     extracted_text (TEXT)         │
└───────────────────────┘         │     clauses (TEXT/JSON)           │
                                  │     upload_date (TIMESTAMP)       │
                                  └───────────────────────────────────┘
```

### Relationship
- **One-to-Many**: One user can have many documents
- **CASCADE DELETE**: When user is deleted, all their documents are also deleted

---

## 8. Security Features

### 8.1 Password Security

| Feature | Implementation |
|---------|----------------|
| **Hashing** | PBKDF2 (Werkzeug) - Never store plain text passwords |
| **Minimum Length** | 8 characters |
| **Complexity** | Uppercase, lowercase, digit, special character required |
| **Max Length** | 128 characters (prevent DoS) |

### 8.2 Authentication

| Method | Description |
|--------|-------------|
| **Session-based** | Cookies for web browsers |
| **JWT Tokens** | Bearer tokens for API/mobile apps |
| **Token Expiry** | 24 hours |

### 8.3 API Security

| Feature | Implementation |
|---------|----------------|
| **CORS** | Restricted to specific origins |
| **Input Validation** | Server-side validation for all inputs |
| **File Upload** | Max 10MB, PDF only |
| **SQL Injection** | Parameterized queries (sqlite3) |
| **XSS Prevention** | React escapes output by default |

---

## 9. API Endpoints

### Authentication Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/register` | Register new user | No |
| POST | `/api/login` | User login | No |
| POST | `/api/logout` | User logout | Yes |
| GET | `/api/profile` | Get user profile | Yes |

### Document Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/upload` | Upload PDF document | Yes |
| POST | `/api/extract-text` | Extract clauses from text | Yes |
| GET | `/api/documents` | Get all user documents | Yes |
| GET | `/api/document/{id}` | Get single document | Yes |
| DELETE | `/api/document/{id}` | Delete document | Yes |

### Dashboard Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/dashboard` | Get dashboard stats | Yes |

### Sample API Requests/Responses

#### Register
```json
// Request
POST /api/register
{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "Test@1234",
    "role": "client"
}

// Response (201)
{
    "success": true,
    "message": "User registered successfully",
    "user": { "id": 1, "username": "john_doe", "role": "client" }
}
```

#### Login
```json
// Request
POST /api/login
{
    "email": "john@example.com",
    "password": "Test@1234"
}

// Response (200)
{
    "success": true,
    "access_token": "eyJhbGciOiJIUzI1NiIs...",
    "user": { "id": 1, "username": "john_doe", "email": "john@example.com" }
}
```

#### Upload Document
```json
// Request
POST /api/upload
Content-Type: multipart/form-data
file: contract.pdf

// Response (200)
{
    "success": true,
    "document": {
        "id": 1,
        "original_filename": "contract.pdf",
        "clauses": {
            "Termination": ["Either party may terminate..."],
            "Payment": ["Payment due within 30 days..."],
            "Confidentiality": [],
            "Liability": ["Neither party shall be liable..."],
            "Intellectual Property": []
        },
        "clauses_summary": {
            "total_clauses": 3,
            "categories": { "Termination": 1, "Payment": 1, "Liability": 1 }
        }
    }
}
```

---

## 10. How to Run the Project

### Prerequisites

1. **Python 3.8+** - For backend
2. **Node.js 18+** - For frontend
3. **Java JDK 8+** - For testing
4. **Chrome Browser** - For testing

### Step 1: Start Backend

```bash
cd contractiqB
pip install -r requirements.txt
python app.py
```
Backend runs on: `http://localhost:5000`

### Step 2: Start Frontend

```bash
cd contractiqF
npm install
npm run dev
```
Frontend runs on: `http://localhost:5173`

### Step 3: Run Tests (Optional)

1. Download Selenium JARs to `ContractIQTests/lib/`
2. Download ChromeDriver to `ContractIQTests/drivers/`
3. Open in Eclipse IDE
4. Run `TestRunner.java`

---

## 11. Viva Questions & Answers

### General Questions

**Q1: What is ContractIQ?**
> ContractIQ is a smart contract analysis application that allows users to upload PDF contracts and automatically extract key legal clauses using keyword-based analysis.

**Q2: What problem does this project solve?**
> It helps lawyers, businesses, and individuals quickly analyze contracts by automatically identifying important clauses like termination, payment, liability, confidentiality, and intellectual property terms.

**Q3: What is the architecture of this project?**
> It's a 3-tier architecture:
> - Frontend: React + Vite (provides UI)
> - Backend: Flask REST API (handles business logic)
> - Database: SQLite (stores user data and documents)

### Backend Questions

**Q4: Why did you choose Flask over Django?**
> Flask is lightweight and perfect for REST APIs. Django would be overkill for this project since we don't need its ORM, admin panel, or template engine. Flask gives us more control with less boilerplate.

**Q5: How does password hashing work?**
> We use Werkzeug's `generate_password_hash()` which implements PBKDF2. It never stores plain text passwords. When logging in, we use `check_password_hash()` to compare.

**Q6: How does JWT authentication work?**
> After login, the server creates a token using PyJWT signed with a secret key. The token contains user ID and expiration time. The frontend stores it and sends it with every request in the Authorization header.

**Q7: How does clause extraction work?**
> We split the text into sentences, then check each sentence against a dictionary of keywords for each category. If a sentence contains a keyword, it's classified into that category.

**Q8: Why SQLite instead of PostgreSQL/MySQL?**
> SQLite is perfect for development and small-scale applications. It requires no server setup, is portable (single file), and is built into Python.

### Frontend Questions

**Q9: Why React with Vite instead of Create React App?**
> Vite offers much faster hot module replacement (HMR) and build times. Create React App is slower and has more dependencies.

**Q10: What is Context API and why use it?**
> Context API is React's built-in state management. We use AuthContext to share authentication state across all components without prop drilling.

**Q11: How do you handle protected routes?**
> We created a ProtectedRoute component that checks if the user is authenticated. If not, it redirects to the login page using React Router's Navigate component.

**Q12: Why Tailwind CSS instead of Bootstrap?**
> Tailwind offers utility-first classes that make styling faster and more consistent. It results in smaller CSS bundles because unused styles are purged.

### Testing Questions

**Q13: What is Page Object Model (POM)?**
> POM is a design pattern where each web page has a corresponding class that contains locators and methods for interacting with that page. It separates test logic from page interaction logic.

**Q14: Why is POM beneficial?**
> - **Maintainability**: If UI changes, only page class needs updating
> - **Reusability**: Same page methods used across multiple tests
> - **Readability**: Tests read like user stories

**Q15: What is explicit wait vs implicit wait?**
> - **Implicit wait**: Global timeout applied to all element searches
> - **Explicit wait**: Waits for a specific condition (visibility, clickability) on a specific element

**Q16: How many test cases do you have?**
> We have 76 test cases covering:
> - Home page (11 tests)
> - Login (16 tests)
> - Registration (19 tests)
> - Dashboard (15 tests)
> - Documents (15 tests)

### Security Questions

**Q17: What password requirements do you enforce?**
> Minimum 8 characters, at least one uppercase, one lowercase, one digit, and one special character.

**Q18: How do you prevent SQL injection?**
> We use parameterized queries with the sqlite3 library. User input is never directly concatenated into SQL strings.

**Q19: How do you handle CORS?**
> We use Flask-CORS with explicit origin whitelist. Only requests from localhost:5173 (frontend) are allowed.

**Q20: What happens when a JWT token expires?**
> The backend returns a 401 status. The Axios interceptor catches this and redirects the user to the login page.

### Design Pattern Questions

**Q21: What design patterns did you use?**
> - **MVC** (Model-View-Controller) in Flask
> - **Component-based architecture** in React
> - **Page Object Model** in Selenium tests
> - **Factory pattern** for test data generation
> - **Singleton pattern** for database connection

**Q22: How do you handle errors?**
> - Backend: Try-catch blocks with proper HTTP status codes
> - Frontend: Axios interceptors for API errors, toast notifications for user feedback
> - Tests: Try-finally blocks to ensure teardown runs

---

## Summary

This project demonstrates a complete full-stack web application with:

✅ **Backend**: Flask REST API with SQLite database, JWT authentication, PDF processing  
✅ **Frontend**: React SPA with routing, state management, responsive design  
✅ **Testing**: 76 Selenium test cases using Page Object Model  
✅ **Security**: Password hashing, JWT tokens, input validation, CORS  
✅ **Best Practices**: Clean code, separation of concerns, design patterns  

**Valid Test Credentials:**
- Email: `testuser@test.com`
- Password: `Test@1234`

---

*Good luck with your viva!* 🎓
