# <center> Integration Testing </center>

## 1. Overview
Integration tests collect microservices together in order to verify that they **collaborate** 
as intended, achieving a larger piece of business logic. Also, it tests the communication
path between the microservices to check for any incorrect assumptions each microservice has 
about how to interact with its peers.

In the context of the project, we are using integration testing to test that the connection 
between the *Reservations*, *Users*, and *Authentication* microservices works as intended.

## 2. Setup
To make sure all tests run as intended, make sure that these preconditions are satisfied:
   
### PostgreSQL 
Make sure you have **PostgreSQL 13** installed on your machine and that the username 
and password are both set to `postgres`.

### Databases
If you have not already, add 3 databases to that default configuration - `authentication`,
`users` and `reservations`. Make sure that their names match exactly, otherwise you will 
run into problems when you are executing these tests (and the application, in general).

### Communication
Communication between microservices employs `Spring Eureka`. In essence, this technology
handles the complexity of communication and load balancing, allowing for better abstraction
when microservices need to cooperate with each other. With this in mind, make sure that the 
`DiscoveryServerApplication`, located in `discovery-server/src/main/java/discovery/`, is up
and running before executing any of the tests. If you run into any problems with communication
(that were not specified as a desired outcome of the test) please restart this application first.
Also make sure that you give some time to the other microservices to load before accessing any
of the API endpoints, as registering with the server can take some time.

### Microservices
Before attempting any of these tests, please make sure that all the **Reservations**, **Users** and **Authentication** microservices
are running (unless the test requires otherwise). To do so, run `ReservationsApplication` in `reservations/src/main/java/reservations`,
`UsersApplication` in `users/src/main/java/users` and `AuthneticationApplication` in `authentication/src/main/java/authentication`.

## 3. Tests
The tests below exercise the communication between pairs of services only. For global tests that include
all microservices, please take a look at the functional testing document. It is located in the same folder as 
this one.

If you are unsure about a step, please check out the *Appendix* at the end of this document, which gives some
insight on how to add and remove different entities.

### 3.1 Tests for communication between Reservations and Users
Make sure that you have authenticated as a normal user and **not** an admin one. Make sure to save the `JW token` somewhere as it will be needed.
If no users exists in the database, add some by registering and log in with the registered username and password afterwards. If you are unsure
how to do any of this, you can take a look at the *Appendix*.

Communication between the Reservations and Users microservices is done when a user tries to make or delete a 
reservation for a certain lesson/equipment/sports facility. Therefore, these will be the focus of the tests
in `3.1`.

*Disclaimer: **Reservations** does not communicate with **Users** when deleting equipment. Therefore that part is not tested.*

#### 3.1.1 Booking equipment throws an exception when the *Users* microservice has not been started
1. Make sure that the *Reservations* and *Authentication* services are running, but the *Users* one is **not**.

2. The endpoint that we will be accessing is `http://localhost:9091/api/v1/equipment/book`. This is a `PUT` endpoint.
   It takes the following parameters:
- `teamUuid` - make sure that you are using the ID of a team that belongs to the currently authenticated user
- `equipmentUuid` - make sure that this is the ID of an equipment that has at least 1 free item during that start time
- `startTime` - make sure that you select a valid starting time, meaning a time in the future, between `16:00`
  and `23:00`
- `amount` - make sure that the amount you select is less than or equal to the amount of pieces of equipment available
  for booking
- Add an `Authorization` header to the request with value `Bearer {token}` where `{token}` is the JW token you received
  when authenticating
- You can verify that all of these properties hold by checking the database. If no such teams or equipment exist,
  add them to the database.

3. Send the request to the server. It should return an error, with status `500 (Internal Server Error)`, because
   communication with the *Users* microservice has failed. You can verify that nothing has been saved in the
   `Reservation` table in the *Reservations* microservice, as well.

4. This concludes the test.

#### 3.1.2 Booking equipment works as intended if the team exists
1. The endpoint that we will be accessing is `http://localhost:9091/api/v1/equipment/book`. This is a `PUT` endpoint.
   It takes the following parameters:
- `teamUuid` - make sure that you are using the ID of a team that belongs to the currently authenticated user, that 
  it exists and that it can still book (i.e. each member has at least one remaining reservation for the day of that
  `startTime`).
- `equipmentUuid` - make sure that this is the ID of an equipment that has at least 1 free item during that start time
- `startTime` - make sure that you select a valid starting time, meaning a time in the future, between `16:00`
  and `23:00`
- `amount` - make sure that the amount you select is less than or equal to the amount of pieces of equipment available
  for booking
- Add an `Authorization` header to the request with value `Bearer {token}` where `{token}` is the JW token you received
  when authenticating
- You can verify that all of these properties hold by checking the database. If no such teams or equipment exist,
  add them to the database.

2. Send the request to the server. It should not return anything apart from the status code, which should be `200 (OK)`. 
   
3. You can now verify that the reservation has been saved by checking the `reservations/reservation` table. There 
   should be a reservation with the provided `startTime`, `teamUuid`, `bookableUuid` and `amount`.
   
4. Check the `users/users_reservations` table. It should not contain anything because equipment does not count towards
   the maximum number of reservations per day.

5. This concludes the test.

#### 3.1.3 Booking equipment fails if the team does NOT exist.
1. The endpoint that we will be accessing is `http://localhost:9091/api/v1/equipment/book`. This is a `PUT` endpoint.
   It takes the following parameters:
- `teamUuid` - make sure that you are using a UUID of a team that does **not** exist. You can make it up or use an online
  generator. UUIDs are of the form `xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx` where `x` is any hexadecimal digit.
- `equipmentUuid` - make sure that this is the ID of an equipment that has at least 1 free item during that start time
- `startTime` - make sure that you select a valid starting time, meaning a time in the future, between `16:00`
  and `23:00`
- `amount` - make sure that the amount you select is less than or equal to the amount of pieces of equipment available
  for booking
- Add an `Authorization` header to the request with value `Bearer {token}` where `{token}` is the JW token you received
  when authenticating
- You can verify that all of these properties hold by checking the database. If no such teams or equipment exist,
  add them to the database.

2. Send the request to the server. It should return a body that says `Team with this UUID does not exist` with a 
   status code of `400 (Bad Request)`.

3. You can verify that nothing has been saved in the`reservations/reservation` table of the *Reservations* microservice.

4. You can also verify that nothing has been saved in the `users/user_reservation` table of the *Users* microservice.
   
5. This concludes the test.

#### 3.1.4 Booking lessons fails when *Users* microservice is not available
1.  Make sure that the *Reservations* and *Authentication* services are running, but the *Users* one is **not**.
    
2. The endpoint that we will be accessing is `http://localhost:9091/api/v1/lesson/book`. This is a `PUT` endpoint.
   It takes the following parameters:
- `teamUuid` - make sure that you are using the ID of a team that belongs to the currently authenticated user, that
  it exists, that it can still book and whose size does not exceed the maximum remaining capacity for the lesson.
- `lessonUuid` - make sure that this is the ID of the lesson that has at least as many spots the size of the team
  that you have provided with the `teamUuid`.
- Add an `Authorization` header to the request with value `Bearer {token}` where `{token}` is the JW token you received
  when authenticating.
- You can verify that all of these properties hold by checking the database. If no such teams or lessons exist,
  add them to the database.

3. Send the request to the server. It should return an error, with status `500 (Internal Server Error)`, because 
   communication with the *Users* microservice has failed. You can verify that nothing has been saved in the
   `reservations/reservation` table in the *Reservations* microservice, as well.

4. This concludes the test.

#### 3.1.5 Booking lessons works as intended if all parameters are completely valid
1. The endpoint that we will be accessing is `http://localhost:9091/api/v1/lesson/book`. This is a `PUT` endpoint.
   It takes the following parameters:
- `teamUuid` - make sure that you are using the ID of a team that belongs to the currently authenticated user, that
  it exists, that it can still book and whose size does not exceed the maximum remaining capacity for the lesson.
- `lessonUuid` - make sure that this is the ID of the lesson that has at least as many spots the size of the team
  that you have provided with the `teamUuid`.
- Add an `Authorization` header to the request with value `Bearer {token}` where `{token}` is the JW token you received
  when authenticating.
- You can verify that all of these properties hold by checking the database. If no such teams or lessons exist,
  add them to the database.

2. Send the request to the server. It should not return anything apart from the status code, which should be `200 (OK)`.

3. You can now verify that the reservation has been saved by checking the `reservations/reservation` table. There
   should be a reservation with the `startTime` of the lesson, the provided `teamUuid`, a `bookableUuid`, equal
   to the `lessonUuid` provided and an `amount` equal to the size of the team, whose UUID has been given.

4. Check the `users/users_reservations` table. The number of rows added should be equal to the size of the team, and the
   `reservationUuid` should be the same across all added rows - the UUID of the reservation that has just been created.  

5. This concludes the test.

#### 3.1.6 Booking lesson fails if team does NOT exist.
1. The endpoint that we will be accessing is `http://localhost:9091/api/v1/lesson/book`. This is a `PUT` endpoint.
   It takes the following parameters:
- `teamUuid` - make sure that you are using the UUID of a team that does **not** exist. You can make it up or use an online
  generator. UUIDs are of the form `xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx` where `x` is any hexadecimal digit.
- `lessonUuid` - make sure that this is the ID of an existing lesson.
- Add an `Authorization` header to the request with value `Bearer {token}` where `{token}` is the JW token you received
  when authenticating.
- You can verify that all of these properties hold by checking the database. If no such teams or lessons exist,
  add them to the database.

2. Send the request to the server. It should return a body that says `Team with this UUID does not exist` with a
   status code of `400 (Bad Request)`.

3. You can verify that nothing has been saved in the`reservations/reservation` table of the *Reservations* microservice.

4. You can also verify that nothing has been saved in the `users/user_reservation` table of the *Users* microservice.

5. This concludes the test.

#### 3.1.7 Booking lesson fails if the team can NOT book.
1. The endpoint that we will be accessing is `http://localhost:9091/api/v1/lesson/book`. This is a `PUT` endpoint.
   It takes the following parameters:
- `teamUuid` - make sure that you are using the ID of a team that belongs to the currently authenticated user, that
  it exists and whose size does not exceed the maximum remaining capacity for the lesson. Ensure that some member of this team
  can no longer book under their current subscription. You can do this by adding other lesson or sports facility
  reservations beforehand.
- `lessonUuid` - make sure that this is the ID of the lesson that has at least as many spots the size of the team
  that you have provided with the `teamUuid`.
- Add an `Authorization` header to the request with value `Bearer {token}` where `{token}` is the JW token you received
  when authenticating.
- You can verify that all of these properties hold by checking the database. If no such teams or lessons exist,
  add them to the database.

2. Send the request to the server. It should return a body that says `Team with this UUID cannot book` with a
   status code of `400 (Bad Request)`.

3. You can verify that nothing has been saved in the`reservations/reservation` table of the *Reservations* microservice.

4. You can also verify that nothing has been saved in the `users/user_reservation` table of the *Users* microservice.

5. This concludes the test.

#### 3.1.8 Booking lesson fails if the team size is NOT within the size constraints of the lesson.
1. The endpoint that we will be accessing is `http://localhost:9091/api/v1/lesson/book`. This is a `PUT` endpoint.
   It takes the following parameters:
- `teamUuid` - make sure that you are using the ID of a team that belongs to the currently authenticated user, that
  it exists, that it can still book but whose size **exceeds** the remaining capacity for the lesson.
- `lessonUuid` - make sure that this is the ID of the lesson that has fewer spots than the size of the team
  that you have provided with the `teamUuid`.
- Add an `Authorization` header to the request with value `Bearer {token}` where `{token}` is the JW token you received
  when authenticating.
- You can verify that all of these properties hold by checking the database. If no such teams or lessons exist,
  add them to the database.

2. Send the request to the server. It should return a body that says `There are not enough spots left` with a
   status code of `400 (Bad Request)`.

3. You can verify that nothing has been saved in the`reservations/reservation` table of the *Reservations* microservice.

4. You can also verify that nothing has been saved in the `users/user_reservation` table of the *Users* microservice.
   
5. This concludes the test.

#### 3.1.9 Remove reservation for lesson also deletes the corresponding reduced reservations in the users microservice
1. The endpoint that we will be accessing is `http://localhost:9091/api/v1/lesson/deleteBooking`. This is a `PUT` endpoint.
   It takes the following parameters:
- `reservationUuid` - make sure that this is a Uuid of a reservation for a lesson that exists.
- `teamUuid` - make sure that you are using the UUID of the team that made the reservation with that `reservationUuid`
- Add an `Authorization` header to the request with value `Bearer {token}` where `{token}` is the JW token you received
  when authenticating.
- You can verify that all of these properties hold by checking the database. If no such teams or lessons exist,
  add them to the database.

2. Send the request to the server. It should not return anything apart from the status code, which should be `200 (OK)`.

3. You can verify that the reservation with the provided UUID has been deleted from the`reservations/reservation` 
   table of the *Reservations* microservice.

4. You can also verify that the corresponding `ReducedReservation` entity has been deleted from the 
   `users/reduced_reservation` table of the *Users* microservice and that there are no rows with that `reservationUuid`
   reservationUuid anymore in the `users/users_reservations` table.

5. This concludes the test.

#### 3.1.10 Delete lesson also deletes the corresponding reservations in the users microservice
1. For this test, make sure that you are authenticated as an **admin**, instead of a regular user. Remember to save the
   JW token somewhere.
2. The endpoint that we will be accessing is `http://localhost:9091/api/v1/lesson/delete`. This is a `DELETE` endpoint.
   It takes the following parameters:
- `lessonUuid` - make sure that this is the UUID of a lesson that exists and that it has **at least** 1 reservation.
- Add an `Authorization` header to the request with value `Bearer {token}` where `{token}` is the JW token you received
  when authenticating.
- You can verify that all of these properties hold by checking the database. If no such teams or lessons exist,
  add them to the database.

2. Send the request to the server. It should not return anything apart from the status code, which should be `200 (OK)`.

3. You can verify that the lesson entity has been successfully deleted from `reservations/lesson` table in the *Reservations*
   microservice. Any corresponding reservations in `reservations/reservation` should also be gone.
   
4. You can also verify that reservations in the `users/reduced_reservation` table of the *Users* microservice
   have been removed.

5. This concludes the test.

#### 3.1.11 Booking sports facility fails when the *Users* microservice is not available
1.  Make sure that the *Reservations* and *Authentication* services are running, but the *Users* one is **not**.

2. The endpoint that we will be accessing is `http://localhost:9091/api/v1/facility/book`. This is a `PUT` endpoint.
   It takes the following parameters:
- `teamUuid` - make sure that you are using the ID of a team that belongs to the currently authenticated user, that
  it exists, that it can still book and whose size is within the capacity constraints for the sports facility.
- `facilityUuid` - make sure that this is the ID of the sports facility, whose minimum capacity is smaller or equal to
  the team size and whose maximum capacity is bigger or equal to the team size.
- `startTime` - make sure you are using a starting time that is in the future, and between `16:00` and `23:00`
- Add an `Authorization` header to the request with value `Bearer {token}` where `{token}` is the JW token you received
  when authenticating.
- You can verify that all of these properties hold by checking the database. If no such teams or facilities exist,
  add them to the database.

3. Send the request to the server. It should return an error, with status `500 (Internal Server Error)`, because
   communication with the *Users* microservice has failed. You can verify that nothing has been saved in the
   `reservations/reservation` table in the *Reservations* microservice, as well.

4. This concludes the test.

#### 3.1.12 Booking sports facility works as intended if all parameters are completely valid
1. The endpoint that we will be accessing is `http://localhost:9091/api/v1/facility/book`. This is a `PUT` endpoint.
   It takes the following parameters:
- `teamUuid` - make sure that you are using the ID of a team that belongs to the currently authenticated user, that
  it exists, that it can still book and whose size is within the capacity constraints for the sports facility.
- `facilityUuid` - make sure that this is the ID of the sports facility, whose minimum capacity is smaller or equal to
  the team size and whose maximum capacity is bigger or equal to the team size.
- `startTime` - make sure you are using a starting time that is in the future, and between `16:00` and `23:00`
- Add an `Authorization` header to the request with value `Bearer {token}` where `{token}` is the JW token you received
  when authenticating.
- You can verify that all of these properties hold by checking the database. If no such teams or facilities exist,
  add them to the database.

2. Send the request to the server. It should not return anything apart from the status code, which should be `200 (OK)`.

3. You can now verify that the reservation has been saved by checking the `reservations/reservation` table. There
   should be a reservation with the provided `startTime` and `teamUuid`, a `bookableUuid`, equal
   to the `facilityUuid` provided and an `amount` of 1.

4. Check the `users/users_reservations` table. The number of rows added should be equal to the size of the team, and the
   `reservationUuid` should be the same across all added rows - the UUID of the reservation that has just been created.

5. This concludes the test.

#### 3.1.13 Booking sports facility fails if team does NOT exist.
1. The endpoint that we will be accessing is `http://localhost:9091/api/v1/facility/book`. This is a `PUT` endpoint.
   It takes the following parameters:
- `teamUuid` - make sure that you are using the UUID of a team that does **not** exist. You can make it up or use an online
  generator. UUIDs are of the form `xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx` where `x` is any hexadecimal digit.
- `facilityUuid` - make sure that this is the ID of the sports facility, whose minimum capacity is smaller or equal to
  the team size and whose maximum capacity is bigger or equal to the team size.
- `startTime` - make sure you are using a starting time that is in the future, and between `16:00` and `23:00`
- Add an `Authorization` header to the request with value `Bearer {token}` where `{token}` is the JW token you received
  when authenticating.
- You can verify that all of these properties hold by checking the database. If no such teams or facilities exist,
  add them to the database.

2. Send the request to the server. It should return a body that says `Team with this UUID does not exist` with a
   status code of `400 (Bad Request)`.

3. You can verify that nothing has been saved in the`reservations/reservation` table of the *Reservations* microservice.

4. You can also verify that nothing has been saved in the `users/user_reservation` table of the *Users* microservice.

5. This concludes the test.

#### 3.1.14 Booking sports facility fails if the team can NOT book.
1. The endpoint that we will be accessing is `http://localhost:9091/api/v1/facility/book`. This is a `PUT` endpoint.
   It takes the following parameters:
- `teamUuid` - make sure that you are using the ID of a team that belongs to the currently authenticated user, that
  it exists and whose size is within the capacity constraints for that facility. Ensure that some user of this team
  can no longer book under their current subscription. You can do this by adding other sports facility or lesson
  reservations beforehand.
- `facilityUuid` - make sure that this is the ID of the sports facility, whose minimum capacity is smaller or equal to
  the team size and whose maximum capacity is bigger or equal to the team size.
- `startTime` - make sure you are using a starting time that is in the future, and between `16:00` and `23:00`
- Add an `Authorization` header to the request with value `Bearer {token}` where `{token}` is the JW token you received
  when authenticating.
- You can verify that all of these properties hold by checking the database. If no such teams or facilities exist,
  add them to the database.

2. Send the request to the server. It should return a body that says `Team with this UUID cannot book` with a
   status code of `400 (Bad Request)`.

3. You can verify that nothing has been saved in the`reservations/reservation` table of the *Reservations* microservice.

4. You can also verify that nothing has been saved in the `users/user_reservation` table of the *Users* microservice.

5. This concludes the test.

#### 3.1.15 Booking sports facility fails if the team size is SMALLER than the minimum capacity of the sports facility.
1. The endpoint that we will be accessing is `http://localhost:9091/api/v1/facility/book`. This is a `PUT` endpoint.
   It takes the following parameters:
- `teamUuid` - make sure that you are using the ID of a team that belongs to the currently authenticated user, that
  it exists, that it can still book but whose size is **smaller** than the minimum allowed capacity of the sports facility.
- `facilityUuid` - make sure that this is the ID of the sports facility whose minimum capacity is higher than the 
  size of the provided team
- `startTime` - make sure you are using a starting time that is in the future, and between `16:00` and `23:00`
- Add an `Authorization` header to the request with value `Bearer {token}` where `{token}` is the JW token you received
  when authenticating.
- You can verify that all of these properties hold by checking the database. If no such teams or facilities exist,
  add them to the database.

2. Send the request to the server. It should return a body that says:
   `The team is too small to book this sports facility. The minimum allowed number of people is {minCapacity}`
   where `minCapacity` is the minimum capacity of the facility that will be booked. The status code should be `400 (Bad Request)`.

3. You can verify that nothing has been saved in the`reservations/reservation` table of the *Reservations* microservice.

4. You can also verify that nothing has been saved in the `users/user_reservation` table of the *Users* microservice.

5. This concludes the test.

#### 3.1.15 Booking sports facility fails if the team size is GREATER than the maximum capacity of the sports facility.
1. The endpoint that we will be accessing is `http://localhost:9091/api/v1/facility/book`. This is a `PUT` endpoint.
   It takes the following parameters:
- `teamUuid` - make sure that you are using the ID of a team that belongs to the currently authenticated user, that
  it exists, that it can still book but whose size is **greater** than the maximum allowed capacity of the sports facility.
- `facilityUuid` - make sure that this is the ID of the sports facility whose maximum capacity is lower than the
  size of the provided team
- `startTime` - make sure you are using a starting time that is in the future, and between `16:00` and `23:00`
- Add an `Authorization` header to the request with value `Bearer {token}` where `{token}` is the JW token you received
  when authenticating.
- You can verify that all of these properties hold by checking the database. If no such teams or facilities exist,
  add them to the database.

2. Send the request to the server. It should return a body that says:
   `The team is too big to book this sports facility. The maximum allowed number of people is {maxCapacity}`
   where `maxCapacity` is the maximum capacity of the facility that will be booked. The status code should be `400 (Bad Request)`.

3. You can verify that nothing has been saved in the`reservations/reservation` table of the *Reservations* microservice.

4. You can also verify that nothing has been saved in the `users/user_reservation` table of the *Users* microservice.

5. This concludes the test.

#### 3.1.16 Remove reservation for sports facility also deletes the corresponding reduced reservations in the users microservice
1. The endpoint that we will be accessing is `http://localhost:9091/api/v1/facility/deleteBooking`. This is a `PUT` endpoint.
   It takes the following parameters:
- `reservationUuid` - make sure that this is a Uuid of a reservation for a sports facility that exists.
- `teamUuid` - make sure that you are using the UUID of the team that made the reservation with that `reservationUuid`
- Add an `Authorization` header to the request with value `Bearer {token}` where `{token}` is the JW token you received
  when authenticating.
- You can verify that all of these properties hold by checking the database. If no such teams or lessons exist,
  add them to the database.

2. Send the request to the server. It should not return anything apart from the status code, which should be `200 (OK)`.

3. You can verify that the reservation with the provided UUID has been deleted from the`reservations/reservation`
   table of the *Reservations* microservice.

4. You can also verify that the corresponding `ReducedReservation` entity has been deleted from the
   `users/reduced_reservation` table of the *Users* microservice and that there are no rows with that `reservationUuid`
   reservationUuid anymore in the `users/users_reservations` table.

5. This concludes the test.

#### 3.1.17 Delete lesson also deletes the corresponding reservations in the users microservice
1. For this test, make sure that you are authenticated as an **admin**, instead of a regular user. Remember to save the
   JW token somewhere.
2. The endpoint that we will be accessing is `http://localhost:9091/api/v1/facility/delete`. This is a `DELETE` endpoint.
   It takes the following parameters:
- `sportsFacilityUuid` - make sure that this is the UUID of a sports facility that exists and that it has **at least** 1 reservation.
- Add an `Authorization` header to the request with value `Bearer {token}` where `{token}` is the JW token you received
  when authenticating.
- You can verify that all of these properties hold by checking the database. If no such teams or lessons exist,
  add them to the database.

2. Send the request to the server. It should not return anything apart from the status code, which should be `200 (OK)`.

3. You can verify that the lesson entity has been successfully deleted from `reservations/lesson` table in the *Reservations*
   microservice. Any corresponding reservations in `reservations/reservation` should also be gone.

4. You can also verify that reservations in the `users/reduced_reservation` table of the *Users* microservice
   have been removed.

5. This concludes the test.

### 3.2 Tests for communication between Reservations and Authentication
The **Reservations** and **Authentication** microservices communicate when there is a request, sent to **Reservations**
that needs to be authenticated. For instance, some endpoints are only accessible to admins. We also do not want people
who do not have an account to make reservations, hence the need for both authorization and authentication.
With that in mind, we will only be testing one endpoint on the **Authorization**'s end - `/permit`, which given a JW
token, returns the role of a user.

For the following tests you only need to have the `Authentication` and `Reservations` microservices up and running,
unless stated otherwise by the test.

#### 3.2.1 A request to the Reservations microservice fails, if Authentication is not running.
1. For this test, make sure the `Authentication` microservice is not running but that the `Reservations` one is.
   
2. The endpoint that we will be accessing in the **Reservations** microservice is `http://localhost:9091/api/v1/reservation/getAll`,
which retrieves all reservations for a specific team. It is a `GET` endpoint, and it takes the following parameters:
- `teamUuid` - Make sure that you provide a UUID of a team that has had at least one reservation.

3. Since the `Authentication` microservice will not have been started, we cannot log in, hence we will not have valid JW token. 
   Instead, we can generate one (using [this](https://jwt.io/) website, for instance). When you have generated the token,
   make sure to put it in the `Authorization` header of the request. Preceed the token with `Bearer ` (including the space).
   
4. Execute the request. After waiting a bit (around 5 seconds), you should receive a response with status code `500 (Internal Server Error)`.
   This is the desired outcome of this test.

5. This concludes the test.

#### 3.2.2 A request to the Reservations microservice fails, if an invalid JWT is provided.
1. The endpoint that we will be accessing in the **Reservations** microservice is `http://localhost:9091/api/v1/reservation/getAll`,
   which retrieves all reservations for a specific team. It is a `GET` endpoint and it takes the following parameters:
- `teamUuid` - Make sure that you provide a UUID of a team that has had at least one reservation.

2. We want to have an invalid JWT token, so we can just generate a random one (using [this](https://jwt.io/) website, for instance). 
   When you have generated the token, make sure to put it in the `Authorization` header of the request. 
   Preceed the token with `Bearer ` (including the space).

3. Execute the request. You should have now received a response, saying `You are not authorized to perform this action.`
alongside a status code of `401 (Unauthorized)`. This is the expected outcome of this test

4. This concludes the test.

#### 3.2.3 A request to the Reservations microservice fails, if a normal user tries to access an admin endpoint.

##### 3.2.3.1 Add endpoint
1. The endpoint that we will be accessing in the **Reservations** microservice is `http://localhost:9091/api/v1/equipment/add`.
   It is a `PUT` endpoint, and it takes the following parameters:
    - `name` - the name of the equipment
    - `capacity` - the amount of equipment available
    - `relatedSport` - a string, representing a sport the equipment is used for (e.g. "baseball", "basketball", etc.)
    
2. Normally, this endpoint is only accessible to admins. However, we will try to access it as a normal user. So make
  sure you are authenticated as such. Save the JW token.

3. Add an `Authorization` header to the request with value `Bearer {token}` where `{token}` is the JW token you received
  when authenticating.

4. Execute the request. You should have now received a response, saying `You are not authorized to perform this action.`
   alongside a status code of `401 (Unauthorized)`. This is the expected outcome of this test.

5. This concludes the test.

##### 3.2.3.2 Update endpoint
1. The endpoint that we will be accessing in the **Reservations** microservice is `http://localhost:9091/api/v1/equipment/update`.
   It is a `PUT` endpoint, and it takes the following parameters:
    - `equipmentUuid` - The UUID of the equipment to update (make sure such an equipment already exists in the database)
    - `name` - the name of the equipment
    - `capacity` - the amount of equipment available
    - `relatedSport` - a string, representing a sport the equipment is used for (e.g. "baseball", "basketball", etc.)

2. Normally, this endpoint is only accessible to admins. However, we will try to access it as a normal user. So make
   sure you are authenticated as such. Save the JW token.

3. Add an `Authorization` header to the request with value `Bearer {token}` where `{token}` is the JW token you received
   when authenticating.

4. Execute the request. You should have now received a response, saying `You are not authorized to perform this action.`
   alongside a status code of `401 (Unauthorized)`. This is the expected outcome of this test.

5. This concludes the test.

##### 3.2.3.3 Delete endpoint
1. The endpoint that we will be accessing in the **Reservations** microservice is `http://localhost:9091/api/v1/equipment/delete`.
   It is a `DELETE` endpoint, and it takes the following parameters:
    - `equipmentUuid` - The UUID of the equipment to delete (make sure such an equipment already exists in the database)
    
2. Normally, this endpoint is only accessible to admins. However, we will try to access it as a normal user. So make
   sure you are authenticated as such. Save the JW token.

3. Add an `Authorization` header to the request with value `Bearer {token}` where `{token}` is the JW token you received
   when authenticating.

4. Execute the request. You should have now received a response, saying `You are not authorized to perform this action.`
   alongside a status code of `401 (Unauthorized)`. This is the expected outcome of this test.

5. This concludes the test.

##### 3.2.3.4 Get all reservations by bookable UUID
1. The endpoint that we will be accessing in the **Reservations** microservice is `http://localhost:9091/api/v1/reservation/getByBookable`.
   It is a `GET` endpoint, and it takes the following parameters:
    - `bookableUuid` - The UUID of a bookable to retrieve all reservations for. Make sure such a bookable exists and that 
    it has at least one reservation related to it.

2. Normally, this endpoint is only accessible to admins. However, we will try to access it as a normal user. So make
   sure you are authenticated as such. Save the JW token.

3. Add an `Authorization` header to the request with value `Bearer {token}` where `{token}` is the JW token you received
   when authenticating.

4. Execute the request. You should have now received a response, saying `You are not authorized to perform this action.`
   alongside a status code of `401 (Unauthorized)`. This is the expected outcome of this test.

5. This concludes the test.

#### 3.2.4 A request to the Reservations microservice succeeds, if an admin accesses it.

##### 3.2.4.1 Add endpoint
1. The endpoint that we will be accessing in the **Reservations** microservice is `http://localhost:9091/api/v1/equipment/add`.
   It is a `PUT` endpoint, and it takes the following parameters:
    - `name` - the name of the equipment
    - `capacity` - the amount of equipment available
    - `relatedSport` - a string, representing a sport the equipment is used for (e.g. "baseball", "basketball", etc.)

2. We will now access this endpoint as an **admin**, so make sure that you are authenticated as such. Save your Javascript
Web Token for the next step.

3. Add an `Authorization` header to the request with value `Bearer {token}` where `{token}` is the JW token you received
   when authenticating.

4. Execute the request. The response should now be empty with a status code of `200 (OK)`. You can verify that the piece
of equipment has been added successfully to the database.

5. This concludes the test.

##### 3.2.4.2 Update endpoint
1. The endpoint that we will be accessing in the **Reservations** microservice is `http://localhost:9091/api/v1/equipment/update`.
   It is a `PUT` endpoint, and it takes the following parameters:
    - `equipmentUuid` - The UUID of the equipment to update (make sure such an equipment already exists in the database)
    - `name` - the name of the equipment
    - `capacity` - the amount of equipment available
    - `relatedSport` - a string, representing a sport the equipment is used for (e.g. "baseball", "basketball", etc.)

2. We will now access this endpoint as an **admin**, so make sure that you are authenticated as such. Save your Javascript
   Web Token for the next step.

3. Add an `Authorization` header to the request with value `Bearer {token}` where `{token}` is the JW token you received
   when authenticating.

4. Execute the request. The response should now be empty with a status code of `200 (OK)`. You can verify that the piece
   of equipment has been updated successfully by checking the database.

5. This concludes the test.

##### 3.2.4.3 Delete endpoint
1. The endpoint that we will be accessing in the **Reservations** microservice is `http://localhost:9091/api/v1/equipment/delete`.
   It is a `DELETE` endpoint and it takes the following parameters:
    - `equipmentUuid` - The UUID of the equipment to delete (make sure such an equipment already exists in the database)

2. We will now access this endpoint as an **admin**, so make sure that you are authenticated as such. Save your Javascript
   Web Token for the next step.

3. Add an `Authorization` header to the request with value `Bearer {token}` where `{token}` is the JW token you received
   when authenticating.

4. Execute the request. The response should now be empty with a status code of `200 (OK)`. You can verify that the piece
   of equipment (as well as any reservations related to it) has been deleted successfully by checking the database.

5. This concludes the test.

##### 3.2.4.4 Get all reservations by bookable UUID
1. The endpoint that we will be accessing in the **Reservations** microservice is `http://localhost:9091/api/v1/reservation/getByBookable`.
   It is a `GET` endpoint, and it takes the following parameters:
    - `bookableUuid` - The UUID of a bookable to retrieve all reservations for. Make sure such a bookable exists and that
      it has at least one reservation related to it.

2. We will now access this endpoint as an **admin**, so make sure that you are authenticated as such. Save your Javascript
   Web Token for the next step.

3. Add an `Authorization` header to the request with value `Bearer {token}` where `{token}` is the JW token you received
   when authenticating.

4. Execute the request. The response should now be a list of reservation for that equipment. You can verify that the
   list is correct by checking a database. The response should also have a status code of `200 (OK)`. 

5. This concludes the test.

#### 3.2.5 A user is able to access a normal endpoint successfully
1. The endpoint that we will be accessing in the **Reservations** microservice is `http://localhost:9091/api/v1/reservation/getAll`,
   which retrieves all reservations for a specific team. It is a `GET` endpoint and it takes the following parameters:
- `teamUuid` - Make sure that you provide a UUID of a team that has had at least one reservation.

2. We will now access this endpoint as a **normal user**, so make sure that you are authenticated as such. Save your Javascript
   Web Token for the next step.

3. Add an `Authorization` header to the request with value `Bearer {token}` where `{token}` is the JW token you received
   when authenticating.

4. Execute the request. The response should now be a list of all reservations that the team with the provided teamUuid 
   have ever made. The response's status code should be `200 (OK)`.

5. This concludes the test.

#### 3.2.6 An admin is able to access a normal endpoint successfully
1. The endpoint that we will be accessing in the **Reservations** microservice is `http://localhost:9091/api/v1/reservation/getAll`,
   which retrieves all reservations for a specific team. It is a `GET` endpoint and it takes the following parameters:
- `teamUuid` - Make sure that you provide a UUID of a team that has had at least one reservation.

2. We will now access this endpoint as an **admin**, so make sure that you are authenticated as such. Save your Javascript
   Web Token for the next step.

3. Add an `Authorization` header to the request with value `Bearer {token}` where `{token}` is the JW token you received
   when authenticating.

4. Execute the request. The response should now be a list of all reservations that the team with the provided teamUuid
   have ever made. The response's status code should be `200 (OK)`.

5. This concludes the test.

### 3.3 Tests for communication between Users and Authentication

#### 3.3.1 A request to the Users microservice fails, if Authentication is not running.
1. For this test, make sure the `Authentication` microservice is not running but that the `Users` one is.
   
2. The endpoint that we will be accessing in the **Users** microservice is `http://localhost:9090/api/v1/user/changeSubscription`,
which changes a user's subscription status. It is a `PUT` endpoint, and it takes the following parameters:
- `userUuid` - The user's UUID.
- `hasPremium` - subscription status to change to.

3. Since the `Authentication` microservice will not have been started, we cannot log in, hence we will not have valid JW token. 
   Instead, we can generate one (using [this](https://jwt.io/) website, for instance). When you have generated the token,
   make sure to put it in the `Authorization` header of the request. Preceed the token with `Bearer ` (including the space).
   
4. Execute the request. After waiting a bit (around 5 seconds), you should receive a response with status code `500 (Internal Server Error)`.
   This is the desired outcome of this test.

5. This concludes the test.

#### 3.3.2 A request to the Users microservice fails, if an invalid JWT is provided.
1. The endpoint that we will be accessing in the **Users** microservice is `http://localhost:9090/api/v1/user/changeSubscription`,
which changes a user's subscription status. It is a `PUT` endpoint, and it takes the following parameters:
- `userUuid` - The user's UUID.
- `hasPremium` - subscription status to change to.

2. We want to have an invalid JWT token, so we can just generate a random one (using [this](https://jwt.io/) website, for instance). 
   When you have generated the token, make sure to put it in the `Authorization` header of the request. 
   Preceed the token with `Bearer ` (including the space).

3. Execute the request. You should have now received a response, saying `You are not authorized to perform this action.`
alongside a status code of `401 (Unauthorized)`. This is the expected outcome of this test

4. This concludes the test.

#### 3.3.3 A request to the Users microservice fails, if a normal user tries to access an admin endpoint.
1. The endpoint that we will be accessing in the **User** microservice is `http://localhost:9090/api/v1/user/changeSubscription`,
   which changes a user's subscription status. It is a `PUT` endpoint and it takes the following parameters:
- `userUuid` - The user's UUID.
- `hasPremium` - subscription status to change to.

2. We will now access this endpoint as a **normal user**, so make sure that you are authenticated as such.

3. Execute the request. The response should now be a list of information of the created user. The response should be `You are not authorized to perform this action`.

4. This concludes the test.

#### 3.3.4 A request to the Users microservice runs, if an admin tries to access an admin endpoint.
1. The endpoint that we will be accessing in the **User** microservice is `http://localhost:9090/api/v1/user/changeSubscription`,
   which changes a user's subscription status. It is a `PUT` endpoint and it takes the following parameters:
- `userUuid` - The user's UUID.
- `hasPremium` - subscription status to change to.

2. We will now access this endpoint as an **admin**, so make sure that you are authenticated as such.

3. Execute the request. The response should now be a whitespace. The user's subscription will now be updated in the database.

4. This concludes the test.

#### 3.3.5 A request to the Users microservice runs, if a normal user tries to access a normal user endpoint.
1. The endpoint that we will be accessing in the **User** microservice is `http://localhost:9090/api/v1/user/getUserTeams`,
   which returns the teams that a user is in. It is a `GET` endpoint and it takes the following parameters:
- `userUuid` - The user's UUID.

2. We will now access this endpoint as a **normal user**, so make sure that you are authenticated as such.

3. Execute the request. The response should now be a list of teams that the user is in.

4. This concludes the test.

#### 3.3.6 A request to the Users microservice runs, if an admin tries to access a normal user endpoint.
1. The endpoint that we will be accessing in the **User** microservice is `http://localhost:9090/api/v1/user/getUserTeams`,
   which returns the teams that a user is in. It is a `GET` endpoint and it takes the following parameters:
- `userUuid` - The user's UUID.

2. We will now access this endpoint as an **admin**, so make sure that you are authenticated as such.

3. Execute the request. The response should now be a list of teams that the user is in.

4. This concludes the test.

## Appendix

Here you can find how you can access various endpoints of the application, including adding and removing bookables,
adding and removing reservations, adding users and teams, as well as authenticating. 

### Equipment
#### Add equipment (**Admin** permissions required):
`PUT` endpoint
API: `http://localhost:9091/api/v1/equipment/add`
Parameters it takes:
- `name` - the name given to the equipment 
- `capacity` - the amount of units
- `relatedSport` - the sport related to the equipment

#### Retrieve equipment:
`GET` endpoint
API: `http://localhost:9091/api/v1/equipment/get`
Parameters it takes:
- `equipmentUuid` the UUID of the equipment to be retrieved

#### Update equipment (**Admin** permissions required):
`PUT` endpoint
API: `http://localhost:9091/api/v1/equipment/update`
Parameters it takes:
- `equipmentUuid` - The UUID of the equipment
- `newName` - The new name to use
- `newCapacity` - The new capacity to use 
- `newRelatedSport` The new related sport to use

#### Delete equipment (**Admin** permissions required):
`DELETE` endpoint
API: `http://localhost:9091/api/v1/equipment/delete`
Parameters it takes:
- `equipmentUuid` - The UUID of the equipment to remove

#### Book equipment:
`PUT` endpoint
API: `http://localhost:9091/api/v1/equipment/book`
Parameters it takes:
- `teamUuid` - The UUID of the team making the booking
- `equipmentUuid` - The UUID of the equipment requested for booking
- `startTime` - The starting time of the booking
- `amount` - The amount of pieces of equipment requested for booking

#### Delete booking for equipment: 
`PUT` endpoint
API: `http://localhost:9091/api/v1/equipment/deleteBooking`
Parameters it takes:
- `reservationUuid` - The UUID of the reservation to be deleted
- `teamUuid` - The UUID of the team that made the reservation

### Sports Facilities
#### Add a sports facility (**Admin** permissions required):
`PUT` endpoint
API: `http://localhost:9091/api/v1/facility/add` 
Parameters it takes:
- `name` - The name of the sports facility
- `minCapacity` - The minimum amount of people this sports facility can be booked in teams with
- `maxCapacity` - The maximum amount of people this sports facility can be booked in teams with

#### Retrieve a sports facility:
`GET` endpoint
API: `http://localhost:9091/api/v1/facility/get`
Parameters it takes:
- `sportsFacilityUuid` - The UUID of the sports facility

#### Update a sports facility (**Admin** permissions required):
`PUT` endpoint
API: `http://localhost:9091/api/v1/facility/update`
Parameters it takes:
- `sportsFacilityUuid` - The UUID of the facility to update
- `newName` - The new name of the facility
- `newMinCapacity` - The new minimum capacity of the facility
- `newMaxCapacity` - The new maximum capacity of the facility

#### Delete a sports facility (**Admin** permissions required):
`DELETE` endpoint
API: `http://localhost:9091/api/v1/facility/delete`
Parameters it takes:
- `sportsFacilityUuid` - The UUID of the sports facility to remove

#### Book a sports facility:
`PUT` endpoint
API: `http://localhost:9091/api/v1/facility/book`
Parameters it takes:
- `teamUuid` - The UUID of the team that is trying to make the booking
- `sportsFacilityUuid` - The UUID of the facility that this team wishes to book
- `startTime` - The start time of the reservation. End time is automatically set to 1 hour after the start time

#### Delete booking for a sports facility:
`PUT` endpoint
API: `http://localhost:9091/api/v1/facility/deleteBooking`
Parameters it takes:
- `reservationUuid` The UUID of the reservation to remove
- `teamUuid` The team that is trying to remove the booking. Only teams that have made 
  the reservation with this reservationUUID have a right to cancel that 
  booking

### Lessons
#### Add a lesson (**Admin** permissions required):
`PUT` endpoint
API: `http://localhost:9091/api/v1/lesson/add`
Parameters it takes:
- `name` - The name given to the lesson
- `maxCapacity` - The amount of units
- `sportsFacilityUuid` - The Uuid of the facility in which the lesson is to take place
- `startTime` - The starting time of the lesson
- `endTime` - The ending time of the lesson

#### Retrieve a lesson:
`GET` endpoint
API: `http://localhost:9091/api/v1/lesson/get`
Parameters it takes:
- `lessonUuid` - The UUID of the lesson to be retrieved

#### Update a lesson (**Admin** permissions required):
`PUT` endpoint
API: `http://localhost:9091/api/v1/lesson/update`
Parameters it takes:
- `lessonUuid` - The UUID of the lesson to update
- `newName` - The new name of the lesson
- `newMaxCapacity` - The new max capacity of the lesson
- `newSportsFacilityUuid` - The new UUID of the sports facility that is being used
- `newStartTime` - The new start time of the lesson
- `newEndTime` - The new end time of the lesson

#### Delete a lesson (**Admin** permissions required):
`DELETE` endpoint
API: `http://localhost:9091/api/v1/lesson/delete`
Parameters it takes:
- `lessonUuid` The Uuid of the lesson to delete

#### Book a lesson:
`PUT` endpoint
API: `http://localhost:9091/api/v1/lesson/book` 
Parameters it takes:
- `teamUuid` - The UUID of the team making the booking
- `lessonUuid` - The UUID of the lesson requested for booking

#### Delete booking for a lesson:
`PUT` endpoint
API: `http://localhost:9091/api/v1/lesson/deleteBooking`
Parameters it takes:
- `reservationUuid` - The UUID of the reservation to be deleted
- `teamUuid` - The UUID of the team that made the reservation

### Reservations
#### Get all reservations for a bookable (**Admin** permissions required)
`GET` endpoint
API: `http://localhost:9091/api/v1/reservation/getByBookable`
Parameters it takes:
- `bookableUuid` - The UUID of the bookable, whose reservations should be displayed.

#### Get all reservations for a team
`GET` endpoint
API: `http://localhost:9091/api/v1/reservation/getAll`
Parameters it takes:
- `teamUuid` - The UUID of the team to get the reservations for

#### Get all **upcoming** reservations for a team
`GET` endpoint
API: `http://localhost:9091/api/v1/reseravtion/getAllUpcoming`
Parameters it takes:
- `teamUuid` - The UUID of the team to get the upcoming reservations for

### Users

#### Add a user
`PUT` endpoint
API: `http://localhost:9090/api/v1/user/add`
Parameters it takes:
- `userUuid` - The UUID of the user to be added
- `name` - The name of the user to be added

#### Delete a user
`DELETE` endpoint
API: `http://localhost:9090/api/v1/user/delete`
Parameters it takes:
- `userUuid` - The UUID of the user to be deleted

#### Get the teams that a user is in
`GET` endpoint
API: `http://localhost:9090/api/v1/user/getUserTeams`
Parameters it takes:
- `userUuid` - The UUID of the user that we want to get the teams of

#### Change a user's subscription
`PUT` endpoint
API: `http://localhost:9090/api/v1/user/changeSubscription`
Parameters it takes:
- `userUuid` - The user's UUID
- `hasPremium` - subscription status to change to

### Teams

#### Add a team
`PUT` endpoint
API: `http://localhost:9090/api/v1/team/addTeam`
Parameters it takes:
- `name` - The name of the team to be added

#### Add a user to a team
`PUT` endpoint
API: `http://localhost:9090/api/v1/team/addUserToTeam`
Parameters it takes:
- `userUuid` - The UUID of the user to be added
- `teamUuid` - The UUID of the team that the user will be added to

#### Check if a team exists
`GET` endpoint
API: `http://localhost:9090/api/v1/team/exists`
Parameters it takes:
- `teamUuid` - The UUID of the team that should be checked

#### Check if a team is able to book a reservation
`GET` endpoint
API: `http://localhost:9090/api/v1/team/canTeamBook`
Parameters it takes:
- `teamUuid` - The UUID of the team that should be checked
- `reservationTime` - the time of the reservation

#### Get the size of a team
`GET` endpoint
API: `http://localhost:9090/api/v1/team/getTeamSize`
Parameters it takes:
- `teamUuid` - The UUID of the team that we want the size of

#### Add a reservation to a team
`PUT` endpoint
API: `http://localhost:9090/api/v1/team/addReservation`
Parameters it takes:
- `teamUuid` - The UUID of the team that made the reservation
- `reservationUuid` - The UUID of the reservation that is to be added
- `timestamp` - The timestamp of the reservation

#### Delete a team's reservation
`DELETE` endpoint
API: `http://localhost:9090/api/v1/team/deleteReservation`
Parameters it takes:
- `reservationUuid` - The UUID of the reservation that we want to delete
- `teamUuid` - The UUID of the team that owns the reservation

### Authentication

#### Authenticate user credentials
`POST` endpoint
API: `http://localhost:8080/api/v1/authentication/authenticate`
Parameters it takes:
- `username` - The username of the account
- `password` - The password of the account

#### Activate the first admin account
`POST` endpoint
API: `http://localhost:8080/api/v1/authentication/activate`
Parameters it takes:
- `activationKey` - The activation key
- `password` - The password you want to give the `admin` admin account
- `password2` - The second entry of the password

#### Add admin account
`POST` endpoint
API: `http://localhost:8080/api/v1/authentication/add-admin`
Parameters it takes:
- `username` - The username you want to give the admin account
- `password` - The password you want to give the admin account
- `password2` - The second entry of the password

#### Register a new account
`POST` endpoint
API: `http://localhost:8080/api/v1/authentication/register`
Parameters it takes:
- `name` - The name of the person registering an account
- `username` - The username you want to give the account
- `password` - The password you want to give the account
- `password2` - The second entry of the password

#### Change password
`POST` endpoint
API: `http://localhost:8080/api/v1/authentication/change-password`
Parameters it takes:
- `password` - The current password of the account
- `newPassword` - The new password you want to give the account
- `password2` - The second entry of the new password

#### Delete account
`DELETE` endpoint
API: `http://localhost:8080/api/v1/authentication/delete`
Parameters it takes:
- `password` - The password of the account you want to delete
