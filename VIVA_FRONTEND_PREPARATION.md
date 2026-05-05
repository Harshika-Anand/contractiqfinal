# 🎨 ContractIQ Frontend - Viva Preparation Document

## Complete React + Vite Architecture & Interview Guide

---

## 📌 **PART 1: Frontend Overview**

### **What is ContractIQ Frontend?**
A modern **React + Vite** single-page application (SPA) that provides an intuitive interface for users to upload contracts, extract clauses, analyze documents, and compare contract terms. It communicates with the Flask backend API.

### **Key Technologies:**
| Technology | Purpose | Version |
|-----------|---------|---------|
| **React** | UI framework | 18.x |
| **Vite** | Build tool & dev server | Latest |
| **React Router** | Client-side routing | 6.x |
| **Axios** | HTTP client for API calls | Latest |
| **Tailwind CSS** | Utility-first CSS framework | Latest |
| **Context API** | State management | Built-in |
| **React Hot Toast** | Notifications | Latest |

---

## 🗂️ **PART 2: Project Structure**

### **Directory Layout:**
```
contractiqF/
├── src/
│   ├── App.jsx                    # Main app component with routes
│   ├── main.jsx                   # Entry point
│   ├── index.css                  # Global styles
│   ├── App.css                    # App-specific styles
│   │
│   ├── pages/                     # Page components (full pages)
│   │   ├── Home.jsx              # Landing page
│   │   ├── Login.jsx             # Login page
│   │   ├── Register.jsx          # Registration page
│   │   ├── Dashboard.jsx         # User dashboard
│   │   ├── Documents.jsx         # Documents list & upload
│   │   ├── DocumentDetails.jsx   # Single document view
│   │   └── DocumentComparison.jsx# Compare two documents
│   │
│   ├── components/                # Reusable components
│   │   └── layout/
│   │       ├── Layout.jsx        # Main layout wrapper
│   │       └── Navbar.jsx        # Navigation bar
│   │
│   ├── context/                   # State management
│   │   └── AuthContext.jsx       # Authentication state & logic
│   │
│   ├── hooks/                     # Custom React hooks
│   │   └── useDocuments.js       # Hook for document operations
│   │
│   ├── services/                  # API & external services
│   │   └── api.js                # Axios instance & API calls
│   │
│   └── assets/                    # Static files
│       └── images, icons, etc.
│
├── public/                        # Static files (served as-is)
├── package.json                   # Dependencies & scripts
├── vite.config.js                # Vite configuration
├── tailwind.config.js            # Tailwind CSS config
├── postcss.config.js             # PostCSS configuration
└── eslint.config.js              # ESLint rules
```

---

## 💻 **PART 3: Core Components Explained**

### **Component 1: App.jsx (Main Router)**

**Purpose:** Main routing configuration and authentication guards

**Key Code Structure:**
```jsx
import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';

// Protected Route Component
const ProtectedRoute = ({ children }) => {
  const { isAuthenticated, loading } = useAuth();
  
  if (loading) return <LoadingSpinner />;
  if (!isAuthenticated) return <Navigate to="/login" />;
  
  return children;
};

// Public Route Component (redirect if logged in)
const PublicRoute = ({ children }) => {
  const { isAuthenticated, loading } = useAuth();
  
  if (loading) return <LoadingSpinner />;
  if (isAuthenticated) return <Navigate to="/dashboard" />;
  
  return children;
};

function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      
      <Route path="/login" element={<PublicRoute><Login /></PublicRoute>} />
      <Route path="/register" element={<PublicRoute><Register /></PublicRoute>} />
      
      <Route path="/dashboard" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
      <Route path="/documents" element={<ProtectedRoute><Documents /></ProtectedRoute>} />
      <Route path="/documents/:id" element={<ProtectedRoute><DocumentDetails /></ProtectedRoute>} />
      <Route path="/compare" element={<ProtectedRoute><DocumentComparison /></ProtectedRoute>} />
    </Routes>
  );
}
```

**Key Features:**
- ✅ Route protection (public vs protected)
- ✅ Loading state handling
- ✅ Automatic redirect (auth to dashboard, logged-in to home)
- ✅ Parameterized routes for document details

---

### **Component 2: AuthContext.jsx (Authentication State)**

**Purpose:** Global authentication state management using Context API

**What it manages:**
```jsx
const AuthContext = createContext();

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(localStorage.getItem('token'));
  const [loading, setLoading] = useState(true);
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  // Initialize auth state on app load
  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      // Verify token with backend
      verifyToken(token);
    }
    setLoading(false);
  }, []);

  const register = async (username, email, password, role) => {
    // Call backend /api/register
    // Store token & user data
    // Return success/error
  };

  const login = async (email, password) => {
    // Call backend /api/login
    // Store token in localStorage
    // Set user state
    // Return success/error
  };

  const logout = () => {
    // Clear token
    // Clear user
    // Call backend /api/logout
  };

  return (
    <AuthContext.Provider value={{
      user,
      token,
      isAuthenticated,
      loading,
      register,
      login,
      logout
    }}>
      {children}
    </AuthContext.Provider>
  );
}
```

**Why Context API?**
- ✅ No external dependencies (built-in React)
- ✅ Sufficient for small-medium apps
- ✅ Easy to understand and maintain
- ✅ Avoid prop drilling (passing props through many levels)

---

### **Component 3: Layout.jsx & Navbar.jsx**

**Layout.jsx Purpose:** Main page wrapper with navigation

```jsx
function Layout({ children }) {
  return (
    <div className="flex flex-col min-h-screen">
      <Navbar />
      <main className="flex-1">
        {children}
      </main>
      <Footer /> {/* Optional */}
    </div>
  );
}
```

**Navbar.jsx Purpose:** Navigation bar with links & user menu

```jsx
function Navbar() {
  const { user, isAuthenticated, logout } = useAuth();
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  return (
    <nav className="bg-white shadow">
      <div className="container mx-auto px-4 py-4">
        <div className="flex justify-between items-center">
          
          {/* Logo */}
          <Link to="/" className="text-2xl font-bold text-indigo-600">
            ContractIQ
          </Link>

          {/* Navigation Links */}
          <div className="hidden md:flex space-x-6">
            {isAuthenticated ? (
              <>
                <Link to="/dashboard">Dashboard</Link>
                <Link to="/documents">Documents</Link>
                <button onClick={logout}>Logout</button>
              </>
            ) : (
              <>
                <Link to="/login">Login</Link>
                <Link to="/register">Register</Link>
              </>
            )}
          </div>

          {/* Mobile Menu Toggle */}
          <button onClick={() => setIsMenuOpen(!isMenuOpen)} className="md:hidden">
            ☰
          </button>
        </div>

        {/* Mobile Menu */}
        {isMenuOpen && (
          <div className="md:hidden mt-4 space-y-2">
            {/* Mobile navigation items */}
          </div>
        )}
      </div>
    </nav>
  );
}
```

---

### **Component 4: Pages**

#### **Home.jsx (Landing Page)**
```jsx
function Home() {
  return (
    <div className="min-h-screen bg-gradient-to-b from-indigo-50 to-white">
      {/* Hero Section */}
      <section className="container mx-auto px-4 py-20 text-center">
        <h1 className="text-4xl font-bold mb-4">
          Smart Contract Analysis at Your Fingertips
        </h1>
        <p className="text-xl text-gray-600 mb-8">
          Upload contracts and automatically extract key clauses
        </p>
        <Link to="/register" className="bg-indigo-600 text-white px-8 py-3 rounded-lg">
          Get Started
        </Link>
      </section>

      {/* Features Section */}
      <section className="container mx-auto px-4 py-16">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
          <FeatureCard title="Upload PDFs" description="..." />
          <FeatureCard title="Extract Clauses" description="..." />
          <FeatureCard title="Compare Documents" description="..." />
        </div>
      </section>
    </div>
  );
}
```

#### **Login.jsx (Authentication)**
```jsx
function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    
    try {
      const response = await login(email, password);
      if (response.success) {
        toast.success('Login successful!');
        navigate('/dashboard');
      } else {
        setError(response.error);
      }
    } catch (err) {
      setError('Login failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50">
      <form onSubmit={handleSubmit} className="bg-white p-8 rounded-lg shadow w-full max-w-md">
        <h2 className="text-2xl font-bold mb-6">Login</h2>
        
        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          className="w-full px-4 py-2 border rounded-lg mb-4"
          required
        />
        
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          className="w-full px-4 py-2 border rounded-lg mb-4"
          required
        />
        
        {error && <div className="text-red-500 mb-4">{error}</div>}
        
        <button type="submit" disabled={loading} className="w-full bg-indigo-600 text-white py-2 rounded-lg">
          {loading ? 'Logging in...' : 'Login'}
        </button>
        
        <p className="mt-4 text-center">
          Don't have account? <Link to="/register" className="text-indigo-600">Register</Link>
        </p>
      </form>
    </div>
  );
}
```

#### **Register.jsx (User Registration)**
```jsx
function Register() {
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    confirmPassword: '',
    role: 'client'
  });
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);
  const { register } = useAuth();
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    // Client-side validation
    if (formData.password !== formData.confirmPassword) {
      setErrors({ confirmPassword: 'Passwords do not match' });
      return;
    }

    setLoading(true);
    try {
      const response = await register(
        formData.username,
        formData.email,
        formData.password,
        formData.role
      );
      
      if (response.success) {
        toast.success('Registration successful!');
        navigate('/dashboard');
      } else {
        setErrors({ submit: response.error });
      }
    } catch (err) {
      setErrors({ submit: 'Registration failed' });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50">
      <form onSubmit={handleSubmit} className="bg-white p-8 rounded-lg shadow w-full max-w-md">
        <h2 className="text-2xl font-bold mb-6">Register</h2>
        
        {/* Form fields for username, email, password, role */}
        
        <button type="submit" disabled={loading} className="w-full bg-indigo-600 text-white py-2 rounded-lg">
          {loading ? 'Registering...' : 'Register'}
        </button>
      </form>
    </div>
  );
}
```

#### **Dashboard.jsx (User Dashboard)**
```jsx
function Dashboard() {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const { user, token } = useAuth();

  useEffect(() => {
    fetchDashboardStats();
  }, []);

  const fetchDashboardStats = async () => {
    try {
      const response = await api.get('/api/dashboard', {
        headers: { Authorization: `Bearer ${token}` }
      });
      setStats(response.data.stats);
    } catch (error) {
      toast.error('Failed to load dashboard');
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <LoadingSpinner />;

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-8">Welcome, {user?.username}!</h1>
      
      {/* Statistics Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        <StatCard title="Total Documents" value={stats?.total_documents} icon="📄" />
        <StatCard title="Total Clauses" value={stats?.total_clauses_extracted} icon="📋" />
        <StatCard title="Recent Uploads" value={stats?.recent_uploads} icon="⬆️" />
      </div>
      
      {/* Recent Documents List */}
      <section>
        <h2 className="text-2xl font-bold mb-4">Recent Documents</h2>
        {/* List recent documents */}
      </section>
    </div>
  );
}
```

#### **Documents.jsx (Upload & List)**
```jsx
function Documents() {
  const [documents, setDocuments] = useState([]);
  const [file, setFile] = useState(null);
  const [uploading, setUploading] = useState(false);
  const [loading, setLoading] = useState(true);
  const { token } = useAuth();

  useEffect(() => {
    fetchDocuments();
  }, []);

  const fetchDocuments = async () => {
    try {
      const response = await api.get('/api/documents', {
        headers: { Authorization: `Bearer ${token}` }
      });
      setDocuments(response.data.documents);
    } catch (error) {
      toast.error('Failed to load documents');
    } finally {
      setLoading(false);
    }
  };

  const handleUpload = async (e) => {
    e.preventDefault();
    if (!file) return;

    setUploading(true);
    const formData = new FormData();
    formData.append('file', file);

    try {
      const response = await api.post('/api/upload', formData, {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'multipart/form-data'
        }
      });

      if (response.status === 201) {
        toast.success('Document uploaded successfully!');
        setFile(null);
        fetchDocuments(); // Refresh list
      }
    } catch (error) {
      toast.error(error.response?.data?.message || 'Upload failed');
    } finally {
      setUploading(false);
    }
  };

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-8">My Documents</h1>

      {/* Upload Section */}
      <section className="bg-indigo-50 p-8 rounded-lg mb-8">
        <h2 className="text-2xl font-bold mb-4">Upload a Contract</h2>
        <form onSubmit={handleUpload} className="flex gap-4">
          <input
            type="file"
            accept=".pdf"
            onChange={(e) => setFile(e.target.files[0])}
            required
            className="flex-1"
          />
          <button type="submit" disabled={uploading} className="bg-indigo-600 text-white px-6 py-2 rounded-lg">
            {uploading ? 'Uploading...' : 'Upload'}
          </button>
        </form>
      </section>

      {/* Documents List */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {documents.map(doc => (
          <DocumentCard key={doc.id} document={doc} />
        ))}
      </div>
    </div>
  );
}
```

#### **DocumentDetails.jsx (Single Document View)**
```jsx
function DocumentDetails() {
  const { id } = useParams();
  const [document, setDocument] = useState(null);
  const [clauses, setClauses] = useState(null);
  const [loading, setLoading] = useState(true);
  const { token } = useAuth();

  useEffect(() => {
    fetchDocumentDetails();
  }, [id]);

  const fetchDocumentDetails = async () => {
    try {
      const response = await api.get(`/api/documents/${id}`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      setDocument(response.data.document);
      setClauses(response.data.clauses);
    } catch (error) {
      toast.error('Failed to load document');
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <LoadingSpinner />;

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-4">{document?.filename}</h1>
      <p className="text-gray-600 mb-8">Uploaded: {document?.upload_date}</p>

      {/* Display Clauses by Category */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {Object.entries(clauses || {}).map(([category, items]) => (
          <ClauseCard key={category} title={category} clauses={items} />
        ))}
      </div>
    </div>
  );
}
```

#### **DocumentComparison.jsx (Compare Clauses)**
```jsx
function DocumentComparison() {
  const [doc1, setDoc1] = useState(null);
  const [doc2, setDoc2] = useState(null);
  const [documents, setDocuments] = useState([]);
  const [comparisonResults, setComparisonResults] = useState(null);
  const [loading, setLoading] = useState(false);
  const { token } = useAuth();

  const handleCompare = async () => {
    if (!doc1 || !doc2) {
      toast.error('Select two documents to compare');
      return;
    }

    setLoading(true);
    try {
      const response = await api.post('/api/compare', 
        { doc1_id: doc1, doc2_id: doc2 },
        { headers: { Authorization: `Bearer ${token}` } }
      );
      setComparisonResults(response.data.comparison);
    } catch (error) {
      toast.error('Comparison failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-8">Compare Documents</h1>

      {/* Document Selection */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
        <div>
          <label>Document 1:</label>
          <select value={doc1} onChange={(e) => setDoc1(e.target.value)}>
            <option value="">Select document...</option>
            {documents.map(doc => (
              <option key={doc.id} value={doc.id}>{doc.filename}</option>
            ))}
          </select>
        </div>
        <div>
          <label>Document 2:</label>
          <select value={doc2} onChange={(e) => setDoc2(e.target.value)}>
            <option value="">Select document...</option>
            {documents.map(doc => (
              <option key={doc.id} value={doc.id}>{doc.filename}</option>
            ))}
          </select>
        </div>
      </div>

      <button onClick={handleCompare} disabled={loading} className="bg-indigo-600 text-white px-8 py-3 rounded-lg">
        {loading ? 'Comparing...' : 'Compare'}
      </button>

      {/* Comparison Results */}
      {comparisonResults && (
        <div className="mt-8">
          {/* Display comparison results */}
        </div>
      )}
    </div>
  );
}
```

---

## 📡 **PART 4: API Service Layer**

### **services/api.js**

**Purpose:** Centralized API configuration and endpoints

```javascript
import axios from 'axios';

// Create Axios instance with default config
const api = axios.create({
  baseURL: process.env.REACT_APP_API_URL || 'http://localhost:5000',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
});

// Add token to every request
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Handle response errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Token expired, redirect to login
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default api;
```

**Benefits:**
- ✅ Centralized configuration
- ✅ Automatic token injection
- ✅ Global error handling
- ✅ Request/response interceptors

---

## 🎨 **PART 5: Styling with Tailwind CSS**

### **Why Tailwind?**
- ✅ Utility-first approach (no custom CSS usually needed)
- ✅ Responsive design (md:, lg: prefixes)
- ✅ Consistent spacing & colors
- ✅ Small bundle size

### **Common Tailwind Classes:**
```css
/* Layout */
flex, grid, container, mx-auto

/* Spacing */
p-4 (padding), m-4 (margin), gap-4 (grid gap)

/* Colors */
bg-indigo-600, text-gray-600, border-red-500

/* Responsive */
md:flex (medium screens), lg:grid (large screens)

/* Effects */
rounded-lg, shadow, opacity-50
```

---

## ⚡ **PART 6: Custom Hooks**

### **useDocuments.js**
```javascript
import { useState, useEffect } from 'react';
import api from '../services/api';

export function useDocuments() {
  const [documents, setDocuments] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchDocuments = async (token) => {
    setLoading(true);
    try {
      const response = await api.get('/api/documents', {
        headers: { Authorization: `Bearer ${token}` }
      });
      setDocuments(response.data.documents);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const uploadDocument = async (file, token) => {
    const formData = new FormData();
    formData.append('file', file);
    
    try {
      const response = await api.post('/api/upload', formData, {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'multipart/form-data'
        }
      });
      return response.data;
    } catch (error) {
      throw error;
    }
  };

  const deleteDocument = async (docId, token) => {
    try {
      await api.delete(`/api/documents/${docId}`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      setDocuments(prev => prev.filter(doc => doc.id !== docId));
    } catch (error) {
      throw error;
    }
  };

  return { documents, loading, error, fetchDocuments, uploadDocument, deleteDocument };
}
```

---

## 🔄 **PART 7: State Management Flow**

### **Authentication Flow:**
```
User Action (Login) 
    ↓
Form Submission in Login.jsx
    ↓
Call login() from AuthContext
    ↓
API Call to /api/login
    ↓
Backend validates credentials
    ↓
Returns token + user data
    ↓
AuthContext stores token in localStorage
    ↓
Sets isAuthenticated = true
    ↓
App redirects to /dashboard
```

### **Document Upload Flow:**
```
User selects PDF file
    ↓
Click Upload button
    ↓
File sent to /api/upload
    ↓
Backend processes PDF
    ↓
Extracts text & clauses
    ↓
Saves to database
    ↓
Returns document data
    ↓
Frontend displays clauses
    ↓
Document added to list
```

---

## 🚀 **PART 8: Build & Deployment**

### **Development Server:**
```bash
# Start Vite dev server
npm run dev

# Runs on http://localhost:5173 (or next available port)
# HMR enabled (changes reload instantly)
```

### **Production Build:**
```bash
# Create optimized production build
npm run build

# Generates optimized files in 'dist' folder
# Ready to deploy to any static hosting
```

### **Build Optimization:**
- ✅ Code splitting (automatic by Vite)
- ✅ Tree shaking (unused code removed)
- ✅ Minification & compression
- ✅ Fast refresh during development

---

## ⚡ **PART 9: Common Viva Questions & Answers**

### **Q1: What is React and why use it?**
**A:** React is a JavaScript library for building UI. We use it because:
- Component-based architecture (reusable pieces)
- Efficient rendering with Virtual DOM
- Unidirectional data flow (easy to debug)
- Large ecosystem & community support
- State management capabilities

### **Q2: Explain Vite vs traditional bundlers like Webpack**
**A:** Vite advantages:
- **Instant server start** - No bundling during dev
- **Faster HMR** - Hot Module Replacement is instant
- **Smaller config** - Almost zero configuration
- **Better dev experience** - Instant feedback
- **Native ES modules** - Uses browser's native support
- **Faster builds** - Optimized production builds

### **Q3: What is the Context API and why use it?**
**A:** Context API allows sharing state without prop drilling:
- Create context object
- Wrap components with Provider
- Use useContext hook to access state
- Advantages: Built-in, simple, no extra dependencies
- Limitations: Not for very complex state (use Redux then)

### **Q4: Explain React Router and routing in this app**
**A:** React Router handles client-side routing:
- `<BrowserRouter>` - Enables routing
- `<Routes>` - Define all routes
- `<Route>` - Individual route with path & component
- `<Navigate>` - Programmatic redirect
- `useParams()` - Get route parameters
- `useNavigate()` - Navigate programmatically

Example routes:
- `/` - Home page
- `/login` - Login (public)
- `/dashboard` - Dashboard (protected)
- `/documents/:id` - Specific document

### **Q5: How is authentication state managed?**
**A:** Using Context API:
1. AuthContext created with user, token, isAuthenticated
2. Login/Logout functions update state
3. Token stored in localStorage for persistence
4. On app load, verify token with backend
5. ProtectedRoute component checks isAuthenticated
6. Redirects to login if not authenticated

### **Q6: Explain the protected route component**
**A:** ProtectedRoute guards pages requiring authentication:
```jsx
const ProtectedRoute = ({ children }) => {
  const { isAuthenticated, loading } = useAuth();
  
  if (loading) return <LoadingSpinner />;
  if (!isAuthenticated) return <Navigate to="/login" />;
  
  return children;
};
```
- Checks authentication status
- Shows loading spinner while verifying
- Redirects to login if not authenticated
- Otherwise renders the page

### **Q7: How does file upload work?**
**A:** File upload process:
1. User selects PDF file from input
2. Store file in state: `setFile(e.target.files[0])`
3. On submit, create FormData object
4. Append file to FormData
5. Send POST to `/api/upload`
6. Include Content-Type: multipart/form-data
7. Backend processes file
8. Response contains extracted clauses
9. Frontend displays results

### **Q8: Explain Axios interceptors**
**A:** Interceptors modify requests/responses automatically:
```javascript
// Request interceptor - adds token to every request
api.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Response interceptor - handles auth errors
api.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      // Token expired, redirect to login
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);
```

### **Q9: What is useState and how does it work?**
**A:** useState hook manages component state:
```jsx
const [value, setValue] = useState(initialValue);
```
- `value` - Current state
- `setValue` - Function to update state
- `initialValue` - Starting value
- When setValue called, component re-renders with new state
- Used for form inputs, loading states, lists, etc.

### **Q10: Explain useEffect and when to use it**
**A:** useEffect runs side effects after render:
```jsx
useEffect(() => {
  // Code here runs after component mounts
  // Can fetch data, setup subscriptions, etc.
  
  return () => {
    // Cleanup code (optional)
  };
}, [dependencies]); // Re-run if dependencies change
```
- No dependency array - runs after every render
- Empty [] - runs once on mount
- [variable] - runs when variable changes
- Common uses: fetch data, set up listeners, cleanup

### **Q11: What is localStorage and why use it?**
**A:** localStorage stores data persistently on browser:
```javascript
// Store
localStorage.setItem('token', token);

// Retrieve
const token = localStorage.getItem('token');

// Remove
localStorage.removeItem('token');
```
- Data persists even after browser closes
- Perfect for storing JWT tokens
- About 5-10 MB storage limit
- Same origin policy (different sites can't access)

### **Q12: Explain the upload component**
**A:** Upload component:
1. Shows file input for selecting PDF
2. Shows upload button
3. On click, validates file selected
4. Creates FormData with file
5. Sends to backend
6. Shows loading state during upload
7. On success, displays extracted clauses
8. On error, shows error message

### **Q13: How does the comparison feature work?**
**A:** Comparison process:
1. User selects two documents
2. Sends doc1_id & doc2_id to `/api/compare`
3. Backend extracts clauses from both
4. Compares clause content
5. Returns:
   - Matching clauses (in both documents)
   - Unique clauses (only in doc1)
   - Unique clauses (only in doc2)
6. Frontend displays comparison results

### **Q14: What is the difference between component and page?**
**A:**
| Component | Page |
|-----------|------|
| Reusable pieces (Navbar, Card) | Full page views |
| Used multiple times | Used once as route |
| Smaller, focused | Larger, complete |
| Props-driven | State-driven |

### **Q15: Explain CSS-in-JS vs Tailwind**
**A:**
| CSS-in-JS | Tailwind |
|-----------|----------|
| Write CSS in JS | Use utility classes |
| Examples: Styled-components, Emotion | Class names: p-4, bg-blue-600 |
| Scoped to component | Global utilities |
| More CSS power | Faster development |
| We use | **We use** ✅ |

### **Q16: How is responsive design handled?**
**A:** Using Tailwind breakpoints:
```jsx
<div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3">
  {/* 1 column mobile, 2 medium, 3 large */}
</div>
```
- `sm:` → 640px
- `md:` → 768px
- `lg:` → 1024px
- `xl:` → 1280px
- Mobile-first approach (default is mobile)

---

## 📝 **PART 10: Common Issues & Solutions**

### **Issue 1: CORS Error**
```
Error: Access to XMLHttpRequest from 'http://localhost:3000' 
has been blocked by CORS policy
```
**Solution:**
- Backend must have Flask-CORS configured
- Check that frontend URL is in CORS allowed origins
- Backend configuration: `CORS(app, origins=["http://localhost:3000"])`

### **Issue 2: Token not persisting**
```
User logs in but after refresh, logged out
```
**Solution:**
- On app load, check localStorage for token
- Verify token validity with backend
- Use useEffect to restore auth state

### **Issue 3: Form validation not working**
```
Form submits even with invalid data
```
**Solution:**
- Add HTML5 validation: `required`, `type="email"`
- Add JavaScript validation before API call
- Show error messages to user

### **Issue 4: File upload fails**
```
Upload button doesn't work or returns error
```
**Solution:**
- Check file size (< 10 MB)
- Verify MIME type is PDF
- Ensure FormData created correctly
- Check Content-Type header

---

## ✅ **Preparation Checklist**

- [ ] Understand React basics (components, state, props)
- [ ] Know how routing works (React Router)
- [ ] Understand Context API for auth
- [ ] Know how to make API calls (Axios)
- [ ] Explain Tailwind CSS utility classes
- [ ] Know component structure & file organization
- [ ] Understand useState, useEffect, useContext hooks
- [ ] Know how file upload works
- [ ] Explain ProtectedRoute component
- [ ] Know localStorage usage
- [ ] Understand form handling & validation
- [ ] Know error handling & interceptors
- [ ] Explain page components & their purpose
- [ ] Know responsive design approach
- [ ] Understand the complete user flow

---

**Good luck bhai! 🚀 Frontend mein crush kar de! 💪**
