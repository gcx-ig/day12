package com.xiaoshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.GoodsMapper;
import com.xiaoshu.dao.GoodstypeMapper;
import com.xiaoshu.dao.UserMapper;
import com.xiaoshu.entity.Goods;
import com.xiaoshu.entity.GoodsVo;
import com.xiaoshu.entity.Goodstype;
import com.xiaoshu.entity.User;
import com.xiaoshu.entity.UserExample;
import com.xiaoshu.entity.UserExample.Criteria;

import redis.clients.jedis.Jedis;

@Service
public class GoodsService {

	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private GoodstypeMapper goodstypeMapper;

	public PageInfo<GoodsVo> findPage(GoodsVo goodsVo,Integer pageNum,Integer pageSize){
		PageHelper.startPage(pageNum, pageSize);
		 List<GoodsVo> list =goodsMapper.findAll(goodsVo);
		return new PageInfo<>(list);
	}
	public List<Goodstype> findAllT(){
		return goodstypeMapper.selectAll();
	}
	
	public void updateG(Goods goods){
		goodsMapper.updateByPrimaryKeySelective(goods);
	}
	public void addG(Goods goods){
		goodsMapper.insert(goods);
	}
	public void addT(Goodstype goodstype){
		goodstypeMapper.insert(goodstype);
		Jedis jedis = new Jedis("127.0.0.1",6379);
		Goodstype param = new Goodstype();
		param.setTypename(goodstype.getTypename());
		Goodstype goodstype2 = goodstypeMapper.selectOne(param );
		jedis.hset("分类信息", goodstype2.getId()+"", goodstype2.toString());
	}
	public Goods findByName(String code){
		Goods param = new Goods();
		param.setCode(code);
		return goodsMapper.selectOne(param);
	}
}
