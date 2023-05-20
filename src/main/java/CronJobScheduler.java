import com.example.demo.HabitReminderJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class CronJobScheduler {
    public static void main(String[] args) throws SchedulerException {
        // Create a Quartz scheduler factory
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();

        // Define the cron job and its trigger
        JobDetail jobDetail = JobBuilder.newJob(HabitReminderJob.class)
                .withIdentity("habitReminderJob", "group1")
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("habitReminderTrigger", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 * * ?")) // Run daily at 12:00 AM
                .build();

        // Schedule the job with the trigger
        scheduler.scheduleJob(jobDetail, trigger);

        // Start the scheduler
        scheduler.start();
    }
}
