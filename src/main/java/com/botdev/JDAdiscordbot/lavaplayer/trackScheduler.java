package com.botdev.JDAdiscordbot.lavaplayer;

import com.botdev.JDAdiscordbot.musicCmds.CmdNowPlaying;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class trackScheduler extends AudioEventAdapter {
    public final AudioPlayer audioPlayer;
    public final BlockingQueue<AudioTrack> queue;

    public SlashCommandInteractionEvent e = null;
    public trackScheduler(AudioPlayer audioPlayer){
        this.audioPlayer = audioPlayer;
        this.queue = new LinkedBlockingQueue<>();
    }

    public void queue(AudioTrack track){
        if(!this.audioPlayer.startTrack(track, true)){
            this.queue.offer(track);
        }
    }
    public void recieveEvent(SlashCommandInteractionEvent event){
        e = event;
    }
    public void nextTrack(){
        this.audioPlayer.startTrack(this.queue.poll(),false);
        if(this.audioPlayer.getPlayingTrack() != null) {
            e.getGuildChannel().sendMessage("**" + "Now Playing: " + "**" + this.audioPlayer.getPlayingTrack().getInfo().title
                    + "\n" + audioPlayer.getPlayingTrack().getInfo().uri).queue();
        }
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if(endReason.mayStartNext){
            nextTrack();
        }
    }

}
