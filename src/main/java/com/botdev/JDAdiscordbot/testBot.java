package com.botdev.JDAdiscordbot;
import com.botdev.JDAdiscordbot.commands.CmdSay;
import com.botdev.JDAdiscordbot.commands.commandManager;
import com.botdev.JDAdiscordbot.listeners.eventListener;
import com.botdev.JDAdiscordbot.musicCmds.CmdNowPlaying;
import com.botdev.JDAdiscordbot.musicCmds.CmdPlay;
import com.botdev.JDAdiscordbot.musicCmds.CmdQueue;
import com.botdev.JDAdiscordbot.musicCmds.CmdSkip;
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

public class testBot {
    private final Dotenv config;
    private final ShardManager shardManager;
    public testBot() throws InvalidTokenException {
        config = Dotenv.configure().load();
        String token = config.get("TOKEN");
//        JDA jda = JDABuilder.createDefault(token);
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);

        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.watching("Taxi Driver"));
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_PRESENCES);
        //intents
        builder.setMemberCachePolicy(MemberCachePolicy.ALL); // cache
        builder.setChunkingFilter(ChunkingFilter.ALL); // forcing cache on start
        builder.enableCache(CacheFlag.ONLINE_STATUS, CacheFlag.VOICE_STATE); // what you are caching specifically
        shardManager = builder.build(); // creating the instance of the bot
        shardManager.addEventListener(new eventListener(), new commandManager(), new CmdPlay());//register listeners
        shardManager.addEventListener(new CmdSkip());
        shardManager.addEventListener(new CmdSay());
        shardManager.addEventListener(new CmdQueue());
        shardManager.addEventListener(new CmdNowPlaying());

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
