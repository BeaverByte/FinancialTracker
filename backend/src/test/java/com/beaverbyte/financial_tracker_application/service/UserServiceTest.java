package com.beaverbyte.financial_tracker_application.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.beaverbyte.financial_tracker_application.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService userService;

	@Test
	void shouldBeTrueIfEmailExists() {
		String email = "fake@gmail.com";
		Mockito.when(userRepository.existsByEmail(email)).thenReturn(true);

		boolean result = userService.existsByEmail(email);

		Assertions.assertTrue(result);
		Mockito.verify(userRepository, Mockito.times(1)).existsByEmail(email);
	}

	@Test
	void shouldBeFalseIfEmailDoesNotExist() {
		String email = "bademail";
		Mockito.when(userRepository.existsByEmail(email)).thenReturn(false);

		boolean result = userService.existsByEmail(email);

		Assertions.assertFalse(result);
		Mockito.verify(userRepository, Mockito.times(1)).existsByEmail(email);
	}

	@Test
	void shouldBeTrueIfUserExists() {
		String username = "testuser";
		Mockito.when(userRepository.existsByUsername(username)).thenReturn(true);

		boolean result = userService.existsByUsername(username);

		Assertions.assertTrue(result);
		Mockito.verify(userRepository, Mockito.times(1)).existsByUsername(username);
	}

	@Test
	void shouldBeFalseIfUserDoesNotExist() {
		String username = "nonexistentuser";
		Mockito.when(userRepository.existsByUsername(username)).thenReturn(false);

		boolean result = userService.existsByUsername(username);

		Assertions.assertFalse(result);
		Mockito.verify(userRepository, Mockito.times(1)).existsByUsername(username);
	}
}
