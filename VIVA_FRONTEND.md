# ContractIQ Frontend - Detailed Documentation

> **React + Vite Single Page Application for Smart Contract Analysis**

---

## 1. Overview

The frontend is a modern React application built with Vite that provides:
- User authentication (login/registration)
- PDF document upload
- Text paste for direct analysis
- Dashboard with analytics
- Document management

**Development URL:** `http://localhost:5173`

---

## 2. Technology Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| React | 18.x | UI Component Library |
| Vite | 5.x | Build Tool & Dev Server |
| React Router | 6.x | Client-Side Routing |
| Axios | 1.x | HTTP Client |
| Tailwind CSS | 3.x | Utility-First CSS |
| React Hot Toast | 2.x | Toast Notifications |

### Why These Technologies?

- **React** - Component-based, large ecosystem, virtual DOM for performance
- **Vite** - 10x faster than Create React App, instant HMR
- **Tailwind CSS** - Rapid styling without writing custom CSS
- **Axios** - Better than fetch, interceptors for auth handling
- **React Router** - Industry standard for React routing

---

## 3. Project Structure

```
contractiqF/
├── index.html              # HTML entry point
├── package.json            # Dependencies & scripts
├── vite.config.js          # Vite configuration
├── tailwind.config.js      # Tailwind CSS config
├── postcss.config.js       # PostCSS config (for Tailwind)
├── eslint.config.js        # ESLint rules
│
└── src/
    ├── main.jsx            # React entry point
    ├── App.jsx             # Main component with routing
    ├── App.css             # Component styles
    ├── index.css           # Global styles (Tailwind directives)
    │
    ├── context/
    │   └── AuthContext.jsx # Authentication state management
    │
    ├── services/
    │   └── api.js          # Axios API client & endpoints
    │
    ├── hooks/
    │   └── useDocuments.js # Document operations hook
    │
    ├── components/
    │   └── layout/
    │       ├── Layout.jsx  # Main layout wrapper
    │       └── Navbar.jsx  # Navigation bar component
    │
    └── pages/
        ├── Home.jsx        # Landing page
        ├── Login.jsx       # Login page
        ├── Register.jsx    # Registration page
        ├── Dashboard.jsx   # User dashboard
        ├── Documents.jsx   # Document upload & list
        └── DocumentDetails.jsx # Single document view
```

---

## 4. Application Entry Points

### index.html
```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>ContractIQ - Smart Contract Analysis</title>
  </head>
  <body>
    <div id="root"></div>
    <script type="module" src="/src/main.jsx"></script>
  </body>
</html>
```

### main.jsx
```jsx
import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App'
import './index.css'

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
)
```

### index.css (Tailwind Directives)
```css
@tailwind base;
@tailwind components;
@tailwind utilities;
```

---

## 5. Main Application (App.jsx)

### Complete Application Structure

```jsx
import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import { AuthProvider, useAuth } from './context/AuthContext';

// Import pages
import Home from './pages/Home';
import Login from './pages/Login';
import Register from './pages/Register';
import Dashboard from './pages/Dashboard';
import Documents from './pages/Documents';
import DocumentDetails from './pages/DocumentDetails';
import Layout from './components/layout/Layout';

// Protected Route Component
const ProtectedRoute = ({ children }) => {
  const { isAuthenticated, loading } = useAuth();

  // Show loading spinner while checking auth
  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
      </div>
    );
  }

  // Redirect to login if not authenticated
  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return children;
};

// Public Route Component (redirects if already logged in)
const PublicRoute = ({ children }) => {
  const { isAuthenticated, loading } = useAuth();

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
      </div>
    );
  }

  // Redirect to dashboard if already authenticated
  if (isAuthenticated) {
    return <Navigate to="/dashboard" replace />;
  }

  return children;
};

// Route Configuration
function AppRoutes() {
  return (
    <Routes>
      {/* Public Routes */}
      <Route path="/" element={<Layout><Home /></Layout>} />
      
      <Route path="/login" element={
        <PublicRoute><Login /></PublicRoute>
      } />
      
      <Route path="/register" element={
        <PublicRoute><Register /></PublicRoute>
      } />

      {/* Protected Routes */}
      <Route path="/dashboard" element={
        <ProtectedRoute><Layout><Dashboard /></Layout></ProtectedRoute>
      } />
      
      <Route path="/documents" element={
        <ProtectedRoute><Layout><Documents /></Layout></ProtectedRoute>
      } />
      
      <Route path="/documents/:id" element={
        <ProtectedRoute><Layout><DocumentDetails /></Layout></ProtectedRoute>
      } />

      {/* Catch all - redirect to home */}
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}

// Main App Component
function App() {
  return (
    <AuthProvider>
      <Router>
        <AppRoutes />
        <Toaster
          position="top-right"
          toastOptions={{
            duration: 4000,
            style: { background: '#363636', color: '#fff' },
            success: { duration: 3000 },
            error: { duration: 4000 },
          }}
        />
      </Router>
    </AuthProvider>
  );
}

export default App;
```

### Key Concepts

1. **AuthProvider** wraps entire app to provide auth context
2. **ProtectedRoute** checks authentication before rendering
3. **PublicRoute** redirects authenticated users away from login/register
4. **Toaster** provides toast notifications

---

## 6. Authentication Context (AuthContext.jsx)

### State Management for Authentication

```jsx
import React, { createContext, useContext, useState, useEffect } from 'react';
import { authAPI } from '../services/api';

// Create context
const AuthContext = createContext(null);

// Custom hook to use auth context
export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

// Auth Provider Component
export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  // Initialize authentication from localStorage
  useEffect(() => {
    const initAuth = () => {
      const storedUser = localStorage.getItem('user');
      const token = localStorage.getItem('accessToken');

      if (storedUser && token) {
        try {
          const parsedUser = JSON.parse(storedUser);
          setUser(parsedUser);
          setIsAuthenticated(true);
        } catch (error) {
          console.error('Error parsing stored user:', error);
          // Clear invalid data
          localStorage.removeItem('user');
          localStorage.removeItem('accessToken');
        }
      }
      setLoading(false);
    };

    initAuth();
  }, []);

  // Login function
  const login = async (email, password) => {
    try {
      const data = await authAPI.login({ email, password });
      if (data.success) {
        setUser(data.user);
        setIsAuthenticated(true);
        return { success: true };
      }
      return { success: false, error: data.error };
    } catch (error) {
      const errorMessage = error.response?.data?.error || 'Login failed. Please try again.';
      return { success: false, error: errorMessage };
    }
  };

  // Register function
  const register = async (userData) => {
    try {
      const data = await authAPI.register(userData);
      if (data.success) {
        // Auto-login after successful registration
        return await login(userData.email, userData.password);
      }
      return { success: false, error: data.error };
    } catch (error) {
      const errorMessage = error.response?.data?.error || 'Registration failed.';
      return { success: false, error: errorMessage };
    }
  };

  // Logout function
  const logout = async () => {
    try {
      await authAPI.logout();
    } catch (error) {
      console.error('Logout error:', error);
    } finally {
      setUser(null);
      setIsAuthenticated(false);
    }
  };

  // Update user data
  const updateUser = (userData) => {
    setUser(userData);
    localStorage.setItem('user', JSON.stringify(userData));
  };

  // Context value
  const value = {
    user,
    loading,
    isAuthenticated,
    login,
    register,
    logout,
    updateUser,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
```

### How to Use AuthContext

```jsx
// In any component
import { useAuth } from '../context/AuthContext';

function MyComponent() {
  const { user, isAuthenticated, login, logout } = useAuth();
  
  if (!isAuthenticated) {
    return <p>Please log in</p>;
  }
  
  return <p>Welcome, {user.username}!</p>;
}
```

---

## 7. API Service (api.js)

### Axios Configuration with Interceptors

```javascript
import axios from 'axios';

// Base URL for API
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:5000';

// Create Axios instance
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true,  // Send cookies with requests
});

// REQUEST INTERCEPTOR - Add JWT token to every request
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('accessToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// RESPONSE INTERCEPTOR - Handle 401 errors globally
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Token expired or invalid - log out user
      localStorage.removeItem('accessToken');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// AUTHENTICATION API
export const authAPI = {
  register: async (userData) => {
    const response = await api.post('/api/register', userData);
    return response.data;
  },

  login: async (credentials) => {
    const response = await api.post('/api/login', credentials);
    if (response.data.success) {
      // Store token and user in localStorage
      localStorage.setItem('accessToken', response.data.access_token);
      localStorage.setItem('user', JSON.stringify(response.data.user));
    }
    return response.data;
  },

  logout: async () => {
    const response = await api.post('/api/logout');
    // Clear localStorage
    localStorage.removeItem('accessToken');
    localStorage.removeItem('user');
    return response.data;
  },

  getProfile: async () => {
    const response = await api.get('/api/profile');
    return response.data;
  },
};

// DOCUMENT API
export const documentAPI = {
  upload: async (file, onUploadProgress) => {
    const formData = new FormData();
    formData.append('file', file);
    
    const response = await api.post('/api/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
      onUploadProgress,  // Progress callback
    });
    return response.data;
  },

  getAll: async () => {
    const response = await api.get('/api/documents');
    return response.data;
  },

  getById: async (documentId) => {
    const response = await api.get(`/api/document/${documentId}`);
    return response.data;
  },

  delete: async (documentId) => {
    const response = await api.delete(`/api/document/${documentId}`);
    return response.data;
  },
};

// DASHBOARD API
export const dashboardAPI = {
  getStats: async () => {
    const response = await api.get('/api/dashboard');
    return response.data;
  },
};

export default api;
```

---

## 8. Pages

### 8.1 Home Page (Home.jsx)

Landing page with hero section and feature highlights.

```jsx
import React from 'react';
import { Link } from 'react-router-dom';

const Home = () => {
  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-50 via-blue-50 to-indigo-50">
      <div className="container mx-auto px-4 py-20">
        {/* Hero Section */}
        <div className="text-center max-w-4xl mx-auto">
          <h1 className="text-6xl font-bold text-gray-900 mb-6">
            Smart Contract Analysis
            <span className="block text-indigo-600 mt-2">Made Simple</span>
          </h1>
          
          <p className="text-xl text-gray-600 mb-10">
            Upload PDF contracts and automatically identify key legal clauses. 
            Save time, reduce risk, and understand your agreements better.
          </p>

          {/* CTA Buttons */}
          <div className="flex gap-4 justify-center">
            <Link to="/register" className="px-8 py-4 bg-indigo-600 text-white rounded-lg font-semibold hover:bg-indigo-700">
              Get Started Free
            </Link>
            <Link to="/login" className="px-8 py-4 bg-white text-indigo-600 rounded-lg font-semibold border-2 border-indigo-600">
              Sign In
            </Link>
          </div>
        </div>

        {/* Feature Cards */}
        <div className="mt-24 grid md:grid-cols-3 gap-8">
          <FeatureCard 
            title="Smart PDF Analysis"
            description="Upload contracts and automatically extract key clauses."
            icon={DocumentIcon}
          />
          <FeatureCard 
            title="Clause Detection"
            description="Identify termination, liability, payment clauses instantly."
            icon={ShieldIcon}
          />
          <FeatureCard 
            title="Dashboard Analytics"
            description="Track documents, view statistics, manage library."
            icon={ChartIcon}
          />
        </div>
      </div>
    </div>
  );
};
```

### 8.2 Login Page (Login.jsx)

```jsx
import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import toast from 'react-hot-toast';

const Login = () => {
  const navigate = useNavigate();
  const { login } = useAuth();
  
  const [formData, setFormData] = useState({
    email: '',
    password: '',
  });
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!formData.email || !formData.password) {
      toast.error('Please fill in all fields');
      return;
    }

    setLoading(true);

    try {
      const result = await login(formData.email, formData.password);
      
      if (result.success) {
        toast.success('Login successful!');
        navigate('/dashboard');
      } else {
        toast.error(result.error || 'Login failed');
      }
    } catch (error) {
      toast.error('An error occurred. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-indigo-50 via-white to-blue-50 flex items-center justify-center px-4">
      <div className="max-w-md w-full">
        <div className="text-center mb-8">
          <h1 className="text-4xl font-bold text-indigo-600 mb-2">ContractIQ</h1>
          <p className="text-gray-600">Sign in to your account</p>
        </div>

        <div className="bg-white rounded-2xl shadow-xl p-8 border border-gray-100">
          <form onSubmit={handleSubmit} className="space-y-6">
            <div>
              <label htmlFor="email" className="block text-sm font-medium text-gray-700 mb-2">
                Email Address
              </label>
              <input
                id="email"
                name="email"
                type="email"
                required
                value={formData.email}
                onChange={handleChange}
                className="block w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500"
                placeholder="you@example.com"
              />
            </div>

            <div>
              <label htmlFor="password" className="block text-sm font-medium text-gray-700 mb-2">
                Password
              </label>
              <p className="text-xs text-gray-500 mb-2">
                Must contain: 8+ chars, uppercase, lowercase, number, special char
              </p>
              <input
                id="password"
                name="password"
                type="password"
                required
                value={formData.password}
                onChange={handleChange}
                className="block w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500"
                placeholder="Enter your password"
              />
            </div>

            <button
              type="submit"
              disabled={loading}
              className="w-full bg-indigo-600 text-white py-3 rounded-lg font-semibold hover:bg-indigo-700 disabled:opacity-50"
            >
              {loading ? 'Signing in...' : 'Sign In'}
            </button>
          </form>

          <div className="mt-6 text-center">
            <p className="text-gray-600">
              Don't have an account?{' '}
              <Link to="/register" className="text-indigo-600 font-semibold hover:text-indigo-700">
                Register here
              </Link>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};
```

### 8.3 Register Page (Register.jsx) - With Password Validation

```jsx
import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import toast from 'react-hot-toast';

// Password validation function
const validatePassword = (password) => {
  if (!password) return { valid: false, errors: ['Password is required'] };
  
  const errors = [];
  
  if (password.length < 8) errors.push('At least 8 characters');
  if (!/[A-Z]/.test(password)) errors.push('One uppercase letter (A-Z)');
  if (!/[a-z]/.test(password)) errors.push('One lowercase letter (a-z)');
  if (!/\d/.test(password)) errors.push('One number (0-9)');
  if (!/[!@#$%^&()\-_=+[\]{}|;:'",.<>?/]/.test(password)) 
    errors.push('One special character (!@#$%^&*)');
  
  return { valid: errors.length === 0, errors };
};

const Register = () => {
  const navigate = useNavigate();
  const { register } = useAuth();
  
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    confirmPassword: '',
    role: 'client',
  });
  const [loading, setLoading] = useState(false);
  const [passwordErrors, setPasswordErrors] = useState([]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
    
    // Real-time password validation
    if (name === 'password') {
      const validation = validatePassword(value);
      setPasswordErrors(validation.errors);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    // Validate all fields
    if (!formData.username || !formData.email || !formData.password) {
      toast.error('Please fill in all fields');
      return;
    }

    // Validate password strength
    const passwordValidation = validatePassword(formData.password);
    if (!passwordValidation.valid) {
      toast.error(`Password requirements not met`);
      return;
    }

    // Check password match
    if (formData.password !== formData.confirmPassword) {
      toast.error('Passwords do not match');
      return;
    }

    setLoading(true);

    try {
      const result = await register({
        username: formData.username,
        email: formData.email,
        password: formData.password,
        role: formData.role,
      });
      
      if (result.success) {
        toast.success('Registration successful!');
        navigate('/dashboard');
      } else {
        toast.error(result.error || 'Registration failed');
      }
    } catch (error) {
      toast.error('An error occurred.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-indigo-50 flex items-center justify-center px-4 py-12">
      <div className="max-w-md w-full">
        {/* Header */}
        <div className="text-center mb-8">
          <h1 className="text-4xl font-bold text-indigo-600">ContractIQ</h1>
          <p className="text-gray-600">Create your account</p>
        </div>

        <div className="bg-white rounded-2xl shadow-xl p-8">
          <form onSubmit={handleSubmit} className="space-y-5">
            {/* Username */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">Username</label>
              <input
                name="username"
                type="text"
                required
                value={formData.username}
                onChange={handleChange}
                className="block w-full px-4 py-3 border border-gray-300 rounded-lg"
                placeholder="johndoe"
              />
            </div>

            {/* Email */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">Email</label>
              <input
                name="email"
                type="email"
                required
                value={formData.email}
                onChange={handleChange}
                className="block w-full px-4 py-3 border border-gray-300 rounded-lg"
                placeholder="you@example.com"
              />
            </div>

            {/* Role */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">Role</label>
              <select
                name="role"
                value={formData.role}
                onChange={handleChange}
                className="block w-full px-4 py-3 border border-gray-300 rounded-lg"
              >
                <option value="client">Client</option>
                <option value="lawyer">Lawyer</option>
                <option value="admin">Admin</option>
              </select>
            </div>

            {/* Password with validation */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">Password</label>
              <input
                name="password"
                type="password"
                required
                value={formData.password}
                onChange={handleChange}
                className={`block w-full px-4 py-3 border rounded-lg ${
                  passwordErrors.length > 0 ? 'border-red-300 bg-red-50' : 'border-gray-300'
                }`}
              />
              
              {/* Password requirements checklist */}
              {passwordErrors.length > 0 && (
                <div className="mt-3 p-3 bg-red-50 border border-red-200 rounded-lg">
                  <p className="text-xs font-medium text-red-700 mb-2">Missing:</p>
                  <ul className="space-y-1">
                    {passwordErrors.map((error, idx) => (
                      <li key={idx} className="text-xs text-red-600 flex items-center">
                        <span className="mr-2">✗</span>{error}
                      </li>
                    ))}
                  </ul>
                </div>
              )}
              
              {/* Success indicator */}
              {formData.password && passwordErrors.length === 0 && (
                <p className="mt-2 text-sm text-green-600 flex items-center">
                  <span className="mr-2">✓</span>Strong password
                </p>
              )}
            </div>

            {/* Confirm Password */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">Confirm Password</label>
              <input
                name="confirmPassword"
                type="password"
                required
                value={formData.confirmPassword}
                onChange={handleChange}
                className={`block w-full px-4 py-3 border rounded-lg ${
                  formData.confirmPassword && formData.password !== formData.confirmPassword
                    ? 'border-red-300 bg-red-50'
                    : 'border-gray-300'
                }`}
              />
              
              {/* Match indicator */}
              {formData.confirmPassword && (
                formData.password === formData.confirmPassword ? (
                  <p className="mt-2 text-sm text-green-600">✓ Passwords match</p>
                ) : (
                  <p className="mt-2 text-sm text-red-600">✗ Passwords do not match</p>
                )
              )}
            </div>

            <button
              type="submit"
              disabled={loading}
              className="w-full bg-indigo-600 text-white py-3 rounded-lg font-semibold hover:bg-indigo-700 disabled:opacity-50"
            >
              {loading ? 'Creating Account...' : 'Create Account'}
            </button>
          </form>

          <div className="mt-6 text-center">
            <Link to="/login" className="text-indigo-600 font-semibold">
              Already have an account? Sign in
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};
```

### 8.4 Dashboard Page (Dashboard.jsx)

```jsx
import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { dashboardAPI } from '../services/api';
import toast from 'react-hot-toast';

const Dashboard = () => {
  const { user } = useAuth();
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchDashboardStats();
  }, []);

  const fetchDashboardStats = async () => {
    try {
      const data = await dashboardAPI.getStats();
      if (data.success) {
        setStats(data.stats);
      }
    } catch (error) {
      toast.error('Failed to load dashboard');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="container mx-auto px-4">
        {/* Welcome Header */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900">
            Welcome back, {user?.username}!
          </h1>
          <p className="text-gray-600 mt-1">Here's your contract analysis overview</p>
        </div>

        {/* Stats Cards */}
        <div className="grid md:grid-cols-3 gap-6 mb-8">
          <StatCard 
            title="Total Documents" 
            value={stats?.total_documents || 0}
            color="indigo"
          />
          <StatCard 
            title="Recent Uploads" 
            value={stats?.recent_uploads || 0}
            color="green"
          />
          <StatCard 
            title="Clauses Extracted" 
            value={stats?.total_clauses_extracted || 0}
            color="purple"
          />
        </div>

        {/* Recent Documents */}
        <div className="bg-white rounded-xl shadow-sm p-6 border border-gray-100">
          <div className="flex items-center justify-between mb-6">
            <h2 className="text-xl font-bold text-gray-900">Recent Documents</h2>
            <Link to="/documents" className="text-indigo-600 font-medium">
              View All →
            </Link>
          </div>

          {stats?.recent_documents?.length > 0 ? (
            <div className="space-y-3">
              {stats.recent_documents.map((doc) => (
                <div key={doc.id} className="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
                  <div>
                    <p className="font-medium text-gray-900">{doc.original_filename}</p>
                    <p className="text-sm text-gray-600">
                      {new Date(doc.upload_date).toLocaleDateString()}
                    </p>
                  </div>
                  <Link to={`/documents/${doc.id}`} className="text-indigo-600 font-medium">
                    View
                  </Link>
                </div>
              ))}
            </div>
          ) : (
            <div className="text-center py-12">
              <p className="text-gray-500 mb-4">No documents yet</p>
              <Link to="/documents" className="px-6 py-3 bg-indigo-600 text-white rounded-lg font-semibold">
                Upload Your First Document
              </Link>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};
```

### 8.5 Documents Page (Documents.jsx) - With Tabs

```jsx
import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { useDocuments } from '../hooks/useDocuments';
import toast from 'react-hot-toast';

const Documents = () => {
  const { documents, loading, uploadDocument, deleteDocument } = useDocuments();
  const [uploading, setUploading] = useState(false);
  const [uploadProgress, setUploadProgress] = useState(0);
  const [activeTab, setActiveTab] = useState('upload'); // 'upload' or 'text'
  const [textInput, setTextInput] = useState('');

  // Handle PDF upload
  const handleFileUpload = async (e) => {
    const file = e.target.files[0];
    
    if (!file) return;

    // Validate file type
    if (file.type !== 'application/pdf') {
      toast.error('Please upload a PDF file');
      return;
    }

    // Validate file size (10MB max)
    if (file.size > 10 * 1024 * 1024) {
      toast.error('File size must be less than 10MB');
      return;
    }

    setUploading(true);
    setUploadProgress(0);

    const result = await uploadDocument(file, (progressEvent) => {
      const progress = Math.round((progressEvent.loaded * 100) / progressEvent.total);
      setUploadProgress(progress);
    });

    setUploading(false);
    setUploadProgress(0);

    if (result.success) {
      e.target.value = '';  // Reset file input
    }
  };

  // Handle text extraction
  const handleTextExtraction = async (e) => {
    e.preventDefault();

    if (!textInput.trim() || textInput.trim().length < 50) {
      toast.error('Text must be at least 50 characters long');
      return;
    }

    // API call to extract-text endpoint
    // ...
  };

  // Handle document deletion
  const handleDelete = async (id, filename) => {
    if (window.confirm(`Delete "${filename}"?`)) {
      await deleteDocument(id);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="container mx-auto px-4">
        <h1 className="text-3xl font-bold text-gray-900 mb-8">My Documents</h1>

        {/* Tab Navigation */}
        <div className="bg-white rounded-xl shadow-sm border border-gray-100 mb-8">
          <div className="flex border-b border-gray-200">
            <button
              onClick={() => setActiveTab('upload')}
              className={`flex-1 px-6 py-4 font-medium ${
                activeTab === 'upload'
                  ? 'border-b-2 border-indigo-600 text-indigo-600'
                  : 'text-gray-600'
              }`}
            >
              📤 Upload PDF
            </button>
            <button
              onClick={() => setActiveTab('text')}
              className={`flex-1 px-6 py-4 font-medium ${
                activeTab === 'text'
                  ? 'border-b-2 border-indigo-600 text-indigo-600'
                  : 'text-gray-600'
              }`}
            >
              📝 Paste Text
            </button>
          </div>

          {/* Tab Content */}
          <div className="p-8">
            {activeTab === 'upload' ? (
              <div>
                <h2 className="text-xl font-bold mb-4">Upload PDF Document</h2>
                <input
                  type="file"
                  accept=".pdf"
                  onChange={handleFileUpload}
                  disabled={uploading}
                  className="block w-full text-sm text-gray-500 file:mr-4 file:py-3 file:px-6 file:rounded-lg file:border-0 file:bg-indigo-50 file:text-indigo-700 hover:file:bg-indigo-100"
                />
                
                {/* Upload Progress */}
                {uploading && (
                  <div className="mt-4">
                    <div className="flex justify-between mb-1">
                      <span className="text-sm font-medium text-indigo-700">Uploading...</span>
                      <span className="text-sm font-medium text-indigo-700">{uploadProgress}%</span>
                    </div>
                    <div className="w-full bg-gray-200 rounded-full h-2">
                      <div
                        className="bg-indigo-600 h-2 rounded-full transition-all"
                        style={{ width: `${uploadProgress}%` }}
                      ></div>
                    </div>
                  </div>
                )}
              </div>
            ) : (
              <div>
                <h2 className="text-xl font-bold mb-4">Paste Contract Text</h2>
                <textarea
                  value={textInput}
                  onChange={(e) => setTextInput(e.target.value)}
                  className="w-full h-40 px-4 py-3 border border-gray-300 rounded-lg"
                  placeholder="Paste your contract text here..."
                />
                <button
                  onClick={handleTextExtraction}
                  className="mt-4 px-6 py-3 bg-indigo-600 text-white rounded-lg font-semibold"
                >
                  Extract Clauses
                </button>
              </div>
            )}
          </div>
        </div>

        {/* Document List */}
        <div className="bg-white rounded-xl shadow-sm p-6">
          <h2 className="text-xl font-bold mb-6">Uploaded Documents</h2>
          
          {documents.length > 0 ? (
            <div className="space-y-4">
              {documents.map((doc) => (
                <div key={doc.id} className="flex items-center justify-between p-4 border rounded-lg">
                  <div>
                    <p className="font-medium">{doc.original_filename}</p>
                    <p className="text-sm text-gray-500">
                      {doc.clauses_summary?.total_clauses || 0} clauses extracted
                    </p>
                  </div>
                  <div className="flex gap-3">
                    <Link to={`/documents/${doc.id}`} className="text-indigo-600 font-medium">
                      View
                    </Link>
                    <button
                      onClick={() => handleDelete(doc.id, doc.original_filename)}
                      className="text-red-600 font-medium"
                    >
                      Delete
                    </button>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <p className="text-center text-gray-500 py-8">No documents uploaded yet</p>
          )}
        </div>
      </div>
    </div>
  );
};
```

---

## 9. Tailwind CSS Configuration

### tailwind.config.js

```javascript
/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",  // Scan all src files
  ],
  theme: {
    extend: {},
  },
  plugins: [],
}
```

### Common Tailwind Classes Used

| Class | Purpose |
|-------|---------|
| `min-h-screen` | Minimum height = viewport height |
| `bg-gradient-to-br` | Gradient background bottom-right |
| `flex items-center justify-center` | Center content |
| `rounded-xl` | Large border radius |
| `shadow-xl` | Large shadow |
| `hover:bg-indigo-700` | Hover state |
| `disabled:opacity-50` | Disabled state |
| `focus:ring-2` | Focus ring |
| `transition-all` | Smooth transitions |

---

## 10. Running the Frontend

### Prerequisites
- Node.js 18 or higher
- npm (comes with Node.js)

### Installation

```bash
# Navigate to frontend folder
cd contractiqF

# Install dependencies
npm install

# Start development server
npm run dev
```

### Expected Output
```
  VITE v5.x.x  ready in 300 ms

  ➜  Local:   http://localhost:5173/
  ➜  Network: http://192.168.x.x:5173/
  ➜  press h + enter to show help
```

### Build for Production

```bash
npm run build
```

Output goes to `dist/` folder.

---

## 11. Viva Questions - Frontend

**Q: Why React over Vue or Angular?**
> React has the largest ecosystem, more job opportunities, and is component-based with a virtual DOM for better performance.

**Q: What is Virtual DOM?**
> React creates a lightweight copy of the real DOM in memory. When state changes, it compares (diffs) the virtual DOM with the real DOM and only updates what changed.

**Q: Why Vite over Create React App?**
> Vite uses ES modules for instant HMR (Hot Module Replacement). CRA uses Webpack which is slower. Vite cold starts in <1 second.

**Q: What is Context API?**
> React's built-in state management. It allows sharing state across components without prop drilling.

**Q: How do protected routes work?**
> The `ProtectedRoute` component checks `isAuthenticated` from AuthContext. If false, it redirects to `/login` using React Router's `Navigate`.

**Q: Why Axios over Fetch?**
> Axios has automatic JSON parsing, request/response interceptors for adding auth headers, better error handling, and request cancellation.

**Q: What are interceptors?**
> Functions that run before every request (add auth token) or after every response (handle 401 errors globally).

**Q: Why Tailwind over Bootstrap?**
> Tailwind is utility-first, more customizable, smaller bundle size (purges unused CSS), and better for custom designs.

---

*Frontend documentation complete for viva preparation.*
