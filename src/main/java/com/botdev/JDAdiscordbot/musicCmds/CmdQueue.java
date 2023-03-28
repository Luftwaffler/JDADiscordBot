package com.botdev.JDAdiscordbot.musicCmds;

import com.botdev.JDAdiscordbot.lavaplayer.GuildMusicManager;
import com.botdev.JDAdiscordbot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class CmdQueue extends ListenerAdapter {
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
        String command = event.getName();
        if(command.equals("queue")){
            final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
            final BlockingQueue<AudioTrack> queue = musicManager.scheduler.queue; //gets the queue from Playermanager's music manager
            final int trackCount = Math.min(queue.size(), 20);
            final List<AudioTrack> trackList = new ArrayList<>(queue);

            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(new Color(235, 26, 217));
            embed.setTitle("Queue");
            if(queue.isEmpty()){
                embed.setDescription("No tracks currently queued.");
                event.replyEmbeds(embed.build()).queue();
            }
            else{
                for(int i = 0 ; i < trackCount; i++){
                    final AudioTrack track = trackList.get(i);
                    final AudioTrackInfo trackInfo = track.getInfo();
                    embed.appendDescription(" `")
                            .appendDescription(String.valueOf(i + 1))
                            .appendDescription(".")
                            .appendDescription("` ")
                            .appendDescription(trackInfo.author)
                            .appendDescription(" - ")
                            .appendDescription(trackInfo.title)
                            .appendDescription(" **" + "[")
                            .appendDescription(timeFormat(track.getDuration()))
                            .appendDescription("]" + "** ")
                            .appendDescription("\n");
                }
                event.replyEmbeds(embed.build()).queue();
            }
        }
    }

    public String timeFormat(Long millis){
        final long minutes = millis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = millis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d", minutes, seconds);
    }
    @Override
    public void onGuildReady(GuildReadyEvent event) {
        event.getGuild().upsertCommand("queue", "returns the top 20 tracks in the queue").queue();
    }
}
