# ContractIQ Backend - Detailed Documentation

> **Flask REST API for Smart Contract Analysis**

---

## 1. Overview

The backend is a Flask-based REST API that handles:
- User authentication (registration, login, logout)
- PDF document upload and processing
- Contract clause extraction
- Dashboard statistics

**Base URL:** `http://localhost:5000`

---

## 2. Technology Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Python | 3.8+ | Programming Language |
| Flask | 3.0.0 | Web Framework |
| Flask-CORS | 4.0.0 | Cross-Origin Resource Sharing |
| SQLite | Built-in | Database |
| PyPDF2 | 3.0.1 | PDF Text Extraction |
| Werkzeug | 3.0.1 | Password Hashing |
| PyJWT | 2.8.0 | JWT Authentication |

---

## 3. Project Structure

```
contractiqB/
├── app.py              # Main Flask application (2084 lines)
│   ├── Configuration
│   ├── Helper Functions
│   ├── Auth Routes (/api/register, /api/login, /api/logout)
│   ├── Document Routes (/api/upload, /api/documents)
│   └── Dashboard Routes (/api/dashboard)
│
├── database.py         # Database operations (749 lines)
│   ├── init_db() - Initialize tables
│   ├── create_user() - Create new user
│   ├── get_user_by_email() - Find user
│   ├── save_document() - Save uploaded doc
│   ├── get_user_documents() - List user docs
│   └── get_dashboard_stats() - Get statistics
│
├── pdf_processor.py    # PDF processing (358 lines)
│   ├── extract_text_from_pdf() - Main extraction
│   ├── clean_extracted_text() - Text cleanup
│   └── get_pdf_info() - Get PDF metadata
│
├── clause_extractor.py # Clause extraction (554 lines)
│   ├── CLAUSE_KEYWORDS - Keyword dictionary
│   ├── split_into_sentences() - Text splitting
│   ├── extract_clauses() - Main extraction
│   └── get_clause_summary() - Summary stats
│
├── requirements.txt    # Python dependencies
├── instance/
│   └── contractiq.db   # SQLite database file
└── uploads/            # Uploaded PDF storage
```

---

## 4. Configuration (app.py)

### Flask App Setup
```python
from flask import Flask, request, jsonify, session
from flask_cors import CORS

app = Flask(__name__)

# Secret key for session signing and JWT
app.config['SECRET_KEY'] = os.environ.get('SECRET_KEY', 'dev-secret-key-change-in-production')

# File upload settings
UPLOAD_FOLDER = os.path.join(os.path.dirname(__file__), 'uploads')
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER
app.config['MAX_CONTENT_LENGTH'] = 10 * 1024 * 1024  # 10 MB max

# Allowed file extensions
ALLOWED_EXTENSIONS = {'pdf'}

# Session configuration
app.config['SESSION_TYPE'] = 'filesystem'
app.config['SESSION_PERMANENT'] = False
app.config['SESSION_USE_SIGNER'] = True
```

### CORS Configuration
```python
CORS(app, supports_credentials=True, origins=[
    "http://localhost:3000",      # Create React App default
    "http://127.0.0.1:3000",
    "http://localhost:5173",      # Vite default port
    "http://127.0.0.1:5173"
])
```

**Why specific origins?** Security - prevents unauthorized domains from making API requests.

---

## 5. Helper Functions

### Password Validation
```python
def validate_password(password):
    """
    Validates password against security requirements.
    
    Requirements:
    - Minimum 8 characters
    - Maximum 128 characters
    - At least one uppercase letter (A-Z)
    - At least one lowercase letter (a-z)
    - At least one digit (0-9)
    - At least one special character (!@#$%^&* etc.)
    
    Returns:
        tuple: (is_valid: bool, error_message: str or None)
    """
    if not password:
        return False, "Password is required"
    
    if len(password) < 8:
        return False, "Password must be at least 8 characters long"
    
    if len(password) > 128:
        return False, "Password must not exceed 128 characters"
    
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

### File Extension Check
```python
def allowed_file(filename):
    """Check if uploaded file has allowed extension (.pdf only)"""
    return '.' in filename and \
           filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS
```

### JWT Token Generation
```python
def generate_access_token(user_id):
    """Generate JWT access token with 24-hour expiry"""
    payload = {
        'user_id': user_id,
        'exp': datetime.utcnow() + timedelta(hours=24)
    }
    return jwt.encode(payload, app.config['SECRET_KEY'], algorithm='HS256')
```

---

## 6. API Endpoints

### 6.1 Authentication Endpoints

#### POST /api/register
Register a new user account.

**Request:**
```json
{
    "username": "john_doe",        // Required: 3-50 chars, alphanumeric + underscore
    "email": "john@example.com",   // Required: valid email format
    "password": "Test@1234",       // Required: meets password requirements
    "role": "client"               // Optional: "admin" | "lawyer" | "client"
}
```

**Response (201 Created):**
```json
{
    "success": true,
    "message": "User registered successfully",
    "user": {
        "id": 1,
        "username": "john_doe",
        "role": "client"
    }
}
```

**Error Responses:**
```json
// 400 - Validation Error
{
    "success": false,
    "error": "Password must contain at least one uppercase letter"
}

// 409 - Conflict
{
    "success": false,
    "error": "Username already exists"
}
```

---

#### POST /api/login
Authenticate user and get access token.

**Request:**
```json
{
    "email": "john@example.com",
    "password": "Test@1234"
}
```

**Response (200 OK):**
```json
{
    "success": true,
    "message": "Login successful",
    "user": {
        "id": 1,
        "username": "john_doe",
        "email": "john@example.com",
        "role": "client"
    },
    "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Error Responses:**
```json
// 400 - Missing Fields
{
    "success": false,
    "error": "Email and password are required"
}

// 401 - Invalid Credentials
{
    "success": false,
    "error": "Invalid email or password"
}
```

---

#### POST /api/logout
Log out the current user (clears session).

**Request:** No body required

**Response (200 OK):**
```json
{
    "success": true,
    "message": "Logged out successfully"
}
```

---

#### GET /api/profile
Get current user's profile information.

**Headers:**
```
Authorization: Bearer <access_token>
```

**Response (200 OK):**
```json
{
    "success": true,
    "user": {
        "id": 1,
        "username": "john_doe",
        "email": "john@example.com",
        "role": "client",
        "created_at": "2025-02-06 10:30:00"
    }
}
```

---

### 6.2 Document Endpoints

#### POST /api/upload
Upload a PDF document for clause extraction.

**Headers:**
```
Authorization: Bearer <access_token>
Content-Type: multipart/form-data
```

**Request:**
- Form field `file`: PDF file (max 10 MB)

**Response (200 OK):**
```json
{
    "success": true,
    "message": "Document uploaded and processed successfully",
    "document": {
        "id": 1,
        "filename": "a1b2c3d4_1707234567.pdf",
        "original_filename": "contract.pdf",
        "upload_date": "2025-02-06 14:30:52",
        "clauses": {
            "Termination": [
                "Either party may terminate this agreement with 30 days written notice."
            ],
            "Payment": [
                "Client agrees to pay all invoices within 30 days of receipt."
            ],
            "Confidentiality": [
                "Both parties agree to keep all proprietary information confidential."
            ],
            "Liability": [
                "Neither party shall be liable for indirect damages."
            ],
            "Intellectual Property": []
        },
        "clauses_summary": {
            "total_clauses": 4,
            "categories": {
                "Termination": 1,
                "Payment": 1,
                "Confidentiality": 1,
                "Liability": 1
            }
        }
    }
}
```

---

#### POST /api/extract-text
Extract clauses from pasted text (no file upload).

**Headers:**
```
Authorization: Bearer <access_token>
Content-Type: application/json
```

**Request:**
```json
{
    "text": "This Agreement is entered into... TERMINATION: Either party may terminate...",
    "document_name": "My Contract"  // Optional
}
```

**Response (200 OK):**
```json
{
    "success": true,
    "message": "Clauses extracted successfully",
    "document": {
        "id": 2,
        "original_filename": "My Contract",
        "clauses": { ... },
        "clauses_summary": { ... }
    }
}
```

---

#### GET /api/documents
Get all documents for the authenticated user.

**Headers:**
```
Authorization: Bearer <access_token>
```

**Response (200 OK):**
```json
{
    "success": true,
    "documents": [
        {
            "id": 1,
            "original_filename": "contract.pdf",
            "upload_date": "2025-02-06 14:30:52",
            "clauses_summary": {
                "total_clauses": 4,
                "categories": { ... }
            }
        }
    ]
}
```

---

#### GET /api/document/{id}
Get a specific document with full details.

**Headers:**
```
Authorization: Bearer <access_token>
```

**Response (200 OK):**
```json
{
    "success": true,
    "document": {
        "id": 1,
        "original_filename": "contract.pdf",
        "upload_date": "2025-02-06 14:30:52",
        "extracted_text": "Full extracted text here...",
        "clauses": {
            "Termination": [...],
            "Payment": [...],
            ...
        },
        "clauses_summary": { ... }
    }
}
```

---

#### DELETE /api/document/{id}
Delete a document (ownership verified).

**Headers:**
```
Authorization: Bearer <access_token>
```

**Response (200 OK):**
```json
{
    "success": true,
    "message": "Document deleted successfully"
}
```

---

### 6.3 Dashboard Endpoint

#### GET /api/dashboard
Get dashboard statistics for the authenticated user.

**Headers:**
```
Authorization: Bearer <access_token>
```

**Response (200 OK):**
```json
{
    "success": true,
    "user": {
        "username": "john_doe",
        "email": "john@example.com",
        "role": "client"
    },
    "stats": {
        "total_documents": 5,
        "recent_uploads": 2,
        "total_clauses_extracted": 15,
        "recent_documents": [
            {
                "id": 5,
                "original_filename": "contract.pdf",
                "upload_date": "2025-02-06 14:30:00"
            }
        ],
        "account_created": "2025-02-01 10:00:00"
    }
}
```

---

## 7. Database Module (database.py)

### Database Schema

```sql
-- Users Table
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT UNIQUE NOT NULL,
    email TEXT UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    role TEXT DEFAULT 'client' CHECK(role IN ('admin', 'lawyer', 'client')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Documents Table
CREATE TABLE IF NOT EXISTS documents (
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

-- Indexes for faster queries
CREATE INDEX IF NOT EXISTS idx_documents_user_id ON documents(user_id);
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
```

### Key Functions

```python
def init_db():
    """Initialize database and create tables"""
    
def get_db_connection():
    """Get SQLite connection with row factory"""
    conn = sqlite3.connect(DATABASE_PATH)
    conn.row_factory = sqlite3.Row  # Access columns by name
    conn.execute("PRAGMA foreign_keys = ON")
    return conn

def create_user(username, email, password_hash, role='client'):
    """Create new user and return user_id or None"""
    
def get_user_by_email(email):
    """Find user by email for login"""
    
def save_document(user_id, filename, original_filename, file_path, extracted_text, clauses):
    """Save document metadata and return document_id"""
    
def get_user_documents(user_id):
    """Get all documents for a user"""
    
def get_dashboard_stats(user_id):
    """Get statistics for user's dashboard"""
```

---

## 8. PDF Processor Module (pdf_processor.py)

### Main Function

```python
def extract_text_from_pdf(pdf_path):
    """
    Extract text content from a PDF file.
    
    Process:
    1. Validate file exists and is not empty
    2. Open PDF using PyPDF2.PdfReader
    3. Check if encrypted (password-protected)
    4. Extract text from each page
    5. Clean and combine text
    6. Return extracted text
    
    Args:
        pdf_path (str): Path to PDF file
    
    Returns:
        str: Cleaned extracted text, or None if extraction fails
    """
    if not os.path.exists(pdf_path):
        return None
    
    if os.path.getsize(pdf_path) == 0:
        return None
    
    reader = PdfReader(pdf_path)
    
    if reader.is_encrypted:
        try:
            if reader.decrypt('') == 0:  # Try empty password
                return None
        except:
            return None
    
    all_text = []
    for page in reader.pages:
        page_text = page.extract_text()
        if page_text:
            all_text.append(page_text)
    
    if not all_text:
        return None
    
    return clean_extracted_text('\n'.join(all_text))
```

### Text Cleaning

```python
def clean_extracted_text(text):
    """
    Clean extracted text by:
    - Removing extra whitespace
    - Fixing broken lines
    - Normalizing line endings
    """
    # Replace multiple spaces with single space
    text = re.sub(r' +', ' ', text)
    
    # Replace multiple newlines with double newline
    text = re.sub(r'\n+', '\n\n', text)
    
    return text.strip()
```

---

## 9. Clause Extractor Module (clause_extractor.py)

### Keyword Dictionary

```python
CLAUSE_KEYWORDS = {
    "Termination": [
        "termination", "terminate", "cancel", "cancellation",
        "end agreement", "end of agreement", "notice period",
        "right to terminate", "termination for cause",
        "termination for convenience"
    ],
    
    "Liability": [
        "liability", "indemnify", "indemnification", "damages",
        "liable", "limitation of liability", "hold harmless",
        "indemnity", "consequential damages", "direct damages"
    ],
    
    "Payment": [
        "payment", "fee", "invoice", "compensation",
        "remuneration", "price", "billing", "pay", "cost",
        "charges", "payment terms", "due date", "late payment"
    ],
    
    "Confidentiality": [
        "confidential", "confidentiality", "nda", "non-disclosure",
        "secret", "proprietary information", "confidential information",
        "trade secret", "disclose", "disclosure"
    ],
    
    "Intellectual Property": [
        "intellectual property", "copyright", "trademark",
        "patent", "ip rights", "proprietary rights",
        "ownership of work", "work for hire", "license",
        "royalty", "invention"
    ]
}
```

### Extraction Algorithm

```python
def extract_clauses(text):
    """
    Extract clauses from contract text using keyword matching.
    
    Algorithm:
    1. Split text into sentences
    2. For each sentence, check against all category keywords
    3. If keyword found, add sentence to that category
    4. Return dictionary with categories and matching sentences
    """
    sentences = split_into_sentences(text)
    clauses = {category: [] for category in CLAUSE_KEYWORDS}
    
    for sentence in sentences:
        sentence_lower = sentence.lower()
        for category, keywords in CLAUSE_KEYWORDS.items():
            for keyword in keywords:
                if keyword in sentence_lower:
                    if sentence not in clauses[category]:
                        clauses[category].append(sentence.strip())
                    break  # Only add once per category
    
    return clauses

def get_clause_summary(clauses):
    """Get summary statistics for extracted clauses"""
    total = sum(len(c) for c in clauses.values())
    categories = {cat: len(items) for cat, items in clauses.items() if items}
    return {
        "total_clauses": total,
        "categories": categories
    }
```

---

## 10. Security Implementation

### Password Hashing
```python
from werkzeug.security import generate_password_hash, check_password_hash

# During registration
password_hash = generate_password_hash(password)

# During login
if check_password_hash(user['password_hash'], password):
    # Login successful
```

### JWT Authentication
```python
import jwt

def generate_access_token(user_id):
    payload = {
        'user_id': user_id,
        'exp': datetime.utcnow() + timedelta(hours=24)
    }
    return jwt.encode(payload, app.config['SECRET_KEY'], algorithm='HS256')

def verify_access_token(token):
    try:
        payload = jwt.decode(token, app.config['SECRET_KEY'], algorithms=['HS256'])
        return payload['user_id']
    except jwt.ExpiredSignatureError:
        return None  # Token expired
    except jwt.InvalidTokenError:
        return None  # Invalid token
```

### Authentication Decorator
```python
@app.route('/api/protected')
def protected_route():
    # Check Authorization header
    auth_header = request.headers.get('Authorization')
    if auth_header and auth_header.startswith('Bearer '):
        token = auth_header.split(' ')[1]
        user_id = verify_access_token(token)
        if user_id:
            # User authenticated
            ...
        else:
            return jsonify({"error": "Invalid token"}), 401
    else:
        # Check session (cookie-based)
        if 'user_id' in session:
            user_id = session['user_id']
            ...
        else:
            return jsonify({"error": "Authentication required"}), 401
```

---

## 11. Running the Backend

### Prerequisites
- Python 3.8 or higher
- pip (Python package manager)

### Installation

```bash
# Navigate to backend folder
cd contractiqB

# Install dependencies
pip install -r requirements.txt

# Run the application
python app.py
```

### Expected Output
```
✓ Database initialized successfully at: instance/contractiq.db
 * Running on http://127.0.0.1:5000
 * Press CTRL+C to stop
```

### requirements.txt
```
Flask==3.0.0
Flask-CORS==4.0.0
PyPDF2==3.0.1
Werkzeug==3.0.1
PyJWT==2.8.0
```

---

## 12. Viva Questions - Backend

**Q: Why Flask over Django?**
> Flask is lightweight and perfect for REST APIs. We don't need Django's ORM, admin panel, or template engine.

**Q: How does password hashing work?**
> Werkzeug uses PBKDF2 algorithm. It never stores plain text - creates a hash on registration, verifies hash on login.

**Q: What is JWT and why use it?**
> JSON Web Token - stateless authentication. Server doesn't need to store session data. Good for APIs and mobile apps.

**Q: How do you handle CORS?**
> Flask-CORS with explicit whitelist of allowed origins. Only localhost:5173 can make requests.

**Q: Why SQLite?**
> Zero configuration, single file database, perfect for development and small applications.

**Q: How does clause extraction work?**
> Keyword matching - split text into sentences, check each against category keywords, classify matches.

---

*Backend documentation complete for viva preparation.*
