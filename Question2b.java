public class Question2b {
    
    public static int[] closestPair(int[] x_coords, int[] y_coords) {
        // Initialize the minimum distance to a large value
        int minDistance = Integer.MAX_VALUE;
        int[] result = new int[2]; // To store the indices of the closest pair

        // Iterate over all pairs of points
        for (int i = 0; i < x_coords.length; i++) {
            for (int j = i + 1; j < x_coords.length; j++) {
                // Calculate the Manhattan distance between points (i, j)
                int distance = Math.abs(x_coords[i] - x_coords[j]) + Math.abs(y_coords[i] - y_coords[j]);

                // If a smaller distance is found, update minDistance and result
                if (distance < minDistance) {
                    minDistance = distance;
                    result[0] = i; // Store the index of the first point
                    result[1] = j; // Store the index of the second point
                }
                // If the distance is the same, check lexicographical order
                else if (distance == minDistance) {
                    // If i < i1 or (i == i1 and j < j1), update the result
                    if (i < result[0] || (i == result[0] && j < result[1])) {
                        result[0] = i;
                        result[1] = j;
                    }
                }
            }
        }

        return result; // Return the indices of the closest pair
    }

    public static void main(String[] args) {
        // Test case 1
        int[] x_coords = {1, 2, 3, 2, 4};
        int[] y_coords = {2, 3, 1, 2, 3};
        int[] result = closestPair(x_coords, y_coords);
        System.out.println("Closest pair indices: [" + result[0] + ", " + result[1] + "]");
    }
}
