package com.botdev.JDAdiscordbot.musicCmds;
import com.botdev.JDAdiscordbot.lavaplayer.GuildMusicManager;
import com.botdev.JDAdiscordbot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class CmdNowPlaying extends ListenerAdapter {
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
        String command = event.getName();
        if(command.equals("nowplaying")){
            final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
            final AudioPlayer audioPlayer = musicManager.audioPlayer;
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(new Color(235, 26, 217));

            if(audioPlayer.getPlayingTrack() == null){
                embed.setDescription("There is no track currently playing.");
                event.replyEmbeds(embed.build()).queue();
            }
            else{
                final AudioTrack track = audioPlayer.getPlayingTrack();
                final AudioTrackInfo trackInfo = track.getInfo();
                embed.setDescription("Now playing " + "[" + trackInfo.title + "]" + "(" + trackInfo.uri + ")" + " by "
                        + trackInfo.author + " " + "**" + "[" + timeFormat(track.getDuration()) + "]" + "**");
            }
            event.replyEmbeds(embed.build()).queue();
        }
    }

    public String timeFormat(Long millis){
        final long minutes = millis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = millis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d", minutes, seconds);
    }
    @Override
    public void onGuildReady(GuildReadyEvent event) {
        event.getGuild().upsertCommand("nowplaying", "returns the current track").queue();
    }
}
