package tests;

/**
 * TestRunner - DEPRECATED - Use Maven + TestNG instead
 * 
 * This class is no longer needed. Your project now uses:
 * - Maven (pom.xml) for dependency management and building
 * - TestNG (testng.xml) for running tests
 * 
 * TO RUN TESTS IN ECLIPSE:
 * 1. Right-click the ContractIQTests project
 * 2. Run As → Maven test
 * 
 * OR use command line:
 * mvn clean test
 * 
 * The testng.xml file defines which tests to run.
 */
public class TestRunner {
    
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                              ║");
        System.out.println("║         CONTRACTIQ TEST AUTOMATION - MAVEN + TESTNG          ║");
        System.out.println("║                                                              ║");
        System.out.println("║  Project now uses Maven and TestNG framework                 ║");
        System.out.println("║  DO NOT use this TestRunner class anymore                    ║");
        System.out.println("║                                                              ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("Run tests using eclipse:");
        System.out.println("1. Right-click ContractIQTests project");
        System.out.println("2. Run As → Maven test");
        System.out.println();
        System.out.println("OR use command line:");
        System.out.println("mvn clean test");
    }
}
