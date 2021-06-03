# ZJUHealthNavigation-Article
浙大医疗导航APP-医疗资讯模块

A2组的医疗资讯模块代码就放在这里啦，方便大家协作和版本控制～

友情提示：用Android Studio打开此工程，选择Open Existing Project，选择此仓库里的navigationTest文件夹

## 进度

Kim:

- 2021-05-20
- 实现资讯界面的资讯列表

- 2021-05-21
- 实现资讯点击跳转和资讯详情页

- ~~计划：2021-5-22完善后端，2021-5-23实现前后端连接~~
- 完善失败，整不透mongoose这玩意儿
- 参考文档：

node.js操作数据库之MongoDB+mongoose篇 https://www.cnblogs.com/champyin/p/11666892.html  
Android 连接MongoDB与基本操作 https://blog.csdn.net/u012131769/article/details/40586153  
Mongoose中文文档 http://mongoosejs.net/docs/index.html  

目前我已经学习到的：
- 先下载node.js，再下载mongodb，最后用IntelliJ IDEA Ultimate/别的什么软件打开Backend项目，在项目内用 `npm install mongoose` 下载mongoose
- Mac下载mongodb最后一步新建/data/db时会提示无权限，请参考这个文档 https://blog.csdn.net/cq20110310/article/details/114929072  
- 理论上IntelliJ IDEA Ultimate会在"mongoose"字段下出现提示要求安装mongoose，点按即可安装，不需要自己打开命令行输入  
--------------
- 2021-05-28
- 实现（伪）点赞、收藏、分享、评论功能
- 2021-06-01
- 资讯界面添加“发布资讯”按钮，发布资讯页面实现添加本地图片
- 2021-06-03
- 实现真正的获取并展示资讯列表、选中资讯跳转到真正的资讯详情页
