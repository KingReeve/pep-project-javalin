package Service;


import Model.Message;
import DAO.MessageDAO;

import java.util.List;

public class MessageService {
    public MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO DAO){
        this.messageDAO = DAO;
    }


    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageByID(int ID){
        return messageDAO.getMessageByID(ID);
    }

    public List<Message> getAllMessagesFromUser(int accountID) {
        return messageDAO.getAllMessagesFromUser(accountID);
    }

    public Message postMessage(Message message){
        return messageDAO.postMessage(message);
    }

    public void deleteMessageByID(int message_id){
        messageDAO.deleteMessageByID(message_id);
    }

    public void updateMessage(int message_id, String updatedMessage){
        messageDAO.updateMessage(message_id, updatedMessage);
    }
}
