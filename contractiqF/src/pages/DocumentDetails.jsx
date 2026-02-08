import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { documentAPI } from '../services/api';
import toast from 'react-hot-toast';

const DocumentDetails = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [document, setDocument] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchDocument();
  }, [id]);

  const fetchDocument = async () => {
    try {
      const data = await documentAPI.getById(id);
      if (data.success) {
        setDocument(data.document);
      }
    } catch (error) {
      toast.error('Failed to load document');
      navigate('/documents');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async () => {
    if (window.confirm('Are you sure you want to delete this document?')) {
      try {
        const data = await documentAPI.delete(id);
        if (data.success) {
          toast.success('Document deleted');
          navigate('/documents');
        }
      } catch (error) {
        toast.error('Failed to delete document');
      }
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
      </div>
    );
  }

  if (!document) {
    return <div>Document not found</div>;
  }

  const clauseCategories = [
    { key: 'Termination', color: 'bg-red-100 text-red-800' },
    { key: 'Payment', color: 'bg-green-100 text-green-800' },
    { key: 'Confidentiality', color: 'bg-blue-100 text-blue-800' },
    { key: 'Liability', color: 'bg-yellow-100 text-yellow-800' },
    { key: 'Intellectual Property', color: 'bg-purple-100 text-purple-800' },
  ];

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="container mx-auto px-4">
        <div className="mb-6">
          <Link to="/documents" className="text-indigo-600 hover:text-indigo-700 font-medium">
            ← Back to Documents
          </Link>
        </div>

        <div className="bg-white rounded-xl shadow-sm p-8 border border-gray-100 mb-6">
          <div className="flex items-start justify-between mb-6">
            <div>
              <h1 className="text-3xl font-bold text-gray-900 mb-2">
                {document.original_filename}
              </h1>
              <p className="text-gray-600">
                Uploaded: {new Date(document.upload_date).toLocaleDateString()}
              </p>
            </div>
            <button
              onClick={handleDelete}
              className="px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700"
            >
              Delete Document
            </button>
          </div>

          <div className="mb-8">
            <h2 className="text-xl font-bold text-gray-900 mb-4">Extracted Clauses</h2>
            
            <div className="space-y-6">
              {clauseCategories.map(({ key, color }) => {
                const clauses = document.clauses?.[key] || [];
                
                if (clauses.length === 0) return null;

                return (
                  <div key={key} className="border border-gray-200 rounded-lg p-6">
                    <div className="flex items-center gap-3 mb-4">
                      <span className={`px-3 py-1 rounded-full text-sm font-medium ${color}`}>
                        {key}
                      </span>
                      <span className="text-gray-600 text-sm">
                        {clauses.length} clause{clauses.length !== 1 ? 's' : ''}
                      </span>
                    </div>
                    
                    <div className="space-y-3">
                      {clauses.map((clause, index) => (
                        <div key={index} className="bg-gray-50 p-4 rounded-lg">
                          <p className="text-gray-700 leading-relaxed">{clause}</p>
                        </div>
                      ))}
                    </div>
                  </div>
                );
              })}
            </div>
          </div>

          {document.extracted_text && (
            <div>
              <h2 className="text-xl font-bold text-gray-900 mb-4">Full Document Text</h2>
              <div className="bg-gray-50 p-6 rounded-lg max-h-96 overflow-y-auto">
                <p className="text-gray-700 whitespace-pre-wrap leading-relaxed">
                  {document.extracted_text}
                </p>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default DocumentDetails;