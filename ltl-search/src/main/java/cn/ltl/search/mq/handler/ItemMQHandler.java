package cn.ltl.search.mq.handler;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn.ltl.search.pojo.Item;
import cn.ltl.search.service.ItemService;

public class ItemMQHandler {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private HttpSolrServer httpSolrServer;

    @Autowired
    private ItemService itemService;

    /**
     * 处理消息，新增、修改、删除的消息，将商品数据同步到solr中
     * 消息中并没有包含商品的基本数据，需要通过id到后台系统提供的接口中获取
     * 
     * @param msg
     */
    public void execute(String msg) {
        try {
            JsonNode jsonNode = MAPPER.readTree(msg);
            Long itemId = jsonNode.get("itemId").asLong();
            String type = jsonNode.get("type").asText();
            if (StringUtils.equals(type, "insert") || StringUtils.equals(type, "update")) {
                Item item = this.itemService.queryById(itemId);
                this.httpSolrServer.addBean(item);
                this.httpSolrServer.commit();
            } else if (StringUtils.equals(type, "delete")) {
                this.httpSolrServer.deleteById(String.valueOf(itemId));
                this.httpSolrServer.commit();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
