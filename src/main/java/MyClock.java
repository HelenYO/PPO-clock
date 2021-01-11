import java.time.Instant;
import java.time.Clock;
import java.time.ZoneId;

public class MyClock extends Clock {
    private Instant now;

    public Instant getNow() {
        return now;
    }

    public MyClock(Instant now) {
        this.now = now;
    }

    public void setNow(Instant now) {
        this.now = now;
    }

    @Override
    public ZoneId getZone() {
        return null;
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return null;
    }

    @Override
    public Instant instant() {
        return now;
    }
}
