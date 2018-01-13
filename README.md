# pq-MillionHero
百万英雄答题

## 1、使用
因为我用的苹果手机，也不会使用adb，所以用了一个简单粗暴的方法。（截图分析）

在电脑上（Mac、Windows都行）装一个安卓模拟器，在模拟器里面装上西瓜视频。

在模拟器里面出现问题，使用快速截图（因为我使用的mac，使用的系统截图可以快速截屏到桌面），

打开代码，修改截图保存的地址（Run.java DEFAULT_IMAGE）运行Run即可。

## 2、原理说明
* 1、启动程序
* 2、使用截图工具截取问题和答案部分的图片
* 3、使用百度ocr进行文字识别
* 4、将文字的结果提取出来
* 5、使用百度搜索并统计搜索得到结果数量：问题+各个答案count(q&a)、问题 count(q)、答案 count(a)
* 6、计算匹配值pmi: pmi[i]=count(q&a[i])/(count(q)*count(a[i]))
* 7、选择pmi值最高的为答案（该公式来自：[https://en.wikipedia.org/wiki/Pointwise_mutual_information](https://en.wikipedia.org/wiki/Pointwise_mutual_information)）

> 注：因算法识别率目前只有70%，还在进一步研究算法