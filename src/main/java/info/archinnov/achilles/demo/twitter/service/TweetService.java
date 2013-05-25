package info.archinnov.achilles.demo.twitter.service;

import info.archinnov.achilles.demo.twitter.entity.TweetIndex;
import info.archinnov.achilles.demo.twitter.entity.User;
import info.archinnov.achilles.demo.twitter.model.Tweet;
import info.archinnov.achilles.entity.manager.ThriftEntityManager;
import info.archinnov.achilles.type.KeyValueIterator;
import info.archinnov.achilles.type.WideMap;

import java.util.UUID;

import javax.inject.Inject;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;

import org.apache.commons.lang.StringUtils;
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

	public Tweet removeTweet(String tweetId)
	{

		TweetIndex tweetIndex = fetchTweetIndex(tweetId);

		Tweet tweet = tweetIndex.getTweet();
		UUID id = tweet.getId();

		User user = em.find(User.class, tweet.getAuthor());
		user.getUserline().remove(id);
		user.getTimeline().remove(id);
		user.getTweetsCounter().decr();

		// Remove from all users timeline
		KeyValueIterator<String, String> timelineUsers = tweetIndex.getTimelineUsers().iterator();
		while (timelineUsers.hasNext())
		{
			String userLogin = timelineUsers.nextKey();
			if (StringUtils.isNotBlank(userLogin))
			{
				User follower = em.getReference(User.class, userLogin);
				follower.getTimeline().remove(id);
			}
		}

		em.remove(tweetIndex);

		return tweet;
	}

	private void spreadTweetCreation(Tweet tweet, User user, TweetIndex tweetIndex)
	{

		// Add current tweet to user timeline & userline
		user.getTimeline().insert(tweet.getId(), tweet);
		user.getUserline().insert(tweet.getId(), tweet);

		// Index current tweet as in user timeline
		WideMap<String, String> timelineUsers = tweetIndex.getTimelineUsers();
		timelineUsers.insert(user.getLogin(), "");

		KeyValueIterator<String, User> followers = user.getFollowers().iterator(10);
		while (followers.hasNext())
		{
			User follower = followers.nextValue();

			// Add tweet to follower timeline
			follower.getTimeline().insert(tweet.getId(), tweet);
			timelineUsers.insert(follower.getLogin(), "");
		}
	}

}
