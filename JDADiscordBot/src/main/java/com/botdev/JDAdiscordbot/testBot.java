package com.botdev.JDAdiscordbot;
import com.botdev.JDAdiscordbot.commands.commandManager;
import com.botdev.JDAdiscordbot.listeners.eventListener;
import com.botdev.JDAdiscordbot.musicCmds.CmdPlay;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.util.EventListener;

public class testBot {
    private final Dotenv config;
    private final ShardManager shardManager;
    public testBot() throws InvalidTokenException {
        config = Dotenv.configure().load();
        String token = config.get("TOKEN");
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);

        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.watching("Taxi Driver"));
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_PRESENCES);
        //intents
        builder.setMemberCachePolicy(MemberCachePolicy.ALL); // cache
        builder.setChunkingFilter(ChunkingFilter.ALL); // forcing cache on start
        builder.enableCache(CacheFlag.ONLINE_STATUS, CacheFlag.VOICE_STATE); // what you are caching specifically
        shardManager = builder.build(); // creating the instance of the bot
        shardManager.addEventListener(new eventListener(), new commandManager(), new CmdPlay()); //register listeners
    }
    //getter for config
    public Dotenv getConfig() {
        return config;
    }
    //getter for shard manager
    public ShardManager getShardManager(){
        return shardManager;
    }

    public static void main(String[] args) {
        try{
            testBot bot = new testBot();
        } catch(InvalidTokenException e){
            System.out.println("Error! Bot token invalid.");
        }
    }
}
