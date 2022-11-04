package jpabook.jpashop.api.controller;
import jpabook.jpashop.api.service.ItemService;
import jpabook.jpashop.db.entity.item.Item;
import jpabook.jpashop.socket.Connection;
import lombok.RequiredArgsConstructor;

import java.util.List;

//0033A99520000000000000000{0:0428,1:1}

@TcpController
@RequiredArgsConstructor
public class EchoController {
    private final ItemService itemService;

    private StringBuilder header = new StringBuilder();
    private StringBuilder body = new StringBuilder();

//     public void receiveData(Connection connection, byte[] data) {
//
//         String s = new String(data);
//         connection.send(s.toUpperCase().getBytes());
//         int i = 0;
//         while(i < s.length()){
//
//             if(header.length() < 4){
//                 header.append(s.charAt(i));
//             }else if(header.length() == 4 & body.length() < Integer.parseInt(header.toString())){
//                 body.append(s.charAt(i));
//             }
//             if (header.length() == 4 & body.length() == Integer.parseInt(header.toString())){
//                 System.out.println(body);
//                 header = new StringBuilder();
//                 body = new StringBuilder();
//             }
//             i++;
//         }
//     }
     public void receiveData(Connection connection, byte[] data) {
         String s = new String(data);
         connection.send(s.toUpperCase().getBytes());
         System.out.println(s);
     }

     public void sendData(Connection connection){
         List<Item> items = itemService.findItems();
         connection.send(items);
     }

    public void connect(Connection connection) {
        System.out.println("New connection " + connection.getAddress().getCanonicalHostName());
        sendData(connection);
    }

    public void disconnect(Connection connection) {
        System.out.println("Disconnect " + connection.getAddress().getCanonicalHostName());
    }
}
