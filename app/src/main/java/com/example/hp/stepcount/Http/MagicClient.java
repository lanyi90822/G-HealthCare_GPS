package com.example.hp.stepcount.Http;


/**
 * Magic 客户端
 * @author
 *
 */
public interface MagicClient {

//    public static String VERIFICATION_METHOD_SMS = "sms";//短信方式获取验证码
//    public static String VERIFICATION_METHOD_MAIL = "mail";//邮件方式获取验证码
//
//    /**
//     * 注册账号
//     * @param account
//     * @param password
//     * @param verification 验证码
//     * @param callback
//     * onFailure result 1:账号已存在, 2:账号为空, 3:密码为空
//     */
//    public void register(String account, String password, String verification, final MagicCallback<Member> callback);
//
//    /**
//     * 使用微信账号登录
//     * @param accessToken
//     * @param refreshToken
//     * @param weiXinOpenid
//     * @param callback
//     * onFailure result 1: WeiXin刷新时返回错误信息,2: 查找没用的用户名次数过多，请重试,3: refresh_token过期
//     */
//    public void loginWeixin(String accessToken, String refreshToken, String weiXinOpenid, final MagicCallback<Member> callback);
//
//    /**
//     * 使用本系统账号登录
//     * @param account
//     * @param pwd
//     * @param callback
//     * onFailure result 1:没有该用户, 2:密码不正确
//     */
//    public void login(String account, String pwd, final MagicCallback<Member> callback);
//
//    /**
//     * 登出系统
//     * @param callback
//     */
//    public void logout(final MagicCallback<JSONObject> callback);
//
//    /**
//     * 使用本系统账号登录
//     * @param account
//     * @param pwd
//     * @param verification Code 验证码
//     * @param callback
//     * onFailure result 1:没有该用户, 2:密码不正确
//     */
//    public void login(String account, String pwd, String verification, final MagicCallback<Member> callback);
//
//    /**
//     * 获得验证码
//     * @param method  "sms";//短信方式获取验证码, "mail";//邮件方式获取验证码
//     * @param account 手机号或电子邮箱地址
//     */
//    public void getVerification(String method, String account, final MagicCallback<JSONObject> callback);
//
//    /**
//     * 短信方式获取验证码
//     * 开发时，默认返回1234
//     * @param account 手机号
//     */
//    public void getVerification(String account, final MagicCallback<JSONObject> callback);
//
//    /**
//     * 获取用户本人信息
//     * @param callback
//     * onFailure result 1:没有该用户,
//     */
//    public void getMemberInfo(final MagicCallback<Member> callback);
//
//    /**
//     * 获取用户本人信息
//     * @return
//     * null没有该用户
//     */
//    public Member getMemberInfo();
//
//    /**
//     * 更新用户本人信息
//     * @param member
//     * @param callback
//     */
//    public void updateMember(Member member, final MagicCallback<Member> callback);
//
//    /**
//     * 修改密码
//     * @param oldPassword
//     * @param newPassword
//     * @param callback
//     */
//    public void updatePassword(String oldPassword, String newPassword, final MagicCallback<JSONObject> callback);
//
//    /**
//     * 更换手机号
//     */
//    public void updateTel(final String verificationCode, final String newTel, final MagicCallback<Member> callback);
//
//
//    /**
//     * 根据验证码修改密码
//     */
//    public void updatePasswordByVerificationCode(final String tel, final String newPassword, final String verificationCode, final MagicCallback<Member> callback);
//    /**
//     * 找回密码
//     * @param method 1:通过短信, 2:通过邮件
//     * @param callback
//     */
//    public void findBackPassword(String method, String emailOrTel, final MagicCallback<JSONObject> callback);
//
//    /**
//     * 保存测量数据
//     * @param measure
//     * @param callback
//     */
//    public void saveMeasure(Measure measure, final MagicCallback<Measure> callback);
//
//    /**
//     * 下载测量数据
//     *
//     * 如果begin > 0 下载begin之前的测量数据,不包括begin.
//     * 如果begin <=0 and end > 0 下载end之后的测量数据,不包括end.
//     * 如果begin <= 0 and end <= 0 下载最近的数据.
//     * @param begin 测量数据id
//     * @param end   测量数据id
//     * @param pageSize 每次下载记录条数
//     * @param callback
//     * onSuccess(int result, String msg, List<Measure> measureList);
//     */
//    public void listMeasure(int begin, int end, int pageSize, final MagicCallback<List<Measure>> callback);
//
//    /**
//     * 下载心电数据文件
//     * @param rsId 测量记录id
//     * @param destFilePath 下载的文件存放路径
//     * @param callback
//     */
//    public void downloadWaveFile(int rsId, final String destFilePath, final MagicCallback<File> callback);
//
//    /**
//     * 下载心电数据文件
//     * @param rsId 测量记录id
//     * @param destFilePath 下载的文件存放路径
//     * @return
//     */
//    public File downloadWaveFile(int rsId, String destFilePath);
//
//    /**
//     * 检测手机号是否已注册
//     * @param avatar
//     * @param callback
//     */
//    public void isTelNumAvailable(String number, final MagicCallback<JSONObject> callback);
//
//    /**
//     * 上传头像
//     * @param avatar
//     * @param callback
//     */
//    public void uploadAvatar(File avatar, final MagicCallback<String> callback);
//
//    /**
//     * 下载头像
//     * @param destFilePath
//     * @param callback
//     */
//    public void downloadAvatarFile(final String destFilePath, final MagicCallback<File> callback);
//
//
//    /**
//     * 下载头像
//     * @param destFilePath 下载的文件存放路径
//     * @return
//     */
//    public File downloadAvatarFile(String destFilePath);
//
//    /**
//     * 反馈信息
//     */
//    public void saveFeedback(String feedback, String name, int type, final MagicCallback<Feedback> callback);
//
//    /**
//     * 获取最新版本信息
//     * @param callback
//     */
//    public void getAppUpdateInfo(final MagicCallback<JSONObject> callback);
//
//    /**
//     * 获取最新版本信息
//     * @return
//     */
//    public JSONObject getAppUpdateInfo();
//    /**
//     * 版本号
//     * @return
//     */
//    public String getVersion();
//
//    /**
//     * 版本时间
//     * @return
//     */
//    public String getVersionDate();
//
//    /**
//     * 设置环境回调接口
//     * @param callback
//     */
//    public void setEvnCallback(EvnCallback callback);
//
//    /**
//     * 设置服务器地址
//     * 格式: http://127.0.0.1:8080/
//     * @param host
//     */
//    public void setHost(String host);
//
//    /**
//     * 设备日志记录器
//     * @param log
//     */
//    public void setLog(Log log);
//
    /**
     * 增加任务
     * @param task
     */
    public void addTask(Task task);

    /**
     * 设置sessionId
     * @param sessionId
     */
    public void setSessionID(String sessionId);

    /**
     * 获取sessionid
     * @return
     */
    public String getSessionID();

//    /**
//     * 获得登录任务,如果本地没有保存登录信息返回null
//     *
//     * @return
//     */
//    public LoginTask getLoginTask();
//
//    /**
//     * 获取最后一次保存的校准数据
//     * @param callback
//     */
//    public void getLastCalibration(final MagicCallback<Calibration> callback);
//
//    /**
//     * 保存校准
//     * @param calibration
//     * @param callback
//     */
//    public void saveCalibration(Calibration calibration, final MagicCallback<Calibration> callback);
//
//    /**
//     * 自动登录
//     * @param callback
//     */
//    public void loginAuto(final MagicCallback<Member> callback);
}
