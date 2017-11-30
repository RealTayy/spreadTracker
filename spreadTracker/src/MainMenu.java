import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MainMenu {

    //Java GUI Components
    private JPanel mainPane;

    private JPanel gameExclusions;
    private JCheckBox ARICardinalsCheckBox;
    private JCheckBox DENBroncosCheckBox;
    private JCheckBox DETLionsCheckBox;
    private JCheckBox GBPackersCheckBox;
    private JCheckBox HOUTexansCheckBox;
    private JCheckBox INDColtsCheckBox;
    private JCheckBox JACJaguarsCheckBox;
    private JCheckBox KCChiefsCheckBox;
    private JCheckBox DALCowboysCheckBox;
    private JCheckBox MIADolphinsCheckBox;
    private JCheckBox ATLFalconsCheckBox;
    private JCheckBox BALRavensCheckBox;
    private JCheckBox BUFBillsCheckBox;
    private JCheckBox CARPanthersCheckBox;
    private JCheckBox CHIBearsCheckBox;
    private JCheckBox CINBengalsCheckBox;
    private JCheckBox CLEBrownsCheckBox;
    private JCheckBox PITSteelersCheckBox;
    private JCheckBox SDChargersCheckBox;
    private JCheckBox SF49ersCheckBox;
    private JCheckBox SEASeahawksCheckBox;
    private JCheckBox STLRamsCheckBox;
    private JCheckBox TBBuccaneersCheckBox;
    private JCheckBox TENTitansCheckBox;
    private JCheckBox WASRedskinsCheckBox;
    private JCheckBox PHIEaglesCheckBox;
    private JCheckBox OAKRaidersCheckBox;
    private JCheckBox NYJJetsCheckBox;
    private JCheckBox NYGGiantsCheckBox;
    private JCheckBox NOSaintsCheckBox;
    private JCheckBox NEPatriotsCheckBox;
    private JCheckBox MINVikingsCheckBox;
    private JCheckBox[] teamCheckBoxArr = {ARICardinalsCheckBox,
            DENBroncosCheckBox,
            DETLionsCheckBox,
            GBPackersCheckBox,
            HOUTexansCheckBox,
            INDColtsCheckBox,
            JACJaguarsCheckBox,
            KCChiefsCheckBox,
            DALCowboysCheckBox,
            MIADolphinsCheckBox,
            ATLFalconsCheckBox,
            BALRavensCheckBox,
            BUFBillsCheckBox,
            CARPanthersCheckBox,
            CHIBearsCheckBox,
            CINBengalsCheckBox,
            CLEBrownsCheckBox,
            PITSteelersCheckBox,
            SDChargersCheckBox,
            SF49ersCheckBox,
            SEASeahawksCheckBox,
            STLRamsCheckBox,
            TBBuccaneersCheckBox,
            TENTitansCheckBox,
            WASRedskinsCheckBox,
            PHIEaglesCheckBox,
            OAKRaidersCheckBox,
            NYJJetsCheckBox,
            NYGGiantsCheckBox,
            NOSaintsCheckBox,
            NEPatriotsCheckBox,
            MINVikingsCheckBox};

    private JCheckBox homeOnlyCheckBox;
    private JCheckBox excludeHOFGamesCheckBox;

    private JCheckBox spreadCondition;
    private JTextField spreadCondValue;
    private JComboBox spreadCondCBox;

    private JCheckBox overUnderCondition;
    private JTextField ouCondValue;
    private JComboBox ouCondCBox;

    private JButton fetchResults;
    private JTextArea currentStatus;
    private JCheckBox excludePreSeasonCheckBox;
    private JCheckBox excludePostSeasonCheckBox;
    private JCheckBox excludeRegSeasonCheckBox;
    private JTextArea exGamesArea;
    private JComboBox curTeamCBox;
    private JCheckBox awayOnlyCheckBox;
    private JCheckBox seasonLimitedCheckBox;
    private JComboBox seasonCondCBox;
    private JTextArea winGamesArea;
    private JTextArea loseGamesArea;
    private JTextArea pushGamesArea;
    private JButton clearResultsButton;
    private JButton testButton;
    private JTextField seasonTextField;

    String projectLocation = System.getProperty("user.dir");
    List<Game> exGameList = new ArrayList<>();
    List<Game> winGameList = new ArrayList<>();
    List<Game> loseGameList = new ArrayList<>();
    List<Game> pushGameList = new ArrayList<>();

    private String curTeam = "Cardinals";

    public MainMenu() {
        SpreadResults spreadResults = new SpreadResults();
        spreadResults.updateFiles();
        spreadResults.scraper.quit();

        initializeMasterGameList();
        initializeCBox();

        // This is the test button
        testButton.addActionListener(e -> {
            for (Game game : winGameList) System.out.println(game);
        });

        // This happens when you press the button to get results
        fetchResults.addActionListener(e -> {
            // Emptys out GUI display for results
            clearArea();
            clearList();

            // Add all games of curTeam to Win List
            moveWin();

            // Exclude all games that involve checkboxed teams from Win List
            excludeSelectedGames();

            // Exclude checked criteria
            excludeHomeAway();
            excludeSeasonLimit();
            excludeSpread();
            excludeOU();
            excludeHOF();
            excludePre();
            excludeReg();
            excludePos();


            // Move lose games to Lose List
            moveLose();

            // Move push games to Push List
            movePush();

            // Fills up all the list!
            fillUpList();
        });

        // This should clear all the list and results
        clearResultsButton.addActionListener(e -> {
            clearList();
            clearArea();
        });

        // This happens when you change the current team combo box
        curTeamCBox.addActionListener(e -> {
            curTeam = curTeamCBox.getSelectedItem().toString().replaceAll("[^ ]+ ", "");
            System.out.println(curTeam);
        });
    }

    private Scoreboard getScoreboard(int season, int position) {
        try {
            FileInputStream fis = new FileInputStream("spread data\\" + season + "\\" + position);
            ObjectInputStream ois = new ObjectInputStream(fis);

            Scoreboard scoreboard = (Scoreboard) ois.readObject();

            fis.close();
            ois.close();

            return scoreboard;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Umm... how did you get here?");
        return null;
    }

    private void initializeMasterGameList() {
        for (int season = 2017; season > 2014; season--) {
            for (int position = 1; position < 27; position++) {
                for (Game game : getScoreboard(season, position).gameList) {
                    if (game.isComplete()) exGameList.add(game);
                }
            }
        }
    }

    private void initializeCBox() {
        // Team selector
        curTeamCBox.addItem("ARI Cardinals");
        curTeamCBox.addItem("ATL Falcons");
        curTeamCBox.addItem("BAL Ravens");
        curTeamCBox.addItem("BUF Bills");
        curTeamCBox.addItem("CAR Panthers");
        curTeamCBox.addItem("CHI Bears");
        curTeamCBox.addItem("CIN Bengals");
        curTeamCBox.addItem("CLE Browns");
        curTeamCBox.addItem("DAL Cowboys");
        curTeamCBox.addItem("DEN Broncos");
        curTeamCBox.addItem("DET Lions");
        curTeamCBox.addItem("GB Packers");
        curTeamCBox.addItem("HOU Texans");
        curTeamCBox.addItem("IND Colts");
        curTeamCBox.addItem("JAC Jaguars");
        curTeamCBox.addItem("KC Chiefs");
        curTeamCBox.addItem("MIA Dolphins");
        curTeamCBox.addItem("MIN Vikings");
        curTeamCBox.addItem("NE Patriots");
        curTeamCBox.addItem("NYG Giants");
        curTeamCBox.addItem("NYJ Jets");
        curTeamCBox.addItem("OAK Raiders");
        curTeamCBox.addItem("PHI Eagles");
        curTeamCBox.addItem("PIT Steelers");
        curTeamCBox.addItem("SD Chargers");
        curTeamCBox.addItem("SF 49ers");
        curTeamCBox.addItem("SEA Seahawks");
        curTeamCBox.addItem("STL Rams");
        curTeamCBox.addItem("TB Buccaneers");
        curTeamCBox.addItem("TEN Titans");
        curTeamCBox.addItem("WAS Redskins");

        // Season Limiter CBox
        seasonCondCBox.addItem("Include");
        seasonCondCBox.addItem("Exclude");

        // Spread CBox
        spreadCondCBox.addItem(">");
        spreadCondCBox.addItem("=");
        spreadCondCBox.addItem("<");

        // OU CBox
        ouCondCBox.addItem(">");
        ouCondCBox.addItem("=");
        ouCondCBox.addItem("<");
    }

    private void excludeSelectedGames() {
        System.out.println("Checking to see if any teams are excluded");
        for (JCheckBox teamCheckBox : teamCheckBoxArr) {
            if (teamCheckBox.isSelected()) {
                System.out.println("Removing " + teamCheckBox.getText().replaceAll("[^ ]+ ", "") + " from winList");
                removeNick(teamCheckBox.getText().replaceAll("[^ ]+ ", ""));
            }
        }
    }

    private void moveWin() {
        for (int i = 0; i < exGameList.size(); i++) {
            if (exGameList.get(i).getHomeNick().equals(getCurTeam())) {
                winGameList.add(exGameList.remove(i));
                i--;
            } else if (exGameList.get(i).getAwayNick().equals(getCurTeam())) {
                winGameList.add(exGameList.remove(i));
                i--;
            }
        }
    }

    private void moveLose() {
        for (int i = 0; i < winGameList.size(); i++) {
            if (winGameList.get(i).getHomeNick().equals(getCurTeam()) && winGameList.get(i).getHomeSpreadRe() == 'D') {
                loseGameList.add(winGameList.remove(i));
                i--;
            } else if (winGameList.get(i).getAwayNick().equals(getCurTeam()) && winGameList.get(i).getAwaySpreadRe() == 'D') {
                loseGameList.add(winGameList.remove(i));
                i--;
            }
        }
    }

    private void movePush() {
        for (int i = 0; i < winGameList.size(); i++) {
            if (winGameList.get(i).getHomeNick().equals(getCurTeam()) && winGameList.get(i).getHomeSpreadRe() == 'P') {
                pushGameList.add(winGameList.remove(i));
                i--;
            } else if (winGameList.get(i).getAwayNick().equals(getCurTeam()) && winGameList.get(i).getAwaySpreadRe() == 'P') {
                pushGameList.add(winGameList.remove(i));
                i--;
            }
        }
    }

    private void fillUpList() {
        System.out.println("Populating games list");

        // Fill up excluded games
        exGamesArea.append("Number of games: " + exGameList.size() + "\n");
        for (Game game : exGameList) {
            exGamesArea.append(game.toString() + "\n");
        }

        // Fill up win games
        winGamesArea.append("Number of games: " + winGameList.size() + "\n");
        for (Game game : winGameList) {
            winGamesArea.append(game.toString() + "\n");
        }

        // Fill up lose games
        loseGamesArea.append("Number of games: " + loseGameList.size() + "\n");
        for (Game game : loseGameList) {
            loseGamesArea.append(game.toString() + "\n");
        }

        // Fill up push games
        pushGamesArea.append("Number of games: " + pushGameList.size() + "\n");
        for (Game game : pushGameList) {
            pushGamesArea.append(game.toString() + "\n");
        }
    }

    private void clearArea() {
        exGamesArea.setText("");
        winGamesArea.setText("");
        loseGamesArea.setText("");
        pushGamesArea.setText("");
    }

    private void clearList() {
        winGameList.clear();
        loseGameList.clear();
        pushGameList.clear();
        exGameList.clear();
        initializeMasterGameList();
    }

    private void removeNick(String teamNick) {
        for (int i = 0; i < winGameList.size(); i++)
            if (winGameList.get(i).getHomeNick().equals(teamNick)) {
                exGameList.add(winGameList.remove(i));
            } else if (winGameList.get(i).getAwayNick().equals(teamNick)) {
                exGameList.add(winGameList.remove(i));
            }
    }

    private void excludeHomeAway() {
        if (homeOnlyCheckBox.isSelected()) {
            for (int i = 0; i < winGameList.size(); i++) {
                if (winGameList.get(i).getAwayNick().equals(getCurTeam())) {
                    exGameList.add(winGameList.remove(i));
                    i--;
                }
            }
        }
        if (awayOnlyCheckBox.isSelected()) {
            for (int i = 0; i < winGameList.size(); i++) {
                if (winGameList.get(i).getHomeNick().equals(getCurTeam())) {
                    exGameList.add(winGameList.remove(i));
                    i--;
                }
            }
        }
    }

    private void excludeSeasonLimit() {
        if (seasonLimitedCheckBox.isSelected()) {
            if (seasonCondCBox.getSelectedItem().equals("Include")) {
                for (int i = 0; i < winGameList.size(); i++) {
                    if (!Integer.toString(winGameList.get(i).getSeason()).equals(seasonTextField.getText().trim())) {
                        exGameList.add(winGameList.remove(i));
                        i--;
                    }
                }
            } else {
                for (int i = 0; i < winGameList.size(); i++) {
                    if (Integer.toString(winGameList.get(i).getSeason()).equals(seasonTextField.getText().trim())) {
                        exGameList.add(winGameList.remove(i));
                        i--;
                    }
                }
            }
        }

    }

    private void excludeSpread() {
        if (spreadCondition.isSelected()) {
            switch (spreadCondCBox.getSelectedItem().toString()) {
                case ">":
                    for (int i = 0; i < winGameList.size(); i++) {
                        if (winGameList.get(i).getHomeNick().equals(getCurTeam())
                                && winGameList.get(i).getHomeSpread() < Double.parseDouble(spreadCondValue.getText())) {
                            exGameList.add(winGameList.remove(i));
                            i--;
                        } else if (winGameList.get(i).getAwayNick().equals(getCurTeam())
                                && winGameList.get(i).getAwaySpread() < Double.parseDouble(spreadCondValue.getText())) {
                            exGameList.add(winGameList.remove(i));
                            i--;
                        }
                    }
                    break;
                case "=":
                    for (int i = 0; i < winGameList.size(); i++) {
                        if (winGameList.get(i).getHomeNick().equals(getCurTeam())
                                && winGameList.get(i).getHomeSpread() != Double.parseDouble(spreadCondValue.getText())) {
                            exGameList.add(winGameList.remove(i));
                            i--;
                        } else if (winGameList.get(i).getAwayNick().equals(getCurTeam())
                                && winGameList.get(i).getAwaySpread() != Double.parseDouble(spreadCondValue.getText())) {
                            exGameList.add(winGameList.remove(i));
                            i--;
                        }
                    }
                    break;
                case "<":
                    for (int i = 0; i < winGameList.size(); i++) {
                        if (winGameList.get(i).getHomeNick().equals(getCurTeam())
                                && winGameList.get(i).getHomeSpread() > Double.parseDouble(spreadCondValue.getText())) {
                            exGameList.add(winGameList.remove(i));
                            i--;
                        } else if (winGameList.get(i).getAwayNick().equals(getCurTeam())
                                && winGameList.get(i).getAwaySpread() > Double.parseDouble(spreadCondValue.getText())) {
                            exGameList.add(winGameList.remove(i));
                            i--;
                        }
                    }
                    break;
            }
        }
    }

    private void excludeOU() {
        if (overUnderCondition.isSelected()) {
            switch (ouCondCBox.getSelectedItem().toString()) {
                case ">":
                    for (int i = 0; i < winGameList.size(); i++) {
                        if (winGameList.get(i).getOverUnderSpread() < Double.parseDouble(ouCondValue.getText())) {
                            exGameList.add(winGameList.remove(i));
                            i--;
                        }
                    }
                    break;
                case "=":
                    for (int i = 0; i < winGameList.size(); i++) {
                        if (winGameList.get(i).getOverUnderSpread() != Double.parseDouble(ouCondValue.getText())) {
                            exGameList.add(winGameList.remove(i));
                            i--;
                        }
                    }
                    break;
                case "<":
                    for (int i = 0; i < winGameList.size(); i++) {
                        if (winGameList.get(i).getOverUnderSpread() > Double.parseDouble(ouCondValue.getText())) {
                            exGameList.add(winGameList.remove(i));
                            i--;
                        }
                    }
                    break;
            }
        }
    }

    private void excludeHOF() {
        if (excludeHOFGamesCheckBox.isSelected()) {
            for (int i = 0; i < winGameList.size(); i++) {
                if (winGameList.get(i).getType().substring(0,2).equals("Ha")) {
                    exGameList.add(winGameList.remove(i));
                    i--;
                }
            }
        }
    }

    private void excludePre() {
        if (excludePreSeasonCheckBox.isSelected()) {
            for (int i = 0; i < winGameList.size(); i++) {
                if (winGameList.get(i).getType().substring(0,2).equals("Pr")) {
                    exGameList.add(winGameList.remove(i));
                    i--;
                }
            }
        }
    }

    private void excludeReg() {
        if (excludeRegSeasonCheckBox.isSelected()) {
            for (int i = 0; i < winGameList.size(); i++) {
                if (winGameList.get(i).getType().substring(0,2).equals("Re")) {
                    exGameList.add(winGameList.remove(i));
                    i--;
                }
            }
        }
    }

    private void excludePos() {
        if (excludePostSeasonCheckBox.isSelected()) {
            for (int i = 0; i < winGameList.size(); i++) {
                if (winGameList.get(i).getType().substring(0,2).equals("Po")) {
                    exGameList.add(winGameList.remove(i));
                    i--;
                }
            }
        }
    }

    public String getCurTeam() {
        return curTeam;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Launching app");

        JFrame frame = new JFrame("App");
        frame.setContentPane(new MainMenu().mainPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
