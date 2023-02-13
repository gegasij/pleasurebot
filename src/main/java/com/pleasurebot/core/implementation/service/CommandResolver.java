package com.pleasurebot.core.implementation.service;

import com.pleasurebot.core.implementation.model.AnswerMessage;
import com.pleasurebot.core.implementation.model.BotImpl;
import com.pleasurebot.core.implementation.model.Invoice;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CommandResolver {

    private final List<Pair<String, AnswerMessage>> pairs;

    public CommandResolver(BotImpl bot) {
        this.invoicePairs=bot.getPayment().getInvoice().stream().map(it -> Pair.of(it.getInvoiceCommand(), it)).toList();
        this.pairs = bot.getAnswerMessage().stream().map(it -> Pair.of(it.getCommand(), it)).toList();
    }

    private final List<Pair<String, Invoice>> invoicePairs;

    public Object resolveCommand(String command) {
        Optional<Pair<String, AnswerMessage>> first = pairs.stream().filter(it -> it.getFirst().equals(command))
                .findFirst();
        if (first.isPresent()) {
            return first.get().getSecond();
        }
        Optional<Pair<String, Invoice>> first1 = invoicePairs.stream().filter(it -> it.getFirst().equals(command))
                .findFirst();
        if (first1.isPresent()) {
            return first1.get().getSecond();
        } else
            return null;
    }
}
