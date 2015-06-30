/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitterbot;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 *
 * @author philipp
 */
public class TwitterActions {

    Twitter twitter;
    long lastStatusTime;
    ArrayList<Status> tweetAlreadyused;
    ArrayList<Status> tweets;

    public TwitterActions() {
        this.lastStatusTime = this.lastRetweetFromFile();
        this.tweetAlreadyused = new ArrayList<Status>();
        if (this.twitter != null) {

        } else {
            this.twitter = TwitterFactory.getSingleton();
        }
    }

    public void postTweet(String tweet) {
        try {
            Status status = this.twitter.updateStatus(tweet);
        } catch (TwitterException ex) {
            Logger.getLogger(TwitterActions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
     not finished - DO NOT USE!!!
     */
    public void searchTweetandRetweet(String hashtag) {
        Query query = new Query(hashtag);
        query.count(40);
        int check = 0;
        int alreadyused = 0;
        int remove = 0;

        try {
            QueryResult queryresult = this.twitter.search(query);
            this.tweets = (ArrayList<Status>) queryresult.getTweets();
            Collections.reverse(tweets);
            for (Status status : tweets) {
                if (!status.isRetweetedByMe() && status.getCreatedAt().getTime() > this.lastStatusTime) {
                    try {
                        this.retweetTweet(status);
                        this.lastStatusTime = status.getCreatedAt().getTime();
                        this.writeTimeToFile();
                        Thread.sleep(10000);
                    } catch (TwitterException tex) {
                        System.out.println("Twitter Exception - Already Retweeted");
                    }
                }
            }
        } catch (TwitterException ex) {
            Logger.getLogger(TwitterActions.class.getName()).log(Level.SEVERE, null, ex);

        } catch (InterruptedException ex) {
            Logger.getLogger(TwitterActions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void retweetTweet(Status status) throws TwitterException {
        this.twitter.retweetStatus(status.getId());
    }

    private long lastRetweetFromFile() {
        long lastretweet = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader("time.dat"));
            lastretweet = Long.parseLong(br.readLine());
        } catch (FileNotFoundException ex) {
            lastretweet = new Date().getTime();
            try {
                PrintWriter pw = new PrintWriter("time.dat");
                pw.print(new Date().getTime());
                pw.close();
            } catch (FileNotFoundException ex1) {
                Logger.getLogger(TwitterActions.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (IOException ex) {
            lastretweet = new Date().getTime();
            try {
                PrintWriter pw = new PrintWriter("time.dat");
                pw.print(new Date().getTime());
                pw.close();
            } catch (FileNotFoundException ex1) {
                Logger.getLogger(TwitterActions.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

        return lastretweet;

    }

    public void writeTimeToFile() {
        try {
            PrintWriter pw = new PrintWriter("time.dat");
            pw.print(this.lastStatusTime);
            pw.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(TwitterActions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
