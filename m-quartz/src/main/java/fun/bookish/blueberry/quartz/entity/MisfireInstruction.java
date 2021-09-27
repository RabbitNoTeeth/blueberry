package fun.bookish.blueberry.quartz.entity;

import java.io.Serializable;

/**
 * 补偿策略实体类
 *
 * @author Don9
 */
public class MisfireInstruction implements Serializable {
    private static final long serialVersionUID = 5320773166527619894L;

    private String name;

    private Integer value;

    public MisfireInstruction() {
    }

    public MisfireInstruction(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
