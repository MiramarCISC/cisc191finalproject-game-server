package edu.sdccd.cisc191.server;

import edu.sdccd.cisc191.server.grpc.GameGrpcService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GameServerApplication {

    private Server grpcServer;

    public static void main(String[] args) {
        SpringApplication.run(GameServerApplication.class, args);
    }

    @Bean
    CommandLineRunner startGrpcServer(
            GameGrpcService gameGrpcService,
            @Value("${game.grpc.port:50052}") int grpcPort
    ) {
        return args -> {
            grpcServer = ServerBuilder
                    .forPort(grpcPort)
                    .addService(gameGrpcService)
                    .build()
                    .start();

            System.out.println("Spring Boot 3 gRPC Game Server started on port " + grpcPort);

            Thread awaitThread = new Thread(() -> {
                try {
                    grpcServer.awaitTermination();
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                }
            });

            awaitThread.setDaemon(false);
            awaitThread.start();
        };
    }

    @Bean
    DisposableBean stopGrpcServer() {
        return () -> {
            if (grpcServer != null) {
                grpcServer.shutdown();
            }
        };
    }
}
