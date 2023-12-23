package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Service.MessageService;
import Service.AccountService;
import Model.Account;
import Model.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    MessageService messageService;
    AccountService accountService;
    public SocialMediaController(){
        messageService = new MessageService();
        accountService = new AccountService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerUserHandler);
        app.post("/login", this::loginHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages/{message_id}", this::getMessageByIDHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesFromUserHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIDHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    public void getAllMessagesHandler(Context ctx) {
        List<Message> message = messageService.getAllMessages();
        ctx.json(message);
    }

    public void postMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        if(message.getMessage_text().length() == 0 || message.getMessage_text().length() > 255 || accountService.queryAccountID(message.getPosted_by()) == false){
            ctx.status(400);
        }else{
            Message addedMessage = messageService.postMessage(message);
            ctx.json(mapper.writeValueAsString(addedMessage));
        }
    }

    public void updateMessageHandler(Context ctx) throws JsonProcessingException{
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        ObjectMapper mapper = new ObjectMapper();
        Message body = mapper.readValue(ctx.body(), Message.class);
        String updatedMessage = body.getMessage_text();

        Message message = messageService.getMessageByID(id);

        if(message != null && updatedMessage.length() > 0 && updatedMessage.length() <= 255){
            messageService.updateMessage(id, updatedMessage);
            Message newMessage = messageService.getMessageByID(id);
            ctx.json(newMessage);
        }else{
            ctx.status(400);
        }
    }

    public void deleteMessageByIDHandler(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageByID(id);
        if(message != null){
            ctx.json(message);
            messageService.deleteMessageByID(id);
        }else{
            ctx.status(200);
            ctx.body();
        }
    }


    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    /*if(accountService.queryUsername(account.getUsername()) == false){
            ctx.status(400);
        } */
    public void registerUserHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        if(account.getUsername().isBlank() || account.getPassword().length() < 4 || account == null || accountService.queryUsername(account.getUsername()) == true){
            ctx.status(400);
        }else{
            Account addedAccount = accountService.registerUser(account);
            ctx.json(mapper.writeValueAsString(addedAccount));
        }
    }


    public void loginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account loginAttempt = mapper.readValue(ctx.body(), Account.class);
        if(accountService.login(loginAttempt) == null){
            ctx.status(401);
        }else{
            ctx.json(accountService.login(loginAttempt));
        }
    }

    /**
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    public void getMessageByIDHandler(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageByID(id);
        if(message != null){
            ctx.json(message);
        }else{
            ctx.body();
        }
    }

    public void getAllMessagesFromUserHandler(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> userPostHistory = messageService.getAllMessagesFromUser(id);
        ctx.json(userPostHistory);
    }


}