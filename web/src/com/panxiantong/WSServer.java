package com.panxiantong;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;


// This is not work on the intranet when using the phone to connect(safari. the connection failed)
@ServerEndpoint("/WSServer")
public class WSServer {

    private int connectionID;

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    // 若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static CopyOnWriteArraySet<WSServer> webSocketSet = new CopyOnWriteArraySet<>();


    //private static CopyOnWriteArraySet<WSServer> sit1 = new CopyOnWriteArraySet<>();
    //private static CopyOnWriteArraySet<WSServer> sit2 = new CopyOnWriteArraySet<>();


    private static Map<Integer, RoomData> roomMap = new ConcurrentHashMap<>();

    static {
        for (int i = 1; i < 9; i++) {
            roomMap.put(i, new RoomData());
        }
    }

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session) {
        //System.out.println("id--"+session.getId());
        this.session = session;

        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        System.out.println("有新连接加入id=" + session.getId() + "！当前在线人数为" + getOnlineCount());

        //onMessage(session.getId());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {

        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭id=" + session.getId() + "！当前在线人数为" + getOnlineCount());

        webSocketSet.remove(this);  //从set中删除

        for (RoomData room : roomMap.values()) {
            room.delete(this);
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端id=" + session.getId() + "的消息:" + message);
        int sit = Integer.parseInt(message.substring(0, 1));
        RoomData room = roomMap.get((sit + 1) / 2);
        if (message.length() == 1) {
            //first time to tell the server the sit number


            //black
            if (sit % 2 == 1) {
                room.addBlack(this);
            } else {//white
                room.addWhite(this);
            }


            sendMessage(room.getData());
        } else {
            //data=",77,78,79"
            String data = message.substring(1, message.length());

            int len = data.length() / 3;
            //if len is odd, it means this is a black move, that is sit is odd
            //this means (len+sit)%2==0


            if (len % 2 == 1) {// black to move
                if (this.equals(room.getBlackMover())) {
                    room.setData(data);

                    //send data
                    for (WSServer s : room.getBlack()) {
                        s.sendMessage(data);
                    }
                    for (WSServer s : room.getWhite()) {
                        s.sendMessage(data);
                    }

                    return;
                }
            }
            if (len % 2 == 0) {// white to move
                if (this.equals(room.getWhiteMover())) {
                    room.setData(data);

                    //send data
                    for (WSServer s : room.getBlack()) {
                        s.sendMessage(data);
                    }
                    for (WSServer s : room.getWhite()) {
                        s.sendMessage(data);
                    }
                    return;
                }
            }

            System.out.println("...");
        }
        //this.sendMessage("You are not allowed to move");

//            for (WSServer item : webSocketSet) {
//
//                item.sendMessage(message);
//            }
    }


    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("error: id = " + session.getId());
        error.printStackTrace();
    }

    /**
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) {

        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WSServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WSServer.onlineCount--;
    }


}