import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { documentAPI, comparisonAPI } from '../services/api';
import toast from 'react-hot-toast';

const DocumentComparison = () => {
  const navigate = useNavigate();
  
  // State management
  const [documents, setDocuments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedDoc1, setSelectedDoc1] = useState(null);
  const [selectedDoc2, setSelectedDoc2] = useState(null);
  const [comparing, setComparing] = useState(false);
  const [comparison, setComparison] = useState(null);
  const [filterType, setFilterType] = useState('all'); // all, matching, different, missing

  // Fetch documents on mount
  useEffect(() => {
    fetchDocuments();
  }, []);

  const fetchDocuments = async () => {
    try {
      setLoading(true);
      const data = await documentAPI.getAll();
      if (data.success) {
        setDocuments(data.documents || []);
      }
    } catch (error) {
      toast.error('Failed to load documents');
    } finally {
      setLoading(false);
    }
  };

  const handleCompare = async () => {
    if (!selectedDoc1 || !selectedDoc2) {
      toast.error('Please select two documents to compare');
      return;
    }

    if (selectedDoc1 === selectedDoc2) {
      toast.error('❌ Cannot compare a document with itself! Please select two DIFFERENT documents');
      return;
    }

    try {
      setComparing(true);
      console.log('Starting comparison:', { selectedDoc1, selectedDoc2 });
      
      const data = await comparisonAPI.compare(selectedDoc1, selectedDoc2);
      console.log('Comparison response:', data);
      console.log('Comparison summary:', data?.comparison?.summary);
      
      if (data.success) {
        console.log('Setting comparison state:', data);
        setComparison(data);
        setFilterType('all');
        toast.success('Comparison completed!');
      } else {
        console.error('Comparison failed:', data?.error);
        toast.error(data.error || 'Comparison failed');
      }
    } catch (error) {
      console.error('Comparison exception:', error);
      toast.error('Error comparing documents');
      console.error(error);
    } finally {
      setComparing(false);
    }
  };

  // Filter clauses based on filter type
  const getFilteredComparison = () => {
    if (!comparison) return null;

    const comp = comparison.comparison;
    switch (filterType) {
      case 'matching':
        return {
          ...comp,
          different_clauses: [],
          only_in_first: {},
          only_in_second: {},
        };
      case 'different':
        return {
          ...comp,
          matching_categories: [],
          only_in_first: {},
          only_in_second: {},
        };
      case 'missing':
        return {
          ...comp,
          matching_categories: [],
          different_clauses: [],
        };
      default:
        return comp;
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
      </div>
    );
  }

  if (documents.length < 2) {
    return (
      <div className="max-w-7xl mx-auto px-4 py-12">
        <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-6">
          <h2 className="text-lg font-semibold text-yellow-800 mb-2">
            Not Enough Documents
          </h2>
          <p className="text-yellow-700 mb-4">
            You need at least 2 documents to perform a comparison. Please upload more documents first.
          </p>
          <button
            onClick={() => navigate('/documents')}
            className="bg-yellow-600 hover:bg-yellow-700 text-white font-semibold py-2 px-4 rounded"
          >
            Go to Documents
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 py-12">
      {/* Header */}
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900 mb-2">Compare Contracts</h1>
        <p className="text-gray-600">
          Select two documents to compare their clauses and identify differences
        </p>
      </div>

      {/* Selection Panel */}
      <div className="bg-white rounded-lg shadow p-6 mb-8">
        <h2 className="text-xl font-semibold text-gray-900 mb-4">Select Documents</h2>
        
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
          {/* Document 1 Selection */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Document 1
            </label>
            <select
              value={selectedDoc1 || ''}
              onChange={(e) => setSelectedDoc1(parseInt(e.target.value))}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
            >
              <option value="">-- Select a document --</option>
              {documents.map((doc) => (
                <option key={doc.id} value={doc.id}>
                  {doc.original_filename}
                </option>
              ))}
            </select>
          </div>

          {/* Document 2 Selection */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Document 2
            </label>
            <select
              value={selectedDoc2 || ''}
              onChange={(e) => setSelectedDoc2(parseInt(e.target.value))}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
            >
              <option value="">-- Select a document --</option>
              {documents.map((doc) => (
                <option key={doc.id} value={doc.id}>
                  {doc.original_filename}
                </option>
              ))}
            </select>
          </div>
        </div>

        {/* Compare Button */}
        <button
          onClick={handleCompare}
          disabled={comparing || !selectedDoc1 || !selectedDoc2 || selectedDoc1 === selectedDoc2}
          className={`w-full py-3 px-4 rounded-lg font-semibold transition ${
            comparing || !selectedDoc1 || !selectedDoc2 || selectedDoc1 === selectedDoc2
              ? 'bg-gray-300 text-gray-500 cursor-not-allowed'
              : 'bg-indigo-600 hover:bg-indigo-700 text-white'
          }`}
        >
          {comparing ? (
            <span className="flex items-center justify-center">
              <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white mr-2"></div>
              Comparing...
            </span>
          ) : selectedDoc1 === selectedDoc2 && selectedDoc1 ? (
            '⚠️ Select Different Documents'
          ) : (
            'Compare Documents'
          )}
        </button>

        {/* Warning Message */}
        {selectedDoc1 === selectedDoc2 && selectedDoc1 && (
          <div className="mt-4 p-4 bg-red-50 border border-red-200 rounded-lg">
            <p className="text-red-700 font-semibold">
              ❌ Cannot compare the same document with itself!
            </p>
            <p className="text-red-600 text-sm mt-1">
              Please select two different documents from the dropdowns.
            </p>
          </div>
        )}
      </div>

      {/* Results */}
      {comparison && (
        <div className="space-y-6">
          {/* Summary */}
          <div className="bg-gradient-to-r from-indigo-50 to-blue-50 rounded-lg p-6 border border-indigo-200">
            <h2 className="text-xl font-semibold text-gray-900 mb-4">Comparison Summary</h2>
            
            <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
              <div className="bg-white rounded p-4">
                <div className="text-sm text-gray-600 mb-1">Overall Similarity</div>
                <div className="text-2xl font-bold text-indigo-600">
                  {comparison.comparison.summary.overall_similarity}%
                </div>
              </div>
              <div className="bg-white rounded p-4">
                <div className="text-sm text-gray-600 mb-1">Matching Categories</div>
                <div className="text-2xl font-bold text-green-600">
                  {comparison.comparison.summary.matching_categories_count}
                </div>
              </div>
              <div className="bg-white rounded p-4">
                <div className="text-sm text-gray-600 mb-1">Different Clauses</div>
                <div className="text-2xl font-bold text-yellow-600">
                  {comparison.comparison.summary.different_clauses_count}
                </div>
              </div>
              <div className="bg-white rounded p-4">
                <div className="text-sm text-gray-600 mb-1">Risk Flags</div>
                <div className="text-2xl font-bold text-red-600">
                  {comparison.comparison.summary.risk_flags_count}
                </div>
              </div>
            </div>
          </div>

          {/* Risk Flags */}
          {comparison.comparison.risk_flags.length > 0 && (
            <div className="bg-red-50 border border-red-200 rounded-lg p-6">
              <h2 className="text-lg font-semibold text-red-900 mb-4">⚠️ Risk Flags</h2>
              <div className="space-y-3">
                {comparison.comparison.risk_flags.map((flag, idx) => (
                  <div
                    key={idx}
                    className={`p-4 rounded border-l-4 ${
                      flag.severity === 'HIGH'
                        ? 'bg-red-100 border-l-red-600'
                        : 'bg-yellow-100 border-l-yellow-600'
                    }`}
                  >
                    <div className="flex items-start">
                      <div className="flex-grow">
                        <div className="font-semibold text-gray-900">
                          {flag.type}: {flag.category}
                        </div>
                        <div className="text-sm text-gray-700 mt-1">{flag.message}</div>
                        {flag.similarity_score && (
                          <div className="text-xs text-gray-600 mt-1">
                            Similarity: {(flag.similarity_score * 100).toFixed(0)}%
                          </div>
                        )}
                      </div>
                      <span
                        className={`ml-4 px-2 py-1 rounded text-xs font-semibold ${
                          flag.severity === 'HIGH'
                            ? 'bg-red-600 text-white'
                            : 'bg-yellow-600 text-white'
                        }`}
                      >
                        {flag.severity}
                      </span>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* Filter Buttons */}
          <div className="flex flex-wrap gap-2">
            <button
              onClick={() => setFilterType('all')}
              className={`px-4 py-2 rounded font-semibold transition ${
                filterType === 'all'
                  ? 'bg-indigo-600 text-white'
                  : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
              }`}
            >
              All
            </button>
            <button
              onClick={() => setFilterType('matching')}
              className={`px-4 py-2 rounded font-semibold transition ${
                filterType === 'matching'
                  ? 'bg-green-600 text-white'
                  : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
              }`}
            >
              ✓ Matching
            </button>
            <button
              onClick={() => setFilterType('different')}
              className={`px-4 py-2 rounded font-semibold transition ${
                filterType === 'different'
                  ? 'bg-yellow-600 text-white'
                  : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
              }`}
            >
              ⚠ Different
            </button>
            <button
              onClick={() => setFilterType('missing')}
              className={`px-4 py-2 rounded font-semibold transition ${
                filterType === 'missing'
                  ? 'bg-red-600 text-white'
                  : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
              }`}
            >
              ✗ Missing
            </button>
          </div>

          {/* Split View Comparison */}
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
            {/* Document 1 */}
            <div className="bg-white rounded-lg shadow p-6">
              <h3 className="text-lg font-semibold text-gray-900 mb-4">
                {comparison.documents.document1.filename}
              </h3>
              <div className="space-y-3 max-h-96 overflow-y-auto">
                {getFilteredComparison().matching_categories.map((category) => (
                  <div key={category} className="p-3 bg-green-50 border border-green-200 rounded">
                    <div className="font-semibold text-green-900">✓ {category}</div>
                    <div className="text-sm text-green-700 mt-1">Present in both documents</div>
                  </div>
                ))}
                {Object.entries(getFilteredComparison().only_in_first).map(
                  ([category, clauses]) => (
                    <div key={category} className="p-3 bg-blue-50 border border-blue-200 rounded">
                      <div className="font-semibold text-blue-900">📄 {category}</div>
                      <div className="text-sm text-blue-700 mt-1">Only in Doc 1</div>
                    </div>
                  )
                )}
              </div>
            </div>

            {/* Document 2 */}
            <div className="bg-white rounded-lg shadow p-6">
              <h3 className="text-lg font-semibold text-gray-900 mb-4">
                {comparison.documents.document2.filename}
              </h3>
              <div className="space-y-3 max-h-96 overflow-y-auto">
                {getFilteredComparison().matching_categories.map((category) => (
                  <div key={category} className="p-3 bg-green-50 border border-green-200 rounded">
                    <div className="font-semibold text-green-900">✓ {category}</div>
                    <div className="text-sm text-green-700 mt-1">Present in both documents</div>
                  </div>
                ))}
                {Object.entries(getFilteredComparison().only_in_second).map(
                  ([category, clauses]) => (
                    <div key={category} className="p-3 bg-blue-50 border border-blue-200 rounded">
                      <div className="font-semibold text-blue-900">📄 {category}</div>
                      <div className="text-sm text-blue-700 mt-1">Only in Doc 2</div>
                    </div>
                  )
                )}
              </div>
            </div>
          </div>

          {/* Detailed Differences */}
          {getFilteredComparison().different_clauses.length > 0 && (
            <div className="bg-white rounded-lg shadow p-6">
              <h2 className="text-lg font-semibold text-gray-900 mb-4">
                Clause Differences ({getFilteredComparison().different_clauses.length})
              </h2>
              <div className="space-y-4">
                {getFilteredComparison().different_clauses.map((diff, idx) => (
                  <div key={idx} className="border border-gray-200 rounded-lg p-4">
                    <div className="flex items-center justify-between mb-3">
                      <h3 className="font-semibold text-gray-900">{diff.category}</h3>
                      <div className="text-sm">
                        <span className="inline-block bg-yellow-100 text-yellow-800 px-3 py-1 rounded">
                          Similarity: {(diff.similarity * 100).toFixed(0)}%
                        </span>
                      </div>
                    </div>
                    <div className="grid grid-cols-2 gap-4 text-sm">
                      <div>
                        <div className="font-semibold text-gray-700 mb-2">Document 1</div>
                        <p className="text-gray-600 bg-gray-50 p-3 rounded">
                          {diff.clause_1}
                        </p>
                      </div>
                      <div>
                        <div className="font-semibold text-gray-700 mb-2">Document 2</div>
                        <p className="text-gray-600 bg-gray-50 p-3 rounded">
                          {diff.clause_2}
                        </p>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* New Comparison Button */}
          <div className="flex justify-center gap-4">
            <button
              onClick={() => {
                setComparison(null);
                setSelectedDoc1(null);
                setSelectedDoc2(null);
              }}
              className="px-6 py-3 bg-indigo-600 hover:bg-indigo-700 text-white font-semibold rounded-lg"
            >
              Compare Again
            </button>
            <button
              onClick={() => navigate('/documents')}
              className="px-6 py-3 bg-gray-200 hover:bg-gray-300 text-gray-700 font-semibold rounded-lg"
            >
              Back to Documents
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default DocumentComparison;
