# Database configuration and models
# This module handles all SQLite database operations for ContractIQ
# Uses Python's built-in sqlite3 module for database connectivity

import sqlite3
import os
import json
from datetime import datetime

# =============================================================================
# DATABASE CONFIGURATION
# =============================================================================

# Database file path - stored in the instance folder for Flask convention
# The instance folder is typically used for deployment-specific files
DATABASE_PATH = os.path.join(os.path.dirname(__file__), 'instance', 'contractiq.db')


def get_db_connection():
    """
    Create and return a database connection.
    
    This function establishes a connection to the SQLite database.
    - Row factory is set to sqlite3.Row to enable column access by name
    - This allows us to access results like dictionaries (row['column_name'])
    
    Returns:
        sqlite3.Connection: A connection object to the database
    
    Example:
        conn = get_db_connection()
        cursor = conn.execute("SELECT * FROM users")
        conn.close()
    """
    # Ensure the instance directory exists before connecting
    os.makedirs(os.path.dirname(DATABASE_PATH), exist_ok=True)
    
    # Create connection with row factory for dict-like access
    conn = sqlite3.connect(DATABASE_PATH)
    conn.row_factory = sqlite3.Row
    
    # Enable foreign key support (disabled by default in SQLite)
    conn.execute("PRAGMA foreign_keys = ON")
    
    return conn


# =============================================================================
# DATABASE INITIALIZATION
# =============================================================================

def init_db():
    """
    Initialize the database by creating all required tables.
    
    This function should be called when the application starts.
    It creates the following tables if they don't already exist:
    - users: Stores user account information
    - documents: Stores uploaded contract documents and extracted data
    
    The function uses 'IF NOT EXISTS' to prevent errors if tables already exist.
    This makes it safe to call multiple times (idempotent).
    
    Returns:
        bool: True if initialization was successful
    
    Example:
        if init_db():
            print("Database ready!")
    """
    conn = get_db_connection()
    cursor = conn.cursor()
    
    try:
        # ---------------------------------------------------------------------
        # USERS TABLE
        # ---------------------------------------------------------------------
        # Stores user authentication and profile information
        # - id: Unique identifier, auto-incremented by SQLite
        # - username: User's login name, must be unique
        # - email: User's email address, must be unique
        # - password_hash: Hashed password (never store plain text passwords!)
        # - role: User's permission level (admin/lawyer/client)
        # - created_at: Timestamp when the account was created
        cursor.execute('''
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE NOT NULL,
                email TEXT UNIQUE NOT NULL,
                password_hash TEXT NOT NULL,
                role TEXT DEFAULT 'client' CHECK(role IN ('admin', 'lawyer', 'client')),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        ''')
        
        # ---------------------------------------------------------------------
        # DOCUMENTS TABLE
        # ---------------------------------------------------------------------
        # Stores uploaded PDF documents and their extracted content
        # - id: Unique identifier for each document
        # - user_id: Links to the user who uploaded the document (foreign key)
        # - filename: System-generated unique filename (for storage)
        # - original_filename: The original name of the uploaded file
        # - file_path: Full path to where the PDF is stored on disk
        # - extracted_text: The full text content extracted from the PDF
        # - clauses: JSON string containing identified contract clauses
        # - upload_date: When the document was uploaded
        # 
        # ON DELETE CASCADE: If a user is deleted, their documents are also deleted
        cursor.execute('''
            CREATE TABLE IF NOT EXISTS documents (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                filename TEXT NOT NULL,
                original_filename TEXT NOT NULL,
                file_path TEXT NOT NULL,
                extracted_text TEXT,
                clauses TEXT,
                upload_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
            )
        ''')
        
        # Create indexes for faster queries on frequently searched columns
        # Indexes speed up SELECT queries but slightly slow down INSERT/UPDATE
        cursor.execute('CREATE INDEX IF NOT EXISTS idx_documents_user_id ON documents(user_id)')
        cursor.execute('CREATE INDEX IF NOT EXISTS idx_users_username ON users(username)')
        cursor.execute('CREATE INDEX IF NOT EXISTS idx_users_email ON users(email)')
        
        # ---------------------------------------------------------------------
        # COMPARISONS TABLE
        # ---------------------------------------------------------------------
        # Stores clause comparison results between two documents
        # - id: Unique identifier for each comparison
        # - user_id: Links to the user who performed the comparison
        # - document1_id: ID of the first document being compared
        # - document2_id: ID of the second document being compared
        # - comparison_result: JSON string containing detailed comparison analysis
        # - created_at: When the comparison was performed
        cursor.execute('''
            CREATE TABLE IF NOT EXISTS comparisons (
                id TEXT PRIMARY KEY,
                user_id INTEGER NOT NULL,
                document1_id TEXT NOT NULL,
                document2_id TEXT NOT NULL,
                comparison_result TEXT NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
            )
        ''')
        
        # Create indexes for comparison queries
        cursor.execute('CREATE INDEX IF NOT EXISTS idx_comparisons_user_id ON comparisons(user_id)')
        cursor.execute('CREATE INDEX IF NOT EXISTS idx_comparisons_docs ON comparisons(document1_id, document2_id)')
        
        # Commit all changes to the database
        conn.commit()
        print(f"✓ Database initialized successfully at: {DATABASE_PATH}")
        return True
        
    except sqlite3.Error as e:
        # If anything goes wrong, print the error
        print(f"✗ Database initialization error: {e}")
        return False
        
    finally:
        # Always close the connection, even if an error occurred
        conn.close()


# =============================================================================
# USER MANAGEMENT FUNCTIONS
# =============================================================================

def create_user(username, email, password_hash, role='client'):
    """
    Create a new user account in the database.
    
    This function inserts a new user record with the provided information.
    The password should already be hashed before calling this function!
    Never pass plain-text passwords to this function.
    
    Args:
        username (str): Unique username for the account
        email (str): User's email address (must be unique)
        password_hash (str): Pre-hashed password (use Werkzeug's generate_password_hash)
        role (str): User's role - 'admin', 'lawyer', or 'client' (default: 'client')
    
    Returns:
        int: The ID of the newly created user, or None if creation failed
    
    Raises:
        sqlite3.IntegrityError: If username or email already exists
    
    Example:
        from werkzeug.security import generate_password_hash
        hashed = generate_password_hash('mypassword')
        user_id = create_user('john_doe', 'john@example.com', hashed, 'lawyer')
    """
    conn = get_db_connection()
    cursor = conn.cursor()
    
    try:
        cursor.execute('''
            INSERT INTO users (username, email, password_hash, role)
            VALUES (?, ?, ?, ?)
        ''', (username, email, password_hash, role))
        
        conn.commit()
        
        # lastrowid gives us the ID of the just-inserted row
        user_id = cursor.lastrowid
        print(f"✓ User created successfully: {username} (ID: {user_id})")
        return user_id
        
    except sqlite3.IntegrityError as e:
        # This happens if username or email already exists (UNIQUE constraint)
        print(f"✗ User creation failed - duplicate entry: {e}")
        return None
        
    except sqlite3.Error as e:
        print(f"✗ User creation error: {e}")
        return None
        
    finally:
        conn.close()


def get_user_by_username(username):
    """
    Retrieve a user record by their username.
    
    This function is typically used during login to fetch the user's
    stored password hash for verification.
    
    Args:
        username (str): The username to search for
    
    Returns:
        dict: User data as a dictionary with keys:
              - id, username, email, password_hash, role, created_at
              Returns None if user not found
    
    Example:
        user = get_user_by_username('john_doe')
        if user:
            print(f"Found user: {user['email']}")
    """
    conn = get_db_connection()
    
    try:
        cursor = conn.execute(
            'SELECT * FROM users WHERE username = ?',
            (username,)
        )
        row = cursor.fetchone()
        
        # Convert Row object to dictionary, or return None if not found
        if row:
            return dict(row)
        return None
        
    except sqlite3.Error as e:
        print(f"✗ Error fetching user by username: {e}")
        return None
        
    finally:
        conn.close()


def get_user_by_email(email):
    """
    Retrieve a user record by their email address.
    
    This function is used during login to fetch the user's
    stored password hash for verification when logging in with email.
    
    Args:
        email (str): The email address to search for
    
    Returns:
        dict: User data as a dictionary with keys:
              - id, username, email, password_hash, role, created_at
              Returns None if user not found
    
    Example:
        user = get_user_by_email('john@example.com')
        if user:
            print(f"Found user: {user['username']}")
    """
    conn = get_db_connection()
    
    try:
        cursor = conn.execute(
            'SELECT * FROM users WHERE email = ?',
            (email.lower(),)  # Normalize email to lowercase
        )
        row = cursor.fetchone()
        
        # Convert Row object to dictionary, or return None if not found
        if row:
            return dict(row)
        return None
        
    except sqlite3.Error as e:
        print(f"✗ Error fetching user by email: {e}")
        return None
        
    finally:
        conn.close()


def get_user_by_id(user_id):
    """
    Retrieve a user record by their ID.
    
    This function is useful for fetching user details when you have
    the user's ID (e.g., from a JWT token or session).
    
    Args:
        user_id (int): The unique ID of the user
    
    Returns:
        dict: User data as a dictionary (without password_hash for security)
              Returns None if user not found
    
    Example:
        user = get_user_by_id(1)
        if user:
            print(f"User role: {user['role']}")
    """
    conn = get_db_connection()
    
    try:
        # Note: We exclude password_hash from the result for security
        cursor = conn.execute(
            'SELECT id, username, email, role, created_at FROM users WHERE id = ?',
            (user_id,)
        )
        row = cursor.fetchone()
        
        if row:
            return dict(row)
        return None
        
    except sqlite3.Error as e:
        print(f"✗ Error fetching user by ID: {e}")
        return None
        
    finally:
        conn.close()


# =============================================================================
# DOCUMENT MANAGEMENT FUNCTIONS
# =============================================================================

def save_document(user_id, filename, original_filename, file_path, extracted_text=None, clauses=None):
    """
    Save a new document record to the database.
    
    This function stores metadata about an uploaded PDF document,
    including the extracted text and identified clauses.
    
    Args:
        user_id (int): ID of the user who uploaded the document
        filename (str): System-generated unique filename (e.g., UUID-based)
        original_filename (str): Original name of the uploaded file
        file_path (str): Full filesystem path where the PDF is stored
        extracted_text (str, optional): Text content extracted from the PDF
        clauses (dict/str, optional): Identified clauses as dict or JSON string
    
    Returns:
        int: The ID of the saved document, or None if save failed
    
    Example:
        doc_id = save_document(
            user_id=1,
            filename='abc123.pdf',
            original_filename='Contract_2024.pdf',
            file_path='/uploads/abc123.pdf',
            extracted_text='This agreement is made between...',
            clauses={'confidentiality': 'Section 5...', 'termination': 'Section 8...'}
        )
    """
    conn = get_db_connection()
    cursor = conn.cursor()
    
    try:
        # Convert clauses to JSON string if it's a dict
        # Handle double-encoding by detecting and fixing it
        if isinstance(clauses, dict):
            clauses_json = json.dumps(clauses)
        elif isinstance(clauses, str):
            # Check if it's already JSON-encoded
            try:
                # Try to parse it - if it succeeds, check if the result is also a string
                parsed = json.loads(clauses)
                if isinstance(parsed, str):
                    # It was double-encoded! Parse again and re-encode once
                    clauses_json = clauses  # Use the original since parsing twice would give us the dict
                else:
                    # It's properly encoded
                    clauses_json = clauses
            except json.JSONDecodeError:
                # Not valid JSON, just use as-is
                clauses_json = clauses
        else:
            clauses_json = None
        
        cursor.execute('''
            INSERT INTO documents (user_id, filename, original_filename, file_path, extracted_text, clauses)
            VALUES (?, ?, ?, ?, ?, ?)
        ''', (user_id, filename, original_filename, file_path, extracted_text, clauses_json))
        
        conn.commit()
        
        doc_id = cursor.lastrowid
        print(f"✓ Document saved: {original_filename} (ID: {doc_id})")
        return doc_id
        
    except sqlite3.Error as e:
        print(f"✗ Document save error: {e}")
        return None
        
    finally:
        conn.close()


def get_user_documents(user_id):
    """
    Retrieve all documents uploaded by a specific user.
    
    Returns documents in reverse chronological order (newest first).
    The clauses field is automatically parsed from JSON back to Python dict.
    
    Args:
        user_id (int): ID of the user whose documents to retrieve
    
    Returns:
        list: List of document dictionaries, each containing:
              - id, filename, original_filename, file_path, 
              - extracted_text, clauses (as dict), upload_date
              Returns empty list if no documents or error
    
    Example:
        docs = get_user_documents(1)
        for doc in docs:
            print(f"Document: {doc['original_filename']}, Uploaded: {doc['upload_date']}")
    """
    conn = get_db_connection()
    
    try:
        cursor = conn.execute('''
            SELECT * FROM documents 
            WHERE user_id = ? 
            ORDER BY upload_date DESC
        ''', (user_id,))
        
        rows = cursor.fetchall()
        
        # Convert rows to list of dictionaries and parse JSON clauses
        documents = []
        for row in rows:
            doc = dict(row)
            # Parse the clauses JSON string back to Python dict/list
            if doc['clauses']:
                try:
                    doc['clauses'] = json.loads(doc['clauses'])
                except json.JSONDecodeError:
                    doc['clauses'] = None
            documents.append(doc)
        
        return documents
        
    except sqlite3.Error as e:
        print(f"✗ Error fetching user documents: {e}")
        return []
        
    finally:
        conn.close()


def get_document_by_id(document_id, user_id=None):
    """
    Retrieve a single document by its ID.
    
    Optionally verify that the document belongs to a specific user
    for security purposes.
    
    Args:
        document_id (int): The ID of the document to retrieve
        user_id (int, optional): If provided, verify document ownership
    
    Returns:
        dict: Document data, or None if not found (or not owned by user)
    
    Example:
        doc = get_document_by_id(5, user_id=1)
        if doc:
            print(doc['extracted_text'])
    """
    conn = get_db_connection()
    
    try:
        if user_id:
            # Security check: only return if user owns the document
            cursor = conn.execute(
                'SELECT * FROM documents WHERE id = ? AND user_id = ?',
                (document_id, user_id)
            )
        else:
            cursor = conn.execute(
                'SELECT * FROM documents WHERE id = ?',
                (document_id,)
            )
        
        row = cursor.fetchone()
        
        if row:
            doc = dict(row)
            # Parse clauses JSON
            if doc['clauses']:
                try:
                    doc['clauses'] = json.loads(doc['clauses'])
                except json.JSONDecodeError:
                    doc['clauses'] = None
            return doc
        return None
        
    except sqlite3.Error as e:
        print(f"✗ Error fetching document: {e}")
        return None
        
    finally:
        conn.close()


def delete_document(document_id, user_id):
    """
    Delete a document from the database.
    
    This function includes an ownership check - it will only delete
    the document if it belongs to the specified user. This prevents
    users from deleting other users' documents.
    
    Note: This only deletes the database record. The actual PDF file
    should be deleted separately by the calling code.
    
    Args:
        document_id (int): ID of the document to delete
        user_id (int): ID of the user requesting deletion (for ownership verification)
    
    Returns:
        bool: True if document was deleted, False otherwise
    
    Example:
        if delete_document(5, user_id=1):
            # Also delete the physical file
            os.remove(file_path)
    """
    conn = get_db_connection()
    
    try:
        # First, get the document to verify ownership and get file path
        cursor = conn.execute(
            'SELECT file_path FROM documents WHERE id = ? AND user_id = ?',
            (document_id, user_id)
        )
        doc = cursor.fetchone()
        
        if not doc:
            print(f"✗ Document not found or not owned by user")
            return False
        
        # Delete the database record
        cursor = conn.execute(
            'DELETE FROM documents WHERE id = ? AND user_id = ?',
            (document_id, user_id)
        )
        
        conn.commit()
        
        # rowcount tells us how many rows were affected
        if cursor.rowcount > 0:
            print(f"✓ Document deleted (ID: {document_id})")
            return True
        return False
        
    except sqlite3.Error as e:
        print(f"✗ Document deletion error: {e}")
        return False
        
    finally:
        conn.close()


# =============================================================================
# DASHBOARD & STATISTICS FUNCTIONS
# =============================================================================

def get_dashboard_stats(user_id):
    """
    Get statistics for a user's dashboard.
    
    This function aggregates data to provide an overview of the user's
    document library and activity.
    
    Args:
        user_id (int): ID of the user
    
    Returns:
        dict: Dashboard statistics containing:
              - total_documents: Total number of uploaded documents
              - recent_documents: List of 5 most recent documents
              - total_clauses_extracted: Count of documents with extracted clauses
              - account_created: When the user account was created
    
    Example:
        stats = get_dashboard_stats(1)
        print(f"You have {stats['total_documents']} documents")
    """
    conn = get_db_connection()
    
    try:
        stats = {}
        
        # Count total documents
        cursor = conn.execute(
            'SELECT COUNT(*) as count FROM documents WHERE user_id = ?',
            (user_id,)
        )
        stats['total_documents'] = cursor.fetchone()['count']
        
       # Count TOTAL clauses across all documents
        cursor = conn.execute(
            'SELECT id, filename, clauses FROM documents WHERE user_id = ?',
            (user_id,)
        )
        
        all_rows = cursor.fetchall()
        print(f"→ Dashboard: Found {len(all_rows)} total documents for user {user_id}")

        total_clauses = 0
        for row in all_rows:
            clauses_data = row['clauses']
            doc_id = row['id']
            doc_filename = row['filename']
            
            if clauses_data:
                try:
                    # Parse JSON - handle double-encoding
                    clauses_dict = clauses_data
                    
                    # First parse
                    if isinstance(clauses_dict, str):
                        clauses_dict = json.loads(clauses_dict)
                    
                    # Check for double-encoding (if result is still a string after parsing)
                    if isinstance(clauses_dict, str):
                        print(f"  Doc {doc_id}: Detected double-encoding, parsing again...")
                        clauses_dict = json.loads(clauses_dict)
                    
                    # Count clauses in each category
                    if isinstance(clauses_dict, dict):
                        doc_clause_count = 0
                        for category, clause_list in clauses_dict.items():
                            if isinstance(clause_list, list):
                                doc_clause_count += len(clause_list)
                        total_clauses += doc_clause_count
                        if doc_clause_count > 0:
                            print(f"  ✓ Doc {doc_id}: {doc_clause_count} clauses")
                except (json.JSONDecodeError, TypeError, AttributeError) as e:
                    print(f"  ✗ Doc {doc_id}: Parse error - {e}")
                    continue
            else:
                print(f"  Doc {doc_id}: No clauses")

        stats['total_clauses_extracted'] = total_clauses
        print(f"→ Dashboard: Total clauses: {total_clauses}")
        # Get 5 most recent documents (just basic info, not full text)
        cursor = conn.execute('''
            SELECT id, original_filename, upload_date 
            FROM documents 
            WHERE user_id = ? 
            ORDER BY upload_date DESC 
            LIMIT 5
        ''', (user_id,))
        stats['recent_documents'] = [dict(row) for row in cursor.fetchall()]
        
        # Get user account creation date
        cursor = conn.execute(
            'SELECT created_at FROM users WHERE id = ?',
            (user_id,)
        )
        user_row = cursor.fetchone()
        stats['account_created'] = user_row['created_at'] if user_row else None
        
        return stats
        
    except sqlite3.Error as e:
        print(f"✗ Error fetching dashboard stats: {e}")
        return {
            'total_documents': 0,
            'recent_documents': [],
            'total_clauses_extracted': 0,
            'account_created': None
        }
        
    finally:
        conn.close()


# =============================================================================
# UTILITY FUNCTIONS
# =============================================================================

def user_exists(username=None, email=None):
    """
    Check if a user with the given username or email already exists.
    
    Useful for validation before attempting to create a new user.
    
    Args:
        username (str, optional): Username to check
        email (str, optional): Email to check
    
    Returns:
        bool: True if user exists, False otherwise
    
    Example:
        if user_exists(username='john_doe'):
            print("Username already taken!")
    """
    conn = get_db_connection()
    
    try:
        if username:
            cursor = conn.execute(
                'SELECT 1 FROM users WHERE username = ?',
                (username,)
            )
            if cursor.fetchone():
                return True
        
        if email:
            cursor = conn.execute(
                'SELECT 1 FROM users WHERE email = ?',
                (email,)
            )
            if cursor.fetchone():
                return True
        
        return False
        
    except sqlite3.Error as e:
        print(f"✗ Error checking user existence: {e}")
        return False
        
    finally:
        conn.close()


# =============================================================================
# CLAUSE COMPARISON FUNCTIONS
# =============================================================================

def save_comparison(user_id, document1_id, document2_id, comparison_result):
    """
    Save a clause comparison result to the database.
    
    Args:
        user_id (int): ID of the user performing comparison
        document1_id (str): UUID of first document
        document2_id (str): UUID of second document
        comparison_result (dict): Comparison result from compare_clauses()
    
    Returns:
        bool: True if saved successfully, False otherwise
    """
    conn = get_db_connection()
    
    try:
        comparison_id = str(__import__('uuid').uuid4())
        
        conn.execute('''
            INSERT INTO comparisons 
            (id, user_id, document1_id, document2_id, comparison_result, created_at)
            VALUES (?, ?, ?, ?, ?, ?)
        ''', (
            comparison_id,
            user_id,
            document1_id,
            document2_id,
            json.dumps(comparison_result),
            datetime.now().isoformat()
        ))
        
        conn.commit()
        print(f"✓ Comparison saved: {comparison_id}")
        return True
        
    except sqlite3.Error as e:
        print(f"✗ Error saving comparison: {e}")
        return False
        
    finally:
        conn.close()


def get_comparison(user_id, document1_id, document2_id):
    """
    Get a previously saved comparison between two documents.
    
    Args:
        user_id (int): ID of the user
        document1_id (str): UUID of first document
        document2_id (str): UUID of second document
    
    Returns:
        dict: Comparison result or None if not found
    """
    conn = get_db_connection()
    
    try:
        cursor = conn.execute('''
            SELECT comparison_result FROM comparisons
            WHERE user_id = ? AND document1_id = ? AND document2_id = ?
            ORDER BY created_at DESC
            LIMIT 1
        ''', (user_id, document1_id, document2_id))
        
        row = cursor.fetchone()
        
        if row:
            return json.loads(row['comparison_result'])
        
        return None
        
    except sqlite3.Error as e:
        print(f"✗ Error retrieving comparison: {e}")
        return None
        
    finally:
        conn.close()


def get_user_comparisons(user_id, limit=20):
    """
    Get all comparisons for a user.
    
    Args:
        user_id (int): ID of the user
        limit (int): Maximum number of comparisons to retrieve
    
    Returns:
        list: List of comparison records
    """
    conn = get_db_connection()
    
    try:
        cursor = conn.execute('''
            SELECT id, document1_id, document2_id, created_at, comparison_result
            FROM comparisons
            WHERE user_id = ?
            ORDER BY created_at DESC
            LIMIT ?
        ''', (user_id, limit))
        
        rows = cursor.fetchall()
        
        comparisons = []
        for row in rows:
            comparisons.append({
                'id': row['id'],
                'document1_id': row['document1_id'],
                'document2_id': row['document2_id'],
                'created_at': row['created_at'],
                'comparison_result': json.loads(row['comparison_result'])
            })
        
        return comparisons
        
    except sqlite3.Error as e:
        print(f"✗ Error retrieving comparisons: {e}")
        return []
        
    finally:
        conn.close()


def delete_comparison(comparison_id):
    """
    Delete a saved comparison.
    
    Args:
        comparison_id (str): UUID of the comparison
    
    Returns:
        bool: True if deleted successfully
    """
    conn = get_db_connection()
    
    try:
        conn.execute('DELETE FROM comparisons WHERE id = ?', (comparison_id,))
        conn.commit()
        return True
        
    except sqlite3.Error as e:
        print(f"✗ Error deleting comparison: {e}")
        return False
        
    finally:
        conn.close()


# =============================================================================
# MODULE INITIALIZATION
# =============================================================================

# When this module is run directly (not imported), initialize the database
if __name__ == '__main__':
    print("Initializing ContractIQ Database...")
    print("=" * 50)
    init_db()
    print("=" * 50)
    print("Database setup complete!")
