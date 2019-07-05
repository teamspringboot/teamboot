package com.advert;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableAutoConfiguration
//@ComponentScan({"com.advert"}) //如果用登陆请使用这个
@ComponentScan({ "com.advert.controller", "com.advert.service","com.advert.pageing"})
@MapperScan("com.advert.mapper")
//@EnableScheduling	//启用定时器使用这个注解
public class Application{
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
/**
 * com.github.pagehelper.Page
 * PageInfo{pageNum=20, pageSize=100, size=100, startRow=1901, endRow=2000, total=90000, pages=900,
 *  list=Page{count=true, pageNum=20, pageSize=100, startRow=1900, endRow=2000, total=90000, pages=900, reasonable=false, pageSizeZero=false} 
 *  prePage=19, nextPage=21, isFirstPage=false, isLastPage=false, hasPreviousPage=true, hasNextPage=true, navigatePages=8, navigateFirstPage=16, 
 *  navigateLastPage=23, navigatepageNums=[16, 17, 18, 19, 20, 21, 22, 23]}
 * pageNum 			第几页
 * pageSize 		每页需要条数
 * size				本页条数
 * startRow			开始条数
 * endRow			结束条数
 * total			总条数
 * pages			总页数
 * prePage			上一页
 * nextPage			下一页
 * isFirstPage		是否是第一页
 * isLastPage		是否是最后一页
 * hasPreviousPage	是否有上一页
 * hasNextPage		是否有下一页
 */
