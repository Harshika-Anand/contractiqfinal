# 🔧 ContractIQ Backend - Viva Preparation Document

## Complete Backend Architecture & Interview Guide

---

## 📌 **PART 1: Project Overview & Architecture**

### **What is ContractIQ?**
ContractIQ is a **Flask-based REST API** that automatically analyzes PDF contracts and extracts important clauses using keyword-based identification. It's designed for lawyers, businesses, and freelancers to quickly identify critical contract terms.

### **Key Features:**
1. **PDF Upload & Processing** - Users upload contracts, system extracts text
2. **Clause Extraction** - Automatically identifies 5 clause types
3. **User Authentication** - Secure login with JWT tokens & session management
4. **Document Management** - Store, retrieve, and organize contracts
5. **Comparison Feature** - Compare clauses between two documents
6. **Dashboard Analytics** - Statistics about documents and clauses

### **Technology Stack:**
| Component | Technology | Purpose |
|-----------|-----------|---------|
| Framework | Flask 3.0 | Lightweight web framework for REST API |
| Database | SQLite | File-based database, no separate server |
| PDF Processing | PyPDF2 | Text extraction from PDF files |
| Security | Werkzeug | Password hashing (PBKDF2) |
| Authentication | JWT (PyJWT) | Token-based API authentication |
| CORS | Flask-CORS | Enable frontend communication |

---

## 📋 **PART 2: Core Modules Explained**

### **Module 1: app.py (Main Flask Application)**

#### **Purpose:** 
Core Flask application containing all API endpoints and business logic

#### **Key Sections:**

**1. Imports & Initialization:**
```python
- Flask core components (request, jsonify, session)
- Security modules (generate_password_hash, check_password_hash)
- Custom modules (database, pdf_processor, clause_extractor)
- JWT for token authentication
```

**2. Configuration:**
```python
SECRET_KEY = For signing sessions & tokens
MAX_CONTENT_LENGTH = 10 MB (file upload limit)
UPLOAD_FOLDER = './uploads' (stores PDF files)
CORS Configuration = Allows frontend at localhost:3000 & 5173
```

#### **API Endpoints Implemented:**

**Authentication Endpoints:**
```
1. POST /api/register
   - Register new user with email, password, role
   - Validates password (min 8 chars, uppercase, lowercase, digit, special char)
   - Returns user ID & token

2. POST /api/login
   - Authenticate user with email & password
   - Returns JWT access token (24-hour expiry)
   - Token used for all subsequent API calls

3. POST /api/logout
   - Clear session
   - Invalidate token on client side

4. GET /api/profile
   - Get logged-in user's profile information
   - Requires: Authorization header with token
```

**Document Endpoints:**
```
5. POST /api/upload
   - Upload PDF contract file
   - Extract text from PDF
   - Extract clauses automatically
   - Store document metadata in database
   - Returns document ID & extracted clauses

6. GET /api/documents
   - Get all documents of logged-in user
   - Returns list with metadata

7. GET /api/documents/<id>
   - Get specific document details
   - Returns full text & extracted clauses

8. DELETE /api/documents/<id>
   - Delete document (ownership verified)
   - Remove from database & file system
```

**Analytics Endpoints:**
```
9. GET /api/dashboard
   - Get user statistics (total docs, clauses count)
   - Recent uploads info
   - Used for dashboard display
```

**Comparison Endpoints:**
```
10. POST /api/compare
    - Compare clauses from two documents
    - Identifies matching & unique clauses

11. GET /api/comparisons
    - Get comparison history of user
```

---

### **Module 2: clause_extractor.py (Core Logic)**

#### **Purpose:**
Extracts and categorizes contract clauses using keyword matching

#### **5 Clause Categories:**

**1. Termination Clauses:**
- Keywords: "terminate", "cancel", "end agreement", "notice period"
- What it finds: How contract can be ended, notice periods required

**2. Liability Clauses:**
- Keywords: "liability", "damages", "indemnify", "hold harmless"
- What it finds: Responsibility for damages, indemnification terms

**3. Payment Clauses:**
- Keywords: "payment", "fee", "invoice", "compensation", "price"
- What it finds: Financial terms, payment schedules, pricing

**4. Confidentiality Clauses:**
- Keywords: "confidential", "NDA", "trade secret", "proprietary"
- What it finds: Information protection requirements, NDAs

**5. Intellectual Property:**
- Keywords: "copyright", "trademark", "patent", "IP ownership"
- What it finds: IP ownership, usage rights, licenses

#### **How It Works:**
```python
1. Takes extracted text from PDF
2. Converts to lowercase for matching
3. Searches for keywords in each category
4. Extracts full sentences containing keywords
5. Groups by category
6. Returns organized clause data
```

#### **Key Functions:**

```python
extract_clauses(text) → {category: [clauses]}
- Input: Full contract text
- Output: Dictionary with 5 clause categories

get_clause_summary(clauses) → summary stats
- Shows clause count by category

compare_clauses(text1, text2) → comparison results
- Identifies matching & unique clauses between two contracts
```

---

### **Module 3: pdf_processor.py (PDF Handling)**

#### **Purpose:**
Extract text from PDF files using PyPDF2

#### **Key Functions:**

```python
extract_text_from_pdf(filepath) → text
- Input: Path to PDF file
- Process: Read PDF using PyPDF2
- Output: Full text from all pages
- Error handling: Invalid PDFs, corruption checks

get_pdf_info(filepath) → {pages: count, etc}
- Returns PDF metadata (page count, creation date)
- Used for validation before processing
```

#### **Security Considerations:**
- Filename sanitization using `secure_filename()`
- File extension validation (only .pdf allowed)
- File size limits (10 MB maximum)
- Stored in isolated uploads folder

---

### **Module 4: database.py (Data Persistence)**

#### **Purpose:**
SQLite database operations for users, documents, and comparisons

#### **Database Schema:**

**Table: Users**
```sql
id (PRIMARY KEY)
username (UNIQUE, 3-50 chars)
email (UNIQUE, valid email)
password (hashed with Werkzeug PBKDF2)
role (admin, lawyer, client)
created_at (timestamp)
```

**Table: Documents**
```sql
id (PRIMARY KEY)
user_id (FOREIGN KEY)
filename (original PDF name)
upload_date
extracted_text (full PDF content)
clauses (JSON object with 5 categories)
```

**Table: Comparisons**
```sql
id (PRIMARY KEY)
user_id
doc1_id, doc2_id (comparison between two docs)
comparison_results (matching clauses)
created_at
```

#### **Key Database Functions:**

```python
# User Management
create_user(username, email, password_hash, role)
get_user_by_email(email)
get_user_by_id(id)
user_exists(username, email)

# Document Management
save_document(user_id, filename, text, clauses_json)
get_user_documents(user_id)
get_document_by_id(doc_id)
delete_document(doc_id, user_id)  # ownership verified

# Analytics
get_dashboard_stats(user_id)  # returns stats for dashboard

# Comparisons
save_comparison(user_id, doc1_id, doc2_id, results)
get_comparison(comparison_id)
get_user_comparisons(user_id)
delete_comparison(comp_id, user_id)
```

#### **Why SQLite?**
- ✅ No separate database server needed
- ✅ Portable (single file)
- ✅ Perfect for small to medium applications
- ✅ ACID compliant
- ✅ Easy to backup & deploy

---

## 🔐 **PART 3: Authentication & Security**

### **Authentication Flow:**

```
Registration:
1. User provides username, email, password, role
2. Validations performed (length, complexity, duplicates)
3. Password hashed using Werkzeug (PBKDF2 - 160,000 iterations)
4. User record saved to database
5. JWT token generated & returned

Login:
1. User provides email & password
2. User record fetched from database
3. Password verified against stored hash
4. JWT token generated with 24-hour expiry
5. Token sent to frontend

Authenticated Requests:
1. Client includes token in Authorization header: "Bearer <token>"
2. Backend verifies token signature & expiry
3. Extract user_id from token claims
4. Process request with user context
```

### **Password Security:**
```python
Requirements:
- Minimum 8 characters
- At least 1 uppercase letter (A-Z)
- At least 1 lowercase letter (a-z)
- At least 1 digit (0-9)
- At least 1 special character (!@#$%^&*)

Hashing: PBKDF2 with 160,000 iterations
Cost: Extremely difficult to crack (takes days/weeks per password)
```

### **JWT Token Structure:**
```
Header: {alg: "HS256", type: "JWT"}
Payload: {user_id: 1, exp: 1234567890, iat: 1234567800}
Signature: HMAC-SHA256(header.payload, secret_key)

Expiry: 24 hours after login
Verification: Signature checked on every request using SECRET_KEY
```

---

## 📊 **PART 4: File Upload Process**

### **Step-by-Step Upload Flow:**

```
1. Frontend sends POST /api/upload with PDF file
2. Backend validates:
   - File exists in request
   - Correct MIME type (application/pdf)
   - File size < 10 MB
   - Filename allowed (no path traversal)
3. Filename sanitized using secure_filename()
4. File saved to ./uploads/ folder
5. PyPDF2 extracts text from PDF
6. clause_extractor processes text
7. Clauses extracted & organized by category
8. Document metadata + clauses saved to database
9. Response sent with document ID & clauses
```

### **Security Measures:**
- ✅ Filename sanitization (no ../ or malicious paths)
- ✅ File extension validation (only .pdf)
- ✅ File size limits (10 MB)
- ✅ MIME type verification
- ✅ Files stored outside web root
- ✅ Ownership verification on deletion

---

## ⚡ **PART 5: Common Viva Questions & Answers**

### **Q1: What is Flask and why use it for this project?**
**A:** Flask is a lightweight Python web framework. We chose it because:
- Minimal setup for REST APIs
- Easy to learn and understand
- Good for prototyping and small-medium projects
- Excellent extensions (Flask-CORS, Flask-Session)
- RESTful API development is straightforward

### **Q2: Explain the clause extraction logic. How does it work?**
**A:** Our system uses keyword-based matching:
1. Extracts all text from PDF using PyPDF2
2. Converts text to lowercase (case-insensitive)
3. Searches for predefined keywords for each clause type
4. When keyword found, captures the entire sentence/paragraph
5. Groups results by clause category
6. Returns organized JSON with 5 categories

Example: If text contains "terminate the agreement", it's categorized under Termination.

### **Q3: How is password security implemented?**
**A:** We use industry-standard practices:
- Passwords never stored in plain text
- PBKDF2 hashing with 160,000 iterations (Werkzeug default)
- Salt automatically included by Werkzeug
- Strong password requirements enforced at registration
- Password verification uses check_password_hash()

### **Q4: How does JWT authentication work?**
**A:** JWT authentication process:
1. User logs in with email & password
2. Server verifies credentials
3. Server generates JWT token containing user_id & expiry
4. Token sent to client, stored in localStorage
5. For each API call, client includes token in Authorization header
6. Server verifies token signature using SECRET_KEY
7. If valid, request processed; if invalid, 401 Unauthorized returned
8. Tokens expire after 24 hours, requiring re-login

### **Q5: What is CORS and why is it needed?**
**A:** CORS (Cross-Origin Resource Sharing) allows requests from different domains:
- Frontend (localhost:3000) needs to call Backend API (localhost:5000)
- Without CORS, browsers block this cross-origin request
- Flask-CORS enables this by adding proper headers
- In production, we restrict to specific frontend domains only

### **Q6: Explain the database schema. Why SQLite?**
**A:** Database schema has 3 main tables:
- **Users**: Store user profiles, credentials, roles
- **Documents**: Store uploaded PDFs and extracted clauses
- **Comparisons**: Store comparison history between documents

SQLite chosen because:
- No server setup required
- Portable single-file database
- Sufficient for our use case
- Easy to backup & deploy

### **Q7: How is file upload security handled?**
**A:** Multiple security layers:
1. File extension validated (only .pdf)
2. File size limited to 10 MB
3. Filename sanitized with secure_filename()
4. Files stored outside web root (/uploads/)
5. User_id verified before deletion
6. MIME type checked

### **Q8: What happens if a user tries to delete another user's document?**
**A:** Ownership verification is enforced:
1. User provides document ID to delete
2. Backend extracts document from database
3. Document's user_id compared with logged-in user_id
4. If mismatch, return 403 Forbidden
5. If match, delete allowed

### **Q9: Explain the comparison feature. How does it work?**
**A:** Comparison process:
1. User selects two documents to compare
2. API extracts clauses from both documents
3. Compares clause content for matches
4. Returns:
   - Matching clauses (common between both)
   - Unique clauses in doc1
   - Unique clauses in doc2
5. Results stored in Comparisons table for history

### **Q10: What validation is performed on user registration?**
**A:** Registration validates:
1. **Username**: 3-50 characters, alphanumeric + underscore
2. **Email**: Valid email format, unique (not already registered)
3. **Password**: Min 8 chars, uppercase, lowercase, digit, special char
4. **Role**: Only "admin", "lawyer", "client" allowed
5. **Duplicates**: Check if username or email already exists
6. Returns appropriate error if any validation fails

### **Q11: How does the dashboard calculate statistics?**
**A:** Dashboard stats query:
```python
get_dashboard_stats(user_id):
1. Count total documents uploaded by user
2. Count total clauses extracted across all documents
3. Get recent uploads (last 5-10 documents)
4. Count documents with each clause type
5. Return summary statistics
```

### **Q12: What happens if PDF parsing fails?**
**A:** Error handling:
1. Try to extract text using PyPDF2
2. If corrupted/invalid PDF, catch exception
3. Return error response with status 400
4. File deleted from filesystem
5. No database entry created
6. User receives error message

### **Q13: How is the SECRET_KEY used?**
**A:** SECRET_KEY is used for:
1. Signing session cookies (prevents tampering)
2. Signing JWT tokens (authentication)
3. CSRF protection
4. Any cryptographic operations
5. **Important**: Never hardcode in production, use environment variables

### **Q14: Explain the file upload endpoint process**
**A:** POST /api/upload process:
1. Check if file exists in request
2. Validate file extension (.pdf only)
3. Check file size (< 10 MB)
4. Sanitize filename
5. Save to ./uploads/ with unique name
6. Extract text using PyPDF2
7. Extract clauses using keyword matching
8. Save document record + clauses to database
9. Return success response with document ID

### **Q15: What is the difference between session-based and token-based auth?**
**A:** 
| Session-Based | Token-Based (JWT) |
|---------------|-------------------|
| Server stores session data | Server doesn't store anything |
| Cookie sent with each request | Token in Authorization header |
| Easier for web browsers | Suited for APIs/mobile |
| Requires server-side storage | Stateless |
| Our implementation | Used for API clients |

---

## 🔍 **PART 6: Error Handling & Edge Cases**

### **Common Errors & Handling:**

```
400 Bad Request:
- Missing required fields
- Invalid file format
- Malformed JSON

401 Unauthorized:
- Invalid credentials
- Expired token
- Missing token

403 Forbidden:
- Trying to delete another user's document
- Insufficient permissions

409 Conflict:
- Username or email already exists

500 Internal Server Error:
- PDF parsing fails
- Database corruption
- Unexpected exceptions
```

---

## 📝 **PART 7: API Testing Examples**

### **Test 1: Register User**
```bash
POST http://localhost:5000/api/register
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "Secure@1234",
  "role": "lawyer"
}
Response: 201 Created
{
  "success": true,
  "user": { "id": 1, "username": "john_doe", "role": "lawyer" },
  "access_token": "eyJhbGciOiJIUzI1NiIs..."
}
```

### **Test 2: Login**
```bash
POST http://localhost:5000/api/login
{
  "email": "john@example.com",
  "password": "Secure@1234"
}
Response: 200 OK
{
  "success": true,
  "access_token": "eyJhbGciOiJIUzI1NiIs...",
  "user": { "id": 1, "username": "john_doe", "email": "john@example.com" }
}
```

### **Test 3: Upload Document**
```bash
POST http://localhost:5000/api/upload
Authorization: Bearer <token>
Content-Type: multipart/form-data
File: contract.pdf

Response: 201 Created
{
  "success": true,
  "document": {
    "id": 1,
    "filename": "contract.pdf",
    "upload_date": "2025-02-06 10:30:00"
  },
  "clauses": {
    "Termination": ["The agreement can be terminated..."],
    "Payment": ["Payment terms are..."],
    ...
  }
}
```

### **Test 4: Get Documents**
```bash
GET http://localhost:5000/api/documents
Authorization: Bearer <token>

Response: 200 OK
{
  "success": true,
  "documents": [
    {
      "id": 1,
      "filename": "contract1.pdf",
      "upload_date": "2025-02-06 10:30:00"
    },
    ...
  ]
}
```

---

## 🚀 **PART 8: Deployment & Production Considerations**

### **What needs to change for production:**

```
1. SECRET_KEY
   - Never hardcode
   - Use strong random key from environment variable
   - Command: python -c "import secrets; print(secrets.token_hex(32))"

2. Database
   - Consider PostgreSQL or MySQL (more robust than SQLite)
   - Regular backups
   - Connection pooling

3. File Storage
   - Use cloud storage (AWS S3, Google Cloud Storage)
   - Not local filesystem

4. CORS
   - Restrict to specific frontend domain
   - Remove wildcard origins

5. HTTPS
   - All requests must be encrypted
   - Use SSL certificates

6. Environment Variables
   - All secrets in .env file
   - Never commit to version control

7. Logging & Monitoring
   - Log all errors & suspicious activities
   - Monitor API performance

8. Rate Limiting
   - Prevent abuse
   - Limit uploads per user
```

---

## ✅ **Preparation Checklist**

- [ ] Understand Flask basics and routing
- [ ] Know how clauses are extracted (keyword matching)
- [ ] Explain authentication flow (session & JWT)
- [ ] Understand password hashing (PBKDF2)
- [ ] Know database schema (Users, Documents, Comparisons)
- [ ] Explain file upload process & security
- [ ] Know error codes & handling
- [ ] Understand CORS and why it's needed
- [ ] Know security best practices
- [ ] Be able to debug common issues

---

**Good luck bhai! 💪 Teri lab eval mein crush karle! 🚀**
