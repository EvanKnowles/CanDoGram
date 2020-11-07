package za.co.knonchalant.candogram;

import za.co.knonchalant.candogram.api.IBotAPI;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.BaseMessageHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;

public class EightBallHandler extends BaseMessageHandler {
    private static final String[] RESPONSES = new String[]{"It is certain",
            "It is decidedly so",
            "Without a doubt",
            "Yes, definitely",
            "You may rely on it",
            "As I see it, yes",
            "Most likely",
            "Outlook good",
            "Yes",
            "Signs point to yes",
            "Reply hazy try again",
            "Ask again later",
            "Better not tell you now",
            "Cannot predict now",
            "Concentrate and ask again",
            "Don't count on it",
            "My reply is no",
            "My sources say no",
            "Outlook not so good",
            "Very doubtful"};
    private static final String[] LIFT_OFF = new String[]{
            "%s raises the ball, looks deeply into it, thinks hard on the question and gently oscillates the ball.",
            "%s lifts the ball and shakes it",
            "%s pauses hesitantly, thinks briefly, then gives the ball a vigorous shaking",
            "Gang signs are thrown as %s weaves the ball in an intricate pattern"
    };
    private static final String[] LOOK = new String[]{
            "%s turns the ball over and looks for the secrets it brings",
            "%s raises the ball to the sky and peruses its mysteries. A passing bird takes pity, and yells down the answer.",
            "Up there in the sky! It's a bird! It's a plane! No, no... %s eventually works out it's the bottom of the 8ball",
            "%s glances at the underside of the ball, then grimaces",
            "Good news? Or the worst? %s braces and examines the underside of the ball"
    };

    public EightBallHandler(String botName, IBotAPI bot) {
        super(botName, "8ball", bot, true);
    }

    @Override
    public PendingResponse handle(IUpdate update) {
        String name = update.getUser().getFirstName();

        typing(update);
        sleep(1500);

        sendMessage(update, "\\*" + getLine(LIFT_OFF, name) + "\\*");
        typing(update);
        sleep(1500);

        sendMessage(update, "\\*" + getLine(LOOK, name) + "\\*");
        typing(update);
        sleep(2000);

        sendMessage(update, "*\"" + getLine(RESPONSES, name).toUpperCase() + "\"*");

        return null;
    }

    private String getLine(String[] array, String name) {
        return String.format(array[(int) (Math.random() * array.length)], name);
    }


    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
