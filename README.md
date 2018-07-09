# car-adverts

PLEASE NOTE THAT THIS PROJECT IS NOT COMPLETE, AND IS UNUSABLE.

This is an application on play which helps you create, modify and delete car advertisements.


#### What works? 

- Dockerisation; DynamoDB and the play application both run on containers locally

- Basic project setup, and data modelling. There is some dummy data which is returned on doing a GET call to /v1/adverts.

#### What doesnt work?

- Everything else

### Setup

This application runs on docker.


```bash
cd docker
docker-compose up
```

There are two containers - one runs sbt, and the other runs a local DynamoDB instance. the application runs on port 9000, while DynamoDB runs on port 8000.

Play will start up on the HTTP port at <http://localhost:9000/>.   You don't need to deploy or reload anything -- changing any source code while the server is running will automatically recompile and hot-reload the application on the next HTTP request. 

### Usage

If you call the same URL from the command line, you’ll see JSON. Using httpie, we can execute the command:

```bash
http --verbose http://localhost:9000/v1/adverts
```

and get back:

```routes
GET /v1/adverts HTTP/1.1
```
