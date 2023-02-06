package com.pleasurebot.core.bot.handler;

import com.pleasurebot.core.bot.command.StartHandler;
import com.pleasurebot.core.bot.command.admin.AdminMenuHandler;
import com.pleasurebot.core.bot.command.AnonymousMessageHandler;
import com.pleasurebot.core.bot.command.CommandHandler;
import com.pleasurebot.core.bot.command.client.*;
import com.pleasurebot.core.bot.command.consumer.ConsumerMessageHandler;
import com.pleasurebot.core.bot.command.consumer.RequestMessageHandler;
import com.pleasurebot.core.service.utils.CallbackDataParser;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class HandlerFabric {
    private final Map<String, CommandHandler> commandHandlerMap = new HashMap<>();

    public HandlerFabric(
            AdminMenuHandler adminMenuHandler,

            BundleAddHandler bundleAddHandler,
            BundleConfigEditMenuHandler bundleConfigEditMenuHandler,
            BundleEditMenuHandler bundleEditMenuHandler,
            BundlePageHandler bundlePageHandler,
            ChangeConsumerCredsHandler changeConsumerCredsHandler,
            ClientMenuHandler clientMenuHandler,
            DeleteBundleHandler deleteBundleHandler,
            MessagesMenuHandler messagesMenuHandler,
            NewBundleHandler newBundleHandler,


            AnonymousMessageHandler anonymousMessageHandler,
            RequestMessageHandler requestMessageHandler,

            ConsumerMessageHandler consumerMessageHandler,
            StartHandler menuHandler
    ) {
        commandHandlerMap.put(adminMenuHandler.getCommand(), adminMenuHandler);

        commandHandlerMap.put(bundleAddHandler.getCommand(), bundleAddHandler);
        commandHandlerMap.put(bundleConfigEditMenuHandler.getCommand(), bundleConfigEditMenuHandler);
        commandHandlerMap.put(bundleEditMenuHandler.getCommand(), bundleEditMenuHandler);
        commandHandlerMap.put(bundlePageHandler.getCommand(), bundlePageHandler);
        commandHandlerMap.put(changeConsumerCredsHandler.getCommand(), changeConsumerCredsHandler);
        commandHandlerMap.put(clientMenuHandler.getCommand(), clientMenuHandler);
        commandHandlerMap.put(deleteBundleHandler.getCommand(), deleteBundleHandler);
        commandHandlerMap.put(messagesMenuHandler.getCommand(), messagesMenuHandler);
        commandHandlerMap.put(newBundleHandler.getCommand(), newBundleHandler);

        commandHandlerMap.put(consumerMessageHandler.getCommand(), consumerMessageHandler);
        commandHandlerMap.put(requestMessageHandler.getCommand(), requestMessageHandler);

        commandHandlerMap.put(anonymousMessageHandler.getCommand(), anonymousMessageHandler);
        commandHandlerMap.put(menuHandler.getCommand(), menuHandler);
    }

    public CommandHandler getHandler(String command) {
        String command1 = CallbackDataParser.getCommand(command);
        return commandHandlerMap.get(command1);
    }
}
