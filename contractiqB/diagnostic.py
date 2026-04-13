#!/usr/bin/env python
"""
Diagnostic script to check the ContractIQ application status.
This helps troubleshoot data display issues.
"""

import sqlite3
import json
import os
from database import get_db_connection

def print_section(title):
    """Print a formatted section header."""
    print("\n" + "=" * 70)
    print(f"  {title}")
    print("=" * 70)

def check_database():
    """Check database connectivity and basic info."""
    print_section("DATABASE STATUS")
    
    try:
        conn = get_db_connection()
        cursor = conn.cursor()
        
        # Check if tables exist
        cursor.execute("SELECT name FROM sqlite_master WHERE type='table';")
        tables = cursor.fetchall()
        
        print(f"✓ Database connected: {os.path.basename(os.path.dirname(conn.execute('PRAGMA database_list').fetchall()[0][2]))}")
        print(f"✓ Tables found: {len(tables)}")
        for table in tables:
            print(f"  • {table[0]}")
        
        conn.close()
        return True
    except Exception as e:
        print(f"✗ Database error: {e}")
        return False

def check_users():
    """Check users in the database."""
    print_section("USERS")
    
    try:
        conn = get_db_connection()
        cursor = conn.cursor()
        
        cursor.execute("SELECT id, username, email, created_date FROM users;")
        users = cursor.fetchall()
        
        if users:
            print(f"Found {len(users)} user(s):")
            for user in users:
                print(f"\n  ID: {user[0]}")
                print(f"  Username: {user[1]}")
                print(f"  Email: {user[2]}")
                print(f"  Created: {user[3]}")
        else:
            print("⚠ No users found. You need to register and log in first.")
        
        conn.close()
        return len(users) > 0
    except Exception as e:
        print(f"✗ Error checking users: {e}")
        return False

def check_documents(user_id=None):
    """Check documents and their processing status."""
    print_section("DOCUMENTS")
    
    try:
        conn = get_db_connection()
        cursor = conn.cursor()
        
        if user_id:
            cursor.execute("""
                SELECT id, original_filename, upload_date, extracted_text, clauses 
                FROM documents WHERE user_id = ? ORDER BY upload_date DESC
            """, (user_id,))
        else:
            cursor.execute("""
                SELECT id, user_id, original_filename, upload_date, extracted_text, clauses 
                FROM documents ORDER BY upload_date DESC LIMIT 10
            """)
        
        documents = cursor.fetchall()
        
        if documents:
            print(f"✓ Found {len(documents)} document(s):\n")
            for doc in documents:
                doc_id = doc[0]
                if user_id:
                    filename = doc[1]
                    upload_date = doc[2]
                    extracted_text = doc[3]
                    clauses = doc[4]
                else:
                    user_id_col = doc[1]
                    filename = doc[2]
                    upload_date = doc[3]
                    extracted_text = doc[4]
                    clauses = doc[5]
                
                print(f"  Document ID: {doc_id}")
                print(f"  Filename: {filename}")
                print(f"  Upload Date: {upload_date}")
                
                # Check extracted text
                text_len = len(extracted_text) if extracted_text else 0
                print(f"  Extracted Text: {text_len} characters")
                
                if text_len == 0:
                    print(f"    ⚠ WARNING: No text extracted! This could be:")
                    print(f"       - Scanned/image-based PDF")
                    print(f"       - Password-protected PDF")
                    print(f"       - Corrupted PDF")
                
                # Check clauses
                try:
                    if clauses:
                        clauses_dict = json.loads(clauses) if isinstance(clauses, str) else clauses
                        total_clauses = sum(len(v) for v in clauses_dict.values() if v)
                        categories_found = [k for k, v in clauses_dict.items() if v]
                        print(f"  Clauses Found: {total_clauses}")
                        if categories_found:
                            print(f"  Categories: {', '.join(categories_found)}")
                        else:
                            print(f"    ⚠ WARNING: No clauses extracted from this document!")
                            print(f"       Text may not match contract keywords.")
                    else:
                        print(f"  Clauses: None")
                except:
                    print(f"  Clauses: Error parsing")
                
                print()
        else:
            print("⚠ No documents found. You need to upload documents first.")
        
        conn.close()
        return len(documents) > 0 if documents else False
    except Exception as e:
        print(f"✗ Error checking documents: {e}")
        return False

def check_comparisons():
    """Check comparison history."""
    print_section("COMPARISONS")
    
    try:
        conn = get_db_connection()
        cursor = conn.cursor()
        
        cursor.execute("""
            SELECT id, user_id, document1_id, document2_id, comparison_date 
            FROM comparisons ORDER BY comparison_date DESC LIMIT 5
        """)
        
        comparisons = cursor.fetchall()
        
        if comparisons:
            print(f"✓ Found {len(comparisons)} comparison(s):\n")
            for comp in comparisons:
                print(f"  ID: {comp[0]}")
                print(f"  Documents: {comp[2]} vs {comp[3]}")
                print(f"  Date: {comp[4]}")
                print()
        else:
            print("No comparisons yet. Run a comparison to test.")
        
        conn.close()
    except Exception as e:
        print(f"✗ Error checking comparisons: {e}")

def check_uploads_folder():
    """Check the uploads folder."""
    print_section("UPLOADS FOLDER")
    
    uploads_path = os.path.join(os.path.dirname(__file__), 'uploads')
    
    if os.path.exists(uploads_path):
        files = os.listdir(uploads_path)
        print(f"✓ Uploads folder exists")
        print(f"  Files: {len(files)}")
        if files:
            for f in files[:10]:  # Show first 10 files
                file_path = os.path.join(uploads_path, f)
                size = os.path.getsize(file_path) / (1024 * 1024)  # Size in MB
                print(f"  • {f} ({size:.2f} MB)")
    else:
        print(f"⚠ Uploads folder not found at: {uploads_path}")

def main():
    """Run all diagnostics."""
    print("\n" + "🔍 ContractIQ Diagnostic Report")
    print("=" * 70)
    
    # Run checks
    db_ok = check_database()
    
    if db_ok:
        users_exist = check_users()
        
        if users_exist:
            # Use first user for document check
            conn = get_db_connection()
            cursor = conn.cursor()
            cursor.execute("SELECT id FROM users LIMIT 1")
            user = cursor.fetchone()
            conn.close()
            
            if user:
                check_documents(user[0])
        else:
            check_documents()  # Check all documents
        
        check_comparisons()
    
    check_uploads_folder()
    
    print("\n" + "=" * 70)
    print("📋 NEXT STEPS:")
    print("=" * 70)
    print("""
1. If no documents found:
   - Open the application in your browser
   - Log in or register
   - Upload PDF documents (ensure they're text-based, not scanned)

2. If documents found but no clauses:
   - The PDFs may be scanned/image-based (no extractable text)
   - Try uploading a different PDF with selectable text
   - You can also use the "Extract Text" feature to paste text directly

3. If clauses found but comparisons don't show data:
   - The fix has been applied to the backend
   - Restart the Flask server and try again
   - Check browser console for errors (F12)

4. For testing:
   - Use the text extraction feature (/api/extract-text)
   - Paste sample contract text with keywords like "payment", "termination", etc.
   """)

if __name__ == '__main__':
    main()
