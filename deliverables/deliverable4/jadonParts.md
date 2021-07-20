## Deliverable 4.1: Introduction

needs a paragraph and these links:
https://github.com/Csaluski/cs-386-project
https://github.com/Csaluski/cs-386-project/projects/1

## Deliverable 4.3: Adopted Technologies

> List the adopted technologies with a brief description and justification for choosing them.
>
> Grading (2 points): This section will be evaluated in terms of correctness, completeness, thoroughness, consistency, coherence, and adequate use of language

We have a Web Server built in Java using Vert.x. For a database, we use
PostgreSQL and connect to it using JDBC. For the frontend, we use HTML + CSS +
JS and we use Handlebars templates to insert data into the HTML. To build
the application, we use the Gradle build system. Docker is used for
packaging the webapp. GitHub Actions is used for CI & CD to build and deploy
the webapp to Heroku.

## Deliverable 4.4: Learning

Jadon gave lectures every night for each part of the website that needed to
be created. This includes the webserver, how the webserver handles different
routes, how those routes run Java lambdas, how Java lambdas work, how
templates work, and how Gradle builds everything.

## Deliverable 4.6: Licensing

We have decided to use the MIT License as it is a very simple license that
lets people do almost anything with the project, such as forking it or
creating copies of it.

## Deliverable 4.7: Readme File

This is actually not just the readme. This is the existence of the Readme,
Contributing, Code of Conduct, and License.

## Deliverable 4.8: Look & Feel

The user interface was made to feel like ...

## Deliverable 4.9: Lessons Learned

This project turned out to be way more work than we initially guessed.
The majority of time spent was teaching the team everything they needed
to know about how webservers working, from top to bottom. Jadon discussed
how Vert.x works, how Java programs works, how Gradle works, and how paths
on the webserver work, with all the gaps between those. A simpler project
would have taken less time to implement so we weren't scrambling to get basic
functionality completed.
