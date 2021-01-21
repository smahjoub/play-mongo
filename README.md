Reactivemongo for Play 2.8 Example
=======================
This is a Play CRUD Example using reactiveMongo driver. It demontrates:
<ul>
<li>MongoDb Connection using reactiveMongoDb in Play</li>
<li>BSONReader and BSONWriter Implementation</li>
<li>CRUD using different data type</li>
</ul>

This example use the following:
<ul>
<li>Play Framework 2.8.0</li>
<li>Reactive Scala Driver for MongoDB 0.20.13-play28</li>
<li>Scala 2.13.3</li>
<li>Mongodb</li>
</ul>

### How to run
Setup a local mongo server using docker:
```shell script
mkdir ~/data
docker run --name local-mongodb -d -p 27017:27017 -v ~/data:/data/db mongo
```
Test and run the application:
```shell script
sbt test
sbt run
```
