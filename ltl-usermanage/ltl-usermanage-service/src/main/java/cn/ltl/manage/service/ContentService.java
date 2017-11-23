package cn.ltl.manage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cn.ltl.common.bean.EasyUIResult;
import cn.ltl.manage.mapper.ContentMapper;
import cn.ltl.manage.pojo.Content;

@Service
public class ContentService extends BaseService<Content> {

    @Autowired
    private ContentMapper contentMapper;

    public EasyUIResult queryListByCategoryId(Long categoryId, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        List<Content> list = this.contentMapper.queryContentList(categoryId);
        PageInfo<Content> pageInfo = new PageInfo<Content>(list);
        return new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
    }

}
