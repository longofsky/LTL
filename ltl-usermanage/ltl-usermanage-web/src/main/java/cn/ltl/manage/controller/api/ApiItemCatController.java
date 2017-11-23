package cn.ltl.manage.controller.api;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.util.StringUtil;

import cn.ltl.common.bean.ItemCatResult;
import cn.ltl.common.service.RedisService;
import cn.ltl.manage.service.ItemCatService;

@RequestMapping("api/item/cat")
@Controller
public class ApiItemCatController {

	@Autowired
	private ItemCatService itemCatService;
	@Autowired
	private RedisService redisService;

	private static final ObjectMapper MAPPER = new ObjectMapper();

	/**
	 * 对外提供接口服务，查询所有类目数据
	 * 
	 * @return
	 */
	// @RequestMapping(method = RequestMethod.GET)
	// public ResponseEntity<String> queryItemCatList(
	// @RequestParam(value = "callback", required = false) String callback) {
	// try {
	// ItemCatResult itemCatResult = this.itemCatService.queryAllToTree();
	// String json = MAPPER.writeValueAsString(itemCatResult);
	// if (StringUtils.isEmpty(callback)) {
	// return ResponseEntity.ok(json);
	// }
	// return ResponseEntity.ok(callback + "(" + json + ");");
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return
	// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	// }

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity queryItemCatList() {

		// 从缓存中命中
		String key = "TAOTAO_MANAGE_ITEM_CAT_ALL";// 定义key 的逻辑 项目名_模块名_业务名
		String cacheData = redisService.get(key);
		try {
			//先从 缓存中命中，如果命中的话 返回 没有命中，继续执行
			if (StringUtil.isNotEmpty(cacheData)) {

				return ResponseEntity.ok(MAPPER.readValue(cacheData,
						ItemCatResult.class));

			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		try {
			ItemCatResult itemCatResult = this.itemCatService.queryAllToTree();
			// redisService.set(key,
			// MAPPER.writeValueAsString(itemCatResult),60*60*24*30*3);//事务不同步
			return ResponseEntity.ok(itemCatResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
				null);
	}

}
