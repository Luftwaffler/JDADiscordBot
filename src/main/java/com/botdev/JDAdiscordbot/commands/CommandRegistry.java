package com.botdev.JDAdiscordbot.commands;

import com.botdev.JDAdiscordbot.TestBot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.NotNull;

import java.security.Permissions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandRegistry extends ListenerAdapter {
    public static final List<Command> commands = new ArrayList<>();
    public static final Map<String, Command> commandsMap = new HashMap<>();

    public CommandRegistry(TestBot bot) {
        mapCommand(
                new CmdSay(bot)
        );
    }

    public void mapCommand(Command... cmds) {
        for (Command cmd : cmds) {
            commandsMap.put(cmd.name, cmd);
            commands.add(cmd);
        }
    }

    /**
     * This method packs and returns all the relevant command data
     * is used during registration to neatly access the command data
     *
     * @return list of command data to be used during command registration
     */
    public static List<CommandData> getCommandData() {
        // List that stores all relevant command data (names,options,subcommands,etc)
        List<CommandData> commandData = new ArrayList<>();

        // Loops through the global commands arrayList and sets up each before adding to commandData list
        for (Command cmd : commands) {
            SlashCommandData slashCommand = Commands.slash(cmd.name, cmd.description).addOptions(cmd.optionDataList);
            if (cmd.userPermission != null) {
                slashCommand.setDefaultPermissions(DefaultMemberPermissions.enabledFor(cmd.userPermission));
            }
            if (!cmd.subcommandDataList.isEmpty()) {
                slashCommand.addSubcommands(cmd.subcommandDataList);
            }
            commandData.add(slashCommand);
        }
        return commandData;
    }

    /**
     * Activates when a slash command is detected
     */
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        // gets corresponding Command object from Key (String Name) from the hashmap
        Command cmd = commandsMap.get(event.getName());
        if (cmd != null) {
            Role botRole = event.getGuild().getBotRole();
            // checks for relevant bot roles in the event that a bot permission is required
            if (cmd.botPermission != null && !botRole.hasPermission(cmd.botPermission)
                    && !botRole.hasPermission(Permission.ADMINISTRATOR)) {
                String replyText = "I need the `" + cmd.botPermission.getName() + "` permission to execute that command.";
                event.reply(replyText).setEphemeral(true).queue();
                return;
            }
        }
        cmd.execute(event);
    }

    /**
     * Registers slash commands as guild commands.
     *
     * @param event that is created when a guild is ready
     */
    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        // registers slash commands as guild commands
        event.getGuild().updateCommands().addCommands(getCommandData()).queue();
    }
}
