package fun.bookish.blueberry.server.dev.service;

import fun.bookish.blueberry.core.utils.DateUtils;
import fun.bookish.blueberry.server.videoqualitydetect.detect.entity.VideoStreamQualityDetectResult;
import fun.bookish.blueberry.server.videoqualitydetect.detect.VideoStreamDetectService;
import fun.bookish.blueberry.sip.SipServer;
import fun.bookish.blueberry.sip.command.executor.query.SipQueryCommandExecutor;
import fun.bookish.blueberry.sip.command.executor.query.param.SipQueryCatalogParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DevService {

    @Autowired
    private SipServer sipServer;
    @Autowired
    private VideoStreamDetectService videoStreamDetectService;

    /**
     * 发送sip命令，查询设备列表
     *
     * @param param
     * @return
     * @throws Exception
     */
    public Object sipCatalog(SipQueryCatalogParam param) throws Exception {
        SipQueryCommandExecutor sipQueryCommandExecutor = sipServer.getSipCommandExecutorManager().getSipQueryCommandExecutor();
        sipQueryCommandExecutor.queryCatalog(param);
        return "success";
    }

    /**
     * 触发设备图像质量检测钩子
     *
     * @param deviceId
     * @param channelId
     * @return
     */
    public Object streamQualityDetect(String deviceId, String channelId) {
        VideoStreamQualityDetectResult data = videoStreamDetectService.detectStreamQuality(deviceId, channelId, DateUtils.getNowDateStr("yyyyMMddHHmmss"));
        return data;
    }


}
