# CainiaoAlliance

APP应用程序的主要功能包括商品管理、重量数据接收和预处理、交易记录管理、设置、生成报表，如下图所示：
![app简介](https://github.com/g-curry/CainiaoAlliance/blob/master/app/ReadmeImage/app%E6%B5%81%E7%A8%8B.png)

APP应用程序的主要功能包括商品管理、重量数据接收和预处理、交易记录管理、生成报表。

### 1.首页功能介绍
打开APP进入首页，显示当前日期，若今日没有数据则会提示今天没有订单，往左边滑动显示按照日期分类的历史订单，同时显示每日销售金额总价，点击总价可以查看到可视化界面（饼状图）显示出了当天的交易的种类，每一个种类的占比等信息；
在系统的listview上面展示了每一笔订单的详情，如订单号、VIP号、品名、单价、重量、总价、是否有打折（如果有打折则将原价格划去，并用红色显示出打折后的价格）、具体的交易时间等等，如果没有VIP号的顾客会显示非会员，同时价格只会显示出原价。具体界面信息如图一所示：
 
![image首页](https://github.com/g-curry/CainiaoAlliance/blob/master/app/ReadmeImage/%E9%A6%96%E9%A1%B5.png)
![image首页](https://github.com/g-curry/CainiaoAlliance/blob/master/app/ReadmeImage/%E9%A6%96%E9%A1%B52.png)
  
  
### 2.下载上传界面
![image下载上传](https://github.com/g-curry/CainiaoAlliance/blob/master/app/ReadmeImage/%E4%B8%8B%E8%BD%BD%E4%B8%8A%E4%BC%A0%E7%95%8C%E9%9D%A2.png)

此界面进行数据的交互，输入电子称的IP地址和端口号（当电子称的数量变多时后期会在数据库进行添加，设置称账号密码的形式进行登陆）进行连接，连接上电子称时连接按钮会变成断开并且在此界面进行Toast弹窗进行提醒是否连接成功，连接失败会提示重新连接。此界面更新价格可以将手机Sqlite数据库的所有设置的价格信息全部传递给电子称（设置界面如图三所示），同时，在称端的数据库中设置了主键，只有库中不存在此数据时才会进行添加操作。修改价格可以通过旁边的设置框进行设置。更新数据可以将每天的销售信息进行更新（更新时会在数据库中进行搜索，搜索出手机端数据库中没有的数据进行添加，避免重复添加），得到数据之后手机端就可以进行分析处理，得到需要的报表形式，让卖家更加容易的看到自己所出售的商品的种类，每天销售的金额的变化等。
  
### 3.价格设置修改界面

![image价格修改](https://github.com/g-curry/CainiaoAlliance/blob/master/app/ReadmeImage/%E4%BB%B7%E6%A0%BC%E8%AE%BE%E7%BD%AE%E7%95%8C%E9%9D%A2.png)

价格设置界面，商家可以在此界面进行添加，输入想要添加的商品名称和价格，点击加号即可添加，同时，
需要修改时点击书写按钮即可对数据库中已经拥有的价格进行修改，商家同样可以添加称端没有的商品，添加完成后发送给称端即可。

### 4.可视化界面
手机端提供了按照日期的总价可视化，同时为了适应不同商家的需求，在设计时添加了柱状图和折线图两种样式，商家可以根据不同的需求进行查看。

![image柱状图](https://github.com/g-curry/CainiaoAlliance/blob/master/app/ReadmeImage/%E6%8A%98%E7%BA%BF%E5%9B%BE.png)
![image折线图](https://github.com/g-curry/CainiaoAlliance/blob/master/app/ReadmeImage/%E6%9F%B1%E7%8A%B6%E5%9B%BE.png)
![image饼状图](https://github.com/g-curry/CainiaoAlliance/blob/master/app/ReadmeImage/%E9%A5%BC%E7%8A%B6%E5%9B%BE.png)
