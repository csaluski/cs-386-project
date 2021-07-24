# Project Inception

Group 02 - "Pulp"

Date: 2021-07-23

## 1. Description

## 2. Architecture

![Architecture of Pulp system](deliverable5/Architecture.png)
We use a layered architecture that is effectively MVC for out system, with a web layer that renders our pages
functioning as our view layer, managers that conduct business logic and talk to our database for the controllers, and
the data objects that make up our model in the data layer.

## 3. Class diagram

![Class diagram of Pulp system](deliverable5/ClassDiagram.png)

## 4. Sequence Diagram

![Sequence diagram of account creation](deliverable5/Sequence-Request_Account_Creation.png)

Description: A user comes to our web site with the objective of creating an account

Preconditions: The user is on the website.

Postcondions: The user has setup an account.

Main Flow:

1. The user is on our main page
2. The user clicks the create account page
3. The system sends the user the account creation page
4. The user enters their display name and email and clicks the create account button
5. The system takes the user to their account page

Alternate Flow: From 4, the user's account is invalid.

1. The system tells the user that an error occured in their submission and the type of error, and takes them back to the
   account creation page.

## 5. Design Patterns

### 5.1 Singleton

### 5.2 Facade

## 6. Design Principles


