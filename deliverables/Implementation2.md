# Implementation 2

Group 2 - "Pulp"

Date: 2021-07-26

## 1. Introduction

Pulp is a web based platform to assist individuals performing literature research to save time and reduce frustration.
By providing a service to sort their literature searches, Pulp will help researchers keep the important information at
their fingertips instead of having to re-search it.

Currently, Pulp has a profile feature that allows users to keep track of their information. User may upload papers and
view them in the browser. There are fields to store the paper's pertinent information, including author, abstract, and
DOI. After viewing a paper, users may associate the paper with an existing tag or create a new tag to personalize the
sorting of their papers. User may view a tag to review all associated papers, thus reducing the time a user has to
search for information they previously reviewed. In case of any mistakes or if a user changes their mind on how they
want to note something, user profiles, paper details, and tag information are all able to be edited.

Pulp is open source at https://github.com/Csaluski/cs-386-project

## 2. Implemented requirements

Requirement: As a paper writer, I want to be able to log in and out of my account at will. Also I want to be able to
tags to be able to help readers understand what my paper is about before they read. \
Pull Request(s): #62- Fixing user manager import and cleaning up main verticle;
link: https://github.com/Csaluski/cs-386-project/pull/62, \
65-Updated Cookies with a working edi HBS page. put cookies in a function  \
link:https://github.com/Csaluski/cs-386-project/pull/65, \
68-current progress on D5 part 6 \
link:https://github.com/Csaluski/cs-386-project/pull/68, \
71-Adding logout functionality \
link:https://github.com/Csaluski/cs-386-project/pull/71, \
72-Main Verticle refactor \
link:https://github.com/Csaluski/cs-386-project/pull/72, \
73-Start of tags and small logout features \
link: https://github.com/Csaluski/cs-386-project/pull/73 \
Implemented by: Jonathan Boal and Christopher Murphy \
Approved by: Jadon Fowler

Requirement: As a websurfer, I want the website that I'm on to be easy to use and good to look at. \
Pull Request(s): #74- Feature/tags html; link: https://github.com/Csaluski/cs-386-project/pull/74 \
Implemented by: Hannah Larreau \
Approved by: Jadon Fowler

Requirement: As a researcher, I want my account information to be saved on a database so that my interests are more
easily met. \
Pull Request: #75- Feature/database update link: https://github.com/Csaluski/cs-386-project/pull/75 \
Implemented by: Charles Saluski \
Approved by: Jadon Fowler

## 3. Demo

link to demo: https://youtu.be/D9IswvDrJCU

## 4. Code quality

We refactored our MainVerticle to follow a consistent pattern for every GET/POST Request handler. We also adopted a
Manager pattern for the data objects we are dealing with, making every Database interaction we have follow the same
convention. We used the default formatter in IntelliJ to keep all the code looking similar.

## 5. Lessons learned

Our biggest problem was choosing technologies that were difficult to use. Using PostgreSQL and Docker turned out to be
challenging. A simpler language and smaller libraries would have made creating the website faster. We are still working
on being comfortable merging big git branches.
