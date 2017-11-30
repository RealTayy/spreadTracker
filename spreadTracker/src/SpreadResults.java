import java.io.*;
import java.time.LocalDate;

public class SpreadResults {
    OddsSharkScraper scraper = new OddsSharkScraper();
    String projectLocation = System.getProperty("user.dir");
    File spreadData = new File("spread data");

    SpreadResults() {
    }

    void updateFiles() {
        System.out.println("Updating spread data");
        createSpreadDataFolder();
        createSeasonFolders();

        System.out.println("Update Complete");
        scraper.quit();
    }

    void createSpreadDataFolder() {
        //Creates spreadData folder if doesn't exist yet
        if (spreadData.exists()) {
            System.out.println("First time running: false");
        } else {
            System.out.println("First time running: true");
            System.out.println("Creating \"/spread data\"");
            spreadData.mkdir();
        }
    }

    void createSeasonFolders() {
        // Creates folder for and updates seasons
        System.out.println("Checking season folders");
        LocalDate date = LocalDate.now();
        for (int curSeason = getSeasonYear(date); curSeason > 2014; curSeason--) {
            File season = new File(spreadData.getPath() + "/" + String.valueOf(curSeason));

            if (!season.exists()) {
                System.out.println("Creating \"/spread data/" + curSeason + "\"");
                season.mkdir();
            }
            updateSeason(season);
        }
    }

    //Create/Name/Update scoreboards for each week in a season
    void updateSeason(File season) { //26 total items
        System.out.println("Currently checking: Season " + season.getName());
        for (int i = 1; i < 27; i++) {
            try {
                // Start stream
                FileInputStream fis = new FileInputStream(season + "\\" + i);
                ObjectInputStream ois = new ObjectInputStream(fis);

                Scoreboard scoreboard = (Scoreboard) ois.readObject();
                if (!scoreboard.isComplete()) {
                    System.out.println(season.getName() + "\\" + i + " is not yet complete");
                    createNewScoreboardFile(season, i);
                }

                fis.close();
                ois.close();
            } catch (FileNotFoundException e) {
                System.out.println(season.getName() + "\\" + i + " does not yet exist");
                createNewScoreboardFile(season, i);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    void createNewScoreboardFile (File season, int position) {
        try {
            FileOutputStream fos = new FileOutputStream(season + "\\" + position);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            Scoreboard scoreboard = scraper.getScoreBoard(Integer.parseInt(season.getName()), position);
            oos.writeObject(scoreboard);

            fos.close();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int getSeasonYear(LocalDate date) {
        return (date.getMonthValue() < 9) ? date.getYear() - 1 : date.getYear();
    }
}
