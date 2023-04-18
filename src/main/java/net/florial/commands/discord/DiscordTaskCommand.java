package net.florial.commands.discord;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.florial.Florial;
import net.florial.database.FlorialDatabase;
import net.florial.models.ChequeData;
import net.florial.models.DiscordUser;

import java.awt.*;
import java.util.List;

@CommandInfo(name = "task")
public class DiscordTaskCommand extends SlashCommand {

    public DiscordTaskCommand() {
        this.name = "task";
        this.options = List.of(
                new OptionData(OptionType.STRING, "task", "What was the task?", true),
                new OptionData(OptionType.ATTACHMENT, "evidence1", "Evidence 1", true)
        );
    }
    @Override
    protected void execute(SlashCommandEvent slashCommandEvent) {
        if (!slashCommandEvent.getMember().getRoles().contains(Florial.getDiscordServer().getRoleById(Florial.getInstance().getConfig().getString("discord.trustedStaffId")))) {
            slashCommandEvent.reply("No permissions").setEphemeral(true).queue();
            return;
        }
        slashCommandEvent.deferReply().queue();
        TextChannel shiftChannel = Florial.getDiscordServer().getTextChannelById(Florial.getInstance().getConfig().getString("discord.shiftChannel"));
        MessageEmbed embed = new EmbedBuilder()
                .setTitle("Task Competed")
                .addField("Staff", slashCommandEvent.getUser().getName(), true)
                .setColor(Color.PINK)
                .setImage(slashCommandEvent.getOption("evidence1").getAsAttachment().getUrl())
                .setFooter("Your payment has been added to your cheque, please run /cashout in game to claim your payment")
                .build();


        shiftChannel.sendMessageEmbeds(embed).queue();
        ChequeData cheque = FlorialDatabase.getChequeData(slashCommandEvent.getUser().getId()).join();
        DiscordUser discordUser = FlorialDatabase.getDiscordUserData(slashCommandEvent.getUser().getId()).join();
        if (cheque == null) cheque = new ChequeData(slashCommandEvent.getUser().getId(), false);
        cheque.setDiscordId(discordUser.getMcUUID());
        cheque.setCash(cheque.getCash() + 10000);
        cheque.setCoins(cheque.getCoins() + 5);
        cheque.setFlories(cheque.getFlories() + 5);
        cheque.save(true);
        slashCommandEvent.reply("Submitted the task uwu").setEphemeral(true).queue();
    }
}
