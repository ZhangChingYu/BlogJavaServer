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
           * controllers `// 控制器`
             * SearchController.java `// 處理查詢請求的控制器`
             * ...
           * services `// 業務邏輯執行`
             * SearchService.java `// 實現查詢服務的類`
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
主要是對MySQL數據表進行增刪改查的操作，首先需要引入MySQL提供的驅動API: JDBC，接著實現數據庫 **MySQL** 的連接方法，我們將建立連接與數據庫的業務邏輯區分開，由 **ConnectionManager** 類負責與數據庫連接。

    // build mysql connection
    public static Connection getConnection() throws SQLException {
        if(connection == null || connection.isClosed()){
            String database = "blog";
            String jdbcUrl = "jdbc:mysql://localhost:3306/"+database;
            String username = "root";
            String password = "Sunny.1218";
            connection = DriverManager.getConnection(jdbcUrl, username, password);
        }
        return connection;
    }
    // close database connection manually
    public static void closeConnection() throws SQLException {
        if(connection != null || !connection.isClosed()){
            connection.close();
        }
    }

接著為數據庫的數據編寫對應的實體類並統一放置在 **/models/** 文件夾下方便管理。

    public class Category{
        private int id;
        private String name;
        ...
    }

有了對應的實體類後根據項目的需求實現像硬的數據操作方法，例如簡單的增刪查改，數據庫操作的方法實現統一放置在 **/dao/** 文件夾下管理。

    public class CategoryDao {
        private final Connection connection;
        public CategoryDao(Connection connection){
            this.connection = connection;
        }
        // 數據插入
        public void insertCategory(String name){...}

        // 數據查詢
        public List<Category> getAllCategory(){
            List<Category> categories = new ArrayList<>();
            ...
            return  categories;
        }

        // 數據更改
        public void updateCategory(int id, String newName){...}
        
        // 數據刪除
        public void deleteCategory(int id){...}
    }
### 2. 業務邏輯實現
在傳統的三層架構（MVC 架構）中，業務邏輯通常由控制器（Controller）來管理和實現。控制器負責接收來自用戶界面的請求，處理相應的業務邏輯，然後根據結果生成響應返回給用戶。

***
## 後端接口
