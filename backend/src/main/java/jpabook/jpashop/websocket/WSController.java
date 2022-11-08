package jpabook.jpashop.websocket;

import io.jsonwebtoken.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import javax.inject.Singleton;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value="/ws")
@Controller
@Singleton
public class WSController {
    private static String rtnMsg = "";
    private static final java.util.Set<Session> sessions = java.util.Collections.synchronizedSet(new java.util.HashSet<Session>());

    @OnOpen
    public void onOpen(Session session){
        System.out.println("세션ID : " + session.getId());

        try {
            final RemoteEndpoint.Basic basic = session.getBasicRemote();
            basic.sendText("연결됐다.");
        }catch (java.io.IOException e){
            System.out.println(e.getMessage());
        }
        sessions.add(session);
    }

    @OnMessage
    public void onMessage(String message, Session session){
        System.out.println("세션ID : " + session.getId() + ", 내용: " + message);
        rtnMsg = rtnMsg + message + "1";

        sendAll(session, rtnMsg);
        try {
            final RemoteEndpoint.Basic basic = session.getBasicRemote();
            basic.sendText("응답 : " + rtnMsg);
        }catch (java.io.IOException ex){
            ex.printStackTrace();
        }
    }

    @OnError
    public void onError(Throwable t){
        t.printStackTrace();
    }

    @OnClose
    public void onClose(Session session){
        System.out.println("종료 : " + session.getId());
        sessions.remove(session);
    }

    public void sendAll(Session ss, String message){
        try {
            for (Session session: WSController.sessions) {
                if(!ss.getId().equals(session.getId())){
                    session.getBasicRemote().sendText("전체응답 : " + message);
                }
            }
        }catch(java.io.IOException e){
            e.printStackTrace();
        }
    }
}
