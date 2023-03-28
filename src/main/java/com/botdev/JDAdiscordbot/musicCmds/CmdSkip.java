package com.botdev.JDAdiscordbot.musicCmds;
import com.botdev.JDAdiscordbot.lavaplayer.GuildMusicManager;
import com.botdev.JDAdiscordbot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.awt.*;

public class CmdSkip extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();
        final Member member = event.getMember();
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;
        final AudioChannelUnion current = event.getGuild().getSelfMember().getVoiceState().getChannel();

        if (command.equals("skip")) {
            if(audioPlayer.getPlayingTrack() == null){
                event.reply("There is no track playing currently.").setEphemeral(true).queue();
            }
            if (!event.getMember().getVoiceState().inAudioChannel()) {
                event.reply(event.getUser().getAsMention() + " You must be in a voice channel!").setEphemeral(true).queue();
                System.out.println("TEST1");
            }
            if(!event.getMember().getVoiceState().getChannel().equals(current)){
                event.reply("Must be in the same channel as me!").setEphemeral(true).queue();
            }
            else{
                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(new Color(235, 26, 217));
                final AudioTrackInfo trackInfo = audioPlayer.getPlayingTrack().getInfo();
                embed.setDescription("Skipped: " + "[" + trackInfo.title + "]" + "(" + trackInfo.uri + ")" + " by "
                        + trackInfo.author);
                event.replyEmbeds(embed.build()).queue();
                musicManager.scheduler.nextTrack();
            }

        }
    }
    @Override
    public void onGuildReady(GuildReadyEvent event) {
        event.getGuild().upsertCommand("skip", "skips the current track").queue();
    }
}
