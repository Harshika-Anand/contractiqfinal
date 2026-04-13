#!/usr/bin/env python
"""
Direct test of comparison function to see what data is returned.
"""

import json
import sqlite3
from database import get_db_connection
from clause_extractor import compare_clauses

def test_comparison_direct():
    """Test the compare_clauses function directly."""
    print("\n" + "=" * 80)
    print("  DIRECT COMPARISON FUNCTION TEST")
    print("=" * 80)
    
    try:
        conn = get_db_connection()
        cursor = conn.cursor()
        
        # Get documents 7 and 8
        cursor.execute("SELECT id, original_filename, clauses FROM documents WHERE id IN (7, 8)")
        documents = cursor.fetchall()
        
        if len(documents) != 2:
            print(f"✗ Could not find both documents (found {len(documents)})")
            return
        
        doc1 = documents[0]
        doc2 = documents[1]
        
        print(f"\nDocument 1: ID={doc1[0]}, Name={doc1[1]}")
        print(f"Document 2: ID={doc2[0]}, Name={doc2[1]}")
        
        # Parse clauses
        try:
            clauses1 = json.loads(doc1[2]) if isinstance(doc1[2], str) else doc1[2]
            clauses2 = json.loads(doc2[2]) if isinstance(doc2[2], str) else doc2[2]
        except:
            print("✗ Failed to parse clauses JSON")
            return
        
        print(f"\nClause Data Loaded:")
        print(f"  Doc1 Categories: {list(clauses1.keys())}")
        print(f"  Doc2 Categories: {list(clauses2.keys())}")
        
        # Show what clauses are present
        print(f"\n  Doc1 clauses per category:")
        for cat, clauses in clauses1.items():
            if clauses:
                print(f"    {cat}: {len(clauses)} clause(s)")
        
        print(f"\n  Doc2 clauses per category:")
        for cat, clauses in clauses2.items():
            if clauses:
                print(f"    {cat}: {len(clauses)} clause(s)")
        
        # Call comparison
        print(f"\n{'─' * 80}")
        print("CALLING compare_clauses()...")
        result = compare_clauses(clauses1, clauses2)
        
        print(f"\nComparison Result:")
        print(json.dumps(result, indent=2))
        
        # Detailed analysis
        summary = result.get('summary', {})
        print(f"\n{'─' * 80}")
        print("SUMMARY ANALYSIS:")
        print(f"  Overall Similarity: {summary.get('overall_similarity')}%")
        print(f"  Total Categories: {summary.get('total_categories')}")
        print(f"  Matching Categories: {summary.get('matching_categories_count')}")
        print(f"  Different Clauses: {summary.get('different_clauses_count')}")
        print(f"  Missing in Doc2: {summary.get('missing_in_second_count')}")
        print(f"  New in Doc2: {summary.get('new_in_second_count')}")
        print(f"  Risk Flags: {summary.get('risk_flags_count')}")
        
        matching = result.get('matching_categories', [])
        print(f"\nMatching Categories: {matching}")
        
        conn.close()
        
    except Exception as e:
        print(f"✗ Error: {e}")
        import traceback
        traceback.print_exc()

if __name__ == '__main__':
    test_comparison_direct()
