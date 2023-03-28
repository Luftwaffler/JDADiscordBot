package com.botdev.JDAdiscordbot.commands;

import com.botdev.JDAdiscordbot.testBot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.ArrayList;
import java.util.List;

public abstract class Command {
    public String name;
    public String description;
    public Category category;
    public List<OptionData> optionDataList;
    public List<SubcommandData> subcommandDataList;
    public Permission userPermission;
    public testBot bot;

    public Command(testBot bot){
        this.bot = bot;
        this.optionDataList = new ArrayList<>();
        this.subcommandDataList = new ArrayList<>();
    }

    public abstract void execute(SlashCommandInteractionEvent event);
}
