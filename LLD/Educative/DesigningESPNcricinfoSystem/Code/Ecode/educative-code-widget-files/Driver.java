import java.util.*;

public class Driver {
    public static void main(String[] args) {
        System.out.println("🏏 === Welcome to Cricinfo System Simulation ===\n");

        // ==================================================
        // 🛠️ Initialization: Tournament and Admin Setup
        // ==================================================
        System.out.println("📦 === Initialization ===\n");

        Tournament tournament = new Tournament();
        tournament.setStartDate(new Date());
        tournament.setTeams(new ArrayList<>());
        tournament.setMatches(new ArrayList<>());

        PointsTable pointsTable = new PointsTable();
        pointsTable.setTeamPoints(new HashMap<>());
        pointsTable.setMatchResults(new HashMap<>());
        pointsTable.setTournament(tournament);
        pointsTable.setLastUpdated(new Date());
        tournament.setPoints(pointsTable);

        Admin admin = new Admin();

        System.out.println("✅ Tournament created and points table initialized.");
        System.out.println("   ➤ Tournament Start Date: " + tournament.getStartDate());
        System.out.println("   ➤ Points table last updated: " + pointsTable.getLastUpdated() + "\n");

        // ==================================================
        // 🧩 Scenario 1: Match Creation and Assignment
        // ==================================================
        System.out.println("🎯 === Scenario 1: Create Match and Assign Roles ===\n");

        Match match = admin.createMatch(MatchType.T20);
        match.setStartTime(new Date());
        match.setTotalOvers(20);
        System.out.println("✅ Match Created: T20 Format");
        System.out.println("   ➤ Match Start Time: " + match.getStartTime());
        System.out.println("   ➤ Total Overs: " + match.getTotalOvers());

        Stadium stadium = new Stadium();
        stadium.setName("Wankhede Stadium");
        Address address = new Address();
        address.setCity("Mumbai");
        address.setCountry("India");
        stadium.setLocation(address);
        admin.assignStadiumToMatch(stadium, match);
        System.out.println("✅ Stadium Assigned: " + stadium.getName() + ", " + address.getCity() + ", " + address.getCountry());

        Umpire umpire = new Umpire();
        umpire.setName("Aleem Dar");
        umpire.setAge(50);
        umpire.setCountry(92);
        admin.assignUmpireToMatch(umpire, match);
        System.out.println("✅ Umpire Assigned: " + umpire.getName());

        Commentator commentator = new Commentator();
        commentator.setName("Harsha Bhogle");
        admin.assignCommentatorToMatch(commentator, match);
        System.out.println("✅ Commentator Assigned: " + commentator.getName());

        tournament.addMatch(match);
        System.out.println("📥 Match added to tournament.\n");

        // ==================================================
        // 👥 Scenario 2: Add Players and Form Teams
        // ==================================================
        System.out.println("🧢 === Scenario 2: Add Players and Teams ===\n");

        Player player1 = new Player();
        player1.setName("Virat Kohli");
        player1.setAge(35);
        player1.setCountry(91);

        Player player2 = new Player();
        player2.setName("Mitchell Starc");
        player2.setAge(34);
        player2.setCountry(61);

        Playing11 team1 = new Playing11();
        team1.addPlayer(player1);

        Playing11 team2 = new Playing11();
        team2.addPlayer(player2);

        match.addTeam(team1);
        match.addTeam(team2);
        System.out.println("✅ Players added to match:");
        System.out.println("   ➤ Team 1: " + player1.getName());
        System.out.println("   ➤ Team 2: " + player2.getName());

        TournamentSquad squad1 = new TournamentSquad();
        squad1.setPlayers(List.of(player1));
        tournament.addTeam(squad1);

        TournamentSquad squad2 = new TournamentSquad();
        squad2.setPlayers(List.of(player2));
        tournament.addTeam(squad2);
        System.out.println("📥 Teams registered to the tournament.\n");

        // ==================================================
        // 🏏 Scenario 3: Simulate Gameplay and Commentary
        // ==================================================
        System.out.println("🏃 === Scenario 3: Ball Played and Commentary ===\n");

        Ball ball = new Ball();
        ball.setBalledBy(player2);
        ball.setPlayedBy(player1);
        ball.setType(BallType.NORMAL);

        Run run = new Run();
        run.setTotalRuns(4);
        run.setType(RunType.FOUR);
        run.setScoredBy(player1);
        ball.setRuns(List.of(run));

        Commentary commentary = new Commentary();
        commentary.setText("Kohli punches it through the covers for a boundary!");
        commentary.setCreatedAt(new Date());
        commentary.setCommentator(commentator);
        ball.addCommentary(commentary);

        System.out.println("🎬 Ball Delivered:");
        System.out.println("   ➤ Bowler: " + player2.getName());
        System.out.println("   ➤ Batsman: " + player1.getName());
        System.out.println("   ➤ Runs Scored: " + run.getTotalRuns() + " (" + run.getType() + ")");
        System.out.println("   ➤ Commentary: \"" + commentary.getText() + "\" - " + commentator.getName() + "\n");

        // ==================================================
        // 📊 Scenario 4: Update and Display Points Table
        // ==================================================
        System.out.println("📊 === Scenario 4: Points Table Update ===\n");

        pointsTable.getTeamPoints().put("Team 1", 4.0f);
        pointsTable.getTeamPoints().put("Team 2", 0.0f);
        pointsTable.setLastUpdated(new Date());

        System.out.println("🏆 Updated Points Table:");
        for (Map.Entry<String, Float> entry : pointsTable.getTeamPoints().entrySet()) {
            System.out.println("   ➤ " + entry.getKey() + ": " + entry.getValue() + " points");
        }

        System.out.println("\n✅ Points Table Last Updated On: " + pointsTable.getLastUpdated());
        System.out.println("\n🏁 === Cricinfo Tournament Simulation Complete ===");
    }
}
