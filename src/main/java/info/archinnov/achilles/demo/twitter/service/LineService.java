package info.archinnov.achilles.demo.twitter.service;

import info.archinnov.achilles.demo.twitter.entity.User;
import info.archinnov.achilles.demo.twitter.model.Tweet;
import info.archinnov.achilles.entity.manager.ThriftEntityManager;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

@Service
public class LineService
{

	@Inject
	private UserService userService;

	@Inject
	private ThriftEntityManager em;

	public List<Tweet> getTimeline(String userLogin, int length)
	{
		User user = em.getReference(User.class, userLogin);
		return user.getTimeline().findLastValues(length);
	}

	public List<Tweet> getUserline(String userLogin, int lenth)
	{
		User user = em.getReference(User.class, userLogin);
		return user.getUserline().findLastValues(lenth);
	}

}
