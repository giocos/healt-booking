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

    private final long DELAY = 0;
    private final long PERIODO_ESECUZIONE = 10;
    private ScheduledExecutorService scheduler;
    private EliminaPrenotazione eliminaPrenotazione;
    private ResetDatabase resetDatabase;
    private Scheduler dailyScheduler;
    
    @Override
    public void contextInitialized(ServletContextEvent servlet) {
		
          scheduler = Executors.newSingleThreadScheduledExecutor();
          eliminaPrenotazione = new EliminaPrenotazione(); 
          scheduler.scheduleAtFixedRate(eliminaPrenotazione, DELAY, PERIODO_ESECUZIONE, TimeUnit.MINUTES);
     
          dailyScheduler = new Scheduler();
          resetDatabase = new ResetDatabase();
          dailyScheduler.schedule("45 19 * * *", resetDatabase);
          dailyScheduler.start();
    }
	
    @Override
    public void contextDestroyed(ServletContextEvent servlet) {
		
    	  scheduler.shutdown();
	  dailyScheduler.stop();
    }
}
