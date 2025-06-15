package santa.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import santa.SantaConfig;
import santa.entities.Participant;
import santa.entities.SantaGroup;
import santa.repositories.GroupRepository;
import santa.services.ReminderSenderService;
import santa.services.ReminderService;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReminderServiceImpl implements ReminderService {
    private final GroupRepository groupRepository;
    private final ReminderSenderService reminderSenderService;
    private final SantaConfig santaConfig;

    @Transactional
    @Override
    public void remind() {
        log.atInfo().log("Remind started");

        LocalDate today = LocalDate.now(ZoneId.systemDefault());
        List<Integer> reminderDays = santaConfig.notification().reminders().days();

        List<SantaGroup> groups = groupRepository.findAll();
        for (SantaGroup group : groups) {
            Instant instant = group.getExchangeDate();
            LocalDate exchangeDate = LocalDateTime
                    .ofInstant(instant, ZoneId.systemDefault())
                    .toLocalDate();

            long daysRemaining = ChronoUnit.DAYS.between(today, exchangeDate);
            if (reminderDays.contains((int) daysRemaining)) {
                for (Participant part : group.getParticipants()) {
                    reminderSenderService.sendReminder(
                            part.getUser(),
                            group,
                            (int) daysRemaining
                    );
                }
            }
        }

        log.atInfo().log("Remind stopped");
    }
}
