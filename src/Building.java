public class Building {
    private int buildingNum;
    private int executedTime;
    private int totalTime;

    public Building(int buildingNum, int totalTime) {
        this.buildingNum = buildingNum;
        this.totalTime = totalTime;
        this.executedTime = 0;
    }

    public int getBuildingNum() {
        return buildingNum;
    }

    public int getExecutedTime() {
        return executedTime;
    }

    public void setExecutedTime(int executedTime) {
        this.executedTime = executedTime;
    }

    public int getTotalTime() {
        return totalTime;
    }
}
