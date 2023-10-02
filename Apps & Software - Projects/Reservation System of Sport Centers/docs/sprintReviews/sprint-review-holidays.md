# <center> Sprint Review 5 </center>
### <center> Period: 29.12.2021 - 10.01.2022 </center>

## I. What has been done
### 1. General
The sprint was mostly focused on finishing up all of our business logic and testing the functionality.
- We have connected all services with one another, so they can now communicate properly with each other. This utilizes `Spring Eureka`
- We have refactored our code for better readability
- We have added more tests to all microservices.

### 2. Reservations
This sprint we focused on connecting Reservations to Authentication and finalizing the rest of our business logic.

- We have added an `Interceptor` class which checks whether the person behind the request is authenticated. This improves security.
- We have fully finished the `LessonAPI` class and all the business logic that is behind it.
- We have added endpoints for retrieving reservations by team and bookable IDs
- We have added CRUD endpoints for the different bookables which only admins can access.
- We have created new validators (there are now 12 in total) and refactored all business logic to make use of them.
- We have tested everything and were able to achieve 100% branch coverage as reported by `JaCoCO`, as well as 100% mutation
coverage as reported by `pitest`.

### 3. Users
*<div style="text-align: right"> Assigned: Ferhan & Alexandra</div>*
This spring was dedicated to finishing up the business logic and connecting Users to Authentication.

- We have added the ability for users to add and remove other users from their team. If a team remains with 0 users, the
  team is automatically deleted.
- We have added a default team for people who have just registered. Only they are in that team.
- We have refactored the builder patterns so that `PMD` does not complain about them.
- We have added functionality for retrieving all teams for a specific user.
- We have established the communication between Users and Authentication using Interceptors (in a similar fashion to the
  Reservations microservice).

### 4. Authentication
*<div style="text-align: right"> Assigned: Luuk & Jannick </div>*
This week we mainly worked on functionality and testing.

- We have established communication with the Users microservice so that it can create a user with the same UUID in
  the Users microservice. User deletion is now also synchronised in this way.
- We have refactored the code to be consistent with the other microservices.
- We have started and finished testing of the Authentication microservice.


<br>
<div style="text-align: right">
<p>Date compiled: 09-01-2021</p>
Compiled by: Bozhidar Andonov <br>
Approved by: Alexandra-Ioana Negau, Jannick Weitzel, Tudor-George Popica, Luuk van de Laar and Ferhan Yildiz
</div>
