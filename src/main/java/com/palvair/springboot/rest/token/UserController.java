package com.palvair.springboot.rest.token;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	@RequestMapping(value = "/api/users/current", method = RequestMethod.GET)
	public User getCurrent() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof UserAuthentication) {
			return ((UserAuthentication) authentication).getDetails();
		}
		return new User(authentication.getName()); // anonymous user support
	}

	@SuppressWarnings("serial")
	@RequestMapping(value = "/admin/api/users", method = RequestMethod.GET)
	public List<User> list() {
		final User user = new User();
		user.setUsername("admin");
		user.setPassword(new BCryptPasswordEncoder().encode("admin"));
		user.setRoles(new HashSet<UserRole>() {
			{
				add(UserRole.ADMIN);
			}
		});
		return Arrays.asList(user);
	}
}
