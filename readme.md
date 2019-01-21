# 智能咨询项目学习概要

## 1.	大纲
- 1	自然语言处理
- 2	Lucene全文检索
- 3	词向量
- 4	语义相似度

## 2.	自然语言处理（HanLP）
项目中自然语言处理主要是采用的HanLp做的分词、同义词词典、用户自定义分词词典开发。
HanLP是一系列模型与算法组成的NLP工具包，由大快搜索主导并完全开源，目标是普及自然语言处理在生产环境中的应用。HanLP具备功能完善、性能高效、架构清晰、语料时新、可自定义的特点。
HanLP是一个GitHub项目，项目地址是：https://github.com/hankcs/HanLP。面的功能包括：中文分词、词性标注、命名实体识别、关键词提取、自动摘要、短语提取、拼音转换、简繁转换、文本推荐、依存句法分析、文本分类、文本聚类、词向量等相关功能。项目功能非常强大，编程语言是采用的java语言发开，不依赖任何三方库。

## 3.	Lucene全文检索
Lucene是apache软件基金会的子项目，Lucene的目的是为软件开发人员提供一个简单易用的工具包，以方便的在目标系统中实现全文检索的功能，或者是以此为基础建立起完整的全文检索引擎。下图是检索的流程：
 
### 3.1	索引的创建
``` java
//创建目录 directory
Directory directory = FSDirectory.open(FileSystems.getDefault().getPath(path));
//创建索引建立配置文件
IndexWriterConfig iwc = new IndexWriterConfig(new HanLPIndexAnalyzer());
//创建索引写入对象
IndexWriter writer = new IndexWriter(directory, iwc);
//索引写入
Document doc = new Document();
doc.add(new Field(key, value, TextField.TYPE_STORED));
writer.addDocument(doc);
//关闭索引用来保存索引的创建
writer.close();
directory.close();
3.2	根据索引检索
//打开目录 directory
Directory directory = FSDirectory.open(FileSystems.getDefault().getPath(path));
//创建读索引对象
DirectoryReader reader = DirectoryReader.open(directory);
//查询
Query query = new QueryParser(luceneConfig.getIndexKey(), new HanLPAnalyzer()).parse(question);
TopDocs result = searcher.search(query, 100);
for (ScoreDoc doc : result.scoreDocs) {
    Document document = searcher.doc(doc.doc);
}
```
## 4.	词向量
词向量工程的源代码地址是：http://10.19.32.73/zhangzhida/TrainWord.git，用来将税务、维基百科的数据进行训练把文本量化为可计算的向量。详细的词向量相关的内容请参照：https://blog.csdn.net/mawenqi0729/article/details/80698350
## 5.	语义相似度（余弦相似度分析、词林）
语义相似度计算采用余弦相似度计算的方法，针对专业性的知识库经验证，余弦相似度最适合，调用方式（SimilarityAnalyzeUnfamiliarWords）为： 
```` java
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
相似度计算实现原理：
 
相似度计算代码实现：
for (int i = 0; i < vec1.length; ++i) {
    dist += vec1[i] * vec2[i];
    sum1 += Math.pow(vec1[i], 2);
    sum2 += Math.pow(vec2[i], 2);
}
double result = dist / Math.sqrt(sum1 * sum2);
//在计算过程中，由于浮点运算的偏差问题，存在比较小的误差，
//为避免大于1这种情况，对后续计算过程中的英雄，暂时的将相似度控制到100%以内
return result > 1.0 ? 1.0D : result;
````
## 6.	项目工程介绍
### 6.1	项目构建工具
项目采用maven的项目管理工具管理，并且核心采用的SpringBoot微服务框架开发。
### 6.2	配置说明
#### 6.2.1 HanLp配置说明（hanlp.properties）：

 
	项目中只需要配置这个root就可以了，root参数是HanLp分词数据包的物理路径
本配置文件中的路径的根目录，根目录+其他路径=完整路径（支持相对路径，请参考：https://github.com/hankcs/HanLP/pull/254）
Windows用户请注意，路径分隔符统一使用/
root=D:/ideaProject/knowledge-core/
#### 6.2.2 Lucene、词向量等项目配置说明
``` yaml
db:
  mysql:
      driverClass: com.mysql.cj.jdbc.Driver#这个是mysql驱动配置不需要改动
      jdbcUrl: jdbc:mysql://10.111.29.21:3306/tax_knowledge?useUnicode=true&characterEncoding=gb2312 #mysql地址端口号配置
      user: root #mysql用户名和密码配置
      password: Abcd1234! #mysql用户名和密码配置
  mybatis:
      mybatisXml: /mybatis.xml #mybatis文件配置路径
lucene:
  root: /root/lucene/  #Lucene索引位置的根目录
  indexKey: questionWithSynonyms #这个是Lucene查询、建立索引的时候共享的一个key，这个key可以一直不改变
  vectorPath: /root/data/wiki_chinese_word2vec(Google)_200d.bin #词向量物理路径
```
### 6.3	包和类情况说明
- com.hankcs.lucene ---- hanlp中文分词、索引建立代码部分
- com.inspur.tax.configure	springboot配置文件映射类
- com.inspur.tax.core.dictionary 核心词典包
- com.inspur.tax.core.nlp.cosinesimlarity	余弦相似度计算相关包
- com.inspur.tax.data.jdbc	mysql数据库配置类
- com.inspur.tax.mvc	知识库接口提供类
- com.inspur.tax.scene	场景式相关类
- CloudApplication	springboot程序入口

## 7.	程序功能代码入口
所有的程序功能入口都在QAController类中
### 7.1	启动时加载项
说明：启动时请注意启动顺序，先加载词向量到内存中再启动其他词林，因为词林矫正词向量时需要取去词向量做矫正。所以向量加载必须在词林加载之前
```
//加载分词热词
myCustomDictionary.initDictionary();
//加载停用词词典
myCustomDictionary.initStopWords();
//加载词林同义词
myCustomDictionary.initCiLinSynonyms();
//加载全、简称词典将简称字段加入到分词热词中（全称不加入）
myCustomDictionary.initAbbreviation();
//将所有的向量加载
initWordVectors();
//检索所有维护的词林同义词词典，并将所有的税务同义词加入到热词中
initCilin();
//初始化分词服务
initSeg();
```
### 7.2	索引的创建
```
//初始化时创建索引
@RequestMapping("/createIndex")
public CheckResult createIndex() {
    questionsIndex.createIndex();
    return new CheckResult("000", "success", "");
}
```
### 7.3	词林词典重加载
因为知识同义词计算部分采用内存计算的方式，同义词词林的重新加载为了使用户自定义了同义词之后，进行同义词实时生效用的
/**
 * 重新加载词林同义词词典
 * 重新校准向量
 */
@RequestMapping("/reloadCiLin")
public CheckResult reloadCiLin() {
    myCustomDictionary.initCiLinSynonyms();
    initCilin();
    return new CheckResult("000", "success", "");
}

### 7.4	全简称词典重加载
全简称重新加载为了，后台管理系统在维护了全简称词语时来进行发布实时生效用的
```
// 重新加载全简称词典
@RequestMapping("/reloadAbbreviation")
public CheckResult reloadAbbreviation() {
    myCustomDictionary.initAbbreviation();
    return new CheckResult("000", "success", "");
}
```
