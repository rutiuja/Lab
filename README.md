# Lab

## Java health-check application

This minimal Java HTTP service exposes `GET /health` on port `8080` and returns
`{"status":"UP"}` when running. The Docker image includes a health check that
requests the endpoint.

Build and start the container:

```sh
docker build -t java-health-app .
docker run -d --name java-health-app -p 8080:8080 java-health-app
```

Check the endpoint and Docker's health status:

```sh
curl http://localhost:8080/health
docker inspect --format='{{.State.Health.Status}}' java-health-app
```

Docker reports `starting` until the first health check succeeds, then `healthy`.
To stop and remove the container, run `docker rm -f java-health-app`.
