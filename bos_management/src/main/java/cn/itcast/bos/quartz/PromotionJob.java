package cn.itcast.bos.quartz;

import cn.itcast.bos.service.base.PromotionService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class PromotionJob implements Job {
    @Autowired
    private PromotionService promotionService;

    @Override
    public void execute(JobExecutionContext context) {
        System.out.println("活动过期程序执行。。。");
        promotionService.updateStatus(new Date());
    }
}
