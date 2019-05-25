package com.backend.Om;

import com.backend.Om.model.Lobby;
import com.backend.Om.model.User;
import com.backend.Om.model.UserLobby;
import com.backend.Om.repository.LobbyRepository;
import com.backend.Om.repository.UserLobbyRepository;
import com.backend.Om.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//import org.springframework.boot.builder.SpringApplicationBuilder;
//import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
@SpringBootApplication
public class OmApplication implements CommandLineRunner {

	/*@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(OmApplication.class);
	}*/

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private LobbyRepository lobbyRepository;
	@Autowired
	private UserLobbyRepository userLobbyRepository;

	public static void main(String[] args) {
		SpringApplication.run(OmApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Cleanup the tables
		userLobbyRepository.deleteAll();
		lobbyRepository.deleteAll();
		userRepository.deleteAll();
	}
}
