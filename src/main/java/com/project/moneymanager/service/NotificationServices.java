package com.project.moneymanager.service;
import com.project.moneymanager.dto.ExpensesDto;
import com.project.moneymanager.entity.ProfileEntity;
import com.project.moneymanager.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServices
{

        private final ProfileRepository profileRepository;
        private final EmailServices emailServices;
        private final ExpensesServices expensesServices;
        @Value("${money.manager.frontend.url}")
        private String frontendUrl;
//        @Scheduled(cron="0 * * * * *",zone="IST")
        @Scheduled(cron="0 0 22 * * *",zone="IST")
        public void sendDailyIncomeExpensesRemainder()
        {
            log.info("Job started:send daily income expense remainder");
            List<ProfileEntity> profiles=profileRepository.findAll();
            for(ProfileEntity profile:profiles)
            {
                String body="Hi " + profile.getName() + ",<br><br>" +
                    "This is a friendly reminder to add your daily expenses and income to MoneyManager.<br><br>" +
                    "<a href=\"" + frontendUrl + "\" " +
                    "style=\"background-color:#4CAF50; color:white; padding:10px 20px; border:none; border-radius:5px; cursor:pointer; font-size:16px; text-decoration:none;\">" +
                    "Go to MoneyManager" +
                    "</a><br><br>" +
                    "Best regards,<br>" +
                    "MoneyManager Team";

                emailServices.sendEmail(profile.getEmail(), "Daily Remailder:Add your daily expense and income",body);
            }

        }

//        @Scheduled(cron="0 * * * * *",zone="IST")
        @Scheduled(cron="0 0 23 * * *",zone="IST")
        public void getAllTodayExpenses(){
            log.info("sending daily expenses to  user");
            List<ProfileEntity> profiles=profileRepository.findAll();
            for (ProfileEntity profile:profiles)
            {
                StringBuilder table=new StringBuilder();
                List<ExpensesDto> expenses=expensesServices.getExpenseForUserOnDate(profile.getId(), LocalDate.now());
                if(expenses==null)continue;

                table.append("<table style='width:100%; border-collapse: collapse;'>");
                table.append("<tr style='background-color:#f2f2f2;'>")
                        .append("<th style='border:1px solid #ddd; padding:8px;'>#</th>")
                        .append("<th style='border:1px solid #ddd; padding:8px;'>Name</th>")
                        .append("<th style='border:1px solid #ddd; padding:8px;'>Amount</th>")
                        .append("<th style='border:1px solid #ddd; padding:8px;'>Category</th>")
                        .append("</tr>");
                int i=1;
                for(ExpensesDto expense:expenses)
                {
                    table.append("<tr>")
                            .append("<td style='border:1px solid #ddd; padding:8px;'>").append(i++).append("</td>")
                            .append("<td style='border:1px solid #ddd; padding:8px;'>").append(expense.getName()).append("</td>")
                            .append("<td style='border:1px solid #ddd; padding:8px;'>").append(expense.getAmount()).append("</td>")
                            .append("<td style='border:1px solid #ddd; padding:8px;'>").append(expense.getCategoryName()).append("</td>")
                            .append("</tr>");

                }

                table.append("</table>");
                String message = "Hi " + profile.getName() + ",<br><br>" +
                        "Here are your expenses for today:<br><br>" +
                        table.toString() +
                        "<br><br>Best regards,<br>MoneyManager Team";
                emailServices.sendEmail(profile.getEmail(),"your's today expenses list",message);
            }
            log.info("sucesfully sended daily expenses to user");
        }


}
