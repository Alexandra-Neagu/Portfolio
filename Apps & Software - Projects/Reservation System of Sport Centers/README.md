# sem-repo-31b

# <center> Reservations System for Sports Centers </center>

## Overview
Now your clients can easily book halls, lessons, and equipment (collectively named Bookables) from your sports center,
thanks to our new highly customizable reservation system! The system is secure, using JWT authentication to prevent
people who do not have a subscription (Basic/Premium) from accessing the software. This authentication system also
allows users to change the password for their account, once they created one.

The project is realized using a Domain-Driven Design approach, being split into microservices to increase atomicity
and scalability. Communication between microservices employs `Spring Eureka`. In the development process, an emphasis
was put on implementing different software development patterns, namely the builder pattern, and the
chain-of-responsibility pattern.

## Features
### User features:
* Create reservations for halls, lessons, and equipment.
* Delete your reservations.
* Get information about halls, lessons, and equipment (such as number of units, minimum and maximum capacity, number of
  available units, etc.).
* Create teams for team sports and add your friends in them (football, hockey, etc.).

### Admin features:
* All features available to users.
* Add halls, lessons, and equipment.
* Delete halls, lessons, and equipment.
* Track halls, lessons, and equipment.
* Update halls, lessons, and equipment.
* Add/remove reservations of normal users.
* Change a user's subscription.
* Create other user accounts.

## How to run the application?*
1. Download the repository.
2. Make sure you have *PostgreSQL 13* installed on your machine and that the username
   and password are both set to `postgres`.
3. If you have not already, add 3 databases to that default configuration - `authentication`,
   `users` and `reservations`. Make sure that their names match exactly, otherwise you will
   run into problems when you are executing these tests (and the application, in general).
4. Before performing any action, make sure that the `DiscoveryServer` (path: `/discovery-server/src/main/java/`)
   is running.
5. Then, open all the applications. Their paths are as follows:
    * AuthenticationApplication: `/authentication/src/main/java/`
    * ReservationsApplication: `reservations/src/main/java/`
    * UsersApplication: `users/src/main/java`

*NOTE: Since the project does not have a GUI, you will need a tool such as Postman to perform operations.

## How to create admin accounts?
To create your first admin account, you need an activation key. This key is part of aquiring the software license and is unique per license. To generate the first admin user, which has name `admin`, you go to endpoint `/activate` in the `authentication` microservice and send a request containing the `activation key`, the `desired password` and a `second entry of the desired password`. This will create the first admin account. 

Then with this admin account you're able to create more admin accounts, as admins are able to create other admin accounts.