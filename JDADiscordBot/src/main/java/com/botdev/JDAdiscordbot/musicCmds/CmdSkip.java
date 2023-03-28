package com.botdev.JDAdiscordbot.musicCmds;

import com.botdev.JDAdiscordbot.lavaplayer.GuildMusicManager;
import com.botdev.JDAdiscordbot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CmdSkip extends ListenerAdapter {
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();
        if (command.equals("skip")) {
            if (!event.getMember().getVoiceState().inAudioChannel() == true) {
                event.deferReply().queue();
                event.getHook().sendMessage(event.getUser().getAsMention() + " You must be in a voice channel!").queue();
                return;
            }
            final Member member = event.getMember();
            final GuildVoiceState memberVoiceState = event.getMember().getVoiceState();
            final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
            final AudioPlayer audioPlayer = musicManager.audioPlayer;

            VoiceChannel current = (VoiceChannel) event.getGuild().getSelfMember().getVoiceState().getChannel();
            if(!memberVoiceState.getChannel().equals(current)){
                event.reply("Must be in the same voice channel as botforIB");
            }
            if(memberVoiceState.getChannel().equals(current)){
                musicManager.scheduler.nextTrack();
                event.reply("Skipped " + musicManager.audioPlayer.getPlayingTrack().getInfo().title
                        + "`** by **`" + musicManager.audioPlayer.getPlayingTrack().getInfo().author + "`**.").queue();
            }
        }
    }
}
