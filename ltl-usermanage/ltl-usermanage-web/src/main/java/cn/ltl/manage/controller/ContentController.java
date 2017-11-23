package cn.ltl.manage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.ltl.common.bean.EasyUIResult;
import cn.ltl.manage.pojo.Content;
import cn.ltl.manage.service.ContentService;

@RequestMapping("content")
@Controller
public class ContentController {

    @Autowired
    private ContentService contentService;

    /**
     * 新增内容
     * 
     * @param content
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> saveContent(Content content) {
        try {
            content.setId(null);
            this.contentService.save(content);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     *  根据内容分类id查询分类列表
     *  
     * @param categoryId
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<EasyUIResult> queryListByCategoryId(@RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "10") Integer rows) {
        try {
            EasyUIResult easyUIResult = this.contentService.queryListByCategoryId(categoryId, page, rows);
            return ResponseEntity.ok(easyUIResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

}
