# <center> Sprint Review 2 </center>
### <center> Period: 01.12.2021 - 07.12.2021 </center>

## I. What has been done
### 1. General

* We have furthered the development of the software by adding the first endpoints to the API for both the *Users* and
  *Reservations* microservices, as well as [something with the authentication].
* Testing has been a big part of this Sprint, primarily JUnit testing.
* For the *Users* and *Reservations* microservices, we have successfully implemented **Repositories**, **Services**,
  and **APIs**, on top of the previously implemented **Entities** (from Sprint 1).

* For the *Authentication* microservice, [...].

### 2. Reservations
*<div style="text-align: right"> Assigned: Bozhidar & Tudor  </div>*
Whereas in the previous sprint (Sprint 1) we focused on implementing the **Entities** for the *Reservations*
microservice, this Sprint's focus was more on the business logic aspect of the overall architecture. Below there is a
more extensive overview of what went on in Sprint 2, for each individual layer of the software:

* **Entities**
    * Final steps were taken in the building of the 5 entities (Bookable, Lesson, Equipment,
      Sports Facility, Reservation), having reached 100% test coverage for each of them and thus allowing us to close the
      issues related to the creation of the *Reservations* database (#17 - Create Reservations Database,
      and #13 - Add name & related-sport to equipment).

* **Repositories**
    * Created queries that are more complex than the ones available by default for the *ReservationRepository*, having to
      finalize implementation for all the remaining ones in the following sprints. Queries
      `findAllByBookableAndStartTimeBetween` and `findAllByStartTimeBetween` were added, so that in the future we can
      easily deal with the time limits set into place for booking halls, lessons and equipment.

* **Services**
    * Created extensive business logic for the *Equipment* inside the *EquipmentService* class, making use of the
      previously discussed queries created in the *Reservation* repository. Successfully managed to **add**, **book**,
      and **remove** equipment, as well as a number of ways to **retrieve** equipment from the database: we can retrieve
      either one type of equipment (e.g. 'Baseball bat'), *all* the equipment, or all *available* equipment from the
      database.

* **APIs**
    * Whereas in Sprint 1 we only created the classes for each API, this week we advanced the development of the software
      by making several endpoints for the *EquipmentAPI*. The endpoints added in the API forward the requests to the appropriate business logic implemented in the
      *EquipmentService*. When a request has been handled successfully, the response object
      sends a `HttpStatus.OK` to the client that requested/added information,
      with a `200 OK` status code.

### 3. Users
*<div style="text-align: right">  Assigned: Alexandra & Ferhan </div>*
The development of the *Reservations* and *Users* microservices is going hand in hand so far in terms of
functionality implemented. Therefore, most of the things covered in the overview of the *Reservations* microservice are
again found for the *Users*.

* **Entities**
    * Final steps were taken in the building of the 2 entities (Team, User), having reached 100% test coverage for each
      of them and thus allowing us to close the issue related to the creation of the *Users* database
      (#19 - Create Users Database).

* **Repositories**
    * No additional queries created so far, just the default ones.

* **Services**
    * Created business logic inside the *UserService* and *TeamService* classes. Successfully managed to **add** both
      new users and new teams to the database.

* **APIs**
    * Whereas in Sprint 1 we only created the classes for each API, this week we advanced the development of the software
      by making endpoints for the *TeamAPI* and *UserAPI*. The endpoints added in the API forward the requests to the
      appropriate business logic implemented in the *TeamService* or *UserService* respectively.
      When a request has been handled successfully, the response object sends a `HttpStatus.OK` to the client that
      requested/added information, with a `200 OK` status code.

### 4. Authentication
*<div style="text-align: right">  Assigned: Luuk & Jannick </div>*
After spending a considerable amount of time on trying to integrate an authentication system that **doesn't require a
separate database**, by sending user login credentials to the *Users* microservice, so it can check its database, we
sadly realized that this will not be possible, as Spring Authentication does not support this — you are not able to actually touch the login credentials, Spring handles
this.

This means we will have to **create a separate database** and implement it that way. This is what we will be doing
in the next sprint.

### 5. Miscellaneous
* A number of **merge requests** have been created this sprint, merging both the development in the *Reservations* and
  in the *Users* microservices to `main`, after careful analysis by fellow project members.

* We have agreed on a rule of thumb for dealing with merge requests: two people need to approve the request, but the
  two people have to be working on different microservices than the one that the merge request is created for. That way
  we avoid confusion and increase engagement from all members of the team.

* As per the suggestion of our assigned mentor for this project, we started making use of the **priority** labels more
  thoroughly, and made the clarification that while a general overlook of the priority of the tasks is given by the MoSCoW
  model, **priority** is assigned only to issues from the current sprint, so we can better organize weekly workload.

## II. What is planned to be done for *Sprint 3*

### 1. General
* We would like to continue creating business logic for the software.
* A big part of the following sprint will be beginning to think about design patterns that we
  can implement in the project and then creating the architecture document for the first assignment.
* We have to begin testing the business logic, so that bugs will not appear later on in the development process.

### 2. Reservations
* We have to prioritize testing the business logic created in the previous sprint.
* We should start adding endpoints to the remaining APIs, similar to what we did in this sprint.

### 3. Users
* We have to prioritize testing the business logic created in the previous sprint.
* We should start adding more endpoints to the APIs, similar to what we did in this sprint.

### 4. Authentication
* We have to create the database for authentication and start the implementation process for the login/registration of
  users to the database.


<br>
<div style="text-align: right">
<p>Date compiled: 09.12.2021</p>
Compiled by: Tudor-George Popica <br>
Approved by: Alexandra-Ioana Negau, Ferhan Yildiz, Jannick Weitzel, Luuk van de Laar and Bozhidar Andonov
</div>