package com.snakeLaddersGame.server;

import com.snakeLaddersGame.models.Die;
import com.snakeLaddersGame.models.GameState;
import com.snakeLaddersGame.models.Player;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.ThreadLocalRandom;

public class DieStreamingRequest implements StreamObserver<com.snakeLaddersGame.models.Die> {

    private StreamObserver<GameState> gameStateStreamObserver;
    private Player client;
    private Player server;

    public DieStreamingRequest(StreamObserver<GameState> gameStateStreamObserver, Player client, Player server) {
        this.gameStateStreamObserver = gameStateStreamObserver;
        this.client = client;
        this.server = server;
    }

    @Override
    public void onNext(Die die) {
        client = this.getNewPlayerPosition(client, die.getValue());
        if (client.getPosition() != 100) {
            this.server = this.getNewPlayerPosition(server, ThreadLocalRandom.current().nextInt(1, 7));
        }
        this.gameStateStreamObserver.onNext(getGameState());
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {
        this.gameStateStreamObserver.onCompleted();
    }

    private GameState getGameState() {
        return  GameState.newBuilder()
                .addPlayer(client)
                .addPlayer(server)
                .build();
    }
    private Player getNewPlayerPosition(Player player, int dieValue) {
        int position = player.getPosition() + dieValue;

        position = SnakesAndLaddersMap.getPosition(position);

        if (position <= 100) {
            player = player.toBuilder()
                    .setPosition(position)
                    .build();
        }
        return player;
    }
}
