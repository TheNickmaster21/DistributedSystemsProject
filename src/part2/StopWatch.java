package part2;

public class StopWatch {

    private long start;

    public StopWatch() {
        this.start();
    }

    public void start() {
        start = System.nanoTime();
    }

    public long getDifference() {
        return System.nanoTime() - start;
    }

    public long restartAndGetDifferance() {
        long difference = getDifference();
        start = System.nanoTime();
        return difference;
    }
}
