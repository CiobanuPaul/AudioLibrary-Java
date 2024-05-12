package org.example;

import com.sun.jdi.InternalException;

import java.sql.Connection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *CommandProcessor class provides functionality to parse input from stdin which should represent commands.
 *
 * It maps the commands to the actual {@code Command} object and then executes it.
 */
public class CommandProcessor {
    private Map<String, Command> commandMap = new HashMap<>();
    static private CommandProcessor instance = null;
    static private final String parserRegex = "\"*\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)\"*|\"$";
    private CommandProcessor() {
        commandMap.put("login", new LoginCommand());
        commandMap.put("register", new RegisterCommand());
        commandMap.put("logout", new LogoutCommand());
        commandMap.put("promote", new PromoteCommand());
        commandMap.put("create song", new CreateSongCommand());
        commandMap.put("create playlist", new CreatePlaylistCommand());
        commandMap.put("list playlists", new ListPlaylistsCommand());
        commandMap.put("add", new AddToPlaylistCommand());
        commandMap.put("search", new SearchCommand());
        commandMap.put("export playlist", new ExportPlaylistCommand());
        commandMap.put("audit", new AuditCommand());
    }

    /**
     * Parses the input and provides the {@code execute} command with the necessary parameters for execution.
     * @param input
     * @param account
     * @param connection
     * @return
     */
    public boolean processAndExecute(String input, Account account, Connection connection) {
        String[] parts = input.split(parserRegex);
        Command command = commandMap.get(parts[0]);
        if (command != null) {
            String[] args = Arrays.copyOfRange(parts, 1, parts.length);
            try {
                command.execute(args, account, connection);
                return true;
            } catch (NotEnoughPermissionException | IllegalArgumentException | InternalException |
                     InvalidArgumentsException e) {
                System.out.println(e.getMessage());
            }
        }
        else if (parts.length > 1){
            command = commandMap.get(parts[0] + ' ' + parts[1]);
            if (command != null) {
                String[] args = Arrays.copyOfRange(parts, 2, parts.length);
                try {
                    command.execute(args, account, connection);
                    return true;
                } catch (NotEnoughPermissionException | IllegalArgumentException | InternalException |
                         InvalidArgumentsException e) {
                    System.out.println(e.getMessage());
                }
            }
            else{
                System.out.println("Unknown command: " + parts[0]);
            }
        }
        else{
            System.out.println("Unknown command: " + parts[0]);
        }
        return false;
    }

    static public CommandProcessor getInstance() {
        if (instance == null) {
            instance = new CommandProcessor();
        }
        return instance;
    }
}
