#!/usr/bin/env python
"""
Debug script to check documents and their extracted clauses.
"""

import sqlite3
import json
import os
from database import get_db_connection

def check_documents_detail():
    """Check all documents and their clause extraction status."""
    print("\n" + "=" * 80)
    print("  DOCUMENT EXTRACTION STATUS")
    print("=" * 80)
    
    try:
        conn = get_db_connection()
        cursor = conn.cursor()
        
        # Get all documents
        cursor.execute("""
            SELECT id, original_filename, extracted_text, clauses 
            FROM documents ORDER BY upload_date DESC
        """)
        
        documents = cursor.fetchall()
        
        if not documents:
            print("\n⚠ No documents found in database!")
            return
        
        print(f"\n Found {len(documents)} document(s):\n")
        
        for doc in documents:
            doc_id = doc[0]
            filename = doc[1]
            extracted_text = doc[2]
            clauses_json = doc[3]
            
            print(f"{'─' * 80}")
            print(f"Document ID: {doc_id}")
            print(f"Filename: {filename}")
            
            # Check text extraction
            text_len = len(extracted_text) if extracted_text else 0
            print(f"Extracted Text Length: {text_len} characters")
            
            if text_len == 0:
                print("  ⚠ WARNING: NO TEXT EXTRACTED!")
                print("    → Possible causes:")
                print("      1. Scanned/image-based PDF (use 'Extract Text' feature instead)")
                print("      2. Password-protected PDF")
                print("      3. Corrupted PDF file")
                print("    → Solution: Use the text extraction feature to paste text directly")
            else:
                # Show first 200 chars of extracted text
                text_preview = extracted_text[:200].replace('\n', ' ')
                print(f"Text Preview: {text_preview}...")
            
            # Check clauses extraction
            try:
                if clauses_json:
                    clauses = json.loads(clauses_json) if isinstance(clauses_json, str) else clauses_json
                    total_clauses = sum(len(v) for v in clauses.values() if v)
                    categories = {k: len(v) for k, v in clauses.items() if v}
                    
                    print(f"Clauses Found: {total_clauses}")
                    
                    if categories:
                        print("  Categories with clauses:")
                        for cat, count in categories.items():
                            print(f"    • {cat}: {count} clause(s)")
                    else:
                        print("  ⚠ NO CLAUSES MATCHED!")
                        print("    → Possible causes:")
                        print("      1. Text doesn't contain contract keywords")
                        print("      2. PDF is not a contract document")
                        print("    → Solution: Use sample contracts or documents with keywords like:")
                        print("      'payment', 'termination', 'liability', 'confidential', etc.")
                else:
                    print("Clauses: None (empty)")
            except Exception as e:
                print(f"Clauses: Error parsing JSON - {e}")
            
            print()
        
        conn.close()
        
    except Exception as e:
        print(f"✗ Error: {e}")

def main():
    """Run diagnostic."""
    check_documents_detail()
    
    print("\n" + "=" * 80)
    print("  HOW TO FIX")
    print("=" * 80)
    print("""
If your documents show 0% similarity:

OPTION 1: Use Text Extraction Feature (Recommended)
─────────────────────────────────────
1. Go to Dashboard > "Extract Text"
2. Paste contract text directly (don't upload PDF)
3. This bypasses PDF extraction issues
4. The sample_contract_1.txt and sample_contract_2.txt files have proper keywords

OPTION 2: Use the Test Script
──────────────────────────────
1. Make sure Flask is running on port 5000
2. Run: python test_comparison.py
3. This automatically uploads the sample contracts using text extraction
4. Comparison should show results immediately

OPTION 3: Use PDF with Selectable Text
───────────────────────────────────────
1. The PDF must be text-based (not scanned/image)
2. Open in a PDF reader and try to select/copy text
3. If you can't select text, it won't work → use Option 1

Sample Contract Keywords (included):
  • Payment: "monthly fee", "invoice", "due date", "late payment"
  • Termination: "terminate", "written notice", "end agreement"
  • Liability: "liable", "indemnify", "damages", "limitation of liability"
  • Confidentiality: "confidential", "proprietary", "trade secrets"
  • Intellectual Property: "intellectual property", "ownership", "patent"
    """)

if __name__ == '__main__':
    main()
