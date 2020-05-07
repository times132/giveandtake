package com.buysell.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ChatUserRepositoryTest {
    @Autowired
    private ChatUsersRepository chatUserRepository;

    @Test
    public void deleteChatUser(){
        chatUserRepository.deleteById(10L);
    }
}
