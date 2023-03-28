package com.botdev.JDAdiscordbot.musicCmds;
import com.botdev.JDAdiscordbot.lavaplayer.GuildMusicManager;
import com.botdev.JDAdiscordbot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.channel.concrete.*;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.managers.AudioManager;
import java.net.URI;
import java.net.URISyntaxException;

public class CmdPlay extends ListenerAdapter{
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;
        String command = event.getName();
        if(command.equals("play")){
            if(!event.getMember().getVoiceState().inAudioChannel() == true){
                event.deferReply().queue();
                event.getHook().sendMessage(event.getUser().getAsMention() + " You must be in a voice channel!").setEphemeral(true).queue();
            }
            OptionMapping searchOption = event.getOption("search");
            if(searchOption == null){
                event.reply("The contents of the search was null").setEphemeral(true).queue();

            }
            String searchString = event.getOption("search").getAsString();
            String link = String.join(" ", searchString);
            if(!isUrl(link)){
                link = "ytsearch: " + link + " official audio";
            }
            if(event.getMember().getVoiceState().inAudioChannel()){
                final AudioManager audioManager = event.getGuild().getAudioManager();
                final VoiceChannel memberChannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();
                audioManager.openAudioConnection(memberChannel);
            }

            PlayerManager.getInstance().loadAndPlay(event,link);
            musicManager.scheduler.recieveEvent(event);
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
