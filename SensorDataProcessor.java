public class SensorDataProcessor {
// Senson data and limits.
public double[][][] data;
public double[][] limit;
// Constructor with validation (Naila)
    public SensorDataProcessor(double[][][] sensorData, double[][] thresholds) {
        if (sensorData == null || thresholds == null) {
            throw new IllegalArgumentException("Data or thresholds cannot be null.");
        }
        if (sensorData.length != thresholds.length || sensorData[0].length != thresholds[0].length) {
            throw new IllegalArgumentException("Data and thresholds dimensions must match.");
        }
        this.data = sensorData;
        this.limit = thresholds;
    }
// Calculate the average (no change here, as it’s simple and efficient)(toleen)
    private double average(double[] array) {
        double sum = 0;
        for (double val : array) {
            sum += val;
        }
        return sum / array.length;
    }ٍ
// calculate data
public void calculate(double d) {
        if (d == 0) {
            throw new IllegalArgumentException("Divisor 'd' cannot be zero.");
        }

        // Precompute and cache results (toleen)
        double[][][] data2 = new double[data.length][data[0].length][data[0][0].length];
        Map<String, Double> averageCache = new HashMap<>();

        try (BufferedWriter out = new BufferedWriter(new FileWriter("RacingStatsData.txt"))) {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[i].length; j++) {
                    double limitSquared = limit[i][j] * limit[i][j]; // Strength reduction: Replace pow with multiplication(toleen)

                    // Cache averages for efficiency (toleen)
                    String keyData = i + "-" + j + "-data";
                    String keyData2 = i + "-" + j + "-data2";

                    double avgData = averageCache.computeIfAbsent(keyData, k -> average(data[i][j]));

                    for (int k = 0; k < data[i][j].length; k++) {
                        // Precompute data2 transformation (toleen)
                        data2[i][j][k] = data[i][j][k] / d - limitSquared;

                        // Compute or fetch the average for data2
                        double avgData2 = averageCache.computeIfAbsent(keyData2, k -> average(data2[i][j]));

                        // Apply conditions (short-circuit logic - toleen)
                        if (avgData2 > 10 && avgData2 < 50) {
                            break;
                        } else if (Math.max(data[i][j][k], data2[i][j][k]) > data[i][j][k]) {
                            break;
                        } else if (Math.abs(data[i][j][k]) * Math.abs(data[i][j][k]) * Math.abs(data[i][j][k]) <
                                   Math.abs(data2[i][j][k]) * Math.abs(data2[i][j][k]) * Math.abs(data2[i][j][k])
                                   && avgData < data2[i][j][k] && (i + 1) * (j + 1) > 0) {
                            data2[i][j][k] *= 2;
                        }
                    }

                    // Append results to the buffer(Dana)
                    sb.append(Arrays.toString(data2[i][j])).append("\t");
                }
                sb.append("\n");
            }

            // Write results in bulk
            out.write(sb.toString());
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}