#!/usr/bin/env python
"""
Test the comparison API directly to see what it returns.
"""

import requests
import json

# Configuration
API_BASE_URL = "http://localhost:5000/api"

def test_comparison():
    """Test comparison between documents 7 and 8."""
    print("\n" + "=" * 80)
    print("  TESTING COMPARISON API")
    print("=" * 80)
    
    # Test with documents 7 and 8 (Comp1 and comp2)
    data = {
        "document1_id": 7,
        "document2_id": 8
    }
    
    print(f"\nComparing Document 7 (Comp1) vs Document 8 (comp2)")
    print(f"Payload: {json.dumps(data, indent=2)}")
    
    try:
        response = requests.post(
            f"{API_BASE_URL}/compare",
            json=data,
            headers={"Content-Type": "application/json"},
            cookies={}  # No cookies needed for session in test
        )
        
        print(f"\nResponse Status: {response.status_code}")
        print(f"Response Headers: {dict(response.headers)}")
        
        resp_data = response.json()
        print(f"\nResponse JSON:")
        print(json.dumps(resp_data, indent=2))
        
        # Check key values
        if resp_data.get('success'):
            comparison = resp_data.get('comparison', {})
            summary = comparison.get('summary', {})
            
            print(f"\n{'─' * 80}")
            print("SUMMARY RESULTS:")
            print(f"  Overall Similarity: {summary.get('overall_similarity', 'N/A')}%")
            print(f"  Matching Categories: {summary.get('matching_categories_count', 0)}")
            print(f"  Different Clauses: {summary.get('different_clauses_count', 0)}")
            print(f"  Risk Flags: {summary.get('risk_flags_count', 0)}")
            
            matching = comparison.get('matching_categories', [])
            print(f"\nMatching Categories: {matching}")
            
        else:
            print(f"\n✗ API Error: {resp_data.get('error')}")
        
    except Exception as e:
        print(f"✗ Request failed: {e}")

if __name__ == '__main__':
    test_comparison()
