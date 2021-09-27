package fun.bookish.blueberry.sip.entity;


/**
 * catalog设备列表中设备项描述
 */
public class SipChannelDesc {

    /**
     * 设备/区域/系统编码(必选)
     */
    private String deviceId;
    /**
     * 设备/区域/系统名称(必选)
     */
    private String name;
    /**
     * 当为设备时,设备厂商(必选)
     */
    private String manufacturer;
    /**
     * 当为设备时,设备型号(必选)
     */
    private String model;
    /**
     * 当为设备时,设备归属(必选)
     */
    private String owner;
    /**
     * 行政区域(必选)
     */
    private String civilCode;
    /**
     * 警区(可选)
     */
    private String block;
    /**
     * 当为设备时,安装地址(必选)
     */
    private String address;
    /**
     * 当为设备时,是否有子设备(必选) 1 有, 0 没有
     */
    private Integer parental;
    /**
     * 父设备/区域/系统 ID (必选)
     */
    private String parentId;
    /**
     * 父层级ID，避免有些sip平台作为下级时，其返回的设备列表中parentID字段与实际设备目录层级不一致
     */
    private String parentChannelId;
    /**
     * 信令安全模式(可选)缺省为 0 ;
     *       0 :不采用;
     *       2 : S / MIME 签名方式;
     *       3 : S / MIME 加密签名同时采用方式;
     *       4 :数字摘要方式
     */
    private Integer safetyWay;
    /**
     * 注册方式(必选)缺省为 1 ;
     *      1 :符合 IETFRFC3261 标准的认证注册模式;
     *      2 :基于口令的双向认证注册模式;
     *      3 :基于数字证书的双向认证注册模式 -
     */
    private Integer registerWay;
    /**
     * 证书序列号(有证书的设备必选)
     */
    private String certNum;
    /**
     * 证书有效标识(有证书的设备必选)缺省为 0 ;
     *      0 :无效
     *      1 :有效
     */
    private Integer certifiable;
    /**
     * 无效原因码(有证书且证书无效的设备必选)
     */
    private Integer errCode;
    /**
     * 证书终止有效期(有证书的设备必选)
     */
    private String endTime;
    /**
     * 保密属性(必选)缺省为 0 ;
     *      0 :不涉密
     *      1 :涉密
     */
    private Integer secrecy;
    /**
     * 设备/区域/系统 IP 地址(可选)
     */
    private String ipAddress;
    /**
     * 设备/区域/系统端口(可选)
     */
    private Integer port;
    /**
     * 设备口令(可选)
     */
    private String password;
    /**
     * 设备状态(必选)
     */
    private String status;
    /**
     * 经度(可选)
     */
    private Double longitude;
    /**
     * 纬度(可选)
     */
    private Double latitude;
    /**
     *  摄像机类型扩展,标识摄像机类型:
     *      1- 球机;
     *      2- 半球;
     *      3- 固定枪机;
     *      4- 遥控枪机。
     *  当目录项为摄像机时可选。
     */
    private Integer ptzType;
    /**
     * 摄像机位置类型扩展。
     *      1- 省际检查站
     *      2- 党政机关
     *      3- 车站码头
     *      4- 中心广场
     *      5- 体育场馆
     *      6- 商业中心
     *      7- 宗教场所
     *      8- 校园周边
     *      9- 治安复杂区域
     *      10- 交通干线
     * 当目录项为摄像机时可选
     */
    private Integer positionType;
    /**
     * 摄像机安装位置室外、室内属性。
     *      1- 室外
     *      2- 室内
     * 当目录项为摄像机时可选,缺省为 1
     */
    private Integer roomType;
    /**
     *  摄像机用途属性。
     *      1- 治安
     *      2- 交通
     *      3- 重点
     *  当目录项为摄像机时可选。
     */
    private Integer useType;
    /**
     *  摄像机补光属性。
     *      1- 无补光
     *      2- 红外补光
     *      3- 白光补光
     *  当目录项为摄像机时可选,缺省为 1
     */
    private Integer supplyLightType;
    /**
     *  摄像机监视方位属性。
     *      1- 东
     *      2- 西
     *      3- 南
     *      4- 北
     *      5- 东南
     *      6- 东北
     *      7- 西南
     *      8- 西北
     *  当目录项为摄像机时且为固定摄像机或设置看守位摄像机时可选。
     */
    private Integer directionType;
    /**
     *  摄像机支持的分辨率,可有多个分辨率值,各个取值间以“/”分隔。分辨率取值参见附录 F 中 SDPf 字段规定。当目录项为摄像机时可选
     */
    private String resolution;
    /**
     *  虚拟组织所属的业务分组 ID ,业务分组根据特定的业务需求制定,一个业务分组包含一组特定的虚拟组织
     */
    private String businessGroupId;
    /**
     * 下载倍速范围(可选),各可选参数以“/”分隔,如设备支持 1 , 2 , 4 倍速下载则应写为“1 / 2 / 4 ”
     */
    private String downloadSpeed;
    /**
     * 空域编码能力,取值(可选)
     *      0 :不支持;
     *      1 : 1 级增强( 1 个增强层);
     *      2 : 2 级增强(2 个增强层);
     *      3 : 3 级增强( 3 个增强层)
     */
    private Integer svcSpaceSupportMode;
    /**
     * 时域编码能力,取值 (可选)
     *      0 :不支持;
     *      1 : 1 级增强;
     *      2 : 2 级增强;
     *      3 : 3 级增强
     */
    private Integer svcTimeSupportMode;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCivilCode() {
        return civilCode;
    }

    public void setCivilCode(String civilCode) {
        this.civilCode = civilCode;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getParental() {
        return parental;
    }

    public void setParental(Integer parental) {
        this.parental = parental;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getSafetyWay() {
        return safetyWay;
    }

    public void setSafetyWay(Integer safetyWay) {
        this.safetyWay = safetyWay;
    }

    public Integer getRegisterWay() {
        return registerWay;
    }

    public void setRegisterWay(Integer registerWay) {
        this.registerWay = registerWay;
    }

    public String getCertNum() {
        return certNum;
    }

    public void setCertNum(String certNum) {
        this.certNum = certNum;
    }

    public Integer getCertifiable() {
        return certifiable;
    }

    public void setCertifiable(Integer certifiable) {
        this.certifiable = certifiable;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getSecrecy() {
        return secrecy;
    }

    public void setSecrecy(Integer secrecy) {
        this.secrecy = secrecy;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Integer getPtzType() {
        return ptzType;
    }

    public void setPtzType(Integer ptzType) {
        this.ptzType = ptzType;
    }

    public Integer getPositionType() {
        return positionType;
    }

    public void setPositionType(Integer positionType) {
        this.positionType = positionType;
    }

    public Integer getRoomType() {
        return roomType;
    }

    public void setRoomType(Integer roomType) {
        this.roomType = roomType;
    }

    public Integer getUseType() {
        return useType;
    }

    public void setUseType(Integer useType) {
        this.useType = useType;
    }

    public Integer getSupplyLightType() {
        return supplyLightType;
    }

    public void setSupplyLightType(Integer supplyLightType) {
        this.supplyLightType = supplyLightType;
    }

    public Integer getDirectionType() {
        return directionType;
    }

    public void setDirectionType(Integer directionType) {
        this.directionType = directionType;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getBusinessGroupId() {
        return businessGroupId;
    }

    public void setBusinessGroupId(String businessGroupId) {
        this.businessGroupId = businessGroupId;
    }

    public String getDownloadSpeed() {
        return downloadSpeed;
    }

    public void setDownloadSpeed(String downloadSpeed) {
        this.downloadSpeed = downloadSpeed;
    }

    public Integer getSvcSpaceSupportMode() {
        return svcSpaceSupportMode;
    }

    public void setSvcSpaceSupportMode(Integer svcSpaceSupportMode) {
        this.svcSpaceSupportMode = svcSpaceSupportMode;
    }

    public Integer getSvcTimeSupportMode() {
        return svcTimeSupportMode;
    }

    public void setSvcTimeSupportMode(Integer svcTimeSupportMode) {
        this.svcTimeSupportMode = svcTimeSupportMode;
    }

    public void setParentChannelId(String parentChannelId) {
        this.parentChannelId = parentChannelId;
    }

    public String getParentChannelId() {
        return parentChannelId;
    }
}
