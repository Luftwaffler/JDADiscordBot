package com.botdev.JDAdiscordbot.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

public class GuildMusicManager {
    public final AudioPlayer audioPlayer;
    public final trackScheduler scheduler;
    private final audioPlayerSendHandler sendHandler;
    public GuildMusicManager(AudioPlayerManager manager){
        this.audioPlayer = manager.createPlayer();
        this.scheduler = new trackScheduler(this.audioPlayer);
        this.audioPlayer.addListener(this.scheduler);
        this.sendHandler = new audioPlayerSendHandler(this.audioPlayer);
    }

    public audioPlayerSendHandler getSendHandler(){
        return this.sendHandler;
    }
}
