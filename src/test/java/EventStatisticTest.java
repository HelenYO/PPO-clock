import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class EventStatisticTest {
    private MyClock clock;
    private EventsStatistic eventsStatistic;

    @Before
    public void setUp() {
        clock = new MyClock(Instant.now());
        eventsStatistic = new MyEventsStatistic(clock);
    }

    @Test
    public void noEvents() {
        Assert.assertTrue(eventsStatistic.getAllEventStatistic().isEmpty());
    }

    @Test
    public void noEventName() {
        Assert.assertEquals(0, eventsStatistic.getEventStatisticByName("Boom"), 1e-3);
        eventsStatistic.incEvent("Boom");
        Assert.assertEquals(0, eventsStatistic.getEventStatisticByName("Boom2"), 1e-3);
    }

    @Test
    public void oneEventOneHour() {
        eventsStatistic.incEvent("Boom");
        eventsStatistic.incEvent("Boom");
        eventsStatistic.incEvent("Boom");
        Assert.assertEquals(3 / 60.0, eventsStatistic.getEventStatisticByName("Boom"), 1e-3);
        Map<String, Double> statistic = eventsStatistic.getAllEventStatistic();
        Assert.assertEquals(statistic.size(), 1);
        Assert.assertEquals(statistic.get("Boom"), 3 / 60.0, 1e-3);
    }

    @Test
    public void oneEventOneHourDifferentTime() {
        eventsStatistic.incEvent("Boom");
        clock.setNow(clock.getNow().plus(10, ChronoUnit.MINUTES));
        eventsStatistic.incEvent("Boom");
        clock.setNow(clock.getNow().plus(10, ChronoUnit.MINUTES));
        eventsStatistic.incEvent("Boom");
        Assert.assertEquals(3 / 60.0, eventsStatistic.getEventStatisticByName("Boom"), 1e-3);
        Map<String, Double> statistic = eventsStatistic.getAllEventStatistic();
        Assert.assertEquals(statistic.size(), 1);
        Assert.assertEquals(statistic.get("Boom"), 3 / 60.0, 1e-3);
    }

    @Test
    public void eventsOneHour() {
        eventsStatistic.incEvent("Boom");
        eventsStatistic.incEvent("Boom1");
        eventsStatistic.incEvent("Boom2");
        Assert.assertEquals(1 / 60.0, eventsStatistic.getEventStatisticByName("Boom"), 1e-3);
        Assert.assertEquals(1 / 60.0, eventsStatistic.getEventStatisticByName("Boom1"), 1e-3);
        Assert.assertEquals(1 / 60.0, eventsStatistic.getEventStatisticByName("Boom2"), 1e-3);
        Map<String, Double> statistic = eventsStatistic.getAllEventStatistic();
        Assert.assertEquals(statistic.size(), 3);
        Assert.assertEquals(statistic.get("Boom"), 1 / 60.0, 1e-3);
        Assert.assertEquals(statistic.get("Boom1"), 1 / 60.0, 1e-3);
        Assert.assertEquals(statistic.get("Boom2"), 1 / 60.0, 1e-3);
    }

    @Test
    public void eventsOneHourDifferentTime() {
        eventsStatistic.incEvent("Boom");
        clock.setNow(clock.getNow().plus(10, ChronoUnit.MINUTES));
        eventsStatistic.incEvent("Boom1");
        clock.setNow(clock.getNow().plus(10, ChronoUnit.MINUTES));
        eventsStatistic.incEvent("Boom2");
        Assert.assertEquals(1 / 60.0, eventsStatistic.getEventStatisticByName("Boom"), 1e-3);
        Assert.assertEquals(1 / 60.0, eventsStatistic.getEventStatisticByName("Boom1"), 1e-3);
        Assert.assertEquals(1 / 60.0, eventsStatistic.getEventStatisticByName("Boom2"), 1e-3);
        Map<String, Double> statistic = eventsStatistic.getAllEventStatistic();
        Assert.assertEquals(statistic.size(), 3);
        Assert.assertEquals(statistic.get("Boom"), 1 / 60.0, 1e-3);
        Assert.assertEquals(statistic.get("Boom1"), 1 / 60.0, 1e-3);
        Assert.assertEquals(statistic.get("Boom2"), 1 / 60.0, 1e-3);
    }

    @Test
    public void erasingEvents() {
        eventsStatistic.incEvent("Boom");
        eventsStatistic.incEvent("Boom");
        eventsStatistic.incEvent("Boom");
        Assert.assertEquals(3 / 60.0, eventsStatistic.getEventStatisticByName("Boom"), 1e-3);
        clock.setNow(clock.getNow().plus(61, ChronoUnit.MINUTES));
        Assert.assertEquals(0 / 60.0, eventsStatistic.getEventStatisticByName("Boom"), 1e-3);

        eventsStatistic.incEvent("Boom");
        eventsStatistic.incEvent("Boom1");
        eventsStatistic.incEvent("Boom2");
        Map<String, Double> statistic = eventsStatistic.getAllEventStatistic();
        Assert.assertEquals(statistic.size(), 3);
        Assert.assertEquals(1 / 60.0, eventsStatistic.getEventStatisticByName("Boom"), 1e-3);
    }

    @Test
    public void erasingEventsPartially() {
        eventsStatistic.incEvent("Boom");
        clock.setNow(clock.getNow().plus(10, ChronoUnit.MINUTES));
        eventsStatistic.incEvent("Boom");
        clock.setNow(clock.getNow().plus(10, ChronoUnit.MINUTES));
        eventsStatistic.incEvent("Boom");
        Assert.assertEquals(3 / 60.0, eventsStatistic.getEventStatisticByName("Boom"), 1e-3);

        clock.setNow(clock.getNow().plus(51, ChronoUnit.MINUTES));
        Assert.assertEquals(1 / 60.0, eventsStatistic.getEventStatisticByName("Boom"), 1e-3);
        clock.setNow(clock.getNow().plus(61, ChronoUnit.MINUTES));
        Assert.assertEquals(0 / 60.0, eventsStatistic.getEventStatisticByName("Boom"), 1e-3);

        eventsStatistic.incEvent("Boom");
        clock.setNow(clock.getNow().plus(10, ChronoUnit.MINUTES));
        eventsStatistic.incEvent("Boom1");
        clock.setNow(clock.getNow().plus(10, ChronoUnit.MINUTES));
        eventsStatistic.incEvent("Boom2");
        Map<String, Double> statistic = eventsStatistic.getAllEventStatistic();
        Assert.assertEquals(statistic.size(), 3);
        Assert.assertEquals(1 / 60.0, eventsStatistic.getEventStatisticByName("Boom"), 1e-3);

        clock.setNow(clock.getNow().plus(51, ChronoUnit.MINUTES));
        Assert.assertEquals(1 / 60.0, eventsStatistic.getEventStatisticByName("Boom2"), 1e-3);
        Map<String, Double> statistic2 = eventsStatistic.getAllEventStatistic();
        Assert.assertEquals(statistic2.size(), 1);
    }
}