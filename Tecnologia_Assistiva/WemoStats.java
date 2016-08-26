
public class WemoStats {

    private boolean on;
    private int onForSeconds;
    private int consumptionMilliWatts;
    private long[]  WemoStatsValues;

    WemoStats(boolean onState, int onForSecondsState, int consumptionMilliWattsState) {
        on=onState;
        onForSeconds=onForSecondsState;
        consumptionMilliWatts=consumptionMilliWattsState;
    }

    public boolean getOn() {return on;}
    public int getOnForSeconds() {return onForSeconds;}
    public int getConsumptionMilliWatts() {return consumptionMilliWatts;}

    public void setWemoStatsValues(long [] vector){

        WemoStatsValues = new long[10];
        for(int i=0;i<vector.length;i++)
            WemoStatsValues[i] = vector[i];
    }

    public long[] getWemoStatsValues() {
        return WemoStatsValues;
    }


    public String toString() {
        StringBuffer st = new StringBuffer();
        st.append("Operational status: "+(on?"On":"Off"));
        st.append("\nTime On (seconds): "+onForSeconds);
        st.append("\nInstantaneous consumption (mW): "+consumptionMilliWatts).append("\n");
        return st.toString();
    }
}
