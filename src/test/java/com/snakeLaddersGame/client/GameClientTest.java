package com.snakeLaddersGame.client;

import com.snakeLaddersGame.models.Die;
import com.snakeLaddersGame.models.GameServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.CountDownLatch;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GameClientTest {
    private GameServiceGrpc.GameServiceStub stub;

    @BeforeAll
    public void setUp() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 6565).usePlaintext().build();
        this.stub = GameServiceGrpc.newStub(channel);
    }

    @Test
    public void clientGame() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        GameStateStreamingResponse gameStateStreamingResponse = new GameStateStreamingResponse(countDownLatch);
        StreamObserver<Die> dieStreamObserver = this.stub.roll(gameStateStreamingResponse);
        gameStateStreamingResponse.setDieStreamObserver(dieStreamObserver);
        gameStateStreamingResponse.roll();
        countDownLatch.await();
    }
}
