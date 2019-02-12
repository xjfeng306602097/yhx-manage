package com.yhx.common.controller;

import com.github.pagehelper.PageHelper;
import com.yhx.common.dto.BaseResponse;
import com.yhx.common.dto.PageResponse;
import com.yhx.common.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**  
* <p>Title: BaseController</p>  
* <p>Description: Controller的基类，处理异常，公共方法 </p>  
* @author xiaojunfeng
* @date 2018年8月9日  
*/  
public abstract class BaseController<E extends BaseService<T>,T> {
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	protected  E service;

	/**
	 * 通用列表查询
	 * @param selectOption 查询参数
	 * @param pageSize 页大小
	 * @param pageNum  页码
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/list",method = RequestMethod.GET )
	protected BaseResponse<List<T>> list(HttpServletRequest request, T selectOption,
										 @RequestParam(value = "pageSize",required = false) Integer pageSize,
										 @RequestParam(value = "pageNum",required = false) Integer pageNum){
		if(pageNum != null && pageSize != null){
			PageHelper.startPage(pageNum, pageSize);
		}
		List<T> result = service.selectList(selectOption);
		return PageResponse.successPage(result);
	}

	/**
	 * 通用更新操作
	 * 注意body中需要包含主键
	 * @param entity
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/{id}",method = RequestMethod.PUT)
	public BaseResponse<?> modify(HttpServletRequest request,@RequestBody T entity,@PathVariable("id")String id){
		service.updateById(entity);
		return BaseResponse.success();
	}

	/**
	 * 通用查询明细操作
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/{id}",method = RequestMethod.GET)
	public BaseResponse<T> cat(HttpServletRequest request,@PathVariable("id") String id){
		T t = service.selectById(id);
		return BaseResponse.success(t);
	}


	/**
	 * 通用删除操作
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/{id}",method = RequestMethod.DELETE)
	public BaseResponse<?> del(HttpServletRequest request,@PathVariable String id){
		service.deleteById(id);
		return BaseResponse.success();
	}
}