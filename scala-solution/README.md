# Scala Solution
## Running the application
### Docker local
* Build the image with `sbt clean docker:publishLocal`
* Run the container as you would any other image. (Not this does require a database instance running on the specified port with the correct schema) See database section.

### Pre built docker image
Pull this image from DockerHub and run as you would any other image.

`docker pull thiefspin/scala-solution`

### Old School
If you have a postgres instance running with the schema you can just `sbt clean compile run`

## Application Tests 
The application runs with an embedded Postgres that spins up and down as needed.


`sbt clean coverage test coverageReport` will run all the tests and provide you with a  scoverage report. (Coverage etc).

The main components are all above 90% coverage although the app overall has only 48% coverage.

### Database
Use `docker-compose up` to init a database with the correct schema applied.