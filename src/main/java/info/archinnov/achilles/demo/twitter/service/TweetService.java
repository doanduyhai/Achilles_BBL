package info.archinnov.achilles.demo.twitter.service;

import info.archinnov.achilles.demo.twitter.entity.TweetIndex;
import info.archinnov.achilles.demo.twitter.entity.User;
import info.archinnov.achilles.demo.twitter.model.Tweet;
import info.archinnov.achilles.entity.manager.ThriftEntityManager;
import info.archinnov.achilles.type.KeyValueIterator;

import java.util.UUID;

import javax.inject.Inject;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;

import org.springframework.stereotype.Service;

/**
 * TweetAchillesService
 * 
 * @author DuyHai DOAN
 * 
 */
@Service
public class TweetService
{

	@Inject
	private ThriftEntityManager em;

	@Inject
	private UserService userService;

	public UUID createTweet(String authorLogin, String content)
	{
		System.out.println(" ***************************** tweet content = " + content);
		User author = em.find(User.class, authorLogin);

		if (author == null)
		{
			throw new IllegalArgumentException("User with login '" + authorLogin
					+ "' does no exist");
		}

		UUID uuid = TimeUUIDUtils.getUniqueTimeUUIDinMillis();

		Tweet tweet = new Tweet(uuid, author, content);

		// Initialize tweetIndex entity
		TweetIndex tweetIndex = em.find(TweetIndex.class, tweet.getId());
		if (tweetIndex == null)
		{
			tweetIndex = em.merge(new TweetIndex(tweet.getId(), tweet));
		}

		spreadTweetCreation(tweet, author, tweetIndex);
		author.getTweetsCounter().incr();

		return uuid;
	}

	public Tweet getTweet(String tweetId)
	{
		Tweet tweet = null;
		TweetIndex tweetIndex = em.find(TweetIndex.class, UUID.fromString(tweetId));
		if (tweetIndex != null)
		{
			tweet = tweetIndex.getTweet();
		}
		return tweet;
	}

	public TweetIndex fetchTweetIndex(String tweetId)
	{
		TweetIndex tweetIndex = em.find(TweetIndex.class, UUID.fromString(tweetId));
		if (tweetIndex == null)
		{
			throw new IllegalArgumentException("Cannot find tweet with id '" + tweetId
					+ "' to remove");
		}
		return tweetIndex;
	}

	private void spreadTweetCreation(Tweet tweet, User user, TweetIndex tweetIndex)
	{

		// Add current tweet to user timeline & userline
		user.getTimeline().insert(tweet.getId(), tweet);
		user.getUserline().insert(tweet.getId(), tweet);

		KeyValueIterator<String, User> followers = user.getFollowers().iterator(10);
		while (followers.hasNext())
		{
			User follower = followers.nextValue();

			// Add tweet to follower timeline
			follower.getTimeline().insert(tweet.getId(), tweet);
		}
	}

}
