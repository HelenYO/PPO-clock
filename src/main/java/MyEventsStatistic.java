import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyEventsStatistic implements EventsStatistic {
    private HashMap<String, List<Instant>> events = new HashMap<>();
    private final Clock clock;

    public MyEventsStatistic(Clock clock) {
        this.clock = clock;
    }

    public void incEvent(String name) {
        if (!events.containsKey(name)) {
            events.put(name, new ArrayList<>());
        }
        events.get(name).add(clock.instant());
    }

    private void updateEvents() {
        Instant marge = clock.instant().minusSeconds(60 * 60);
        HashMap<String, List<Instant>> eventsNewTime = new HashMap<>();
        for (Map.Entry<String, List<Instant>> entry : events.entrySet()) {
            boolean flagToContinue = false;
            while (true) {
                if (entry.getValue().size() == 0) {
                    flagToContinue = true;
                    break;
                }
                if (entry.getValue().get(0).isBefore(marge)) {
                    entry.getValue().remove(0);
                } else {
                    break;
                }
            }
            if (flagToContinue) {
                continue;
            }
            eventsNewTime.put(entry.getKey(), entry.getValue());
        }

        events = eventsNewTime;
    }

    private double getStatisticByName(String name) {
        if (events.containsKey(name)) {
            return events.get(name).size() / 60.0;
        }
        return 0;
    }

    public double getEventStatisticByName(String name) {
        updateEvents();
        return getStatisticByName(name);
    }

    public HashMap<String, Double> getAllEventStatistic() {
        updateEvents();
        HashMap<String, Double> statistics = new HashMap<>();
        for (Map.Entry<String, List<Instant>> entry : events.entrySet()) {
            statistics.put(entry.getKey(), getStatisticByName(entry.getKey()));
        }
        return statistics;
    }

    public void printStatistic() {
        Map<String, Double> statistics = getAllEventStatistic();

        for (Map.Entry<String, Double> entry : statistics.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }
}
