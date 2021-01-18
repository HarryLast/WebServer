/*
通用JS库
 */

/**
 * AJAX返回状态
 * 与服务器R对象中的定义相同
 */
/** 200 OK - [GET]：服务器成功返回用户请求的数据 */
let OK = 200;

/** 201 CREATED - [POST/PUT/PATCH]：用户新建或修改数据成功。 */
let CREATED = 201;

/** 202 Accepted - [*]：表示一个请求已经进入后台排队（异步任务） */
let ACCEPTED = 202;

/** 204 NO CONTENT - [DELETE]：用户删除数据成功。 */
let NO_CONTENT = 204;

/** 400 INVALID REQUEST - [POST/PUT/PATCH]：用户发出的请求有错误，服务器没有进行新建或修改数据的操作，该操作是幂等的。*/
let INVALID_REQUEST = 400;

/** 401 Unauthorized - [*]：表示用户没有权限（令牌、用户名、密码错误）。 */
let UNAUTHORIZED = 401;

/** 403 Forbidden - [*] 表示用户得到授权（与401错误相对），但是访问是被禁止的。*/
let FORBIDDEN = 403;

/** 404 NOT FOUND - [*]：用户发出的请求针对的是不存在的记录，服务器没有进行操作，该操作是幂等的。 */
let NOT_FOUND = 404;

/** 410 Gone -[GET]：用户请求的资源被永久删除，且不会再得到的。*/
let GONE = 410;

/** 422 Unprocesable entity - [POST/PUT/PATCH] 当创建一个对象时，发生一个验证错误。 */
let UNPROCESABLE_ENTITY = 422;

/** 500 INTERNAL SERVER ERROR - [*]：服务器发生错误，用户将无法判断发出的请求是否成功。 */
let INTERNAL_SERVER_ERROR = 500;