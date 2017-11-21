import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Game implements Serializable {
    String html;
    Boolean complete = false;
    String status;

    int season;
    int position;
    int week;
    String type;

    String homeName;
    String homeCity;
    String homeNick;
    double homeSpread;
    int homeScore;
    char homeSpreadRe;
    char homeScoreRe;

    String awayName;
    String awayCity;
    String awayNick;
    double awaySpread;
    int awayScore;
    char awaySpreadRe;
    char awayScoreRe;

    int totalScore;
    Double overUnderSpread;
    char overUnderResult;

    public Game(String html, int season, int position, int week, String type) {
        this.html = html;
        this.season = season;
        this.position = position;
        this.week = week;
        this.type = type;

        complete = isComplete();
        if (isComplete()) {
            extractInfo();
        }

    }

    void extractInfo() {
        extractTeams();
        extractResults();
    }

    void extractTeams() {
        // Sets home/awayCity
        Pattern p = Pattern.compile("city\">([^<]+)");
        Matcher m = p.matcher(html);
        m.find();
        homeCity = m.group(1);
        System.out.println(homeCity);
        m.find();
        awayCity = m.group(1);

        // Sets home/awayNick
        p = Pattern.compile("nick-name\">([^<]+)");
        m = p.matcher(html);
        m.find();
        homeNick = retNick(homeCity);
        m.find();
        awayNick = retNick(awayCity);

        // Sets home/awayName
        homeName = homeCity + " " + homeNick;
        awayName = awayCity + " " + awayNick;
    }

    void extractResults() {
        // Sets home/awaySpread
        Pattern p = Pattern.compile("value\">([^<]+)");
        Matcher m = p.matcher(html);
        if (!m.find()) {

        } else {
            if (m.group(1).equals("EV")) homeSpread = 0;
            else homeSpread = Double.parseDouble(m.group(1));
            m.find();
            if (m.group(1).equals("EV")) awaySpread = 0;
            else awaySpread = Double.parseDouble(m.group(1));


            // Sets overUnderSpread/Results
            p = Pattern.compile("total-label( flexed)?\">([^<]+)");
            m = p.matcher(html);
            m.find();
            overUnderSpread = Double.parseDouble(m.group(2));

            // If game is complete extract full results
            if (status.equals("final")) {
                // Sets home/awayScore
                p = Pattern.compile("total-score( winner)?\">([^<]+)");
                m = p.matcher(html);
                m.find();
                homeScore = Integer.parseInt(m.group(2));
                m.find();
                awayScore = Integer.parseInt(m.group(2));

                // Sets home/awayScoreRe
                if (homeScore > awayScore) {
                    homeScoreRe = 'W';
                    awayScoreRe = 'L';
                } else if (homeScore < awayScore) {
                    homeScoreRe = 'L';
                    awayScoreRe = 'W';
                } else {
                    homeScoreRe = 'T';
                    awayScoreRe = 'T';
                }

                // Sets home/awaySpreadRe
                if ((homeScore + homeSpread) > awayScore) {
                    homeSpreadRe = 'C';
                    awaySpreadRe = 'D';
                } else if ((homeScore + homeSpread) < awayScore) {
                    homeSpreadRe = 'D';
                    awaySpreadRe = 'C';
                } else {
                    homeSpreadRe = 'P';
                    awaySpreadRe = 'P';
                }

                // Sets overUnderResults and totalScore
                totalScore = homeScore + awayScore;
                if (overUnderSpread > totalScore) overUnderResult = 'U';
                else if (overUnderSpread < totalScore) overUnderResult = 'O';
                else overUnderResult = 'P';
            }
        }
    }

    boolean isComplete() {
        Pattern p = Pattern.compile("status\">([^<]+)");
        Matcher m = p.matcher(html);
        m.find();
        status = m.group(1);
        return m.group(1).equals("final");

    }

    @Override
    public String toString() {
        if (complete)
            return homeName + "(" + homeSpread + homeSpreadRe + ") "
                    + homeScore + homeScoreRe + "v" + awayScore + awayScoreRe + " "
                    + awayName + "(" + awaySpread + awaySpreadRe + ")"
                    + " (OU:" + overUnderSpread + "|" + totalScore + overUnderResult + ")"
                    + " | Complete: " + complete + " | Status: " + status;
        else return homeName + "(" + homeSpread + ") "
                + "v " + awayName + "(" + awaySpread + ")"
                + " (OU:" + overUnderSpread + ")"
                + " | Complete: " + complete + " | Status: " + status;
    }

    String retNick(String homeCity) {
        switch (homeCity) {
            case "Arizona" : return "Cardinals";
            case "Atlanta" : return "Falcons";
            case "Baltimore" : return "Ravens";
            case "Buffalo" : return "Bills";
            case "Carolina" : return "Panthers";
            case "Chicago" : return "Bears";
            case "Cincinnati" : return "Bengals";
            case "Cleveland" : return "Browns";
            case "Dallas" : return "Cowboys";
            case "Denver" : return "Broncos";
            case "Detroit" : return "Lions";
            case "Green Bay" : return "Packers";
            case "Houston" : return "Texans";
            case "Indianapolis" : return "Colts";
            case "Jacksonville" : return "Jaguars";
            case "Kansas City" : return "Chiefs";
            case "Los Angeles" : return "Rams";
            case "Los Angeles" : return "Chargers";
            case "Miami" : return "Dolphins";
            case "Minnesota" : return "Vikings";
            case "New England" : return "Patriots";
            case "New Orleans" : return "Saints";
            case "New York" : return "Giants";
            case "New York" : return "Jets";
            case "Oakland" : return "Raiders";
            case "Philadelphia" : return "Eagles";
            case "Pittsburgh" : return "Steelers";
            case "San Francisco" : return "49ers";
            case "Seattle" : return "Seahawks";
            case "Tampa Bay" : return "Buccaneers";
            case "Tennessee" : return "Titans";
            case "Washington" : return "Redskins";
        }

    }

    public static void main(String[] args) {
        Game game2 = new Game("<div class=\"matchup-container\"><div class=\"matchup pre\"><div class=\"status\">8:30 pm ET</div><div class=\"teams\"><div class=\"away\"><div class=\"team-header\" style=\"background-color: rgb(0, 0, 0); color: rgb(255, 255, 255);\"><div class=\"city\">Atlanta</div><div class=\"nick-name\">Falcons</div><div class=\"record-container\"><div class=\"ats\"><!-- react-text: 1177 -->(<!-- /react-text --><!-- react-text: 1178 -->3-6<!-- /react-text --><!-- react-text: 1179 --> ATS)<!-- /react-text --></div><div class=\"record\">5-4</div></div></div></div><a class=\"base-versus\" href=\"/nfl/atlanta-seattle-odds-november-20-2017-788768\"></a><div class=\"home\"><div class=\"team-header\" style=\"background-color: rgb(84, 159, 39); color: rgb(255, 255, 255);\"><div class=\"city\">Seattle</div><div class=\"nick-name\">Seahawks</div><div class=\"record-container\"><div class=\"record\">6-3</div><div class=\"ats\"><!-- react-text: 1189 -->(<!-- /react-text --><!-- react-text: 1190 -->3-5-1<!-- /react-text --><!-- react-text: 1191 --> ATS)<!-- /react-text --></div></div></div></div></div><!-- react-empty: 1192 --><div class=\"predicted-score\"><div class=\"predicted-score-header\">PREDICTED SCORE</div><div class=\"predicted-score-container\"><div class=\"away\" style=\"background-color: rgb(0, 0, 0); color: rgb(255, 255, 255);\">16.1</div><div class=\"home\" style=\"background-color: rgb(84, 159, 39); color: rgb(255, 255, 255);\">28.7</div></div></div><!-- react-empty: 1198 --><!-- react-empty: 1199 --><div class=\"gc-graph team-colors\"><div class=\"header\">Spread Consensus</div><div class=\"row\"><div class=\"away\" style=\"border-color: rgb(0, 0, 0);\"><div class=\"axis-wrapper\"><div class=\"label\">ATL</div><div class=\"stat\"><!-- react-text: 1207 -->48<!-- /react-text --><!-- react-text: 1208 -->%<!-- /react-text --></div></div><div class=\"value\">+3</div></div><div class=\"home\" style=\"border-color: rgb(84, 159, 39);\"><div class=\"axis-wrapper\"><div class=\"label\">SEA</div><div class=\"stat\"><!-- react-text: 1214 -->52<!-- /react-text --><!-- react-text: 1215 -->%<!-- /react-text --></div></div><div class=\"value\">-3</div></div></div><div class=\"bar\"><div class=\"away\"><div class=\"inner\" style=\"width: 48%; background-color: rgb(0, 0, 0); border-color: rgb(0, 0, 0);\"></div></div><div class=\"home\"><div class=\"inner\" style=\"width: 52%; background-color: rgb(84, 159, 39); border-color: rgb(84, 159, 39);\"></div></div></div></div><!-- react-empty: 1222 --><!-- react-empty: 1223 --><div class=\"gc-graph total-graph total-consensus-graph\"><div class=\"header\">Total Consensus</div><div class=\"row\"><div class=\"under flexed\"><div class=\"axis-wrapper\"><div class=\"label\">Under</div><div class=\"stat\"><!-- react-text: 1231 -->36<!-- /react-text --><!-- react-text: 1232 -->%<!-- /react-text --></div></div></div><div class=\"total-label flexed\">44.5</div><div class=\"over flexed\"><div class=\"axis-wrapper\"><div class=\"label\">Over</div><div class=\"stat\"><!-- react-text: 1238 -->64<!-- /react-text --><!-- react-text: 1239 -->%<!-- /react-text --></div></div></div></div><div class=\"bar\"><div class=\"under\"><div class=\"inner\" style=\"width: 36%;\"><div class=\"top-border\"></div></div></div><div class=\"over\"><div class=\"inner\" style=\"width: 64%;\"><div class=\"top-border\"></div></div></div></div></div><!-- react-empty: 1247 --><!-- react-empty: 1248 --><ul class=\"base-list\"><li><a class=\"base-arrow\" href=\"/nfl/atlanta-seattle-odds-november-20-2017-788768\">Matchup</a></li></ul></div></div>", 2017, 16, 11, "Regular Season");
        System.out.println(game2);


    }

}