
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhoukai
 * @Description:
 * @date 2022/4/19 11:35
 */
public class CallThreadUtil {

    public static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors() * 4;

    private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
    public static final Executor POOL_EXECUTOR =
            new ThreadPoolExecutor(THREAD_COUNT, THREAD_COUNT * 2, 1, TimeUnit.SECONDS
                    , new LinkedBlockingQueue<>(64000), (r) -> {
                Thread thread = new Thread(r);
                thread.setName("CallHandler-" + POOL_NUMBER.getAndIncrement());
                if (thread.isDaemon()) {
                    thread.setDaemon(false);
                }
                return thread;
            }, (r, executor) -> {
                if (!executor.isShutdown()) {
                    executor.getQueue().poll().run();
                    executor.getQueue().add(r);
                }
            });


}
