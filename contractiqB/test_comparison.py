#!/usr/bin/env python
"""
Test script to upload sample contracts and test comparison.
This script uses the text extraction API to create test documents.
"""

import requests
import json
import sys
import time

# Configuration
API_BASE_URL = "http://localhost:5000/api"
TEST_USER = {
    "username": "testuser",
    "email": "testuser@example.com",
    "password": "TestPassword123!"
}

def print_section(title):
    """Print a formatted section header."""
    print("\n" + "=" * 70)
    print(f"  {title}")
    print("=" * 70)

def register_user():
    """Register a test user."""
    print_section("STEP 1: Register Test User")
    
    response = requests.post(
        f"{API_BASE_URL}/register",
        json=TEST_USER,
        headers={"Content-Type": "application/json"}
    )
    
    if response.status_code in [200, 201]:
        print(f"✓ User registered: {TEST_USER['username']}")
        return True
    elif response.status_code == 409:
        print(f"✓ User already exists: {TEST_USER['username']}")
        return True
    else:
        print(f"✗ Registration failed: {response.text}")
        return False

def login_user():
    """Log in the test user and get session."""
    print_section("STEP 2: Login and Get Session")
    
    response = requests.post(
        f"{API_BASE_URL}/login",
        json={"username": TEST_USER["username"], "password": TEST_USER["password"]},
        headers={"Content-Type": "application/json"}
    )
    
    if response.status_code == 200:
        data = response.json()
        if data.get("success"):
            print(f"✓ Logged in successfully")
            print(f"  Token: {data.get('access_token', 'N/A')[:20]}...")
            return response.cookies  # Return session cookies
        else:
            print(f"✗ Login failed: {data.get('error')}")
            return None
    else:
        print(f"✗ Login error: {response.status_code}")
        return None

def load_sample_contract(filename):
    """Load sample contract text."""
    try:
        with open(filename, 'r') as f:
            return f.read()
    except FileNotFoundError:
        print(f"✗ Sample file not found: {filename}")
        return None

def upload_contract_text(contract_text, contract_name, cookies):
    """Upload contract using text extraction endpoint."""
    print(f"\n  Uploading: {contract_name}...")
    
    response = requests.post(
        f"{API_BASE_URL}/extract-text",
        json={
            "text": contract_text,
            "document_name": contract_name
        },
        headers={"Content-Type": "application/json"},
        cookies=cookies
    )
    
    if response.status_code == 200:
        data = response.json()
        if data.get("success"):
            doc_id = data.get("document", {}).get("id")
            clauses = data.get("document", {}).get("clauses_summary", {})
            print(f"  ✓ Uploaded successfully (ID: {doc_id})")
            print(f"    Total clauses found: {clauses.get('total_clauses', 0)}")
            return doc_id
        else:
            print(f"  ✗ Upload failed: {data.get('error')}")
            return None
    else:
        print(f"  ✗ Upload error: {response.status_code}")
        print(f"    {response.text}")
        return None

def upload_contracts(cookies):
    """Upload both sample contracts."""
    print_section("STEP 3: Upload Sample Contracts")
    
    # Load contracts
    contract1_text = load_sample_contract("sample_contract_1.txt")
    contract2_text = load_sample_contract("sample_contract_2.txt")
    
    if not contract1_text or not contract2_text:
        print("✗ Could not load sample contracts")
        return None, None
    
    # Upload contracts
    doc1_id = upload_contract_text(contract1_text, "Sample Contract A", cookies)
    time.sleep(1)  # Brief pause between uploads
    doc2_id = upload_contract_text(contract2_text, "Sample Contract B", cookies)
    
    if doc1_id and doc2_id:
        print(f"\n✓ Both contracts uploaded successfully")
        return doc1_id, doc2_id
    else:
        print(f"\n✗ Failed to upload both contracts")
        return None, None

def compare_contracts(doc1_id, doc2_id, cookies):
    """Compare the two uploaded contracts."""
    print_section("STEP 4: Compare Contracts")
    
    print(f"Comparing Document {doc1_id} vs Document {doc2_id}...")
    
    response = requests.post(
        f"{API_BASE_URL}/compare",
        json={
            "document1_id": doc1_id,
            "document2_id": doc2_id
        },
        headers={"Content-Type": "application/json"},
        cookies=cookies
    )
    
    if response.status_code == 200:
        data = response.json()
        if data.get("success"):
            comparison = data.get("comparison", {})
            summary = comparison.get("summary", {})
            
            print(f"\n✓ Comparison completed successfully!\n")
            print(f"  Overall Similarity: {summary.get('overall_similarity', 'N/A')}%")
            print(f"  Matching Categories: {summary.get('matching_categories_count', 0)}")
            print(f"  Different Clauses: {summary.get('different_clauses_count', 0)}")
            print(f"  Risk Flags: {summary.get('risk_flags_count', 0)}")
            
            # Print matching categories
            matching = comparison.get("matching_categories", [])
            if matching:
                print(f"\n  Matching Categories:")
                for cat in matching:
                    print(f"    • {cat}")
            
            # Print different clauses
            different = comparison.get("different_clauses", [])
            if different:
                print(f"\n  Different Clauses:")
                for clause in different:
                    print(f"    • {clause.get('category')}: Similarity {clause.get('similarity', 'N/A')}")
            
            # Print risk flags
            risks = comparison.get("risk_flags", [])
            if risks:
                print(f"\n  Risk Flags ({len(risks)}):")
                for flag in risks:
                    print(f"    • [{flag.get('severity')}] {flag.get('message')}")
            
            return True
        else:
            print(f"✗ Comparison failed: {data.get('error')}")
            return False
    else:
        print(f"✗ Comparison error: {response.status_code}")
        print(f"  {response.text}")
        return False

def main():
    """Run the test workflow."""
    print("\n" + "🧪 ContractIQ Sample Documents Test")
    print("=" * 70)
    
    # Check if server is running
    try:
        response = requests.get(f"{API_BASE_URL}/health" if hasattr(requests, 'health') else f"{API_BASE_URL}/../health", timeout=2)
    except:
        print("\n⚠ Backend server is not running!")
        print("  Start it with: cd contractiqB && python -m flask run --host=0.0.0.0 --port=5000")
        return
    
    # Step 1: Register user
    if not register_user():
        print("\n✗ Failed to register user. Exiting.")
        return
    
    # Step 2: Login
    cookies = login_user()
    if not cookies:
        print("\n✗ Failed to login. Exiting.")
        return
    
    # Step 3: Upload contracts
    doc1_id, doc2_id = upload_contracts(cookies)
    if not doc1_id or not doc2_id:
        print("\n✗ Failed to upload documents. Exiting.")
        return
    
    # Step 4: Compare contracts
    if not compare_contracts(doc1_id, doc2_id, cookies):
        print("\n✗ Comparison failed.")
        return
    
    print("\n" + "=" * 70)
    print("✓ TEST COMPLETED SUCCESSFULLY!")
    print("=" * 70)
    print("\n📝 Next Steps:")
    print("  1. Check the browser at http://localhost:3000")
    print("  2. Log in with: testuser / TestPassword123!")
    print("  3. Go to 'Compare Documents' page")
    print("  4. You should see the uploaded sample contracts in the dropdown")
    print("  5. Select both and click 'Compare Documents' to see results\n")

if __name__ == '__main__':
    main()
