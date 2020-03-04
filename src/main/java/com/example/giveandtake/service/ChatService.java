package com.example.giveandtake.service;

import com.example.giveandtake.DTO.ChatMessageDTO;
import com.example.giveandtake.DTO.ChatRoomDTO;
import com.example.giveandtake.common.CustomUserDetails;
import com.example.giveandtake.model.entity.ChatMessage;
import com.example.giveandtake.model.entity.ChatRoom;
import com.example.giveandtake.model.entity.User;
import com.example.giveandtake.repository.ChatMessageRepository;
import com.example.giveandtake.repository.ChatRoomRepository;
import com.example.giveandtake.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final SimpMessageSendingOperations messagingTemplate;


    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private UserRepository userRepository;


    // 채팅방 생성순서 최근 순으로 반환
    public List<ChatRoom> findAllRoom() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String my_id= ((CustomUserDetails) authentication.getPrincipal()).getUsername();
        System.out.println("채팅방리스트반환" +my_id);

        List<ChatRoom> chatList = new ArrayList<>();
        chatList.addAll(chatRoomRepository.findByRequest(my_id));
        chatList.addAll(chatRoomRepository.findByReceiver(my_id));
        System.out.println("chatList"+chatList);
        Collections.reverse(chatList);
        return chatList;
    }

    public List<ChatRoom> findRoomById(Long roomId) {
        return chatRoomRepository.findByRoomId(roomId);
    }

    //채팅방만들기
    public List<ChatRoom> createChatRoom(String roomName, String receiver, Principal principal){
        System.out.println("***************create room**************" + roomName);
        ChatRoomDTO chatRoomDTO =new ChatRoomDTO();
        chatRoomDTO.setRoomName(roomName);
        chatRoomDTO.setReceiver(receiver);
        List<User> userList = Collections.singletonList(userRepository.findByUsername(principal.getName()).get());
        chatRoomDTO.setChatMembers(userList);
        chatRoomDTO.setRequest(principal.getName());
        chatRoomRepository.save(chatRoomDTO.toEntity()).getRoomId();


        List<ChatRoom> chatRoom = chatRoomRepository.findByRoomName(roomName);
        return chatRoom;
    }

    //대화내용 저장
    public void createMessage(ChatMessageDTO chatMessageDTO) {

        if (ChatMessage.MessageType.ENTER.equals(chatMessageDTO.getType())) {
            chatMessageDTO.setMessage(chatMessageDTO.getSender() + "님이 입장하셨습니다.");
            chatMessageDTO.setSender("[알림]");
        } else if (ChatMessage.MessageType.QUIT.equals(chatMessageDTO.getType())) {
            chatMessageDTO.setMessage(chatMessageDTO.getSender() + "님이 방에서 나갔습니다.");
            chatMessageDTO.setSender("[알림]");
        }
        Long msgNum = chatMessageRepository.save(chatMessageDTO.toEntity()).getMsgNum();
        Optional <ChatMessage> chatMessageList = chatMessageRepository.findByMsgNum(msgNum);
        ChatMessage chatMessage = chatMessageList.get();
        System.out.println(chatMessage.getRoomId());


        messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessageDTO.getRoomId(), chatMessage);


    }

    public void deleteChatRoom(Long roomId) {

        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%끝 delete"+roomId);
        chatRoomRepository.deleteById(roomId);

    }

//    public List<ChatMessage> findMessages(Long roomId) {
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$findMessage"+roomId);
//        List<ChatMessage> messageList =chatMessageRepository.findMessageByRoomId(roomId);
//        Collections.reverse(messageList);
//        System.out.println("messageList"+messageList);
//        return messageList;
//    }


}