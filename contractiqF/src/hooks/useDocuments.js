import { useState, useEffect } from 'react';
import { documentAPI } from '../services/api';
import toast from 'react-hot-toast';

export const useDocuments = () => {
  const [documents, setDocuments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchDocuments = async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await documentAPI.getAll();
      if (data.success) {
        setDocuments(data.documents);
      }
    } catch (err) {
      const errorMessage = err.response?.data?.error || 'Failed to fetch documents';
      setError(errorMessage);
      toast.error(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchDocuments();
  }, []);

  const uploadDocument = async (file, onProgress) => {
    try {
      const data = await documentAPI.upload(file, onProgress);
      if (data.success) {
        toast.success('Document uploaded successfully!');
        await fetchDocuments();
        return { success: true, document: data.document };
      }
      return { success: false, error: data.error };
    } catch (err) {
      const errorMessage = err.response?.data?.error || 'Upload failed';
      toast.error(errorMessage);
      return { success: false, error: errorMessage };
    }
  };

  const deleteDocument = async (documentId) => {
    try {
      const data = await documentAPI.delete(documentId);
      if (data.success) {
        toast.success('Document deleted successfully!');
        setDocuments(docs => docs.filter(doc => doc.id !== documentId));
        return { success: true };
      }
      return { success: false, error: data.error };
    } catch (err) {
      const errorMessage = err.response?.data?.error || 'Delete failed';
      toast.error(errorMessage);
      return { success: false, error: errorMessage };
    }
  };

  return {
    documents,
    loading,
    error,
    fetchDocuments,
    uploadDocument,
    deleteDocument,
  };
};