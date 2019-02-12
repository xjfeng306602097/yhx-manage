package com.yhx.common.constant;


/**  
* <p>Title: ResultCode</p>  
* <p>Description: 返回的错误代码</p>  
* @author xiaojunfeng
* @date 2019年1月31日
*/  
public class ErrorCode {

    /** 成功 */
    public static final int SUCCESS = 0;

    /** 失败 */
    public static final int FAILURE = -1;

    /** 参数错误 */
    public static final int PARAMETER_ERROR = 1;

    /** 数据库错误 */
    public static final int DB_ERROR = 2;

    /** 未登录 */
    public static final int NOT_LOGIN = 3;

    /** 用户不存在 **/
    public static final int USER_NOT_EXIST = 4;

    /** 无权限访问**/
    public static final int NOT_ACCESS = 5;

    /** 内部服务器错误 */
    public static final int INTERNAL_SERVER_ERROR = 500;
    
    /** 没有此纪录 */
    public static final int NOT_RECORD = 6;
  
}
