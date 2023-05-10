package com.feng.mannage.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.feng.mannage.service.AnalysisService;
import com.feng.mannage.utils.ComputeUtil;
import com.feng.mannage.vo.AnalysisSummaryVo;
import com.feng.mannage.vo.AnalysisUsersVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author f
 * @date 2023/5/10 22:45
 */
@RestController
public class AnalysisController {

    @Resource
    private AnalysisService analysisService;

    /**
     * 概要统计信息
     * @return vo
     */
    @GetMapping("/dashboard/summary")
    public AnalysisSummaryVo getSummary() {
        AnalysisSummaryVo analysisSummaryVo = new AnalysisSummaryVo();
        DateTime dateTime = DateUtil.parseDate("2023-05-10");

        analysisSummaryVo.setCumulativeUsers(analysisService.queryNumRegistered());
        analysisSummaryVo.setActivePassMonth(analysisService.queryActiveUser(dateTime, -30));
        analysisSummaryVo.setActivePassWeek(analysisService.queryActiveUser(dateTime, -7));
        analysisSummaryVo.setActiveUsersToday(analysisService.queryActiveUser(dateTime, 0));

        analysisSummaryVo.setNewUsersToday(analysisService.queryRegisterUser(dateTime, 0));

        analysisSummaryVo.setNewUsersTodayRate(ComputeUtil.computeRate(
                analysisSummaryVo.getNewUsersToday(),
                analysisService.queryRegisterUser(dateTime, - 1)
        ));

        analysisSummaryVo.setLoginTimesToday(analysisService.queryLoginUserCount(dateTime, 0));
        analysisSummaryVo.setLoginTimesTodayRate(ComputeUtil.computeRate(
                analysisSummaryVo.getLoginTimesToday(),
                analysisService.queryLoginUserCount(dateTime, - 1)
        ));
        return analysisSummaryVo;
    }

    @GetMapping("/dashboard/users")
    public AnalysisUsersVo getUsers(@RequestParam(name = "sd") Long sd
            , @RequestParam("ed") Long ed
            , @RequestParam("type") Integer type) {
        return this.analysisService.queryAnalysisUsersVo(sd, ed, type);
    }
}
