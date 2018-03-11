package edu.knoldus.application;

import edu.knoldus.operation.TwitterOperations;
import twitter4j.Status;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class MainApplication {
    public static void main(String[] args) throws InterruptedException {
        LocalDate localDate = LocalDate.now().minusDays(1);
        Optional<CompletableFuture<List<Status>>> listStatus = TwitterOperations.getPostNewToOlder();
        Thread.sleep(5000);
        TwitterOperations.printStatusList(listStatus);
        listStatus = TwitterOperations.getPostOlderToNew();
        Thread.sleep(5000);
        TwitterOperations.printStatusList(listStatus);
        CompletableFuture<List<String>> reTweetList = TwitterOperations.getReTweetsHigherToLower();
        Thread.sleep(5000);
        reTweetList.thenAccept(strings -> strings.forEach(System.out::println));
        CompletableFuture<List<String>> likesCountList = TwitterOperations.getLikesHigherToLower();
        Thread.sleep(5000);
        likesCountList.thenAccept(strings -> strings.forEach(System.out::println));
        likesCountList = TwitterOperations.getNumberAndListOfTweetOnDate(localDate);
        Thread.sleep(10000);
        likesCountList.thenAccept(strings -> strings.forEach(System.out::println));

    }
}
