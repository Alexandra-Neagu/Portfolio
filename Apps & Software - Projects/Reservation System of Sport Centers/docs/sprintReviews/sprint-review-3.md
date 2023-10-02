# <center> Sprint Review 3 </center>
### <center> Period: 08.12.2021 - 14.12.2021 </center>

## I. What has been done
### 1. General

* A third database has been created. Meaning some things have changed, since this wasn't the initial plan. Those changes were mainly in Authentication, but also in Users.
* We've implemented a lot of testing (also edge cases) to eliminate bugs.
* We've implemented two design patterns: **Builder Pattern** (in Users) and **Chain of Responsibility** (in Authentication).

### 2. Reservations
*<div style="text-align: right"> Assigned: Bozhidar & Tudor </div>*
We focused on finalizing testing for what we had implemented this far, we also took a look at implementing sports facilities.

* **General**
    * We started working on Sports Facilities. Here the foundation has been implemented now.
    * We worked on assignment 1 and were able to finalize the document for that.

* **Services**
    * We finished testing the equiment service. We achieved around 99% JaCoCo branch and mutation testing coverage. 

* **APIs**
    * We finished testing the equiment API. We also achieved around 99% JaCoCo branch and mutation testing coverage here.

### 3. Users
*<div style="text-align: right"> Assigned: Alexandra & Ferhan </div>*
This week we took a second look at the current fields our database has and worked on a design pattern for assignment 1.

* **General**
    * We wrote the Assignment 1 part for the Builder Pattern.

* **Entities**
    * We removed Email and password from the Users database. These are now stored in the authentication database.
    * We implemented the builder pattern for the User and Team entity.
    * We surpressed PMD warnings for both the User and Team entity. It complains that the User builder has the same name for the fields and the methods (for name and hasPremium), but this is part of the builder pattern idea.
    * We added the *Reservations* field to the User and created some business logic for it.

### 4. Authentication
*<div style="text-align: right"> Assigned: Luuk & Jannick </div>*
This weeks focus was implementing our new approach and get a working authentication service in place. We also looked at jwt tokens for authentication between different microservices.

* We created a seperate Authentication database, with PostgreSQL.
* We linked the authentication to Spring Security, so Spring can check the database for matching user credentials.
* We added a function to generate jwt tokens (based on user credentials, so that password can be extracted from the token if you have the correct information).
* We added a function that intercepts all requests so that it can validate the token that's part of the request.

## II. What is planned to be done for *Sprint 4*

### 1. General
Our main focus now is to create a connection between the microservices and make sure all current functionality is tested well.

### 2. Reservations
* We have to start and finish working on the SportsEquipment business logic and API testing.
* We have to add some functionality to the ReservationsAPI.
* We have to start creating a connection between the Reservations and Users microservices. 
* We have to implement integration testing to test this connection.
* We probably have to refractor some common functionality when equipment and sports facilities are merged.

### 3. Users
* We have to check if users are allowed to make a reservation, based on their subscription, but to get a User's reservation, we first need the Reservation microservice to be connected.
* We have to add a *List< Team >/List< UUID >* for the User.
* We have to start creating a connection between the Users and Reservations microservices. 
* We have to implement integration testing to test this connection.

### 4. Authentication
* We have to start testing the functionality created in the previous sprint.
* We are going to improve scalability by removing *email* as primary key.
* We should add a way to register a new user to easily add new users to the database.
* We should add a way to change email and password.


<br>
<div style="text-align: right">
<p>Date compiled: 19-12-2021</p>
Compiled by: Jannick Weitzel <br>
Approved by: Alexandra-Ioana Negau, Ferhan Yildiz, Tudor-George Popica, Luuk van de Laar and Bozhidar Andonov
</div>