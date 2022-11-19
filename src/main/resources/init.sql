create table if not exists func_class
(
    id bigint auto_increment comment '主键'
        primary key,
    name varchar(128) null comment '问题',
    lastUpdateTime timestamp null comment '修改时间'
)
    comment '分类表';

create table if not exists func_comment
(
    id bigint auto_increment comment '主键'
        primary key,
    contextId varchar(32) not null comment '内容编号',
    userId bigint not null comment '评论用户编号',
    upUserId bigint null comment '上级评论用户编号',
    content text not null comment '内容',
    lastUpdateTime timestamp null comment '修改时间'
)
    comment '评论表';

create table if not exists func_topic
(
    id varchar(32) not null comment '主键'
        primary key,
    problem text null comment '问题',
    summary varchar(128) null,
    answer text null comment '答案',
    classId bigint not null,
    lastUpdateTime timestamp null comment '修改时间'
)
    comment '问题表';
CREATE FULLTEXT INDEX func_topic__full_text_problem_answer ON func_topic (problem, answer) WITH PARSER ngram;
create table if not exists sys_user
(
    id bigint auto_increment comment '主键'
        primary key,
    nickName varchar(128) null comment '用户昵称',
    email varchar(32) null comment '用户邮箱',
    phone varchar(16) null comment '手机号码',
    openId varchar(128) null comment '微信编号',
    sex varchar(8) null comment '用户性别',
    avatar varchar(128) null comment '用户头像',
    isAdmin bigint default 0 null,
    lastUpdateTime timestamp null comment '修改时间'
)
    comment '用户表';
INSERT INTO fgrapp.func_class (id, name, lastUpdateTime) VALUES (1, 'Redis', '2022-11-06 15:34:01');
INSERT INTO fgrapp.func_class (id, name, lastUpdateTime) VALUES (2, 'MySQL', '2022-11-09 18:47:05');
INSERT INTO fgrapp.func_class (id, name, lastUpdateTime) VALUES (3, 'SpringBoot', '2022-11-12 19:28:52');
INSERT INTO fgrapp.func_class (id, name, lastUpdateTime) VALUES (4, 'Kafka', '2022-11-12 19:29:38');

INSERT INTO fgrapp.func_comment (id, contextId, userId, upUserId, content, lastUpdateTime) VALUES (1, '115732016959324170', 2, null, '测试添加评论', '2022-11-12 13:57:36');
INSERT INTO fgrapp.func_comment (id, contextId, userId, upUserId, content, lastUpdateTime) VALUES (2, '115732016959324170', 2, 2, '@user_1320072891@qq.com 测试评论评论', '2022-11-12 13:59:58');
INSERT INTO fgrapp.func_comment (id, contextId, userId, upUserId, content, lastUpdateTime) VALUES (3, '115732016959324170', 1, null, '测试评论', '2022-11-12 14:07:11');
INSERT INTO fgrapp.func_comment (id, contextId, userId, upUserId, content, lastUpdateTime) VALUES (4, '115732016959324170', 1, 1, ' 测试评论评论', '2022-11-12 14:15:29');
INSERT INTO fgrapp.func_comment (id, contextId, userId, upUserId, content, lastUpdateTime) VALUES (5, '115732016959324170', 1, 1, ' 测试时间', '2022-11-12 14:25:41');
INSERT INTO fgrapp.func_comment (id, contextId, userId, upUserId, content, lastUpdateTime) VALUES (6, '117197601534640129', 1, null, '测试评论', '2022-11-14 15:52:47');
INSERT INTO fgrapp.func_comment (id, contextId, userId, upUserId, content, lastUpdateTime) VALUES (7, '117197601534640129', 1, 1, ' ', '2022-11-14 15:53:00');

INSERT INTO fgrapp.func_topic (id, problem, summary, answer, classId, lastUpdateTime) VALUES ('114906008848957445', '测试添加', '123123', '123123', 1, '2022-10-06 15:34:01');
INSERT INTO fgrapp.func_topic (id, problem, summary, answer, classId, lastUpdateTime) VALUES ('114909736880570374', '测试redis', '测试格式:::hljs-left1.居左:::==标记==123231123123123123123123123', '# 测试

## 格式

::: hljs-left1.


居左

:::
==标记==

1. 123
2. 231
3. 123123
4. 123
5. 123
6. 123
7. 123
8. 123
', 1, '2022-09-06 15:48:28');
INSERT INTO fgrapp.func_topic (id, problem, summary, answer, classId, lastUpdateTime) VALUES ('115729336899731457', '什么是Redis？它主要用来什么的？', 'Redis，英文全称是RemoteDictionaryServer（远程字典服务），是一个开源的使用ANSIC语言编写、支持网络、可基于内存亦可持久化的日志型、Key-Value数据库，并提供多种语言的API。与MySQL数据库不同的是，Redis的数据是', 'Redis，英文全称是Remote Dictionary Server（远程字典服务），是一个开源的使用ANSI C语言编写、支持网络、可基于内存亦可持久化的日志型、Key-Value数据库，并提供多种语言的API。

与MySQL数据库不同的是，Redis的数据是存在内存中的。它的读写速度非常快，每秒可以处理超过10万次读写操作。因此Redis被广泛应用于缓存，另外，Redis也经常用来做分布式锁。除此之外，Redis支持事务、持久化、LUA 脚本、LRU 驱动事件、多种集群方案。', 1, '2022-11-14 14:57:06');
INSERT INTO fgrapp.func_topic (id, problem, summary, answer, classId, lastUpdateTime) VALUES ('115729603187703810', '说说Redis的基本数据结构类型', 'Redis有以下这五种基本类型：-String（字符串）-Hash（哈希）-List（列表）-Set（集合）-zset（有序集合）它还有三种特殊的数据结构类型-Geospatial-Hyperloglog-Bitmap', 'Redis有以下这五种基本类型：
- String（字符串）
- Hash（哈希）
- List（列表）
- Set（集合）
- zset（有序集合）
它还有三种特殊的数据结构类型
- Geospatial
- Hyperloglog
- Bitmap', 1, '2022-08-08 20:49:58');
INSERT INTO fgrapp.func_topic (id, problem, summary, answer, classId, lastUpdateTime) VALUES ('115730518015737859', '什么是缓存击穿、缓存穿透、缓存雪崩？', '缓存穿透问题先来看一个常见的缓存使用方式：读请求来了，先查下缓存，缓存有值命中，就直接返回；缓存没命中，就去查数据库，然后把数据库的值更新到缓存，再返回。缓存穿透：指查询一个一定不存在的数据，由于缓存是不命中时需要从数据库查询，查不到数据则不写入缓存，这将', '## 缓存穿透问题
先来看一个常见的缓存使用方式：读请求来了，先查下缓存，缓存有值命中，就直接返回；缓存没命中，就去查数据库，然后把数据库的值更新到缓存，再返回。


缓存穿透：指查询一个一定不存在的数据，由于缓存是不命中时需要从数据库查询，查不到数据则不写入缓存，这将导致这个不存在的数据每次请求都要到数据库去查询，进而给数据库带来压力。

通俗点说，读请求访问时，缓存和数据库都没有某个值，这样就会导致每次对这个值的查询请求都会穿透到数据库，这就是缓存穿透。

缓存穿透一般都是这几种情况产生的：

- 业务不合理的设计，比如大多数用户都没开守护，但是你的每个请求都去缓存，查询某个userid查询有没有守护。
- 业务/运维/开发失误的操作，比如缓存和数据库的数据都被误删除了。
- 黑客非法请求攻击，比如黑客故意捏造大量非法请求，以读取不存在的业务数据。
### 如何避免缓存穿透呢？
一般有三种方法。

1. 如果是非法请求，我们在API入口，对参数进行校验，过滤非法值。
2. 如果查询数据库为空，我们可以给缓存设置个空值，或者默认值。但是如有有写请求进来的话，需要更新缓存哈，以保证缓存一致性，同时，最后给缓存设置适当的过期时间。（业务上比较常用，简单有效）
3. 使用布隆过滤器快速判断数据是否存在。即一个查询请求过来时，先通过布隆过滤器判断值是否存在，存在才继续往下查。

布隆过滤器原理：它由初始值为0的位图数组和N个哈希函数组成。一个对一个key进行N个hash算法获取N个值，在比特数组中将这N个值散列后设定为1，然后查的时候如果特定的这几个位置都为1，那么布隆过滤器判断该key存在。
## 缓存雪崩问题
缓存雪崩： 指缓存中数据大批量到过期时间，而查询数据量巨大，请求都直接访问数据库，引起数据库压力过大甚至宕机。

缓存雪崩一般是由于大量数据同时过期造成的，对于这个原因，可通过均匀设置过期时间解决，即让过期时间相对离散一点。如采用一个较大固定值+一个较小的随机值，5小时+0到1800秒酱紫。
Redis 故障宕机也可能引起缓存雪崩。这就需要构造Redis高可用集群啦。
## 缓存击穿问题
缓存击穿： 指热点key在某个时间点过期的时候，而恰好在这个时间点对这个Key有大量的并发请求过来，从而大量的请求打到db。

缓存击穿看着有点像缓存雪崩，其实它两区别是，缓存雪崩是指数据库压力过大甚至宕机，缓存击穿只是大量并发请求到了DB数据库层面。可以认为击穿是缓存雪崩的一个子集吧。有些文章认为它俩区别在于击穿针对某一热点key缓存，雪崩则是很多key。

解决方案就有两种：

1. 使用互斥锁方案。缓存失效时，不是立即去加载db数据，而是先使用某些带成功返回的原子操作命令，如(Redis的setnx）去操作，成功的时候，再去加载db数据库数据和设置缓存。否则就去重试获取缓存。
2. “永不过期”，是指没有设置过期时间，但是热点数据快要过期时，异步线程去更新和设置过期时间。', 1, '2022-11-14 15:25:10');
INSERT INTO fgrapp.func_topic (id, problem, summary, answer, classId, lastUpdateTime) VALUES ('115730664044625924', '什么是热Key问题，如何解决热key问题', '什么是热Key呢？在Redis中，我们把访问频率高的key，称为热点key。如果某一热点key的请求到服务器主机时，由于请求量特别大，可能会导致主机资源不足，甚至宕机，从而影响正常的服务。而热点Key是怎么产生的呢？主要原因有两个：用户消费的数据远大于生产', '## 什么是热Key呢？
在Redis中，我们把访问频率高的key，称为热点key。

如果某一热点key的请求到服务器主机时，由于请求量特别大，可能会导致主机资源不足，甚至宕机，从而影响正常的服务。

而热点Key是怎么产生的呢？主要原因有两个：

- 用户消费的数据远大于生产的数据，如秒杀、热点新闻等读多写少的场景。
- 请求分片集中，超过单Redis服务器的性能，比如固定名称key，Hash落入同一台服务器，瞬间访问量极大，超过机器瓶颈，产生热点Key问题。


那么在日常开发中，如何识别到热点key呢？

- 凭经验判断哪些是热Key；
- 客户端统计上报；
- 服务代理层上报

## 如何解决热key问题？

- Redis集群扩容：增加分片副本，均衡读流量；
- 将热key分散到不同的服务器中；
- 使用二级缓存，即JVM本地缓存,减少Redis的读请求。', 1, '2022-11-14 15:17:45');
INSERT INTO fgrapp.func_topic (id, problem, summary, answer, classId, lastUpdateTime) VALUES ('115730874498023429', '谈谈Redis的过期策略和内存淘汰策略', '我们在setkey的时候，可以给它设置一个过期时间，比如expirekey60。指定这key60s后过期，60s后，Redis是如何处理的嘛？我们先来介绍几种过期策略：定时过期每个设置过期时间的key都需要创建一个定时器，到过期时间就会立即对key进行清除', '我们在`set key`的时候，可以给它设置一个过期时间，比如`expire key 60`。指定这key 60s后过期，60s后，Redis是如何处理的嘛？我们先来介绍几种过期策略：
## 定时过期
每个设置过期时间的key都需要创建一个定时器，到过期时间就会立即对key进行清除。该策略可以立即清除过期的数据，对内存很友好；但是会占用大量的CPU资源去处理过期的数据，从而影响缓存的响应时间和吞吐量。
## 惰性过期
只有当访问一个key时，才会判断该key是否已过期，过期则清除。该策略可以最大化地节省CPU资源，却对内存非常不友好。极端情况可能出现大量的过期key没有再次被访问，从而不会被清除，占用大量内存。
## 定期过期
每隔一定的时间，会扫描一定数量的数据库的expires字典中一定数量的key，并清除其中已过期的key。该策略是前两者的一个折中方案。通过调整定时扫描的时间间隔和每次扫描的限定耗时，可以在不同情况下使得CPU和内存资源达到最优的平衡效果。

expires字典会保存所有设置了过期时间的key的过期时间数据，其中，key是指向键空间中的某个键的指针，value是该键的毫秒精度的UNIX时间戳表示的过期时间。键空间是指该Redis集群中保存的所有键。

**Redis中同时使用了惰性过期和定期过期两种过期策略。**

假设Redis当前存放30万个key，并且都设置了过期时间，如果你每隔100ms就去检查这全部的key，CPU负载会特别高，最后可能会挂掉。
因此，Redis采取的是定期过期，每隔100ms就随机抽取一定数量的key来检查和删除的。
但是呢，最后可能会有很多已经过期的key没被删除。这时候，Redis采用惰性删除。在你获取某个key的时候，redis会检查一下，这个key如果设置了过期时间并且已经过期了，此时就会删除。
但是呀，如果定期删除漏掉了很多过期的key，然后也没走惰性删除。就会有很多过期key积在内存内存，直接会导致内存爆的。或者有些时候，业务量大起来了，Redis的key被大量使用，内存直接不够了，运维小哥哥也忘记加大内存了。难道Redis直接这样挂掉？不会的！Redis用8种内存淘汰策略保护自己~

# Redis的内存淘汰策略
- volatile-lru：当内存不足以容纳新写入数据时，从设置了过期时间的key中使用LRU（最近最少使用）算法进行淘汰；
- allkeys-lru：当内存不足以容纳新写入数据时，从所有key中使用LRU（最近最少使用）算法进行淘汰。
- volatile-lfu：4.0版本新增，当内存不足以容纳新写入数据时，在过期的key中，使用LFU算法进行删除key。
- allkeys-lfu：4.0版本新增，当内存不足以容纳新写入数据时，从所有key中使用LFU算法进行淘汰；
- volatile-random：当内存不足以容纳新写入数据时，从设置了过期时间的key中，随机淘汰数据；。
- allkeys-random：当内存不足以容纳新写入数据时，从所有key中随机淘汰数据。
- volatile-ttl：当内存不足以容纳新写入数据时，在设置了过期时间的key中，根据过期时间进行淘汰，越早过期的优先被淘汰；
- noeviction：默认策略，当内存不足以容纳新写入数据时，新写入操作会报错。', 1, '2022-11-14 15:06:18');
INSERT INTO fgrapp.func_topic (id, problem, summary, answer, classId, lastUpdateTime) VALUES ('115730999052075014', '说说Redis的常用应用场景', '缓存排行榜计数器应用共享Session分布式锁社交网络消息队列位操作缓存我们一提到Redis，自然而然就想到缓存，国内外中大型的网站都离不开缓存。合理的利用缓存，比如缓存热点数据，不仅可以提升网站的访问速度，还可以降低数据库DB的压力。并且，Redis相比', '- 缓存
- 排行榜
- 计数器应用
- 共享Session
- 分布式锁
- 社交网络
- 消息队列
- 位操作
## 缓存
我们一提到Redis，自然而然就想到缓存，国内外中大型的网站都离不开缓存。合理的利用缓存，比如缓存热点数据，不仅可以提升网站的访问速度，还可以降低数据库DB的压力。并且，Redis相比于memcached，还提供了丰富的数据结构，并且提供RDB和AOF等持久化机制，强的一批。

## 排行榜
当今互联网应用，有各种各样的排行榜，如电商网站的月度销量排行榜、社交APP的礼物排行榜、小程序的投票排行榜等等。Redis提供的zset数据类型能够实现这些复杂的排行榜。

比如，用户每天上传视频，获得点赞的排行榜可以这样设计：

1.用户Jay上传一个视频，获得6个赞，可以酱紫：
zadd user:ranking:2021-03-03 Jay 3

2.过了一段时间，再获得一个赞，可以这样：
zincrby user:ranking:2021-03-03 Jay 1

3.如果某个用户John作弊，需要删除该用户：
zrem user:ranking:2021-03-03 John

4.展示获取赞数最多的3个用户
zrevrangebyrank user:ranking:2021-03-03 0 2

## 计数器应用
各大网站、APP应用经常需要计数器的功能，如短视频的播放数、电商网站的浏览数。这些播放数、浏览数一般要求实时的，每一次播放和浏览都要做加1的操作，如果并发量很大对于传统关系型数据的性能是一种挑战。Redis天然支持计数功能而且计数的性能也非常好，可以说是计数器系统的重要选择。

## 共享Session
如果一个分布式Web服务将用户的Session信息保存在各自服务器，用户刷新一次可能就需要重新登录了，这样显然有问题。实际上，可以使用Redis将用户的Session进行集中管理，每次用户更新或者查询登录信息都直接从Redis中集中获取。

## 分布式锁
几乎每个互联网公司中都使用了分布式部署，分布式服务下，就会遇到对同一个资源的并发访问的技术难题，如秒杀、下单减库存等场景。

用synchronize或者reentrantlock本地锁肯定是不行的。
如果是并发量不大话，使用数据库的悲观锁、乐观锁来实现没啥问题。
但是在并发量高的场合中，利用数据库锁来控制资源的并发访问，会影响数据库的性能。
实际上，可以用Redis的setnx来实现分布式的锁。
## 社交网络
赞/踩、粉丝、共同好友/喜好、推送、下拉刷新等是社交网站的必备功能，由于社交网站访问量通常比较大，而且传统的关系型数据不太适保存 这种类型的数据，Redis提供的数据结构可以相对比较容易地实现这些功能。

## 消息队列
消息队列是大型网站必用中间件，如ActiveMQ、RabbitMQ、Kafka等流行的消息队列中间件，主要用于业务解耦、流量削峰及异步处理实时性低的业务。Redis提供了发布/订阅及阻塞队列功能，能实现一个简单的消息队列系统。另外，这个不能和专业的消息中间件相比。

## 位操作
用于数据量上亿的场景下，例如几亿用户系统的签到，去重登录次数统计，某用户是否在线状态等等。腾讯10亿用户，要几个毫秒内查询到某个用户是否在线，能怎么做？千万别说给每个用户建立一个key，然后挨个记（你可以算一下需要的内存会很恐怖，而且这种类似的需求很多。这里要用到位操作——使用setbit、getbit、bitcount命令。原理是：redis内构建一个足够长的数组，每个数组元素只能是0和1两个值，然后这个数组的下标index用来表示用户id（必须是数字哈），那么很显然，这个几亿长的大数组就能通过下标和元素值（0和1）来构建一个记忆系统。', 1, '2022-11-14 15:15:29');
INSERT INTO fgrapp.func_topic (id, problem, summary, answer, classId, lastUpdateTime) VALUES ('115731514448150535', 'Redis 的持久化机制有哪些？优缺点说说', 'Redis是基于内存的非关系型K-V数据库，既然它是基于内存的，如果Redis服务器挂了，数据就会丢失。为了避免数据丢失了，Redis提供了持久化，即把数据保存到磁盘。Redis提供了RDB和AOF两种持久化机制RDBRDB，就是把内存数据以快照的形式保存', 'Redis是基于内存的非关系型K-V数据库，既然它是基于内存的，如果Redis服务器挂了，数据就会丢失。为了避免数据丢失了，Redis提供了持久化，即把数据保存到磁盘。

Redis提供了RDB和AOF两种持久化机制

## RDB
RDB，就是把内存数据以快照的形式保存到磁盘上。

什么是快照?可以这样理解，给当前时刻的数据，拍一张照片，然后保存下来。

RDB持久化，是指在指定的时间间隔内，执行指定次数的写操作，将内存中的数据集快照写入磁盘中，它是Redis默认的持久化方式。执行完操作后，在指定目录下会生成一个dump.rdb文件，Redis 重启的时候，通过加载dump.rdb文件来恢复数据。

### RDB的优点
适合大规模的数据恢复场景，如备份，全量复制等
### RDB的缺点
- 没办法做到实时持久化/秒级持久化。
- 新老版本存在RDB格式兼容问题
## AOF
AOF（append only file） 持久化，采用日志的形式来记录每个写操作，追加到文件中，重启时再重新执行AOF文件中的命令来恢复数据。它主要解决数据持久化的实时性问题。默认是不开启的。
### AOF的优点
数据的一致性和完整性更高
### AOF的缺点
AOF记录的内容越多，文件越大，数据恢复变慢。', 1, '2022-11-14 15:08:38');
INSERT INTO fgrapp.func_topic (id, problem, summary, answer, classId, lastUpdateTime) VALUES ('115731845160632328', '为什么Redis 6.0 之后改多线程呢？', 'Redis6.0之前，Redis在处理客户端的请求时，包括读socket、解析、执行、写socket等都由一个顺序串行的主线程处理，这就是所谓的“单线程”。Redis6.0之前为什么一直不使用多线程？使用Redis时，几乎不存在CPU成为瓶颈的情况，Red', 'Redis6.0之前，Redis在处理客户端的请求时，包括读socket、解析、执行、写socket等都由一个顺序串行的主线程处理，这就是所谓的“单线程”。

Redis6.0之前为什么一直不使用多线程？

使用Redis时，几乎不存在CPU成为瓶颈的情况， Redis主要受限于内存和网络。例如在一个普通的Linux系统上，Redis通过使用pipelining每秒可以处理100万个请求，所以如果应用程序主要使用O(N)或O(log(N))的命令，它几乎不会占用太多CPU。

Redis使用多线程并非是完全摒弃单线程，Redis还是使用单线程模型来处理客户端的请求，只是使用多线程来处理数据的读写和协议解析，执行命令还是使用单线程。

这样做的目的是因为Redis的性能瓶颈在于网络IO而非CPU，使用多线程能提升IO读写的效率，从而整体提高Redis的性能。', 1, '2022-11-14 15:00:05');
INSERT INTO fgrapp.func_topic (id, problem, summary, answer, classId, lastUpdateTime) VALUES ('115731931059978249', '聊聊Redis 事务机制', 'Redis通过MULTI、EXEC、WATCH等一组命令集合，来实现事务机制。事务支持一次执行多个命令，一个事务中所有命令都会被序列化。在事务执行过程，会按照顺序串行化执行队列中的命令，其他客户端提交的命令请求不会插入到事务执行命令序列中。简言之，Redi', 'Redis通过MULTI、EXEC、WATCH等一组命令集合，来实现事务机制。事务支持一次执行多个命令，一个事务中所有命令都会被序列化。在事务执行过程，会按照顺序串行化执行队列中的命令，其他客户端提交的命令请求不会插入到事务执行命令序列中。

简言之，Redis事务就是顺序性、一次性、排他性的执行一个队列中的一系列命令。

Redis执行事务的流程如下：

- 开始事务（MULTI）
- 命令入队
- 执行事务（EXEC）、撤销事务（DISCARD ）', 1, '2022-11-14 15:18:48');
INSERT INTO fgrapp.func_topic (id, problem, summary, answer, classId, lastUpdateTime) VALUES ('115732016959324170', '在生成 RDB期间，Redis 可以同时处理写请求么？', '可以的，Redis提供两个指令生成RDB，分别是save和bgsave。如果是save指令，会阻塞，因为是主线程执行的。如果是bgsave指令，是fork一个子进程来写入RDB文件的，快照持久化完全交给子进程来处理，父进程则可以继续处理客户端的请求。', '可以的，Redis提供两个指令生成RDB，分别是save和bgsave。

如果是save指令，会阻塞，因为是主线程执行的。
如果是bgsave指令，是fork一个子进程来写入RDB文件的，快照持久化完全交给子进程来处理，父进程则可以继续处理客户端的请求。', 1, '2022-11-12 19:46:06');
INSERT INTO fgrapp.func_topic (id, problem, summary, answer, classId, lastUpdateTime) VALUES ('116072801139425281', '测试Mysql', '12312313132123123123123', '12312313132123123123123', 2, '2022-07-09 19:01:46');
INSERT INTO fgrapp.func_topic (id, problem, summary, answer, classId, lastUpdateTime) VALUES ('117197601534640129', '测试Kafka', '测试Kafka', '# 测试Kafka', 4, '2022-11-12 19:46:33');
INSERT INTO fgrapp.func_topic (id, problem, summary, answer, classId, lastUpdateTime) VALUES ('117873371688992769', 'Redis 是单进程单线程的？', 'Redis是单进程单线程的，redis利用队列技术将并发访问变为串行访问，消除了传统数据库串行控制的开销。', 'Redis 是单进程单线程的， redis 利用队列技术将并发访问变为串行访问， 消除了传统数据库串行控制的开销。', 1, '2022-11-14 15:28:54');
INSERT INTO fgrapp.func_topic (id, problem, summary, answer, classId, lastUpdateTime) VALUES ('117873474768207874', '一个字符串类型的值能存储最大容量是多少？', '512M', '512M', 1, '2022-11-14 15:29:18');
INSERT INTO fgrapp.func_topic (id, problem, summary, answer, classId, lastUpdateTime) VALUES ('117873942919643139', '为什么 Redis 需要把所有数据放到内存中？', 'Redis为了达到最快的读写速度将数据都读到内存中，并通过异步的方式将数据写入磁盘。所以Redis具有快速和数据持久化的特征。如果数据放在内存中，磁盘I/O速度会严重影响Redis的性能。在内存越来越便宜的今天，Redis将会越来越受欢迎。如果设置了最大使', 'Redis 为了达到最快的读写速度将数据都读到内存中，并通过异步的方式将数据写入磁盘。所以Redis具有快速和数据持久化的特征。如果数据放在内存中，磁盘I/O速度会严重影响 Redis 的性能。在内存越来越便宜的今天， Redis 将会越来越受欢迎。如果设置了最大使用的内存， 则数据已有记录数达到内存限值后不能继续插入新值。', 1, '2022-11-14 15:31:06');
INSERT INTO fgrapp.func_topic (id, problem, summary, answer, classId, lastUpdateTime) VALUES ('117874127603236868', 'Redis 的同步机制了解么？', 'Redis可以使用主从同步，从从同步。第一次同步时，主节点做一次bgsave，并同时将后续修改操作记录到内存buffffer，待完成后将rdb文件全量同步到复制节点，复制节点接受完成后将rdb镜像加载到内存。加载完成后，再通知主节点将期间修改的操作记录同步', 'Redis 可以使用主从同步，从从同步。第一次同步时，主节点做一次 bgsave， 并同时将后续修改操作记录到内存 buffffer， 待完成后将 rdb 文件全量同步到复制节点， 复制节点接受完成后将 rdb 镜像加载到内存。加载完成后， 再通知主节点将期间修改的操作记录同步到复制节点进行重放就完成了同步过程。', 1, '2022-11-14 15:31:49');
INSERT INTO fgrapp.func_topic (id, problem, summary, answer, classId, lastUpdateTime) VALUES ('117874286517026821', 'Pipeline 有什么好处，为什么要用 pipeline？', '可以将多次IO往返的时间缩减为一次，前提是pipeline执行的指令之间没有因果相关性。使用redis-benchmark进行压测的时候可以发现影响redis的QPS峰值的一个重要因素是pipeline批次指令的数目。', '可以将多次 IO 往返的时间缩减为一次，前提是 pipeline 执行的指令之间没有因果相关性。使用redis-benchmark 进行压测的时候可以发现影响 redis 的 QPS 峰值的一个重要因素是 pipeline 批次指令的数目。', 1, '2022-11-14 15:32:26');
INSERT INTO fgrapp.func_topic (id, problem, summary, answer, classId, lastUpdateTime) VALUES ('117874638704345094', '是否使用过 Redis 集群，集群的原理是什么？', 'RedisSentinal着眼于高可用，在master宕机时会自动将slave提升为master，继续提供服务。RedisCluster着眼于扩展性，在单个Redis内存不足时，使用Cluster进行分片存储。', '- Redis Sentinal 着眼于高可用， 在 master 宕机时会自动将 slave 提升为master， 继续提供服务。
- Redis Cluster 着眼于扩展性， 在单个 Redis 内存不足时， 使用 Cluster 进行分片存储。', 1, '2022-11-14 15:33:49');
INSERT INTO fgrapp.func_topic (id, problem, summary, answer, classId, lastUpdateTime) VALUES ('117874827682906119', 'Redis 集群方案什么情况下会导致整个集群不可用？', '有A，B，C三个节点的集群,在没有复制模型的情况下,如果节点B失败了，那么整个集群就会以为缺少5501-11000这个范围的槽而不可用。', '有 A， B， C 三个节点的集群,在没有复制模型的情况下,如果节点 B 失败了， 那么整个集群就会以为缺少 5501-11000 这个范围的槽而不可用。', 1, '2022-11-14 15:34:32');
INSERT INTO fgrapp.func_topic (id, problem, summary, answer, classId, lastUpdateTime) VALUES ('117875089675911176', '说说 Redis 哈希槽的概念？', 'Redis集群没有使用一致性hash,而是引入了哈希槽的概念，Redis集群有16384个哈希槽，每个key通过CRC16校验后对16384取模来决定放置哪个槽，集群的每个节点负责一部分hash槽。', 'Redis 集群没有使用一致性 hash,而是引入了哈希槽的概念， Redis 集群有16384 个哈希槽，每个key 通过 CRC16 校验后对 16384 取模来决定放置哪个槽， 集群的每个节点负责一部分 hash 槽。', 1, '2022-11-14 15:35:33');
INSERT INTO fgrapp.func_topic (id, problem, summary, answer, classId, lastUpdateTime) VALUES ('117875360258850825', 'Redis 集群的主从复制模型是怎样的？', '为了使在部分节点失败或者大部分节点无法通信的情况下集群仍然可用，所以集群使用了主从复制模型,每个节点都会有N-1个复制品.', '为了使在部分节点失败或者大部分节点无法通信的情况下集群仍然可用， 所以集群使用了主从复制模型,每个节点都会有 N-1 个复制品.', 1, '2022-11-14 15:36:36');
INSERT INTO fgrapp.func_topic (id, problem, summary, answer, classId, lastUpdateTime) VALUES ('118209809362190337', '测试缓存', '测试内容测试修改内容', '测试内容
测试修改内容', 1, '2022-11-15 13:15:32');
INSERT INTO fgrapp.func_topic (id, problem, summary, answer, classId, lastUpdateTime) VALUES ('118210569571401730', '测试缓存', '测试新增', '测试新增', 1, '2022-11-15 13:17:23');

INSERT INTO fgrapp.sys_user (id, nickName, email, phone, openId, sex, avatar, isAdmin, lastUpdateTime) VALUES (1, 'fgr', '898365387@qq.com', null, null, null, null, 1, '2022-11-05 09:53:28');
INSERT INTO fgrapp.sys_user (id, nickName, email, phone, openId, sex, avatar, isAdmin, lastUpdateTime) VALUES (2, 'user_1320072891@qq.com', '1320072891@qq.com', null, null, null, null, 0, '2022-11-12 09:40:07');