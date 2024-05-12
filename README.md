AudioLibrary is a command line Java application which manages users, each with his own playlists of melodies. 
A melody is represented by a title, an artist, and the year of creation. 
Users can have several levels of authorization(non-authenticated, authenticated and administrator).
The authorization of each user permits him to use several commands.
The application uses a MariaDB database.

When started, the following message with instructions appears:
Welcome to AudioLibrary!

First you need to login/register using the command:

login <username> <password>

or

register <username> <password>

After authenticating, you can use the following commands:

logout

list playlists

add byName "<playlistName>" <melodyId> <melodyId2> <melodyId3> ...

add byId <playlistId> <melodyId> <melodyId2> <melodyId3> ...

search name "<textToSearch>"

search author "<textToSearch>"

export playlist <playlistName> <format>   (format can be csv or json or txt)

export playlist <playlistId> <format>   (format can be csv or json or txt)

If you have administrator rights than you can use the following commands:

promote <username>

create song "<name>" "<author>" <year>

audit <username>

Enjoy the app!
