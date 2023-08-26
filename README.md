# Pet Store API Test Automation

This repository contains automated tests for the Pet Store API using Java, Maven, Rest-Assured, TestNG, and Extent Reports.

## Technologies Used
- Java: Chosen as the primary programming language due to its widespread adoption and versatility in test automation.
- Maven: Used as the build tool for efficient dependency management, project setup, and integration with CI/CD pipelines.
- Rest-Assured: Employed for API testing due to its simplicity, readability, and comprehensive documentation.
- TestNG: Selected as the test framework for its flexibility, parallel test execution capabilities, and rich reporting features.
- Extent Reports: Utilized for generating detailed and visually appealing test reports for easy result analysis.

## Approach
For the automation of the Pet Store API tests:

I started by gaining a deep understanding of the API's functionalities, designed test cases to cover different scenarios, and then implemented the automation framework. I used Rest-Assured to interact with the API and TestNG to organize and execute the tests. As the API evolved, I kept my tests updated to ensure reliable and meaningful results.
## Setup and Execution

## Pre-condition

Before running the tests, make sure you have set up the Swagger Petstore API server as described in the [Swagger Petstore README](https://github.com/swagger-api/swagger-petstore).

## Repository Setup

* **Clone the Repository:**

   ```sh
   git clone https://github.com/bufaale/Java-Visa.git
   cd Java-Visa
  ```
  
* **Running Tests**:

   To run the tests, you can use Maven from the project's root directory:

   `mvn clean test`

   This command will execute all the tests in the project.

## Generating HTML Test Reports:

Test reports are generated using Extent Reports. You can find the HTML reports in the test-output directory after running the tests.

**Viewing the Report**

To view the report, simply open a web browser (such as Chrome, Firefox, etc.) and navigate to the path of the ExtentReport.html file mentioned above. This will open the report in your browser, where you can see detailed execution details of each test case, including results, times, and more.

## Running Individual Test Classes

You can run individual test classes using Maven. Replace TestClass with the actual name of the test class you want to run.

`mvn clean test -Dtest=TestClass`

For example, to run the UserTests class:

`mvn clean test -Dtest=UserTests`

Similarly, you can run specific test methods within a test class by specifying the method name using the test parameter:

`mvn clean test -Dtest=TestClass#testMethodName`

For example, to run the testCreateUser method from the UserTests class:

`mvn clean test -Dtest=UserTests#testCreateUser`

## Running Test Suites Using TestNG XML
The project includes TestNG XML configuration files (testng.xml) that define test suites and their corresponding test classes. Here's how you can run the test suites using these XML files:

Running a Specific Test Suite:

To run a specific test suite defined in the testng.xml file, you can use Maven from the project's root directory:

`mvn clean test -DsuiteXmlFile=testng.xml
`

## Running Tests from the Executable JAR

You can also run the tests using the generated executable JAR:

`java -jar target/Java-Visa-1.0-SNAPSHOT-jar-with-dependencies.jar`

You can also run individual test using the generated executable JAR:

`java -jar target/Java-Visa-1.0-SNAPSHOT-jar-with-dependencies.jar com.petstore.tests.UserTests`

You can also run TestNG test suites using the generated executable JAR:

`java -jar target/Java-Visa-1.0-SNAPSHOT-jar-with-dependencies.jar path/to/testng.xml`

remember replace the path where there is testng.xml file