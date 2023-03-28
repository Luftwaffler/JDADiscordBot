package com.botdev.JDAdiscordbot.listeners;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class eventListener extends ListenerAdapter {
    @Override
    //several test events
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        User user = event.getUser();
        String emoji = event.getReaction().getEmoji().getAsReactionCode();
        String channel = event.getChannel().getAsMention();
        String jumpurl = event.getJumpUrl();
        String message = user.getAsTag() + " reacted with " + emoji + " in the " + channel + " channel";
        event.getGuild().getDefaultChannel().asStandardGuildMessageChannel().sendMessage(message).queue();
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        String avurl = event.getUser().getEffectiveAvatarUrl();
        event.getGuild().getDefaultChannel().asStandardGuildMessageChannel().sendMessage("Welcome to the server!").queue();
        System.out.println(avurl);
    }

//    bad for memory usage when bot deals with large amounts of users
    public void onUserUpdateOnlineStatus(@NotNull UserUpdateOnlineStatusEvent event){
        List<Member> members = event.getGuild().getMembers();
        int onlineMembers = 0;
        for(Member member : members){
            if(member.getOnlineStatus() == OnlineStatus.ONLINE){
                onlineMembers ++;
            }
        }
        User user = event.getUser();
        String message = user.getAsTag() + " has updated their online status to " + event.getNewOnlineStatus().getKey() + "!" + " There are now " + onlineMembers + " online members!";
        event.getGuild().getDefaultChannel().asStandardGuildMessageChannel().sendMessage(message).queue();
    }
}
