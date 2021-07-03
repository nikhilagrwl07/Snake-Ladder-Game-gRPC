package com.snakeLaddersGame.server;

import com.snakeLaddersGame.models.GameServiceGrpc.GameServiceImplBase;
import com.snakeLaddersGame.models.Player;
import io.grpc.stub.StreamObserver;

public class GameService extends GameServiceImplBase {
    @Override
    public StreamObserver<com.snakeLaddersGame.models.Die> roll(StreamObserver<com.snakeLaddersGame.models.GameState> responseObserver) {
        Player client = Player.newBuilder().setName("client").setPosition(0).build();
        Player server = Player.newBuilder().setName("server").setPosition(0).build();

        return new DieStreamingRequest(responseObserver, client, server);
    }
}
