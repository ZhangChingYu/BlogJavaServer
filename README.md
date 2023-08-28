# BlogJavaServer
本文件是 **BlogWebsite** 的 **Java** 後端服務器，使用 **Maven** 來管理依賴包和整個項目的生命週期，基於 **RESTFul API** 實現網頁服務器的一些基本功能。整個後端大致以MVC的邏輯配置代碼文件，使用的數據庫是 **MySQL** 。
***
### 具體功能如下：
1. 處理前端請求
2. 處理業務邏輯
3. 數據庫操作
4. 網頁日誌編寫
5. 文件讀寫
***
### 文件目錄：
* BlogWebsiteServer
   * src
     * main
       * java
         * com.silvia.blogwebsite
           * Main.java`// 服務器入口點`
           * handlers`// 請求處理器`
             * RootHandler.java
             * ApiHandler.java
             * ...
           * dao`// 數據庫訪問對象`
             * UserDao.java
             * ...
           * models`// 數據模型類`
             * User.java
             * ...
           * utils`// 工具類`
             * LoggerUtil.java`// 日誌工具`
             * FileUtil.java`// 文件讀寫工具`
             * ...
           * AppConstants.java`// 常量定義`
         * resource
           * config.properties`// 配置文件`
     * test
       * java
   * pom.xml
   * README.md
   * media`// 靜態資源`
     * index.html
     * images
       * ...
     * videos
       * ...
***
## 實現流程：
### 1. 數據庫操作
主要是對MySQL數據表進行增刪改查的操作，首先需要引入MySQL提供的驅動API: JDBC，

***
## 後端接口
