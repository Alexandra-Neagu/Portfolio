# <center> Sprint Review 4 </center>
### <center> Period: 22.12.2021 - 28.12.2021 </center>

## I. What has been done
### 1. General

* We connected the User and the Reservation microservices with Spring Eureka
* We've implemented testing for some newly created functionality.
* We've implemented two design patterns: **Builder Pattern** (in Users) and **Chain of Responsibility** (in Authentication).

### 2. Reservations
*<div style="text-align: right"> Assigned: Tudor & Bozhidar</div>*
We focused mainly on connecting the User and Reservation microservices (See 3. Users) and the Lesson API as well as the testing.

* **General**
    * We've made a lot of progress on the Lesson API as well as the testing for it.
    * We've connected the User and Reservation Microservices.

* **APIs**
    * Created a large part of the required functionality for the Lesson API.

### 3. Users
*<div style="text-align: right"> Assigned: Ferhan & Alexandra</div>*
This week we mainly took the time to connect the User and Reservation microservices.

* **General**
    * We've added endpoints: canTeamBook, getTeamSize, addReservationForTeam, addUserToTeam, etc.
    * We've added business logic and helper methods to the services.

* **Entities**
    * We created a separate entity to make us able to store relevant reservation info in our User database.

### 4. Authentication
*<div style="text-align: right"> Assigned: Luuk & Jannick </div>*
This week we mainly worked on the implemetation side of things.

* We've implemented the ability for users to register.
* We've implemented the ability to delete your account.
* We've implemented the ability to change your password.

## II. What is planned to be done for *Sprint 4*

### 1. General
Our main focus now is to finish creating a connection between the microservices and also to create integration testing for these connections.

### 2. Reservations
* Finish the Lesson API
* Finish the testing of the Lesson API

### 3. Users
* Delete expired reservations from the User's reservation tracker
* Integration testing
* Setup automatic integration testing
* Fix the PMD warnings for the Builders by doing "withName" instead of "name"

### 4. Authentication
* Connect User registration to the Users microservice
* Same for account deletion
* Create an activation key which will generate an admin account.
* Create the ability for (only) admin accounts to create other admin accounts.


<br>
<div style="text-align: right">
<p>Date compiled: 30-12-2021</p>
Compiled by: Ferhan Yildiz <br>
Approved by: Alexandra-Ioana Negau, Ferhan Yildiz, Tudor-George Popica, Luuk van de Laar and Bozhidar Andonov
</div>
