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
  const [documentName, setDocumentName] = useState('');
  const [extracting, setExtracting] = useState(false);

  const handleFileUpload = async (e) => {
    const file = e.target.files[0];
    
    if (!file) return;

    if (file.type !== 'application/pdf') {
      toast.error('❌ Please upload a PDF file only');
      return;
    }

    if (file.size > 10 * 1024 * 1024) {
      toast.error('❌ File size must be less than 10MB');
      return;
    }

    // Check if document with same name already exists
    const documentExists = documents.some(
      doc => doc.original_filename === file.name
    );

    if (documentExists) {
      toast.error(
        `❌ Document "${file.name}" already exists! Please rename or choose a different file.`
      );
      e.target.value = ''; // Clear the input
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
      e.target.value = '';
    }
  };

  const handleTextExtraction = async (e) => {
    e.preventDefault();

    if (!textInput.trim()) {
      toast.error('❌ Please paste some text to analyze');
      return;
    }

    if (textInput.trim().length < 50) {
      toast.error('❌ Text must be at least 50 characters long');
      return;
    }

    setExtracting(true);

    try {
      const response = await fetch('http://localhost:5000/api/extract-text', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify({
          text: textInput,
          document_name: documentName || 'Text Submission',
        }),
      });

      const data = await response.json();

      if (data.success) {
        toast.success('✅ Clauses extracted successfully!');
        setTextInput('');
        setDocumentName('');
        // Refresh documents list
        window.location.reload();
      } else {
        toast.error(data.error || 'Failed to extract clauses');
      }
    } catch (error) {
      toast.error('❌ An error occurred. Please try again.');
      console.error('Error:', error);
    } finally {
      setExtracting(false);
    }
  };

  const handleDelete = async (id, filename) => {
    if (window.confirm(`Are you sure you want to delete "${filename}"?`)) {
      await deleteDocument(id);
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
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900">My Documents</h1>
          <p className="text-gray-600 mt-1">Upload PDFs or paste text to extract contract clauses</p>
        </div>

        {/* Tabs */}
        <div className="bg-white rounded-xl shadow-sm border border-gray-100 mb-8">
          <div className="flex border-b border-gray-200">
            <button
              onClick={() => setActiveTab('upload')}
              className={`flex-1 px-6 py-4 text-center font-medium transition-colors ${
                activeTab === 'upload'
                  ? 'border-b-2 border-indigo-600 text-indigo-600'
                  : 'text-gray-600 hover:text-gray-900'
              }`}
            >
              <svg className="w-5 h-5 inline mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12" />
              </svg>
              Upload PDF
            </button>
            <button
              onClick={() => setActiveTab('text')}
              className={`flex-1 px-6 py-4 text-center font-medium transition-colors ${
                activeTab === 'text'
                  ? 'border-b-2 border-indigo-600 text-indigo-600'
                  : 'text-gray-600 hover:text-gray-900'
              }`}
            >
              <svg className="w-5 h-5 inline mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
              </svg>
              Paste Text
            </button>
          </div>

          {/* Tab Content */}
          <div className="p-8">
            {activeTab === 'upload' ? (
              <div>
                <h2 className="text-xl font-bold text-gray-900 mb-4">Upload PDF Document</h2>
                
                <label className="block">
                  <input
                    type="file"
                    accept=".pdf"
                    onChange={handleFileUpload}
                    disabled={uploading}
                    className="block w-full text-sm text-gray-500
                      file:mr-4 file:py-3 file:px-6
                      file:rounded-lg file:border-0
                      file:text-sm file:font-semibold
                      file:bg-indigo-50 file:text-indigo-700
                      hover:file:bg-indigo-100
                      file:cursor-pointer cursor-pointer
                      disabled:opacity-50 disabled:cursor-not-allowed"
                  />
                </label>

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

                <p className="text-sm text-gray-500 mt-3">
                  PDF files up to 10MB are supported. We'll automatically extract contract clauses from your document.
                </p>
              </div>
            ) : (
              <div>
                <h2 className="text-xl font-bold text-gray-900 mb-4">Paste Contract Text</h2>
                
                <form onSubmit={handleTextExtraction} className="space-y-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      Document Name (optional)
                    </label>
                    <input
                      type="text"
                      value={documentName}
                      onChange={(e) => setDocumentName(e.target.value)}
                      placeholder="e.g., Service Agreement v2.1"
                      className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      Contract Text <span className="text-red-500">*</span>
                    </label>
                    <textarea
                      value={textInput}
                      onChange={(e) => setTextInput(e.target.value)}
                      placeholder="Paste contract text here... (minimum 50 characters)"
                      rows="10"
                      className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent font-mono text-sm"
                    />
                    <p className="text-xs text-gray-500 mt-2">
                      {textInput.length} characters • Minimum 50 characters required
                    </p>
                  </div>

                  <button
                    type="submit"
                    disabled={extracting || textInput.trim().length < 50}
                    className="w-full bg-indigo-600 text-white py-3 rounded-lg font-semibold hover:bg-indigo-700 transition-all disabled:opacity-50 disabled:cursor-not-allowed"
                  >
                    {extracting ? 'Extracting Clauses...' : 'Extract Clauses'}
                  </button>
                </form>

                <p className="text-sm text-gray-500 mt-4">
                  Paste contract text directly and we'll analyze it for clauses. No file upload needed.
                </p>
              </div>
            )}
          </div>
        </div>

        {/* Documents List */}
        <div className="mb-8">
          <h2 className="text-xl font-bold text-gray-900 mb-4">Your Documents</h2>
        </div>

        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
          {documents.length > 0 ? (
            documents.map((doc) => (
              <div key={doc.id} className="bg-white rounded-xl shadow-sm p-6 border border-gray-100">
                <div className="flex items-start justify-between mb-4">
                  <div className="bg-indigo-100 p-3 rounded-lg">
                    <svg className="w-6 h-6 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                    </svg>
                  </div>
                  <button
                    onClick={() => handleDelete(doc.id, doc.original_filename)}
                    className="text-red-600 hover:text-red-700"
                  >
                    <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                    </svg>
                  </button>
                </div>

                <h3 className="font-bold text-gray-900 mb-2 truncate">{doc.original_filename}</h3>
                <p className="text-sm text-gray-600 mb-4">
                  Uploaded: {new Date(doc.upload_date).toLocaleDateString()}
                </p>

                {doc.clauses_summary && (
                  <div className="mb-4">
                    <p className="text-sm font-medium text-gray-700 mb-2">
                      {doc.clauses_summary.total_clauses} clauses found
                    </p>
                  </div>
                )}

                <Link
                  to={`/documents/${doc.id}`}
                  className="block w-full text-center px-4 py-2 bg-indigo-600 text-white rounded-lg font-medium hover:bg-indigo-700"
                >
                  View Details
                </Link>
              </div>
            ))
          ) : (
            <div className="col-span-full text-center py-12">
              <p className="text-gray-500 mb-4">No documents yet</p>
              <p className="text-gray-400">Upload a PDF or paste text to extract contract clauses</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Documents;