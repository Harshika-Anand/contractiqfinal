import React from 'react';
import { Link } from 'react-router-dom';

const Home = () => {
  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-50 via-blue-50 to-indigo-50">
      <div className="container mx-auto px-4 py-20">
        <div className="text-center max-w-4xl mx-auto">
          <h1 className="text-6xl font-bold text-gray-900 mb-6 leading-tight">
            Smart Contract Analysis
            <span className="block text-indigo-600 mt-2">Made Simple</span>
          </h1>
          
          <p className="text-xl text-gray-600 mb-10 leading-relaxed">
            Upload PDF contracts and automatically identify key legal clauses. 
            Save time, reduce risk, and understand your agreements better.
          </p>

          <div className="flex gap-4 justify-center">
            <Link
              to="/register"
              className="px-8 py-4 bg-indigo-600 text-white rounded-lg font-semibold hover:bg-indigo-700 transition-all"
            >
              Get Started Free
            </Link>
            
            <Link
              to="/login"
              className="px-8 py-4 bg-white text-indigo-600 rounded-lg font-semibold hover:bg-gray-50 transition-all border-2 border-indigo-600"
            >
              Sign In
            </Link>
          </div>
        </div>

        <div className="mt-24 grid md:grid-cols-3 gap-8 max-w-6xl mx-auto">
          <div className="bg-white p-8 rounded-2xl shadow-lg">
            <div className="bg-indigo-100 w-16 h-16 rounded-xl flex items-center justify-center mb-6">
              <svg className="w-8 h-8 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
              </svg>
            </div>
            <h3 className="text-xl font-bold text-gray-900 mb-3">Smart PDF Analysis</h3>
            <p className="text-gray-600">Upload contracts and automatically extract key clauses using AI-powered analysis.</p>
          </div>

          <div className="bg-white p-8 rounded-2xl shadow-lg">
            <div className="bg-indigo-100 w-16 h-16 rounded-xl flex items-center justify-center mb-6">
              <svg className="w-8 h-8 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
              </svg>
            </div>
            <h3 className="text-xl font-bold text-gray-900 mb-3">Clause Detection</h3>
            <p className="text-gray-600">Identify termination, liability, payment, confidentiality, and IP clauses instantly.</p>
          </div>

          <div className="bg-white p-8 rounded-2xl shadow-lg">
            <div className="bg-indigo-100 w-16 h-16 rounded-xl flex items-center justify-center mb-6">
              <svg className="w-8 h-8 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
              </svg>
            </div>
            <h3 className="text-xl font-bold text-gray-900 mb-3">Dashboard Analytics</h3>
            <p className="text-gray-600">Track your documents, view statistics, and manage your contract library efficiently.</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Home;