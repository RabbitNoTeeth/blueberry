package fun.bookish.blueberry.server.videoqualitydetect.arithmetic.service;

import fun.bookish.blueberry.core.page.PageResult;
import fun.bookish.blueberry.server.videoqualitydetect.arithmetic.entity.*;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 视频质量检测算法 服务类
 * </p>
 *
 * @author RabbitNoTeeth
 * @since 2021-06-10
 */
public interface IVideoQualityDetectArithmeticService extends IService<VideoQualityDetectArithmeticPO> {

    /**
     * 添加
     *
     * @param videoQualityDetectArithmeticVO
     * @return
     */
    Integer add(VideoQualityDetectArithmeticVO videoQualityDetectArithmeticVO);

    /**
     * 更新
     *
     * @param videoQualityDetectArithmeticVO
     * @return
     */
    Integer update(VideoQualityDetectArithmeticVO videoQualityDetectArithmeticVO);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    Integer deleteById(Integer id);

    /**
     * 查询列表
     *
     * @return
     * @param params
     */
    List<VideoQualityDetectArithmeticVO> queryList(VideoQualityDetectArithmeticQueryParamVO params);

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param params
     * @return
     */
    PageResult<VideoQualityDetectArithmeticVO> queryPage(Integer page, Integer pageSize, VideoQualityDetectArithmeticQueryParamVO params);

    /**
     * 查询支持的算法列表
     *
     * @return
     */
    List<String> querySupportedArithmeticList();

    /**
     * 查询可应用的设备列表
     *
     * @return
     */
    List<VideoQualityDetectArithmeticApplicableDeviceVO> queryApplicableDeviceList();

    /**
     * 查询已应用的设备列表
     *
     * @param id
     * @return
     */
    List<String> queryAppliedDeviceList(Integer id);

}
