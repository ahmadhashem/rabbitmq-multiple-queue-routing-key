This app demonstrates how to define multiple RabbitMQ queues that binds to the same ecxchange, and based on the routing key, the message is routed to the related queue

The app defines two queues, one is processing and other is archive. The application then listens to both queues using two consumers for each queue.

The consumers logs the app to the std out.

The application includes a runnable that send two messages , one that should be routed to both processing and archive queue, and one that should be only routed to processing queue.

You can see after running the application, how the consumers log the messages consumed

In order to start the app, make sure to start rabbit mq first, using the command:
docker-compose up
The command uses docker-compose.yaml that starts a docker container based on the rabbitmq image, and mount the configurations to the docker container that contains the definitions to be loaded by rabbitmq

Then in another terminal, simply run the following command and notice in the log messages from the consumers on the console

./gradlew bootRun
