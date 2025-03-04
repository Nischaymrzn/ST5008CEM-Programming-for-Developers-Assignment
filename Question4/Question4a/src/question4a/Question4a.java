package question4a;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.*;

public class Question4a {
    public static void main(String[] args) {
        // Parameters for connecting to the database
        String url = "jdbc:mysql://localhost:3306/TrendingHashtags";
        String user = "root";
        String password = "password";

        // SQL command to extract the top three trending hashtags using a recursive CTE
        String sqlQuery = "WITH RECURSIVE\n" +
                          "  FebruaryTweets AS (\n" +
                          "    SELECT *\n" +
                          "    FROM Tweets\n" +
                          "    WHERE YEAR(tweet_date) = 2024 AND MONTH(tweet_date) = 2\n" +
                          "  ),\n" +
                          "  HashtagToTweet AS (\n" +
                          "    SELECT\n" +
                          "      REGEXP_SUBSTR(tweet, '#[^\\\\s]+') AS hashtag,\n" +
                          "      REGEXP_REPLACE(tweet, '#[^\\\\s]+', '', 1, 1) AS tweet\n" +
                          "    FROM FebruaryTweets\n" +
                          "    UNION ALL\n" +
                          "    SELECT\n" +
                          "      REGEXP_SUBSTR(tweet, '#[^\\\\s]+') AS hashtag,\n" +
                          "      REGEXP_REPLACE(tweet, '#[^\\\\s]+', '', 1, 1) AS tweet\n" +
                          "    FROM HashtagToTweet\n" +
                          "    WHERE POSITION('#' IN tweet) > 0\n" +
                          "  )\n" +
                          "SELECT hashtag, COUNT(*) AS count\n" +
                          "FROM HashtagToTweet\n" +
                          "GROUP BY hashtag\n" +
                          "ORDER BY count DESC, hashtag DESC\n" +
                          "LIMIT 3;";

        try (
            // Set up connection to the database
            Connection conn = DriverManager.getConnection(url, user, password);
            // Initialize SQL statement object
            Statement stmt = conn.createStatement();
            // Run the SQL query and retrieve the results
            ResultSet rs = stmt.executeQuery(sqlQuery);
        ) {
            // Display the output table header
            System.out.println("| hashtag   | count |");
            System.out.println("+-----------+-------+");

            // Loop through and print each record from the result set
            while (rs.next()) {
                String hashtag = rs.getString("hashtag");
                int count = rs.getInt("count");
                System.out.println("| " + hashtag + " | " + count + "     |");
            }

            // Output the table footer
            System.out.println("+-----------+-------+");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
