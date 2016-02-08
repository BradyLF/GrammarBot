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
        searches.add("\"your welcome\"");
        searches.add("\"your the\"");
        searches.add("\"your a \"");
        searches.add("\"your my \"");
        searches.add("\"your kidding \"");

        List<String> replies = new ArrayList();
        replies.add(" I believe you meant \"you're\" here?");
        replies.add(" I've detected the wrong \"you're\".");
        replies.add(" You may want to use the correct \"you're\"");
        replies.add(" Try again. You used \"your\" not \"you're\"");
        replies.add(" Bot here! Inccorect \"your\" usage");
        replies.add(" Hold it! Try again using \"you're\"");
        replies.add(" Aw snap! someone (you) used the incorrect \"you're\"");
        int tweetCount = 14;
        
        //keep tweeting forever
        while(true){

            //create a new search, chosoe from random searches
            Query query = new Query(searches.get((int)(searches.size()*Math.random())));

            //get the results from that search
            QueryResult result = twitter.search(query);

            //get the first tweet from those results
            Status tweetResult = result.getTweets().get(0);

            String your = tweetResult.getText();

            int index = 0;

            index = your.indexOf("your");

            if (index == -1){ index = 0; }

            your = your.substring(index);

            if (your.length() > 30){
                your = your.substring(0,30);
            }

            //reply to that tweet, choose from random replies
            StatusUpdate statusUpdate = new StatusUpdate(".@" + tweetResult.getUser().getScreenName() + ": \"" + your + "...\" " + replies.get((int)(replies.size()*Math.random())));
            statusUpdate.inReplyToStatusId(tweetResult.getId());
            Status status = twitter.updateStatus(statusUpdate); 

            if ((tweetCount % 100) == 0){
                Status count = twitter.updateStatus("Celebrating " + tweetCount + " tweets!");
            }

            System.out.println("Sleeping.");

            //go to sleep for an hour
            Thread.sleep(960000);
        }
    }
}