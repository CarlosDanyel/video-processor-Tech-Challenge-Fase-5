# FIAP X Video Processor

Java 21 / Spring Boot worker consuming durable RabbitMQ jobs. Each video is downloaded from S3-compatible storage, FFmpeg extracts one PNG per second, and the worker uploads a ZIP. Result events use publisher confirmations. Jobs are acknowledged after the result is confirmed; dead-letter queues retain exhausted failures.

Java sources and tests live under `src/main/java/Tech_Challenge_Fase_5/video_processor_Tech_Challenge_Fase_5` and `src/test/java/Tech_Challenge_Fase_5/video_processor_Tech_Challenge_Fase_5`. The package root is `Tech_Challenge_Fase_5.video_processor_Tech_Challenge_Fase_5`.

## Run and test

Start infra dependencies, copy `.env.example` to `.env`, set `S3_ENDPOINT` and RabbitMQ credentials, export them with `set -a; source .env; set +a`, then run `JAVA_HOME=$(/usr/libexec/java_home -v 21) ./gradlew bootRun`. FFmpeg must be in `PATH`. Run `./gradlew clean test` for frame and ZIP tests. The Docker image installs FFmpeg. Prometheus metrics: `/actuator/prometheus` on port 8081.

The worker is stateless and can be scaled horizontally with Kubernetes replicas. The release branch tests and publishes a GHCR image; master deploys after promotion from release when deployment variables are configured. See the [infra documentation](https://github.com/CarlosDanyel/INFRA-Tech-Challenge-Fase-5).
