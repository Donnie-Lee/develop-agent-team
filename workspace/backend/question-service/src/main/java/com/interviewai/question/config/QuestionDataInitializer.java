package com.interviewai.question.config;

import com.interviewai.question.entity.Question;
import com.interviewai.question.repository.QuestionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * Initialize question bank with sample questions
 */
@Slf4j
@Component
public class QuestionDataInitializer implements CommandLineRunner {

    @Autowired
    private QuestionRepository questionRepository;

    @Override
    @Transactional
    public void run(String... args) {
        // Check if questions already exist
        if (questionRepository.selectCount(null) > 0) {
            log.info("Question bank already initialized, skipping...");
            return;
        }

        log.info("Initializing question bank with sample questions...");

        // Frontend questions
        List<Question> frontendQuestions = createFrontendQuestions();
        frontendQuestions.forEach(questionRepository::insert);

        // Backend questions
        List<Question> backendQuestions = createBackendQuestions();
        backendQuestions.forEach(questionRepository::insert);

        // Fullstack questions
        List<Question> fullstackQuestions = createFullstackQuestions();
        fullstackQuestions.forEach(questionRepository::insert);

        // Behavioral questions
        List<Question> behavioralQuestions = createBehavioralQuestions();
        behavioralQuestions.forEach(questionRepository::insert);

        log.info("Question bank initialized with {} questions",
                frontendQuestions.size() + backendQuestions.size() +
                fullstackQuestions.size() + behavioralQuestions.size());
    }

    private List<Question> createFrontendQuestions() {
        return Arrays.asList(
                // JavaScript Basics
                Question.builder()
                        .title("JavaScript 变量提升")
                        .content("请解释什么是变量提升（Hoisting），以及 var、let、const 三者的区别？")
                        .type("technical")
                        .difficulty("medium")
                        .category("frontend")
                        .tags("JavaScript,ES6,变量")
                        .answer("变量提升是指变量和函数的声明会被移动到作用域顶部。var具有函数作用域，存在变量提升，可重复声明；let和const具有块级作用域，不存在变量提升（暂时性死区），不可重复声明；const还要求初始化且不可重新赋值。")
                        .explanation("考察对ES6新特性的理解深度")
                        .viewCount(0).likeCount(0).collectCount(0)
                        .build(),

                Question.builder()
                        .title("Promise 和 async/await")
                        .content("请解释 Promise 的三种状态，以及 async/await 相比 Promise 的优势？")
                        .type("technical")
                        .difficulty("medium")
                        .category("frontend")
                        .tags("JavaScript,Promise,异步")
                        .answer("Promise有三种状态：pending（进行中）、fulfilled（已成功）、rejected（已失败）。async/await是Promise的语法糖，让异步代码看起来像同步代码，更容易阅读和理解，可以避免回调地狱。")
                        .explanation("考察异步编程理解")
                        .viewCount(0).likeCount(0).collectCount(0)
                        .build(),

                Question.builder()
                        .title("Vue 响应式原理")
                        .content("请详细解释 Vue2 和 Vue3 的响应式原理有什么区别？")
                        .type("technical")
                        .difficulty("hard")
                        .category("frontend")
                        .tags("Vue,响应式,原理")
                        .answer("Vue2使用Object.defineProperty()实现响应式，无法检测对象属性的添加或删除，对于数组需要特殊处理。Vue3使用Proxy代理对象，可以监听对象属性的添加、删除，以及数组索引变化，性能更优。")
                        .explanation("考察框架原理理解")
                        .viewCount(0).likeCount(0).collectCount(0)
                        .build(),

                Question.builder()
                        .title("浏览器事件循环")
                        .content("请解释浏览器的事件循环（Event Loop）机制，以及宏任务和微任务的区别？")
                        .type("technical")
                        .difficulty("hard")
                        .category("frontend")
                        .tags("JavaScript,浏览器,事件循环")
                        .answer("事件循环是一个无限循环，不断从任务队列中取出任务执行。宏任务包括setTimeout、setInterval、I/O操作等；微任务包括Promise.then()、MutationObserver等。执行顺序：执行当前宏任务 -> 执行所有微任务 -> 渲染 -> 执行下一个宏任务。")
                        .explanation("考察JavaScript异步执行机制")
                        .viewCount(0).likeCount(0).collectCount(0)
                        .build(),

                Question.builder()
                        .title("React Hooks 规则")
                        .content("React Hooks 有哪些规则？为什么需要这些规则？")
                        .type("technical")
                        .difficulty("medium")
                        .category("frontend")
                        .tags("React,Hooks")
                        .answer("Hook规则：1)只在顶层调用Hook，不要在循环、条件语句或嵌套函数中调用；2)只在React函数组件或自定义Hook中调用。原因是Hook依赖于调用顺序来保存状态，如果顺序改变会导致状态错乱。")
                        .explanation("考察React Hooks理解")
                        .viewCount(0).likeCount(0).collectCount(0)
                        .build()
        );
    }

    private List<Question> createBackendQuestions() {
        return Arrays.asList(
                // Java
                Question.builder()
                        .title("JVM 内存结构")
                        .content("请解释 JVM 的内存结构，包括堆、栈、方法区等各区域的作用？")
                        .type("technical")
                        .difficulty("hard")
                        .category("backend")
                        .tags("Java,JVM,内存")
                        .answer("JVM内存分为：堆（Heap）存储对象实例；栈（Stack）存储局部变量、操作数栈；方法区（Method Area）存储类信息、常量、静态变量；程序计数器（PC Register）记录当前线程执行位置；本地方法栈（Native Method Stack）支持native方法。")
                        .explanation("考察Java底层知识")
                        .viewCount(0).likeCount(0).collectCount(0)
                        .build(),

                Question.builder()
                        .title("Spring Bean 生命周期")
                        .content("请详细描述 Spring Bean 的完整生命周期过程？")
                        .type("technical")
                        .difficulty("hard")
                        .category("backend")
                        .tags("Java,Spring,Bean")
                        .answer("1)实例化Bean；2)设置属性值；3)执行BeanNameAware接口方法；4)执行BeanFactoryAware接口方法；5)执行ApplicationContextAware接口方法；6)BeanPostProcessor.postProcessBeforeInitialization；7)执行InitializingBean接口方法；8)执行自定义init-method；9)BeanPostProcessor.postProcessAfterInitialization；10)Bean就绪；11)容器关闭时执行DisposableBean.destroy()和自定义destroy-method。")
                        .explanation("考察Spring框架深度理解")
                        .viewCount(0).likeCount(0).collectCount(0)
                        .build(),

                Question.builder()
                        .title("MySQL 事务隔离级别")
                        .content("MySQL 有哪些事务隔离级别？分别能解决什么问题？")
                        .type("technical")
                        .difficulty("medium")
                        .category("backend")
                        .tags("MySQL,事务,隔离级别")
                        .answer("四种隔离级别：1)读未提交（Read Uncommitted）可能出现脏读；2)读已提交（Read Committed）解决脏读；3)可重复读（Repeatable Read）解决脏读和不可重复读，MySQL默认；4)串行化（Serializable）解决所有并发问题但性能最差。")
                        .explanation("考察数据库事务知识")
                        .viewCount(0).likeCount(0).collectCount(0)
                        .build(),

                Question.builder()
                        .title("Redis 数据结构")
                        .content("Redis 有哪些主要的数据结构？分别适用于什么场景？")
                        .type("technical")
                        .difficulty("medium")
                        .category("backend")
                        .tags("Redis,数据结构,缓存")
                        .answer("String（字符串）：缓存、计数器、分布式锁；Hash（哈希）：对象存储；List（列表）：消息队列、排行榜；Set（集合）：标签系统、共同好友；Sorted Set（有序集合）：排行榜、带权重的消息队列；Bitmap：签到统计；HyperLogLog：UV统计。")
                        .explanation("考察Redis数据结构理解")
                        .viewCount(0).likeCount(0).collectCount(0)
                        .build(),

                Question.builder()
                        .title("微服务架构优势")
                        .content("微服务架构相比单体架构有哪些优势和挑战？")
                        .type("technical")
                        .difficulty("medium")
                        .category("backend")
                        .tags("微服务,架构,分布式")
                        .answer("优势：1)独立部署，迭代快；2)技术异构；3)容错性好；4)扩展性强。挑战：1)运维复杂；2)分布式事务；3)服务间通信；4)接口一致性；5)调试排错困难。需要完善的CI/CD、服务治理、监控体系支撑。")
                        .explanation("考察架构设计能力")
                        .viewCount(0).likeCount(0).collectCount(0)
                        .build()
        );
    }

    private List<Question> createFullstackQuestions() {
        return Arrays.asList(
                Question.builder()
                        .title("RESTful API 设计原则")
                        .content("请解释 RESTful API 的设计原则，以及如何设计一个好的 RESTful 接口？")
                        .type("technical")
                        .difficulty("medium")
                        .category("fullstack")
                        .tags("RESTful,API,设计")
                        .answer("RESTful原则：1)使用HTTP动词表示操作；2)使用名词表示资源；3)无状态；4)使用HTTP状态码。设计要点：1)版本控制；2)统一响应格式；3)分页；4)过滤和排序；5)错误处理；6)安全性（认证授权）。")
                        .explanation("考察API设计能力")
                        .viewCount(0).likeCount(0).collectCount(0)
                        .build(),

                Question.builder()
                        .title("OAuth2.0 流程")
                        .content("请解释 OAuth2.0 的授权流程，以及四种授权模式的区别？")
                        .type("technical")
                        .difficulty("hard")
                        .category("fullstack")
                        .tags("OAuth2.0,认证,授权")
                        .answer("OAuth2.0四种模式：1)授权码模式：最安全，适用于有后端的Web应用；2)简化模式：不返回code，适用于SPA；3)密码模式：直接用用户名密码换token，适用于受信任应用；4)客户端模式：适用于后端服务间调用。流程：用户授权 -> 获取code -> 换token -> 访问资源。")
                        .explanation("考察认证授权理解")
                        .viewCount(0).likeCount(0).collectCount(0)
                        .build(),

                Question.builder()
                        .title("前后端分离架构")
                        .content("请解释前后端分离架构的优势，以及需要注意哪些问题？")
                        .type("technical")
                        .difficulty("medium")
                        .category("fullstack")
                        .tags("前后端分离,架构")
                        .answer("优势：1)独立开发部署；2)技术异构；3)并行开发；4)提高开发效率。注意事项：1)跨域问题；2)接口规范；3)用户认证；4)错误处理；5)前端路由与后端路由协调；6)SEO问题（SSR解决方案）。")
                        .explanation("考察全栈架构理解")
                        .viewCount(0).likeCount(0).collectCount(0)
                        .build()
        );
    }

    private List<Question> createBehavioralQuestions() {
        return Arrays.asList(
                Question.builder()
                        .title("项目经验介绍")
                        .content("请介绍一个你最引以为豪的项目，你在里面扮演什么角色，解决了什么难题？")
                        .type("behavioral")
                        .difficulty("medium")
                        .category("behavioral")
                        .tags("项目经验,团队协作")
                        .answer("要点：1)项目背景和目标；2)个人角色和贡献；3)遇到的挑战；4)解决方案；5)项目成果和学到的经验。建议用STAR法则（Situation-Task-Action-Result）来组织回答。")
                        .explanation("考察项目经验和表达能力")
                        .viewCount(0).likeCount(0).collectCount(0)
                        .build(),

                Question.builder()
                        .title("团队冲突处理")
                        .content("请描述一次你在团队中与同事产生分歧的经历，你是如何处理的？")
                        .type("behavioral")
                        .difficulty("medium")
                        .category("behavioral")
                        .tags("团队协作,冲突处理")
                        .answer("处理方式：1)倾听对方观点；2)表达自己立场；3)寻找共同点；4)寻求妥协方案；5)必要时请第三方协调。关键点：保持专业、尊重他人、注重沟通技巧、以项目目标为导向。")
                        .explanation("考察人际沟通和冲突处理能力")
                        .viewCount(0).likeCount(0).collectCount(0)
                        .build(),

                Question.builder()
                        .title("技术难点攻克")
                        .content("请描述一次你为了解决技术难题而努力学习新技术或新知识的经历？")
                        .type("behavioral")
                        .difficulty("medium")
                        .category("behavioral")
                        .tags("学习能力,技术成长")
                        .answer("回答要点：1)技术难点的具体内容；2)为什么选择学习新技术；3)学习过程和方法；4)最终如何应用和解决问题；5)这次经历对你的影响。展示学习能力和解决问题的能力。")
                        .explanation("考察学习能力和抗压能力")
                        .viewCount(0).likeCount(0).collectCount(0)
                        .build(),

                Question.builder()
                        .title("职业规划")
                        .content("你未来3-5年的职业规划是什么？你为什么想加入我们公司？")
                        .type("behavioral")
                        .difficulty("easy")
                        .category("behavioral")
                        .tags("职业规划,求职动机")
                        .answer("建议：1)有清晰的短期和长期目标；2)目标与应聘岗位相关；3)展示对公司了解和热情；4)强调能为公司带来的价值。结合个人兴趣、行业发展、公司文化来回答。")
                        .explanation("考察职业规划和求职动机")
                        .viewCount(0).likeCount(0).collectCount(0)
                        .build(),

                Question.builder()
                        .title("代码质量意识")
                        .content("你是如何保证代码质量的？你认为好的代码应该具备哪些特点？")
                        .type("behavioral")
                        .difficulty("medium")
                        .category("behavioral")
                        .tags("代码质量,工程能力")
                        .answer("代码质量保障：1)代码review；2)单元测试；3)编码规范；4)重构习惯。好代码特点：1)可读性（命名、注释）；2)可维护性；3)可扩展性；4)性能优良；5)安全性；6)有适当的测试覆盖。")
                        .explanation("考察工程素养和质量意识")
                        .viewCount(0).likeCount(0).collectCount(0)
                        .build()
        );
    }
}
