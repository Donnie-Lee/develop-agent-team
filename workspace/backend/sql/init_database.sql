-- AI面试练习平台 - 数据库初始化脚本
-- 版本: 1.2.0
-- 日期: 2026-04-07
-- PostgreSQL 兼容语法

-- 创建数据库
CREATE DATABASE interview_ai WITH ENCODING 'UTF8';
\c interview_ai;

-- 1. 用户表 (users)
CREATE TABLE users (
    id              BIGSERIAL PRIMARY KEY,
    phone           VARCHAR(20) UNIQUE,
    password_hash   VARCHAR(255),
    nickname        VARCHAR(50),
    avatar_url      VARCHAR(500),
    member_level    SMALLINT DEFAULT 0,
    member_expire   TIMESTAMP,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at      TIMESTAMP
);

CREATE INDEX idx_users_phone ON users(phone);
CREATE INDEX idx_users_created ON users(created_at);

-- 2. 简历表 (resumes)
CREATE TABLE resumes (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL,
    title           VARCHAR(100),
    is_default      SMALLINT DEFAULT 0,
    file_url        VARCHAR(500),
    parse_status    SMALLINT DEFAULT 0,
    parse_result    JSON,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at      TIMESTAMP
);

CREATE INDEX idx_resumes_user ON resumes(user_id);
ALTER TABLE resumes ADD CONSTRAINT fk_resumes_user FOREIGN KEY (user_id) REFERENCES users(id);

-- 3. 简历详情表 (resume_details)
CREATE TABLE resume_details (
    id              BIGSERIAL PRIMARY KEY,
    resume_id       BIGINT NOT NULL,
    section_type    VARCHAR(20),
    content         JSON,
    sort_order      INT DEFAULT 0,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at      TIMESTAMP
);

CREATE INDEX idx_resume_details_resume ON resume_details(resume_id);
ALTER TABLE resume_details ADD CONSTRAINT fk_resume_details_resume FOREIGN KEY (resume_id) REFERENCES resumes(id);

-- 4. 面试记录表 (interviews)
CREATE TABLE interviews (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL,
    interview_type  VARCHAR(20),
    position        VARCHAR(50),
    duration        INT,
    status          VARCHAR(20) DEFAULT 'pending',
    total_score     INT,
    question_count  INT DEFAULT 0,
    answered_count  INT DEFAULT 0,
    summary         TEXT,
    feedback        TEXT,
    start_time      TIMESTAMP,
    end_time        TIMESTAMP,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at      TIMESTAMP
);

CREATE INDEX idx_interviews_user ON interviews(user_id);
CREATE INDEX idx_interviews_status ON interviews(status);
CREATE INDEX idx_interviews_created ON interviews(created_at);
ALTER TABLE interviews ADD CONSTRAINT fk_interviews_user FOREIGN KEY (user_id) REFERENCES users(id);

-- 5. 面试问答表 (interview_questions)
CREATE TABLE interview_questions (
    id              BIGSERIAL PRIMARY KEY,
    interview_id    BIGINT NOT NULL,
    session_id      BIGINT,
    question_type   VARCHAR(20),
    question_text   TEXT,
    expected_answer TEXT,
    user_answer     TEXT,
    score           INT,
    feedback        TEXT,
    order_index     INT DEFAULT 0,
    status          VARCHAR(20) DEFAULT 'pending',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at      TIMESTAMP
);

CREATE INDEX idx_interview_questions_interview ON interview_questions(interview_id);
CREATE INDEX idx_interview_questions_session ON interview_questions(session_id);
ALTER TABLE interview_questions ADD CONSTRAINT fk_interview_questions_interview FOREIGN KEY (interview_id) REFERENCES interviews(id);

-- 6. 面试会话表 (interview_sessions)
CREATE TABLE interview_sessions (
    id                      BIGSERIAL PRIMARY KEY,
    interview_id            BIGINT NOT NULL,
    user_id                 BIGINT NOT NULL,
    session_type            VARCHAR(20),
    status                  VARCHAR(20) DEFAULT 'pending',
    context_data            TEXT,
    current_question_index  INT DEFAULT 0,
    started_at              TIMESTAMP,
    ended_at                TIMESTAMP,
    created_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at              TIMESTAMP
);

CREATE INDEX idx_interview_sessions_interview ON interview_sessions(interview_id);
CREATE INDEX idx_interview_sessions_user ON interview_sessions(user_id);
ALTER TABLE interview_sessions ADD CONSTRAINT fk_interview_sessions_interview FOREIGN KEY (interview_id) REFERENCES interviews(id);
ALTER TABLE interview_sessions ADD CONSTRAINT fk_interview_sessions_user FOREIGN KEY (user_id) REFERENCES users(id);

-- 7. 题库表 (questions)
CREATE TABLE questions (
    id              BIGSERIAL PRIMARY KEY,
    title           VARCHAR(255),
    content         TEXT,
    type            VARCHAR(20),
    difficulty      VARCHAR(10),
    category        VARCHAR(30),
    tags            VARCHAR(500),
    answer          TEXT,
    explanation     TEXT,
    view_count      INT DEFAULT 0,
    like_count      INT DEFAULT 0,
    collect_count   INT DEFAULT 0,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at      TIMESTAMP
);

CREATE INDEX idx_questions_category ON questions(category);
CREATE INDEX idx_questions_difficulty ON questions(difficulty);
CREATE INDEX idx_questions_created ON questions(created_at);

-- 8. 会员表 (members)
CREATE TABLE members (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL UNIQUE,
    level           INT DEFAULT 0,
    member_since    TIMESTAMP,
    expire_time     TIMESTAMP,
    status          INT DEFAULT 1,
    total_amount   DECIMAL(10,2) DEFAULT 0,
    description     TEXT,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at      TIMESTAMP
);

CREATE INDEX idx_members_user ON members(user_id);
CREATE INDEX idx_members_status ON members(status);
ALTER TABLE members ADD CONSTRAINT fk_members_user FOREIGN KEY (user_id) REFERENCES users(id);

-- 9. 会员订单表 (member_orders)
CREATE TABLE member_orders (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL,
    order_no        VARCHAR(64) UNIQUE,
    member_level    SMALLINT,
    duration_type   VARCHAR(20),
    amount          DECIMAL(10,2),
    status          VARCHAR(20) DEFAULT 'pending',
    paid_at         TIMESTAMP,
    expire_at       TIMESTAMP,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_member_orders_user ON member_orders(user_id);
CREATE INDEX idx_member_orders_order_no ON member_orders(order_no);
ALTER TABLE member_orders ADD CONSTRAINT fk_member_orders_user FOREIGN KEY (user_id) REFERENCES users(id);

-- 10. 消息通知表 (notifications)
CREATE TABLE notifications (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL,
    type            VARCHAR(20),
    title           VARCHAR(100),
    content         TEXT,
    channel         VARCHAR(20),
    status          VARCHAR(20) DEFAULT 'unread',
    receiver        VARCHAR(100),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at      TIMESTAMP
);

CREATE INDEX idx_notifications_user ON notifications(user_id);
CREATE INDEX idx_notifications_status ON notifications(status);
CREATE INDEX idx_notifications_type ON notifications(type);
ALTER TABLE notifications ADD CONSTRAINT fk_notifications_user FOREIGN KEY (user_id) REFERENCES users(id);

-- 11. 用户收藏/错题表 (user_collections)
CREATE TABLE user_collections (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL,
    question_id     BIGINT,
    type            VARCHAR(20),
    interview_id    BIGINT,
    note            TEXT,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_user_collections_user_question ON user_collections(user_id, question_id);
CREATE INDEX idx_user_collections_type ON user_collections(type);
ALTER TABLE user_collections ADD CONSTRAINT fk_user_collections_user FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE user_collections ADD CONSTRAINT fk_user_collections_question FOREIGN KEY (question_id) REFERENCES questions(id);
ALTER TABLE user_collections ADD CONSTRAINT fk_user_collections_interview FOREIGN KEY (interview_id) REFERENCES interviews(id);

-- ==================== 测试数据 ====================

-- 插入测试用户
INSERT INTO users (phone, nickname, member_level) VALUES
('13800138000', '测试用户', 1),
('13800138001', '张三', 0),
('13800138002', '李四', 2);

-- 插入题库测试数据
INSERT INTO questions (title, content, type, difficulty, category, tags, answer, explanation) VALUES
('Vue响应式原理', '请解释Vue3的响应式原理是什么？', 'discussion', 'medium', 'frontend', 'vue,javascript,响应式', 'Vue3使用Proxy替代了Vue2的Object.defineProperty，通过代理实现响应式数据追踪。', 'Vue3的响应式系统基于ES6的Proxy实现，可以监听对象属性的添加、删除、修改等操作。'),

('闭包概念', '请解释什么是闭包？', 'discussion', 'simple', 'frontend', 'javascript,闭包,作用域', '闭包是指函数能够访问其词法作用域外部的变量，形成的组合。', '闭包允许内部函数访问外部函数的变量，即使外部函数已经执行完毕。'),

('CSS Flexbox', 'CSS Flexbox中justify-content和align-items的区别是什么？', 'discussion', 'simple', 'frontend', 'css,flexbox,布局', 'justify-content控制主轴方向对齐，align-items控制交叉轴方向对齐。', '在flex容器中，主轴由flex-direction决定，交叉轴垂直于主轴。'),

('HTTP缓存', '请解释强缓存和协商缓存的区别？', 'discussion', 'medium', 'frontend', 'http,缓存,性能', '强缓存不发请求直接使用缓存，协商缓存需要发起请求验证资源是否更新。', '强缓存由Cache-Control和Expires控制，协商缓存由ETag和Last-Modified控制。'),

('Git rebase vs merge', 'Git中rebase和merge有什么区别？', 'discussion', 'medium', 'frontend', 'git,版本控制', 'merge会创建合并提交保留历史，rebase会变基产生线性历史。', 'rebase会将当前分支的提交在目标分支上重新应用，产生更整洁的提交历史。'),

('JavaScript类型转换', '[] == ![]结果是什么？为什么？', 'multiple_choice', 'hard', 'frontend', 'javascript,类型转换', '结果为true。空数组转数字为0，![]为false，0 == false成立。', '涉及隐式类型转换规则：ToNumber([])得到0，ToBoolean(![])得到false。'),

('React Hooks', 'useEffect的依赖数组为空数组和没有依赖数组有什么区别？', 'discussion', 'medium', 'frontend', 'react,hooks,useEffect', '空数组只在首次渲染后执行一次；没有依赖数组则在每次渲染后都执行。', 'useEffect的第二个参数控制执行时机，不传则每次渲染后都执行。'),

('SQL注入防范', '如何防范SQL注入攻击？', 'discussion', 'medium', 'backend', 'sql,安全,sql注入', '使用参数化查询、输入验证，最小权限原则。', '永远不要拼接SQL字符串，使用PreparedStatement或ORM框架。'),

('Redis数据类型', 'Redis支持哪些数据结构？', 'multiple_choice', 'simple', 'backend', 'redis,nosql', 'String、Hash、List、Set、ZSet、Bitmap、HyperLogLog、GEO等。', 'Redis不只是简单的key-value存储，还支持多种复杂数据结构。'),

('进程与线程', '进程和线程的区别是什么？', 'multiple_choice', 'simple', 'backend', '操作系统,进程,线程', '进程是资源分配的最小单位，线程是CPU调度的最小单位。', '同一进程内的线程共享进程资源，但有独立的执行栈和寄存器。'),

('JWT认证', 'JWT由哪三部分组成？', 'multiple_choice', 'simple', 'backend', 'jwt,认证,token', 'Header、Payload、Signature。', 'JWT是一种开放标准，用于在各方之间安全地传输信息。'),

('数据库索引', '什么情况下不适合建立索引？', 'discussion', 'hard', 'backend', '数据库,索引,性能优化', '数据量小、频繁更新、唯一性差、存储密度低等情况下不适合。', '索引虽然能提高查询速度，但会增加写操作开销和存储空间。'),

('Docker容器重启策略', 'Docker容器的重启策略有哪些？', 'multiple_choice', 'medium', 'backend', 'docker,容器', 'no、always、unless-stopped、on-failure。', 'always表示容器退出后总是重启，on-failure只在非0退出码时重启。'),

('TypeScript泛型', '什么是TypeScript泛型？有什么作用？', 'discussion', 'medium', 'frontend', 'typescript,泛型', '泛型允许创建可重用的组件，支持多种类型同时保持类型安全。', '使用<T>语法定义泛型，可以在函数、类、接口中使用。'),

('性能优化策略', '前端性能优化有哪些常见策略？', 'discussion', 'medium', 'frontend', '性能优化,前端', '代码分割、懒加载、缓存、CDN、压缩、预加载等。', '优化关键渲染路径、减少HTTP请求、使用tree-shaking等。'),

('微服务通信方式', '微服务之间有哪些通信方式？', 'multiple_choice', 'medium', 'backend', '微服务,分布式', '同步通信(REST/gRPC)、异步通信(消息队列)、事件驱动。', '选择通信方式需考虑响应时间、耦合度、可靠性等因素。'),

('Go GMP模型', 'Go语言的GMP模型是什么？', 'discussion', 'hard', 'backend', 'go,调度,goroutine', 'G(Goroutine)、M(Machine)、P(Processor)组成的三层调度模型。', 'P是逻辑处理器，管理G在M上的运行，实际由M执行G。');

-- 插入会员测试数据
INSERT INTO members (user_id, level, member_since, expire_time, status, total_amount, description) VALUES
(1, 1, NOW(), NOW() + INTERVAL '1 year', 1, 299.00, 'Pro会员'),
(3, 2, NOW() - INTERVAL '30 days', NOW() + INTERVAL '1 year', 1, 999.00, 'Enterprise会员');

-- 插入面试记录测试数据
INSERT INTO interviews (user_id, interview_type, position, duration, status, total_score, question_count, answered_count, summary, feedback) VALUES
(1, 'text', 'frontend', 1800, 'completed', 85, 5, 5, '面试表现良好，对Vue和React都有了解', '技术基础扎实，沟通表达能力不错');
