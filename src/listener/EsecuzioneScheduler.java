package listener;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import it.sauronsoftware.cron4j.Scheduler;

@WebListener
public class EsecuzioneScheduler implements ServletContextListener {

    private static final String CRON_SCHEDULE = "45 19 * * *";
    private static final long PERIODO_ESECUZIONE = 10;
    private static final long DELAY = 0;
    //executors
    private ScheduledExecutorService scheduler;
    private Scheduler dailyScheduler;
    //tasks
    private ResetDatabase resetDatabase;
    private EliminaPrenotazione eliminaPrenotazione;
    
    @Override
    public void contextInitialized(ServletContextEvent servlet) {

          eliminaPrenotazione = new EliminaPrenotazione();
          scheduler = Executors.newSingleThreadScheduledExecutor();
          scheduler.scheduleAtFixedRate(eliminaPrenotazione, DELAY, PERIODO_ESECUZIONE, TimeUnit.MINUTES);
     
          dailyScheduler = new Scheduler();
          resetDatabase = new ResetDatabase();

          dailyScheduler.schedule(CRON_SCHEDULE, resetDatabase);
          dailyScheduler.start();
    }
	
    @Override
    public void contextDestroyed(ServletContextEvent servlet) {
		
    	  scheduler.shutdown();
	  dailyScheduler.stop();
    }
}
