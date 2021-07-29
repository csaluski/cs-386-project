# Verification and Validation

Group 2 - "Pulp"

Date: 2021-07-29

## 1. Introduction

Pulp is a web based platform to assist individuals performing literature research to save time and reduce frustration.
By providing a service to sort their literature searches, Pulp will help researchers keep the important information at
their fingertips instead of having to re-search it.

Pulp is open source at https://github.com/Csaluski/cs-386-project

## 2. Verification

### 2.1 Unit Tests

1. We used JUnit for Unit Tests.
2. https://github.com/Csaluski/cs-386-project/tree/main/src/test/java/edu/nau/cs386/manager
3. [Object being tested](https://github.com/Csaluski/cs-386-project/blob/main/src/main/java/edu/nau/cs386/model/User.java). [Use of mock objects](https://github.com/Csaluski/cs-386-project/blob/main/src/test/java/edu/nau/cs386/manager/UserManagerTest.java#L18).

### 2.2 Integration Tests

1. We used a combination of JUnit and Vertx' test framework for Integration Tests.
2. https://github.com/Csaluski/cs-386-project/tree/main/src/test/java/edu/nau/cs386/test/integration
3. https://github.com/Csaluski/cs-386-project/blob/main/src/test/java/edu/nau/cs386/test/integration/IntegrationTest.java
    These integration tests exercise the web router's responses to GET and POST requests for every path.

### 2.3 Acceptance Tests

1. We used the testing framework Selenium IDE for our tests, as it has a very easy to use GUI editor that enabled us to
   quickly pick up the platform.
2. [Selenium IDE project file.](https://github.com/Csaluski/cs-386-project/blob/b013757afed9fd221841311b5e466fc95bcbeace/src/test/selenium/Pulp-papers.side)
   Unfortunately these are not automated, as we had many issues while trying to set up an automated framework.
3. The actions for the task to test the login begin
   on [line 8](https://github.com/Csaluski/cs-386-project/blob/b013757afed9fd221841311b5e466fc95bcbeace/src/test/selenium/Pulp-papers.side#L8)
   of the previous file. This test logs the user in and out of the application.
4. ![](deliverable7/acceptance.png)

## 3. Validation

Script:

Questions After Demonstration:

    1. How was the functionality of the site?
    2. Opinions on Features:
    3. On a scale of 1 to 10 (1 being never, 10 being everyday) how often could you see yourself using this project?
    4. Would you use this product in it's current state?
    5. If not why?/what would it need to be useable?
    6. Any questions?
    7. Closing thoughts on the project?


Results:

We performed three followup interviews with:

        * D. Karp
             -Research Assistant Head

        * M. Mahmoudi
             -PHd Candidate

        * C. Weber
             -Peer Review Researcher
The variation the three had in their specific professional titles gave confidence that we were seeing our project from different angles. Would Use Pulp as is two of them would not use it, one would, in its current state. All three would use pulp if it was further developed. However, each interviewee had their own desired features.


Reflections:

Working with individuals who are in research fields have high standards on their tools. In assuming that all researchers have similiar needs in organization and communication, we didn't acknowledge the specific subgroups of our target clients. For this projects, if we had tightened our scope we may have been able to deliver a more viable product for one subgroup. What our interviewees did like was the concept of the project and the ability to sort papers by tags. However, one interviewee found no value in the collaborative features and the other two found it to be the most important.

If trying to please all our interviewees, some features we would latter implement to address user concerns would include commenting, threads, secure group sharing, task assignment, annotations, and tagging specific PDF sections. It would be an important step to the development of the product to decide which implementations would be the most important to pursue.

In the application of our project the features that worked well were the creation of tags, user profile, and uploading a paper. In application, the viewing of an uploaded PDF needs improvement before useful application. The learning curve of our current system is very little. Our system currently is very in the alpha stages and streamlined. The users had very few options to chose from, so there were no surprises in the user's uses of the project. The users found that actions of the system were intuitive and what was expected, just too simple to be overly simplified. The value proposition wasn't fully completed, but we are able to get a solid start toward. Further work and funding would be necessary to fully fulfill our value proposition.
