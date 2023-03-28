package com.botdev.JDAdiscordbot.musicCmds;
import com.botdev.JDAdiscordbot.lavaplayer.GuildMusicManager;
import com.botdev.JDAdiscordbot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.*;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.managers.AudioManager;
import okhttp3.EventListener;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
public class CmdPlay extends ListenerAdapter{
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
        String command = event.getName();
        if(command.equals("play")){
            if(!event.getMember().getVoiceState().inAudioChannel() == true){
                event.deferReply().queue();
                event.getHook().sendMessage(event.getUser().getAsMention() + " You must be in a voice channel!").queue();
                return;
            }
            if(event.getMember().getVoiceState().inAudioChannel()){
                final AudioManager audioManager = event.getGuild().getAudioManager();
                final VoiceChannel memberChannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();
                audioManager.openAudioConnection(memberChannel);
            }
            OptionMapping searchOption = event.getOption("search");
            if(searchOption == null){
                event.reply("The contents of the search was null");
                return;
            }
            String searchString = event.getOption("search").getAsString();
            String link = String.join(" ", searchString);
            if(!isUrl(link)){
                link = "ytsearch: " + link + " official audio";
            }

            PlayerManager.getInstance().loadAndPlay(event.getChannel().asTextChannel(),link);
        }
    }

    public boolean isUrl(String url){
        try{
            new URI(url);
            return true;
        }
        catch(URISyntaxException e){
            return false;
        }
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        event.getGuild().upsertCommand("play", "search and play music")
                .addOption(OptionType.STRING, "search", "Search a name or url").queue();
    }
}
