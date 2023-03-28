package com.botdev.JDAdiscordbot.lavaplayer;

import com.sedmelluq.discord.lavaplayer.format.StandardAudioDataFormats;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;

import java.io.ByteArrayInputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;

public class audioPlayerSendHandler implements AudioSendHandler {

    private final AudioPlayer audioPlayer;
    private final ByteBuffer buffer;
    private final MutableAudioFrame frame;
    public audioPlayerSendHandler(AudioPlayer audioPlayer){
        this.buffer = ByteBuffer.allocate(StandardAudioDataFormats.DISCORD_OPUS.maximumChunkSize());
        this.frame = new MutableAudioFrame();
        this.frame.setBuffer(buffer);
        this.audioPlayer = audioPlayer;
    }
    @Override
    public boolean canProvide() {
        return this.audioPlayer.provide(this.frame);
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        final Buffer buffer = (this.buffer.flip());
        return (ByteBuffer) buffer;
    }

    @Override
    public boolean isOpus() {
        return true;
    }
}
