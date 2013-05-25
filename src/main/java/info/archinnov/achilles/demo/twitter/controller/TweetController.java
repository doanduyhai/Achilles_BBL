package info.archinnov.achilles.demo.twitter.controller;

import info.archinnov.achilles.demo.twitter.model.Tweet;
import info.archinnov.achilles.demo.twitter.service.TweetService;

import java.util.UUID;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/tweet", produces = "application/json")
public class TweetController
{

	@Inject
	private TweetService tweetService;

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public UUID postTweet(@RequestParam(required = true) String authorLogin,
			@RequestBody(required = true) String content)
	{
		return tweetService.createTweet(authorLogin, content);
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public Tweet getTweet(@RequestParam(required = true) String tweetId)
	{
		return tweetService.getTweet(tweetId);
	}

	@RequestMapping(method = RequestMethod.DELETE)
	@ResponseBody
	public Tweet removeTweet(@RequestParam(required = true) String tweetId)
	{
		return tweetService.removeTweet(tweetId);
	}
}
