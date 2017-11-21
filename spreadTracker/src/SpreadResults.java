import java.io.*;
import java.time.LocalDate;

public class SpreadResults {
    OddsSharkScraper scraper = new OddsSharkScraper();
    String projectLocation = System.getProperty("user.dir");
    File spreadData = new File("spread data");

    SpreadResults() {
        updateSpread();
        scraper.quit();
    }

    void updateSpread() {
        System.out.println("Updating spread data");
        createSpreadDataFolder();
        createSeasonFolders();

        System.out.println("Update Complete");
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
        for (int curSeason = getSeasonYear(date); curSeason > 2010; curSeason--) {
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


//                Scoreboard scoreboard = (Scoreboard) ois.readObject();
//                if (!scoreboard.isComplete())

                fis.close();
                ois.close();
            } catch (FileNotFoundException e) {
                System.out.println(season.getName() + "\\" + i + " does not exist");
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                FileOutputStream fos = new FileOutputStream(season + "\\" + i);
                ObjectOutputStream oos = new ObjectOutputStream(fos);

                Scoreboard scoreboard = scraper.getScoreBoard(Integer.parseInt(season.getName()), i);
                oos.writeObject(scoreboard);

                fos.close();
                oos.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    int getSeasonYear(LocalDate date) {
        return (date.getMonthValue() < 9) ? date.getYear() - 1 : date.getYear();
    }


}
