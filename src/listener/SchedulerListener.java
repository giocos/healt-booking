package listener;

import it.sauronsoftware.cron4j.Scheduler;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class SchedulerListener implements ServletContextListener {

    private static final String CRON_SCHEDULE = "45 19 * * *";
    private static final long EXEC_TIME = 10;
    private static final long DELAY = 0;
    //executors
    private Scheduler dailyScheduler;
    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent servlet) {
        //init scheduler
        dailyScheduler = new Scheduler();
        scheduler = Executors.newSingleThreadScheduledExecutor();
        //init task
        //tasks
        ResetPrenotazioni resetPrenotazioni = new ResetPrenotazioni();
        EliminaPrenotazione eliminaPrenotazione = new EliminaPrenotazione();
        //start
        scheduler.scheduleAtFixedRate(eliminaPrenotazione, DELAY, EXEC_TIME, TimeUnit.MINUTES);
        dailyScheduler.schedule(CRON_SCHEDULE, resetPrenotazioni);
        dailyScheduler.start();
    }
	
    @Override
    public void contextDestroyed(ServletContextEvent servlet) {
        scheduler.shutdown();
        dailyScheduler.stop();
    }
}
