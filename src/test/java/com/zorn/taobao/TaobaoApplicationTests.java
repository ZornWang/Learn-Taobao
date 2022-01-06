package com.zorn.taobao;

import com.zorn.taobao.mapper.IndexMapper;
import com.zorn.taobao.pojo.Goods;
import com.zorn.taobao.pojo.state.RedisHeader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class TaobaoApplicationTests {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private IndexMapper indexMapper;

    @Test
    void contextLoads() {
    }

//    @Test
//    void testjson() {
//        User user = new User(1, "wang", "zhun", "13102058715");
//        System.out.println(JSON.toJSONString(user));
//    }

    @Test
    void testCos() {
        System.out.println(stringRedisTemplate.opsForValue().get(RedisHeader.USER_TOKEN.getHeader() + "13102058715"));
    }

//    @Test
//    void testJwt() {
//        User user = new User(1, "zorn", "wang", "13102058715");
//        String token = jwtService.getToken(user);
//        System.out.println(token);
//        User jwtServiceUser = jwtService.getUser(token);
//        System.out.println(jwtServiceUser);
//    }

//    @Test
//    @Transactional
//    void testGoodsMapper() {
//        Goods goods = new Goods("123", "Mac Pro", "电脑", 20000.00, "Mac");
//        goodsMapper.addGoods(goods);
//        System.out.println(goods.getId());
//        goodsMapper.addUserGoods(2, goods.getId(), 1);
//        User zorn = userMapper.getUserByName("zorn");
//        User userGoods = goodsMapper.userGoods(zorn);
//        System.out.println(userGoods);
//    }

    @Test
    void testSearch() {
        String name = "Mac";
        Goods[] goods = indexMapper.searchGoodsByName(name);
        for (Goods good : goods) {
            System.out.println(good);
        }
    }


}
