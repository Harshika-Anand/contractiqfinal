import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:5000';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true,
});

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

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('accessToken');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export const authAPI = {
  register: async (userData) => {
    const response = await api.post('/api/register', userData);
    return response.data;
  },

  login: async (credentials) => {
    const response = await api.post('/api/login', credentials);
    if (response.data.success) {
      localStorage.setItem('accessToken', response.data.access_token);
      localStorage.setItem('user', JSON.stringify(response.data.user));
    }
    return response.data;
  },

  logout: async () => {
    const response = await api.post('/api/logout');
    localStorage.removeItem('accessToken');
    localStorage.removeItem('user');
    return response.data;
  },

  getProfile: async () => {
    const response = await api.get('/api/profile');
    return response.data;
  },
};

export const documentAPI = {
  upload: async (file, onUploadProgress) => {
    const formData = new FormData();
    formData.append('file', file);
    const response = await api.post('/api/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
      onUploadProgress,
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

export const dashboardAPI = {
  getStats: async () => {
    const response = await api.get('/api/dashboard');
    return response.data;
  },
};

export const comparisonAPI = {
  /**
   * Compare clauses between two documents
   * @param {number} document1Id - ID of first document
   * @param {number} document2Id - ID of second document
   * @returns {Promise} Comparison result with matching, different, and missing clauses
   */
  compare: async (document1Id, document2Id) => {
    const response = await api.post('/api/compare', {
      document1_id: document1Id,
      document2_id: document2Id,
    });
    return response.data;
  },
};

export default api;