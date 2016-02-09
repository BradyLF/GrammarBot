import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class GrammarBot{
    //if something goes wrong, we might see a TwitterException
    public static void main() throws TwitterException, InterruptedException{

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
        .setOAuthConsumerKey("[YOUR_CONSUMER_KEY]")
        .setOAuthConsumerSecret("[YOUR_CONSUMER_SECRET]")
        .setOAuthAccessToken("[YOUR_ACCESS_TOKEN]")
        .setOAuthAccessTokenSecret("[YOUR_TOKEN_SECRET]");

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        List<String> searches = new ArrayList();
        searches.add("\"your welcome \"");
        searches.add("\"your the \"");
        searches.add("\"your a \"");
        searches.add("\"your always \"");
        searches.add("\"your my \"");
        searches.add("\"your kidding \"");
        searches.add("\"your not \"");
        searches.add("\"your the best \"");
        searches.add("\"your making \"");


        List<String> replies = new ArrayList();
        replies.add(" I believe you meant \"you're\" here?");
        replies.add(" I've detected the wrong \"you're\".");
        replies.add(" You may want to use the correct \"you're\"");
        replies.add(" Uh oh! You used \"your\" not \"you're\"");
        replies.add(" Bot here! Incorrect \"your\" usage");
        replies.add(" Hold it! Try again using \"you're\"");
        replies.add(" Aw snap! someone (you) used the incorrect \"you're\"");
        int tweetCount = 132; //starting at this number as tweets were already on the account
        
        //keep tweeting forever
        while(true){

            //create a new search, chosoe from random searches
            Query query = new Query(searches.get((int)(searches.size()*Math.random())));

            //get the results from that search
            QueryResult result = twitter.search(query);

            //get the first tweet from those results
            Status tweetResult = result.getTweets().get(0);
            
            //locating the "your" for quote purposes (people keep deleting the tweets I reply to, shocker)
            String your = tweetResult.getText();
            int index = 0;
            index = your.indexOf("your");                       //look for "your "    
            if (index == -1){ index = your.indexOf("Your"); }   //if no "your" look for "Your"
            if (index == -1){ index = 0; }                      //Still nothing? just post the beginning of the tweet
            your = your.substring(index);                       //set the string equal to the "your" and on"

            //make sure you don't go over 140 chars
            if (your.length() > 30){
                your = your.substring(0,30);
            }

            
            //reply to that tweet, choose from random replies
            StatusUpdate statusUpdate = new StatusUpdate(".@" + tweetResult.getUser().getScreenName() + ": \"" + your + "...\" " + replies.get((int)(replies.size()*Math.random())));
            statusUpdate.inReplyToStatusId(tweetResult.getId());
            Status status = twitter.updateStatus(statusUpdate);
            
            //add to the tweet count and print it
            tweetCount++;
            System.out.println("\nI'm back! Sending Tweet Number " + tweetCount);

            //tweet celebration every 100 tweets
            if ((tweetCount % 100) == 0){
                Status count = twitter.updateStatus("Celebrating " + tweetCount + " tweets!");
                //go to sleep for five minutes
                Thread.sleep(60*1000);
            }
            
            //github plug every 150 tweets
            if ((tweetCount % 150) == 0){
                Status count = twitter.updateStatus("I'm just a robot, but you can check out my source code on GitHub. https://github.com/BradyLF/GrammarBot");
                //go to sleep for five minutes
                Thread.sleep(60*1000);
            }
            
            System.out.println("\nTaking my five minute break...");

            //go to sleep for five minutes
            Thread.sleep(5*60*1000);
        }
    }
}