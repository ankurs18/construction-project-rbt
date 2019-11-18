public class Building {
    private int buildingNum;
    private int executedTime;
    private int totalTime;

    @Override
    public String toString() {
//        return "Building{" +
//                "buildingNum=" + buildingNum +
//                ", executedTime=" + executedTime +
//                ", totalTime=" + totalTime +
//                '}';
        return "(" + buildingNum +
                "," + executedTime +
                "," + totalTime +
                ')';
    }

    public String toStringForOutput() {
        return "(" + buildingNum +
                "," + executedTime +
                "," + totalTime +
                ')';
    }

    public Building(int buildingNum, int totalTime) {
        this.buildingNum = buildingNum;
        this.totalTime = totalTime;
        //this.executedTime = Integer.MAX_VALUE;
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

    // function to compare two buildings, first comparing their respective executed times, and then comparing the building numbers to break ties
    public int compareTo(Building o) {
        int t2 = o.getExecutedTime();
        if( this.executedTime !=t2)
            return this.executedTime - t2;
        else
            return this.buildingNum - o.getBuildingNum();
    }
}
