## Deliverable 4.1: Introduction

Pulp is a collaboration platform that enables academics to be more effective in their literary research by enabling more effective searching and collaborative annotations. The current system allows you to login, edit your account, and  view papers. Future systems will allow you more customizable options on profile settings, uploading and saving papers to your user account, and a search engine to help you filter through papers. Future goals also include an annotation, and a group environment setting to help your research team communicate information more efficiently.

https://github.com/Csaluski/cs-386-project
https://github.com/Csaluski/cs-386-project/projects/1

## Deliverable 4.2: Implemented Requirements

Requirement: As a paper writer, I want to create an account on a website in order to access papers.
Pull Request: #51- https://github.com/Csaluski/cs-386-project/pull/51
Implemented by: Jonathan Boal and Christopher Murphy
Approved by: Charles Saluski

Requirement: As a researcher, I want to be able to access reading material online.
Pull Request: #53-https://github.com/Csaluski/cs-386-project/pull/53
Implemented by: Hannah Larreau
Approved by: Charles Saluski

Requirement: As a researcher I want a website that finds papers based on my profile's interests and have quick access to papers that I like.
Pull Request: #49- https://github.com/Csaluski/cs-386-project/pull/49
Implemented by: Jaden Fowler
Approved by: Charles Saluski

Requirement: As a researcher, I want my account information to be saved on a database so that my interests are more easily met.
Pull Request: #55- https://github.com/Csaluski/cs-386-project/pull/55
Implemented by: Charles Saluski
Approved by: Charles Saluski

Requirement: As an internet surfer, I want the website that I'm viewing to be pleasing to view.
Pull Request: #56-https://github.com/Csaluski/cs-386-project/pull/56
Implemented by: Mina Hemaia and Gemiana Shawky
Approved by: Charles Saluski

## Deliverable 4.3: Adopted Technologies

> List the adopted technologies with a brief description and justification for choosing them.
>
> Grading (2 points): This section will be evaluated in terms of correctness, completeness, thoroughness, consistency, coherence, and adequate use of language

We have a Web Server built in Java using Vert.x. For a database, we use PostgreSQL and connect to it using JDBC. For the frontend, we use HTML + CSS + JS and we use Handlebars templates to insert data into the HTML. To build the application, we use the Gradle build system. Docker is used for packaging the webapp. GitHub Actions is used for CI & CD to build and deploy the webapp to AWS.
We have a Web Server built in Java using Vert.x. For a database, we use
PostgreSQL and connect to it using JDBC. For the frontend, we use HTML + CSS +
JS and we use Handlebars templates to insert data into the HTML. To build
the application, we use the Gradle build system. Docker is used for
packaging the webapp. GitHub Actions is used for CI & CD to build and deploy
the webapp to Heroku.

## Deliverable 4.4: Learning / Training

Many of our group members had little to no experience with the vert.x and server side implementation of code. This made the beginning of working on our implementation a little slow, but we gradually sped up our work rate as the week went on. Jadon gave lectures every night for each part of the website that needed to
be created. This includes the webserver, how the webserver handles different
routes, how those routes run Java lambdas, how Java lambdas work, how
templates work, and how Gradle builds everything.

## Deliverable 4.5: Deployment

Initially we wanted to use AWS for our deployment, however for this initial deployment we decided to use Heroku, as we do not currently rely on a database or other external services.
This required less setup and only changing our code in a very small way to enable passing a port for the config.
The app is currently hosted [here](https://pulp-papers.herokuapp.com/), we have not yet purchased a real domain.

## Deliverable 4.6: Licensing

We have decided to use the MIT License as it is a very simple license that lets people do almost anything with the project, such as forking it or creating copies of it. We have decided to use the MIT License as it is a very simple license that
lets people do almost anything with the project, such as forking it or
creating copies of it.

## Deliverable 4.7: Readme File

This is actually not just the readme. This is the existence of the Readme,
Contributing, Code of Conduct, and License.

## Deliverable 4.8: Look and Feel

While we did create CSS code we did not leave ourselves enough time to implement it. For future implementations we want quick transportation between the pages on our site and to use and improve on the CSS code that we made to make the website look more pleasing.

## Deliverable 4.9: Learning Lessons

One of the first lessons that we learned while setting up the website was that early and clear communication was paramount. Many issues that stalled us could have been settled sooner had communicated a little more efficiently.

This project turned out to be way more work than we initially guessed.
The majority of time spent was teaching the team everything they needed
to know about how webservers working, from top to bottom. Jadon discussed
how Vert.x works, how Java programs works, how Gradle works, and how paths
on the webserver work, with all the gaps between those. A simpler project
would have taken less time to implement so we weren't scrambling to get basic
functionality completed.

## Deliverable 4.10: Demo

https://youtu.be/7XB-mzYoeCE
