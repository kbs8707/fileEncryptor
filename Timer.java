public class Timer {
    private long startTime;
    private long endTime;

    public void start() {
        this.startTime = System.currentTimeMillis();
    }

    public void end() {
        this.endTime = System.currentTimeMillis();
    }

    public long timeElapsed() {
        return endTime - startTime;
    }
}
