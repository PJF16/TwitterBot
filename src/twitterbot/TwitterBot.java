/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package twitterbot;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author philipp
 */
public class TwitterBot {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      TwitterActions actions = new TwitterActions ();
      // works actions.postTweet("Hello! I'm testing my new Application!");
      int i=1;
      while (i>0){
      actions.searchTweetandRetweet("#austria");
          try {
              Thread.sleep (300000);
          } catch (InterruptedException ex) {
              Logger.getLogger(TwitterBot.class.getName()).log(Level.SEVERE, null, ex);
          }
      
      }
    }
    
}
