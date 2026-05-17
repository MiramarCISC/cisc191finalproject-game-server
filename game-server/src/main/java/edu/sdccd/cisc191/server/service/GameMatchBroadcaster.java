package edu.sdccd.cisc191.server.service;

import edu.sdccd.cisc191.server.engine.OnlineMatch;

public interface GameMatchBroadcaster {
    void broadcastMatchUpdate(String matchId, OnlineMatch match, Runnable cleanupCallback);
}
