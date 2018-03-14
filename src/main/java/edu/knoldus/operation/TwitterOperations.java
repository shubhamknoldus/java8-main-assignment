package edu.knoldus.operation;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class TwitterOperations {
    /**
     * Getting instance of twitter factory.
     */
    static Twitter twitter = TwitterFactory.getSingleton();
    static Query query;

    /**
     * @param listStatus prints the list of Status but only getCreatedAt() and getText().
     */
    public static void printStatusList(Optional<CompletableFuture<List<Status>>> listStatus) {
        CompletableFuture<List<Status>> list = listStatus
                .orElseGet(() -> CompletableFuture.completedFuture(new ArrayList<Status>()));
        list.thenAccept(statuses -> statuses.forEach(status -> {
            System.out.println(status.getCreatedAt() + " " + status.getText() + "\n");
        }));
    }

    /**
     * @return a list of status in Newer to Older fashion.
     */
    public static Optional<CompletableFuture<List<Status>>> getPostNewToOlder() {
        return Optional.of(CompletableFuture.supplyAsync(() -> {
            List<Status> statusList = new ArrayList<>();
            try {
                statusList = twitter.getHomeTimeline().stream()
                        .sorted(Comparator.comparing(Status::getCreatedAt))
                        .collect(Collectors.toList());
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return statusList;
        }));
    }

    /**
     * @return a list of status in Older to Newer fashion.
     */
    public static Optional<CompletableFuture<List<Status>>> getPostOlderToNew() {
        return Optional.of(getPostNewToOlder()
                .get()
                .thenApply(statuses -> {
                    Collections.reverse(statuses);
                    return statuses;
                }));
    }

    /**
     * @return returns a list of string displaying ReTweets Higher to Lower.
     */
    public static CompletableFuture<List<String>> getReTweetsHigherToLower() {
        return CompletableFuture.supplyAsync(() -> {
            List<Status> statusList = new ArrayList<>();
            try {
                statusList = twitter.getHomeTimeline()
                        .stream()
                        .sorted(Comparator.comparing((Status::getRetweetCount)).reversed())
                        .collect(Collectors.toList());
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return statusList;
        }).thenApply(statuses ->
                statuses.stream()
                        .map(status -> status.getRetweetCount() + " " + status.getText())
                        .collect(Collectors.toList()));
    }

    /**
     * @return returns a list of string displaying Likes Higher to Lower.
     */
    public static CompletableFuture<List<String>> getLikesHigherToLower() {
        return CompletableFuture.supplyAsync(() -> {
            List<Status> statusList = new ArrayList<>();
            try {
                statusList = twitter.getHomeTimeline()
                        .stream()
                        .sorted(Comparator.comparing((Status::getRetweetCount)).reversed())
                        .collect(Collectors.toList());
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return statusList;
        }).thenApply(statuses ->
                statuses.stream()
                        .map(status -> status.getFavoriteCount() + " " + status.getText())
                        .collect(Collectors.toList()));
    }

    /**
     * @param date for which the tweets need to be displayed.
     * @return a list of tweets on the supplied date formatted as a string including date and test.
     */
    public static CompletableFuture<List<String>> getNumberAndListOfTweetOnDate(LocalDate date) {

        return CompletableFuture.supplyAsync(() -> {
            List<Status> statusList = null;
            query = new Query("#barcelona");
            query.setSince(date.minusDays(1).toString());
            query.setUntil(date.toString());
            query.setCount(50);
            try {
                QueryResult queryResult = twitter.search(query);
                System.out.println(queryResult.getTweets().size());
                statusList = queryResult.getTweets();
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return statusList;
        }).thenApply(statuses -> statuses.stream()
                .map(status -> status.getText() + " " + status.getCreatedAt() + "\n").collect(Collectors.toList()));
    }


}
