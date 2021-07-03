package com.snakeLaddersGame.client;

import com.google.common.util.concurrent.Uninterruptibles;
import com.snakeLaddersGame.models.Die;
import com.snakeLaddersGame.models.GameState;
import com.snakeLaddersGame.models.Player;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class GameStateStreamingResponse implements StreamObserver<GameState> {

    private CountDownLatch countDownLatch;
    private StreamObserver<Die> dieStreamObserver;

    public GameStateStreamingResponse(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void onNext(GameState gameState) {
        List<Player> playerList = gameState.getPlayerList();
        playerList.forEach(player -> {
            System.out.println(player.getName() + " : " + player.getPosition());
        });

        boolean anyPlayerReached100 = playerList.stream().anyMatch(player -> player.getPosition() == 100);
        if (anyPlayerReached100) {
            System.out.println("Game Over !");
            this.dieStreamObserver.onCompleted();
        } else {
            Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
            this.roll();
        }
        System.out.println("----------------------");
    }

    public void setDieStreamObserver(StreamObserver<Die> dieStreamObserver) {
        this.dieStreamObserver = dieStreamObserver;
    }

    public void roll() {
        int dieValue = ThreadLocalRandom.current().nextInt(1, 7);
        Die die = Die.newBuilder().setValue(dieValue).build();
        this.dieStreamObserver.onNext(die);
    }

    @Override
    public void onError(Throwable throwable) {
        this.countDownLatch.countDown();
    }

    @Override
    public void onCompleted() {
        this.countDownLatch.countDown();
    }
}
