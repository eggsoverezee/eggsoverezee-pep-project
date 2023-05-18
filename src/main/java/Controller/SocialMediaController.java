package Controller;
 
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;



/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;
    
    public SocialMediaController(){
        accountService = new AccountService();
        messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIDHandler);
        app.post("/register", this::postAccountHandler);
        app.post("/login", this::postLoginHandler);
        app.post("messages", this::postMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIDHandler);
        app.patch("/messages/{message_id}", this::patchUpdateMessageHandlerByID);
        app.get("/accounts/{account_id}/messages", this::getMessagesByUserIDHandler);
        
        return app;
    }

 
    private void getMessagesByUserIDHandler(Context context){
        int id = Integer.parseInt(context.pathParam("account_id"));
        List<Message> allMessages = messageService.getAllMessages();
        List<Message> userMessages = new ArrayList<>();
        for(Message m: allMessages){
            if(m.getPosted_by() == id)
                userMessages.add(m);
        }
        context.json(userMessages);
    
    }
    private void patchUpdateMessageHandlerByID(Context context) throws JsonProcessingException{
        int id = Integer.parseInt(context.pathParam("message_id"));
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message updatedMessage = messageService.updateMessage(id, message);
        if(updatedMessage == null)
            context.status(400);
        else
            context.json(updatedMessage);

    }

    private void deleteMessageByIDHandler(Context context) throws JsonProcessingException{
        int id = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.deleteMessageByID(id);
        if(message == null)
            context.status(200);
        else
            context.json(message);
    }
    private void postMessageHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message newMessage = messageService.addMessage(message);
        if(newMessage == null)
            context.status(400);
        else
            context.json(mapper.writeValueAsString(newMessage));

    }

    private void postLoginHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account verifiedAccount = accountService.verifyLogin(account);
        if(verifiedAccount == null)
            context.status(401);
        else
            context.json(mapper.writeValueAsString(verifiedAccount));
    }

    private void postAccountHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account createdAccount = accountService.createAccount(account);
        if(createdAccount == null)
            context.status(400);
        else
            context.json(mapper.writeValueAsString(createdAccount));
    }

    private void getMessageByIDHandler(Context context) throws JsonProcessingException{
        int id = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageByID(id);
        if(message == null)
            context.status(200);
        else
            context.json(messageService.getMessageByID(id));
    }
    private void getAllMessagesHandler(Context context){
        List<Message> messages = messageService.getAllMessages();
        context.json(messages);
    }

}