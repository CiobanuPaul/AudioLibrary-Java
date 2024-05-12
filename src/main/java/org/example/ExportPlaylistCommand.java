package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jdi.InternalException;

import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ExportPlaylistCommand extends Command {
    public ExportPlaylistCommand() {
        super(Authorization.AUTHENTICATED);
    }

    private void exportAsJson(Playlist playlist, List<Melody> melodyList,  String fileName) throws IOException {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();

        Map<String, List<Melody>> playlistMelody = new HashMap<>();
        playlistMelody.put(playlist.getName(), melodyList);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new FileWriter(file), playlistMelody);
    }

    private void exportAsCsv(Playlist playlist, List<Melody> melodyList,  String fileName) throws IOException {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        try (CSVWriter writer = new CSVWriter(new FileWriter(file))) {
            writer.writeNext(new String[]{"Playlist:", playlist.getName()});
            writer.writeNext(new String[]{"Melody", "Author", "Year"});
            for (Melody melody : melodyList) {
                writer.writeNext(new String[]{ melody.getName(), melody.getAuthor(), String.valueOf(melody.getYear())});
            }
        }
    }

    private void exportAsTxt(Playlist playlist, List<Melody> melodyList, String fileName) throws IOException {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        try(FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write("Playlist: ".getBytes());
            fileOutputStream.write(playlist.getName().getBytes());
            fileOutputStream.write(System.lineSeparator().getBytes());
            fileOutputStream.write(System.lineSeparator().getBytes());
            for (Melody melody : melodyList) {
                fileOutputStream.write(melody.toString().getBytes());
                fileOutputStream.write(System.lineSeparator().getBytes());
            }
        }
    }

    @Override
    public void execute(String[] args, Account account, Connection connection) throws NotEnoughPermissionException, InvalidArgumentsException, IllegalArgumentException, InternalException {
        if(!account.getAuthorization().hasMorePermissionThan(authorizationRequired))
            throw new NotEnoughPermissionException();
        if(args.length != 2)
            throw new IllegalArgumentException("Invalid usage of command export playlist");
        int id_playlist;
        String name;
        try{
            id_playlist = Integer.parseInt(args[0]);
        }
        catch(NumberFormatException e){
            name = args[0];
            try(PreparedStatement ps = connection.prepareStatement("SELECT id_playlist FROM Playlist WHERE name = ?")){
                ps.setString(1, name);
                ResultSet rs = ps.executeQuery();
                if(!rs.next())
                    throw new InvalidArgumentsException("Playlist " + name + " does not exist!");
                id_playlist = rs.getInt("id_playlist");
            }
            catch(SQLException e2){
                throw new InternalException("There was a problem while querying the database!");
            }
        }
        Playlist playlist;
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from Playlist where id_playlist=?")){
            preparedStatement.setInt(1, id_playlist);
            ResultSet rs = preparedStatement.executeQuery();
            if(!rs.next())
                throw new InvalidArgumentsException("Playlist " + id_playlist + " does not exist!");
            name = rs.getString("name");
            playlist = new Playlist(name, id_playlist, account.getId_account());
        }
        catch (SQLException e){
            throw new InternalException("There was a problem while querying the database!");
        }

        List<Melody> melodyList = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement(
                "select * from Melody where id_melody in (select id_melody from Playlist_Melody where id_playlist=?)"
        )){
            preparedStatement.setInt(1, id_playlist);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                int id_melody = rs.getInt("id_melody");
                String melodyName = rs.getString("name");
                String author = rs.getString("author");
                int year = rs.getInt("year");
                melodyList.add(new Melody(melodyName, author, year, id_melody));
            }
        }
        catch(SQLException e){
            throw new InternalException("There was a problem while querying the database!");
        }

        String format = args[1].toLowerCase();
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = localDate.format(pattern);
        String filename;
        if(format.equals("json")) {
            filename = "export_" + account.getName() + "_" + playlist.getName() + "_" + formattedDate + ".json";
            try {
                exportAsJson(playlist, melodyList, filename);
            }
            catch (IOException e){
                throw new InternalException("There was a problem while exporting playlist to file!");
            }
        }
        else if (format.equals("csv")) {
            filename = "export_" + account.getName() + "_" + playlist.getName() +  "_" + formattedDate + ".csv";
            try {
                exportAsCsv(playlist, melodyList, filename);
            }
            catch (IOException e){
                throw new InternalException("There was a problem while exporting playlist to file!");
            }
        }
        else if(format.equals("txt")){
            filename = "export_" + account.getName() + "_" + playlist.getName() +  "_" + formattedDate + ".txt";
            try {
                exportAsTxt(playlist, melodyList, filename);
            }
            catch (IOException e){
                throw new InternalException("There was a problem while exporting playlist to file!");
            }
        }
        else {
            throw new IllegalArgumentException("Invalid usage of command export playlist");
        }
        System.out.println("Playlist exported as " + filename);
    }
}
