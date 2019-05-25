package com.backend.Om.controller;


import com.backend.Om.model.Lobby;
import com.backend.Om.model.User;
import com.backend.Om.model.UserLobby;
import com.backend.Om.repository.LobbyRepository;
import com.backend.Om.repository.UserLobbyRepository;
import com.backend.Om.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lobby")
public class LobbyController {

    @Autowired
    private LobbyRepository lobbyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserLobbyRepository userLobbyRepository;


    @GetMapping("/getlobbies/{lobby}")
    public List<UserLobby> getLobbyByName(@PathVariable String lobby) throws Exception {
        return userLobbyRepository.findUserLobbiesByLobby_LobbyNameOrderByActive(lobby);
    }


}
