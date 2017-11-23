package cn.ltl.web.bean;

import org.apache.commons.lang3.StringUtils;

public class Item extends cn.ltl.manage.pojo.Item {

    public String[] getImages() {
        return StringUtils.split(super.getImage(), ',');
    }

}
