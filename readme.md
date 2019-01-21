# 咨询知识库系统概要
感谢HanLp、Luncene等开源系统给我们开发系统带来的便捷，也诚挚的邀请各位大神参与完善这个项目来供更多的人学习使用
##大纲
- 	安装与部署
- 	Lucene全文检索
- 	词向量
-	语义相似度
##安装部署
####语料数据准备：
百度网盘下载地址：
####数据库初始化
数据库采用mysql进行静态的数据存储，请在网盘中一并下载
```
init.sql
```
####项目构建工具
项目采用maven的项目管理工具管理，并且核心采用的SpringBoot微服务框架开发。此处建议使用idea工具进行编辑开发。
####配置说明
####HanLp配置说明（hanlp.properties）：
项目中只需要配置这个root就可以了，root参数是HanLp分词数据包的物理路径
``` properties
root=/root/
```
####Lucene、词向量项目配置说明
application.yml
``` yaml
lucene:
    root: /root/lucene/  #Lucene索引位置的根目录
    indexKey: questionWithSynonyms #这个是Lucene查询、建立索引的时候共享的一个key，这个key可以一直不改变
    vectorPath: /root/data/wiki_chinese_word2vec.bin #词向量物理路径
```
####mysql数据库配置
```yaml
db:
  mysql:
      driverClass: com.mysql.cj.jdbc.Driver#这个是mysql驱动配置不需要改动
      jdbcUrl: jdbc:mysql://IP:3306/tax_knowledge?useUnicode=true&characterEncoding=gb2312 #mysql地址端口号配置
      user: username #mysql用户名和密码配置
      password: password #mysql用户名和密码配置
```
####项目编译
执行maven命令，如果不懂maven的童鞋，请恶补一下基础知识，哈哈
```
mvn clean install 
```
####启动应用
springboot项目启动只需要启动编译好的编译包就可以了，不懂springboot的童鞋要使劲学习啦。
```
java -jar knowledge.war
```
####创建索引库
通过浏览器访问创建索引的接口：
```
http://ip:8080/createIndex
```
####测试结果
浏览器访问
```
http://ip:8080/getAnswer?question=收不到验证码
```
## 	语义相似度（余弦相似度分析、词林）
语义相似度计算采用余弦相似度计算的方法，针对专业性的知识库经验证，余弦相似度最适合，调用方式（SimilarityAnalyzeUnfamiliarWords）为： 
``` java
double score = similarAnalyze.sentenceSimilarity(seg_question, seg_question2);
//计算第一句话得每一个词和另一句话中最相似的词的相似度
for (Term sentence1Word : sentence1Words) {
    ++count1;
    sum1 += this.calMaxSimilarity(sentence1Word.word, sentence2Words);
}
//计算第二句话得每一个词和另一句话中最相似的词的相似度
for (Term sentence2Word : sentence2Words) {
    ++count2;
    sum2 += this.calMaxSimilarity(sentence2Word.word, sentence1Words);
}
//检测数量是不是为0是为了避免计算过程中产生NAN导致报错
if (count1 == 0) {
    if (count2 == 0) {
        return 0F;
    } else {
        return sum2 / count2;
    }
} else if (count2 == 0) {
    return sum1 / count1;
}
//去相似度最小的那个，能够避免长短文本比较而产生文本包含关系的问题
return Math.min(sum1 / (count1), sum2 / count2);
```
相似度计算实现原理：

相似度计算代码实现：
```
for (int i = 0; i < vec1.length; ++i) {
    dist += vec1[i] * vec2[i];
    sum1 += Math.pow(vec1[i], 2);
    sum2 += Math.pow(vec2[i], 2);
}
double result = dist / Math.sqrt(sum1 * sum2);
//在计算过程中，由于浮点运算的偏差问题，存在比较小的误差，
//为避免大于1这种情况，对后续计算过程中的英雄，暂时的将相似度控制到100%以内
return result > 1.0 ? 1.0D : result;
```
##	程序功能代码入口
所有的程序功能入口都在QAController类中
