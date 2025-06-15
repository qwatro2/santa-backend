package santa.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import santa.SantaConfig;
import santa.services.ReminderService;

import java.time.LocalTime;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulingConfig implements SchedulingConfigurer {
    private final SantaConfig santaConfig;
    private final ReminderService reminderService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        LocalTime t = LocalTime.parse(santaConfig.notification().schedule().time());
        String cron = String.format("0 %d %d * * *",
                t.getMinute(),
                t.getHour()
        );
        taskRegistrar.addCronTask(reminderService::remind, cron);
    }
}
