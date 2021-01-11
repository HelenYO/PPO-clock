import java.util.HashMap;

public interface EventsStatistic {
    void incEvent(String name);

    double getEventStatisticByName(String name);

    HashMap<String, Double> getAllEventStatistic();

    void printStatistic();
}
