package com.botdev.JDAdiscordbot.commands.moderation;

import com.botdev.JDAdiscordbot.TestBot;
import com.botdev.JDAdiscordbot.commands.Command;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class CmdKick extends Command {
    public CmdKick(TestBot bot){
        super(bot);
        this.name = "kick";
        this.description = "test new command registration method";
        this.optionDataList.add(new OptionData(OptionType.USER, "User", "user you want to kick").setRequired(true));
        this.optionDataList.add(new OptionData(OptionType.STRING, "Reason", "reason for kick").setRequired(false)
                .setChannelTypes(ChannelType.TEXT, ChannelType.NEWS, ChannelType.GUILD_PUBLIC_THREAD));
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        OptionMapping user = event.getOption("User");
    }
}
