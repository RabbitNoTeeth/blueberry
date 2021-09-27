package fun.bookish.blueberry.server.videoqualitydetect.arithmetic.entity;

import java.util.List;

public class VideoQualityDetectArithmeticApplyDeviceVO {

    private List<DeviceTreeNode> deviceTreeNodes;
    private List<String> appliedDevices;

    public List<DeviceTreeNode> getDeviceTreeNodes() {
        return deviceTreeNodes;
    }

    public void setDeviceTreeNodes(List<DeviceTreeNode> deviceTreeNodes) {
        this.deviceTreeNodes = deviceTreeNodes;
    }

    public List<String> getAppliedDevices() {
        return appliedDevices;
    }

    public void setAppliedDevices(List<String> appliedDevices) {
        this.appliedDevices = appliedDevices;
    }

    public static class DeviceTreeNode {
        private String code;
        private String name;
        private List<DeviceTreeNode> children;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<DeviceTreeNode> getChildren() {
            return children;
        }

        public void setChildren(List<DeviceTreeNode> children) {
            this.children = children;
        }
    }
}
