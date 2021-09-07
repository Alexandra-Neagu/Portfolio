Client: ![Client coverage](https://gitlab.ewi.tudelft.nl/cse1105/2020-2021/team-repositories/oopp-group-24/repository-template/badges/master/coverage.svg?job=client-test)
Server: ![Server coverage](https://gitlab.ewi.tudelft.nl/cse1105/2020-2021/team-repositories/oopp-group-24/repository-template/badges/master/coverage.svg?job=server-test)


# Starting template

This README will need to contain a description of your project, how to run it, how to set up the development environment, and who worked on it.
This information can be added throughout the course, except for the names of the group members.
Add your own name (do not add the names for others!) to the section below.

## Description of project

This project is an e-learning tool that facilitates student-lecturer interactions during lectures. It has been created as part of the
Object-Oriented Programming Project course, at the Delft University of Technology.

Our application aims at providing an environment where students can ask questions freely, without feeling pressured by their peers
or interrupting the lecture. Various features help the lecturers organise their classes, efficiently sort and filter the questions, so that the most relevant 
queries can be gathered and answered, and adjust the pace of their lectures accordingly. The application supports moderators, in order to ensure that lectures are kept civil and spam-free.
Using an external persistent database, our software guarantees that no data will be lost in case of system failures. 

Furthermore, the application offers support to lecturers outside of classes. At the end of each lecture, moderators and lecturers can
choose to export to an external file all the questions asked, so that they can be reviewed at a later time if the need arises.
Lastly, but certainly not least, our software supports the creation and distribution of polls and quizzes, that help assess
the understanding of the taught material and keep the lectures engaging and interactive.

## Group members

| 📸 | Name | Email |
|---|---|---|
| ![](https://1.gravatar.com/avatar/1ca3908a1938e4ce2405148175092b06?s=50&d=mm) | Alexandra Ioana Neagu | A.I.Neagu@student.tudelft.nl |
| ![](https://1.gravatar.com/avatar/6b507bcbac97cc55c4690ed8084be710?s=80) | Doğa Cambaz | D.Cambaz@student.tudelft.nl |
| ![](https://eu.ui-avatars.com/api/?name=DÖ&length=4&size=50&color=DDD&background=777&font-size=0.325) | Hasan Doruk Özmetin | H.D.Ozmetin@student.tudelft.nl |
| ![](https://1.gravatar.com/avatar/7e0dc861680e232f4b615b34a4212070?s=80&d=identicon) | Weijun Wu | w.j.wu@student.tudelft.nl |
| ![](https://eu.ui-avatars.com/api/?name=OOPP&length=4&size=50&color=DDD&background=777&font-size=0.325) | Gert van Dijk | g.vandijk-3@student.tudelft.nl |
| ![](https://i.imgur.com/5CxwdIw.png)|Kristóf András Sándor|K.A.Sandor@student.tudelft.nl|
<!-- Instructions (remove once assignment has been completed -->
<!-- - Add (only!) your own name to the table above (use Markdown formatting) -->
<!-- - Mention your *student* email address -->
<!-- - Preferably add a recognisable photo, otherwise add your GitLab photo -->
<!-- - (please make sure the photos have the same size) --> 

## How to run it

1. Open the project in IntelliJ IDEA
2. Run DemoApplication to start up the server and connect to the database
3. Run MainApp to open a client process
   - For each MainApp that is a ran, a new independent client process is created
   - Run MainApp for as many clients as needed 

## How to contribute to it

1. Download IntelliJ IDEA. Any version from 2019 onwards suffices. For versions older than this, compatibility is not guaranteed.
2. Open the project in IntelliJ IDEA. The Spring Boot template and dependencies are set up automatically.
3. Make your contributions!

In order to ensure that everything works properly, keep in mind the following:

    -This project uses Java 13. Using older versions may result in certain features not working properly. 
        Using newer versions might result in some features, practices, and terminology becoming deprecated.
    - In order to use the CheckStyle plugin, make sure that version 8.25 is being used.
    - To use the features of JavaFX optimally, make sure that version 13 is being used.
    - To ensure that the Jacoco tool works, check that version 0.8.5 is being used.

All dependencies can be viewed in the three build.gradle files.

## Copyright / License (opt.)