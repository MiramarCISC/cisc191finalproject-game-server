//package edu.sdccd.cisc191.server;
//
//import edu.sdccd.cisc191.grpc.JoinMatchRequest;
//import edu.sdccd.cisc191.grpc.JoinMatchResponse;
//import edu.sdccd.cisc191.grpc.MatchHistoryRequest;
//import edu.sdccd.cisc191.grpc.MatchHistoryResponse;
//import edu.sdccd.cisc191.grpc.MatchResultResponse;
//import edu.sdccd.cisc191.grpc.PlayMatchRequest;
//import edu.sdccd.cisc191.server.grpc.GameGrpcService;
//import edu.sdccd.cisc191.server.repository.MatchRepository;
//import io.grpc.stub.StreamObserver;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class GameGrpcServiceTest {
//
//    @Autowired
//    private GameGrpcService service;
//
//    @Autowired
//    private MatchRepository matchRepository;
//
//    @BeforeEach
//    void clearDatabase() {
//        matchRepository.deleteAll();
//    }
//
//    @Test
//    void joinMatchReturnsMatchDetailsAndPersistsMatch() {
//        TestObserver<JoinMatchResponse> observer = new TestObserver<>();
//
//        service.joinMatch(
//                JoinMatchRequest.newBuilder()
//                        .setPlayerName("Ada")
//                        .setDifficulty("Hard")
//                        .setRanked(true)
//                        .build(),
//                observer
//        );
//
//        assertNotNull(observer.value);
//        assertTrue(observer.completed);
//        assertEquals("Ada", observer.value.getPlayerName());
//        assertEquals("Bot (Hard)", observer.value.getOpponentName());
//        assertEquals("Hard", observer.value.getDifficulty());
//        assertTrue(observer.value.getRanked());
//        assertTrue(matchRepository.existsById(observer.value.getMatchId()));
//    }
//
////    @Test
////    void playMatchReturnsWinnerAndUpdatesSavedMatch() {
////        TestObserver<JoinMatchResponse> joinObserver = new TestObserver<>();
////
////        service.joinMatch(
////                JoinMatchRequest.newBuilder()
////                        .setPlayerName("Ada")
////                        .setDifficulty("Normal")
////                        .build(),
////                joinObserver
////        );
////
////        TestObserver<MatchResultResponse> playObserver = new TestObserver<>();
////
////        service.playMatch(
////                PlayMatchRequest.newBuilder()
////                        .setMatchId(joinObserver.value.getMatchId())
////                        .setPlayerName("Ada")
////                        .build(),
////                playObserver
////        );
////
////        assertNotNull(playObserver.value);
////        assertTrue(playObserver.completed);
////        assertFalse(playObserver.value.getWinnerName().isBlank());
////        assertFalse(playObserver.value.getLoserName().isBlank());
////        assertNotEquals(playObserver.value.getWinnerName(), playObserver.value.getLoserName());
////
////        var saved = matchRepository.findById(joinObserver.value.getMatchId()).orElseThrow();
////        assertTrue(saved.isComplete());
////        assertEquals(playObserver.value.getWinnerName(), saved.getWinnerName());
////    }
//
//    @Test
//    void loadMatchHistoryReadsSavedMatches() {
//        TestObserver<JoinMatchResponse> joinObserver = new TestObserver<>();
//
//        service.joinMatch(
//                JoinMatchRequest.newBuilder()
//                        .setPlayerName("Ada")
//                        .setDifficulty("Easy")
//                        .build(),
//                joinObserver
//        );
//
//        service.playMatch(
//                PlayMatchRequest.newBuilder()
//                        .setMatchId(joinObserver.value.getMatchId())
//                        .setPlayerName("Ada")
//                        .build(),
//                new TestObserver<>()
//        );
//
//        TestObserver<MatchHistoryResponse> historyObserver = new TestObserver<>();
//
//        service.loadMatchHistory(
//                MatchHistoryRequest.newBuilder()
//                        .setPlayerName("Ada")
//                        .build(),
//                historyObserver
//        );
//
//        assertNotNull(historyObserver.value);
//        assertTrue(historyObserver.completed);
//        assertTrue(historyObserver.value.getMatchesCount() >= 1);
//        assertTrue(historyObserver.value.getMatches(0).contains("Ada"));
//    }
//
//    private static class TestObserver<T> implements StreamObserver<T> {
//        private T value;
//        private boolean completed;
//
//        @Override
//        public void onNext(T value) {
//            this.value = value;
//        }
//
//        @Override
//        public void onError(Throwable throwable) {
//            fail(throwable);
//        }
//
//        @Override
//        public void onCompleted() {
//            this.completed = true;
//        }
//    }
//}
