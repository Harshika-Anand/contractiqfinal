#!/usr/bin/env python3
"""
Setup Test Documents Script
============================
This script prepares the test environment by:
1. Creating PDF files from sample contracts
2. Registering/logging in the test user
3. Uploading the documents to the backend
4. Verifying the documents are available

Run this before executing the DocumentComparisonPageTest to ensure documents exist.
"""

import requests
import json
from pathlib import Path
from reportlab.lib.pagesizes import letter
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.platypus import SimpleDocTemplate, Paragraph, Spacer
from reportlab.lib.units import inch
import sys
import os

# Configuration
API_BASE_URL = "http://localhost:5000"
TEST_USER_EMAIL = "testuser@test.com"
TEST_USER_PASSWORD = "Test@1234"
TEST_USER_USERNAME = "testuser"
UPLOADS_DIR = Path(__file__).parent / "uploads"
SAMPLES_DIR = Path(__file__).parent

# Ensure uploads directory exists
UPLOADS_DIR.mkdir(exist_ok=True)

class DocumentUploader:
    def __init__(self, base_url=API_BASE_URL):
        self.base_url = base_url
        self.session = requests.Session()
        self.token = None
        self.user_id = None
    
    def register_user(self):
        """Register the test user if not already registered"""
        print("\n[1] Registering test user...")
        url = f"{self.base_url}/api/register"
        data = {
            "username": TEST_USER_USERNAME,
            "email": TEST_USER_EMAIL,
            "password": TEST_USER_PASSWORD,
            "role": "client"
        }
        
        try:
            response = self.session.post(url, json=data)
            if response.status_code == 201:
                result = response.json()
                print(f"✓ User registered successfully: {result['user']['username']}")
                return True
            elif response.status_code == 409:
                print(f"✓ User already exists, continuing...")
                return True
            else:
                print(f"✗ Registration failed: {response.text}")
                return False
        except Exception as e:
            print(f"✗ Registration error: {e}")
            return False
    
    def login_user(self):
        """Login and get authentication token"""
        print("\n[2] Logging in test user...")
        url = f"{self.base_url}/api/login"
        data = {
            "email": TEST_USER_EMAIL,
            "password": TEST_USER_PASSWORD
        }
        
        try:
            response = self.session.post(url, json=data)
            if response.status_code == 200:
                result = response.json()
                self.token = result.get('access_token')
                self.user_id = result.get('user', {}).get('id')
                print(f"✓ Login successful")
                print(f"  User ID: {self.user_id}")
                print(f"  Token: {self.token[:20]}..." if self.token else "  Token: None")
                
                # Set authorization header for future requests
                if self.token:
                    self.session.headers.update({
                        "Authorization": f"Bearer {self.token}"
                    })
                return True
            else:
                print(f"✗ Login failed: {response.text}")
                return False
        except Exception as e:
            print(f"✗ Login error: {e}")
            return False
    
    def create_pdf_from_text(self, text_content, filename):
        """Create a PDF file from text content"""
        print(f"\n[*] Creating PDF: {filename}")
        pdf_path = UPLOADS_DIR / filename
        
        try:
            doc = SimpleDocTemplate(
                str(pdf_path),
                pagesize=letter,
                rightMargin=72,
                leftMargin=72,
                topMargin=72,
                bottomMargin=18
            )
            
            # Create stylesheet
            styles = getSampleStyleSheet()
            story = []
            
            # Split text into paragraphs
            paragraphs = text_content.split('\n\n')
            for para_text in paragraphs:
                if para_text.strip():
                    para = Paragraph(para_text.strip(), styles['Normal'])
                    story.append(para)
                    story.append(Spacer(1, 0.2*inch))
            
            # Build PDF
            doc.build(story)
            print(f"  ✓ PDF created: {pdf_path}")
            return str(pdf_path)
        except Exception as e:
            print(f"  ✗ PDF creation failed: {e}")
            return None
    
    def read_sample_contract(self, filename):
        """Read sample contract text"""
        sample_path = SAMPLES_DIR / filename
        try:
            with open(sample_path, 'r', encoding='utf-8') as f:
                return f.read()
        except Exception as e:
            print(f"✗ Failed to read {filename}: {e}")
            return None
    
    def upload_document(self, pdf_path, original_name):
        """Upload a PDF document to the backend"""
        print(f"\n[*] Uploading document: {original_name}")
        url = f"{self.base_url}/api/upload"
        
        try:
            with open(pdf_path, 'rb') as f:
                files = {'file': (original_name, f, 'application/pdf')}
                response = self.session.post(url, files=files)
            
            if response.status_code == 200:
                result = response.json()
                doc_id = result['document']['id']
                filename = result['document']['filename']
                clauses_count = result['document']['clauses_summary']['total_clauses']
                print(f"  ✓ Upload successful!")
                print(f"    Document ID: {doc_id}")
                print(f"    Filename: {filename}")
                print(f"    Clauses found: {clauses_count}")
                return True
            else:
                print(f"  ✗ Upload failed: {response.status_code}")
                print(f"    Response: {response.text}")
                return False
        except Exception as e:
            print(f"  ✗ Upload error: {e}")
            return False
    
    def get_user_documents(self):
        """Get list of documents for the user"""
        print("\n[3] Verifying uploaded documents...")
        url = f"{self.base_url}/api/documents"
        
        try:
            response = self.session.get(url)
            if response.status_code == 200:
                result = response.json()
                documents = result.get('documents', [])
                print(f"✓ Found {len(documents)} document(s)")
                for doc in documents:
                    print(f"  - {doc.get('original_filename', 'Unknown')} (ID: {doc.get('id')})")
                return len(documents) > 0
            else:
                print(f"✗ Failed to get documents: {response.text}")
                return False
        except Exception as e:
            print(f"✗ Error getting documents: {e}")
            return False
    
    def run(self):
        """Execute the complete setup process"""
        print("=" * 60)
        print("ContractIQ - Test Document Setup")
        print("=" * 60)
        
        # Step 1: Register user
        if not self.register_user():
            return False
        
        # Step 2: Login
        if not self.login_user():
            return False
        
        # Step 3: Create and upload documents
        contracts = [
            ("sample_contract_1.txt", "Contract_A.pdf"),
            ("sample_contract_2.txt", "Contract_B.pdf")
        ]
        
        uploaded_count = 0
        for sample_file, pdf_filename in contracts:
            # Read sample contract
            text_content = self.read_sample_contract(sample_file)
            if not text_content:
                continue
            
            # Create PDF
            pdf_path = self.create_pdf_from_text(text_content, pdf_filename)
            if not pdf_path:
                continue
            
            # Upload document
            if self.upload_document(pdf_path, pdf_filename):
                uploaded_count += 1
        
        # Step 4: Verify documents
        print("\n[4] Verification")
        if uploaded_count >= 2:
            if self.get_user_documents():
                print("\n" + "=" * 60)
                print("✓ SETUP COMPLETE - Documents ready for testing!")
                print("=" * 60)
                return True
        
        print("\n" + "=" * 60)
        print("✗ SETUP INCOMPLETE - Some documents failed to upload")
        print("=" * 60)
        return False


def main():
    """Main entry point"""
    try:
        uploader = DocumentUploader()
        success = uploader.run()
        sys.exit(0 if success else 1)
    except Exception as e:
        print(f"\n✗ Setup failed with error: {e}")
        import traceback
        traceback.print_exc()
        sys.exit(1)


if __name__ == "__main__":
    main()
