# <center> Sprint Review 1 </center>
### <center> Period: 23.11.2021 - 30.11.2021 </center>

## I. What has been done
### 1. General
 * We have come up with a draft of the architecture, which will be refined during *week 5*
   
 * We have created the general structure of the project and decided to split it into **3** microservices
   * **Reservations**: This service will be responsible for all reservations and will keep track of everything
     that can be booked - Lessons, Halls/Fields and Equipment.
   
   * **Users**: This service will handle *almost* all information regarding users, including their subscription
     status, usernames, as well as the teams that they have formed
   
   * **Authentication**: This service will handle the authentication part of our application and will
     consist of another database, which contains the users' usernames, passwords, and the authentication tokens that are 
     currently in use. *Disclaimer: This may change as we are still in the process of figuring out how Spring
     security works.*
   
   * All three services are planned to communicate with each other when they need information from each other.
     Communication will probably happen utilizing *Spring Eureka*, though we are still looking into that part.
     
   * From a techinical point of view, these projects are set up as separate *Gradle* projects and common
     dependencies have been extracted in the root folder. This separation allows for microservices to be
     run independently, which is what we want to achieve for scalability.
     
 * We have decided to use **PostgreSQL 13** as our Database Management System. Dependencies for this have been added
   to all services and they will be using their own local databases.

### 2. Reservations
*<div style="text-align: right"> Assigned: Bozhidar & Tudor  </div>*
 We have created the general structure of this microservice, guided by *Spring*. We split this microservice
   into 4 parts - **entities**, **repositories**, **services** and **APIs**.
     
  * **Entities** - these represent the database records in an ORM format. For this service, we have 5 entities.
       
    * Bookable - This entity represents all things that are bookable - Halls/Fields/Equipment/Lessons. It has a name, description,
       maximum capacity/amount and an unique identifier (UUID)
     
    * Lesson - This inherits from `Bookable` and also has a sports facility related to it, as a lesson takes place inside
       one of these sports facilities. UUID is shared with Bookable.
       
    * Equipment - This inherits from `Bookable` and also has a sport, associated with it. UUID is shared with Bookable.
       
    * Sports Facility - This also inherits from `Bookable` and represents a Hall or a Field Lessons take place in. These
       can be booked after `16:00`. UUID is shared with Bookable
         
    * Reservation - This entity corresponds to a reservation made by a team (we will be representing single
       users as teams with one element). It links a `Bookable` with a `Team` and also has a start time of the
       reservation, as well as a unique id (UUID, again). To get the `Team` of the booking, the service should communicate
       with the Users microservice.
       
  * **Repositories** - We have created JPA repositories to connect to *Tables* of the database. There is a repository
    for each of the entities described above. Basic queries, like `findById` or `findAll` are available by default. Custom
    queries will be added when more business logic is implemented
    
  * **Services** - These represent the business logic of the application. There is one service for each *API* (which usually corresponds to a single entity). 
    Services may contain *multiple* repositories and other services, as they may need to update information in multipiple places.
    Currently, we do not have any business logic, so these are almost empty.
    
  * **APIs** - This is where requests come in. Whenever the API recognizes that it is the one that should be handling the request, it forwards it to the appropriate
    service(s) and waits for a response. When it receives that response it sends an HTTP response to the client that requested/added information.
    These are implemented as REST controllers.
    
### 3. Users
*<div style="text-align: right">  Assigned: Alexandra & Ferhan </div>*
Similarly to the reservations microservice, we have also split the microservice into **4** parts and delegated the responsibilities
for each part - **Entities**, **Repositories**, **Services** and **APIs**. What is different from the mentioned microservice is that 
the entities are (naturally) different. We have **2** of those:

 * **User**, which stores a single user, who is identified by a UUID, a name, an email and a password (which we might want to
   move to the authentication microservice, depending on what we conclude from research. It also has a boolean variable indicating
   the type of subscrption the user has - either basic or premium, with premium allowing up to 3 reservations daily.
   
 * **Team**, which stores multiple users, who have been merged in a team. It has a unique team uuid, a name and members (which is stored
   as a list of users, internally). Single users can therefore be represented as teams of size 1. Whenever a reservation is made,
   only the team will be passed along and **NOT** a single user as this unifies all reservation cases and makes implementation much simpler.
   
We have also created repositories, services and APIs for these entities.
### 4. Authentication
*<div style="text-align: right">  Assigned: Luuk & Jannick </div>*
*Spring Security* is a really broad topic that none of us have had prior experience with. Therefore, heavy research should have been
done in order to get a better grasp on how Spring authentication is supposed to work. That is why, the whole sprint for the Authentication 
microservice has been dedicated to this - acquiring the background knowledge that is essential before starting any implementation whatsoever.
### 5. Miscellaneous
 * GitLab milestones have been set up. We refer to those as sprints. We have established that a sprint should run for one week (given the tight
   time constraints we have for finishing this project) and that each sprint starts and ends on Tuesday with a team meeting about how the previous sprint went and what the new one is concerned with.

 * GitLab issues have been created that correspond to the functional requirements. They are accompanied by descriptions, labels, which indicate which
   service this issue is about (can be multiple), a weight, assignees and a milestone.
## II. What is planned to be done for *Sprint 2*
### 1. General

* We should start thinking about the design patterns that we can apply in our scenario and keep them in the back of our heads
  when we are implementing the microservices, so that they are easily added afterwards.
  
* We should start thinking about different testing techniques that we can apply to test the correctness of our code - unit testing
  is a must, we should also look into mutation testing, integration testing and possibly system testing.
  
* We should take a look at our architecture once again and see what has changed from the draft that we submitted this sprint.
  Those changes should be reflected in the final submission of *Assignment 1*.

* We have to keep it in the back of our head that communication between microservices is not done yet and that would probably
  require quite some research, as well.
### 2. Reservations
* We should start adding endpoints for accessing equipment, sports facilities, lessons and reservations. Not everything will be
  done this week, most probably, as that is quite a lot of endpoints. We should also start testing the functionality of the microservice.
### 3. Users
* We should also start adding enpoints, which are concerned with user and team access, as well as the formation of different
teams for the same user.
### 4. Authentication
* Implementation should start in the next sprint, guided by the research that has been done up to this point. The database for authentication should
be set up and synced with the users database in some way.
  

<br>
<div style="text-align: right">
<p>Date compiled: 06.12.2021</p>
Compiled by: Bozhidar Andonov <br>
Approved by: Alexandra-Ioana Negau, Ferhan Yildiz, Jannick Weitzel, Luuk van de Laar and Tudor Popica
</div>