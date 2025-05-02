package com.botdev.JDAdiscordbot.commands;
import com.botdev.JDAdiscordbot.TestBot;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class CmdSay extends Command {
    public CmdSay(TestBot bot){
        super(bot);
        this.name = "say";
        this.description = "test new command registration method";
        this.optionDataList.add(new OptionData(OptionType.STRING, "message", "a message that you want to send").setRequired(true));
        this.optionDataList.add(new OptionData(OptionType.CHANNEL, "channel", "the channel you want to send a message to")
                .setChannelTypes(ChannelType.TEXT, ChannelType.NEWS, ChannelType.GUILD_PUBLIC_THREAD));
    }
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String command = event.getName();
        MessageChannel sendChannel = null;
        OptionMapping messageOption = event.getOption("message");
        OptionMapping channelOption = event.getOption("channel");
        String message = messageOption.getAsString();

        if(channelOption != null){
            sendChannel = channelOption.getAsChannel().asGuildMessageChannel();
        }
        else{
            sendChannel = event.getChannel();
        }
        if(messageOption == null){
            event.reply("No message provided!").setEphemeral(true).queue();
        }
        sendChannel.sendMessage(message).queue();
        event.reply("Your message has been sent.").setEphemeral(true).queue();
    }
}
