package fun.bookish.blueberry.server.schedule.lock;

import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class ScheduleLockManager {

    private final Lock STREAM_LOCK = new ReentrantLock();

    public Lock getStreamLock() {
        return STREAM_LOCK;
    }

}
