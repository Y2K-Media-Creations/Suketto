package sh.miles.suketto.bukkit.command;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents CommandSettings that can be applied to a command for enhanced feature sets
 */
public class CommandSettings {

    private static BaseComponent[] DEFAULT_PERMISSION_MESSAGE = TextComponent.fromLegacyText("You do not have permission for this", ChatColor.RED);
    private static BaseComponent[] DEFAULT_INVALID_SENDER_MESSAGE = TextComponent.fromLegacyText("You are not a valid sender for this command", ChatColor.RED);
    public static List<Class<? extends CommandSender>> DEFAULT_VALID_SENDERS = List.of(CommandSender.class);
    public static Settings DEFAULT_COMMAND_SETTINGS = new Settings(DEFAULT_PERMISSION_MESSAGE, DEFAULT_INVALID_SENDER_MESSAGE, DEFAULT_VALID_SENDERS);

    private BaseComponent[] permissionMessage;
    private BaseComponent[] invalidSenderMessage;
    private List<Class<? extends CommandSender>> validSenders = new ArrayList<>();

    /**
     * Sets the permission message
     *
     * @param permissionMessage the message to set
     */
    public void permissionMessage(BaseComponent[] permissionMessage) {
        this.permissionMessage = permissionMessage.clone();
    }

    /**
     * Sets the invalid sender message
     *
     * @param invalidSenderMessage the invalid sender message to set
     */
    public void invalidSenderMessage(BaseComponent[] invalidSenderMessage) {
        this.invalidSenderMessage = invalidSenderMessage.clone();
    }

    /**
     * Adds a valid sender to valid Sender list. Note if adding {@link CommandSender} all senders will be allowed
     *
     * @param senderType the sender to add
     */
    public void addValidSender(Class<? extends CommandSender> senderType) {
        validSenders.add(senderType);
    }

    /**
     * Builds the CommandSettings instance into a immutable {@link Settings} record which can not be modified
     *
     * @return a Settings instance
     */
    public Settings build() {
        return new Settings(permissionMessage, invalidSenderMessage, validSenders);
    }

    public static BaseComponent[] defaultPermissionMessage() {
        return DEFAULT_PERMISSION_MESSAGE.clone();
    }

    public static BaseComponent[] defaultInvalidSenderMessage() {
        return DEFAULT_INVALID_SENDER_MESSAGE.clone();
    }

    /**
     * A Build Settings Object comprised from the Settings Builder
     *
     * @param permissionMessage    the permission message
     * @param invalidSenderMessage the invalid sender message
     * @param validSenders         valid senders list
     */
    record Settings(BaseComponent[] permissionMessage, BaseComponent[] invalidSenderMessage,
                    List<Class<? extends CommandSender>> validSenders) {

        public <T extends CommandSender> boolean validSender(@NotNull final T sender) {
            for (Class<? extends CommandSender> validSender : validSenders) {
                if (sender.getClass().isAssignableFrom(validSender)) {
                    return true;
                }
            }

            return false;
        }

        public void sendPermissionMessage(CommandSender sender) {
            if (permissionMessage != null) {
                sender.spigot().sendMessage(permissionMessage);
            }
        }

        public void sendInvalidSenderMessage(CommandSender sender) {
            if (invalidSenderMessage != null) {
                sender.spigot().sendMessage(invalidSenderMessage);
            }
        }
    }

}
