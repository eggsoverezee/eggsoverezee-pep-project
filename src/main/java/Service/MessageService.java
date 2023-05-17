package Service;

import Model.Message;
import DAO.MessageDAO;
import java.util.List;

public class MessageService {
    public MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public Message addMessage(Message message){
        if(message == null 
            || message.getMessage_text().isBlank() 
            || message.getMessage_text().length() > 254)
            return null;
        return messageDAO.createMessage(message);
        
    }

    public Message getMessageByID(int id){
        return messageDAO.getMessageByID(id);
    }

    public Message deleteMessageByID(int id){
        return messageDAO.deleteMessage(id);
    }

    public Message updateMessage(int message_id, Message message){
        if(messageDAO.getMessageByID(message_id) == null || message.getMessage_text().isBlank()
            || message.getMessage_text().length() >= 255)
            return null;
        else{
            messageDAO.updateMessage(message_id, message);
            return messageDAO.getMessageByID(message_id);
        }

    }
}
