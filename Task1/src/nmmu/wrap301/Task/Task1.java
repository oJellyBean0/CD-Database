package nmmu.wrap301.Task;

import nmmu.wrap301.Menu.Menu;
import nmmu.wrap301.Menu.Pair;

import java.sql.*;
import java.util.Scanner;

/**
 * Created by s2133 on 2017/04/18.
 */
public class Task1 {
    // fields needed to access database
    // actual connection to db
    private static Connection con = null;
    // object used to issue SQL commands
    private static Statement stmt = null;
    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        Menu mainMenu = new Menu("Menu", false);
        mainMenu.addChoice(new Pair<>("Create new CD", ()->addCD()));
        mainMenu.addChoice(new Pair<>("Edit CD details", ()->editCDDetails()));
        mainMenu.addChoice(new Pair<>("Edit track details", Task1::editTrackDetails));
        Menu queries = new Menu("Queries", true);
        mainMenu.addChoice(new Pair<>("Queries", ()->runSubMenu(queries)));
        queries.addChoice(new Pair<>("All tracks for given artist", Task1::queryGivenArtistInfo));
        queries.addChoice(new Pair<>("All tracks with keyword in track name", Task1::queryNamesGivenKeyword));
        queries.addChoice(new Pair<>("All CDs with keyword in CD title", Task1::queryTitleGivenKeyword));

        connectToDB();
        mainMenu.run();
        disconnectDB();
    }
    /**
     * The submenu passed through is run from here.
     * @param newMenu the submenu needed to be run
     */
    public static void runSubMenu(Menu newMenu){
        newMenu.run();
    }

    public static void connectToDB() {

        System.out.println("Connecting to database...");
        String connectionString = "jdbc:sqlserver://OPENBOX.nmmu.ac.za\\WRR;databaseName=WRAP301Music";

        try {
            con = DriverManager.getConnection(connectionString, "WRAP301User", "1");
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch (SQLException e) {
            System.out.println("Connection to database failed");
            e.printStackTrace();
        }
    }

    public static void disconnectDB() {
        System.out.println("Disconnecting from database...");

        try {
            con.close();
        } catch (Exception ex) {
            System.out.println("   Unable to disconnect from database");
        }
    }

    public static void addCD(){
        System.out.println("Enter title name: ");
        String cdTitle = scanner.nextLine();
        System.out.println("Enter year of CD :");
        int cdYear = scanner.nextInt();
        scanner.nextLine();
        try {
            String sql = "INSERT INTO CD VALUES ('" + cdTitle + "', '" + cdYear + "')";
            stmt.execute(sql);
        } catch (SQLException e){
            System.out.println("Error adding a new CD");
            e.printStackTrace();
        }

        System.out.println("Would you like to add tracks to the CD?");
        System.out.println("Type 'yes' or 'no");
        String addTracks = scanner.nextLine();

        if (addTracks.equals("Yes")||addTracks.equals("yes")){
            System.out.println("How many tracks would you like to add?");
            int numOfTracks = scanner.nextInt();
            scanner.nextLine();

            ResultSet resultSet = null;
            int cdID = 0;
            try {
                resultSet = stmt.executeQuery("SELECT CDID FROM CD WHERE Title = '" + cdTitle + "' AND Year = '" + cdYear + "'");

                if (resultSet != null && resultSet.last())
                    cdID = resultSet.getInt("CDID");
            }
            catch (SQLException e){
                System.out.println("Query for retrieving CD ID failed");
                e.printStackTrace();
            }

            for (int ii = 0; ii<numOfTracks; ii++){
                addTrack(cdID);
            }
        }



    }

    public static void addTrack(int cdID){
        int trackNum;
        String trackName, trackArtist;
        System.out.println("Enter track number: ");
        trackNum = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter track name: ");
        trackName = scanner.nextLine();
        System.out.println("Enter track artist: ");
        trackArtist = scanner.nextLine();

        try{
            String sql = "INSERT INTO Track VALUES ('"+cdID+"','" + trackNum + "', '" + trackName+ "', '" + trackArtist+ "')";
            stmt.execute(sql);
        } catch (SQLException e){
            System.out.println("Error adding track");
            e.printStackTrace();
        }
    }

    public static void editCDDetails(){
        int cdID, cdYear;
        String cdTitle;

        System.out.println("Enter CD ID of CD to be updated: ");
        cdID = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter CD title: ");
        cdTitle = scanner.nextLine();
        try{
            String sql = "UPDATE CD SET Title='" + cdTitle + "' WHERE CDID='" + cdID + "'";
            stmt.execute(sql);
            System.out.println("Title has been updated to "+cdTitle);
        }
        catch (SQLException e){
            System.out.println("Error updating CD title");
            e.printStackTrace();
        }

        System.out.println("Enter CD year: ");
        cdYear = scanner.nextInt();
        try{
            String sql = "UPDATE CD SET Year='" + cdYear + "' WHERE CDID='" + cdID + "'";
            stmt.execute(sql);
            System.out.println("Year of CD has been updated to " + cdYear);
        }
        catch (SQLException e){
            System.out.println("Error updating year of CD");
            e.printStackTrace();
        }
    }

    public static void editTrackDetails(){
        int trackID, trackNum;
        String trackName, trackArtist;

        System.out.println("Enter track ID number: ");
        trackID = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter track number: ");
        trackNum = scanner.nextInt();
        scanner.nextLine();
        try{
            String sql = "UPDATE Track SET TrackNumber='" + trackNum + "' WHERE TID='" + trackID + "'";
            stmt.execute(sql);
            System.out.println("Track number has been updated to "+trackNum);
        }
        catch (SQLException e){
            System.out.println("Error updating track number");
            e.printStackTrace();
        }

        System.out.println("Enter track name: ");
        trackName = scanner.nextLine();
        try{
            String sql = "UPDATE Track SET Name='" + trackName + "' WHERE TID='" + trackID + "'";
            stmt.execute(sql);
            System.out.println("Track name has been updated to "+trackName);
        }
        catch (SQLException e){
            System.out.println("Error updating track name");
            e.printStackTrace();
        }

        System.out.println("Enter track artist: ");
        trackArtist = scanner.nextLine();
        try{
            String sql = "UPDATE Track SET Artist='" + trackArtist+ "' WHERE TID='" + trackID + "'";
            stmt.execute(sql);
            System.out.println("Track artist has been updated to "+trackNum);
        }
        catch (SQLException e){
            System.out.println("Error updating track artist");
            e.printStackTrace();
        }

    }

    public static void queryGivenArtistInfo(){
        System.out.println("Enter artist name:");
        String artistName = scanner.nextLine();

        ResultSet resultSet = null;
        try {
            resultSet = stmt.executeQuery("SELECT T.TrackNumber, T.Name, C.Title, C.Year FROM Track AS T, CD AS C WHERE C.CDID=T.CDID AND T.Artist ='" + artistName + "'");
            ResultSetMetaData meta = resultSet.getMetaData();

            int columns = meta.getColumnCount();

            System.out.println();
            System.out.println("\tResult (Track Number, Track Name, Album Name, Album Year)");
            // while there are tuples in the result set, display them... using indices
            int row = 0;
            while (resultSet.next()) {
                // get values from current tuple
                row++;
                String line = "\t";
                for (int i = 1; i <= columns; i++) {
                    line = line.concat(resultSet.getString(i) + "\t ");
                }

                // use info
                System.out.println(line);
            }
        }
        catch (Exception e){
            System.out.println("Error retrieving query information");
            e.printStackTrace();
        }

    }

    public static void queryNamesGivenKeyword(){
        System.out.println("Enter track name keyword:");
        String keyword = scanner.nextLine();

        ResultSet resultSet = null;
        try {
            resultSet = stmt.executeQuery("SELECT T.TrackNumber, T.Name, T.Artist, C.Title, C.Year FROM Track AS T, CD AS C WHERE C.CDID=T.CDID AND T.Name LIKE '%" + keyword + "%'");
            ResultSetMetaData meta = resultSet.getMetaData();

            int columns = meta.getColumnCount();


            System.out.println();
            System.out.println("\tResult (Track Number, Track Name, Album Name, Album Year)");
            // while there are tuples in the result set, display them... using indices
            int row = 0;
            while (resultSet.next()) {
                // get values from current tuple
                row++;
                String line = "\t";
                for (int i = 1; i <= columns; i++) {
                    line = line.concat(resultSet.getString(i) + "\t ");
                }

                // use info
                System.out.println(line);
            }
        }
        catch (SQLException e){
            System.out.println("Error retrieving query information");
            e.printStackTrace();
        }
    }

    public static void queryTitleGivenKeyword(){
        System.out.println("Enter CD title keyword:");
        String keyword = scanner.nextLine();

        ResultSet resultSet = null;
        try {
            resultSet = stmt.executeQuery("SELECT Title, Year FROM CD WHERE Title LIKE '%" + keyword + "%'");
            ResultSetMetaData meta = resultSet.getMetaData();

            int columns = meta.getColumnCount();

            System.out.println();
            System.out.println("\tResult (CD Title, CD Year)");
            // while there are tuples in the result set, display them... using indices
            int row = 0;
            while (resultSet.next()) {
                // get values from current tuple
                row++;
                String line = "\t";
                for (int i = 1; i <= columns; i++) {
                    line = line.concat(resultSet.getString(i) + "\t ");
                }

                // use info
                System.out.println(line);
            }
        }
        catch (SQLException e){
            System.out.println("Error retrieving query information");
            e.printStackTrace();
        }
    }
}
