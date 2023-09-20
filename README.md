# This is the coding challenge for Software Engineering, Java at Redcare Pharmacy

## Running the service

You can run the service locally using Gradle and Docker. Running the command will make the service available at
`http://localhost:9123`:

```bash
./gradlew build \
  && docker build -t redcare-challenge . \
  && docker run -p 9123:8080 redcare-challenge
```

## Testing the service

You can run the tests locally using Gradle:

```bash
./gradlew test
```

## Endpoints

There are OpenAPI 3.0 API docs available [here](./docs/openapi.yaml).

## Next steps and improvements

- Add (JWT) authentication using Spring Security
- Set the timeout for the GitHub WebClient globally, not per request
- Remove the exception details from the 400 error response
- Use a multi-stage Docker build to remove the Gradle dependency
- ...
