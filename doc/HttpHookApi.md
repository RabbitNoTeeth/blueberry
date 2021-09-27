Blueberry可将内部事件通过http hook的方式向外部通知。同时，所有hook钩子的请求参数全部以data为参数名称，以Json字符串格式发送，客户端接收到之后需要进行反序列化成对应的数据对象。



#### 1 配置示例

```
http-hook:
  enable: true
  timeout: 10
  onStreamQualityDetect: http://localhost:8080/hook/listen/onStreamQualityDetect
  onDeviceOnline: http://localhost:8080/hook/listen/onDeviceOnline
  onDeviceOffline: http://localhost:8080/hook/listen/onDeviceOffline
  onDeviceUpdate: http://localhost:8080/hook/listen/onDeviceUpdate
  onDeviceRemove: http://localhost:8080/hook/listen/onDeviceRemove
  onChannelUpdate: http://localhost:8080/hook/listen/onChannelUpdate
  onChannelRemove: http://localhost:8080/hook/listen/onChannelRemove
```



#### 2 详解

##### 2.1 enable

- 解释：

  是否启用http hook回调。



##### 2.2 timeout

- 解释：

  请求超时时长。



##### 2.3 onStreamQualityDetect

- 解释：

  设备视频质量检测完成后触发。

- 请求参数详解：

  | 参数名    | 参数类型 | 参数说明             |
  | --------- | -------- | -------------------- |
  | deviceId  | string   | 设备ID               |
  | channelId | string   | 通道ID               |
  | hasError  | boolean  | 视频质量是否异常     |
  | error     | string   | 视频质量异常说明     |
  | snapshot  | string   | 视频图像快照保存位置 |
  | detail    | string   | 视频质量检测结果详情 |

- 请求参数示例：

  ```
  {
  	"data": {
          "deviceId":"192.168.3.201",
          "channelId":"192.168.3.201",
          "snapshot":"./image/snapshots/192.168.3.201/192.168.3.201/20210608151711.jpeg"
          "hasError":false,
          "error": null,
          "hasQualityError":true,
          "qualityError":"条纹异常",
          "detail": "xxxxxxxx"
      }
  }
  ```



##### 2.4 onDeviceOnline

- 解释：

  设备上线后触发。

- 请求参数详解：

  | 参数名   | 参数类型 | 参数说明 |
  | -------- | -------- | -------- |
  | deviceId | string   | 设备ID   |

- 请求参数示例：

  ```
  {
  	"data": {
      	"deviceId":"192.168.3.201"
      }
  }
  ```



##### 2.5 onDeviceOffline

- 解释：

  设备离线后触发。

- 请求参数详解：

  | 参数名   | 参数类型 | 参数说明 |
  | -------- | -------- | -------- |
  | deviceId | string   | 设备ID   |

- 请求参数示例：

  ```
  {
  	"data": {
      	"deviceId":"192.168.3.201"
      }
  }
  ```



##### 2.6 onDeviceUpdate

- 解释：

  新建设备或者更新设备后触发。

- 请求参数详解：

  | 参数名           | 参数类型 | 参数说明                                                     |
  | ---------------- | -------- | ------------------------------------------------------------ |
  | id               | string   | 设备ID                                                       |
  | name             | string   | 设备名称                                                     |
  | manufacturer     | string   | 设备生产商                                                   |
  | model            | string   | 设备型号                                                     |
  | firmware         | string   | 设备固件版本                                                 |
  | type             | string   | 设备类型（GB：通过gb28181协议接入的设备；RTSP：通过rtsp协议接入的设备） |
  | commandTransport | string   | sip命令通信协议（UDP；TCP）                                  |
  | remoteIp         | string   | 设备ip                                                       |
  | remotePort       | int      | 设备端口                                                     |
  | online           | integer  | 是否在线                                                     |
  | sipServerAddress | string   | 设备注册的sip服务器地址（仅限于国标设备）                    |
  | expires          | int      | 设备过期时长                                                 |
  | lastRegisterAt   | string   | 设备最后注册时间（仅限于国标设备）                           |
  | lastKeepaliveAt  | string   | 设备最后保活时间（仅限于国标设备）                           |
  | createdAt        | string   | 设备创建时间                                                 |
  | updatedAt        | string   | 设备更新时间                                                 |
  | serverIp         | string   | 所在服务器ip                                                 |
  | serverPort       | int      | 所在服务器端口                                               |

- 请求参数示例：

  ```
  {
  	"data": {
          "id": "13020000001320000202",
          "name": "IP CAMERA",
          "manufacturer": "Hikvision",
          "model": "DS-2CD2310D-I",
          "firmware": "V5.4.24",
          "type": "GB",
          "commandTransport": "UDP",
          "remoteIp": "192.168.3.202",
          "remotePort": 5060,
          "online": 1,
          "sipServerAddress": "192.168.3.81:5060",
          "expires": 3600,
          "lastRegisterAt": "2021-06-08 15:22:52",
          "lastKeepaliveAt": "2021-06-08 15:35:22",
          "createdAt": "2021-06-08 10:25:20",
          "updatedAt": "2021-06-08 15:35:22",
          "serverIp": "192.168.3.81",
          "serverPort": 10001
      }
  }
  ```



##### 2.7 onDeviceUpdate

- 解释：

  新建设备或者更新设备后触发。

- 请求参数详解：

  | 参数名           | 参数类型 | 参数说明                                                     |
  | ---------------- | -------- | ------------------------------------------------------------ |
  | id               | string   | 设备ID                                                       |
  | name             | string   | 设备名称                                                     |
  | manufacturer     | string   | 设备生产商                                                   |
  | model            | string   | 设备型号                                                     |
  | firmware         | string   | 设备固件版本                                                 |
  | type             | string   | 设备类型（GB：通过gb28181协议接入的设备；RTSP：通过rtsp协议接入的设备） |
  | commandTransport | string   | sip命令通信协议（UDP；TCP）                                  |
  | remoteIp         | string   | 设备ip                                                       |
  | remotePort       | int      | 设备端口                                                     |
  | online           | int      | 是否在线                                                     |
  | sipServerAddress | string   | 设备注册的sip服务器地址（仅限于国标设备）                    |
  | expires          | int      | 设备过期时长                                                 |
  | lastRegisterAt   | string   | 设备最后注册时间（仅限于国标设备）                           |
  | lastKeepaliveAt  | string   | 设备最后保活时间（仅限于国标设备）                           |
  | createdAt        | string   | 设备创建时间                                                 |
  | updatedAt        | string   | 设备更新时间                                                 |
  | serverIp         | string   | 所在服务器ip                                                 |
  | serverPort       | int      | 所在服务器端口                                               |

- 请求参数示例：

  ```
  {
  	"data": {
          "id": "13020000001320000202",
          "name": "IP CAMERA",
          "manufacturer": "Hikvision",
          "model": "DS-2CD2310D-I",
          "firmware": "V5.4.24",
          "type": "GB",
          "commandTransport": "UDP",
          "remoteIp": "192.168.3.202",
          "remotePort": 5060,
          "online": 1,
          "sipServerAddress": "192.168.3.81:5060",
          "expires": 3600,
          "lastRegisterAt": "2021-06-08 15:22:52",
          "lastKeepaliveAt": "2021-06-08 15:35:22",
          "createdAt": "2021-06-08 10:25:20",
          "updatedAt": "2021-06-08 15:35:22",
          "serverIp": "192.168.3.81",
          "serverPort": 10001
      }
  }
  ```



##### 2.8 onDeviceRemove

- 解释：

  删除设备后触发。

- 请求参数详解：

  | 参数名   | 参数类型 | 参数说明 |
  | -------- | -------- | -------- |
  | deviceId | string   | 设备ID   |

- 请求参数示例：

  ```
  {
  	"data": {
      	"deviceId":"192.168.3.201"
      }
  }
  ```



##### 2.9 onChannelOnline

- 解释：

  通道上线后触发。

- 请求参数详解：

  | 参数名    | 参数类型 | 参数说明 |
  | --------- | -------- | -------- |
  | deviceId  | string   | 设备ID   |
  | channelId | string   | 通道ID   |

- 请求参数示例：

  ```
  {
  	"data": {
      	"deviceId":"13020000001320000202",
      	"channelId":"13020000001320000202"
      }
  }
  ```



##### 2.10 onChannelOffline

- 解释：

  通道离线后触发。

- 请求参数详解：

  | 参数名    | 参数类型 | 参数说明 |
  | --------- | -------- | -------- |
  | deviceId  | string   | 设备ID   |
  | channelId | string   | 通道ID   |

- 请求参数示例：

  ```
  {
  	"data": {
      	"deviceId":"13020000001320000202",
      	"channelId":"13020000001320000202"
      }
  }
  ```



##### 2.11 onChannelUpdate

- 解释：

  新建通道或者更新通道后触发。

- 请求参数详解：

  | 参数名              | 参数类型 | 参数说明                                                     |
  | ------------------- | -------- | ------------------------------------------------------------ |
  | id                  | string   | 设备/区域/系统编码                                           |
  | deviceId            | string   | 所属设备ID                                                   |
  | name                | string   | 设备/区域/系统名称                                           |
  | manufacturer        | string   | 当为设备时,设备厂商                                          |
  | model               | string   | 当为设备时,设备型号                                          |
  | owner               | string   | 当为设备时,设备归属                                          |
  | civilCode           | string   | 行政区域                                                     |
  | block               | string   | 警区                                                         |
  | address             | string   | 当为设备时,安装地址                                          |
  | parental            | int      | 当为设备时,是否有子设备（1 有, 0 没有）                      |
  | parentId            | string   | 父设备/区域/系统 ID                                          |
  | parentChannelId     | string   | 父层级ID，避免有些sip平台作为下级时，其返回的设备列表中parentID字段与实际设备目录层级不一致 |
  | safetyWay           | int      | 信令安全模式，缺省为 0                                       |
  | registerWay         | int      | 注册方式，缺省为 1                                           |
  | certNum             | string   | 证书序列号                                                   |
  | certifiable         | int      | 证书有效标识，缺省为 0                                       |
  | errCode             | int      | 无效原因码                                                   |
  | endTime             | string   | 证书终止有效期                                               |
  | secrecy             | int      | 保密属性，缺省为 0                                           |
  | ipAddress           | string   | 设备/区域/系统 IP 地址                                       |
  | port                | int      | 设备/区域/系统端口                                           |
  | password            | string   | 设备口令                                                     |
  | status              | string   | 设备状态                                                     |
  | longitude           | double   | 经度                                                         |
  | latitude            | double   | 纬度                                                         |
  | ptzType             | int      | 摄像机类型扩展,标识摄像机类型                                |
  | positionType        | int      | 摄像机位置类型扩展                                           |
  | roomType            | int      | 摄像机安装位置室外、室内属性                                 |
  | useType             | int      | 摄像机用途属性                                               |
  | supplyLightType     | int      | 摄像机补光属性                                               |
  | directionType       | int      | 摄像机监视方位属性                                           |
  | resolution          | string   | 摄像机支持的分辨率,可有多个分辨率值,各个取值间以“/”分隔      |
  | businessGroupId     | string   | 虚拟组织所属的业务分组 ID ,业务分组根据特定的业务需求制定,一个业务分组包含一组特定的虚拟组织 |
  | downloadSpeed       | string   | 下载倍速范围(可选),各可选参数以“/”分隔,如设备支持 1 , 2 , 4 倍速下载则应写为“1 / 2 / 4 ” |
  | svcSpaceSupportMode | int      | 空域编码能力                                                 |
  | svcTimeSupportMode  | int      | 时域编码能力                                                 |
  | rtsp                | string   | rtsp地址                                                     |
  | online              | int      | 是否在线                                                     |
  | createdAt           | string   | 创建时间                                                     |
  | updatedAt           | string   | 更新时间                                                     |
  | device              | object   | 所属设备                                                     |

  device数据结构如下表

  | 参数名           | 参数类型 | 参数说明                                                     |
  | ---------------- | -------- | ------------------------------------------------------------ |
  | id               | string   | 设备ID                                                       |
  | name             | string   | 设备名称                                                     |
  | manufacturer     | string   | 设备生产商                                                   |
  | model            | string   | 设备型号                                                     |
  | firmware         | string   | 设备固件版本                                                 |
  | type             | string   | 设备类型（GB：通过gb28181协议接入的设备；RTSP：通过rtsp协议接入的设备） |
  | commandTransport | string   | sip命令通信协议（UDP；TCP）                                  |
  | remoteIp         | string   | 设备ip                                                       |
  | remotePort       | int      | 设备端口                                                     |
  | online           | integer  | 是否在线                                                     |
  | sipServerAddress | string   | 设备注册的sip服务器地址（仅限于国标设备）                    |
  | expires          | int      | 设备过期时长                                                 |
  | lastRegisterAt   | string   | 设备最后注册时间（仅限于国标设备）                           |
  | lastKeepaliveAt  | string   | 设备最后保活时间（仅限于国标设备）                           |
  | createdAt        | string   | 设备创建时间                                                 |
  | updatedAt        | string   | 设备更新时间                                                 |
  | serverIp         | string   | 所在服务器ip                                                 |
  | serverPort       | int      | 所在服务器端口                                               |

- 请求参数示例：

  ```
  {
  	"data": {
          "id": "13020000001320000202",
          "deviceId": "13020000001320000202",
          "name": "一层",
          "manufacturer": "Hikvision",
          "model": "IP Camera",
          "owner": "Owner",
          "civilCode": "CivilCode",
          "block": null,
          "address": "Address",
          "parental": 0,
          "parentId": "13020000001320000202",
          "parentChannelId": null,
          "safetyWay": 0,
          "registerWay": 1,
          "certNum": null,
          "certifiable": null,
          "errCode": null,
          "endTime": null,
          "secrecy": 0,
          "ipAddress": null,
          "port": null,
          "password": null,
          "status": "ON",
          "longitude": null,
          "latitude": null,
          "ptzType": null,
          "positionType": null,
          "roomType": null,
          "useType": null,
          "supplyLightType": null,
          "directionType": null,
          "resolution": null,
          "businessGroupId": null,
          "downloadSpeed": null,
          "svcSpaceSupportMode": null,
          "svcTimeSupportMode": null,
          "rtsp": null,
          "online": null,
          "createdAt": "2021-06-08 16:08:34",
          "updatedAt": "2021-06-08 16:08:34",
          "device": {
              "id": "13020000001320000202",
              "name": "IP CAMERA",
              "manufacturer": "Hikvision",
              "model": "DS-2CD2310D-I",
              "firmware": "V5.4.24",
              "type": "GB",
              "commandTransport": "UDP",
              "remoteIp": "192.168.3.202",
              "remotePort": 5060,
              "online": 1,
              "sipServerAddress": "192.168.3.81:5060",
              "expires": 3600,
              "lastRegisterAt": "2021-06-08 16:08:34",
              "lastKeepaliveAt": "2021-06-08 16:21:38",
              "createdAt": "2021-06-08 10:25:20",
              "updatedAt": "2021-06-08 16:21:38",
              "serverIp": "192.168.3.81",
              "serverPort": 10001
          }
      }
  }
  ```



##### 2.12 onChannelRemove

- 解释：

  删除通道后触发。

- 请求参数详解：

  | 参数名    | 参数类型 | 参数说明 |
  | --------- | -------- | -------- |
  | deviceId  | string   | 设备ID   |
  | channelId | string   | 通道ID   |

- 请求参数示例：

  ```
  {
  	"data": {
      	"deviceId":"192.168.3.201"
      }
  }
  ```





