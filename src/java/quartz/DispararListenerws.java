package quartz;

import javax.servlet.ServletContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.ee.servlet.QuartzInitializerListener;
import static org.quartz.ee.servlet.QuartzInitializerListener.QUARTZ_FACTORY_KEY;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase donde se implementa el escuchador definido en web.xml El listener se
 * ejecutar� al arrancar la aplicaci�n. Su funcionamiento ser� definir una tarea
 * en Quartz y lanzar el trigger con los par�metros que interesen (en este caso
 * un retardo de 60 segundos).
 *
 * @author gonzalo.delgado
 *
 */
@WebListener
public class DispararListenerws extends QuartzInitializerListener {

    private static final Logger LOG = LoggerFactory.getLogger(DispararListenerws.class);
    
    @Override
    public void contextInitialized(ServletContextEvent event) {
        super.contextInitialized(event);
        ServletContext ctx = event.getServletContext();
        StdSchedulerFactory factory = (StdSchedulerFactory) ctx.getAttribute(QUARTZ_FACTORY_KEY);
        try {
            Scheduler scheduler = factory.getScheduler();
            JobDetail job = JobBuilder.newJob(DisparaJobws.class).build();
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("simple1").withSchedule(
                    CronScheduleBuilder.cronSchedule("0/2 * * * * ? *")
            ).startNow().build();
            scheduler.scheduleJob(job, trigger);
            scheduler.start();
        } catch (Exception e) {
            LOG.error("Ocurrio un error al calendarizar el trabajo", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        super.contextDestroyed(event);
    }
}
