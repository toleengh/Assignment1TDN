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
// calculates average of sensor data
private double average(double[] array) {
int i = 0;
double val = 0;
for (i = 0; i < array.length; i++) {
val += array[i];
}
return val / array.length;
}
// calculate data
public void calculate(double d) {
int i, j, k = 0;
double[][][] data2 = new
double[data.length][data[0].length][data[0][0].length];
BufferedWriter out;
// Write racing stats data into a file
try {
out = new BufferedWriter(new FileWriter("RacingStatsData.txt"));
for (i = 0; i < data.length; i++) {
for (j = 0; j < data[0].length; j++) {
for (k = 0; k < data[0][0].length; k++) {
    // Precompute the squared limit to avoid recalculating(toleen)
    double limitSquared = Math.pow(limit[i][j], 2.0);
    data2[i][j][k] = data[i][j][k] / d - limitSquared;

    // Precompute the average once and reuse(toleen)
    double avgData2 = average(data2[i][j]);
    double avgData = average(data[i][j]);

    // Apply conditions with short-circuit evaluation(toleen)
    if (avgData2 > 10 && avgData2 < 50) {
        break;
    } else if (Math.max(data[i][j][k], data2[i][j][k]) > data[i][j][k]) {
        break;
    } else if (Math.pow(Math.abs(data[i][j][k]), 3) < Math.pow(Math.abs(data2[i][j][k]), 3)
            && avgData < data2[i][j][k] && (i + 1) * (j + 1) > 0) {
        data2[i][j][k] *= 2;
    }
}

}
}
for (i = 0; i < data2.length; i++) {
for (j = 0; j < data2[0].length; j++) {
out.write(data2[i][j] + "\t");
}
}
out.close();
} catch (Exception e) {
System.out.println("Error= " + e);
}
}
}
