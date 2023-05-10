package com.feng.mannage.service;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feng.mannage.domain.AnalysisByDay;
import com.feng.mannage.mapper.AnalysisByDayMapper;
import com.feng.mannage.vo.AnalysisUsersVo;
import com.feng.mannage.vo.DataPointVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author f
 * @date 2023/5/10 22:45
 */
@Service
public class AnalysisService extends ServiceImpl<AnalysisByDayMapper, AnalysisByDay> {

    /**
     * 累计用户
     * @return long
     */
    public Long queryNumRegistered() {
        AnalysisByDay analysisByDay = getOne(Wrappers.<AnalysisByDay>query().select("sum(num_registered) as numRegistered"));
        return Long.valueOf(analysisByDay.getNumRegistered());
    }

    /**
     * 活跃用户
     * @param today     today
     * @param offset    offset
     * @return          long
     */
    public Long queryActiveUser(DateTime today, int offset) {
        return this.queryUserCount(today, offset, "num_active");
    }

    public Long queryRegisterUser(DateTime today, int offset) {
        return this.queryUserCount(today, offset, "num_registered");
    }

    /**
     * login count
     * @param today     today
     * @param offset    offset
     * @return          long
     */
    public Long queryLoginUserCount(cn.hutool.core.date.DateTime today, int offset) {
        return this.queryUserCount(today, offset, "num_login");
    }

    /**
     * user count
     * @param today     today
     * @param offset    offset
     * @param column    column
     * @return          long
     */
    private Long queryUserCount(cn.hutool.core.date.DateTime today, int offset, String column) {
        AnalysisByDay analysisByDay = super.getOne(Wrappers.<AnalysisByDay>query()
                .select("sum(" + column + ") as num_active")
                .le("record_date", today.toDateStr())
                .ge("record_date", DateUtil.offsetDay(today, offset).toDateStr()));
        return Long.valueOf(analysisByDay.getNumActive());
    }

    /**
     * 新增/活跃用户/次日留存率
     * @param sd    sd
     * @param ed    ed
     * @param type  type
     * @return      vo
     */
    public AnalysisUsersVo queryAnalysisUsersVo(Long sd, Long ed, Integer type) {
        DateTime startDate = DateUtil.date(sd);
        DateTime endDate = DateUtil.date(ed);

        AnalysisUsersVo analysisUsersVo = new AnalysisUsersVo();

        analysisUsersVo.setThisYear(this.queryDataPointVos(startDate, endDate, type));
        analysisUsersVo.setLastYear(this.queryDataPointVos(DateUtil.offset(startDate, DateField.YEAR, - 1)
                , DateUtil.offset(endDate, DateField.YEAR, - 1), type));

        return analysisUsersVo;
    }

    private List<DataPointVo> queryDataPointVos(DateTime sd, DateTime ed, Integer type) {
        String startDate = sd.toDateStr();
        String endDate = ed.toDateStr();

        String column = null;
        switch (type) {
            case 101:
                column = "num_registered";
                break;
            case 102:
                column = "num_active";
                break;
            case 103:
                column = "num_retention1d";
                break;
            default:
                column = "num_active";
                break;
        }

        List<AnalysisByDay> analysisByDayList = super.list(Wrappers.<AnalysisByDay>query()
                .select("record_date, " + column + " as num_active")
                .ge("record_date", startDate)
                .le("record_date", endDate));

        return analysisByDayList.stream().map(analysisByDay -> new DataPointVo(
                DateUtil.date(analysisByDay.getRecordDate()).toDateStr(), analysisByDay.getNumActive().longValue()
        )).collect(Collectors.toList());
    }
}
