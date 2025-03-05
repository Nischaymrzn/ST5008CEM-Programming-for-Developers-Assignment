package Question2;

public class Question2a {

    public static int calculateRewards(int[] ratings) {
        int n = ratings.length; // Number of employees
        
        // Step 1: Initialize an array for rewards, each employee gets at least 1 reward
        int[] rewards = new int[n];
        for (int i = 0; i < n; i++) {
            rewards[i] = 1; // Each employee must get at least 1 reward
        }

        // Step 2: First pass (Left to Right)
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                // If the current rating is higher than the previous one, increment the reward
                rewards[i] = rewards[i - 1] + 1;
            }
        }

        // Step 3: Second pass (Right to Left)
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                // If the current rating is higher than the next one, adjust the reward
                // Take the maximum reward needed to satisfy both left and right conditions
                rewards[i] = Math.max(rewards[i], rewards[i + 1] + 1);
            }
        }

        // Step 4: Calculate the total rewards
        int totalRewards = 0;
        for (int reward : rewards) {
            totalRewards += reward; // Sum up the rewards array
        }

        return totalRewards; // Return the total number of rewards
    }

    public static void main(String[] args) {
        // Example 1
        int[] ratings1 = {1, 0, 2};
        System.out.println("Minimum Rewards: " + calculateRewards(ratings1)); // Output: 5

        // Example 2
        int[] ratings2 = {1, 2, 2};
        System.out.println("Minimum Rewards: " + calculateRewards(ratings2)); // Output: 4

        // Additional Example
        int[] ratings3 = {3, 2, 1, 4, 5};
        System.out.println("Minimum Rewards: " + calculateRewards(ratings3)); // Output: 11
    }
}
