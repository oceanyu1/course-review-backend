<a id="readme-top"></a>

<br />
<div align="center">

  <h3 align="center">RavensRate</h3>

  <p align="center">
    RavensRate is a course review platform where Carleton University students can browse, review, and rate courses. This repo includes only the backend, you can access the frontend 
    <a href="https://github.com/oceanyu1/course-review-frontend"><strong>here.</strong></a> The live site is also available at <a href="https://ravensrate.ca"><strong>ravensrate.ca</strong></a>.
    It provides secure authentication, RESTful endpoints, and PostgreSQL integration to support the full review lifecycle from browsing courses to submitting feedback.
    <br />
    <a href="https://github.com/oceanyu1/course-review-backend"><strong>Explore the project »</strong></a>
  </p>
</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li><a href="#installation">Installation</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

I've always used RateMyProf for when I wanted to take courses, to see what professors were good or bad and help plan my semesters. But when I wanted to see the
difficulty of a course or how heavy the workload was, I always had to look through Reddit or search on Google, and a lot of the time it wasn't very reliable.

This is why I made RavensRate; to be able to plan your courses and see past advice given by upper year students or alumni. I scraped all the courses from Carleton's
course list, and a Carleton email is required to submit a review.

The backend includes:
* **Review System**: Users can post detailed reviews and rate courses on factors like difficulty, enjoyment, and usefulness.
* **Secure Authentication**: Implemented JWT-based authentication with Spring Security, requiring Carleton email for review submission.
* **RESTful API Design**: Provides clean, well-documented endpoints for frontend integration and third-party use.
* **PostgreSQL Database**: Uses PostgreSQL with Spring Data JPA for relational data storage and efficient querying.

### Built With
* [![Java][Java]][Java-url]
* [![Spring Boot][SpringBoot]][SpringBoot-url]
* [![PostgreSQL][PostgreSQL]][PostgreSQL-url]
* [![Maven][Maven]][Maven-url]
* [![Docker][Docker]][Docker-url]

<!-- GETTING STARTED -->
### Installation

_Below are instructions on how to install the project and get it running locally. Note that the project will run on port 8080._

1. Clone the repo
   
   ```sh
   git clone https://github.com/oceanyu1/course-review-backend.git
   ```
3. Configure environment variables by creating a .env file in the project root
   ```sh
   POSTGRES_DB=carleton_courses
   POSTGRES_USER=admin
   POSTGRES_PASSWORD=password
   ```
4. Start with Docker Compose
   
   ```sh
   docker-compose up
   ```

<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE.txt` for more information.

<!-- CONTACT -->
## Contact

Ocean - [@oceanyu1](https://github.com/oceanyu1)

Project Link: [https://github.com/oceanyu1/course-review-backend](https://github.com/oceanyu1/course-review-backend)

<!-- ACKNOWLEDGMENTS -->
## Acknowledgments

Biggest acklowledgement goes to Devtiro, specifically his <a href="https://www.youtube.com/watch?v=rki0eVGAVTQ">restaurant review platform</a> tutorial.

* [Devtiro](https://www.youtube.com/@devtiro)
* [Font Awesome](https://fontawesome.com)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[Java]: https://img.shields.io/badge/Java%2021-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white
[Java-url]: https://www.oracle.com/java/

[SpringBoot]: https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white
[SpringBoot-url]: https://spring.io/projects/spring-boot

[PostgreSQL]: https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white
[PostgreSQL-url]: https://www.postgresql.org/

[Maven]: https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white
[Maven-url]: https://maven.apache.org/

[Docker]: https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white
[Docker-url]: https://www.docker.com/
